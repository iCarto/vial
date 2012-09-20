package es.icarto.gvsig.viasobras.domain.catalog.mappers;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

import es.icarto.gvsig.viasobras.domain.catalog.Evento;
import es.icarto.gvsig.viasobras.domain.catalog.Eventos;
import es.icarto.gvsig.viasobras.domain.catalog.utils.EventosRecordsetAdapter;

public abstract class EventosMapperAbstract implements EventosMapper {

    public static final String ID_FIELDNAME = "gid";
    public static final String CARRETERA_FIELDNAME = "codigo_carretera";
    public static final String ORDEN_FIELDNAME = "tramo";
    public static final String CONCELLO_FIELDNAME = "codigo_municipio";
    public static final String PK_FIELDNAME = "pk";
    public static final String CARACTERISTICA_FIELDNAME = "valor";
    public static final String FECHA_ACTUALIZACION_FIELDNAME = "fecha";

    public abstract CachedRowSet getEventos() throws SQLException;

    public abstract CachedRowSet load() throws SQLException;

    public abstract int getLastAvailableID() throws SQLException;

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

    public CachedRowSet getCachedRowSet(String tableName)
	    throws SQLException {
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
	//+ " WHERE gid = gid ORDER BY pk_inicial";
	int[] primaryKeys = { 1 }; // primary key index = gid column index
	CachedRowSet eventos = new CachedRowSetImpl();
	eventos.setUrl(DBFacade.getURL());
	eventos.setUsername(DBFacade.getUserName());
	eventos.setPassword(DBFacade.getPwd());
	eventos.setCommand(sqlQuery);
	eventos.setKeyColumns(primaryKeys);// set primary key
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
		eventos.updateDate(FECHA_ACTUALIZACION_FIELDNAME,
			t.getUpdatingDate());
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
		eventos.updateDate(FECHA_ACTUALIZACION_FIELDNAME,
			t.getUpdatingDate());
		eventos.insertRow();
		eventos.moveToCurrentRow();
		newID++;
	    }
	}
	eventos.acceptChanges();
	return new Eventos(this, EventosRecordsetAdapter.findAll(eventos));
    }

}
