package es.icarto.gvsig.viasobras.queries.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.viasobras.domain.catalog.Carretera;
import es.icarto.gvsig.viasobras.domain.catalog.Catalog;
import es.icarto.gvsig.viasobras.domain.catalog.Concello;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.DBFacade;
import es.icarto.gvsig.viasobras.queries.utils.TableModelQueries;
import es.icarto.gvsig.viasobras.queries.utils.TableModelResults;
import es.icarto.gvsig.viasobras.queries.utils.ActuacionesTooltipRenderer;
import es.icarto.gvsig.viasobras.queries.utils.WhereFactory;
import es.udc.cartolab.gvsig.users.utils.DBSession;


@SuppressWarnings("serial")
public class PanelQueriesActuaciones extends gvWindow {

    private FormPanel formBody;
    private JComboBox carreteras;
    private JComboBox concellos;
    private JTextField anho;
    private JTextField valor;
    private JScrollPane scrollPane;
    private JTable queriesTable;
    private DefaultTableModel queriesModel;
    private JButton runQueriesB;

    private static final Logger logger = Logger.getLogger(PanelQueriesActuaciones.class);

    private DBSession dbs;

    private static String NO_QUERY = "";
    private String queryCode = NO_QUERY;

    public PanelQueriesActuaciones(boolean b) {
	super(460, 620, true);
	this.setTitle("Consultas actuaciones");
	formBody = new FormPanel("consultas-actuaciones-ui.xml");
	formBody.setVisible(true);
	scrollPane = new JScrollPane(queriesTable);
	this.add(formBody, BorderLayout.CENTER);
	this.add(scrollPane, BorderLayout.CENTER);
	dbs = DBSession.getCurrentSession();
	initDomainMapper(dbs);
	Catalog.clear();
	initWidgets();
    }

    private void initWidgets() {
	carreteras = (JComboBox) formBody.getComponentByName("carretera");
	concellos = (JComboBox) formBody.getComponentByName("concello");
	anho = (JTextField) formBody.getComponentByName("anho");
	valor = (JTextField) formBody.getComponentByName("valor");
	queriesTable = (JTable) formBody.getComponentByName("queriesTable");
	runQueriesB = (JButton) formBody.getComponentByName("runQueriesButton");
	runQueriesB.setEnabled(false);

	fillCarreteras();
	fillConcellos();
	fillQueriesTable();
	initListeners();
	initFocus();
    }

    private void initListeners() {
	runQueriesB.addActionListener(new RunQueriesListener());
	carreteras.addItemListener(new CarreteraListener());
	concellos.addItemListener(new ConcelloListener());
	ListSelectionModel rowSM = queriesTable.getSelectionModel();
	rowSM.addListSelectionListener(new ListSelectionListener() {
	    public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
		    return;
		}
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (lsm.isSelectionEmpty()) {
		    queryCode = NO_QUERY;
		    runQueriesB.setEnabled(false);
		} else {
		    runQueriesB.setEnabled(true);
		    int selectedRow = lsm.getMinSelectionIndex();
		    queryCode = (String) queriesModel
			    .getValueAt(selectedRow, 0);
		}
	    }
	});
    }

    private final class ConcelloListener implements ItemListener {
	public void itemStateChanged(ItemEvent e) {
	    if (e.getStateChange() == ItemEvent.SELECTED) {
		String code = Catalog.CONCELLO_ALL;
		if (!concellos.getSelectedItem().equals(Catalog.CONCELLO_ALL)) {
		    code = ((Concello) concellos.getSelectedItem()).getCode();
		}
		Catalog.setConcello(code);
	    }
	}
    }

    private final class CarreteraListener implements ItemListener {
	public void itemStateChanged(ItemEvent e) {
	    if (e.getStateChange() == ItemEvent.SELECTED) {
		String code = Catalog.CARRETERA_ALL;
		if (!carreteras.getSelectedItem()
			.equals(Catalog.CARRETERA_ALL)) {
		    code = ((Carretera) carreteras.getSelectedItem()).getCode();
		}
		Catalog.setCarretera(code);
		fillConcellos();
	    }
	}
    }

    private final class RunQueriesListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    try {
		executeQuery();
	    } catch (SQLException e1) {
		NotificationManager.addError(e1);
	    }
	}

	private void executeQuery() throws SQLException {
	    if (queryCode != NO_QUERY) {
		ArrayList<TableModelResults> resultsMap = new ArrayList<TableModelResults>();

		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {

		    PluginServices.getMDIManager().setWaitCursor();

		    String[] queryContents = getQueryContents();
		    String queryCode = queryContents[0];
		    String queryDescription = queryContents[1];
		    String querySQL = queryContents[2];
		    String queryTitle = queryContents[3];
		    String querySubtitle = queryContents[4];

		    DBSession dbs = DBSession.getCurrentSession();
		    con = dbs.getJavaConnection();
		    st = con.prepareStatement(querySQL);
		    logger.info(querySQL);
		    st.execute();
		    rs = st.getResultSet();
		    TableModelResults result = new TableModelResults(queryCode,
			    queryDescription, queryTitle, querySubtitle,
			    getFilters());

		    resultSetToTable(result, rs);
		    resultsMap.add(result);

		    String html = showResultsAsHTML(resultsMap);


		    PanelQueriesResult resultPanel = new PanelQueriesResult();
		    resultPanel.open();
		    resultPanel.setResult(html);
		    resultPanel.setResultMap(resultsMap);
		    resultPanel.setFilters(getFilters());
		    resultPanel.setQueryType(PanelQueriesResult.ACTUACIONES);

		    PluginServices.getMDIManager().restoreCursor();

		} catch (Exception ex) {
		    logger.error(ex.getMessage());
		    PluginServices.getMDIManager().restoreCursor();
		    if (st != null) {
			st.close();
		    }
		    if (rs != null) {
			rs.close();
		    }
		    if (con != null) {
			con.close();
		    }
		}
	    }
	}
    }
    
    private String[] getFilters() {	
	String[] filters = new String[2];
	filters[0] = Catalog.getCarreteraSelected();
	filters[1] = Catalog.getConcelloSelected();
	return filters;
    }

    private String getWhereClause(boolean hasWhere) throws SQLException {
	String anhoValue;
	String textValue;
	
	anhoValue = anho.getText();
	textValue = valor.getText();
	
	String[] filters = getFilters();
	return WhereFactory.createActuaciones(hasWhere, 
			Integer.parseInt(queryCode.substring(1)), 
			filters[0], 
			filters[1],
			anhoValue,
			textValue);
    }

    private String[] getQueryContents() throws Exception {
	DBSession dbs = DBSession.getCurrentSession();
	String whereClause = "codigo" + " = '" + queryCode + "'";
	String[][] tableContent = dbs.getTable("consultas_actuaciones", "consultas",
		whereClause);

	String[] contents = new String[5];
	contents[0] = tableContent[0][0]; // code
	contents[1] = tableContent[0][1]; // description
	contents[2] = tableContent[0][2].replaceAll("\\[\\[WHERE\\]\\]",
		getWhereClause(tableContent[0][3].compareTo("SI") == 0)); // query
	contents[3] = tableContent[0][4]; // title
	contents[4] = tableContent[0][5]; // subtitle
	return contents;
    }

    private String showResultsAsHTML(ArrayList<TableModelResults> resultMap) {
	StringBuffer sf = new StringBuffer();

	for (TableModelResults result : resultMap) {
	    sf.append("<h3 style=\"color: blue\">" + result.getCode()
		    + "  -  " + result.getDescription() + "</h3>");

	    sf.append("<p>" + result.getQueryTables() + "</p>");
	    sf.append(result.getHTML());
	}
	sf.append("</h2>");
	sf.append("<hr>");

	return sf.toString();
    }

    private void resultSetToTable(TableModelResults result, ResultSet rs)
	    throws SQLException {
	// TODO: don't create empty ResultTableModel
	ResultSetMetaData metaData = rs.getMetaData();
	int numColumns = metaData.getColumnCount();

	for (int i = 0; i < numColumns; i++) {
	    result.addColumn(metaData.getColumnLabel(i + 1));
	}

	// Getting values of the rows that have failed
	// int oldErrors = errorsFound;

	while (rs.next()) {
	    // errorsFound++;
	    Object rowData[] = new Object[numColumns];
	    for (int i = 0; i < numColumns; i++) {
		rowData[i] = rs.getObject(i + 1);
	    }
	    result.addRow(rowData);
	}

    }

    private void initFocus() {
	this.setFocusCycleRoot(true);
	this.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
	carreteras.requestFocusInWindow();
	queriesTable.setFocusable(false);
    }
    
    
    private void fillQueriesTable() {
	queriesModel = new TableModelQueries();
	queriesTable.setModel(queriesModel);

	queriesModel.setRowCount(0);
	queriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	queriesTable.setRowSelectionAllowed(true);
	queriesTable.setColumnSelectionAllowed(false);

	queriesModel.addColumn(new TableColumn());
	queriesModel.addColumn(new TableColumn());

	queriesTable.getColumnModel().getColumn(0).setHeaderValue("Código");
	queriesTable.getColumnModel().getColumn(0).setMaxWidth(75);
	DefaultTableCellRenderer columnCentered = new DefaultTableCellRenderer();
	columnCentered.setHorizontalAlignment(SwingConstants.CENTER);
	queriesTable.getColumnModel().getColumn(0)
	.setCellRenderer(columnCentered);

	queriesTable.getSelectionModel().addListSelectionListener
	(new ListSelectionListener() {
	    public void valueChanged(ListSelectionEvent event)
	    { 
		carreteras.setEnabled(true);
		concellos.setEnabled(true);
		anho.setEnabled(true);
		valor.setEnabled(true);
		int viewRow = queriesTable.getSelectedRow();
		String queryCode = queriesTable.getValueAt(viewRow, 0).toString();
		disableComponents(Integer.parseInt(queryCode.substring(1)));
	    }

	    private void disableComponents(int queryCode) {
		switch (queryCode) {
		case 02:
		    carreteras.setEnabled(false);
		    break;
		case 13:
		    carreteras.setEnabled(false);
		    break;
		case 21:
		    carreteras.setEnabled(false);
		    valor.setEnabled(false);
		    break;
		case 22:
		    valor.setEnabled(false);
		    break;
		case 23:
		    valor.setEnabled(false);
		    break;
		case 31:
		    carreteras.setEnabled(false);
		    valor.setEnabled(false);
		    break;
		case 41:
		    carreteras.setEnabled(false);
		    valor.setEnabled(false);
		    break;
		case 60:
		    valor.setEnabled(false);
		    break;
		case 80:
		    anho.setEnabled(false);
		    break;
		case 81:
		    anho.setEnabled(false);
		    break;
		case 90:
		    anho.setEnabled(false);
		    break;
		}
	    }
	});
	       	
	
	queriesTable.getColumnModel().getColumn(1).setHeaderValue("Consulta");
	
	ActuacionesTooltipRenderer tooltipRenderer = new ActuacionesTooltipRenderer();
	queriesTable.getColumnModel().getColumn(1).setCellRenderer(tooltipRenderer);

	try {
	    String[][] tableContent = dbs.getTable("consultas_actuaciones", "consultas",
		    new String[] { "codigo" }, false);
	    for (int i = 0; i < tableContent.length; i++) {
		// Table Schema: 0-codigo 1-descripcion 2-consulta(SQL) 3-haswhere 4-título 5-subtítulo
		Object[] row = new Object[2];
		row[0] = tableContent[i][0]; //código
		row[1] = tableContent[i][4]; //título
		queriesModel.addRow(row);
		queriesModel.fireTableRowsInserted(0,
			queriesModel.getRowCount() - 1);
	    }
	} catch (SQLException e) {
	    logger.error(e.getMessage(), e);
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

    private void initDomainMapper(DBSession dbs) {
	try {
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

}