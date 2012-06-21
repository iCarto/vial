package es.icarto.gvsig.viasobras.catalog.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.icarto.gvsig.viasobras.catalog.domain.Carretera;
import es.icarto.gvsig.viasobras.catalog.domain.Catalog;
import es.icarto.gvsig.viasobras.catalog.domain.Concello;
import es.icarto.gvsig.viasobras.catalog.view.load.MapLoader;
import es.icarto.gvsig.viasobras.catalog.view.tables.TramosTableModel;

public class CatalogForm extends JPanel implements IWindow, SingletonWindow {

    private static final String VOID_ITEM = "Todos";

    private FormPanel form;
    protected WindowInfo viewInfo = null;
    private HashMap<String, JComponent> widgets;
    private HashMap<String, JButton> buttons;
    private ItemListener carreteraUpdater;
    private ItemListener concelloUpdater;
    private JComboBox carreteras;
    private JComboBox concellos;
    private TableModel tipoPavimento;
    private TableModel anchoPlataforma;

    private JTextField pkStart;
    private JTextField pkEnd;

    public CatalogForm() {
	form = new FormPanel("inventarioform.xml");
	initForm();
	initListeners();
	Catalog.clear();
	JScrollPane scrolledForm = new JScrollPane(form);
	this.add(scrolledForm);
    }

    private void initForm() {
	widgets = AbeilleParser.getWidgetsFromContainer(form);
	buttons = AbeilleParser.getButtonsFromContainer(form);
	carreteras = (JComboBox) widgets.get("carretera");
	concellos = (JComboBox) widgets.get("concello");
	pkStart = (JTextField) widgets.get("pk_inicial");
	pkEnd = (JTextField) widgets.get("pk_final");
	fillComboBoxes();
	connectButtonsToActions();
    }

    private void initListeners() {
	carreteraUpdater = new ItemListener() {

	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
		    if (carreteras.getSelectedItem().equals(VOID_ITEM)) {
			Catalog.setCarretera(Catalog.CARRETERA_NONE);
		    } else {
			Carretera c = (Carretera) carreteras.getSelectedItem();
			Catalog.setCarretera(c.getCode());
		    }
		    fillConcellos();
		}
	    }
	};

	concelloUpdater = new ItemListener() {

	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
		    if (concellos.getSelectedItem().equals(VOID_ITEM)) {
			Catalog.setConcello(Catalog.CONCELLO_NONE);
		    } else {
			Concello c = (Concello) concellos.getSelectedItem();
			Catalog.setConcello(c.getCode());
		    }
		}
	    }

	};

	carreteras.addItemListener(carreteraUpdater);
	concellos.addItemListener(concelloUpdater);
    }

    private void connectButtonsToActions() {
	JButton search = (JButton) buttons.get("buscar");
	search.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		if (!pkStart.getText().equals("")) {
		    double d = Double.parseDouble(pkStart.getText());
		    Catalog.setPKStart(d);
		} else {
		    Catalog.setPKStart(Catalog.PK_NONE);
		}
		if (!pkEnd.getText().equals("")) {
		    double d = Double.parseDouble(pkEnd.getText());
		    Catalog.setPKEnd(d);
		} else {
		    Catalog.setPKEnd(Catalog.PK_NONE);
		}
		fillTables();
	    }
	});
	JButton load = (JButton) buttons.get("cargar");
	load.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent arg0) {
		MapLoader.load();
	    }

	});
	JButton save = (JButton) buttons.get("guardar");
	save.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		try {
		    ((TramosTableModel) tipoPavimento).saveChanges();
		    ((TramosTableModel) anchoPlataforma).saveChanges();
		} catch (SQLException e1) {
		    NotificationManager.addError(e1);
		}
	    }
	});
    }

    private void fillTables() {
	JTable tbTipoPavimento = (JTable) widgets.get("tabla_tipo_pavimento");
	JTable tbAnchoPlataforma = (JTable) widgets
		.get("tabla_ancho_plataforma");
	try {
	    tipoPavimento = Catalog.getTramosTipoPavimento().getTableModel();
	    tbTipoPavimento.setModel(tipoPavimento);
	    anchoPlataforma = Catalog.getTramosAnchoPlataforma()
		    .getTableModel();
	    tbAnchoPlataforma.setModel(anchoPlataforma);
	} catch (SQLException e) {
	    NotificationManager.addError(e);
	}
    }

    private void fillComboBoxes() {
	fillConcellos();
	fillCarreteras();
    }

    private void fillCarreteras() {
	carreteras.removeAllItems();
	carreteras.addItem(VOID_ITEM);
	try {
	    for (Carretera c : Catalog.getCarreteras()) {
		carreteras.addItem(c);
	    }
	} catch (SQLException e) {
	    NotificationManager.addError(e);
	}
    }

    private void fillConcellos() {
	concellos.removeAllItems();
	concellos.addItem(VOID_ITEM);
	try {
	    for (Concello c : Catalog.getConcellos()) {
		concellos.addItem(c);
	    }
	} catch (SQLException e) {
	    NotificationManager.addError(e);
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

    public Object getWindowModel() {
	return "catalog-roads";
    }

}
