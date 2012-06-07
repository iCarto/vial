package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.ResultSet;

import javax.swing.table.TableModel;

public class TramosPlataforma {

    private ResultSet rs;
    private TableModel tm;

    public TramosPlataforma(ResultSet rs) {
	this.rs = rs;
    }

    public TableModel getTableModel() {
	if (this.tm == null) {
	    this.tm = new TramosTableModel(this.rs);
	}
	return this.tm;
    }

}
