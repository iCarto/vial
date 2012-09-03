package es.icarto.gvsig.viasobras.domain.catalog.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.FilteredRowSet;

import com.sun.rowset.FilteredRowSetImpl;

import es.icarto.gvsig.viasobras.domain.catalog.Tramo;
import es.icarto.gvsig.viasobras.domain.catalog.filters.FilterTramosByAnyField;
import es.icarto.gvsig.viasobras.domain.catalog.filters.FilterTramosByCarreteraAndConcello;
import es.icarto.gvsig.viasobras.domain.catalog.filters.FilterTramosByCarreteraAndPK;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.TramosMapperAbstract;

public class TramosRecordsetAdapter {

    public static List<Tramo> findWhereCarretera(CachedRowSet tramos,
	    String carretera)
		    throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new FilterTramosByAnyField(TramosMapperAbstract.CARRETERA_FIELDNAME, carretera));
	return toList(frs);
    }

    public static List<Tramo> findWhereCarreteraAndPK(CachedRowSet tramos,
	    String carretera,
	    double pkStart, double pkEnd) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new FilterTramosByCarreteraAndPK(TramosMapperAbstract.CARRETERA_FIELDNAME, carretera,
		TramosMapperAbstract.PK_START_FIELDNAME, pkStart, TramosMapperAbstract.PK_END_FIELDNAME,
		pkEnd));
	return TramosRecordsetAdapter.toList(frs);
    }

    public static List<Tramo> findWhereConcello(CachedRowSet tramos,
	    String concello) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new FilterTramosByAnyField(TramosMapperAbstract.CONCELLO_FIELDNAME, concello));
	return TramosRecordsetAdapter.toList(frs);
    }

    public static List<Tramo> findWhereCarreteraAndConcello(
	    CachedRowSet tramos, String carretera, String concello)
		    throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new FilterTramosByCarreteraAndConcello(
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
	    tramo.setValue(rs
		    .getObject(TramosMapperAbstract.CARACTERISTICA_FIELDNAME));
	    tramo.setUpdatingDate(rs
		    .getDate(TramosMapperAbstract.FECHA_ACTUALIZACION_FIELDNAME));
	    ts.add(tramo);
	}
	return ts;
    }

}
