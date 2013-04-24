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
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import au.com.bytecode.opencsv.CSVReader;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.DBFacade;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormImportAccidents extends JPanel implements IWindow {

    private WindowInfo windowInfo;
    private FormPanel form;
    private JButton importToDBButton;
    private JButton loadFileButton;
    private JLabel message;
    private String accidentesFile;

    public FormImportAccidents() {
	initPanel();
	initDomainMapper();
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
	message = form.getLabel("messages_panel");
	importToDBButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		message.setText(PluginServices.getText(this,
			"accidentes_processing"));
		importToDatabase();
		message.setText(PluginServices.getText(this, "accidentes_done"));
		importToDBButton.setEnabled(false);
	    }
	});
	loadFileButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		loadFile();
		message.setText(PluginServices.getText(this,
			"accidentes_file_loaded") + " " + accidentesFile);
	    }
	});
    }

    private void loadFile() {
	JFileChooser fileChooser = new JFileChooser();
	int action = fileChooser.showOpenDialog(message);
	if (action == JFileChooser.APPROVE_OPTION) {
	    importToDBButton.setEnabled(true);
	    accidentesFile = fileChooser.getSelectedFile().getAbsolutePath();
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
		"vehiculos_implicados) " +
		"VALUES (?, ?, ?, ?, ?, " +
		"?, ?, ?, ?, ?, " +
		"?, ?, ?, ?, ?, " +
		"?, ?, ?, ?, ?, " +
		"?, ?, ?, ?, ?, " +
		"?, ?, ?, ?)";

	Connection c;
	PreparedStatement st_insert;
	CSVReader csvReader;
	String[] row = null;

	try {
	    c = DBFacade.getConnection();
	    c.setAutoCommit(false);
	    st_insert = c.prepareStatement(sql);
	    csvReader = new CSVReader(new InputStreamReader(
		    new FileInputStream(accidentesFile), "UTF8"));
	    csvReader.readNext(); // header
	    while((row = csvReader.readNext()) != null) {
		/*
		 *  0 ID accidente
		 *  1 fecha hora
		 *  2 provincia
		 *  3 carretera
		 *  4 denominacion
		 *  5 km
		 *  6 hm
		 *  7 se
		 *  8 titularidad via
		 *  9 luminosidad
		 * 10 superficie
		 * 11 visibilidad restringida
		 * 12 factores atmosfericos
		 * 13 mediana
		 * 14 barrera seguridad
		 * 15 paneles direccionales
		 * 16 hitos arista
		 * 17 capta faros
		 * 18 prioridad regulada
		 * 19 circulacion
		 * 20 circulacion medidas especiales
		 * 21 interseccion con
		 * 22 tipo interseccion
		 * 23 acondicionamiento interseccion
		 * 24 fuera interseccion
		 * 25 tipo accidente
		 * 26 m (muertos)
		 * 27 hg (heridos graves)
		 * 28 hl (heridos leves)
		 * 29 t_vehi (vehiculos implicados)
		 * 30 municipio
		 * 31 poblacion
		 */
		st_insert.setString(1, row[3].substring(row[3].length() - 4));// codigo_carretera
		st_insert.setString(2, ""); // UPDATE: got from
					    // carretera_municipio
		st_insert.setString(3, ""); // UPDATE: got from
					    // carretera_municipio
		st_insert.setDouble(4,
			Double.parseDouble(row[5] + "." + row[6]));
		try {
		    DateFormat formatter = new SimpleDateFormat(
			    "dd/MM/yyyy HH:mm");
		    java.sql.Date fecha = new java.sql.Date(formatter.parse(
			    row[1]).getTime());
		    st_insert.setDate(5, fecha);
		} catch (ParseException e) {
		    st_insert.setNull(5, java.sql.Types.DATE);
		}
		st_insert.setString(6, row[0]); // valor
		st_insert.setString(7, row[31]);
		st_insert.setString(8, row[7]);
		st_insert.setString(9, row[9]);
		st_insert.setString(10, row[10]);
		st_insert.setString(11, row[11]);// visibilidad_restringida_por
		st_insert.setString(12, row[12]);
		st_insert.setString(13, row[13]);
		st_insert.setString(14, row[14]);
		st_insert.setString(15, row[15]);
		st_insert.setString(16, row[16]);// hitos_arista
		st_insert.setString(17, row[17]);
		st_insert.setString(18, row[18]);
		st_insert.setString(19, row[19]);
		st_insert.setString(20, row[20]);
		st_insert.setString(21, row[21]);// interseccion_con
		st_insert.setString(22, row[22]);
		st_insert.setString(23, row[23]);
		st_insert.setString(24, row[24]);
		st_insert.setString(25, row[25]);
		try{
		    int muertos = Integer.parseInt(row[26]);
		    st_insert.setInt(26, muertos);// muertos
		} catch (NumberFormatException e) {
		    st_insert.setNull(26, java.sql.Types.INTEGER);
		}
		try {
		    int heridos_graves = Integer.parseInt(row[27]);
		    st_insert.setInt(27, heridos_graves);
		} catch (NumberFormatException e) {
		    st_insert.setNull(27, java.sql.Types.INTEGER);
		}
		try {
		    int heridos_leves = Integer.parseInt(row[28]);
		    st_insert.setInt(28, heridos_leves);
		} catch (NumberFormatException e) {
		    st_insert.setNull(28, java.sql.Types.INTEGER);
		}
		try {
		    int vehiculos_implicados = Integer.parseInt(row[29]);
		    st_insert.setInt(29, vehiculos_implicados);
		} catch (NumberFormatException e) {
		    st_insert.setNull(29, java.sql.Types.INTEGER);
		}
		st_insert.addBatch();
	    }
	    st_insert.executeBatch();
	    st_insert.close();
	    String update_municipio = "UPDATE inventario.accidentes a "
		    + " SET codigo_municipio = "
		    + " (SELECT codigo_municipio "
		    + " FROM inventario.carretera_municipio b "
		    + " WHERE b.codigo_carretera = a.codigo_carretera "
		    + " AND b.pk_inicial_tramo <= a.pk "
		    + " AND b.pk_final_tramo >= a.pk)";
	    String update_tramo = "UPDATE inventario.accidentes a "
		    + " SET tramo = "
		    + " (SELECT orden_tramo " +
		    " FROM inventario.carretera_municipio b " +
		    " WHERE b.codigo_carretera = a.codigo_carretera " +
		    " AND b.pk_inicial_tramo <= a.pk " +
		    " AND b.pk_final_tramo >= a.pk)";
	    PreparedStatement st_update_municipio = c
		    .prepareStatement(update_municipio);
	    PreparedStatement st_update_tramo = c
		    .prepareStatement(update_tramo);
	    st_update_municipio.execute();
	    st_update_tramo.execute();
	    c.commit();
	    c.close();
	    csvReader.close();
	} catch (FileNotFoundException e) {
	    message.setText(PluginServices.getText(this,
		    "accidentes_fail_file_not_found"));
	    e.printStackTrace();
	} catch (IOException e) {
	    message.setText(PluginServices.getText(this,
		    "accidentes_fail_file_not_read"));
	    e.printStackTrace();
	} catch (SQLException e) {
	    message.setText(PluginServices.getText(this,
		    "accidentes_fail_file_not_imported"));
	    e.printStackTrace();
	    e.getNextException().printStackTrace();
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
	    windowInfo.setHeight(90);
	    windowInfo.setWidth(420);
	}
	return windowInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

}
