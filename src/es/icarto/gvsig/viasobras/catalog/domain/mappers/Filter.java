package es.icarto.gvsig.viasobras.catalog.domain.mappers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.FilteredRowSet;

import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.FilteredRowSetImpl;

import es.icarto.gvsig.viasobras.catalog.domain.Tramo;
import es.icarto.gvsig.viasobras.catalog.domain.filters.TramosFilter;
import es.icarto.gvsig.viasobras.catalog.domain.filters.TramosFilterCarreteraConcello;
import es.icarto.gvsig.viasobras.catalog.domain.filters.TramosFilterPK;

public class Filter {

    public static final String ID_FIELDNAME = "gid";
    public static final String CARRETERA_FIELDNAME = "carretera";
    public static final String CONCELLO_FIELDNAME = "municipio";
    public static final String PK_START_FIELDNAME = "origenpavi";
    public static final String PK_END_FIELDNAME = "finalpavim";
    public static final String CARACTERISTICA_FIELDNAME = "tipopavime";

    public static List<Tramo> findWhereCarretera(CachedRowSet tramos,
	    String carretera)
		    throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilter(CARRETERA_FIELDNAME, carretera));
	return toList(frs);
    }

    public static List<Tramo> findWhereCarreteraAndPK(CachedRowSet tramos,
	    String carretera,
	    double pkStart, double pkEnd) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilterPK(Filter.CARRETERA_FIELDNAME, carretera,
		Filter.PK_START_FIELDNAME, pkStart, Filter.PK_END_FIELDNAME,
		pkEnd));
	return Filter.toList(frs);
    }

    public static List<Tramo> findWhereConcello(CachedRowSet tramos,
	    String concello) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilter(Filter.CONCELLO_FIELDNAME, concello));
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

    public static CachedRowSet getCachedRowSet(String query, int[] primaryKeys)
	    throws SQLException {
	Connection c = DomainMapper.getConnection();
	Statement stmt = c.createStatement();
	ResultSet rs = stmt.executeQuery(query);
	CachedRowSet tramos = new CachedRowSetImpl();
	tramos.populate(rs);
	tramos.setUrl(DomainMapper.getURL());
	tramos.setUsername(DomainMapper.getUserName());
	tramos.setPassword(DomainMapper.getPwd());
	tramos.setCommand(query);
	tramos.setKeyColumns(primaryKeys);// set primary key
	return tramos;
    }

    public static List<Tramo> findAll(CachedRowSet tramos) throws SQLException {
	return Filter.toList(tramos);
    }

    private static List<Tramo> toList(ResultSet rs) throws SQLException {
	List<Tramo> ts = new ArrayList<Tramo>();
	rs.beforeFirst();
	while (rs.next()) {
	    Tramo tramo = new Tramo();
	    tramo.setId(Integer.toString(rs.getInt(Filter.ID_FIELDNAME)));
	    tramo.setPkStart(rs.getDouble(Filter.PK_START_FIELDNAME));
	    tramo.setPkEnd(rs.getDouble(Filter.PK_END_FIELDNAME));
	    tramo.setCarretera(rs.getString(Filter.CARRETERA_FIELDNAME));
	    tramo.setConcello(rs.getString(Filter.CONCELLO_FIELDNAME));
	    tramo.setValue(rs.getString(Filter.CARACTERISTICA_FIELDNAME));
	    ts.add(tramo);
	}
	return ts;
    }

}
