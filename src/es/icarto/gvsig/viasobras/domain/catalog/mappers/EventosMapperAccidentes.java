package es.icarto.gvsig.viasobras.domain.catalog.mappers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;


public class EventosMapperAccidentes extends EventosMapperAbstract {

    private static CachedRowSet eventos;
    private static String tableName = "inventario.accidentes";

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
	    eventos = super.getCachedRowSet();
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
		.executeQuery("SELECT nextval('inventario.accidentes_gid_seq') AS value");
	rs.next();
	return rs.getInt("value");
    }

    @Override
    public String getSQLQuery() {
	/*
	 * "WHERE gid = gid" is needed to avoid errors, as it seems -in JDBC- an
	 * ORDER clause cannot be used without WHERE
	 * 
	 * I couldn't order by several fields (ORDER BY codigo_carretera,
	 * codigo_concello, ...) as it give problems when saving, so the
	 * ordering is done in Eventos() builder.
	 */
	String sqlQuery = "SELECT gid, codigo_carretera, codigo_municipio, tramo, pk, valor, fecha "
		+ " FROM " + tableName;
	// + " WHERE gid = gid ORDER BY pk_inicial";
	return sqlQuery;
    }

    @Override
    public String getTableName() {
	return tableName;
    }

}
