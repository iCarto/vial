package es.icarto.gvsig.viasobras.catalog.domain.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.rowset.CachedRowSet;

import es.icarto.gvsig.viasobras.catalog.domain.Tramo;
import es.icarto.gvsig.viasobras.catalog.domain.Tramos;

public class TramosMapperPlataforma extends TramosMapperAbstract {

    // tramos and the register are shared within all mappers of this kind
    private static CachedRowSet tramos;
    private static HashMap<String, Integer> indexRegister;
    private static String tableName = "inventario.ancho_plataforma";

    public CachedRowSet getTramos() throws SQLException {
	if (tramos == null) {
	    tramos = load();
	}
	return tramos;
    }

    public CachedRowSet load() throws SQLException {
	try {
	    tramos = super.getCachedRowSet(tableName);
	    indexRegister = getIndexRegister(tramos);
	    return tramos;
	} catch (SQLException e) {
	    e.printStackTrace();
	    tramos = null;
	    throw new SQLException(e);
	}
    }

    public Tramos save(Tramos ts) throws SQLException {
	Connection c = DomainMapper.getConnection();
	c.setAutoCommit(false);
	for (Tramo t : ts) {
	    if (t.getStatus() == Tramo.STATUS_UPDATE) {
		tramos.absolute(indexRegister.get(t.getId()));
		tramos.updateString(CARRETERA_FIELDNAME, t.getCarretera());
		tramos.updateString(CONCELLO_FIELDNAME, t.getConcello());
		tramos.updateDouble(PK_START_FIELDNAME, t.getPkStart());
		tramos.updateDouble(PK_END_FIELDNAME, t.getPkEnd());
		tramos.updateString(CARACTERISTICA_FIELDNAME, t.getValue());
		tramos.updateRow();
	    } else if (t.getStatus() == Tramo.STATUS_DELETE) {
		tramos.absolute(indexRegister.get(t.getId()));
		tramos.deleteRow();
		tramos.beforeFirst();
	    } else if (t.getStatus() == Tramo.STATUS_INSERT) {
		// TODO: insert by means of updating tramos, so it gets updated
		// without having to launch the query -findAll()- again
		PreparedStatement st = c
			.prepareStatement("INSERT INTO "
				+ tableName
				+ " (codigo_carretera, codigo_concello, valor, pk_inicial, pk_final) VALUES(?, ?, ?, ?, ?)");
		st.setString(1, t.getCarretera());
		st.setString(2, t.getConcello().toString());
		st.setString(3, t.getValue());
		st.setDouble(4, t.getPkStart());
		st.setDouble(5, t.getPkEnd());
		// System.out.println("Query: " + st.toString());
		st.executeUpdate();
	    }
	}
	c.commit();
	tramos.acceptChanges(DomainMapper.getConnection());
	// ensure tramos are updated properly, as INSERT operations will not
	// update them. TODO: make INSERT operations by means of CachedRowSet
	// tramos and these steps may be deleted
	tramos = null;
	return super.findAll();
    }

    private HashMap<String, Integer> getIndexRegister(ResultSet rs)
	    throws SQLException {
	HashMap<String, Integer> register = new HashMap<String, Integer>();
	rs.beforeFirst();
	while (rs.next()) {
	    register.put(Integer.toString(rs.getInt(ID_FIELDNAME)), rs.getRow());
	}
	return register;
    }

}
