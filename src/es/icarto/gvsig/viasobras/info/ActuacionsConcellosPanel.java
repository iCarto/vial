package es.icarto.gvsig.viasobras.info;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.table.gui.Table;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.gui.tables.TableFilter;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;

public class ActuacionsConcellosPanel extends JPanel implements IWindow {

    private WindowInfo viewInfo = null;

    private FormPanel form;
    private JScrollPane scrollPane;

    private ArrayList<String> concellos;
    private String actuacion;

    public ActuacionsConcellosPanel(String actuacion) {
	this.actuacion = actuacion;
	form = new FormPanel("concellos.xml");
	scrollPane = new JScrollPane(form);
	this.add(scrollPane);
	initConcellosAffected();
	setConcellosCheckBoxes();
    }

    private void initConcellosAffected() {
	this.concellos = new ArrayList<String>();
	TableManager tableManager = new TableManager();
	Table t = tableManager.getTableByName("actuacions_concellos");
	ArrayList<String> cols = new ArrayList<String>();
	cols.add("codigo_concello");
	Object[][] cs = TableFilter.getRowsFromSource(t.getModel().getModelo(),
		"codigo_actuacion", actuacion, cols);
	for (int i = 0; i < cs.length; i++) {
	    concellos.add(cs[i][0].toString());
	}
    }

    public void actionPerformed(ActionEvent arg0) {
    }

    public Object getWindowModel() {
	return "form-concellos";
    }

    public WindowInfo getWindowInfo() {
	if (viewInfo == null) {
	    viewInfo = new WindowInfo(WindowInfo.MODALDIALOG
		    | WindowInfo.PALETTE);
	    viewInfo.setTitle("Concellos");
	    viewInfo.setWidth(800);
	    viewInfo.setHeight(360);
	}
	return viewInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

    private void setConcellosCheckBoxes() {
	HashMap<String, JComponent> concellosCHB = AbeilleParser
		.getWidgetsFromContainer(form);
	for (String concello : concellos) {
	    ((JCheckBox) concellosCHB.get(concello)).setSelected(true);
	}
    }

}
