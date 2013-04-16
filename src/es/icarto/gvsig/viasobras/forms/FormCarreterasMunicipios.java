package es.icarto.gvsig.viasobras.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.IForm;
import es.icarto.gvsig.navtableforms.gui.tables.TableModelAlphanumeric;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormCarreterasMunicipios extends JPanel implements IForm, IWindow,
IWindowListener {

    private WindowInfo viewInfo;
    private FormPanel form;
    private TableModelAlphanumeric model;

    private JTextField carretera;
    private JComboBox concello;
    private JTextField ordenTramo;
    private JTextField pkInicial;
    private JTextField pkFinal;
    private JTextField longitud;
    private JTextArea observaciones;
    private JButton save;
    private ActionListener action;
    private AbstractForm parentForm;

    private String concelloValue;
    private String ordenTramoValue;

    private String carreteraCode;
    private long position;

    public FormCarreterasMunicipios(AbstractForm parentForm) {
	super();
	this.parentForm = parentForm;
	form = new FormPanel("carretera-municipio-ui.xml");
	initWidgets();
	action = new CreateAction();
	save.addActionListener(action);
    }

    /**
     * setModel & setCarreteraCode should be set before executing it
     */
    public void actionCreateRecord() {
	this.position = -1;
	save.removeActionListener(action);
	action = new CreateAction();
	save.addActionListener(action);
	fillWidgetsForCreatingRecord();
	this.add(form);
	this.setFocusCycleRoot(true);
	PluginServices.getMDIManager().addWindow(this);
    }

    /**
     * setModel should be set before executing it
     */
    public void actionUpdateRecord(long position) {
	this.position = position;
	save.removeActionListener(action);
	action = new SaveAction();
	save.addActionListener(action);
	fillWidgetsForUpdatingRecord(position);
	this.add(form);
	PluginServices.getMDIManager().addWindow(this);
    }

    /**
     * setModel should be set before executing it
     */
    public void actionDeleteRecord(long position) {
	try {
	    model.delete((int) position);
	    refreshParentForm();
	    reloadPKLayer();
	} catch (Exception e) {
	    NotificationManager.addError(e);
	}
    }

    public IEditableSource getSource() {
	return model.getSource();
    }

    public void setModel(TableModelAlphanumeric model) {
	this.model = model;
    }

    public void setCarretera(String carreteraCode) {
	this.carreteraCode = carreteraCode;
    }

    private void initWidgets() {
	HashMap<String, JComponent> widgets = AbeilleParser
		.getWidgetsFromContainer(form);
	carretera = (JTextField) widgets.get("codigo_carretera");
	concello = (JComboBox) widgets.get("codigo_municipio");
	fillConcellos();
	ordenTramo = (JTextField) widgets.get("orden_tramo");
	pkInicial = (JTextField) widgets.get("pk_inicial_tramo");
	pkFinal = (JTextField) widgets.get("pk_final_tramo");
	longitud = (JTextField) widgets.get("longitud_tramo");
	observaciones = (JTextArea) widgets.get("observaciones_tramo");

	carretera.setEnabled(false);

	save = (JButton) AbeilleParser.getButtonsFromContainer(form).get(
		"guardar");
    }

    private void fillWidgetsForCreatingRecord() {
	concelloValue = "";
	ordenTramoValue = "";

	carretera.setText(carreteraCode);
	fillConcellos();
	ordenTramo.setText("");
	pkInicial.setText("");
	pkFinal.setText("");
	longitud.setText("");
	observaciones.setText("");
    }

    private void fillWidgetsForUpdatingRecord(long position) {
	try {
	    // vars to track changes
	    concelloValue = model.read((int) position).get("codigo_municipio");
	    ordenTramoValue = model.read((int) position).get("orden_tramo");

	    // widgets
	    carretera.setText(model.read((int) position)
		    .get("codigo_carretera"));
	    fillConcellos();
	    setConcelloSelected(concelloValue);
	    ordenTramo.setText(ordenTramoValue);
	    pkInicial.setText(model.read((int) position)
		    .get("pk_inicial_tramo"));
	    pkFinal.setText(model.read((int) position).get("pk_final_tramo"));
	    longitud.setText(model.read((int) position).get(
		    "longitud_tramo"));
	    observaciones.setText(model.read((int) position).get(
		    "observaciones_tramo"));
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    carretera.setText("");
	    concello.removeAll();
	    fillConcellos();
	    ordenTramo.setText("");
	    pkInicial.setText("");
	    pkFinal.setText("");
	    longitud.setText("");
	    observaciones.setText("");
	}
    }

    private void setConcelloSelected(String concelloCode) {
	for(int i=0; i<concello.getItemCount(); i++) {
	    if (((KeyValue) concello.getItemAt(i)).getKey()
		    .equals(concelloCode)) {
		concello.setSelectedIndex(i);
	    }
	}
    }

    private void fillConcellos() {
	ORMLite ormlite = new ORMLite(getXMLPath());
	DomainValues dv = ormlite.getAppDomain().getDomainValuesForComponent(
		"codigo_municipio");
	concello.removeAllItems();
	for (KeyValue kv : dv.getValues()) {
	    concello.addItem(kv);
	}
	concello.setSelectedIndex(0);
    }

    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.viasobras")
		.getClassLoader()
		.getResource("carretera-municipio-metadata.xml")
		.getPath();
    }

    public WindowInfo getWindowInfo() {
	if (viewInfo == null) {
	    viewInfo = new WindowInfo(WindowInfo.MODALDIALOG
		    | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	    viewInfo.setTitle("Vías y Obras: carretera / municipio");
	    viewInfo.setHeight(340);
	    viewInfo.setWidth(350);
	}
	return viewInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

    private final class CreateAction implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
	    if (isOrdenTramoVoid()) {
		showWarningPanel("Aviso: tramo",
			"El campo tramo debe contener un valor válido, ej: A");
	    }
	    if (isPKInicialOK()) {
		showWarningPanel("Aviso: PK inicial",
			"El campo PK inicial debe contener un número válido, ej: 2,5");
		return;
	    }
	    if (isPKFinalOK()) {
		showWarningPanel("Aviso: PK final",
			"El campo PK final debe contener un número válido, ej: 3,2");
	    }
	    if (isConcelloTramoAlreadyInDB()) {
		showWarningPanel("Aviso: campos únicos",
			"El tramo introducido ya existe para el municipio seleccionado.");
		return;
	    }
	    HashMap<String, String> values = new HashMap<String, String>();
	    values.put("codigo_carretera", carretera.getText());
	    values.put("codigo_municipio",
		    ((KeyValue) concello.getSelectedItem()).getKey());
	    values.put("orden_tramo", ordenTramo.getText());
	    values.put("pk_inicial_tramo", pkInicial.getText());
	    values.put("pk_final_tramo", pkFinal.getText());
	    if (longitud.getText().equals("")) {
		longitud.setText("0");
		// will be autocalculated from PKs
	    }
	    values.put("longitud_tramo", longitud.getText());
	    values.put("observaciones_tramo", observaciones.getText());
	    try {
		model.create(values);
		fillWidgetsForCreatingRecord();
		refreshParentForm();
		reloadPKLayer();
	    } catch (Exception e) {
		NotificationManager.addError(e);
	    }
	}
    }

    private final class SaveAction implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
	    if (isOrdenTramoVoid()) {
		showWarningPanel("Aviso: tramo",
			"El campo tramo debe contener un valor válido, ej: A");
	    }
	    if (isPKInicialOK()) {
		showWarningPanel("Aviso: PK inicial",
			"El campo PK inicial debe contener un número válido, ej: 2,5");
		return;
	    }
	    if (isPKFinalOK()) {
		showWarningPanel("Aviso: PK final",
			"El campo PK final debe contener un número válido, ej: 3,2");
	    }
	    if (hasConcelloOrOrdenChanged()) {
		if (isConcelloTramoAlreadyInDB()) {
		    showWarningPanel("Aviso: campos únicos",
			    "El tramo introducido ya existe para el municipio seleccionado.");
		    return;
		}
	    }
	    ordenTramoValue = ordenTramo.getText();
	    concelloValue = ((KeyValue) concello.getSelectedItem()).getKey();
	    model.updateValue("codigo_carretera", carretera.getText());
	    model.updateValue("codigo_municipio", concelloValue);
	    model.updateValue("orden_tramo", ordenTramoValue);
	    model.updateValue("pk_inicial_tramo", pkInicial.getText());
	    model.updateValue("pk_final_tramo", pkFinal.getText());
	    if (longitud.getText().equals("")) {
		longitud.setText("0");
		// will be autocalculated from PKs
	    }
	    model.updateValue("longitud_tramo", longitud.getText());
	    model.updateValue("observaciones_tramo", observaciones.getText());
	    try {
		model.update((int) position);
		refreshParentForm();
		reloadPKLayer();
	    } catch (Exception e) {
		NotificationManager.addError(e);
	    }
	}

	private boolean hasConcelloOrOrdenChanged() {
	    if (!concelloValue.equals(((KeyValue) concello.getSelectedItem())
		    .getKey()) || !ordenTramoValue.equals(ordenTramo.getText())) {
		return true;
	    }
	    return false;
	}
    }

    private void reloadPKLayer() throws ReloadLayerException {
	TOCLayerManager toc = new TOCLayerManager();
	FLyrVect pkLayer = toc.getLayerByName("PKs");
	if (pkLayer != null) {
	    pkLayer.reload();
	}
    }

    private boolean isOrdenTramoVoid() {
	if (ordenTramo.getText().equals("")) {
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

    private boolean isConcelloTramoAlreadyInDB() {
	DBSession dbs = DBSession.getCurrentSession();
	Connection c = dbs.getJavaConnection();
	try {
	    c.setAutoCommit(false);
	    String sqlSelect = "SELECT codigo_carretera, codigo_municipio, orden_tramo "
		    + " FROM inventario.carretera_municipio "
		    + " WHERE codigo_carretera = ? AND codigo_municipio = ? AND orden_tramo = ?;";
	    PreparedStatement stSelect = c.prepareStatement(sqlSelect);
	    stSelect.setString(1, carretera.getText());
	    stSelect.setString(2,
		    ((KeyValue) concello.getSelectedItem()).getKey());
	    stSelect.setString(3, ordenTramo.getText());
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
	    try {
		c.close();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    } finally {
		return true;
	    }
	}
    }

    public void windowActivated() {
    }

    public void windowClosed() {
	refreshParentForm();
    }

    private void refreshParentForm() {
	// will force to refresh the values from layer
	try {
	    this.parentForm.reloadRecordset();
	    this.parentForm.setPosition(this.parentForm.getPosition());
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
    }

    private void showWarningPanel(String title, String message) {
	JOptionPane.showMessageDialog(this, message,
		PluginServices.getText(this, title),
		JOptionPane.WARNING_MESSAGE);
    }

}
