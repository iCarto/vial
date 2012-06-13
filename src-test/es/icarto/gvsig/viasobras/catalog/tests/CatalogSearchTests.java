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
import es.icarto.gvsig.viasobras.catalog.domain.mappers.DomainMapper;

public class CatalogSearchTests {

    static Connection c;

    @BeforeClass
    public static void connectToDatabase() throws SQLException {
	c = DriverManager.getConnection(
		"jdbc:postgresql://localhost:5432/vias_obras", "postgres",
		"postgres");
	Properties p = new Properties();
	p.setProperty("url", c.getMetaData().getURL());
	p.setProperty("username", "postgres");
	p.setProperty("password", "postgres");
	DomainMapper.setConnection(c, p);
    }

    @Test
    public void testNotNullResults() throws SQLException{
	assertNotNull(Catalog.getCarreteras());
	assertNotNull(Catalog.getConcellos());
	assertNotNull(Catalog.getTramosTipoPavimento());
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

	assertEquals(numRows, tramos.getTableModel().getRowCount());
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

	assertEquals(numRows, tramos.getTableModel().getRowCount());
    }

    @Test
    public void testFindPavimentoDependingOnCarretera() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.tipo_pavimento WHERE carretera = '4606'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Tramos tramos = Catalog.getTramosTipoPavimento();

	assertEquals(numRows, tramos.getTableModel().getRowCount());
    }

    @Test
    public void testFindPlataformaDependingOnCarretera() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.ancho_plataforma WHERE carretera = '4606'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Tramos tramos = Catalog.getTramosAnchoPlataforma();

	assertEquals(numRows, tramos.getTableModel().getRowCount());
    }

    @Test
    public void testFindPavimentoDependingOnConcello() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.tipo_pavimento WHERE municipio = '27018'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setConcello("27018"); // Fonsagrada
	Tramos tramos = Catalog.getTramosTipoPavimento();

	assertEquals(numRows, tramos.getTableModel().getRowCount());
    }

    @Test
    public void testFindPlataformaDependingOnConcello() throws SQLException,
    DBException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.ancho_plataforma WHERE municipio = '27018'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setConcello("27018"); // Fonsagrada
	Tramos tramos = Catalog.getTramosAnchoPlataforma();

	assertEquals(numRows, tramos.getTableModel().getRowCount());
    }

    @Test
    public void testFindPavimentoDependingOnCarreteraAndConcello()
	    throws SQLException, DBException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.tipo_pavimento WHERE carretera = '4606' AND municipio = '27018'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setConcello("27018"); // Fonsagrada
	Tramos tramos = Catalog.getTramosTipoPavimento();

	assertEquals(numRows, tramos.getTableModel().getRowCount());
    }

    @Test
    public void testFindPlataformaDependingOnCarreteraAndConcello()
	    throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.ancho_plataforma WHERE carretera = '4606' AND municipio = '27018'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setConcello("27018"); // Fonsagrada
	Tramos tramos = Catalog.getTramosAnchoPlataforma();

	assertEquals(numRows, tramos.getTableModel().getRowCount());
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
