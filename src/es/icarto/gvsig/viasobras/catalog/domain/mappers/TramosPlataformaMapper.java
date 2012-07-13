package es.icarto.gvsig.viasobras.catalog.domain.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.FilteredRowSet;

import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.FilteredRowSetImpl;

import es.icarto.gvsig.viasobras.catalog.domain.Tramo;
import es.icarto.gvsig.viasobras.catalog.domain.Tramos;
import es.icarto.gvsig.viasobras.catalog.domain.filters.TramosFilter;
import es.icarto.gvsig.viasobras.catalog.domain.filters.TramosFilterCarreteraConcello;
import es.icarto.gvsig.viasobras.catalog.domain.filters.TramosFilterPK;

public class TramosPlataformaMapper extends DomainMapper implements
	TramosMapper {

    private static final String ID_FIELDNAME = "gid";
    public static final String CARRETERA_FIELDNAME = "carretera";
    public static final String CONCELLO_FIELDNAME = "municipio";
    public static final String PK_START_FIELDNAME = "origentram";
    public static final String PK_END_FIELDNAME = "finaltramo";
    private static final String CARACTERISTICA_FIELDNAME = "ancho_plataforma";

    // tramos and the register are shared within all mappers of this kind
    private static CachedRowSet tramos;
    private static HashMap<String, Integer> indexRegister;

    public Tramos findAll() throws SQLException {
	if (tramos != null) {
	    return new Tramos(this, toList(tramos));
	}
	// "WHERE gid = gid" is needed to avoid errors, as it seems -in
	// JDBC- an ORDER clause cannot be used without WHERE
	String sqlQuery = "SELECT gid, carretera, municipio, ancho_plataforma, origentram, finaltramo "
		+ " FROM inventario.ancho_plataforma "
		+ " WHERE gid = gid ORDER BY origentram";
	Connection c = DomainMapper.getConnection();
	try {
	    Statement stmt = c.createStatement();
	    ResultSet rs = stmt.executeQuery(sqlQuery);
	    tramos = new CachedRowSetImpl();
	    tramos.populate(rs);
	    tramos.setUrl(DomainMapper.getURL());
	    tramos.setUsername(DomainMapper.getUserName());
	    tramos.setPassword(DomainMapper.getPwd());
	    tramos.setCommand(sqlQuery);
	    int[] keys = { 1 }; // primary key index = gid column index
	    tramos.setKeyColumns(keys);// set primary key
	    indexRegister = getIndexRegister(tramos);
	    return new Tramos(this, toList(tramos));
	} catch (SQLException e) {
	    e.printStackTrace();
	    tramos = null;
	    throw new SQLException(e);
	}
    }

    public Tramos findWhereCarretera(String carretera) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	if (tramos == null) {
	    findAll();// will fill tramos
	}
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilter(CARRETERA_FIELDNAME, carretera));
	return new Tramos(this, toList(frs));
    }

    public Tramos findWhereCarretera(String carretera, double pkStart,
	    double pkEnd) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	if (tramos == null) {
	    findAll();// will fill tramos
	}
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilterPK(CARRETERA_FIELDNAME, carretera,
		PK_START_FIELDNAME, pkStart, PK_END_FIELDNAME, pkEnd));
	return new Tramos(this, toList(frs));
    }

    public Tramos findWhereConcello(String concello) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	if (tramos == null) {
	    findAll();// will fill tramos
	}
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilter(CONCELLO_FIELDNAME, concello));
	return new Tramos(this, toList(frs));
    }

    public Tramos findWhereCarreteraAndConcello(String carretera,
	    String concello) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	if (tramos == null) {
	    findAll();// will fill tramos
	}
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilterCarreteraConcello(carretera, concello));
	return new Tramos(this, toList(frs));
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
		tramos.updateDouble(CARACTERISTICA_FIELDNAME,
			Double.parseDouble(t.getValue()));
		tramos.updateRow();
	    } else if (t.getStatus() == Tramo.STATUS_DELETE) {
		tramos.absolute(indexRegister.get(t.getId()));
		tramos.deleteRow();
		tramos.beforeFirst();
	    } else if (t.getStatus() == Tramo.STATUS_INSERT) {
		// TODO: insert by means of updating tramos, so it gets updated
		// without having to launch the query -findAll()- again
		PreparedStatement st = c
			.prepareStatement("INSERT INTO inventario.ancho_plataforma (carretera, municipio, ancho_plataforma, origentram, finaltramo) VALUES(?, ?, ?, ?, ?)");
		st.setString(1, t.getCarretera());
		st.setString(2, t.getConcello().toString());
		st.setDouble(3, Double.parseDouble(t.getValue()));
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
	return findAll();
    }

    private List<Tramo> toList(ResultSet rs) throws SQLException {
	List<Tramo> ts = new ArrayList<Tramo>();
	rs.beforeFirst();
	while (rs.next()) {
	    Tramo tramo = new Tramo();
	    tramo.setId(Integer.toString(rs.getInt(ID_FIELDNAME)));
	    tramo.setPkStart(rs.getDouble(PK_START_FIELDNAME));
	    tramo.setPkEnd(rs.getDouble(PK_END_FIELDNAME));
	    tramo.setCarretera(rs.getString(CARRETERA_FIELDNAME));
	    tramo.setConcello(rs.getString(CONCELLO_FIELDNAME));
	    tramo.setValue(Double.toString(rs
		    .getDouble(CARACTERISTICA_FIELDNAME)));
	    ts.add(tramo);
	}
	return ts;
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
