package es.icarto.gvsig.viasobras.catalog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class Carreteras implements Iterable<Carretera> {

    private ResultSet rs;

    public Carreteras(ResultSet rs) {
	this.rs = rs;
    }

    public static Carreteras findAll() {
	DBSession dbs = DBSession.getCurrentSession();
	Connection c = dbs.getJavaConnection();
	Statement stmt;
	try {
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT * FROM inventario.carreteras");
	    return new Carreteras(rs);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public Iterator<Carretera> iterator() {
	return new Iterator<Carretera>() {

	    public boolean hasNext() {
		try {
		    return rs.next();
		} catch (SQLException e) {
		    return false;
		}
	    }

	    public Carretera next() {
		try {
		    return new Carretera(rs.getString("código"),
			    rs.getString("código_pr"));
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
