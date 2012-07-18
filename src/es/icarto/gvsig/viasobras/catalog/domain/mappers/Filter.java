package es.icarto.gvsig.viasobras.catalog.domain.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.FilteredRowSet;

import com.sun.rowset.FilteredRowSetImpl;

import es.icarto.gvsig.viasobras.catalog.domain.Tramo;
import es.icarto.gvsig.viasobras.catalog.domain.filters.TramosFilter;
import es.icarto.gvsig.viasobras.catalog.domain.filters.TramosFilterCarreteraConcello;
import es.icarto.gvsig.viasobras.catalog.domain.filters.TramosFilterPK;

public class Filter {

    public static List<Tramo> findWhereCarretera(CachedRowSet tramos,
	    String carretera)
		    throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilter(TramosMapperAbstract.CARRETERA_FIELDNAME, carretera));
	return toList(frs);
    }

    public static List<Tramo> findWhereCarreteraAndPK(CachedRowSet tramos,
	    String carretera,
	    double pkStart, double pkEnd) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilterPK(TramosMapperAbstract.CARRETERA_FIELDNAME, carretera,
		TramosMapperAbstract.PK_START_FIELDNAME, pkStart, TramosMapperAbstract.PK_END_FIELDNAME,
		pkEnd));
	return Filter.toList(frs);
    }

    public static List<Tramo> findWhereConcello(CachedRowSet tramos,
	    String concello) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilter(TramosMapperAbstract.CONCELLO_FIELDNAME, concello));
	return Filter.toList(frs);
    }

    public static List<Tramo> findWhereCarreteraAndConcello(
	    CachedRowSet tramos, String carretera, String concello)
		    throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilterCarreteraConcello(carretera, concello));
	return Filter.toList(frs);
    }

    public static List<Tramo> findAll(CachedRowSet tramos) throws SQLException {
	return Filter.toList(tramos);
    }

    private static List<Tramo> toList(ResultSet rs) throws SQLException {
	List<Tramo> ts = new ArrayList<Tramo>();
	rs.beforeFirst();
	while (rs.next()) {
	    Tramo tramo = new Tramo();
	    tramo.setId(Integer.toString(rs.getInt(TramosMapperAbstract.ID_FIELDNAME)));
	    tramo.setPosition(rs.getRow());
	    tramo.setPkStart(rs.getDouble(TramosMapperAbstract.PK_START_FIELDNAME));
	    tramo.setPkEnd(rs.getDouble(TramosMapperAbstract.PK_END_FIELDNAME));
	    tramo.setCarretera(rs.getString(TramosMapperAbstract.CARRETERA_FIELDNAME));
	    tramo.setConcello(rs.getString(TramosMapperAbstract.CONCELLO_FIELDNAME));
	    tramo.setValue(rs.getString(TramosMapperAbstract.CARACTERISTICA_FIELDNAME));
	    ts.add(tramo);
	}
	return ts;
    }

}
