package es.icarto.gvsig.viasobras.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domain.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormActuacionesAlta extends JPanel implements IWindow {

    private WindowInfo viewInfo = null;
    private FormPanel form;
    private JButton save;

    private JComboBox codigoCarretera;
    private JTextField pkInicial;
    private JTextField pkFinal;
    private JTextField codigoActuacion;
    private JComboBox tipo;
    private JTextField descripcion;
    private JTextField tituloProyecto;
    private JTextField importe;
    private JTextField fecha;
    private JTextField contratista;
    private JTextArea observaciones;

    public FormActuacionesAlta() {
	initPanel();
    }

    private void initPanel() {
	form = new FormPanel("actuaciones-alta-ui.xml");
	JScrollPane scrollPane = new JScrollPane(form);
	this.add(scrollPane);
	JPanel panel = new JPanel();
	save = new JButton("Guardar");
	save.addActionListener(new SaveAction());
	panel.add(save);
	this.add(panel);
	initWidgets();
    }

    private void initWidgets() {
	codigoCarretera = (JComboBox) form.getComboBox("codigo_carretera");
	codigoCarretera.removeAllItems();
	DomainValues dv = ORMLite.getAplicationDomainObject(getXMLPath())
		.getDomainValuesForComponent("codigo_carretera");
	if (dv != null) {
	    for (KeyValue kv : dv.getValues()) {
		codigoCarretera.addItem(kv);
	    }
	}
	pkInicial = (JTextField) form.getTextField("pk_inicial");
	pkFinal = (JTextField) form.getTextField("pk_final");
	codigoActuacion = (JTextField) form.getTextField("codigo_actuacion");
	tipo = (JComboBox) form.getComboBox("tipo");
	descripcion = (JTextField) form.getTextField("descripcion");
	tituloProyecto = (JTextField) form.getTextField("titulo_proyecto");
	importe = (JTextField) form.getComponentByName("importe");
	fecha = (JTextField) form.getTextField("fecha");
	contratista = (JTextField) form.getTextField("contratista");
	observaciones = (JTextArea) form.getComponentByName("observaciones");
    }

    public WindowInfo getWindowInfo() {
	if (viewInfo == null) {
	    viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG
		    | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	    viewInfo.setTitle("Vías Obras: alta actuación");
	    viewInfo.setWidth(560);
	    viewInfo.setHeight(430);
	}
	return viewInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

    private final class SaveAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    if (isPKInicialVoid() || isPKFinalVoid() || isCodigoActuacionVoid()) {
		showWarningPanel("Aviso: campos obligatorios",
			"Debe rellenar los campos Código actuacion, PK inicial y PK final");
		return;
	    } else if (isCodigoActuacionAlreadyInDB()) {
		showWarningPanel("Aviso: campos únicos",
			"El código de actuación introducido ya existe.");
		return;
	    }
	    DBSession dbs = DBSession.getCurrentSession();
	    Connection c = dbs.getJavaConnection();
	    try {
		c.setAutoCommit(false);
		String sqlInsert = "INSERT INTO inventario.actuaciones "
			+ "(codigo_carretera, pk_inicial, pk_final, codigo_actuacion, tipo, descripcion, titulo_proyecto, importe, fecha, contratista, observaciones) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stInsert = c.prepareStatement(sqlInsert);
		String codigoCarreteraValue = ((KeyValue) codigoCarretera
			.getSelectedItem()).getKey();
		stInsert.setString(1, codigoCarreteraValue);
		stInsert.setDouble(2, Double.parseDouble(pkInicial.getText()));
		stInsert.setDouble(3, Double.parseDouble(pkFinal.getText()));
		stInsert.setString(4, codigoActuacion.getText());
		stInsert.setString(5, (String) tipo.getSelectedItem()
			.toString());
		stInsert.setString(6, descripcion.getText());
		stInsert.setString(7, tituloProyecto.getText());
		try {
		    double importeValue = Double.parseDouble(importe.getText());
		    stInsert.setDouble(8, importeValue);
		} catch (NumberFormatException nfe) {
		    stInsert.setNull(8, java.sql.Types.DOUBLE);
		}
		try {
		    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		    Date date = (Date) formatter.parse(fecha.getText());
		    stInsert.setDate(9, date);
		} catch (ParseException e1) {
		    stInsert.setNull(9, java.sql.Types.DATE);
		}
		stInsert.setString(10, contratista.getText());
		stInsert.setString(11, observaciones.getText());
		stInsert.execute();
		c.commit();
		showWarningPanel("Actuación guardada", "La actuación "
			+ codigoActuacion.getText()
			+ " ha sido guardada correctamente");
	    } catch (SQLException e1) {
		e1.printStackTrace();
		NotificationManager.addError(e1);
	    }
	}
    }

    private void showWarningPanel(String title, String message) {
	JOptionPane.showMessageDialog(this, message,
		PluginServices.getText(this, title),
		JOptionPane.WARNING_MESSAGE);
    }

    private boolean isCodigoActuacionVoid() {
	if (codigoActuacion.getText().equals("")) {
	    return true;
	}
	return false;
    }

    private boolean isPKFinalVoid() {
	if (pkFinal.getText().equals("")) {
	    return true;
	}
	try {
	    Double.parseDouble(pkFinal.getText());
	    return false;
	} catch (NumberFormatException nfe) {
	    return true;
	}
    }

    private boolean isPKInicialVoid() {
	if (pkInicial.getText().equals("")) {
	    return true;
	}
	try {
	    Double.parseDouble(pkInicial.getText());
	    return false;
	} catch (NumberFormatException nfe) {
	    return true;
	}
    }

    private boolean isCodigoActuacionAlreadyInDB() {
	DBSession dbs = DBSession.getCurrentSession();
	Connection c = dbs.getJavaConnection();
	try {
	    c.setAutoCommit(false);
	    String sqlSelect = "SELECT codigo_actuacion "
		    + " FROM inventario.actuaciones "
		    + " WHERE codigo_actuacion = ?;";
	    PreparedStatement stSelect = c.prepareStatement(sqlSelect);
	    stSelect.setString(1, codigoActuacion.getText());
	    stSelect.execute();
	    c.commit();
	    ResultSet rs = stSelect.executeQuery();
	    rs.last();
	    int numberOfRows = rs.getRow();
	    if (numberOfRows > 0) {
		return true;
	    }
	    return false;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return true;
	}
    }

    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.viasobras")
		.getClassLoader().getResource("viasobras-metadata.xml")
		.getPath();
    }

}
