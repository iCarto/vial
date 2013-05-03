package es.icarto.gvsig.viasobras.forms;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.icarto.gvsig.viasobras.domain.catalog.Carretera;
import es.icarto.gvsig.viasobras.domain.catalog.Catalog;
import es.icarto.gvsig.viasobras.domain.catalog.Concello;
import es.icarto.gvsig.viasobras.domain.catalog.Evento;
import es.icarto.gvsig.viasobras.domain.catalog.Tramo;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.DBFacade;
import es.icarto.gvsig.viasobras.domain.catalog.utils.EventosTableModel;
import es.icarto.gvsig.viasobras.domain.catalog.utils.TramosTableModel;
import es.icarto.gvsig.viasobras.maploader.MapLoader;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormCatalog extends JPanel implements IWindow, SingletonWindow {

    private FormPanel form;
    protected WindowInfo viewInfo = null;

    private HashMap<String, JComponent> widgets;
    private HashMap<String, JButton> buttons;

    private JComboBox carreteras;
    private JComboBox concellos;
    private JTextField pkStart;
    private JTextField pkEnd;

    private JButton load;
    private JButton save;

    private JComboBox mapLoad;

    private JTable tipoPavimento;
    private TramosTableModel tipoPavimentoModel;
    private JButton insertTramoPavimento;
    private JButton deleteTramoPavimento;

    private JTable anchoPlataforma;
    private TramosTableModel anchoPlataformaModel;
    private JButton insertTramoPlataforma;
    private JButton deleteTramoPlataforma;

    private JTable cotas;
    private TramosTableModel cotasModel;
    private JButton insertCota;
    private JButton deleteCota;

    private JTable aforos;
    private EventosTableModel aforosModel;
    private JButton insertAforo;
    private JButton deleteAforo;

    private JTable accidentes;
    private EventosTableModel accidentesModel;
    private JButton insertAccidente;
    private JButton deleteAccidente;

    public FormCatalog() {
	form = new FormPanel("catalogo-ui.xml");
	initDomainMapper();
	Catalog.clear();
	initForm();
	JScrollPane scrolledForm = new JScrollPane(form);
	this.add(scrolledForm);
    }

    private void initDomainMapper() {
	try {
	    DBSession dbs = DBSession.getCurrentSession();
	    Properties p = new Properties();
	    p.setProperty(DBFacade.URL, dbs.getJavaConnection().getMetaData()
		    .getURL());
	    p.setProperty(DBFacade.USERNAME, dbs.getUserName());
	    p.setProperty(DBFacade.PASSWORD, dbs.getPassword());
	    // Create the connection ourselves, as at this moment, gvSIG has an
	    // old driver (lower than jdbc4) which doesn't implement the methods
	    // we need. So, take care that the jar in
	    // lib/postgresql-8.4-jdbc4.jar is used instead of gvSIG ones.
	    Class.forName("org.postgresql.Driver");
	    Connection c = DriverManager.getConnection(
		    p.getProperty(DBFacade.URL),
		    p.getProperty(DBFacade.USERNAME),
		    p.getProperty(DBFacade.PASSWORD));
	    DBFacade.setConnection(c, p);
	} catch (Exception e) {
	    NotificationManager.addError(e);
	}
    }

    private void initForm() {

	setWidgets();

	fillComboBoxes();
	fillTables();

	initFocus();
	initListeners();
    }

    private void setWidgets() {
	widgets = AbeilleParser.getWidgetsFromContainer(form);
	buttons = AbeilleParser.getButtonsFromContainer(form);

	carreteras = (JComboBox) widgets.get("carretera");
	concellos = (JComboBox) widgets.get("concello");

	pkStart = (JTextField) widgets.get("pk_inicial");
	pkEnd = (JTextField) widgets.get("pk_final");
	pkStart.setEnabled(false);
	pkEnd.setEnabled(false);

	load = (JButton) buttons.get("cargar");
	save = (JButton) buttons.get("guardar");

	mapLoad = (JComboBox) widgets.get("mapa");

	tipoPavimento = (JTable) widgets.get("tabla_tipo_pavimento");
	insertTramoPavimento = (JButton) buttons.get("insertar_pavimento");
	deleteTramoPavimento = (JButton) buttons.get("borrar_pavimento");

	anchoPlataforma = (JTable) widgets.get("tabla_ancho_plataforma");
	insertTramoPlataforma = (JButton) buttons.get("insertar_plataforma");
	deleteTramoPlataforma = (JButton) buttons.get("borrar_plataforma");

	cotas = (JTable) widgets.get("tabla_cotas");
	insertCota = (JButton) buttons.get("insertar_cota");
	deleteCota = (JButton) buttons.get("borrar_cota");

	aforos = (JTable) widgets.get("tabla_aforos");
	insertAforo = (JButton) buttons.get("insertar_aforo");
	deleteAforo = (JButton) buttons.get("borrar_aforo");

	accidentes = (JTable) widgets.get("tabla_accidentes");
	insertAccidente = (JButton) buttons.get("insertar_accidente");
	deleteAccidente = (JButton) buttons.get("borrar_accidente");

    }

    private void initFocus() {
	this.setFocusCycleRoot(true);
	this.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
	carreteras.requestFocusInWindow();
	tipoPavimento.setFocusable(false);
	anchoPlataforma.setFocusable(false);
	cotas.setFocusable(false);
	aforos.setFocusable(false);
	accidentes.setFocusable(false);
    }

    private void initListeners() {

	carreteras.addItemListener(new CarreteraListener());
	concellos.addItemListener(new ConcelloListener());

	SearchListener mySearchListener = new SearchListener();
	pkStart.addKeyListener(mySearchListener);
	pkEnd.addKeyListener(mySearchListener);

	load.addActionListener(new LoadMapListener());
	save.addActionListener(new SaveChangesListener());

	insertTramoPavimento.addActionListener(
		new InsertTramoPavimentoListener());
	deleteTramoPavimento.addActionListener(
		new DeleteTramoPavimentoListener());

	insertTramoPlataforma.addActionListener(
		new InsertTramoPlataformaListener());
	deleteTramoPlataforma.addActionListener(
		new DeleteTramoPlataformaListener());

	insertCota.addActionListener(
		new InsertTramoCotaListener());
	deleteCota.addActionListener(
		new DeleteTramoCotaListener());

	insertAforo.addActionListener(new InsertEventoAforoListener());
	deleteAforo.addActionListener(new DeleteEventoAforoListener());

	insertAccidente.addActionListener(new InsertEventoAccidenteListener());
	deleteAccidente.addActionListener(new DeleteEventoAccidenteListener());
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
	double start;
	try {
	    start = Double.parseDouble(pkStart.getText());
	} catch (NumberFormatException e) {
	    pkStart.setText("");
	    start = Catalog.PK_NONE;
	}
	Catalog.setPKStart(start);
	double end;
	try {
	    end = Double.parseDouble(pkEnd.getText());
	} catch (NumberFormatException e) {
	    pkEnd.setText("");
	    end = Catalog.PK_NONE;
	}
	Catalog.setPKEnd(end);
    }

    private void fillTables() {
	try {
	    TableCellRenderer dateCellRenderer = new DefaultTableCellRenderer() {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		    if (value instanceof Date) {
			value = f.format(value);
		    }
		    return super.getTableCellRendererComponent(table, value,
			    isSelected, hasFocus, row, column);
		}
	    };
	    DefaultCellEditor dateCellEditor = new DefaultCellEditor(
		    new JTextField());

	    /*
	     * Although the class of field caracteristica is set when retrieving
	     * the data -see TramosRecorsetAdapter.toList() or
	     * EventosRecordsetAdapter.toList()- when a road have no tramos (ie:
	     * roads recently created) the type is not set, so we need tell the
	     * model which specific class is the field, hence the
	     * "tipoPavimentoModel.getMetadata().setValueClass(String.class);"
	     * command.
	     * 
	     * TODO: try to do this in such a way there is only 1 point to
	     * retrieve the class of the field.
	     */
	    tipoPavimentoModel = (TramosTableModel) Catalog
		    .getTramosTipoPavimento().getTableModel();
	    tipoPavimentoModel.getMetadata().setValueClass(String.class);
	    tipoPavimento.setModel(tipoPavimentoModel);
	    tipoPavimento.getColumnModel()
	    .getColumn(Tramo.PROPERTY_UPDATING_DATE)
	    .setCellRenderer(dateCellRenderer);
	    tipoPavimento.getColumnModel()
	    .getColumn(Tramo.PROPERTY_UPDATING_DATE)
	    .setCellEditor(dateCellEditor);

	    anchoPlataformaModel = (TramosTableModel) Catalog
		    .getTramosAnchoPlataforma()
		    .getTableModel();
	    anchoPlataformaModel.getMetadata().setValueClass(Double.class);
	    anchoPlataforma.setModel(anchoPlataformaModel);
	    anchoPlataforma.getColumnModel()
	    .getColumn(Tramo.PROPERTY_UPDATING_DATE)
	    .setCellRenderer(dateCellRenderer);
	    anchoPlataforma.getColumnModel()
	    .getColumn(Tramo.PROPERTY_UPDATING_DATE)
	    .setCellEditor(dateCellEditor);

	    cotasModel = (TramosTableModel) Catalog.getTramosCotas()
		    .getTableModel();
	    cotasModel.getMetadata().setValueClass(Double.class);
	    cotas.setModel(cotasModel);
	    cotas.getColumnModel().getColumn(Tramo.PROPERTY_UPDATING_DATE)
	    .setCellRenderer(dateCellRenderer);
	    cotas.getColumnModel().getColumn(Tramo.PROPERTY_UPDATING_DATE)
	    .setCellEditor(dateCellEditor);

	    aforosModel = (EventosTableModel) Catalog.getEventosAforos()
		    .getTableModel();
	    aforosModel.getMetadata().setValueClass(Integer.class);
	    aforos.setModel(aforosModel);
	    aforos.getColumnModel().getColumn(Evento.PROPERTY_DATE)
	    .setCellRenderer(dateCellRenderer);
	    aforos.getColumnModel().getColumn(Evento.PROPERTY_DATE)
	    .setCellEditor(dateCellEditor);

	    accidentesModel = (EventosTableModel) Catalog
		    .getEventosAccidentes().getTableModel();
	    accidentesModel.getMetadata().setValueClass(String.class);
	    accidentes.setModel(accidentesModel);
	    accidentes.getColumnModel().getColumn(Evento.PROPERTY_DATE)
	    .setCellRenderer(dateCellRenderer);
	    accidentes.getColumnModel().getColumn(Evento.PROPERTY_DATE)
	    .setCellEditor(dateCellEditor);

	    this.repaint();
	} catch (SQLException e) {
	    NotificationManager.addError(e);
	}
    }

    private void fillComboBoxes() {
	fillConcellos();
	fillCarreteras();
	fillELLEMaps();
    }

    private void fillELLEMaps() {
	mapLoad.removeAllItems();
	List<String> maps = MapLoader.getAllMapNames();
	for (String mapName : maps) {
	    mapLoad.addItem(mapName);
	}
    }

    private void fillCarreteras() {
	carreteras.removeAllItems();
	carreteras.addItem(Catalog.CARRETERA_ALL);
	try {
	    for (Carretera c : Catalog.getCarreteras()) {
		carreteras.addItem(c);
	    }
	} catch (SQLException e) {
	    carreteras.removeAllItems();
	    carreteras.addItem(Catalog.CARRETERA_ALL);
	    System.out.println(e.getMessage());
	    NotificationManager.addError(e);
	}
    }

    private void fillConcellos() {
	concellos.removeAllItems();
	concellos.addItem(Catalog.CONCELLO_ALL);
	try {
	    for (Concello c : Catalog.getConcellos()) {
		concellos.addItem(c);
	    }
	} catch (SQLException e) {
	    concellos.removeAllItems();
	    concellos.addItem(Catalog.CONCELLO_ALL);
	    System.out.println(e.getMessage());
	    NotificationManager.addError(e);
	}
    }

    public WindowInfo getWindowInfo() {
	if (viewInfo == null) {
	    viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG
		    | WindowInfo.ICONIFIABLE
		    | WindowInfo.PALETTE);
	    viewInfo.setTitle("Vías y Obras: catálogo");
	    viewInfo.setWidth(960);
	    viewInfo.setHeight(540);
	}
	return viewInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

    public Object getWindowModel() {
	return "catalog-roads";
    }

    private final class DeleteTramoPlataformaListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    if ((anchoPlataforma.getRowCount() > 0)
		    && (anchoPlataforma.getSelectedRow() != -1)) {
		((TramosTableModel) anchoPlataformaModel)
		.deleteTramo(anchoPlataforma.getSelectedRow());
		refreshTables();
	    }
	}
    }

    private void refreshTables() {
	// will force embedded tables to repaint
	this.repaint();
    }

    private final class InsertTramoPlataformaListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    Tramo t = createNewTramo();
	    ((TramosTableModel) anchoPlataformaModel).addTramo(t);
	}

    }

    private final class DeleteTramoPavimentoListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    if ((tipoPavimento.getRowCount() > 0)
		    && (tipoPavimento.getSelectedRow() != -1)) {
		((TramosTableModel) tipoPavimentoModel)
		.deleteTramo(tipoPavimento.getSelectedRow());
		refreshTables();
	    }
	}
    }

    private final class InsertTramoPavimentoListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    Tramo t = createNewTramo();
	    ((TramosTableModel) tipoPavimentoModel).addTramo(t);
	}
    }

    private final class DeleteTramoCotaListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    if ((cotas.getRowCount() > 0) && (cotas.getSelectedRow() != -1)) {
		((TramosTableModel) cotasModel).deleteTramo(cotas
			.getSelectedRow());
		refreshTables();
	    }
	}
    }

    private final class InsertTramoCotaListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    Tramo t = createNewTramo();
	    ((TramosTableModel) cotasModel).addTramo(t);
	}
    }

    private final class DeleteEventoAforoListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    if ((aforos.getRowCount() > 0) && (aforos.getSelectedRow() != -1)) {
		((EventosTableModel) aforosModel).deleteEvento(aforos
			.getSelectedRow());
		refreshTables();
	    }
	}
    }

    private final class InsertEventoAforoListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    Evento ev = createNewEvento();
	    ((EventosTableModel) aforosModel).addEvento(ev);
	}

    }

    private final class InsertEventoAccidenteListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    Evento ev = createNewEvento();
	    ((EventosTableModel) accidentesModel).addEvento(ev);
	}

    }

    private final class DeleteEventoAccidenteListener implements
    ActionListener {
	public void actionPerformed(ActionEvent e) {
	    if ((accidentes.getRowCount() > 0)
		    && (accidentes.getSelectedRow() != -1)) {
		((EventosTableModel) accidentesModel).deleteEvento(accidentes
			.getSelectedRow());
		refreshTables();
	    }
	}
    }

    private final class SaveChangesListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    try {
		if (((TramosTableModel) tipoPavimentoModel).canSaveTramos()
			&& ((TramosTableModel) anchoPlataformaModel)
			.canSaveTramos()
			&& ((TramosTableModel) cotasModel).canSaveTramos()
			&& ((EventosTableModel) aforosModel).canSaveEventos()
			&& ((EventosTableModel) accidentesModel)
			.canSaveEventos()) {

		    ((TramosTableModel) tipoPavimentoModel).saveChanges();
		    ((TramosTableModel) anchoPlataformaModel).saveChanges();
		    ((TramosTableModel) cotasModel).saveChanges();
		    ((EventosTableModel) aforosModel).saveChanges();
		    ((EventosTableModel) accidentesModel).saveChanges();

		    doSearch();
		} else {
		    showWarning();
		}
	    } catch (SQLException e1) {
		NotificationManager.addError(e1);
	    }
	}
    }

    private void showWarning() {
	JOptionPane.showMessageDialog(this,
		"No se puede guardar, existen tramos inválidos",
		"Tramos inválidos", JOptionPane.WARNING_MESSAGE);
    }

    private final class LoadMapListener implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
	    try {
		if (mapLoad.getSelectedItem() != null) {
		    MapLoader.loadMap(mapLoad.getSelectedItem().toString());
		} else {
		    MapLoader.loadDefaultMap();
		}
	    } catch (Exception e) {
		NotificationManager.addError(e);
	    }
	}
    }

    private final class SearchListener implements KeyListener, ActionListener {
	public void keyPressed(KeyEvent arg0) {
	}

	public void keyReleased(KeyEvent e) {
	    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		doSearch();
	    }
	}

	public void keyTyped(KeyEvent arg0) {
	}

	public void actionPerformed(ActionEvent arg0) {
	    doSearch();
	}

    }

    private void doSearch() {
	setPKOnCatalog();
	fillTables();
    }

    private final class ConcelloListener implements ItemListener {
	public void itemStateChanged(ItemEvent e) {
	    if (e.getStateChange() == ItemEvent.SELECTED) {
		if (concellos.getSelectedItem().equals(Catalog.CONCELLO_ALL)) {
		    Catalog.setConcello(Catalog.CONCELLO_ALL);
		    if (Catalog.getCarreteraSelected() != Catalog.CARRETERA_ALL) {
			enablePKControls();
		    } else {
			disablePKControls();
		    }
		} else {
		    Concello c = (Concello) concellos.getSelectedItem();
		    Catalog.setConcello(c.getCode());
		    disablePKControls();
		}
		doSearch();
	    }
	}
    }

    private final class CarreteraListener implements ItemListener {
	public void itemStateChanged(ItemEvent e) {
	    if (e.getStateChange() == ItemEvent.SELECTED) {
		if (carreteras.getSelectedItem().equals(Catalog.CARRETERA_ALL)) {
		    Catalog.setCarretera(Catalog.CARRETERA_ALL);
		    disablePKControls();
		} else {
		    Carretera c = (Carretera) carreteras.getSelectedItem();
		    Catalog.setCarretera(c.getCode());
		    enablePKControls();
		}
		fillConcellos();
		doSearch();
	    }
	}
    }

    private Tramo createNewTramo() {
	Tramo t = new Tramo();
	if (Catalog.getCarreteraSelected() != Catalog.CARRETERA_ALL) {
	    t.setCarretera(Catalog.getCarreteraSelected());
	}
	if (Catalog.getConcelloSelected() != Catalog.CONCELLO_ALL) {
	    t.setConcello(Catalog.getConcelloSelected());
	}
	if (Catalog.getPKStart() != Catalog.PK_NONE) {
	    t.setPkStart(Catalog.getPKStart());
	}
	if (Catalog.getPKEnd() != Catalog.PK_NONE) {
	    t.setPkEnd(Catalog.getPKEnd());
	}
	return t;
    }

    private Evento createNewEvento() {
	Evento ev = new Evento();
	if (Catalog.getCarreteraSelected() != Catalog.CARRETERA_ALL) {
	    ev.setCarretera(Catalog.getCarreteraSelected());
	}
	if (Catalog.getConcelloSelected() != Catalog.CONCELLO_ALL) {
	    ev.setConcello(Catalog.getConcelloSelected());
	}
	if (Catalog.getPKStart() != Catalog.PK_NONE) {
	    ev.setPk(Catalog.getPKStart());
	}
	if (Catalog.getPKEnd() != Catalog.PK_NONE) {
	    ev.setPk(Catalog.getPKEnd());
	}
	return ev;
    }

}
