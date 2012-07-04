package es.icarto.gvsig.viasobras.catalog.tests;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import es.icarto.gvsig.viasobras.catalog.domain.Catalog;
import es.icarto.gvsig.viasobras.catalog.domain.Tramo;
import es.icarto.gvsig.viasobras.catalog.domain.Tramos;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.DomainMapper;

public class CatalogEditTests {

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
    public void testPavimentoDelete() throws SQLException {
	int gid = deleteLastTramo();

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.tipo_pavimento WHERE carretera = '4606' AND municipio = '27018'");
	boolean updated = true;
	while (rs.next()) {
	    if (rs.getInt("gid") == gid) {
		updated = false;
		break;
	    }
	}
	assertEquals(true, updated);
    }

    @Test
    public void testPavimentoUpdate() throws SQLException {
	String myValue = updateTramo();

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT tipopavime FROM inventario.tipo_pavimento WHERE carretera = '4606' AND municipio = '27018'");
	boolean updated = true;
	while (rs.next()) {
	    if (!rs.getString("tipopavime").equals(myValue)) {
		updated = false;
	    }
	}
	assertEquals(true, updated);
    }

    @Test
    public void testPavimentoInsert() throws SQLException {
	int tramosNumber = insertTramo();

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT COUNT(*) AS rowNumber FROM inventario.tipo_pavimento WHERE carretera = '4606' AND municipio = '27018'");
	rs.next();
	assertEquals(tramosNumber, rs.getInt("rowNumber"));
    }

    @Test
    public void testTramosAreSinchronizedAfterCRUDOperations()
	    throws SQLException {
	insertTramo();
	int gid = deleteLastTramo(); // should delete the recently created tramo

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.tipo_pavimento WHERE carretera = '4606' AND municipio = '27018'");
	boolean updated = true;
	while (rs.next()) {
	    if (rs.getInt("gid") == gid) {
		updated = false;
		break;
	    }
	}
	assertEquals(true, updated);
    }

    @Test
    public void testPavimentoUpdateGeomWhenPKChanges() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT ST_Length(the_geom) AS geom_length FROM inventario.tipo_pavimento WHERE carretera = '0102' AND municipio = '27001' AND gid = 9");
	rs.next();
	String oldLength = rs.getString("geom_length");

	// update pavimento PK
	Catalog.clear();
	Catalog.setCarretera("0102");
	Catalog.setConcello("27001");
	Tramos tipoPavimento = Catalog.getTramosTipoPavimento();
	for (Tramo t : tipoPavimento) {
	    if (t.getId() == 9) {
		t.setPkEnd(t.getPkEnd() + 10);
		t.setStatus(Tramo.STATUS_UPDATE);
	    }
	}
	tipoPavimento.save();

	// check if the later made effect
	ResultSet rs2 = stmt
		.executeQuery("SELECT ST_Length(the_geom) AS geom_length FROM inventario.tipo_pavimento WHERE carretera = '0102' AND municipio = '27001' AND gid = 9");
	rs2.next();
	String newLength = rs2.getString("geom_length");
	assertEquals(true, !newLength.equals(oldLength));
    }

    private int deleteLastTramo() throws SQLException {
	String carretera = "4606";
	String concello = "27018";
	int gid = getLastId();

	// add new tramo
	Catalog.clear();
	Catalog.setCarretera(carretera);
	Catalog.setConcello(concello);
	Tramos tramos = Catalog.getTramosTipoPavimento();
	tramos.removeTramo(gid);
	tramos.save();
	return gid;
    }

    private String updateTramo() throws SQLException {
	String myValue = "B";

	// modify and save tramos
	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setConcello("27018");
	Tramos tipoPavimento = Catalog.getTramosTipoPavimento();
	for (Tramo t : tipoPavimento) {
	    t.setValue(myValue);
	    t.setStatus(Tramo.STATUS_UPDATE);
	}
	tipoPavimento.save();
	return myValue;
    }

    private int insertTramo() throws SQLException {
	String carretera = "4606";
	String concello = "27018";
	double pkStart = 10.2;
	double pkEnd = 10.4;
	String myValue = "foo";

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

    private int getLastId() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.tipo_pavimento WHERE carretera = '4606' AND municipio = '27018' ORDER BY gid DESC LIMIT 1");
	rs.next();
	return rs.getInt("gid");
    }

}
