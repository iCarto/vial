package es.icarto.gvsig.viasobras.queries;

import javax.swing.table.DefaultTableModel;

public class QueriesTableModel extends DefaultTableModel {

    public boolean isCellEditable(int row, int col) {
	return false;
    }

}
