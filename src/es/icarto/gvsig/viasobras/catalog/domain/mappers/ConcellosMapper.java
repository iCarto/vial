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

import es.icarto.gvsig.viasobras.catalog.domain.Concello;
import es.icarto.gvsig.viasobras.catalog.domain.Concellos;
import es.icarto.gvsig.viasobras.catalog.domain.filters.FilterConcelloByCarretera;

public class ConcellosMapper extends DBFacade {

    public static String CODE = "codigo";

    /*
     * As concellos is not going to change usually, is safe to asume that the
     * same concellos are going to be the same during the whole session.
     */
    private static CachedRowSet concellos;

    public static Concellos findAll() throws SQLException {
	if (concellos != null) {
	    return new Concellos(ConcellosMapper.toList(concellos));
	}
	Connection c = DBFacade.getConnection();
	Statement stmt;
	try {
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT codigo, nombre FROM info_base.concellos ORDER BY nombre");
	    concellos = new CachedRowSetImpl();
	    concellos.populate(rs);
	    return new Concellos(ConcellosMapper.toList(concellos));
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static Concellos findWhereCarretera(String carretera)
	    throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	if (concellos == null) {
	    ConcellosMapper.findAll();
	}
	concellos.beforeFirst();
	frs.populate((ResultSet) concellos);
	frs.setFilter(new FilterConcelloByCarretera(carretera));
	return new Concellos(ConcellosMapper.toList(frs));
    }

    private static List<Concello> toList(ResultSet rs) throws SQLException {
	List<Concello> cs = new ArrayList<Concello>();
	rs.beforeFirst();
	while (rs.next()) {
	    Concello concello = new Concello(rs.getString("codigo"),
		    rs.getString("codigo") + " - " + rs.getString("nombre"));
	    cs.add(concello);
	}
	return cs;
    }

}
