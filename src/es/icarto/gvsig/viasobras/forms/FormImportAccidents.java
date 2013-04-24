package es.icarto.gvsig.viasobras.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import au.com.bytecode.opencsv.CSVReader;

import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.DBFacade;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormImportAccidents extends JPanel implements IWindow {

    private WindowInfo windowInfo;
    private JButton executeButton;
    private FormPanel form;
    private JComboBox codigoCarretera;

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
	executeButton = (JButton) AbeilleParser.getButtonsFromContainer(form)
		.get("ejecutar");
	executeButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		execute();
	    }
	});
    }

    public void execute() {
	String sql = "INSERT INTO inventario.accidentes(" +
		"codigo_carretera, " +
		"codigo_municipio, " +
		"tramo, " +
		"pk," +
		"fecha," +
		"valor," +
		"poblacion," +
		"se," +
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
		"tipo_otros," +
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

	Connection connection;
	PreparedStatement ps;
	String csvFilename = "/tmp/accidentes.csv";
	CSVReader csvReader;
	String[] row = null;

	try {
	    connection = DBFacade.getConnection();
	    ps = connection.prepareStatement(sql);
	    csvReader = new CSVReader(new FileReader(csvFilename));
	    csvReader.readNext(); // header
	    while((row = csvReader.readNext()) != null) {
		ps.setString(1, row[0]);// codigo_carretera
		ps.setString(2, row[1]);
		ps.setString(3, row[2]);
		ps.setDouble(4, Double.parseDouble(row[3].replaceAll(",", ".")));
		try {
		    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		    java.sql.Date fecha = new java.sql.Date(formatter.parse(
			    row[4]).getTime());
		    ps.setDate(5, fecha);
		} catch (ParseException e) {
		    ps.setNull(5, java.sql.Types.DATE);
		}
		ps.setString(6, row[5]); // valor
		ps.setString(7, row[6]);
		ps.setString(8, row[7]);
		ps.setString(9, row[8]);
		ps.setString(10, row[9]);
		ps.setString(11, row[10]);// visibilidad_restringida_por
		ps.setString(12, row[11]);
		ps.setString(13, row[12]);
		ps.setString(14, row[13]);
		ps.setString(15, row[14]);
		ps.setString(16, row[15]);// hitos_arista
		ps.setString(17, row[16]);
		ps.setString(18, row[17]);
		ps.setString(19, row[18]);
		ps.setString(20, row[19]);
		ps.setString(21, row[20]);// interseccion_con
		ps.setString(22, row[21]);
		ps.setString(23, row[22]);
		ps.setString(24, row[23]);
		ps.setString(25, row[24]);
		try{
		    int muertos = Integer.parseInt(row[25]);
		    ps.setInt(26, muertos);// muertos
		} catch (NumberFormatException e) {
		    ps.setNull(26, java.sql.Types.INTEGER);
		}
		try {
		    int heridos_graves = Integer.parseInt(row[26]);
		    ps.setInt(27, heridos_graves);
		} catch (NumberFormatException e) {
		    ps.setNull(27, java.sql.Types.INTEGER);
		}
		try {
		    int heridos_leves = Integer.parseInt(row[27]);
		    ps.setInt(28, heridos_leves);
		} catch (NumberFormatException e) {
		    ps.setNull(28, java.sql.Types.INTEGER);
		}
		try {
		    int vehiculos_implicados = Integer.parseInt(row[28]);
		    ps.setInt(29, vehiculos_implicados);
		} catch (NumberFormatException e) {
		    ps.setNull(29, java.sql.Types.INTEGER);
		}
		ps.addBatch();
	    }
	    ps.executeBatch();
	    ps.close();
	    connection.close();
	    csvReader.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (SQLException e) {
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
	    windowInfo.setTitle("Vías y Obras: recalcular PKs");
	    windowInfo.setHeight(90);
	    windowInfo.setWidth(300);
	}
	return windowInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

}
