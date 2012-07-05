package es.icarto.gvsig.viasobras.queries;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.jeta.forms.components.panel.FormPanel;

import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class QueriesPanel extends gvWindow {

    private static final String DEFAULT_FILTER = "--TODOS--";

    private FormPanel formBody;
    private JScrollPane scrollPane;

    private JButton runQueriesB;
    private JTable queriesTable;

    private boolean isReport;

    private static final Logger logger = Logger.getLogger(QueriesPanel.class);

    private DBSession dbs;

    private JComboBox carretera;

    private JComboBox concello;

    public QueriesPanel(boolean report) {
	super(600, 500, true);
	this.isReport = report;
	if (isReport) {
	    this.setTitle("Informes");
	} else {
	    this.setTitle("Consultas");
	}
	formBody = new FormPanel("consultas.xml");
	formBody.setVisible(true);
	scrollPane = new JScrollPane(queriesTable);
	this.add(formBody, BorderLayout.CENTER);
	this.add(scrollPane, BorderLayout.CENTER);
	dbs = DBSession.getCurrentSession();
	initWidgets();
	initListeners();
    }

    private void initListeners() {
	runQueriesB.addActionListener(new RunQueriesListener());
    }

    public void initWidgets() {
	runQueriesB = (JButton) formBody.getComponentByName("runQueriesButton");
	queriesTable = (JTable) formBody.getComponentByName("queriesTable");

	carretera = (JComboBox) formBody.getComponentByName("carretera");
	concello = (JComboBox) formBody.getComponentByName("concello");

	fillComboBoxes();
	initQueriesTable();
	fillQueriesTable();
    }

    private void fillComboBoxes() {
	carretera.addItem(new String(DEFAULT_FILTER));
	concello.addItem(new String(DEFAULT_FILTER));
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
	    filters[0] = "0101";
	    filters[1] = "27001";
	    return filters;
	}

	private String getWhereClause(boolean hasWhere) throws SQLException {
	    return "";
	    // String whereC;
	    // if (!hasWhere) {
	    // whereC = "WHERE";
	    // } else {
	    // whereC = " AND ";
	    // }
	    // if (tramoSelected.compareToIgnoreCase(DEFAULT_FILTER) != 0) {
	    // whereC = whereC + " " + DBNames.FIELD_TRAMO + " = " + "'"
	    // + getTramoId() + "'";
	    // }
	    // if (ucSelected.compareToIgnoreCase(DEFAULT_FILTER) != 0) {
	    // whereC = whereC + " AND " + DBNames.FIELD_UC + " = " + "'"
	    // + getUcId() + "'";
	    // }
	    // if (ayuntamientoSelected.compareToIgnoreCase(DEFAULT_FILTER) !=
	    // 0) {
	    // whereC = whereC + " AND " + DBNames.FIELD_AYUNTAMIENTO + " = "
	    // + "'" + getAyuntamientoId() + "'";
	    // }
	    // if (whereC.equalsIgnoreCase("WHERE")) {
	    // whereC = ""; // has no combobox selected
	    // }
	    // if (whereC.equalsIgnoreCase(" AND ")) {
	    // whereC = "AND 1=1";
	    // }
	    // return whereC;
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
		    QueriesResultPanel resultPanel;
		    // if (councilCB.getSelectedIndex() > 0) {
		    // resultPanel = new EIELValidationResultPanel(councilCB
		    // .getSelectedItem().toString());
		    // } else {
		    resultPanel = new QueriesResultPanel();
		    // }
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

