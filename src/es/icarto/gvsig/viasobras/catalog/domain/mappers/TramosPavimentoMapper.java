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
import es.icarto.gvsig.viasobras.catalog.domain.Tramos;
import es.icarto.gvsig.viasobras.catalog.domain.filters.TramosFilter;
import es.icarto.gvsig.viasobras.catalog.domain.filters.TramosFilterCarreteraConcello;

public class TramosPavimentoMapper extends DomainMapper {

    public static final String CARRETERA_FIELDNAME = "carretera";
    public static final String CONCELLO_FIELDNAME = "municipio";

    private static CachedRowSet tramos;

    public static Tramos findAll() throws SQLException {
	if (tramos != null) {
	    return new Tramos(TramosPavimentoMapper.toList(tramos));
	}
	Connection c = DomainMapper.getConnection();
	Statement stmt;
	try {
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT gid, carretera, municipio, tipopavime, origenpavi, finalpavim FROM inventario.tipo_pavimento ORDER BY origenpavi");
	    tramos = new CachedRowSetImpl();
	    tramos.populate(rs);
	    return new Tramos(TramosPavimentoMapper.toList(tramos));
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static Tramos findWhereCarretera(String carretera)
	    throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilter(CARRETERA_FIELDNAME, carretera));
	return new Tramos(TramosPavimentoMapper.toList(frs));
    }

    public static Tramos findWhereConcello(String concello)
	    throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilter(CONCELLO_FIELDNAME, concello));
	return new Tramos(TramosPavimentoMapper.toList(frs));
    }

    public static Tramos findWhereCarreteraAndConcello(
	    String carretera, String concello) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilterCarreteraConcello(carretera, concello));
	return new Tramos(TramosPavimentoMapper.toList(frs));
    }

    private static List<Tramo> toList(ResultSet rs) throws SQLException {
	List<Tramo> ts = new ArrayList<Tramo>();
	rs.beforeFirst();
	while (rs.next()) {
	    Tramo tramo = new Tramo();
	    tramo.setPkStart(rs.getDouble("origenpavi"));
	    tramo.setPkEnd(rs.getDouble("finalpavim"));
	    tramo.setCarretera(rs.getString("carretera"));
	    tramo.setConcello(rs.getString("municipio"));
	    tramo.setValue(rs.getString("tipopavime"));
	    ts.add(tramo);
	}
	return ts;
    }

}
