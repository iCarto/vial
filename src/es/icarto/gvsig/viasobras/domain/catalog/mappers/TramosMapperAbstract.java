package es.icarto.gvsig.viasobras.domain.catalog.mappers;

import java.sql.SQLException;
import java.sql.Types;

import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

import es.icarto.gvsig.viasobras.domain.catalog.Tramo;
import es.icarto.gvsig.viasobras.domain.catalog.Tramos;
import es.icarto.gvsig.viasobras.domain.catalog.utils.TramosRecordsetAdapter;

public abstract class TramosMapperAbstract implements TramosMapper {

    public static final int VALUE_FIELD_POSITION = 7;
    public static final String ID_FIELDNAME = "gid";
    public static final String CARRETERA_FIELDNAME = "codigo_carretera";
    public static final String ORDEN_TRAMO_FIELDNAME = "tramo";
    public static final String CONCELLO_FIELDNAME = "codigo_municipio";
    public static final String PK_START_FIELDNAME = "pk_inicial";
    public static final String PK_END_FIELDNAME = "pk_final";
    public static final String CARACTERISTICA_FIELDNAME = "valor";
    public static final String FECHA_ACTUALIZACION_FIELDNAME = "fecha_actualizacion";

    public abstract CachedRowSet getTramos() throws SQLException;

    public abstract CachedRowSet load() throws SQLException;

    public abstract void setInvalid();

    public abstract int getLastAvailableID() throws SQLException;

    public Tramos findAll() throws SQLException {
	CachedRowSet tramos = getTramos();
	return new Tramos(this, TramosRecordsetAdapter.findAll(tramos));
    }

    public Tramos findWhereCarretera(String carretera) throws SQLException {
	CachedRowSet tramos = getTramos();
	return new Tramos(this, TramosRecordsetAdapter.findWhereCarretera(tramos, carretera));
    }

    public Tramos findWhereCarreteraAndPK(String carretera, double pkStart,
	    double pkEnd) throws SQLException {
	CachedRowSet tramos = getTramos();
	return new Tramos(this, TramosRecordsetAdapter.findWhereCarreteraAndPK(tramos,
		carretera, pkStart, pkEnd));
    }

    public Tramos findWhereConcello(String concello) throws SQLException {
	CachedRowSet tramos = getTramos();
	return new Tramos(this, TramosRecordsetAdapter.findWhereConcello(tramos, concello));
    }

    public Tramos findWhereCarreteraAndConcello(String carretera,
	    String concello) throws SQLException {
	CachedRowSet tramos = getTramos();
	return new Tramos(this, TramosRecordsetAdapter.findWhereCarreteraAndConcello(tramos,
		carretera, concello));
    }

    public CachedRowSet getCachedRowSet(String tableName)
	    throws SQLException {
	/*
	 * "WHERE gid = gid" is needed to avoid errors, as it seems -in JDBC- an
	 * ORDER clause cannot be used without WHERE
	 * 
	 * I couldn't order by several fields (ORDER BY codigo_carretera,
	 * codigo_concello, ...) as it give problems when saving, so the
	 * ordering is done in Tramos() builder.
	 */
	String sqlQuery = "SELECT gid, codigo_carretera, tramo, codigo_municipio, pk_inicial, pk_final, valor, fecha_actualizacion "
		+ " FROM " + tableName;
	//+ " WHERE gid = gid ORDER BY pk_inicial";
	int[] primaryKeys = { 1 }; // primary key index = gid column index
	CachedRowSet tramos = new CachedRowSetImpl();
	tramos.setUrl(DBFacade.getURL());
	tramos.setUsername(DBFacade.getUserName());
	tramos.setPassword(DBFacade.getPwd());
	tramos.setCommand(sqlQuery);
	tramos.setKeyColumns(primaryKeys);// set primary key
	tramos.execute(DBFacade.getConnection());
	return tramos;
    }

    public Tramos save(Tramos ts) throws SQLException {
	int newID = getLastAvailableID();
	CachedRowSet tramos = getTramos();
	int valueType = tramos.getMetaData().getColumnType(VALUE_FIELD_POSITION);
	for (Tramo t : ts) {
	    if (t.getStatus() == Tramo.STATUS_UPDATE) {
		tramos.absolute(t.getPosition());
		tramos.updateString(CARRETERA_FIELDNAME, t.getCarretera());
		tramos.updateString(ORDEN_TRAMO_FIELDNAME, t.getOrdenTramo());
		tramos.updateString(CONCELLO_FIELDNAME, t.getConcello());
		tramos.updateDouble(PK_START_FIELDNAME, t.getPkStart());
		tramos.updateDouble(PK_END_FIELDNAME, t.getPkEnd());
		updateValue(tramos, t, valueType);
		updateDate(tramos, t);
		tramos.updateRow();
	    } else if (t.getStatus() == Tramo.STATUS_DELETE) {
		if (t.getPosition() != Tramo.NO_POSITION) {
		    // Tramo.NO_POSITION means that the user is deleting a new
		    // created tramo, with no assigned position
		    // That "virtual" tramo (as it is not in the datasource yet)
		    // will be "deleted" automatically when refreshing the
		    // CachedRowSet so no need to delete here
		    tramos.absolute(t.getPosition());
		    if (tramos.getRow() != 0) {
			// only delete if cursor is proper placed
			tramos.deleteRow();
		    }
		    tramos.beforeFirst();
		}
	    } else if (t.getStatus() == Tramo.STATUS_INSERT) {
		tramos.moveToInsertRow();
		tramos.updateInt(ID_FIELDNAME, newID);
		tramos.updateString(CARRETERA_FIELDNAME, t.getCarretera());
		tramos.updateString(ORDEN_TRAMO_FIELDNAME, t.getOrdenTramo());
		tramos.updateString(CONCELLO_FIELDNAME, t.getConcello()
			.toString());
		tramos.updateDouble(PK_START_FIELDNAME, t.getPkStart());
		tramos.updateDouble(PK_END_FIELDNAME, t.getPkEnd());
		updateValue(tramos, t, valueType);
		updateDate(tramos, t);
		tramos.insertRow();
		tramos.moveToCurrentRow();
		newID++;
	    }
	}
	tramos.acceptChanges();
	return new Tramos(this, TramosRecordsetAdapter.findAll(tramos));
    }

    private void updateDate(CachedRowSet tramos, Tramo t) throws SQLException {
	/*
	 * BIG BUG in CachedRowSet: if one sets all values in a column to NULL
	 * and then serializes the CachedRowSet then deserializing and
	 * attempting an update would result in an Exception where apparently
	 * the column type is not being set so defaults to the type of the prior
	 * column:
	 * 
	 * org.postgresql.util.PSQLException: ERROR column "fecha_actualizacion"
	 * is of type character varying but expression is of type Date
	 * 
	 * http://books.google.es/books?id=4yjiwDMmOZMC&lpg=PA232&ots=9yQFXXfd5H&
	 * pg=PA232#v=onepage&q&f=false
	 * 
	 * So, the trick we do here is: in case the date is NULL, set it to
	 * first milisecond from epoch (1970-01-01). When reading back from
	 * database will check if the data is higher than this and, if not, will
	 * return a NULL. See TramosRecordsetAdapter.getDate()
	 */
	if (t.getUpdatingDate() == null) {
	    tramos.updateDate(FECHA_ACTUALIZACION_FIELDNAME, new java.sql.Date(
		    0));
	} else {
	    tramos.updateDate(FECHA_ACTUALIZACION_FIELDNAME,
		    t.getUpdatingDate());
	}
    }

    private void updateValue(CachedRowSet tramos, Tramo t, int valueType)
	    throws SQLException {
	switch (valueType) {
	case Types.VARCHAR:
	    if (t.getValue() == null) {
		tramos.updateNull(CARACTERISTICA_FIELDNAME);
	    } else {
		tramos.updateString(CARACTERISTICA_FIELDNAME,
			(String) t.getValue());
	    }
	    break;
	case Types.DOUBLE:
	    if (t.getValue() == null) {
		tramos.updateNull(CARACTERISTICA_FIELDNAME);
	    } else {
		tramos.updateDouble(CARACTERISTICA_FIELDNAME,
			(Double) t.getValue());
	    }
	    break;
	default:
	    tramos.updateObject(CARACTERISTICA_FIELDNAME, t.getValue());
	    break;
	}
    }

}
