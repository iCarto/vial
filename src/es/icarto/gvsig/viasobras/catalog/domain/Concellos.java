package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.FilteredRowSet;

import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.FilteredRowSetImpl;

public class Concellos extends DomainMapper implements Iterable<Concello> {

    /*
     * As concellos is not going to change usually, is safe to asume that the
     * same concellos are going to be the same during the whole session. So:
     * 
     * - static concellosAll: keep all the concellos in DB
     * 
     * - concellosSelection: keep the filtered concellos depending on the
     * carretera selected.
     */
    private static CachedRowSet concellosAll;
    private CachedRowSet concellosSelection;

    public Concellos(ResultSet rs) {
	this.concellosSelection = (CachedRowSet) rs;
    }

    public static Concellos findAll() {
	if (concellosAll != null) {
	    return new Concellos(concellosAll);
	}
	Connection c = DomainMapper.getConnection();
	Statement stmt;
	try {
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT codigo, nombre FROM info_base.concellos ORDER BY nombre");
	    concellosAll = new CachedRowSetImpl();
	    concellosAll.populate(rs);
	    return new Concellos(concellosAll);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static Concellos findWhereCarretera(String carretera) {
	try {
	    FilteredRowSet frs = new FilteredRowSetImpl();
	    concellosAll.beforeFirst();
	    frs.populate((ResultSet) concellosAll);
	    frs.setFilter(new CarreteraFilter(carretera));
	    return new Concellos(frs);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public boolean next() {
	try {
	    if (!concellosSelection.next()) {
		return false;
	    }
	    return true;
	} catch (SQLException e) {
	    return false;
	}
    }

    public Iterator<Concello> iterator() {
	return new Iterator<Concello>() {

	    public boolean hasNext() {
		try {
		    return concellosSelection.next();
		} catch (SQLException e) {
		    return false;
		}
	    }

	    public Concello next() {
		try {
		    return new Concello(concellosSelection.getString("codigo"),
			    concellosSelection.getString("nombre"));
		} catch (SQLException e) {
		    return null;
		}
	    }

	    public void remove() {
		// do nothing
	    }

	};
    }

}
