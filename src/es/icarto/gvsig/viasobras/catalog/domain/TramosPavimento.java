package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.ResultSet;

import javax.swing.table.TableModel;

public class TramosPavimento {

    private ResultSet rs;
    private TableModel tm;

    public TramosPavimento(ResultSet rs) {
	this.rs = rs;
    }

    public TableModel getTableModel() {
	if (this.tm == null) {
	    this.tm = new TramosPavimentoTableModel(this.rs);
	}
	return this.tm;
    }

}
