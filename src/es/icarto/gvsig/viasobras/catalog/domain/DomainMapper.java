package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.Connection;
import java.sql.SQLException;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class DomainMapper {

    private static Connection c;

    public static void setConnection(Connection con) {
	c = con;
    }

    public static Connection getConnection() {
	if (c == null) {
	    DBSession dbs = DBSession.getCurrentSession();
	    c = dbs.getJavaConnection();
	}
	return c;
    }

    public void close() {
	try {
	    c.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }
}
