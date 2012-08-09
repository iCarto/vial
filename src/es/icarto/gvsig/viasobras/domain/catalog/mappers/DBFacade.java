package es.icarto.gvsig.viasobras.domain.catalog.mappers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

public class DBFacade {

    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private static Connection c;
    private static Properties metadata;

    public static void setConnection(Connection con, Properties p)
	    throws SQLException {
	c = con;
	metadata = p;
    }

    public static Connection getConnection() throws SQLException {
	if ((c == null) || (c.isClosed())) {
	    throw new PSQLException("Connection error",
		    PSQLState.CONNECTION_FAILURE);
	}
	return c;
    }

    public static String getUserName() {
	return metadata.getProperty("username");
    }

    public static String getURL() {
	return metadata.getProperty("url");
    }

    public static String getPwd() throws SQLException {
	return metadata.getProperty("password");
    }

    public void close() {
	try {
	    c.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

}
