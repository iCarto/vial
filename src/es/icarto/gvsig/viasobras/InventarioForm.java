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

    public InventarioForm() {
	form = new FormPanel("inventarioform.xml");
	JScrollPane scrolledForm = new JScrollPane(form);
	this.add(scrolledForm);
	fillComboBoxes();
    }

    private void fillComboBoxes() {
	HashMap<String, JComponent> widgets = AbeilleParser
		.getWidgetsFromContainer(form);
	JComboBox cbConcello = (JComboBox) widgets.get("concello");
	cbConcello.removeAllItems();
	ResultSet rs = Concellos.findAll();
	try {
	    while (rs.next()) {
		cbConcello.addItem(rs.getString("concellos1"));
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
