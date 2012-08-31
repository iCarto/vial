package es.icarto.gvsig.viasobras.domain.catalog.utils.tramos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import es.icarto.gvsig.viasobras.domain.catalog.Tramo;
import es.icarto.gvsig.viasobras.domain.catalog.Tramos;


public class TramosTableModel extends AbstractTableModel {

    private Tramos tramos;
    private Tramo metadata;
    private int rowCount;
    private int colCount;
    public static int NO_COL_NUMBER = -1;
    public static int NO_ROW_NUMBER = -1;
    public List<Integer> rows;

    public TramosTableModel(Tramos tramos) {
	super();
	this.tramos = tramos;
	initMetaData();
    }

    private void initMetaData() {
	rows = new ArrayList<Integer>();
	if (tramos.size() > 0) {
	    this.metadata = tramos.getFromList(0);
	    this.rowCount = tramos.size();
	    this.colCount = metadata.getNumberOfProperties();
	    for (int i = 0; i < rowCount; i++) {
		rows.add(i);
	    }
	} else {
	    this.metadata = new Tramo();
	    this.colCount = metadata.getNumberOfProperties();
	    this.rowCount = 0;
	}
    }

    public String getColumnName(int column) {
	return metadata.getPropertyName(column);
    }

    public int getColumnCount() {
	return colCount;
    }

    public Class getColumnClass(int colIndex) {
	return this.metadata.getClass(colIndex);
    }

    public int getRowCount() {
	return rowCount;
    }

    public boolean isCellEditable(int arg0, int arg1) {
	return true;
    }

    public Object getValueAt(int row, int col) {
	int rowInTramos = rows.get(row);
	return tramos.getFromList(rowInTramos).getPropertyValue(col);
    }

    public void setValueAt(Object value, int row, int col) {
	int rowInTramos = rows.get(row);
	String id = tramos.getFromList(rowInTramos).getId();
	tramos.updateTramo(id, col, value);
	this.fireTableCellUpdated(row, col);
    }

    public void addTramo(Tramo t) {
	int idx = tramos.addTramo(t);
	rowCount++;
	rows.add(idx);
	this.fireTableRowsInserted(rowCount, rowCount);
    }

    public String deleteTramo(int row) {
	String id = tramos.getFromList(rows.get(row)).getId();
	tramos.removeTramo(id);
	this.fireTableRowsDeleted(row, row);
	rows.remove(row);
	rowCount--;
	return id;
    }

    public boolean saveChanges() throws SQLException {
	this.tramos = tramos.save();
	initMetaData();
	return true;
    }

}
