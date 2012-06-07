package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class TramosTableModel extends AbstractTableModel {

    private ResultSet rs;
    private ResultSetMetaData metadata;
    private int rowCount;
    private int colCount;
    public static int NO_COL_NUMBER = -1;
    public static int NO_ROW_NUMBER = -1;

    public TramosTableModel(ResultSet rs) {
	super();
	this.rs = rs;
	initMetaData();
    }

    private void initMetaData() {
	try {
	    this.metadata = this.rs.getMetaData();
	    this.colCount = this.metadata.getColumnCount();
	    this.rs.beforeFirst();
	    while (rs.next()) {
		this.rowCount++;
	    }
	    rs.beforeFirst();
	} catch (SQLException e) {
	    e.printStackTrace();
	    rowCount = NO_ROW_NUMBER;
	    colCount = NO_COL_NUMBER;
	    metadata = null;
	}
    }

    public void close() {
	try {
	    rs.getStatement().close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    /** Automatically close when we're garbage collected */
    protected void finalize() {
	close();
    }

    public String getColumnName(int column) {
	try {
	    return this.metadata.getColumnName(column + 1);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public int getColumnCount() {
	return colCount;
    }

    public int getRowCount() {
	return rowCount;
    }

    public Object getValueAt(int rowIndex, int colIndex) {
	try {
	    this.rs.absolute(rowIndex + 1);
	    Object o = this.rs.getObject(colIndex + 1);
	    if (o == null) {
		return null;
	    } else {
		return o.toString();
	    }
	} catch (SQLException e) {
	    return e.toString();
	}
    }

    public boolean isCellEditable(int arg0, int arg1) {
	// if return true, evaluate what to do with addTableModelListener,
	// closeTableModelListener & setValueAt methods
	return false;
    }

    /**
     * As cell is set to not editable (see {@link #isCellEditable(int, int)},
     * this method will do nothing.
     * 
     * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
     */
    public void addTableModelListener(TableModelListener arg0) {
    }

    /**
     * As cell is set to not editable (see {@link #isCellEditable(int, int)},
     * this method will do nothing.
     */
    public void removeTableModelListener(TableModelListener arg0) {
    }

    /**
     * As cell is set to not editable (see {@link #isCellEditable(int, int)},
     * this method will do nothing.
     */
    public void setValueAt(Object value, int row, int col) {
	System.out.println("Calling setValueAt row " + row + ", column " + col);
    }

}
