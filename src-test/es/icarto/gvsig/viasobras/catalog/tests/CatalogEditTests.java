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
	String carretera = "4606";
	String concello = "27018";
	String gid = deleteLastTramoPavimento(carretera, concello);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.tipo_pavimento WHERE carretera = '4606' AND municipio = '27018'");
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
	String carretera = "4606";
	String concello = "27018";
	String gid = deleteLastTramoPlataforma(carretera, concello);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.ancho_plataforma WHERE carretera = '4606' AND municipio = '27018'");
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
	String carretera = "4606";
	String concello = "27018";
	String myValue = updateTramoPavimento(carretera, concello);

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
    public void testPlataformaUpdate() throws SQLException {
	String carretera = "4606";
	String concello = "27018";
	String value = "666";
	updateTramoPlataforma(carretera, concello, value);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT ancho_plataforma FROM inventario.ancho_plataforma WHERE carretera = '4606' AND municipio = '27018'");
	boolean updated = true;
	while (rs.next()) {
	    if (rs.getDouble("ancho_plataforma") != Double.parseDouble(value)) {
		updated = false;
	    }
	}
	assertEquals(true, updated);
    }

    @Test
    public void testPavimentoInsert() throws SQLException {
	int tramosNumber = insertTramoPavimento();

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT COUNT(*) AS rowNumber FROM inventario.tipo_pavimento WHERE carretera = '4606' AND municipio = '27018'");
	rs.next();
	assertEquals(tramosNumber, rs.getInt("rowNumber"));
    }

    @Test
    public void testPlataformaInsert() throws SQLException {
	String carretera = "4606";
	String concello = "27018";
	int tramosNumber = insertTramoPlataforma(carretera, concello);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT COUNT(*) AS rowNumber FROM inventario.ancho_plataforma WHERE carretera = '4606' AND municipio = '27018'");
	rs.next();
	assertEquals(tramosNumber, rs.getInt("rowNumber"));
    }

    @Test
    public void testTramosAreSinchronizedAfterCRUDOperations()
	    throws SQLException {
	insertTramoPavimento();
	String carretera = "4606";
	String concello = "27018";
	// should delete the recently created tramo
	String gid = deleteLastTramoPavimento(carretera, concello);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.tipo_pavimento WHERE carretera = '4606' AND municipio = '27018'");
	boolean updated = true;
	while (rs.next()) {
	    if (Integer.toString(rs.getInt("gid")).equals(gid)) {
		updated = false;
		break;
	    }
	}
	assertEquals(true, updated);
    }

    private String deleteLastTramoPlataforma(String carretera, String concello)
	    throws SQLException {
	String gid = getLastIdPlataforma();

	// add new tramo
	Catalog.clear();
	Catalog.setCarretera(carretera);
	Catalog.setConcello(concello);
	Tramos tramos = Catalog.getTramosAnchoPlataforma();
	tramos.removeTramo(gid);
	tramos.save();
	return gid;
    }

    private String deleteLastTramoPavimento(String carretera, String concello)
	    throws SQLException {
	String gid = getLastIdPavimento();

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
	    String value) throws SQLException {

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

    private String updateTramoPavimento(String carretera, String concello)
	    throws SQLException {
	String myValue = "B";

	// modify and save tramos
	Catalog.clear();
	Catalog.setCarretera(carretera);
	Catalog.setConcello(concello);
	Tramos tipoPavimento = Catalog.getTramosTipoPavimento();
	for (Tramo t : tipoPavimento) {
	    t.setValue(myValue);
	    t.setStatus(Tramo.STATUS_UPDATE);
	}
	tipoPavimento.save();
	return myValue;
    }

    private int insertTramoPavimento() throws SQLException {
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

    private int insertTramoPlataforma(String carretera, String concello)
	    throws SQLException {
	double pkStart = 10.2;
	double pkEnd = 10.4;
	String myValue = "666";

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

    private String getLastIdPlataforma() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.ancho_plataforma WHERE carretera = '4606' AND municipio = '27018' ORDER BY gid DESC LIMIT 1");
	rs.next();
	return Integer.toString(rs.getInt("gid"));
    }

    private String getLastIdPavimento() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.tipo_pavimento WHERE carretera = '4606' AND municipio = '27018' ORDER BY gid DESC LIMIT 1");
	rs.next();
	return Integer.toString(rs.getInt("gid"));
    }

}
