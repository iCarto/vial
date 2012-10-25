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
		.executeQuery("SELECT nextval('inventario.aforos_gid_seq') AS value");
	rs.next();
	return rs.getInt("value");
    }

    @Override
    public String getSQLQuery() {
	String sqlQuery = "WITH p AS ("
		+ "SELECT codigo_carretera, codigo_municipio, tramo, MAX(fecha) AS fecha_ultimo_aforo "
		+ " FROM "
		+ tableName
		+ " GROUP BY codigo_carretera, codigo_municipio, tramo "
		+ " ORDER BY codigo_carretera, codigo_municipio, tramo) "
		+ "SELECT i.gid, i.codigo_carretera, i.codigo_municipio, i.tramo, i.pk, i.valor, i.fecha "
		+ "FROM "
		+ tableName
		+ " AS i, p "
		+ " WHERE i.codigo_carretera = p.codigo_carretera AND i.codigo_municipio = p.codigo_municipio "
		+ " 	  AND i.tramo = p.tramo AND i.fecha = p.fecha_ultimo_aforo "
		+ " ORDER BY i.codigo_carretera, i.codigo_municipio, i.tramo, i.pk";
	return sqlQuery;
    }

    @Override
    public String getTableName() {
	return tableName;
    }
}
