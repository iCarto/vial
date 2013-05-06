package es.icarto.gvsig.viasobras.domain.catalog.mappers;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

import es.icarto.gvsig.viasobras.domain.catalog.Evento;
import es.icarto.gvsig.viasobras.domain.catalog.Eventos;
import es.icarto.gvsig.viasobras.domain.catalog.utils.EventosRecordsetAdapter;

public abstract class EventosMapperAbstract implements EventosMapper {

    public static final int VALUE_FIELD_POSITION = 6;
    public static final String ID_FIELDNAME = "gid";
    public static final String CARRETERA_FIELDNAME = "codigo_carretera";
    public static final String ORDEN_FIELDNAME = "tramo";
    public static final String CONCELLO_FIELDNAME = "codigo_municipio";
    public static final String PK_FIELDNAME = "pk";
    public static final String CARACTERISTICA_FIELDNAME = "valor";
    public static final String FECHA_ACTUALIZACION_FIELDNAME = "fecha";

    public abstract CachedRowSet getEventos() throws SQLException;

    public abstract void setInvalid();

    public abstract CachedRowSet load() throws SQLException;

    public abstract int getLastAvailableID() throws SQLException;

    public abstract String getSQLQuery();

    public abstract String getTableName();

    public Eventos findAll() throws SQLException {
	CachedRowSet eventos = getEventos();
	return new Eventos(this, EventosRecordsetAdapter.findAll(eventos));
    }

    public Eventos findWhereCarretera(String carretera) throws SQLException {
	CachedRowSet eventos = getEventos();
	return new Eventos(this, EventosRecordsetAdapter.findWhereCarretera(
		eventos, carretera));
    }

    public Eventos findWhereCarreteraAndPK(String carretera, double pkStart,
	    double pkEnd) throws SQLException {
	CachedRowSet eventos = getEventos();
	return new Eventos(this,
		EventosRecordsetAdapter.findWhereCarreteraAndPK(eventos,
			carretera, pkStart, pkEnd));
    }

    public Eventos findWhereConcello(String concello) throws SQLException {
	CachedRowSet eventos = getEventos();
	return new Eventos(this, EventosRecordsetAdapter.findWhereConcello(
		eventos, concello));
    }

    public Eventos findWhereCarreteraAndConcello(String carretera,
	    String concello) throws SQLException {
	CachedRowSet eventos = getEventos();
	return new Eventos(this,
		EventosRecordsetAdapter.findWhereCarreteraAndConcello(eventos,
			carretera, concello));
    }

    public CachedRowSet getCachedRowSet()
	    throws SQLException {
	String sqlQuery = getSQLQuery();
	int[] primaryKeys = { 1 }; // primary key index = gid column index
	CachedRowSet eventos = new CachedRowSetImpl();
	eventos.setUrl(DBFacade.getURL());
	eventos.setUsername(DBFacade.getUserName());
	eventos.setPassword(DBFacade.getPwd());
	eventos.setCommand(sqlQuery);
	eventos.setKeyColumns(primaryKeys);// set primary key
	eventos.setTableName(getTableName());
	eventos.execute(DBFacade.getConnection());
	return eventos;
    }

    public Eventos save(Eventos ts) throws SQLException {
	int newID = getLastAvailableID();
	CachedRowSet eventos = getEventos();
	for (Evento t : ts) {
	    if (t.getStatus() == Evento.STATUS_UPDATE) {
		eventos.absolute(t.getPosition());
		eventos.updateString(CARRETERA_FIELDNAME, t.getCarretera());
		eventos.updateString(ORDEN_FIELDNAME, t.getOrden());
		eventos.updateString(CONCELLO_FIELDNAME, t.getConcello());
		eventos.updateDouble(PK_FIELDNAME, t.getPk());
		eventos.updateObject(CARACTERISTICA_FIELDNAME, t.getValue());
		updateDate(eventos, t);
		eventos.updateRow();
	    } else if (t.getStatus() == Evento.STATUS_DELETE) {
		eventos.absolute(t.getPosition());
		eventos.deleteRow();
		eventos.beforeFirst();
	    } else if (t.getStatus() == Evento.STATUS_INSERT) {
		eventos.moveToInsertRow();
		eventos.updateInt(ID_FIELDNAME, newID);
		eventos.updateString(CARRETERA_FIELDNAME, t.getCarretera());
		eventos.updateString(ORDEN_FIELDNAME, t.getOrden());
		eventos.updateString(CONCELLO_FIELDNAME, t.getConcello()
			.toString());
		eventos.updateDouble(PK_FIELDNAME, t.getPk());
		eventos.updateObject(CARACTERISTICA_FIELDNAME, t.getValue());
		updateDate(eventos, t);
		eventos.insertRow();
		eventos.moveToCurrentRow();
		newID++;
	    }
	}
	eventos.acceptChanges();
	return new Eventos(this, EventosRecordsetAdapter.findAll(eventos));
    }

    private void updateDate(CachedRowSet eventos, Evento t) throws SQLException {
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
	 * return a NULL. See EventosRecordsetAdapter.getDate()
	 */
	if (t.getUpdatingDate() == null) {
	    eventos.updateDate(FECHA_ACTUALIZACION_FIELDNAME,
		    new java.sql.Date(
			    0));
	} else {
	    eventos.updateDate(FECHA_ACTUALIZACION_FIELDNAME,
		    t.getUpdatingDate());
	}
    }

}
