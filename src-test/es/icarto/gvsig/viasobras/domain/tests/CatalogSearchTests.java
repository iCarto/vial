package es.icarto.gvsig.viasobras.domain.tests;

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

import es.icarto.gvsig.viasobras.domain.catalog.Catalog;
import es.icarto.gvsig.viasobras.domain.catalog.Concellos;
import es.icarto.gvsig.viasobras.domain.catalog.Eventos;
import es.icarto.gvsig.viasobras.domain.catalog.Tramos;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.DBFacade;

public class CatalogSearchTests {

    static Connection c;

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
    public void testNotNullCotas() throws SQLException {
	assertNotNull(Catalog.getTramosCotas());
    }

    @Test
    public void testNotNullAforos() throws SQLException {
	assertNotNull(Catalog.getEventosAforos());
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
    public void testFindCotasAll() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT COUNT(*) AS num_rows FROM inventario.cotas");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Tramos tramos = Catalog.getTramosCotas();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindAforosAll() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT COUNT(*) AS num_rows FROM inventario.aforos");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Eventos eventos = Catalog.getEventosAforos();

	assertEquals(numRows, eventos.size());
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
    public void testFindCotasDependingOnCarretera() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.cotas WHERE codigo_carretera = '4606'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Tramos tramos = Catalog.getTramosCotas();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindAforosDependingOnCarretera() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.aforos WHERE codigo_carretera = '4606'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Eventos eventos = Catalog.getEventosAforos();

	assertEquals(numRows, eventos.size());
    }

    @Test
    public void testFindPavimentoDependingOnPK() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.tipo_pavimento WHERE codigo_carretera = '4606' AND pk_inicial <= 10 AND pk_final >= 0");
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
    public void testFindPlataformaDependingOnPK() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.ancho_plataforma WHERE codigo_carretera = '4606' AND pk_inicial <= 10 AND pk_final >= 0");
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
    public void testFindCotasDependingOnPK() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.cotas WHERE codigo_carretera = '4606' AND pk_inicial <= 10 AND pk_final >= 0");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setPKStart(0.0);
	Catalog.setPKEnd(10.0);
	Tramos tramos = Catalog.getTramosCotas();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindAforosDependingOnPK() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.aforos WHERE codigo_carretera = '4606' AND pk >= 0 AND pk <= 10");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setPKStart(0.0);
	Catalog.setPKEnd(10.0);
	Eventos eventos = Catalog.getEventosAforos();

	assertEquals(numRows, eventos.size());
    }

    @Test
    public void testFindPavimentoDependingOnPKStart() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.tipo_pavimento WHERE codigo_carretera = '4606' AND pk_final >= 2.0");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setPKStart(2.0);
	Tramos tramos = Catalog.getTramosTipoPavimento();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindPlataformaDependingOnPKStart() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.ancho_plataforma WHERE codigo_carretera = '4606' AND pk_final >= 2.0");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setPKStart(2.0);
	Tramos tramos = Catalog.getTramosAnchoPlataforma();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindCotasDependingOnPKStart() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.cotas WHERE codigo_carretera = '4606' AND pk_final >= 2.0");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setPKStart(2.0);
	Tramos tramos = Catalog.getTramosCotas();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindAforosDependingOnPKStart() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.aforos WHERE codigo_carretera = '4606' AND pk >= 2.0");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setPKStart(2.0);
	Eventos eventos = Catalog.getEventosAforos();

	assertEquals(numRows, eventos.size());
    }

    @Test
    public void testFindPavimentoDependingOnPKEnd() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.tipo_pavimento WHERE codigo_carretera = '4606' AND pk_inicial <= 8.0");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setPKEnd(8.0);
	Tramos tramos = Catalog.getTramosTipoPavimento();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindPlataformaDependingOnPKEnd() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.ancho_plataforma WHERE codigo_carretera = '4606' AND pk_inicial <= 8.0");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setPKEnd(8.0);
	Tramos tramos = Catalog.getTramosAnchoPlataforma();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindCotasDependingOnPKEnd() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.cotas WHERE codigo_carretera = '4606' AND pk_inicial <= 8.0");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setPKEnd(8.0);
	Tramos tramos = Catalog.getTramosCotas();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindAforosDependingOnPKEnd() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) As num_rows FROM inventario.aforos WHERE codigo_carretera = '4606' AND pk <= 8.0");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setPKEnd(8.0);
	Eventos eventos = Catalog.getEventosAforos();

	assertEquals(numRows, eventos.size());
    }

    @Test
    public void testFindPavimentoDependingOnConcello() throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.tipo_pavimento WHERE codigo_municipio = '27018'");
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
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.ancho_plataforma WHERE codigo_municipio = '27018'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setConcello("27018"); // Fonsagrada
	Tramos tramos = Catalog.getTramosAnchoPlataforma();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindCotasDependingOnConcello() throws SQLException,
    DBException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.cotas WHERE codigo_municipio = '27018'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setConcello("27018"); // Fonsagrada
	Tramos tramos = Catalog.getTramosCotas();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindAforosDependingOnConcello() throws SQLException,
    DBException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.aforos WHERE codigo_municipio = '27018'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setConcello("27018"); // Fonsagrada
	Eventos eventos = Catalog.getEventosAforos();

	assertEquals(numRows, eventos.size());
    }

    @Test
    public void testFindPavimentoDependingOnCarreteraAndConcello()
	    throws SQLException, DBException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.tipo_pavimento WHERE codigo_carretera = '4606' AND codigo_municipio = '27018'");
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
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.ancho_plataforma WHERE codigo_carretera = '4606' AND codigo_municipio = '27018'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setConcello("27018"); // Fonsagrada
	Tramos tramos = Catalog.getTramosAnchoPlataforma();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindCotasDependingOnCarreteraAndConcello()
	    throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.cotas WHERE codigo_carretera = '4606' AND codigo_municipio = '27018'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setConcello("27018"); // Fonsagrada
	Tramos tramos = Catalog.getTramosCotas();

	assertEquals(numRows, tramos.size());
    }

    @Test
    public void testFindAforosDependingOnCarreteraAndConcello()
	    throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.aforos WHERE codigo_carretera = '4606' AND codigo_municipio = '27018'");
	rs.next();
	int numRows = rs.getInt("num_rows");

	Catalog.clear();
	Catalog.setCarretera("4606");
	Catalog.setConcello("27018"); // Fonsagrada
	Eventos eventos = Catalog.getEventosAforos();

	assertEquals(numRows, eventos.size());
    }

    @Test
    public void testFilterConcelloDependingOnCarretera() throws SQLException {
	Catalog.setCarretera("4606");
	Concellos cs = Catalog.getConcellos();
	int numConcellos = cs.size();

	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.carretera_municipio WHERE codigo_carretera = '4606'");
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
		.executeQuery("SELECT Count(*) AS num_rows FROM inventario.carretera_municipio WHERE codigo_carretera = '4606'");
	rs2.next();
	int numRows = rs2.getInt("num_rows");

	assertEquals(numRows, numConcellos);
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
	c.close();
    }

}
