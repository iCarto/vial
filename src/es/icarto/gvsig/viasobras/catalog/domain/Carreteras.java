package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

public class Carreteras extends DomainMapper implements Iterable<Carretera> {

    private ResultSet rs;

    public Carreteras(ResultSet rs) {
	this.rs = rs;
    }

    public static Carreteras findAll() {
	Connection c = DomainMapper.getConnection();
	Statement stmt;
	try {
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT codigo, código_pr FROM inventario.rede_carreteras");
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
		    return new Carretera(rs.getString("codigo"),
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
