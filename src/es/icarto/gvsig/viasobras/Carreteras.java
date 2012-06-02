package es.icarto.gvsig.viasobras;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class Carreteras {

    public static ResultSet findAll() {
	DBSession dbs = DBSession.getCurrentSession();
	Connection c = dbs.getJavaConnection();
	Statement stmt;
	try {
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT * FROM inventario.carreteras");
	    return rs;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

}
