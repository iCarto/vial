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

    /**
     * Get tramo as it is ordered in the tramos list
     * 
     * @param index
     * @return
     */
    public Tramo getFromList(int index) {
	return tramos.get(index);
    }

    public void removeFromList(int index) {
	tramos.remove(index);
    }

    public void save() throws SQLException {
	TramosPavimentoMapper.save(this);
    }

    public Iterator<Tramo> iterator() {
	return tramos.iterator();
    }

    public int addTramo(Tramo tramo) {
	tramo.setStatus(Tramo.STATUS_INSERT);
	if (tramo.getId() == Tramo.NO_GID) {
	    // as we don't know what is the next ID in source, just create a
	    // random one. It will be ignored as INSERT queries should be leave
	    // the id blank for the DB to autocalculate
	    tramo.setId("virtual-" + tramos.size() + "-"
		    + Double.toString(Math.random()));
	}
	tramos.add(tramo);
	return tramos.size() - 1;
    }

    /**
     * Returns tramo by its gid
     * 
     * @param id
     * @return
     */
    public Tramo getTramo(int id) {
	for (Tramo t : tramos) {
	    if (t.getId().equals(id)) {
		return t;
	    }
	}
	return null;
    }

    public boolean removeTramo(String id) {
	if (id.equals(Tramo.NO_GID)) {
	    return false;
	}
	for (Tramo t : tramos) {
	    if (t.getId().equals(id)) {
		t.setStatus(Tramo.STATUS_DELETE);
		return true;
	    }
	}
	return false;
    }
}
