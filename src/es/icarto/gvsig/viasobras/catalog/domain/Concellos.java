package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

public class Concellos extends DomainMapper implements Iterable<Concello> {

    private ResultSet rs;

    public Concellos(ResultSet rs) {
	this.rs = rs;
    }

    public static Concellos findAll() {
	Connection c = DomainMapper.getConnection();
	Statement stmt;
	try {
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT * FROM info_base.concellos");
	    return new Concellos(rs);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public boolean next() {
	try {
	    if (!rs.next()) {
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
		    return rs.next();
		} catch (SQLException e) {
		    return false;
		}
	    }

	    public Concello next() {
		try {
		    return new Concello(rs.getInt("concellos_"),
			    rs.getString("concellos1"));
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
