package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.TableModel;

import es.icarto.gvsig.viasobras.catalog.domain.mappers.TramosPavimentoMapper;
import es.icarto.gvsig.viasobras.catalog.view.tables.TramosTableModel;

public class Tramos implements Iterable<Tramo> {

    private TableModel tm;
    private List<Tramo> tramos;

    public Tramos(List<Tramo> tramos) {
	this.tramos = tramos;
	this.tm = new TramosTableModel(this);
    }

    public TableModel getTableModel() {
	return this.tm;
    }

    public int size() {
	return this.tramos.size();
    }

    public Tramo getTramo(int index) {
	return tramos.get(index);
    }

    public void save() throws SQLException {
	TramosPavimentoMapper.save(this);
    }

    public Iterator<Tramo> iterator() {
	return tramos.iterator();
    }

    public void addTramo(Tramo tramo) {
	tramo.setStatus(Tramo.STATUS_INSERT);
	tramos.add(tramo);
    }

    public void removeTramo(int gid) {
	for (Tramo t : tramos) {
	    if (t.getId() == gid) {
		t.setStatus(Tramo.STATUS_DELETE);
		break;
	    }
	}
    }

}
