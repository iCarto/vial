package es.icarto.gvsig.viasobras.catalog.domain.mappers;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

import es.icarto.gvsig.viasobras.catalog.domain.Tramo;
import es.icarto.gvsig.viasobras.catalog.domain.Tramos;

public abstract class TramosMapperAbstract implements TramosMapper {

    public static final String ID_FIELDNAME = "gid";
    public static final String CARRETERA_FIELDNAME = "codigo_carretera";
    public static final String CONCELLO_FIELDNAME = "codigo_concello";
    public static final String PK_START_FIELDNAME = "pk_inicial";
    public static final String PK_END_FIELDNAME = "pk_final";
    public static final String CARACTERISTICA_FIELDNAME = "valor";

    public abstract CachedRowSet getTramos() throws SQLException;

    public abstract CachedRowSet load() throws SQLException;

    public abstract int getLastAvailableID() throws SQLException;

    public Tramos findAll() throws SQLException {
	CachedRowSet tramos = getTramos();
	return new Tramos(this, Filter.findAll(tramos));
    }

    public Tramos findWhereCarretera(String carretera) throws SQLException {
	CachedRowSet tramos = getTramos();
	return new Tramos(this, Filter.findWhereCarretera(tramos, carretera));
    }

    public Tramos findWhereCarreteraAndPK(String carretera, double pkStart,
	    double pkEnd) throws SQLException {
	CachedRowSet tramos = getTramos();
	return new Tramos(this, Filter.findWhereCarreteraAndPK(tramos,
		carretera, pkStart, pkEnd));
    }

    public Tramos findWhereConcello(String concello) throws SQLException {
	CachedRowSet tramos = getTramos();
	return new Tramos(this, Filter.findWhereConcello(tramos, concello));
    }

    public Tramos findWhereCarreteraAndConcello(String carretera,
	    String concello) throws SQLException {
	CachedRowSet tramos = getTramos();
	return new Tramos(this, Filter.findWhereCarreteraAndConcello(tramos,
		carretera, concello));
    }

    public CachedRowSet getCachedRowSet(String tableName)
	    throws SQLException {
	String sqlQuery = "SELECT gid, codigo_carretera, codigo_concello, valor, pk_inicial, pk_final "
		+ " FROM "
		+ tableName
		+ " WHERE gid = gid ORDER BY pk_inicial";
	// "WHERE gid = gid" is needed to avoid errors, as it seems -in
	// JDBC- an ORDER clause cannot be used without WHERE
	int[] primaryKeys = { 1 }; // primary key index = gid column index
	CachedRowSet tramos = new CachedRowSetImpl();
	tramos.setUrl(DomainMapper.getURL());
	tramos.setUsername(DomainMapper.getUserName());
	tramos.setPassword(DomainMapper.getPwd());
	tramos.setCommand(sqlQuery);
	tramos.setKeyColumns(primaryKeys);// set primary key
	tramos.execute();
	return tramos;
    }

    public Tramos save(Tramos ts) throws SQLException {
	int newID = getLastAvailableID();
	CachedRowSet tramos = getTramos();
	for (Tramo t : ts) {
	    if (t.getStatus() == Tramo.STATUS_UPDATE) {
		tramos.absolute(t.getPosition());
		tramos.updateString(CARRETERA_FIELDNAME, t.getCarretera());
		tramos.updateString(CONCELLO_FIELDNAME, t.getConcello());
		tramos.updateDouble(PK_START_FIELDNAME, t.getPkStart());
		tramos.updateDouble(PK_END_FIELDNAME, t.getPkEnd());
		tramos.updateString(CARACTERISTICA_FIELDNAME, t.getValue());
		tramos.updateRow();
	    } else if (t.getStatus() == Tramo.STATUS_DELETE) {
		tramos.absolute(t.getPosition());
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
	return new Tramos(this, Filter.findAll(tramos));
    }

}
