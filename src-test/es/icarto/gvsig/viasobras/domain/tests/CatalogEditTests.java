package es.icarto.gvsig.viasobras.domain.tests;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import es.icarto.gvsig.viasobras.domain.catalog.Catalog;
import es.icarto.gvsig.viasobras.domain.catalog.Tramo;
import es.icarto.gvsig.viasobras.domain.catalog.Tramos;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.DBFacade;

public class CatalogEditTests {

    private static Connection c;
    private static String carreteraPlataforma;
    private static String concelloPlataforma;
    private static String carreteraPavimento;
    private static String concelloPavimento;

    @BeforeClass
    public static void connectToDatabase() throws SQLException,
    ClassNotFoundException {
	String url = "jdbc:postgresql://localhost:5432/vias_obras";
	String user = "viasobras";
	String passwd = "viasobras";
	Properties p = new Properties();
	p.setProperty(DBFacade.URL, url);
	p.setProperty(DBFacade.USERNAME, user);
	p.setProperty(DBFacade.PASSWORD, passwd);
	// postgresql-8.4.-jdbc4.jar needs to be in the classpasth before the
	// other gvSIG jars related to pgsql (which are jdbc3 are may not work).
	// Configure that in your classpath tab if you use eclipse
	Class.forName("org.postgresql.Driver");
	c = DriverManager.getConnection(p.getProperty(DBFacade.URL),
		p.getProperty(DBFacade.USERNAME),
		p.getProperty(DBFacade.PASSWORD));
	DBFacade.setConnection(c, p);
	setCarreteraAndConcelloForPlataforma();
	setCarreteraAndConcelloForPavimento();
    }

    private static void setCarreteraAndConcelloForPlataforma()
	    throws SQLException {
	// use the area (carretera, concello) with more tramos
	String sql = "SELECT codigo_carretera, codigo_municipio, COUNT(gid) AS tramos_count FROM inventario.ancho_plataforma GROUP BY codigo_carretera, codigo_municipio ORDER BY tramos_count DESC LIMIT 1";
	Statement stmt = c.createStatement();
	ResultSet rs = stmt.executeQuery(sql);
	rs.next();
	carreteraPlataforma = rs.getString("codigo_carretera");
	concelloPlataforma = rs.getString("codigo_municipio");
    }

    private static void setCarreteraAndConcelloForPavimento()
	    throws SQLException {
	// use the area (carretera, concello) with more tramos
	String sql = "SELECT codigo_carretera, codigo_municipio, COUNT(gid) AS tramos_count FROM inventario.tipo_pavimento GROUP BY codigo_carretera, codigo_municipio ORDER BY tramos_count DESC LIMIT 1";
	Statement stmt = c.createStatement();
	ResultSet rs = stmt.executeQuery(sql);
	rs.next();
	carreteraPavimento = rs.getString("codigo_carretera");
	concelloPavimento = rs.getString("codigo_municipio");
    }

    @Test
    public void testPavimentoDelete() throws SQLException {
	String carretera = carreteraPavimento;
	String concello = concelloPavimento;
	String lastID = getLastIdPavimento(carretera, concello);
	String gid = deleteTramoPavimento(carretera, concello, lastID);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.tipo_pavimento WHERE codigo_carretera = '"
			+ carretera
			+ "' AND codigo_municipio = '"
			+ concello
			+ "'");
	boolean updated = true;
	while (rs.next()) {
	    if (Integer.toString(rs.getInt("gid")).equals(gid)) {
		updated = false;
		break;
	    }
	}
	assertEquals(true, updated);
    }

    @Test
    public void testPlataformaDelete() throws SQLException {
	String carretera = carreteraPlataforma;
	String concello = concelloPlataforma;
	String lastID = getLastIdPlataforma(carretera, concello);
	String gid = deleteTramoPlataforma(carretera, concello, lastID);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.ancho_plataforma WHERE codigo_carretera = '"
			+ carretera
			+ "' AND codigo_municipio = '"
			+ concello
			+ "'");
	boolean updated = true;
	while (rs.next()) {
	    if (Integer.toString(rs.getInt("gid")).equals(gid)) {
		updated = false;
		break;
	    }
	}
	assertEquals(true, updated);
    }

    @Test
    public void testPavimentoUpdate() throws SQLException {
	String carretera = carreteraPavimento;
	String concello = concelloPavimento;
	String myValue = "B";
	updateTramoPavimento(carretera, concello, myValue);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT valor FROM inventario.tipo_pavimento WHERE codigo_carretera = '"
			+ carretera
			+ "' AND codigo_municipio = '"
			+ concello
			+ "'");
	boolean updated = true;
	while (rs.next()) {
	    if (!rs.getString("valor").equals(myValue)) {
		updated = false;
	    }
	}
	assertEquals(true, updated);
    }

    @Test
    public void testPlataformaUpdate() throws SQLException {
	String carretera = carreteraPlataforma;
	String concello = concelloPlataforma;
	double value = 666;
	updateTramoPlataforma(carretera, concello, value);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT valor FROM inventario.ancho_plataforma WHERE codigo_carretera = '"
			+ carretera
			+ "' AND codigo_municipio = '"
			+ concello
			+ "'");
	boolean updated = true;
	while (rs.next()) {
	    if (rs.getDouble("valor") != value) {
		updated = false;
	    }
	}
	assertEquals(true, updated);
    }

    @Test
    public void testPavimentoInsert() throws SQLException {
	String carretera = carreteraPavimento;
	String concello = concelloPavimento;
	int tramosNumber = insertTramoPavimento(carretera, concello);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT COUNT(*) AS rowNumber FROM inventario.tipo_pavimento WHERE codigo_carretera = '"
			+ carretera
			+ "' AND codigo_municipio = '"
			+ concello
			+ "'");
	rs.next();
	assertEquals(tramosNumber, rs.getInt("rowNumber"));
    }

    @Test
    public void testPlataformaInsert() throws SQLException {
	String carretera = carreteraPlataforma;
	String concello = concelloPlataforma;
	int tramosNumber = insertTramoPlataforma(carretera, concello);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT COUNT(*) AS rowNumber FROM inventario.ancho_plataforma WHERE codigo_carretera = '"
			+ carretera
			+ "' AND codigo_municipio = '"
			+ concello
			+ "'");
	rs.next();
	assertEquals(tramosNumber, rs.getInt("rowNumber"));
    }

    @Test
    public void testTipoPavimentoIsSinchronizedAfterCRUDOperations()
	    throws SQLException {
	String carretera = carreteraPavimento;
	String concello = concelloPavimento;
	insertTramoPavimento(carretera, concello);
	String lastID = getLastIdPavimento(carretera, concello);
	// should delete the recently created tramo
	String gid = deleteTramoPavimento(carretera, concello, lastID);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.tipo_pavimento WHERE codigo_carretera = '"
			+ carretera
			+ "' AND codigo_municipio = '"
			+ concello
			+ "'");
	boolean updated = true;
	while (rs.next()) {
	    if (Integer.toString(rs.getInt("gid")).equals(gid)) {
		updated = false;
		break;
	    }
	}
	assertEquals(true, updated);
    }

    @Test
    public void testAnchoPlataformaIsSinchronizedAfterCRUDOperations()
	    throws SQLException {
	String carretera = carreteraPlataforma;
	String concello = concelloPlataforma;
	insertTramoPlataforma(carretera, concello);
	// should delete the recently created tramo
	String lastID = getLastIdPlataforma(carretera, concello);
	String gid = deleteTramoPlataforma(carretera, concello, lastID);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.ancho_plataforma WHERE codigo_carretera = '"
			+ carretera
			+ "' AND codigo_municipio = '"
			+ concello
			+ "'");
	boolean updated = true;
	while (rs.next()) {
	    if (Integer.toString(rs.getInt("gid")).equals(gid)) {
		updated = false;
		break;
	    }
	}
	assertEquals(true, updated);
    }

    private String deleteTramoPlataforma(String carretera, String concello,
	    String gid)
		    throws SQLException {

	// add new tramo
	Catalog.clear();
	Catalog.setCarretera(carretera);
	Catalog.setConcello(concello);
	Tramos tramos = Catalog.getTramosAnchoPlataforma();
	tramos.removeTramo(gid);
	tramos.save();
	return gid;
    }

    private String deleteTramoPavimento(String carretera, String concello,
	    String gid)
		    throws SQLException {

	// add new tramo
	Catalog.clear();
	Catalog.setCarretera(carretera);
	Catalog.setConcello(concello);
	Tramos tramos = Catalog.getTramosTipoPavimento();
	tramos.removeTramo(gid);
	tramos.save();
	return gid;
    }

    private void updateTramoPlataforma(String carretera, String concello,
	    double value) throws SQLException {

	// modify and save tramos
	Catalog.clear();
	Catalog.setCarretera(carretera);
	Catalog.setConcello(concello);
	Tramos anchoPlataforma = Catalog.getTramosAnchoPlataforma();
	for (Tramo t : anchoPlataforma) {
	    t.setValue(value);
	    t.setStatus(Tramo.STATUS_UPDATE);
	}
	anchoPlataforma.save();
    }

    private void updateTramoPavimento(String carretera, String concello,
	    String value)
		    throws SQLException {

	// modify and save tramos
	Catalog.clear();
	Catalog.setCarretera(carretera);
	Catalog.setConcello(concello);
	Tramos tipoPavimento = Catalog.getTramosTipoPavimento();
	for (Tramo t : tipoPavimento) {
	    t.setValue(value);
	    t.setStatus(Tramo.STATUS_UPDATE);
	}
	tipoPavimento.save();
    }

    private int insertTramoPavimento(String carretera, String concello)
	    throws SQLException {
	double pkStart = 10.2;
	double pkEnd = 10.5;
	String myValue = "AAA";

	// add new tramo
	Catalog.clear();
	Catalog.setCarretera(carretera);
	Catalog.setConcello(concello);
	Tramos tipoPavimento = Catalog.getTramosTipoPavimento();
	Tramo tramo = new Tramo();
	tramo.setCarretera(carretera);
	tramo.setConcello(concello);
	tramo.setPkStart(pkStart);
	tramo.setPkEnd(pkEnd);
	tramo.setValue(myValue);
	tipoPavimento.addTramo(tramo);
	tipoPavimento.save();
	int tramosNumber = tipoPavimento.size();
	return tramosNumber;
    }

    private int insertTramoPlataforma(String carretera, String concello)
	    throws SQLException {
	double pkStart = 10.2;
	double pkEnd = 10.4;
	double myValue = 666;

	// add new tramo
	Catalog.clear();
	Catalog.setCarretera(carretera);
	Catalog.setConcello(concello);
	Tramos anchoPlataforma = Catalog.getTramosAnchoPlataforma();
	Tramo tramo = new Tramo();
	tramo.setCarretera(carretera);
	tramo.setConcello(concello);
	tramo.setPkStart(pkStart);
	tramo.setPkEnd(pkEnd);
	tramo.setValue(myValue);
	anchoPlataforma.addTramo(tramo);
	anchoPlataforma.save();
	int tramosNumber = anchoPlataforma.size();
	return tramosNumber;
    }

    private String getLastIdPlataforma(String carretera, String concello)
	    throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.ancho_plataforma WHERE codigo_carretera = '"
			+ carretera
			+ "' AND codigo_municipio = '"
			+ concello
			+ "' ORDER BY gid DESC LIMIT 1");
	rs.next();
	return Integer.toString(rs.getInt("gid"));
    }

    private String getLastIdPavimento(String carretera, String concello)
	    throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.tipo_pavimento WHERE codigo_carretera = '"
			+ carretera
			+ "' AND codigo_municipio = '"
			+ concello
			+ "' ORDER BY gid DESC LIMIT 1");
	rs.next();
	return Integer.toString(rs.getInt("gid"));
    }

}
