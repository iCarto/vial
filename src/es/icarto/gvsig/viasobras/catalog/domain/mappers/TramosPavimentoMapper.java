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

public class TramosPavimentoMapper extends DomainMapper {

    public static final String CARRETERA_FIELDNAME = "carretera";
    public static final String CONCELLO_FIELDNAME = "municipio";
    public static final String PK_START_FIELDNAME = "origenpavi";
    public static final String PK_END_FIELDNAME = "finalpavim";

    private static CachedRowSet tramos;
    private static HashMap<Integer, Integer> indexRegister;

    public static Tramos findAll() throws SQLException {
	if (tramos != null) {
	    return new Tramos(TramosPavimentoMapper.toList(tramos));
	}
	try {
	    // "WHERE gid = gid" is needed to avoid errors, as it seems -in
	    // JDBC- an ORDER clause cannot be used without WHERE
	    String query = "SELECT gid, carretera, municipio, tipopavime, origenpavi, finalpavim "
		    + "FROM inventario.tipo_pavimento "
		    + "WHERE gid = gid ORDER BY origenpavi";
	    Connection c = DomainMapper.getConnection();
	    Statement stmt = c.createStatement();
	    ResultSet rs = stmt.executeQuery(query);
	    tramos = new CachedRowSetImpl();
	    tramos.populate(rs);
	    tramos.setUrl(DomainMapper.getURL());
	    tramos.setUsername(DomainMapper.getUserName());
	    tramos.setPassword(DomainMapper.getPwd());
	    tramos.setCommand(query);
	    int[] keys = { 1 }; // primary key index = gid column index
	    tramos.setKeyColumns(keys);// set primary key
	    indexRegister = getIndexRegister(tramos);
	    return new Tramos(TramosPavimentoMapper.toList(tramos));
	} catch (SQLException e) {
	    e.printStackTrace();
	    tramos = null;
	    throw new SQLException(e);
	}
    }

    public static Tramos findWhereCarretera(String carretera)
	    throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	if (tramos == null) {
	    TramosPavimentoMapper.findAll();// will fill tramos
	}
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilter(CARRETERA_FIELDNAME, carretera));
	return new Tramos(TramosPavimentoMapper.toList(frs));
    }

    public static Tramos findWhereCarretera(String carretera, double pkStart,
	    double pkEnd) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	if (tramos == null) {
	    TramosPlataformaMapper.findAll();
	}
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilterPK(CARRETERA_FIELDNAME, carretera,
		PK_START_FIELDNAME, pkStart, PK_END_FIELDNAME, pkEnd));
	return new Tramos(TramosPavimentoMapper.toList(frs));
    }

    public static Tramos findWhereConcello(String concello) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	if (tramos == null) {
	    TramosPavimentoMapper.findAll();// will fill tramos
	}
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilter(CONCELLO_FIELDNAME, concello));
	return new Tramos(TramosPavimentoMapper.toList(frs));
    }

    public static Tramos findWhereCarreteraAndConcello(String carretera,
	    String concello) throws SQLException {
	FilteredRowSet frs = new FilteredRowSetImpl();
	if (tramos == null) {
	    TramosPavimentoMapper.findAll();// will fill tramos
	}
	tramos.beforeFirst();
	frs.populate((ResultSet) tramos);
	frs.setFilter(new TramosFilterCarreteraConcello(carretera, concello));
	return new Tramos(TramosPavimentoMapper.toList(frs));
    }

    public static void save(Tramos ts) throws SQLException {
	Connection c = DomainMapper.getConnection();
	c.setAutoCommit(false);
	for (Tramo t : ts) {
	    if (t.getStatus() == Tramo.STATUS_UPDATE) {
		tramos.absolute(indexRegister.get(t.getId()));
		tramos.updateString("carretera", t.getCarretera());
		tramos.updateString("municipio", t.getConcello());
		tramos.updateDouble("origenpavi", t.getPkStart());
		tramos.updateDouble("finalpavim", t.getPkEnd());
		tramos.updateString("tipopavime", t.getValue());
		tramos.updateRow();
	    } else if (t.getStatus() == Tramo.STATUS_DELETE) {
		tramos.absolute(indexRegister.get(t.getId()));
		tramos.deleteRow();
		tramos.beforeFirst();
	    } else if (t.getStatus() == Tramo.STATUS_INSERT) {
		// TODO: insert by means of updating tramos, so it gets updated
		// without having to launch the query -findAll()- again
		PreparedStatement st = c
			.prepareStatement("INSERT INTO inventario.tipo_pavimento (carretera, municipio, tipopavime, origenpavi, finalpavim) VALUES(?, ?, ?, ?, ?)");
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
	findAll();
    }

    private static List<Tramo> toList(ResultSet rs) throws SQLException {
	List<Tramo> ts = new ArrayList<Tramo>();
	rs.beforeFirst();
	while (rs.next()) {
	    Tramo tramo = new Tramo();
	    tramo.setId(rs.getInt("gid"));
	    tramo.setPkStart(rs.getDouble("origenpavi"));
	    tramo.setPkEnd(rs.getDouble("finalpavim"));
	    tramo.setCarretera(rs.getString("carretera"));
	    tramo.setConcello(rs.getString("municipio"));
	    tramo.setValue(rs.getString("tipopavime"));
	    ts.add(tramo);
	}
	return ts;
    }

    private static HashMap<Integer, Integer> getIndexRegister(ResultSet rs)
	    throws SQLException {
	HashMap<Integer, Integer> register = new HashMap<Integer, Integer>();
	rs.beforeFirst();
	while (rs.next()) {
	    register.put(rs.getInt("gid"), rs.getRow());
	}
	return register;
    }

}
