package es.icarto.gvsig.viasobras.domain.catalog.mappers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;


public class EventosMapperAforos extends EventosMapperAbstract {

    private static CachedRowSet eventos;
    private static String tableName = "inventario.aforos";

    @Override
    public CachedRowSet getEventos() throws SQLException {
	if (eventos == null) {
	    eventos = load();
	}
	return eventos;
    }

    @Override
    public CachedRowSet load() throws SQLException {
	try {
	    eventos = super.getCachedRowSet(tableName);
	    return eventos;
	} catch (SQLException e) {
	    e.printStackTrace();
	    eventos = null;
	    throw new SQLException(e);
	}
    }

    @Override
    public int getLastAvailableID() throws SQLException {
	Connection c = DBFacade.getConnection();
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT nextval('inventario.aforos_gid_seq') AS value");
	rs.next();
	return rs.getInt("value");
    }

}
