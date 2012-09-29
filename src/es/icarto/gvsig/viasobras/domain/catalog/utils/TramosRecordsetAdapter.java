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

import es.icarto.gvsig.viasobras.domain.catalog.Tramo;
import es.icarto.gvsig.viasobras.domain.catalog.filters.FilterRecordsetByCarreteraAndConcello;
import es.icarto.gvsig.viasobras.domain.catalog.filters.FilterRecordsetByCarreteraAndPK;
import es.icarto.gvsig.viasobras.domain.catalog.filters.FilterRecordsetByField;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.TramosMapperAbstract;

public class TramosRecordsetAdapter {

    public static List<Tramo> findWhereCarretera(CachedRowSet tramos,
	    String carretera)
		    throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new FilterRecordsetByField(TramosMapperAbstract.CARRETERA_FIELDNAME, carretera));
	return toList(frs);
    }

    public static List<Tramo> findWhereCarreteraAndPK(CachedRowSet tramos,
	    String carretera,
	    double pkStart, double pkEnd) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new FilterRecordsetByCarreteraAndPK(TramosMapperAbstract.CARRETERA_FIELDNAME, carretera,
		TramosMapperAbstract.PK_START_FIELDNAME, pkStart, TramosMapperAbstract.PK_END_FIELDNAME,
		pkEnd));
	return TramosRecordsetAdapter.toList(frs);
    }

    public static List<Tramo> findWhereConcello(CachedRowSet tramos,
	    String concello) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new FilterRecordsetByField(TramosMapperAbstract.CONCELLO_FIELDNAME, concello));
	return TramosRecordsetAdapter.toList(frs);
    }

    public static List<Tramo> findWhereCarreteraAndConcello(
	    CachedRowSet tramos, String carretera, String concello)
		    throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new FilterRecordsetByCarreteraAndConcello(
		TramosMapperAbstract.CARRETERA_FIELDNAME, carretera,
		TramosMapperAbstract.CONCELLO_FIELDNAME, concello));
	return TramosRecordsetAdapter.toList(frs);
    }

    public static List<Tramo> findAll(CachedRowSet tramos) throws SQLException {
	return TramosRecordsetAdapter.toList(tramos);
    }

    private static List<Tramo> toList(ResultSet rs) throws SQLException {
	List<Tramo> ts = new ArrayList<Tramo>();
	rs.beforeFirst();
	int valueType = rs.getMetaData().getColumnType(
		TramosMapperAbstract.VALUE_FIELD_POSITION);
	while (rs.next()) {
	    Tramo tramo = new Tramo();
	    tramo.setId(Integer.toString(rs.getInt(TramosMapperAbstract.ID_FIELDNAME)));
	    tramo.setPosition(rs.getRow());
	    tramo.setCarretera(rs.getString(TramosMapperAbstract.CARRETERA_FIELDNAME));
	    tramo.setOrdenTramo(rs
		    .getString(TramosMapperAbstract.ORDEN_TRAMO_FIELDNAME));
	    tramo.setConcello(rs.getString(TramosMapperAbstract.CONCELLO_FIELDNAME));
	    tramo.setPkStart(rs
		    .getDouble(TramosMapperAbstract.PK_START_FIELDNAME));
	    tramo.setPkEnd(rs.getDouble(TramosMapperAbstract.PK_END_FIELDNAME));
	    tramo.setValue(getValueByType(rs, valueType));
	    tramo.setValueClass(getClassByType(valueType));
	    tramo.setUpdatingDate(getDate(rs
		    .getDate(TramosMapperAbstract.FECHA_ACTUALIZACION_FIELDNAME)));
	    ts.add(tramo);
	}
	return ts;
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

    private static Object getValueByType(ResultSet rs, int valueType)
	    throws SQLException {
	switch (valueType) {
	case Types.VARCHAR:
	    return rs.getString(TramosMapperAbstract.CARACTERISTICA_FIELDNAME);
	case Types.DOUBLE:
	    Double d = rs
	    .getDouble(TramosMapperAbstract.CARACTERISTICA_FIELDNAME);
	    if (rs.wasNull()) {
		return null;
	    } else {
		return d;
	    }
	default:
	    return rs.getObject(TramosMapperAbstract.CARACTERISTICA_FIELDNAME);
	}

    }

}
