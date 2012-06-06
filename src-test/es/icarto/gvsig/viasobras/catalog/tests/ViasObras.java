package es.icarto.gvsig.viasobras.catalog.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import es.icarto.gvsig.viasobras.catalog.domain.Catalog;
import es.icarto.gvsig.viasobras.catalog.domain.Concellos;
import es.icarto.gvsig.viasobras.catalog.domain.DomainMapper;
import es.icarto.gvsig.viasobras.catalog.domain.TipoPavimento;

public class ViasObras {

    static Connection c;

    @BeforeClass
    public static void connectToDatabase() throws SQLException {
	c = DriverManager.getConnection(
		"jdbc:postgresql://localhost:5432/vias_obras", "postgres",
		"postgres");
	DomainMapper.setConnection(c);
    }

    @Test
    public void testNotNullResults() {
	assertNotNull(Catalog.getCarreteras());
	assertNotNull(Catalog.getConcellos());
	assertNotNull(Catalog.getTramosTipoPavimento());
    }

    @Test
    public void testCarreterasLoaded() {
	boolean ok;
	if (Catalog.getCarreteras().size() > 0) {
	    ok = true;
	} else {
	    ok = false;
	}
	assertEquals(true, ok);
    }

    @Test
    public void testFindPavimentoDependingOnCarretera() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.tipo_pavimento WHERE numeroinve = '4606'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	TipoPavimento tp = TipoPavimento.findWhereCarretera("4606");
	assertEquals(numRows, tp.getTableModel().getRowCount());
    }

    @Test
    public void testFindPavimentoDependingOnConcello() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.tipo_pavimento WHERE numeromuni = 46");
	rs.next();
	int numRows = rs.getInt("num_rows");

	TipoPavimento tp = TipoPavimento.findWhereConcello(46);
	assertEquals(numRows, tp.getTableModel().getRowCount());
    }

    @Test
    public void testFindPavimentoDependingOnCarreteraAndConcello()
	    throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.tipo_pavimento WHERE numeroinve = '4606' AND numeromuni = 46");
	rs.next();
	int numRows = rs.getInt("num_rows");

	TipoPavimento tp = TipoPavimento.findWhereCarretraAndConcello("4606",
		46);
	assertEquals(numRows, tp.getTableModel().getRowCount());
    }

    @Test
    public void testFilterConcelloDependingOnCarretera() throws SQLException {
	Catalog.setCarretera("4606");
	Concellos cs = Catalog.getConcellos();
	int numConcellos = 0;
	while (cs.next()) {
	    numConcellos++;
	}
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.carreteras_concellos WHERE codigo_carretera = '4606'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	assertEquals(numRows, numConcellos);
    }

    @Test
    public void testConsecutiveQuery() throws SQLException {
	Concellos cs1 = Catalog.getConcellos();

	Catalog.setCarretera("4606");
	Concellos cs2 = Catalog.getConcellos();
	int numConcellos = 0;
	while (cs2.next()) {
	    numConcellos++;
	}
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
