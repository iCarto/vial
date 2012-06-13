package es.icarto.gvsig.viasobras.catalog.domain;

import java.util.List;

import javax.swing.table.TableModel;

import es.icarto.gvsig.viasobras.catalog.view.tables.TramosTableModel;

public class Tramos {

    private TableModel tm;
    private List<Tramo> tramos;

    public Tramos(List<Tramo> tramos) {
	this.tramos = tramos;
    }

    public TableModel getTableModel() {
	if (this.tm == null) {
	    this.tm = new TramosTableModel(this.tramos);
	}
	return this.tm;
    }

}
