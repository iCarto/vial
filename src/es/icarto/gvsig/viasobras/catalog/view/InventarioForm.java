package es.icarto.gvsig.viasobras.catalog.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import es.icarto.gvsig.viasobras.catalog.domain.Carretera;
import es.icarto.gvsig.viasobras.catalog.domain.Catalog;
import es.icarto.gvsig.viasobras.catalog.domain.Concello;

public class InventarioForm extends JPanel implements IWindow {

    protected static final String VOID_ITEM = "-";
    private FormPanel form;
    protected WindowInfo viewInfo = null;
    private HashMap<String, JComponent> widgets;
    private HashMap<String, JButton> buttons;
    private ItemListener catalogUpdater;
    private JComboBox carreteras;
    private JComboBox concellos;

    public InventarioForm() {
	form = new FormPanel("inventarioform.xml");
	initForm();
	initListeners();
	JScrollPane scrolledForm = new JScrollPane(form);
	this.add(scrolledForm);
    }

    private void initForm() {
	widgets = AbeilleParser.getWidgetsFromContainer(form);
	buttons = AbeilleParser.getButtonsFromContainer(form);
	fillComboBoxes();
	connectButtonsToActions();
    }

    private void initListeners() {
	catalogUpdater = new ItemListener() {

	    public void itemStateChanged(ItemEvent e) {
		if (carreteras.getSelectedItem().equals(VOID_ITEM)) {
		    Catalog.setCarretera(null);
		}
		Catalog.setCarretera((String) carreteras.getSelectedItem());
	    }
	};
    }

    private void connectButtonsToActions() {
	JButton search = (JButton) buttons.get("buscar");
	search.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		fillTables();
	    }
	});
    }

    private void fillTables() {
	JTable tbTipoPavimento = (JTable) widgets.get("tabla_tipo_pavimento");
	TableModel tipoPavimento = Catalog.getTramosTipoPavimento()
		.getTableModel();
	tbTipoPavimento.setModel(tipoPavimento);
    }

    private void fillComboBoxes() {
	fillConcellos();
	fillCarreteras();
    }

    private void fillCarreteras() {
	carreteras = (JComboBox) widgets.get("carretera");
	carreteras.removeItemListener(catalogUpdater);
	carreteras.removeAllItems();
	carreteras.addItem(VOID_ITEM);
	for (Carretera c : Catalog.getCarreteras()) {
	    carreteras.addItem(c.getName());
	}
	carreteras.addItemListener(catalogUpdater);
    }

    private void fillConcellos() {
	concellos = (JComboBox) widgets.get("concello");
	concellos.removeAllItems();
	concellos.addItem(VOID_ITEM);
	for (Concello c : Catalog.getConcellos()) {
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
