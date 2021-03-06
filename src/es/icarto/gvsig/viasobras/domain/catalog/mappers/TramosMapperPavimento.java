package es.icarto.gvsig.viasobras.domain.catalog.mappers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;

public class TramosMapperPavimento extends TramosMapperAbstract {

    // tramos are shared within all mappers of this kind
    private static CachedRowSet tramos;
    private static String tableName = "inventario.tipo_pavimento";

    public CachedRowSet getTramos() throws SQLException {
	if (tramos == null) {
	    tramos = load();
	}
	return tramos;
    }

    public void setInvalid() {
	tramos = null;
    }

    public CachedRowSet load() throws SQLException {
	try {
	    tramos = super.getCachedRowSet(tableName);
	    return tramos;
	} catch (SQLException e) {
	    e.printStackTrace();
	    tramos = null;
	    throw new SQLException(e);
	}
    }

    public int getLastAvailableID() throws SQLException {
	Connection c = DBFacade.getConnection();
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT nextval('inventario.tipo_pavimento_gid_seq') AS value");
	rs.next();
	return rs.getInt("value");
    }

}
