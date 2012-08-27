package es.icarto.gvsig.viasobras.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.gui.tables.IForm;
import es.icarto.gvsig.navtableforms.gui.tables.TableModelAlphanumeric;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;

public class FormCarreterasConcellos extends JPanel implements IForm, IWindow {

    private WindowInfo viewInfo;
    private JScrollPane form;
    private TableModelAlphanumeric model;

    private JTextField carretera;
    private JTextField concello;
    private JTextField pkInicial;
    private JTextField pkFinal;
    private JTextArea observaciones;
    private JButton save;
    private ActionListener action;

    private String carreteraCode;
    private long position;

    public FormCarreterasConcellos() {
	super();
	FormPanel formBody = new FormPanel("carreteras-concellos.xml");
	form = new JScrollPane(formBody);
	initWidgets();
	action = new CreateAction();
	save.addActionListener(action);
    }

    /**
     * setModel & setCarreteraCode should be set before executing it
     */
    public void createRecord(long position) {
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
    public void updateRecord(long position) {
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
    public void deleteRecord(long position) {
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
	concello = (JTextField) widgets.get("codigo_municipio");
	pkInicial = (JTextField) widgets.get("pk_inicial");
	pkFinal = (JTextField) widgets.get("pk_final");
	observaciones = (JTextArea) widgets.get("observaciones");

	carretera.setEnabled(false);

	save = (JButton) AbeilleParser.getButtonsFromContainer(form).get(
		"guardar");
    }

    private void fillWidgetsForUpdatingRecord(long position) {
	try {
	    carretera.setText(model.read((int) position)
		    .get("codigo_carretera"));
	    concello.setText(model.read((int) position).get("codigo_municipio"));
	    pkInicial.setText(model.read((int) position).get("pk_inicial"));
	    pkFinal.setText(model.read((int) position).get("pk_final"));
	    observaciones.setText(model.read((int) position).get(
		    "observaciones"));
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    carretera.setText("");
	    concello.setText("");
	    pkInicial.setText("");
	    pkFinal.setText("");
	    observaciones.setText("");
	}
    }

    private void fillWidgetsForCreatingRecord() {
	carretera.setText(carreteraCode);
	concello.setText("");
	pkInicial.setText("");
	pkFinal.setText("");
	observaciones.setText("");
    }

    public WindowInfo getWindowInfo() {
	if (viewInfo == null) {
	    viewInfo = new WindowInfo(WindowInfo.MODALDIALOG
		    | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	    viewInfo.setTitle(PluginServices.getText(this,
		    "Carreteras / Ayuntamientos"));
	    viewInfo.setWidth(275);
	    viewInfo.setHeight(275);
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
	    values.put("codigo_municipio", concello.getText());
	    values.put("pk_inicial", pkInicial.getText());
	    values.put("pk_final", pkFinal.getText());
	    values.put("observaciones", observaciones.getText());
	    try {
		model.create(values);
		fillWidgetsForCreatingRecord();
	    } catch (Exception e) {
		NotificationManager.addError(e);
	    }
	}
    }

    private final class SaveAction implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
	    model.updateValue("codigo_carretera", carretera.getText());
	    model.updateValue("codigo_municipio", concello.getText());
	    model.updateValue("pk_inicial", pkInicial.getText());
	    model.updateValue("pk_final", pkFinal.getText());
	    model.updateValue("observaciones", observaciones.getText());
	    try {
		model.update((int) position);
	    } catch (Exception e) {
		NotificationManager.addError(e);
	    }
	}
    }

}
