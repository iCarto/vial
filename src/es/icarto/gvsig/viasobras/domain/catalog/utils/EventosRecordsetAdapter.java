package es.icarto.gvsig.viasobras.domain.catalog.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.FilteredRowSet;

import com.sun.rowset.FilteredRowSetImpl;

import es.icarto.gvsig.viasobras.domain.catalog.Evento;
import es.icarto.gvsig.viasobras.domain.catalog.filters.FilterRecordsetByField;
import es.icarto.gvsig.viasobras.domain.catalog.filters.FilterRecordsetByCarreteraAndConcello;
import es.icarto.gvsig.viasobras.domain.catalog.filters.FilterRecordsetByCarreteraAndPK;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.EventosMapperAbstract;

public class EventosRecordsetAdapter {

    public static List<Evento> findWhereCarretera(CachedRowSet eventos,
	    String carretera)
		    throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	eventos.beforeFirst();
	frs.populate((ResultSet) eventos);
	frs.setFilter(new FilterRecordsetByField(
		EventosMapperAbstract.CARRETERA_FIELDNAME, carretera));
	return toList(frs);
    }

    public static List<Evento> findWhereCarreteraAndPK(CachedRowSet eventos,
	    String carretera,
	    double pkStart, double pkEnd) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	eventos.beforeFirst();
	frs.populate((ResultSet) eventos);
	frs.setFilter(new FilterRecordsetByCarreteraAndPK(
		EventosMapperAbstract.CARRETERA_FIELDNAME, carretera,
		EventosMapperAbstract.PK_FIELDNAME, pkStart,
		EventosMapperAbstract.PK_FIELDNAME, pkEnd));
	return EventosRecordsetAdapter.toList(frs);
    }

    public static List<Evento> findWhereConcello(CachedRowSet eventos,
	    String concello) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	eventos.beforeFirst();
	frs.populate((ResultSet) eventos);
	frs.setFilter(new FilterRecordsetByField(
		EventosMapperAbstract.CONCELLO_FIELDNAME, concello));
	return EventosRecordsetAdapter.toList(frs);
    }

    public static List<Evento> findWhereCarreteraAndConcello(
	    CachedRowSet eventos, String carretera, String concello)
		    throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	eventos.beforeFirst();
	frs.populate((ResultSet) eventos);
	frs.setFilter(new FilterRecordsetByCarreteraAndConcello(
		EventosMapperAbstract.CARRETERA_FIELDNAME, carretera,
		EventosMapperAbstract.CONCELLO_FIELDNAME, concello));
	return EventosRecordsetAdapter.toList(frs);
    }

    public static List<Evento> findAll(CachedRowSet eventos)
	    throws SQLException {
	return EventosRecordsetAdapter.toList(eventos);
    }

    private static List<Evento> toList(ResultSet rs) throws SQLException {
	List<Evento> ts = new ArrayList<Evento>();
	rs.beforeFirst();
	while (rs.next()) {
	    Evento evento = new Evento();
	    evento.setId(Integer.toString(rs
		    .getInt(EventosMapperAbstract.ID_FIELDNAME)));
	    evento.setPosition(rs.getRow());
	    evento.setCarretera(rs
		    .getString(EventosMapperAbstract.CARRETERA_FIELDNAME));
	    evento.setOrden(rs
		    .getString(EventosMapperAbstract.ORDEN_FIELDNAME));
	    evento.setConcello(rs
		    .getString(EventosMapperAbstract.CONCELLO_FIELDNAME));
	    evento.setPk(rs
		    .getDouble(EventosMapperAbstract.PK_FIELDNAME));
	    evento.setValue(rs
		    .getObject(EventosMapperAbstract.CARACTERISTICA_FIELDNAME));
	    evento.setUpdatingDate(rs
		    .getDate(EventosMapperAbstract.FECHA_ACTUALIZACION_FIELDNAME));
	    ts.add(evento);
	}
	return ts;
    }

}
