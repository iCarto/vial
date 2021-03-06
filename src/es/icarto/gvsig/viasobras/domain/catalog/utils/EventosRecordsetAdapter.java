package es.icarto.gvsig.viasobras.domain.catalog.utils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.FilteredRowSet;

import com.sun.rowset.FilteredRowSetImpl;

import es.icarto.gvsig.viasobras.domain.catalog.Evento;
import es.icarto.gvsig.viasobras.domain.catalog.filters.FilterRecordsetByCarreteraAndConcello;
import es.icarto.gvsig.viasobras.domain.catalog.filters.FilterRecordsetByCarreteraAndPK;
import es.icarto.gvsig.viasobras.domain.catalog.filters.FilterRecordsetByField;
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
	int valueType = rs.getMetaData().getColumnType(
		EventosMapperAbstract.VALUE_FIELD_POSITION);
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
	    evento.setValueClass(getClassByType(valueType));
	    evento.setUpdatingDate(getDate(rs
		    .getDate(EventosMapperAbstract.FECHA_ACTUALIZACION_FIELDNAME)));
	    ts.add(evento);
	}
	return ts;
    }

    private static Class getClassByType(int valueType) {
	switch (valueType) {
	case Types.VARCHAR:
	    return String.class;
	case Types.DOUBLE:
	    return Double.class;
	case Types.INTEGER:
	    return Integer.class;
	default:
	    return Object.class;
	}
    }

    private static Date getDate(Date date) {
	/*
	 * BIG BUG in CachedRowSet: if one sets all values in a column to NULL
	 * and then serializes the CachedRowSet then deserializing and
	 * attempting an update would result in an Exception where apparently
	 * the column type is not being set so defaults to the type of the prior
	 * column:
	 * 
	 * org.postgresql.util.PSQLException: ERROR column "fecha_actualizacion"
	 * is of type character varying but expression is of type Date
	 * 
	 * http://books.google.es/books?id=4yjiwDMmOZMC&lpg=PA232&ots=9yQFXXfd5H&
	 * pg=PA232#v=onepage&q&f=false
	 * 
	 * So, the trick we do here is: in case the date is NULL, set it to
	 * first milisecond from epoch (1970-01-01). When reading back from
	 * database will check if the data is higher than this and, if not, will
	 * return a NULL. See TramosMapperAbstract.updateDate()
	 */
	if (date.before(new java.sql.Date(1))) {
	    return null;
	} else {
	    return date;
	}
    }

}
