package es.icarto.gvsig.viasobras.catalog;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.utils.AbeilleParser;

public class InventarioForm extends JPanel implements IWindow {

    private FormPanel form;
    protected WindowInfo viewInfo = null;
    private HashMap<String, JComponent> widgets;

    public InventarioForm() {
	form = new FormPanel("inventarioform.xml");
	JScrollPane scrolledForm = new JScrollPane(form);
	this.add(scrolledForm);
	widgets = AbeilleParser.getWidgetsFromContainer(form);
	fillComboBoxes();
    }

    private void fillComboBoxes() {
	fillConcellos();
	fillCarreteras();
    }

    private void fillCarreteras() {
	JComboBox cbCarreteras = (JComboBox) widgets.get("carretera");
	cbCarreteras.removeAllItems();
	Carreteras cs = Carreteras.findAll();
	for (Carretera c : cs) {
	    cbCarreteras.addItem(c.getName());
	}
    }

    private void fillConcellos() {
	JComboBox cbConcellos = (JComboBox) widgets.get("concello");
	cbConcellos.removeAllItems();
	Concellos cs = Concellos.findAll();
	for (Concello c : cs) {
	    cbConcellos.addItem(c.getName());
	}
    }

    public WindowInfo getWindowInfo() {
	if (viewInfo == null) {
	    viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG
		    | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	    viewInfo.setTitle("Vias Obras");
	    viewInfo.setWidth(850);
	    viewInfo.setHeight(380);
	}
	return viewInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

}
