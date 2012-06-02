package es.icarto.gvsig.viasobras.catalog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

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
	init();
    }

    private void init() {
	widgets = AbeilleParser.getWidgetsFromContainer(form);
	fillComboBoxes();
	connectButtonsToActions();
    }

    private void connectButtonsToActions() {
	JButton search = (JButton) widgets.get("buscar");
	search.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		fillTables();
	    }
	});
    }

    private void fillTables() {
	JTable tbTipoPavimento = (JTable) widgets.get("tabla_tipo_pavimento");
	TableModel tipoPavimento = TipoPavimento.findAll();
	tbTipoPavimento.setModel(tipoPavimento);
    }

    private void fillComboBoxes() {
	fillConcellos();
	fillCarreteras();
    }

    private void fillCarreteras() {
	JComboBox carreteras = (JComboBox) widgets.get("carretera");
	carreteras.removeAllItems();
	Carreteras cs = Carreteras.findAll();
	for (Carretera c : cs) {
	    carreteras.addItem(c.getName());
	}
    }

    private void fillConcellos() {
	JComboBox concellos = (JComboBox) widgets.get("concello");
	concellos.removeAllItems();
	Concellos cs = Concellos.findAll();
	for (Concello c : cs) {
	    concellos.addItem(c.getName());
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
