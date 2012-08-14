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

    private JTextField concello;
    private JTextField carretera;
    private JTextField pkInicial;
    private JTextField pkFinal;
    private JTextArea observaciones;
    private JButton save;

    private long position;

    public FormCarreterasConcellos() {
	super();
    }

    public void open(long position) {
	this.position = position;
	if (form == null) {
	    FormPanel formBody = new FormPanel("carreteras-concellos.xml");
	    form = new JScrollPane(formBody);
	}
	initWidgets();
	fillWidgets(position);
	this.add(form);
	PluginServices.getMDIManager().addWindow(this);
    }

    public void setModel(TableModelAlphanumeric model) {
	this.model = model;
    }

    private void initWidgets() {
	HashMap<String, JComponent> widgets = AbeilleParser
		.getWidgetsFromContainer(form);
	concello = (JTextField) widgets.get("codigo_concello");
	carretera = (JTextField) widgets.get("codigo_carretera");
	pkInicial = (JTextField) widgets.get("pk_inicial");
	pkFinal = (JTextField) widgets.get("pk_final");
	observaciones = (JTextArea) widgets.get("observaciones");

	save = (JButton) AbeilleParser.getButtonsFromContainer(form).get(
		"guardar");
	save.addActionListener(new SaveAction());
    }

    private void fillWidgets(long position) {
	try {
	    concello.setText(model.read((int) position).get("codigo_concello"));
	    carretera.setText(model.read((int) position)
		    .get("codigo_carretera"));
	    carretera.setEnabled(false);
	    pkInicial.setText(model.read((int) position).get("pk_inicial"));
	    pkFinal.setText(model.read((int) position).get("pk_final"));
	    observaciones.setText(model.read((int) position).get(
		    "observaciones"));
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    concello.setText("");
	    carretera.setText("");
	    pkInicial.setText("");
	    pkFinal.setText("");
	    observaciones.setText("");
	}
    }

    public IEditableSource getSource() {
	return model.getSource();
    }

    public void delete(long position) {
	try {
	    model.delete((int) position);
	} catch (Exception e) {
	    NotificationManager.addError(e);
	}
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

    private final class SaveAction implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
	    model.updateValue("codigo_carretera", carretera.getText());
	    model.updateValue("codigo_concello", concello.getText());
	    model.updateValue("pk_inicial", pkInicial.getText());
	    model.updateValue("pk_final", pkFinal.getText());
	    model.updateValue("observaciones", observaciones.getText());
	    try {
		model.update((int) position);
	    } catch (ReadDriverException e) {
		NotificationManager.addError(e);
	    }
	}
    }

}
