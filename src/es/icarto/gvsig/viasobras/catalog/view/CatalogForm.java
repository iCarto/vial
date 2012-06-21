package es.icarto.gvsig.viasobras.catalog.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutFocusTraversalPolicy;
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

    private JComboBox carreteras;
    private ItemListener carreteraUpdater;
    private JComboBox concellos;
    private ItemListener concelloUpdater;

    private JTextField pkStart;
    private JTextField pkEnd;

    private JTable tipoPavimento;
    private TableModel tipoPavimentoModel;
    private JTable anchoPlataforma;
    private TableModel anchoPlataformaModel;

    private JButton search;
    private JButton load;
    private JButton save;

    public CatalogForm() {
	form = new FormPanel("inventarioform.xml");
	Catalog.clear();
	initForm();
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
	pkStart.setEnabled(false);
	pkEnd.setEnabled(false);

	tipoPavimento = (JTable) widgets.get("tabla_tipo_pavimento");
	anchoPlataforma = (JTable) widgets.get("tabla_ancho_plataforma");

	search = (JButton) buttons.get("buscar");
	load = (JButton) buttons.get("cargar");
	save = (JButton) buttons.get("guardar");

	fillComboBoxes();
	fillTables();

	initFocus();
	initListeners();
    }

    private void initFocus() {
	this.setFocusCycleRoot(true);
	this.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
	carreteras.requestFocusInWindow();
	tipoPavimento.setFocusable(false);
	anchoPlataforma.setFocusable(false);
    }

    private void initListeners() {

	carreteraUpdater = new ItemListener() {
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
		    if (carreteras.getSelectedItem().equals(VOID_ITEM)) {
			Catalog.setCarretera(Catalog.CARRETERA_NONE);
			disablePKControls();
		    } else {
			Carretera c = (Carretera) carreteras.getSelectedItem();
			Catalog.setCarretera(c.getCode());
			enablePKControls();
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
			if (Catalog.getCarreteraSelected() != Catalog.CARRETERA_NONE) {
			    enablePKControls();
			} else {
			    disablePKControls();
			}
		    } else {
			Concello c = (Concello) concellos.getSelectedItem();
			Catalog.setConcello(c.getCode());
			disablePKControls();
		    }
		}
	    }

	};

	carreteras.addItemListener(carreteraUpdater);
	concellos.addItemListener(concelloUpdater);

	KeyListener mySearchListener = new KeyListener() {
	    public void keyPressed(KeyEvent arg0) {
	    }

	    public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		    setPKOnCatalog();
		    fillTables();
		}
	    }

	    public void keyTyped(KeyEvent arg0) {
	    }
	};
	pkStart.addKeyListener(mySearchListener);
	pkEnd.addKeyListener(mySearchListener);

	search.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		setPKOnCatalog();
		fillTables();
	    }
	});
	load.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent arg0) {
		MapLoader.load();
	    }

	});
	save.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		try {
		    ((TramosTableModel) tipoPavimentoModel).saveChanges();
		    ((TramosTableModel) anchoPlataformaModel).saveChanges();
		} catch (SQLException e1) {
		    NotificationManager.addError(e1);
		}
	    }
	});

    }

    private void enablePKControls() {
	pkStart.setEnabled(true);
	pkEnd.setEnabled(true);
    }

    private void disablePKControls() {
	pkStart.setEnabled(false);
	pkEnd.setEnabled(false);
    }

    private void setPKOnCatalog() {
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
    }

    private void fillTables() {
	try {
	    tipoPavimentoModel = Catalog.getTramosTipoPavimento()
		    .getTableModel();
	    tipoPavimento.setModel(tipoPavimentoModel);
	    anchoPlataformaModel = Catalog.getTramosAnchoPlataforma()
		    .getTableModel();
	    anchoPlataforma.setModel(anchoPlataformaModel);
	    this.repaint();
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
