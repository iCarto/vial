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

public class FormCarreterasMunicipios extends JPanel implements IForm, IWindow {

    private WindowInfo viewInfo;
    private JScrollPane form;
    private TableModelAlphanumeric model;

    private JTextField carretera;
    private JTextField concello;
    private JTextField ordenTramo;
    private JTextField pkInicial;
    private JTextField pkFinal;
    private JTextArea observaciones;
    private JButton save;
    private ActionListener action;

    private String carreteraCode;
    private long position;

    public FormCarreterasMunicipios() {
	super();
	FormPanel formBody = new FormPanel("carreteras-municipios.xml");
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
	ordenTramo = (JTextField) widgets.get("orden_tramo");
	pkInicial = (JTextField) widgets.get("pk_inicial_tramo");
	pkFinal = (JTextField) widgets.get("pk_final_tramo");
	observaciones = (JTextArea) widgets.get("observaciones_tramo");

	carretera.setEnabled(false);

	save = (JButton) AbeilleParser.getButtonsFromContainer(form).get(
		"guardar");
    }

    private void fillWidgetsForUpdatingRecord(long position) {
	try {
	    carretera.setText(model.read((int) position)
		    .get("codigo_carretera"));
	    concello.setText(model.read((int) position).get("codigo_municipio"));
	    ordenTramo.setText(model.read((int) position).get("orden_tramo"));
	    pkInicial.setText(model.read((int) position)
		    .get("pk_inicial_tramo"));
	    pkFinal.setText(model.read((int) position).get("pk_final_tramo"));
	    observaciones.setText(model.read((int) position).get(
		    "observaciones_tramo"));
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    carretera.setText("");
	    concello.setText("");
	    ordenTramo.setText("");
	    pkInicial.setText("");
	    pkFinal.setText("");
	    observaciones.setText("");
	}
    }

    private void fillWidgetsForCreatingRecord() {
	carretera.setText(carreteraCode);
	concello.setText("");
	ordenTramo.setText("");
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
	    viewInfo.setHeight(300);
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
	    values.put("orden_tramo", ordenTramo.getText());
	    values.put("pk_inicial_tramo", pkInicial.getText());
	    values.put("pk_final_tramo", pkFinal.getText());
	    values.put("observaciones_tramo", observaciones.getText());
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
	    model.updateValue("orden_tramo", ordenTramo.getText());
	    model.updateValue("pk_inicial_tramo", pkInicial.getText());
	    model.updateValue("pk_final_tramo", pkFinal.getText());
	    model.updateValue("observaciones_tramo", observaciones.getText());
	    try {
		model.update((int) position);
	    } catch (Exception e) {
		NotificationManager.addError(e);
	    }
	}
    }

}
