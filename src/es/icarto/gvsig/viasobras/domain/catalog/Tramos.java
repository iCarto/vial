package es.icarto.gvsig.viasobras.domain.catalog;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.TableModel;

import es.icarto.gvsig.viasobras.domain.catalog.mappers.TramosMapper;
import es.icarto.gvsig.viasobras.domain.catalog.utils.tramos.TramosComparator;
import es.icarto.gvsig.viasobras.domain.catalog.utils.tramos.TramosTableModel;

public class Tramos implements Iterable<Tramo> {

    private TableModel tm;
    private List<Tramo> tramos;
    private TramosMapper mapper;

    public Tramos(TramosMapper mapper, List<Tramo> tramos) {
	this.mapper = mapper;
	this.tramos = tramos;
	Collections.sort(this.tramos, new TramosComparator());
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

    public Tramos save() throws SQLException {
	return mapper.save(this);
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

    public void updateTramo(String id, int propertyIndex, Object propertyValue) {
	Tramo t = this.getTramo(id);
	t.setProperty(propertyIndex, propertyValue);
	int status = t.getStatus();
	if ((status == Tramo.STATUS_ORIGINAL)
		|| (status == Tramo.STATUS_UPDATE)) {
	    // if tramo has INSERT status we should not set as UPDATE,
	    // as it will affect how mapper will process it
	    this.getTramo(id).setStatus(Tramo.STATUS_UPDATE);
	}
    }

    private Tramo getTramo(String id) {
	for (Tramo t : tramos) {
	    if (t.getId().equals(id)) {
		return t;
	    }
	}
	return null;
    }

}
