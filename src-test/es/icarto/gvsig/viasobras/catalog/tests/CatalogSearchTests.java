package es.icarto.gvsig.viasobras.catalog.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.iver.cit.gvsig.fmap.drivers.DBException;

import es.icarto.gvsig.viasobras.catalog.domain.Catalog;
import es.icarto.gvsig.viasobras.catalog.domain.Concellos;
import es.icarto.gvsig.viasobras.catalog.domain.Tramos;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.DBFacade;

public class CatalogSearchTests {

    static Connection c;

    @BeforeClass
    public static void connectToDatabase() throws SQLException {
	c = DriverManager.getConnection(
		"jdbc:postgresql://localhost:5432/vias_obras", "viasobras",
		"viasobras");
	Properties p = new Properties();
	p.setProperty("url", c.getMetaData().getURL());
	p.setProperty("username", "viasobras");
	p.setProperty("password", "viasobras");
	DBFacade.setConnection(c, p);
    }

    @Test
    public void testNotNullCarretera() throws SQLException {
	assertNotNull(Catalog.getCarreteras());
    }

    @Test
    public void testNotNullConcellos() throws SQLException {
	assertNotNull(Catalog.getConcellos());
    }

    @Test
    public void testNotNullTipoPavimento() throws SQLException {
	assertNotNull(Catalog.getTramosTipoPavimento());
    }

    @Test
    public void testNotNullAnchoPlataforma() throws SQLException {
	assertNotNull(Catalog.getTramosAnchoPlataforma());
    }

    @Test
    public void testCarreterasLoaded() throws SQLException {
	boolean ok;
	if (Catalog.getCarreteras().size() > 0) {
	    ok = true;
	} else {
	    ok = false;
	}
	assertEquals(true, ok);
    }

    @Test
    public void testFindPavimentoAll() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT COUNT(*) AS num_rows FROM inventario.tipo_pavimento");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Tramos tramos = Catalog.getTramosTipoPavimento();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindPlataformaAll() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT COUNT(*) AS num_rows FROM inventario.ancho_plataforma");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Tramos tramos = Catalog.getTramosAnchoPlataforma();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindPavimentoDependingOnCarretera() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.tipo_pavimento WHERE codigo_carretera = '4606'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Tramos tramos = Catalog.getTramosTipoPavimento();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindPlataformaDependingOnCarretera() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.ancho_plataforma WHERE codigo_carretera = '4606'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Tramos tramos = Catalog.getTramosAnchoPlataforma();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindPavimentoDependingOnPK() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.tipo_pavimento WHERE codigo_carretera = '4606' AND pk_inicial >= 0 AND pk_final <= 10");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setPKStart(0.0);
	Catalog.setPKEnd(10.0);
	Tramos tramos = Catalog.getTramosTipoPavimento();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindPavimentoDependingOnPKStart() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.tipo_pavimento WHERE codigo_carretera = '4606' AND pk_inicial >= 2.0");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setPKStart(2.0);
	Tramos tramos = Catalog.getTramosTipoPavimento();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindPavimentoDependingOnPKEnd() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.tipo_pavimento WHERE codigo_carretera = '4606' AND pk_final <= 8.0");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setPKEnd(8.0);
	Tramos tramos = Catalog.getTramosTipoPavimento();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindPlataformaDependingOnPK() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.ancho_plataforma WHERE codigo_carretera = '4606' AND pk_inicial >= 0 AND pk_final <= 10");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setPKStart(0.0);
	Catalog.setPKEnd(10.0);
	Tramos tramos = Catalog.getTramosAnchoPlataforma();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindPavimentoDependingOnConcello() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.tipo_pavimento WHERE codigo_concello = '27018'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setConcello("27018"); // Fonsagrada
	Tramos tramos = Catalog.getTramosTipoPavimento();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindPlataformaDependingOnConcello() throws SQLException,
    DBException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.ancho_plataforma WHERE codigo_concello = '27018'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setConcello("27018"); // Fonsagrada
	Tramos tramos = Catalog.getTramosAnchoPlataforma();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindPavimentoDependingOnCarreteraAndConcello()
	    throws SQLException, DBException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.tipo_pavimento WHERE codigo_carretera = '4606' AND codigo_concello = '27018'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setConcello("27018"); // Fonsagrada
	Tramos tramos = Catalog.getTramosTipoPavimento();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindPlataformaDependingOnCarreteraAndConcello()
	    throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.ancho_plataforma WHERE codigo_carretera = '4606' AND codigo_concello = '27018'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setConcello("27018"); // Fonsagrada
	Tramos tramos = Catalog.getTramosAnchoPlataforma();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFilterConcelloDependingOnCarretera() throws SQLException {
	Catalog.setCarretera("4606");
	Concellos cs = Catalog.getConcellos();
	int numConcellos = cs.size();

	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.carreteras_concellos WHERE codigo_carretera = '4606'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	assertEquals(numRows, numConcellos);
    }

    @Test
    public void testConsecutiveQuery() throws SQLException, DBException {
	Concellos cs1 = Catalog.getConcellos();

	Catalog.setCarretera("4606");
	Concellos cs2 = Catalog.getConcellos();
	int numConcellos = cs2.size();
	Statement stmt2 = c.createStatement();
	ResultSet rs2 = stmt2
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.carreteras_concellos WHERE codigo_carretera = '4606'");
	rs2.next();
	int numRows = rs2.getInt("num_rows");

	assertEquals(numRows, numConcellos);
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
	c.close();
    }

}
