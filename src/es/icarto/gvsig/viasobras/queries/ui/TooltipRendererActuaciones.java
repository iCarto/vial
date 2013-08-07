package es.icarto.gvsig.viasobras.queries.ui;

import java.awt.Component;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class TooltipRendererActuaciones extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table,
	    Object value,
	    boolean isSelected,
	    boolean hasFocus,
	    int row, int column) {
	super.getTableCellRendererComponent(table, value, isSelected,
		hasFocus, row, column);
	try {
	    String[][] tableContent = DBSession.getCurrentSession().getTable("consultas_actuaciones", 
		    "consultas", "codigo = '" + table.getValueAt(row, 0).toString() + "'");
	    setToolTipText(tableContent[0][1]);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return this;
    }  
}
