package es.icarto.gvsig.viasobras.domain.catalog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;

import es.icarto.gvsig.viasobras.domain.catalog.mappers.DBFacade;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.TramosMapperAbstract;

public class TramosMapperCotas extends TramosMapperAbstract {

    private static CachedRowSet tramos;
    private static String tableName = "inventario.cotas";

    @Override
    public CachedRowSet getTramos() throws SQLException {
	if (tramos == null) {
	    tramos = load();
	}
	return tramos;
    }

    @Override
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

    @Override
    public int getLastAvailableID() throws SQLException {
	Connection c = DBFacade.getConnection();
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT nextval('inventario.cotas_gid_seq') AS value");
	rs.next();
	return rs.getInt("value");
    }

}
