package es.icarto.gvsig.viasobras;

import java.sql.ResultSet;
import java.sql.SQLException;
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
	ResultSet rs = Carreteras.findAll();
	try {
	    while (rs.next()) {
		cbCarreteras.addItem(rs.getString("denominaci"));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    private void fillConcellos() {
	JComboBox cbConcellos = (JComboBox) widgets.get("concello");
	cbConcellos.removeAllItems();
	ResultSet rs = Concellos.findAll();
	try {
	    while (rs.next()) {
		cbConcellos.addItem(rs.getString("concellos1"));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
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
