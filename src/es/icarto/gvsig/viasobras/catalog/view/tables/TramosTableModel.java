package es.icarto.gvsig.viasobras.catalog.view.tables;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import es.icarto.gvsig.viasobras.catalog.domain.Tramo;

public class TramosTableModel extends AbstractTableModel {

    private List<Tramo> tramos;
    private Tramo metadata;
    private int rowCount;
    private int colCount;
    public static int NO_COL_NUMBER = -1;
    public static int NO_ROW_NUMBER = -1;

    public TramosTableModel(List<Tramo> tramos) {
	super();
	this.tramos = tramos;
	initMetaData();
    }

    private void initMetaData() {
	if (tramos.size() > 0) {
	    this.rowCount = tramos.size();
	    this.colCount = tramos.get(0).getNumberOfProperties();
	    this.metadata = tramos.get(0);
	} else {
	    this.colCount = NO_COL_NUMBER;
	    this.rowCount = NO_ROW_NUMBER;
	    this.metadata = null;
	}
    }

    public String getColumnName(int column) {
	return metadata.getPropertyName(column);
    }

    public int getColumnCount() {
	return colCount;
    }

    public int getRowCount() {
	return rowCount;
    }

    public Object getValueAt(int rowIndex, int colIndex) {
	return tramos.get(rowIndex).getPropertyValue(colIndex);
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
