package es.icarto.gvsig.viasobras.catalog.domain.mappers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.sql.rowset.CachedRowSet;

import es.icarto.gvsig.viasobras.catalog.domain.Tramo;
import es.icarto.gvsig.viasobras.catalog.domain.Tramos;

public class TramosMapperPavimento extends TramosMapperAbstract {

    // tramos and the register are shared within all mappers of this kind
    private static CachedRowSet tramos;
    private String tableName = "inventario.tipo_pavimento";
    private static HashMap<String, Integer> indexRegister;

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
	int newID = getLastAvailableID();
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
		tramos.moveToInsertRow();
		tramos.updateInt(ID_FIELDNAME, newID);
		tramos.updateString(CARRETERA_FIELDNAME, t.getCarretera());
		tramos.updateString(CONCELLO_FIELDNAME, t.getConcello()
			.toString());
		tramos.updateDouble(PK_START_FIELDNAME, t.getPkStart());
		tramos.updateDouble(PK_END_FIELDNAME, t.getPkEnd());
		tramos.updateString(CARACTERISTICA_FIELDNAME, t.getValue());
		tramos.insertRow();
		tramos.moveToCurrentRow();
		newID++;
	    }
	}
	tramos.acceptChanges();
	indexRegister = getIndexRegister(tramos);
	return new Tramos(this, Filter.findAll(tramos));
    }

    private int getLastAvailableID() throws SQLException {
	Connection c = DomainMapper.getConnection();
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT nextval('inventario.tipo_pavimento_gid_seq') AS value FROM inventario.tipo_pavimento");
	rs.next();
	return rs.getInt("value");
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
