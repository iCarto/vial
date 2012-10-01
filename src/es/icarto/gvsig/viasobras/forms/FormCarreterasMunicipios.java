package es.icarto.gvsig.viasobras.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.IForm;
import es.icarto.gvsig.navtableforms.gui.tables.TableModelAlphanumeric;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domain.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;

public class FormCarreterasMunicipios extends JPanel implements IForm, IWindow,
IWindowListener {

    private WindowInfo viewInfo;
    private JScrollPane form;
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

    private String carreteraCode;
    private long position;

    public FormCarreterasMunicipios(AbstractForm parentForm) {
	super();
	this.parentForm = parentForm;
	FormPanel formBody = new FormPanel("carretera-municipio-ui.xml");
	form = new JScrollPane(formBody);
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
	    carretera.setText(model.read((int) position)
		    .get("codigo_carretera"));
	    fillConcellos();
	    setConcelloSelected(model.read((int) position).get(
		    "codigo_municipio"));
	    ordenTramo.setText(model.read((int) position).get("orden_tramo"));
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
	DomainValues dv = ORMLite.getAplicationDomainObject(getXMLPath())
		.getDomainValuesForComponent("codigo_municipio");
	concello.removeAllItems();
	for (KeyValue kv : dv.getValues()) {
	    concello.addItem(kv);
	}
	concello.setSelectedIndex(0);
    }

    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.viasobras")
		.getClassLoader().getResource("viasobras-metadata.xml")
		.getPath();
    }

    public WindowInfo getWindowInfo() {
	if (viewInfo == null) {
	    viewInfo = new WindowInfo(WindowInfo.MODALDIALOG
		    | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	    viewInfo.setTitle(PluginServices.getText(this,
		    "Carreteras / Ayuntamientos"));
	    viewInfo.setHeight(350);
	    viewInfo.setWidth(350);
	}
	return viewInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

    private final class CreateAction implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
	    HashMap<String, String> values = new HashMap<String, String>();
	    values.put("codigo_carretera", carretera.getText());
	    values.put("codigo_municipio",
		    ((KeyValue) concello.getSelectedItem()).getKey());
	    values.put("orden_tramo", ordenTramo.getText());
	    values.put("pk_inicial_tramo", pkInicial.getText());
	    values.put("pk_final_tramo", pkFinal.getText());
	    values.put("longitud_tramo", longitud.getText());
	    values.put("observaciones_tramo", observaciones.getText());
	    try {
		model.create(values);
		fillWidgetsForCreatingRecord();
		refreshParentForm();
	    } catch (Exception e) {
		NotificationManager.addError(e);
	    }
	}
    }

    private final class SaveAction implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
	    model.updateValue("codigo_carretera", carretera.getText());
	    model.updateValue("codigo_municipio",
		    ((KeyValue) concello.getSelectedItem()).getKey());
	    model.updateValue("orden_tramo", ordenTramo.getText());
	    model.updateValue("pk_inicial_tramo", pkInicial.getText());
	    model.updateValue("pk_final_tramo", pkFinal.getText());
	    model.updateValue("longitud_tramo", longitud.getText());
	    model.updateValue("observaciones_tramo", observaciones.getText());
	    try {
		model.update((int) position);
		refreshParentForm();
		fillWidgetsForUpdatingRecord((int) position);
	    } catch (Exception e) {
		NotificationManager.addError(e);
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

}
