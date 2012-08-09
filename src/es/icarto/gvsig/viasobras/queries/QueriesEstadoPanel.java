package es.icarto.gvsig.viasobras.queries;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.viasobras.domain.catalog.Carretera;
import es.icarto.gvsig.viasobras.domain.catalog.Catalog;
import es.icarto.gvsig.viasobras.domain.catalog.Concello;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.DBFacade;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class QueriesEstadoPanel extends gvWindow {

    private FormPanel formBody;
    private JScrollPane scrollPane;

    private JButton runQueriesB;
    private JTable queriesTable;

    private boolean isReport;

    private static final Logger logger = Logger.getLogger(QueriesEstadoPanel.class);

    private DBSession dbs;

    private JComboBox carreteras;
    private JComboBox concellos;

    public QueriesEstadoPanel(boolean report) {
	super(600, 400, true);
	this.isReport = report;
	if (isReport) {
	    this.setTitle("Informes");
	} else {
	    this.setTitle("Consultas");
	}
	formBody = new FormPanel("queries-estado.xml");
	formBody.setVisible(true);
	scrollPane = new JScrollPane(queriesTable);
	this.add(formBody, BorderLayout.CENTER);
	this.add(scrollPane, BorderLayout.CENTER);
	dbs = DBSession.getCurrentSession();
	initDomainMapper(dbs);
	Catalog.clear();
	initWidgets();
	initListeners();

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

    public void initWidgets() {
	runQueriesB = (JButton) formBody.getComponentByName("runQueriesButton");
	queriesTable = (JTable) formBody.getComponentByName("queriesTable");

	carreteras = (JComboBox) formBody.getComponentByName("carretera");
	concellos = (JComboBox) formBody.getComponentByName("concello");

	fillCarreteras();
	fillConcellos();
	initQueriesTable();
	fillQueriesTable();
    }

    private void initListeners() {
	runQueriesB.addActionListener(new RunQueriesListener());
	carreteras.addItemListener(new CarreteraListener());
	concellos.addItemListener(new ConcelloListener());
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

    private void initQueriesTable() {

	// QUERIES TABLE
	DefaultTableModel model = new QueriesTableModel();
	queriesTable.setModel(model);
	String[] columnNames = { "Ejecutar", "Código", "Descripción" };

	model.setRowCount(0);
	queriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	queriesTable.setRowSelectionAllowed(true);
	queriesTable.setColumnSelectionAllowed(false);

	TableColumn column00 = new TableColumn();
	model.addColumn(column00);

	TableColumn column01 = new TableColumn();
	model.addColumn(column01);

	TableColumn column02 = new TableColumn();
	model.addColumn(column02);

	queriesTable.getColumnModel().getColumn(0)
	.setHeaderValue(columnNames[0]);
	queriesTable.getColumnModel().getColumn(0).setMaxWidth(100);
	queriesTable.getColumnModel().getColumn(1)
	.setHeaderValue(columnNames[1]);
	queriesTable.getColumnModel().getColumn(1).setMinWidth(100);
	queriesTable.getColumnModel().getColumn(1).setMaxWidth(110);
	queriesTable.getColumnModel().getColumn(2)
	.setHeaderValue(columnNames[2]);
	queriesTable.getColumnModel().getColumn(2).setMaxWidth(500);
    }

    private void fillQueriesTable() {
	DefaultTableModel model = (DefaultTableModel) queriesTable.getModel();
	model.setRowCount(0);

	// fill from database
	try {
	    String[] orderBy = new String[1];
	    orderBy[0] = "codigo";
	    String[][] tableContent = dbs.getTable("queries", "queries",
		    orderBy, false);

	    int numRows = 0;
	    for (int i = 0; i < tableContent.length; i++) {
		Object[] row = new Object[5];
		row[0] = new Boolean(false);
		// Table Schema: 0-codigo, 1-descripcion, 2-consulta(SQL)
		row[1] = tableContent[i][0];
		row[2] = tableContent[i][1];
		model.addRow(row);
		numRows++;
		model.fireTableRowsInserted(0, model.getRowCount() - 1);
	    }
	} catch (SQLException e) {
	    logger.error(e.getMessage(), e);
	}
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
	    executeValidations();
	    return;
	}
    }

    private void executeValidations() {
	QueriesTask qt = new QueriesTask();
	ProgressBarDialog progressBarDialog = new ProgressBarDialog(qt);
	progressBarDialog.open();
    }

    private class RunStatementThread extends Thread {

	private Statement st = null;
	private ResultSet rs = null;
	private Connection con;
	private boolean finished = false;
	private String error = null;
	private String query;

	public RunStatementThread(Connection con, String query) {
	    this.con = con;
	    this.query = query;
	}

	public void run() {
	    try {
		st = con.createStatement();
		rs = st.executeQuery(query);
		finished = true;
	    } catch (SQLException e) {
		finished = false;
		error = e.getMessage();
		close();
	    }
	}

	public void cancel() throws SQLException, DBException {
	    if (st != null) {
		st.cancel();
		DBSession.reconnect();
	    }
	}

	public ResultSet getResult() {
	    if (finished) {
		return rs;
	    } else {
		return null;
	    }
	}

	/**
	 * Close a ResultSet
	 * 
	 * @param rs
	 *            , the resultset to be closed
	 * @return true if the resulset was correctly closed. false in any other
	 *         case
	 */
	public boolean closeResultSet(ResultSet rs) {
	    boolean error = false;

	    if (rs != null) {
		try {
		    rs.close();
		    error = true;
		} catch (SQLException e) {
		    logger.error(e.getMessage(), e);
		}
	    }

	    return error;
	}

	/**
	 * Close a Statement
	 * 
	 * @param st
	 *            , the statement to be closed
	 * @return true if the statement was correctly closed, false in any
	 *         other case
	 */
	public boolean closeStatement(Statement st) {
	    boolean error = false;

	    if (st != null) {
		try {
		    st.close();
		    error = true;
		} catch (SQLException e) {
		    logger.error(e.getMessage(), e);
		}
	    }

	    return error;
	}

	public void close() {
	    closeResultSet(rs);
	    closeStatement(st);
	}
    } // RunStatementThread Class

    private class QueriesTask extends SwingWorker<String, Void> {

	private RunStatementThread thread = null;
	private boolean sqlError = false;
	private String error = "";
	private ArrayList<ResultTableModel> resultsMap;

	@Override
	protected String doInBackground() throws Exception {
	    DBSession dbs = DBSession.getCurrentSession();

	    DefaultTableModel model = (DefaultTableModel) queriesTable
		    .getModel();

	    setProgress(0);

	    resultsMap = new ArrayList<ResultTableModel>();
	    // check queries
	    int count = 0;
	    Connection con = null;
	    for (int i = 0; i < model.getRowCount(); i++) {
		try {
		    if (!isCancelled()) {
			Object isChecked = model.getValueAt(i, 0);
			if (isChecked instanceof Boolean && (Boolean) isChecked) {

			    String queryCode = (String) model.getValueAt(i, 1);
			    String[] queryContents = doQuery(queryCode);

			    String queryDescription = queryContents[0];
			    String querySQL = queryContents[1];
			    String queryTitle = queryContents[2];
			    String querySubtitle = queryContents[3];

			    con = dbs.getJavaConnection();
			    thread = new RunStatementThread(con, querySQL);
			    logger.info(querySQL + ": " + queryContents[1]);
			    thread.start();
			    thread.join();
			    ResultTableModel result = new ResultTableModel(
				    queryCode, queryDescription, queryTitle,
				    querySubtitle, getFilters());
			    // result.setQueryTables(getValidationTables(query));
			    resultSetToTable(result, thread.getResult());
			    resultsMap.add(result);

			    count++;
			    setProgress(count * 100
				    / getCheckedQueriesCount(model));
			    thread = null;
			}
		    } else {
			break;
		    }
		} catch (InterruptedException e) {
		    System.out.println("Interrupted");
		    if (thread != null) {
			thread.cancel();
		    }
		    break;
		} catch (Exception ex) {
		    logger.error(ex.getMessage());
		    con.close();
		}
	    }
	    String html = showResultsAsHTML(resultsMap);
	    return html;
	}

	private String[] getFilters() {
	    String[] filters = new String[2];
	    filters[0] = Catalog.getCarreteraSelected();
	    filters[1] = Catalog.getConcelloSelected();
	    return filters;
	}

	private String getWhereClause(boolean hasWhere) throws SQLException {
	    String whereC;
	    if (!hasWhere) {
		whereC = "WHERE";
	    } else {
		whereC = " AND ";
	    }
	    if (!Catalog.getCarreteraSelected().equalsIgnoreCase(
		    Catalog.CARRETERA_ALL)) {
		whereC = whereC + " carretera = '"
			+ ((Carretera) carreteras.getSelectedItem()).getCode()
			+ "'";
	    } else {
		whereC = whereC + " 1=1 ";
	    }
	    if (!Catalog.getConcelloSelected().equalsIgnoreCase(
		    Catalog.CONCELLO_ALL)) {
		whereC = whereC + " AND municipio = '"
			+ ((Concello) concellos.getSelectedItem()).getCode()
			+ "'";
	    }
	    if (whereC.equalsIgnoreCase("WHERE 1=1 ")) {
		whereC = ""; // has no combobox selected
	    }
	    return whereC;
	}

	private String[] doQuery(String queryCode) throws Exception {
	    DBSession dbs = DBSession.getCurrentSession();

	    String whereClause = "codigo" + " = '" + queryCode + "'";
	    String[][] tableContent = dbs.getTable("queries", "queries",
		    whereClause);

	    String[] contents = new String[4];
	    String query;
	    query = tableContent[0][2];
	    boolean hasWhere = false;
	    if (tableContent[0][3].compareToIgnoreCase("SI") == 0) {
		hasWhere = true;
	    }
	    // description
	    contents[0] = tableContent[0][1];
	    // query
	    contents[1] = query.replaceAll("\\[\\[WHERE\\]\\]",
		    getWhereClause(hasWhere));
	    // title
	    contents[2] = tableContent[0][4];
	    // subtitle
	    contents[3] = tableContent[0][5];
	    return contents;
	}

	public void done() {
	    if (!isCancelled() && !sqlError) {
		try {
		    String str = get();
		    QueriesResultPanel resultPanel = new QueriesResultPanel();
		    resultPanel.open();
		    resultPanel.setResult(str);
		    resultPanel.setResultMap(resultsMap);
		    resultPanel.setFilters(getFilters());
		    PluginServices.getMDIManager().restoreCursor();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		} catch (ExecutionException e) {
		    e.printStackTrace();
		}
	    } else if (sqlError) {
		String message = error + "\n"
			+ PluginServices.getText(this, "checkSchema");
		JOptionPane.showMessageDialog(null, message,
			PluginServices.getText(this, "validationError"),
			JOptionPane.ERROR_MESSAGE);
	    }
	}

	private String showResultsAsHTML(ArrayList<ResultTableModel> resultMap) {
	    StringBuffer sf = new StringBuffer();

	    for (ResultTableModel result : resultMap) {
		sf.append("<h3 style=\"color: blue\">" + result.getCode()
			+ "  -  " + result.getDescription() + "</h3>");

		sf.append("<p>" + result.getQueryTables() + "</p>");
		sf.append(result.getHTML());
	    }
	    sf.append("</h2>");
	    sf.append("<hr>");

	    return sf.toString();
	}

	private int getCheckedQueriesCount(DefaultTableModel model) {
	    // get number of queries
	    int total = 0;
	    for (int i = 0; i < model.getRowCount(); i++) {
		Object isChecked = model.getValueAt(i, 0);
		if (isChecked instanceof Boolean && (Boolean) isChecked) {
		    total++;
		}
	    }
	    return total;
	}

	private void resultSetToTable(ResultTableModel result, ResultSet rs)
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

    }// QueriesTask Class

}// Main Class

