package es.icarto.gvsig.viasobras.domain.catalog.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import es.icarto.gvsig.viasobras.domain.catalog.Evento;
import es.icarto.gvsig.viasobras.domain.catalog.Eventos;


public class EventosTableModel extends AbstractTableModel {

    private Eventos eventos;
    private Evento metadata;
    private int rowCount;
    private int colCount;
    public static int NO_COL_NUMBER = -1;
    public static int NO_ROW_NUMBER = -1;
    public List<Integer> rows;

    public EventosTableModel(Eventos eventos) {
	super();
	this.eventos = eventos;
	initMetaData();
    }

    private void initMetaData() {
	rows = new ArrayList<Integer>();
	if (eventos.size() > 0) {
	    this.metadata = eventos.getFromList(0);
	    this.rowCount = eventos.size();
	    this.colCount = metadata.getNumberOfProperties();
	    for (int i = 0; i < rowCount; i++) {
		rows.add(i);
	    }
	} else {
	    this.metadata = new Evento();
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
	int rowInEventos = rows.get(row);
	return eventos.getFromList(rowInEventos).getPropertyValue(col);
    }

    public void setValueAt(Object value, int row, int col) {
	int rowInEventos = rows.get(row);
	String id = eventos.getFromList(rowInEventos).getId();
	eventos.updateEvento(id, col, value);
	this.fireTableCellUpdated(row, col);
    }

    public void addEvento(Evento e) {
	int idx = eventos.addEvento(e);
	rowCount++;
	rows.add(idx);
	this.fireTableRowsInserted(rowCount, rowCount);
    }

    public String deleteEvento(int row) {
	String id = eventos.getFromList(rows.get(row)).getId();
	eventos.removeEvento(id);
	this.fireTableRowsDeleted(row, row);
	rows.remove(row);
	rowCount--;
	return id;
    }

    public boolean saveChanges() throws SQLException {
	this.eventos = eventos.save();
	initMetaData();
	return true;
    }

    public boolean canSaveEventos() {
	return this.eventos.canSaveEventos();
    }

}
