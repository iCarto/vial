package es.icarto.gvsig.viasobras.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
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
import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;
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
	    viewInfo.setTitle("V�as Obras: alta actuaci�n");
	    viewInfo.setHeight(480);
	    viewInfo.setWidth(560);
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
			"Debe rellenar los campos C�digo actuacion, PK inicial y PK final");
		return;
	    } else if (isCodigoActuacionAlreadyInDB()) {
		showWarningPanel("Aviso: campos �nicos",
			"El c�digo de actuaci�n introducido ya existe.");
		return;
	    }
	    double pkInicialValue, pkFinalValue;
	    try {
		NumberFormat doubleFormat = DoubleFormatNT
			.getDisplayingFormat();
		pkInicialValue = (Double) doubleFormat.parse(
			pkInicial.getText()).doubleValue();
		pkFinalValue = (Double) doubleFormat.parse(pkFinal.getText())
			.doubleValue();
	    } catch (ParseException pe) {
		showWarningPanel("Aviso: PKs",
			"Revise los valores en los campos PKs.");
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
		stInsert.setDouble(2, pkInicialValue);
		stInsert.setDouble(3, pkFinalValue);
		stInsert.setString(4, codigoActuacion.getText());
		stInsert.setString(5, (String) tipo.getSelectedItem()
			.toString());
		stInsert.setString(6, descripcion.getText());
		stInsert.setString(7, tituloProyecto.getText());
		try {
		    NumberFormat doubleFormat = DoubleFormatNT
			    .getDisplayingFormat();
		    double importeValue = (Double) doubleFormat.parse(
			    importe.getText()).doubleValue();
		    stInsert.setDouble(8, importeValue);
		} catch (ParseException pe) {
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
		c.close();
		showWarningPanel("Actuaci�n guardada", "La actuaci�n "
			+ codigoActuacion.getText()
			+ " ha sido guardada correctamente");
	    } catch (SQLException e1) {
		e1.printStackTrace();
		NotificationManager.addError(e1);
		try {
		    c.close();
		} catch (SQLException e2) {
		    e2.printStackTrace();
		}
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
	    NumberFormat doubleFormat = DoubleFormatNT.getDisplayingFormat();
	    doubleFormat.parse(pkFinal.getText());
	    return false;
	} catch (ParseException e) {
	    e.printStackTrace();
	    return true;
	}
    }

    private boolean isPKInicialVoid() {
	if (pkInicial.getText().equals("")) {
	    return true;
	}
	try {
	    NumberFormat doubleFormat = DoubleFormatNT.getDisplayingFormat();
	    doubleFormat.parse(pkInicial.getText());
	    return false;
	} catch (ParseException e) {
	    e.printStackTrace();
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
	    c.close();
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