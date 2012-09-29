package es.icarto.gvsig.viasobras.domain.catalog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.TableModel;

import es.icarto.gvsig.viasobras.domain.catalog.mappers.EventosMapper;
import es.icarto.gvsig.viasobras.domain.catalog.utils.EventosComparator;
import es.icarto.gvsig.viasobras.domain.catalog.utils.EventosTableModel;
import es.icarto.gvsig.viasobras.domain.catalog.validation.EventoValidator;

public class Eventos implements Iterable<Evento> {

    private TableModel tm;
    private List<Evento> eventos;
    private List<Evento> eventosToValidate;
    private EventosMapper mapper;
    private EventoValidator eventoValidator;

    public Eventos(EventosMapper mapper, List<Evento> eventos) {
	this.mapper = mapper;
	this.eventos = eventos;
	Collections.sort(this.eventos, new EventosComparator());
	this.tm = new EventosTableModel(this);
	this.eventoValidator = new EventoValidator();
	this.eventosToValidate = new ArrayList<Evento>();
    }

    public TableModel getTableModel() {
	return this.tm;
    }

    public int size() {
	return this.eventos.size();
    }

    /**
     * Get evento as it is ordered in the eventos list
     * 
     * @param index
     * @return
     */
    public Evento getFromList(int index) {
	return eventos.get(index);
    }

    public void removeFromList(int index) {
	eventos.remove(index);
    }

    public Eventos save() throws SQLException {
	eventosToValidate.clear();
	return mapper.save(this);
    }

    public Iterator<Evento> iterator() {
	return eventos.iterator();
    }

    public int addEvento(Evento evento) {
	evento.setStatus(Evento.STATUS_INSERT);
	if (evento.getId() == Evento.NO_GID) {
	    // as we don't know what is the next ID in source, just create a
	    // random one. It will be ignored as INSERT queries should be leave
	    // the id blank for the DB to autocalculate
	    evento.setId("virtual-" + eventos.size() + "-"
		    + Double.toString(Math.random()));
	}
	eventos.add(evento);
	eventosToValidate.add(evento);
	return eventos.size() - 1;
    }

    public boolean removeEvento(String id) {
	if (id.equals(Evento.NO_GID)) {
	    return false;
	}
	for (Evento e : eventos) {
	    if (e.getId().equals(id)) {
		e.setStatus(Evento.STATUS_DELETE);
		return true;
	    }
	}
	return false;
    }

    public boolean updateEvento(String id, int propertyIndex,
	    Object propertyValue) {
	Evento e = this.getEvento(id);
	if (e.setProperty(propertyIndex, propertyValue)) {
	    int status = e.getStatus();
	    if ((status == Evento.STATUS_ORIGINAL)
		    || (status == Evento.STATUS_UPDATE)) {
		// if evento has INSERT status we should not set as UPDATE,
		// as it will affect how mapper will process it
		this.getEvento(id).setStatus(Evento.STATUS_UPDATE);
	    }
	    eventosToValidate.add(e);
	    return true;
	}
	return false;
    }

    private Evento getEvento(String id) {
	for (Evento e : eventos) {
	    if (e.getId().equals(id)) {
		return e;
	    }
	}
	return null;
    }

    public boolean canSaveEventos() {
	if (eventosToValidate.size() == 0) {
	    return true;
	}
	for (Evento e : eventosToValidate) {
	    if (!eventoValidator.validate(e)) {
		return false;
	    }
	}
	return true;
    }

}
