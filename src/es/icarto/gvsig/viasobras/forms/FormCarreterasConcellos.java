package es.icarto.gvsig.viasobras.forms;

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

    public FormCarreterasConcellos() {
	super();
    }

    public void open(long position) {
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
	concello = (JTextField) AbeilleParser.getWidgetsFromContainer(form)
		.get("codigo_concello");
	carretera = (JTextField) AbeilleParser.getWidgetsFromContainer(form)
		.get("codigo_carretera");
	pkInicial = (JTextField) AbeilleParser.getWidgetsFromContainer(form)
		.get("pk_inicial");
	pkFinal = (JTextField) AbeilleParser.getWidgetsFromContainer(form).get(
		"pk_final");
	observaciones = (JTextArea) AbeilleParser.getWidgetsFromContainer(form)
		.get("observaciones");
    }

    private void fillWidgets(long position) {
	try {
	    concello.setText(model.read((int) position).get("codigo_concello"));
	    carretera.setText(model.read((int) position)
		    .get("codigo_carretera"));
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
		    "Carreteras / Concellos"));
	    viewInfo.setWidth(275);
	    viewInfo.setHeight(250);
	}
	return viewInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

}
