package es.icarto.gvsig.viasobras.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
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

    private FLyrVect layer;

    public FormActuacionesAlta(FLyrVect layer) {
	this.layer = layer;
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
	this.setFocusCycleRoot(true);
    }

    private void initWidgets() {
	codigoCarretera = (JComboBox) form.getComboBox("codigo_carretera");
	codigoCarretera.removeAllItems();
	ORMLite ormlite = new ORMLite(getXMLPath());
	DomainValues dv = ormlite.getAppDomain().getDomainValuesForComponent(
		"codigo_carretera");
	if (dv != null) {
	    for (KeyValue kv : dv.getValues()) {
		codigoCarretera.addItem(kv);
	    }
	}
	pkInicial = (JTextField) form.getTextField("pk_inicial");
	pkFinal = (JTextField) form.getTextField("pk_final");
	codigoActuacion = (JTextField) form.getTextField("codigo_actuacion");
	tipo = (JComboBox) form.getComboBox("tipo");
    }

    public WindowInfo getWindowInfo() {
	if (viewInfo == null) {
	    viewInfo = new WindowInfo(WindowInfo.MODALDIALOG
		    | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	    viewInfo.setTitle("Vías y Obras: alta actuación");
	    viewInfo.setHeight(250);
	    viewInfo.setWidth(560);
	}
	return viewInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

    private final class SaveAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    if (isPKInicialOK()) {
		showWarningPanel("Aviso: PK inicial",
			"El campo PK inicial debe contener un número válido, ej: 2,5");
		return;
	    }
	    if (isPKFinalOK()) {
		showWarningPanel("Aviso: PK final",
			"El campo PK final debe contener un número válido, ej: 3,2");
		return;
	    }
	    if (isCodigoActuacionVoid()) {
		showWarningPanel("Aviso: código actuación",
			"El código de actuación no puede estar vacío.");
		return;
	    }
	    if (isCodigoActuacionAlreadyInDB()) {
		showWarningPanel("Aviso: campos únicos",
			"El código de actuación introducido ya existe.");
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
			+ "(codigo_carretera, pk_inicial, pk_final, codigo_actuacion, tipo) "
			+ "VALUES (?, ?, ?, ?, ?)";
		PreparedStatement stInsert = c.prepareStatement(sqlInsert);
		String codigoCarreteraValue = ((KeyValue) codigoCarretera
			.getSelectedItem()).getKey();
		stInsert.setString(1, codigoCarreteraValue);
		stInsert.setDouble(2, pkInicialValue);
		stInsert.setDouble(3, pkFinalValue);
		stInsert.setString(4, codigoActuacion.getText());
		stInsert.setString(5, (String) tipo.getSelectedItem()
			.toString());
		stInsert.execute();
		c.commit();
		c.close();
		showWarningPanel("Actuación guardada", "La actuación "
			+ codigoActuacion.getText()
			+ " ha sido guardada correctamente");
		layer.reload();
	    } catch (SQLException e1) {
		e1.printStackTrace();
		NotificationManager.addError(e1);
		try {
		    c.close();
		} catch (SQLException e2) {
		    e2.printStackTrace();
		}
	    } catch (ReloadLayerException e3) {
		e3.printStackTrace();
		NotificationManager.addError(e3);
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

    private boolean isPKFinalOK() {
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

    private boolean isPKInicialOK() {
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
		.getClassLoader().getResource("actuaciones-metadata.xml")
		.getPath();
    }

}
