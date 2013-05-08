package es.icarto.gvsig.viasobras.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import au.com.bytecode.opencsv.CSVReader;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.icarto.gvsig.viasobras.domain.catalog.Catalog;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.DBFacade;
import es.icarto.gvsig.viasobras.forms.utils.CatalogUpdater;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormImportAccidents extends JPanel implements IWindow {

    private WindowInfo windowInfo;
    private FormPanel form;
    private JButton importToDBButton;
    private JButton loadFileButton;
    private JTextArea areaMessages;
    private String accidentesFile;
    private int accidentesToImport;
    private int accidentesTotal;

    private String ID_ACCIDENTE = "ID_ACCIDENTE";
    private String FECHA_HORA = "FECHA_HORA_ACCI";
    private String CARRETERA = "CARRETERA";
    private String KM = "KM";
    private String HM = "HM";
    private String SENTIDO = "SE";
    private String LUMINOSIDAD = "LUMINOSIDAD";
    private String SUPERFICIE = "SUPERFICIE";
    private String VISIBILIDAD = "VISIBILIDAD_RESTRINGIDA_POR";
    private String FACTORES_ATMOSFERICOS = "FACTORES_ATMOSFERICOS";
    private String MEDIANA = "MEDIANA_ENTRE_CALZADAS";
    private String BARRERA = "BARRERA_SEGURIDAD";
    private String PANELES = "PANELES_DIRECCIONALES";
    private String ARISTA = "HITOS_ARISTA";
    private String CAPTA_FAROS = "CAPTA_FAROS";
    private String PRIORIDAD = "PRIORIDAD_REGULADA_POR";
    private String CIRCULACION = "CIRCULACION";
    private String CIRCULACION_ESPECIAL = "CIRCULACION_MEDIDAS_ESPECIALES";
    private String INTERSECCION = "INTERSECCION_CON";
    private String INTERSECCION_TIPO = "TIPO_INTERSECCION";
    private String INTERSECCION_ACONDICIONAMIENTO = "ACONDICIONAMIENTO_INTERSECCION";
    private String INTERSECCION_FUERA = "FUERA_INTERSECCION";
    private String TIPO_ACCIDENTE = "TIPO_ACCIDENTE";
    private String MUERTOS = "M";
    private String HERIDOS_GRAVES = "HG";
    private String HERIDOS_LEVES = "HL";
    private String VEHICULOS_IMPLICADOS = "T_VEHI";
    private String POBLACION = "Población";

    public FormImportAccidents() {
	initPanel();
    }

    private void initPanel() {
	form = new FormPanel("importar-accidentes-ui.xml");
	this.add(form);
	initWidgets();
	this.setFocusCycleRoot(true);
    }

    private void initWidgets() {
	importToDBButton = (JButton) AbeilleParser
		.getButtonsFromContainer(form).get("import_file");
	loadFileButton = (JButton) AbeilleParser.getButtonsFromContainer(form)
		.get("load_file");
	areaMessages = (JTextArea) form.getTextComponent("area_messages");
	areaMessages.append(PluginServices.getText(this, "select_file") + "\n");
	areaMessages.setEditable(false);
	importToDBButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		areaMessages.append(PluginServices.getText(this,
			"accidentes_processing") + "\n");
		importToDatabase();
		areaMessages.append("\n"
			+ PluginServices.getText(this, "accidentes_done") + " "
			+ accidentesToImport + "/" + accidentesTotal + "\n");
		importToDBButton.setEnabled(false);
	    }
	});
	loadFileButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		loadFile();
	    }
	});
    }

    private void loadFile() {
	JFileChooser fileChooser = new JFileChooser();
	int action = fileChooser.showOpenDialog(areaMessages);
	if (action == JFileChooser.APPROVE_OPTION) {
	    importToDBButton.setEnabled(true);
	    accidentesFile = fileChooser.getSelectedFile().getAbsolutePath();
	    areaMessages.append("\n"
		    + PluginServices.getText(this,
			    "accidentes_file_loaded") + " " + accidentesFile + "\n");
	}
    }

    public void importToDatabase() {
	String sql = "INSERT INTO inventario.accidentes(" +
		"codigo_carretera, " +
		"codigo_municipio, " +
		"tramo, " +
		"pk," +
		"fecha," +
		"valor," +
		"km,"+
		"hm," +
		"poblacion," +
		"sentido," +
		"luminosidad," +
		"superficie," +
		"visibilidad_restringida_por," +
		"factores_atmosfericos," +
		"mediana," +
		"barrera_seguridad," +
		"paneles_direccionales," +
		"hitos_arista," +
		"capta_faros," +
		"prioridad_regulada_por," +
		"circulacion," +
		"circulacion_medidas_especiales," +
		"interseccion_con," +
		"tipo_interseccion," +
		"acondicionamiento_interseccion," +
		"fuera_interseccion," +
		"tipo_accidente," +
		"muertos," +
		"heridos_graves," +
		"heridos_leves," +
		"vehiculos_implicados," +
		"id_accidente) " +
		"VALUES (?, ?, ?, ?, ?, " +
		"?, ?, ?, ?, ?, " +
		"?, ?, ?, ?, ?, " +
		"?, ?, ?, ?, ?, " +
		"?, ?, ?, ?, ?, " +
		"?, ?, ?, ?, ?, " +
		"?, ?)";

	Connection c;
	PreparedStatement st_insert;
	CSVReader csvReader;
	String[] row = null;

	try {
	    initDomainMapper();
	    c = DBFacade.getConnection();
	    c.setAutoCommit(false);
	    List<String> accidentes = getIDsAccidente();
	    Set<String> colsToRead = getColumnsToRead();
	    st_insert = c.prepareStatement(sql);
	    csvReader = new CSVReader(new InputStreamReader(
		    new FileInputStream(accidentesFile)), '\t');
	    String[] header = csvReader.readNext(); // header
	    HashMap<String, Integer> cols = new HashMap<String, Integer>();
	    for (int i = 0; i < header.length; i++) {
		if (colsToRead.contains(header[i])) {
		    cols.put(header[i], i);
		    colsToRead.remove(header[i]);
		}
	    }
	    areaMessages.append(PluginServices.getText(this,
		    "accidentes_processing_accidentes") + "\n \n");
	    accidentesToImport = 0;
	    accidentesTotal = 0;
	    if (colsToRead.size() > 0) {
		for (String col : colsToRead) {
		    areaMessages.append(col
			    + " - "
			    + PluginServices.getText(this,
				    "accidentes_col_missing") + "\n");
		}
		c.rollback();
		c.close();
		csvReader.close();
		return;
	    }
	    while((row = csvReader.readNext()) != null) {
		accidentesTotal++;
		String codigoCarretera = row[cols.get(CARRETERA)]
			.substring(row[cols.get(CARRETERA)].length() - 4);
		Double pk;
		try {
		    pk = Double.parseDouble(row[cols.get(KM)] + "."
			    + row[cols.get(HM)]);
		} catch (NumberFormatException e) {
		    pk = null;
		}
		if (validateCarretera(row[cols.get(ID_ACCIDENTE)], codigoCarretera)
			&& validatePK(row[cols.get(ID_ACCIDENTE)], pk)
			&& validateID(row[cols.get(ID_ACCIDENTE)], accidentes)) {
		    st_insert.setString(1, codigoCarretera);// codigo_carretera
		    st_insert.setNull(2, java.sql.Types.VARCHAR);
		    st_insert.setNull(3, java.sql.Types.VARCHAR);
		    st_insert.setDouble(4, pk);
		    setDateNoException(5, row[cols.get(FECHA_HORA)], st_insert); // fecha
		    st_insert.setString(6, row[cols.get(TIPO_ACCIDENTE)]);
		    setKMNoException(7, row[cols.get(KM)], st_insert);
		    setHMNoException(8, row[cols.get(HM)], st_insert);
		    st_insert.setString(9, row[cols.get(POBLACION)]); // poblacion
		    st_insert.setString(10, row[cols.get(SENTIDO)]);
		    st_insert.setString(11, row[cols.get(LUMINOSIDAD)]);
		    st_insert.setString(12, row[cols.get(SUPERFICIE)]);
		    st_insert.setString(13, row[cols.get(VISIBILIDAD)]); // visibilidad
		    st_insert.setString(14,
			    row[cols.get(FACTORES_ATMOSFERICOS)]);
		    st_insert.setString(15, row[cols.get(MEDIANA)]);
		    st_insert.setString(16, row[cols.get(BARRERA)]);
		    st_insert.setString(17, row[cols.get(PANELES)]); // paneles_direccionales
		    st_insert.setString(18, row[cols.get(ARISTA)]);
		    st_insert.setString(19, row[cols.get(CAPTA_FAROS)]);
		    st_insert.setString(20, row[cols.get(PRIORIDAD)]);
		    st_insert.setString(21, row[cols.get(CIRCULACION)]); // circulacion
		    st_insert
		    .setString(22, row[cols.get(CIRCULACION_ESPECIAL)]);
		    st_insert.setString(23, row[cols.get(INTERSECCION)]);
		    st_insert.setString(24, row[cols.get(INTERSECCION_TIPO)]);
		    st_insert.setString(25,
			    row[cols.get(INTERSECCION_ACONDICIONAMIENTO)]); // interseccion_acondicionamiento
		    st_insert.setString(26, row[cols.get(INTERSECCION_FUERA)]);
		    st_insert.setString(27, row[cols.get(TIPO_ACCIDENTE)]);
		    setMuertosNoException(28, row[cols.get(MUERTOS)], st_insert);
		    setHeridosGravesNoException(29,
			    row[cols.get(HERIDOS_GRAVES)], st_insert); // heridos_graves
		    setHeridosLevesNoException(30,
			    row[cols.get(HERIDOS_LEVES)], st_insert);
		    setVehiculosImplicadosNoException(31,
			    row[cols.get(VEHICULOS_IMPLICADOS)], st_insert);
		    st_insert.setString(32, row[cols.get(ID_ACCIDENTE)]);
		    st_insert.addBatch();
		    accidentesToImport++;
		}
	    }
	    st_insert.executeBatch();
	    st_insert.close();
	    String update_municipio = "UPDATE inventario.accidentes a "
		    + " SET codigo_municipio = " + " (SELECT codigo_municipio "
		    + " FROM inventario.carretera_municipio b "
		    + " WHERE b.codigo_carretera = a.codigo_carretera "
		    + " AND b.pk_inicial_tramo <= a.pk "
		    + " AND b.pk_final_tramo >= a.pk)"
		    + " WHERE codigo_municipio IS NULL";
	    String update_tramo = "UPDATE inventario.accidentes a "
		    + " SET tramo = " + " (SELECT orden_tramo "
		    + " FROM inventario.carretera_municipio b "
		    + " WHERE b.codigo_carretera = a.codigo_carretera "
		    + " AND b.pk_inicial_tramo <= a.pk "
		    + " AND b.pk_final_tramo >= a.pk)"
		    + " WHERE tramo IS NULL";
	    String update_pk = "UPDATE inventario.accidentes a "
		    + " SET pk = ("
		    + " SELECT max(pk_final_tramo) "
		    + " FROM inventario.carretera_municipio c "
		    + " WHERE a.codigo_carretera = c.codigo_carretera"
		    + ") WHERE codigo_municipio IS NULL OR tramo IS NULL"
		    + " RETURNING a.id_accidente";
	    String delete_nulls = "DELETE FROM inventario.accidentes "
		    + " WHERE codigo_municipio IS NULL OR tramo IS NULL"
		    + " RETURNING id_accidente";
	    PreparedStatement st_update_pk = c.prepareStatement(update_pk);
	    PreparedStatement st_update_municipio = c
		    .prepareStatement(update_municipio);
	    PreparedStatement st_update_tramo = c
		    .prepareStatement(update_tramo);
	    PreparedStatement st_delete_nulls = c
		    .prepareStatement(delete_nulls);
	    st_update_municipio.execute();
	    st_update_tramo.execute();
	    ResultSet rsPK = st_update_pk.executeQuery();
	    // accidentes with codigo_municipio IS NULL or tramo IS NULL
	    // will be updated to match last PK of carretera
	    st_update_municipio.execute();
	    st_update_tramo.execute();
	    while (rsPK.next()) {
		areaMessages.append(rsPK.getString(1)
			+ " "
			+ PluginServices.getText(this,
				"accidentes_adjust_tramo") + "\n");
	    }
	    // those which still are NULL for unknown reasons are deleted
	    ResultSet rsNulls = st_delete_nulls.executeQuery();
	    while (rsNulls.next()) {
		accidentesToImport--;
		areaMessages.append(rsNulls.getString(1)
			+ " "
			+ PluginServices.getText(this,
				"accidentes_delete_nulls") + "\n");
	    }
	    c.commit();
	    c.close();
	    csvReader.close();
	    CatalogUpdater.update();
	} catch (FileNotFoundException e) {
	    areaMessages.setText("\n"
		    + PluginServices.getText(this,
			    "accidentes_fail_file_not_found") + "\n");
	    e.printStackTrace();
	} catch (IOException e) {
	    areaMessages.setText("\n"
		    + PluginServices.getText(this,
			    "accidentes_fail_file_not_read") + "\n");
	    e.printStackTrace();
	} catch (SQLException e) {
	    areaMessages.setText("\n"
		    + PluginServices.getText(this,
			    "accidentes_fail_file_not_imported") + "\n");
	    e.printStackTrace();
	    e.getNextException().printStackTrace();
	}
    }

    private Set<String> getColumnsToRead() {
	Set<String> columns = new HashSet<String>();
	columns.add(ID_ACCIDENTE);
	columns.add(FECHA_HORA);
	columns.add(CARRETERA);
	columns.add(KM);
	columns.add(HM);
	columns.add(SENTIDO);
	columns.add(LUMINOSIDAD);
	columns.add(SUPERFICIE);
	columns.add(VISIBILIDAD);
	columns.add(FACTORES_ATMOSFERICOS);
	columns.add(MEDIANA);
	columns.add(BARRERA);
	columns.add(PANELES);
	columns.add(ARISTA);
	columns.add(CAPTA_FAROS);
	columns.add(PRIORIDAD);
	columns.add(CIRCULACION);
	columns.add(CIRCULACION_ESPECIAL);
	columns.add(INTERSECCION);
	columns.add(INTERSECCION_TIPO);
	columns.add(INTERSECCION_ACONDICIONAMIENTO);
	columns.add(INTERSECCION_FUERA);
	columns.add(TIPO_ACCIDENTE);
	columns.add(MUERTOS);
	columns.add(HERIDOS_GRAVES);
	columns.add(HERIDOS_LEVES);
	columns.add(VEHICULOS_IMPLICADOS);
	columns.add(POBLACION);
	return columns;
    }

    private void setHMNoException(int position, String value,
	    PreparedStatement st_insert) throws SQLException {
	try {
	    int hm = Integer.parseInt(value);
	    st_insert.setInt(position, hm);
	} catch (NumberFormatException e) {
	    st_insert.setNull(position, java.sql.Types.INTEGER);
	}
    }

    private void setKMNoException(int position, String value,
	    PreparedStatement st_insert) throws SQLException {
	try {
	    int km = Integer.parseInt(value);
	    st_insert.setInt(position, km);
	} catch (NumberFormatException e) {
	    st_insert.setNull(position, java.sql.Types.INTEGER);
	}
    }

    private boolean validateID(String idAccidente, List<String> accidentes) {
	if ((accidentes != null) && !accidentes.contains(idAccidente)) {
	    return true;
	}
	areaMessages.append(idAccidente + " "
		+ PluginServices
		.getText(this, "error_id_already_in_db") + "\n");
	return false;
    }

    private List<String> getIDsAccidente() {
	Connection c;
	try {
	    c = DBFacade.getConnection();
	    c.setAutoCommit(false);
	    String sql = "SELECT id_accidente FROM inventario.accidentes";
	    PreparedStatement st_select = c.prepareStatement(sql);
	    st_select.execute();
	    c.commit();
	    ResultSet rs = st_select.executeQuery();
	    List<String> accidentes = new ArrayList<String>();
	    while(rs.next()) {
		accidentes.add(rs.getString(1));
	    }
	    return accidentes;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private boolean validatePK(String idAccidente, Double pk) {
	if (pk != null) {
	    return true;
	}
	areaMessages.append(idAccidente + " "
		+ PluginServices.getText(this, "error_pk_not_found")
		+ "\n");
	return false;
    }

    private boolean validateCarretera(String idAccidente, String codigoCarretera)
	    throws SQLException {
	if (Catalog.getCarreteras().contains(codigoCarretera)) {
	    return true;
	}
	areaMessages.append(idAccidente + " "
		+ PluginServices.getText(this, "error_carretera_not_found")
		+ "\n");
	return false;
    }

    private void setVehiculosImplicadosNoException(int position, String value,
	    PreparedStatement st_insert) throws SQLException {
	try {
	    int vehiculos_implicados = Integer.parseInt(value);
	    st_insert.setInt(position, vehiculos_implicados);
	} catch (NumberFormatException e) {
	    st_insert.setNull(position, java.sql.Types.INTEGER);
	}
    }

    private void setHeridosLevesNoException(int position, String value,
	    PreparedStatement st_insert) throws SQLException {
	try {
	    int heridos_leves = Integer.parseInt(value);
	    st_insert.setInt(position, heridos_leves);
	} catch (NumberFormatException e) {
	    st_insert.setNull(position, java.sql.Types.INTEGER);
	}
    }

    private void setHeridosGravesNoException(int position, String value,
	    PreparedStatement st_insert)
		    throws SQLException {
	try {
	    int heridos_graves = Integer.parseInt(value);
	    st_insert.setInt(position, heridos_graves);
	} catch (NumberFormatException e) {
	    st_insert.setNull(position, java.sql.Types.INTEGER);
	}
    }

    private void setMuertosNoException(int position, String value,
	    PreparedStatement st_insert)
		    throws SQLException {
	try{
	    int muertos = Integer.parseInt(value);
	    st_insert.setInt(position, muertos);// muertos
	} catch (NumberFormatException e) {
	    st_insert.setNull(position, java.sql.Types.INTEGER);
	}
    }

    private void setDateNoException(int position, String value,
	    PreparedStatement st_insert)
		    throws SQLException {
	try {
	    DateFormat formatter = new SimpleDateFormat(
		    "dd/MM/yyyy HH:mm");
	    java.sql.Date fecha = new java.sql.Date(formatter.parse(value)
		    .getTime());
	    st_insert.setDate(position, fecha);
	} catch (ParseException e) {
	    // set date to 1970-01-01
	    st_insert.setDate(position, new java.sql.Date(0));
	}
    }

    private void initDomainMapper() {
	try {
	    DBSession dbs = DBSession.getCurrentSession();
	    Properties p = new Properties();
	    p.setProperty(DBFacade.URL, dbs.getJavaConnection().getMetaData()
		    .getURL());
	    p.setProperty(DBFacade.USERNAME, dbs.getUserName());
	    p.setProperty(DBFacade.PASSWORD, dbs.getPassword());
	    // Create the connection ourselves, as at this moment, gvSIG has an
	    // old driver (lower than jdbc4) which doesn't implement the methods
	    // we need. So, take care that the jar in
	    // lib/postgresql-8.4-jdbc4.jar is used instead of gvSIG ones.
	    Class.forName("org.postgresql.Driver");
	    Connection c = DriverManager.getConnection(
		    p.getProperty(DBFacade.URL),
		    p.getProperty(DBFacade.USERNAME),
		    p.getProperty(DBFacade.PASSWORD));
	    DBFacade.setConnection(c, p);
	} catch (Exception e) {
	    NotificationManager.addError(e);
	}
    }

    public WindowInfo getWindowInfo() {
	if (windowInfo == null) {
	    windowInfo = new WindowInfo(WindowInfo.MODALDIALOG
		    | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	    windowInfo.setTitle("Vías y Obras: importar accidentes");
	    windowInfo.setHeight(475);
	    windowInfo.setWidth(650);
	}
	return windowInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

}
