package es.icarto.gvsig.viasobras.catalog.domain.mappers;

import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

public class DomainMapper {

    private static Connection c;

    public static void setConnection(Connection con) {
	c = con;
    }

    public static Connection getConnection() throws SQLException {
	if ((c == null) || (c.isClosed())) {
	    throw new PSQLException("Connection error",
		    PSQLState.CONNECTION_FAILURE);
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
