package es.icarto.gvsig.viasobras.queries.reports;

import javax.swing.table.DefaultTableModel;

public class TableModelQueries extends DefaultTableModel {

    public boolean isCellEditable(int row, int col) {
	return false;
    }

}
