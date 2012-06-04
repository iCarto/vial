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

import es.icarto.gvsig.viasobras.catalog.domain.Carreteras;
import es.icarto.gvsig.viasobras.catalog.domain.Concellos;
import es.icarto.gvsig.viasobras.catalog.domain.DomainMapper;
import es.icarto.gvsig.viasobras.catalog.domain.TipoPavimento;

public class ViasObras {

    static Connection c;

    @BeforeClass
    public static void connectToDatabase() throws SQLException {
	c = DriverManager.getConnection(
		"jdbc:postgresql://localhost:5432/vias_obras",
		"postgres",
		"postgres");
	DomainMapper.setConnection(c);
    }

    @Test
    public void testLoad() {
	assertNotNull(Carreteras.findAll());
	assertNotNull(Concellos.findAll());
    }

    @Test
    public void testLoadPavimento() {
	assertNotNull(TipoPavimento.findAll());
    }

    @Test
    public void testLoadPavimentoDependingOnCarretera()
	    throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT * FROM inventario.tipo_pavimento WHERE numeroinve = '4606'");
	// Retrieve the number of rows.
	rs.beforeFirst();
	int numrows = 0;
	while (rs.next()) {
	    numrows++;
	}
	rs.beforeFirst();

	TipoPavimento tp = TipoPavimento.findWhereCarretera("4606");
	assertEquals(numrows, tp.getTableModel().getRowCount());
    }

    @Test
    public void testLoadPavimentoDependingOnConcello()
	    throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT * FROM inventario.tipo_pavimento WHERE numeromuni = 46");
	// Retrieve the number of rows.
	rs.beforeFirst();
	int numrows = 0;
	while (rs.next()) {
	    numrows++;
	}
	rs.beforeFirst();

	TipoPavimento tp = TipoPavimento.findWhereConcello(46);
	assertEquals(numrows, tp.getTableModel().getRowCount());
    }

    @Test
    public void testLoadPavimentoDependingOnCarreteraAndConcello()
	    throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT * FROM inventario.tipo_pavimento WHERE numeroinve = '4606' AND numeromuni = 46");
	// Retrieve the number of rows.
	rs.beforeFirst();
	int numrows = 0;
	while (rs.next()) {
	    numrows++;
	}
	rs.beforeFirst();

	TipoPavimento tp = TipoPavimento.findWhereCarretraAndConcello("4606", 46);
	assertEquals(numrows, tp.getTableModel().getRowCount());
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
	c.close();
    }

}
