package es.icarto.gvsig.viasobras.tests.catalog;

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
import es.icarto.gvsig.viasobras.domain.catalog.Evento;
import es.icarto.gvsig.viasobras.domain.catalog.Eventos;
import es.icarto.gvsig.viasobras.domain.catalog.Tramo;
import es.icarto.gvsig.viasobras.domain.catalog.Tramos;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.DBFacade;

public class TestCatalogDelete {

    private static Connection c;
    private static String carreteraPlataforma;
    private static String concelloPlataforma;
    private static String carreteraPavimento;
    private static String concelloPavimento;
    private static String carreteraCota;
    private static String concelloCota;
    private static String carreteraAforo;
    private static String concelloAforo;
    private static String carreteraAccidente;
    private static String concelloAccidente;

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
	// useful methods to know municipio/carretera with most tramos
	setCarreteraAndConcelloForAccidente();
	setCarreteraAndConcelloForAforo();
	setCarreteraAndConcelloForCota();
	setCarreteraAndConcelloForPavimento();
	setCarreteraAndConcelloForPlataforma();
    }

    private static void setCarreteraAndConcelloForAccidente()
	    throws SQLException {
	// use the area (carretera, concello) with more tramos
	String sql = "SELECT codigo_carretera, codigo_municipio, COUNT(gid) AS tramos_count FROM inventario.accidentes GROUP BY codigo_carretera, codigo_municipio ORDER BY tramos_count DESC LIMIT 1";
	Statement stmt = c.createStatement();
	ResultSet rs = stmt.executeQuery(sql);
	rs.next();
	carreteraAccidente = rs.getString("codigo_carretera");
	concelloAccidente = rs.getString("codigo_municipio");
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

    private static void setCarreteraAndConcelloForCota() throws SQLException {
	// use the area (carretera, concello) with more tramos
	String sql = "SELECT codigo_carretera, codigo_municipio, COUNT(gid) AS tramos_count FROM inventario.cotas GROUP BY codigo_carretera, codigo_municipio ORDER BY tramos_count DESC LIMIT 1";
	Statement stmt = c.createStatement();
	ResultSet rs = stmt.executeQuery(sql);
	rs.next();
	carreteraCota = rs.getString("codigo_carretera");
	concelloCota = rs.getString("codigo_municipio");
    }

    private static void setCarreteraAndConcelloForAforo() throws SQLException {
	// use the area (carretera, concello) with more tramos
	String sql = "SELECT codigo_carretera, codigo_municipio, COUNT(gid) AS tramos_count FROM inventario.aforos GROUP BY codigo_carretera, codigo_municipio ORDER BY tramos_count DESC LIMIT 1";
	Statement stmt = c.createStatement();
	ResultSet rs = stmt.executeQuery(sql);
	rs.next();
	carreteraAforo = rs.getString("codigo_carretera");
	concelloAforo = rs.getString("codigo_municipio");
    }

    @Test
    public void testDeletePavimento() throws SQLException {
	String carretera = carreteraPavimento;
	String concello = concelloPavimento;
	String lastID = getLastId("inventario.tipo_pavimento", carretera,
		concello);
	String gid = deletePavimento(carretera, concello, lastID);

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
    public void testDeletePlataforma() throws SQLException {
	String carretera = carreteraPlataforma;
	String concello = concelloPlataforma;
	String lastID = getLastId("inventario.ancho_plataforma", carretera,
		concello);
	String gid = deletePlataforma(carretera, concello, lastID);

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
    public void testDeleteCota() throws SQLException {
	String carretera = carreteraCota;
	String concello = concelloCota;
	String lastID = getLastId("inventario.cotas", carretera, concello);
	String gid = deleteCota(carretera, concello, lastID);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.cotas WHERE codigo_carretera = '"
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
    public void testDeleteAforo() throws SQLException {
	String carretera = carreteraAforo;
	String concello = concelloAforo;
	String lastID = getLastIdAforos(carretera, concello);
	String gid = deleteAforo(carretera, concello, lastID);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.aforos WHERE codigo_carretera = '"
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
    public void testDeleteAccidente() throws SQLException {
	String carretera = carreteraAccidente;
	String concello = concelloAccidente;
	String lastID = getLastId("inventario.accidentes", carretera, concello);
	String gid = deleteAccidente(carretera, concello, lastID);

	// check if the later made effect
	Statement stmt = c.createStatement();
	ResultSet rs = stmt
		.executeQuery("SELECT gid FROM inventario.accidentes WHERE codigo_carretera = '"
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
    public void testDeleteLastTwoPavimentos() {
	Catalog.clear();
	Tramos tramosPavi;
	boolean thrown = false;
	try {
	    tramosPavi = Catalog.getTramosTipoPavimento();
	    int lastPosition = 2;
	    String lastID = "", lastCarretera = "", lastConcello = "";
	    int beforeLastPosition = 1;
	    String beforeLastID = "", beforeLastCarretera = "", beforeLastConcello = "";
	    for (Tramo t : tramosPavi) {
		if (t.getPosition() > lastPosition) {
		    lastPosition = t.getPosition();
		    lastID = t.getId();
		    lastCarretera = t.getCarretera();
		    lastConcello = t.getConcello();
		    beforeLastPosition = lastPosition - 1;
		}
	    }
	    for (Tramo t : tramosPavi) {
		if (t.getPosition() == beforeLastPosition) {
		    beforeLastID = t.getId();
		    beforeLastCarretera = t.getCarretera();
		    beforeLastConcello = t.getConcello();
		}
	    }
	    // System.out.println("[beforeLast pavimento] Carretera: "
	    // + beforeLastCarretera + " - Concello: "
	    // + beforeLastConcello + " - ID: " + beforeLastID
	    // + " - Position: " + beforeLastPosition);
	    // System.out.println("[last pavimento] Carretera: " + lastCarretera
	    // + " - Concello: " + lastConcello + " - ID: " + lastID
	    // + " - Position: " + lastPosition);
	    tramosPavi.removeTramo(beforeLastID);
	    tramosPavi.removeTramo(lastID);
	    tramosPavi.save();
	} catch (SQLException e) {
	    thrown = true;
	}
	assertEquals(false, thrown);
    }

    @Test
    public void testDeleteLastTwoAnchos() {
	Catalog.clear();
	Tramos ancho;
	boolean thrown = false;
	try {
	    ancho = Catalog.getTramosAnchoPlataforma();
	    int lastPosition = 2;
	    String lastID = "", lastCarretera = "", lastConcello = "";
	    int beforeLastPosition = 1;
	    String beforeLastID = "", beforeLastCarretera = "", beforeLastConcello = "";
	    for (Tramo t : ancho) {
		if (t.getPosition() > lastPosition) {
		    lastPosition = t.getPosition();
		    lastID = t.getId();
		    lastCarretera = t.getCarretera();
		    lastConcello = t.getConcello();
		    beforeLastPosition = lastPosition - 1;
		}
	    }
	    for (Tramo t : ancho) {
		if (t.getPosition() == beforeLastPosition) {
		    beforeLastID = t.getId();
		    beforeLastCarretera = t.getCarretera();
		    beforeLastConcello = t.getConcello();
		}
	    }
	    //	    System.out.println("[beforeLast ancho] Carretera: "
	    //		    + beforeLastCarretera + " - Concello: "
	    //		    + beforeLastConcello + " - ID: " + beforeLastID
	    //		    + " - Position: " + beforeLastPosition);
	    //	    System.out.println("[last ancho] Carretera: " + lastCarretera
	    //		    + " - Concello: " + lastConcello + " - ID: " + lastID
	    //		    + " - Position: " + lastPosition);
	    ancho.removeTramo(beforeLastID);
	    ancho.removeTramo(lastID);
	    ancho.save();
	} catch (SQLException e) {
	    thrown = true;
	}
	assertEquals(false, thrown);
    }

    @Test
    public void testDeleteLastTwoCotas() {
	Catalog.clear();
	Tramos cotas;
	boolean thrown = false;
	try {
	    cotas = Catalog.getTramosCotas();
	    int lastPosition = 2;
	    String lastID = "", lastCarretera = "", lastConcello = "";
	    int beforeLastPosition = 1;
	    String beforeLastID = "", beforeLastCarretera = "", beforeLastConcello = "";
	    for (Tramo t : cotas) {
		if (t.getPosition() > lastPosition) {
		    lastPosition = t.getPosition();
		    lastID = t.getId();
		    lastCarretera = t.getCarretera();
		    lastConcello = t.getConcello();
		    beforeLastPosition = lastPosition - 1;
		}
	    }
	    for (Tramo t : cotas) {
		if (t.getPosition() == beforeLastPosition) {
		    beforeLastID = t.getId();
		    beforeLastCarretera = t.getCarretera();
		    beforeLastConcello = t.getConcello();
		}
	    }
	    //	    System.out.println("[beforeLast cotas] Carretera: "
	    //		    + beforeLastCarretera + " - Concello: "
	    //		    + beforeLastConcello + " - ID: " + beforeLastID
	    //		    + " - Position: " + beforeLastPosition);
	    //	    System.out.println("[last cotas] Carretera: " + lastCarretera
	    //		    + " - Concello: " + lastConcello + " - ID: " + lastID
	    //		    + " - Position: " + lastPosition);
	    cotas.removeTramo(beforeLastID);
	    cotas.removeTramo(lastID);
	    cotas.save();
	} catch (SQLException e) {
	    thrown = true;
	}
	assertEquals(false, thrown);
    }

    @Test
    public void testDeleteLastTwoAccidentes() {
	Catalog.clear();
	Eventos accidentes;
	boolean thrown = false;
	try {
	    accidentes = Catalog.getEventosAccidentes();
	    int lastPosition = 2;
	    String lastID = "", lastCarretera = "", lastConcello = "";
	    int beforeLastPosition = 1;
	    String beforeLastID = "", beforeLastCarretera = "", beforeLastConcello = "";
	    for (Evento e : accidentes) {
		if (e.getPosition() > lastPosition) {
		    lastPosition = e.getPosition();
		    lastID = e.getId();
		    lastCarretera = e.getCarretera();
		    lastConcello = e.getConcello();
		    beforeLastPosition = lastPosition - 1;
		}
	    }
	    for (Evento e : accidentes) {
		if (e.getPosition() == beforeLastPosition) {
		    beforeLastID = e.getId();
		    beforeLastCarretera = e.getCarretera();
		    beforeLastConcello = e.getConcello();
		}
	    }
	    // System.out.println("[beforeLast accidente] Carretera: "
	    // + beforeLastCarretera + " - Concello: "
	    // + beforeLastConcello + " - ID: " + beforeLastID
	    // + " - Position: " + beforeLastPosition);
	    // System.out.println("[last accidente] Carretera: " + lastCarretera
	    // + " - Concello: " + lastConcello + " - ID: " + lastID
	    // + " - Position: " + lastPosition);
	    accidentes.removeEvento(beforeLastID);
	    accidentes.removeEvento(lastID);
	    accidentes.save();
	} catch (SQLException e) {
	    thrown = true;
	}
	assertEquals(false, thrown);
    }

    @Test
    public void testDeleteLastTwoAforos() {
	Catalog.clear();
	Eventos aforos;
	boolean thrown = false;
	try {
	    aforos = Catalog.getEventosAforos();
	    int lastPosition = 2;
	    String lastID = "", lastCarretera = "", lastConcello = "";
	    int beforeLastPosition = 1;
	    String beforeLastID = "", beforeLastCarretera = "", beforeLastConcello = "";
	    for (Evento e : aforos) {
		if (e.getPosition() > lastPosition) {
		    lastPosition = e.getPosition();
		    lastID = e.getId();
		    lastCarretera = e.getCarretera();
		    lastConcello = e.getConcello();
		    beforeLastPosition = lastPosition - 1;
		}
	    }
	    for (Evento e : aforos) {
		if (e.getPosition() == beforeLastPosition) {
		    beforeLastID = e.getId();
		    beforeLastCarretera = e.getCarretera();
		    beforeLastConcello = e.getConcello();
		}
	    }
	    // System.out.println("[beforeLast aforo] Carretera: "
	    // + beforeLastCarretera + " - Concello: "
	    // + beforeLastConcello + " - ID: " + beforeLastID
	    // + " - Position: " + beforeLastPosition);
	    // System.out.println("[last aforo] Carretera: " + lastCarretera
	    // + " - Concello: " + lastConcello + " - ID: " + lastID
	    // + " - Position: " + lastPosition);
	    aforos.removeEvento(beforeLastID);
	    aforos.removeEvento(lastID);
	    aforos.save();
	} catch (SQLException e) {
	    thrown = true;
	}
	assertEquals(false, thrown);
    }

    @Test
    public void testDeleteTwoPavimentosDeletesProperPavimentos()
	    throws SQLException {
	Catalog.clear();
	Tramos tramosPavi = Catalog.getTramosTipoPavimento();
	String idPositionA = "", idPositionB = "";
	String carreteraPositionA = "", concelloPositionA = "";
	String carreteraPositionB = "", concelloPositionB = "";
	boolean first = true;
	for (Tramo t : tramosPavi) {
	    if (first) {
		idPositionA = t.getId();
		carreteraPositionA = t.getCarretera();
		concelloPositionA = t.getConcello();
		first = false;
	    } else {
		idPositionB = t.getId();
		carreteraPositionB = t.getCarretera();
		concelloPositionB = t.getConcello();
		break;
	    }
	}
	tramosPavi.removeTramo(idPositionA);
	tramosPavi.removeTramo(idPositionB);
	tramosPavi.save();

	// check if the tramo A was deleted
	Statement stmtA = c.createStatement();
	ResultSet rsA = stmtA
		.executeQuery("SELECT gid FROM inventario.tipo_pavimento WHERE codigo_carretera = '"
			+ carreteraPositionA
			+ "' AND codigo_municipio = '"
			+ concelloPositionA + "'");
	boolean updatedA = true;
	while (rsA.next()) {
	    if (Integer.toString(rsA.getInt("gid")).equals(idPositionA)) {
		updatedA = false;
		break;
	    }
	}
	assertEquals(true, updatedA);

	// check if the tramo B was deleted
	Statement stmtB = c.createStatement();
	ResultSet rsB = stmtB
		.executeQuery("SELECT gid FROM inventario.tipo_pavimento WHERE codigo_carretera = '"
			+ carreteraPositionB
			+ "' AND codigo_municipio = '"
			+ concelloPositionB + "'");
	boolean updatedB = true;
	while (rsB.next()) {
	    if (Integer.toString(rsB.getInt("gid")).equals(idPositionB)) {
		updatedB = false;
		break;
	    }
	}
	assertEquals(true, updatedB);
    }

    @Test
    public void testDeleteTwoAnchosDeletesProperAnchos() throws SQLException {
	Catalog.clear();
	Tramos anchos = Catalog.getTramosAnchoPlataforma();
	String idPositionA = "", idPositionB = "";
	String carreteraPositionA = "", concelloPositionA = "";
	String carreteraPositionB = "", concelloPositionB = "";
	boolean first = true;
	for (Tramo t : anchos) {
	    if (first) {
		idPositionA = t.getId();
		carreteraPositionA = t.getCarretera();
		concelloPositionA = t.getConcello();
		first = false;
	    } else {
		idPositionB = t.getId();
		carreteraPositionB = t.getCarretera();
		concelloPositionB = t.getConcello();
		break;
	    }
	}
	anchos.removeTramo(idPositionA);
	anchos.removeTramo(idPositionB);
	anchos.save();

	// check if the tramo A was deleted
	Statement stmtA = c.createStatement();
	ResultSet rsA = stmtA
		.executeQuery("SELECT gid FROM inventario.ancho_plataforma WHERE codigo_carretera = '"
			+ carreteraPositionA
			+ "' AND codigo_municipio = '"
			+ concelloPositionA + "'");
	boolean updatedA = true;
	while (rsA.next()) {
	    if (Integer.toString(rsA.getInt("gid")).equals(idPositionA)) {
		updatedA = false;
		break;
	    }
	}
	assertEquals(true, updatedA);

	// check if the tramo B was deleted
	Statement stmtB = c.createStatement();
	ResultSet rsB = stmtB
		.executeQuery("SELECT gid FROM inventario.ancho_plataforma WHERE codigo_carretera = '"
			+ carreteraPositionB
			+ "' AND codigo_municipio = '"
			+ concelloPositionB + "'");
	boolean updatedB = true;
	while (rsB.next()) {
	    if (Integer.toString(rsB.getInt("gid")).equals(idPositionB)) {
		updatedB = false;
		break;
	    }
	}
	assertEquals(true, updatedB);
    }

    @Test
    public void testDeleteTwoCotasDeletesProperCotas() throws SQLException {
	Catalog.clear();
	Tramos cotas = Catalog.getTramosCotas();
	String idPositionA = "", idPositionB = "";
	String carreteraPositionA = "", concelloPositionA = "";
	String carreteraPositionB = "", concelloPositionB = "";
	boolean first = true;
	for (Tramo t : cotas) {
	    if (first) {
		idPositionA = t.getId();
		carreteraPositionA = t.getCarretera();
		concelloPositionA = t.getConcello();
		first = false;
	    } else {
		idPositionB = t.getId();
		carreteraPositionB = t.getCarretera();
		concelloPositionB = t.getConcello();
		break;
	    }
	}
	cotas.removeTramo(idPositionA);
	cotas.removeTramo(idPositionB);
	cotas.save();

	// check if the tramo A was deleted
	Statement stmtA = c.createStatement();
	ResultSet rsA = stmtA
		.executeQuery("SELECT gid FROM inventario.cotas WHERE codigo_carretera = '"
			+ carreteraPositionA
			+ "' AND codigo_municipio = '"
			+ concelloPositionA + "'");
	boolean updatedA = true;
	while (rsA.next()) {
	    if (Integer.toString(rsA.getInt("gid")).equals(idPositionA)) {
		updatedA = false;
		break;
	    }
	}
	assertEquals(true, updatedA);

	// check if the tramo B was deleted
	Statement stmtB = c.createStatement();
	ResultSet rsB = stmtB
		.executeQuery("SELECT gid FROM inventario.cotas WHERE codigo_carretera = '"
			+ carreteraPositionB
			+ "' AND codigo_municipio = '"
			+ concelloPositionB + "'");
	boolean updatedB = true;
	while (rsB.next()) {
	    if (Integer.toString(rsB.getInt("gid")).equals(idPositionB)) {
		updatedB = false;
		break;
	    }
	}
	assertEquals(true, updatedB);
    }

    @Test
    public void testDeleteTwoAforosDeletesProperAforos() throws SQLException {
	Catalog.clear();
	Eventos aforos = Catalog.getEventosAforos();
	String idPositionA = "", idPositionB = "";
	String carreteraPositionA = "", concelloPositionA = "";
	String carreteraPositionB = "", concelloPositionB = "";
	boolean first = true;
	for (Evento e : aforos) {
	    if (first) {
		idPositionA = e.getId();
		carreteraPositionA = e.getCarretera();
		concelloPositionA = e.getConcello();
		first = false;
	    } else {
		idPositionB = e.getId();
		carreteraPositionB = e.getCarretera();
		concelloPositionB = e.getConcello();
		break;
	    }
	}
	aforos.removeEvento(idPositionA);
	aforos.removeEvento(idPositionB);
	aforos.save();

	// check if the tramo A was deleted
	Statement stmtA = c.createStatement();
	ResultSet rsA = stmtA
		.executeQuery("SELECT gid FROM inventario.aforos WHERE codigo_carretera = '"
			+ carreteraPositionA
			+ "' AND codigo_municipio = '"
			+ concelloPositionA + "'");
	boolean updatedA = true;
	while (rsA.next()) {
	    if (Integer.toString(rsA.getInt("gid")).equals(idPositionA)) {
		updatedA = false;
		break;
	    }
	}
	assertEquals(true, updatedA);

	// check if the evento B was deleted
	Statement stmtB = c.createStatement();
	ResultSet rsB = stmtB
		.executeQuery("SELECT gid FROM inventario.aforos WHERE codigo_carretera = '"
			+ carreteraPositionB
			+ "' AND codigo_municipio = '"
			+ concelloPositionB + "'");
	boolean updatedB = true;
	while (rsB.next()) {
	    if (Integer.toString(rsB.getInt("gid")).equals(idPositionB)) {
		updatedB = false;
		break;
	    }
	}
	assertEquals(true, updatedB);
    }

    @Test
    public void testDeleteTwoAccidentesDeletesProperAccidentes()
	    throws SQLException {
	Catalog.clear();
	Eventos accidentes = Catalog.getEventosAccidentes();
	String idPositionA = "", idPositionB = "";
	String carreteraPositionA = "", concelloPositionA = "";
	String carreteraPositionB = "", concelloPositionB = "";
	boolean first = true;
	for (Evento e : accidentes) {
	    if (first) {
		idPositionA = e.getId();
		carreteraPositionA = e.getCarretera();
		concelloPositionA = e.getConcello();
		first = false;
	    } else {
		idPositionB = e.getId();
		carreteraPositionB = e.getCarretera();
		concelloPositionB = e.getConcello();
		break;
	    }
	}
	accidentes.removeEvento(idPositionA);
	accidentes.removeEvento(idPositionB);
	accidentes.save();

	// check if the tramo A was deleted
	Statement stmtA = c.createStatement();
	ResultSet rsA = stmtA
		.executeQuery("SELECT gid FROM inventario.accidentes WHERE codigo_carretera = '"
			+ carreteraPositionA
			+ "' AND codigo_municipio = '"
			+ concelloPositionA + "'");
	boolean updatedA = true;
	while (rsA.next()) {
	    if (Integer.toString(rsA.getInt("gid")).equals(idPositionA)) {
		updatedA = false;
		break;
	    }
	}
	assertEquals(true, updatedA);

	// check if the evento B was deleted
	Statement stmtB = c.createStatement();
	ResultSet rsB = stmtB
		.executeQuery("SELECT gid FROM inventario.accidentes WHERE codigo_carretera = '"
			+ carreteraPositionB
			+ "' AND codigo_municipio = '"
			+ concelloPositionB + "'");
	boolean updatedB = true;
	while (rsB.next()) {
	    if (Integer.toString(rsB.getInt("gid")).equals(idPositionB)) {
		updatedB = false;
		break;
	    }
	}
	assertEquals(true, updatedB);
    }

    @Test
    public void testDeleteTwoPavimentosActuallyDeletesTwoPavimentos()
	    throws SQLException {
	Catalog.clear();
	Tramos pavimentos = Catalog.getTramosTipoPavimento();

	int tramosSizeStart = pavimentos.size();
	Statement stmtA = c.createStatement();
	ResultSet rsA = stmtA
		.executeQuery("SELECT COUNT(*) AS n_tramos FROM inventario.tipo_pavimento");
	int tramosInDBStart = 0;
	while (rsA.next()) {
	    tramosInDBStart = rsA.getInt("n_tramos");
	}

	String idPositionA = "", idPositionB = "";
	boolean first = true;
	for (Tramo t : pavimentos) {
	    if (first) {
		idPositionA = t.getId();
		first = false;
	    } else {
		idPositionB = t.getId();
		break;
	    }
	}
	pavimentos.removeTramo(idPositionA);
	pavimentos.removeTramo(idPositionB);
	Tramos tramosPaviNew = pavimentos.save();

	int tramosSizeEnd = tramosPaviNew.size();
	Statement stmtB = c.createStatement();
	ResultSet rsB = stmtB
		.executeQuery("SELECT COUNT(*) AS n_tramos FROM inventario.tipo_pavimento");
	int tramosInDBEnd = 0;
	while (rsB.next()) {
	    tramosInDBEnd = rsB.getInt("n_tramos");
	}

	assertEquals(tramosInDBStart, tramosSizeStart);
	assertEquals(tramosInDBEnd, tramosSizeEnd);
	assertEquals(tramosInDBEnd, tramosSizeStart - 2);
	assertEquals(tramosInDBEnd, tramosSizeEnd);

    }

    @Test
    public void testDeleteTwoAnchosActuallyDeletesTwoAnchos()
	    throws SQLException {
	Catalog.clear();
	Tramos anchos = Catalog.getTramosAnchoPlataforma();

	int tramosSizeStart = anchos.size();
	Statement stmtA = c.createStatement();
	ResultSet rsA = stmtA
		.executeQuery("SELECT COUNT(*) AS n_tramos FROM inventario.ancho_plataforma");
	int tramosInDBStart = 0;
	while (rsA.next()) {
	    tramosInDBStart = rsA.getInt("n_tramos");
	}

	String idPositionA = "", idPositionB = "";
	boolean first = true;
	for (Tramo t : anchos) {
	    if (first) {
		idPositionA = t.getId();
		first = false;
	    } else {
		idPositionB = t.getId();
		break;
	    }
	}
	anchos.removeTramo(idPositionA);
	anchos.removeTramo(idPositionB);
	Tramos tramosPaviNew = anchos.save();

	int tramosSizeEnd = tramosPaviNew.size();
	Statement stmtB = c.createStatement();
	ResultSet rsB = stmtB
		.executeQuery("SELECT COUNT(*) AS n_tramos FROM inventario.ancho_plataforma");
	int tramosInDBEnd = 0;
	while (rsB.next()) {
	    tramosInDBEnd = rsB.getInt("n_tramos");
	}

	assertEquals(tramosInDBStart, tramosSizeStart);
	assertEquals(tramosInDBEnd, tramosSizeEnd);
	assertEquals(tramosInDBEnd, tramosSizeStart - 2);
	assertEquals(tramosInDBEnd, tramosSizeEnd);
    }

    @Test
    public void testDeleteTwoCotasActuallyDeletesTwoCotas()
	    throws SQLException {
	Catalog.clear();
	Tramos cotas = Catalog.getTramosCotas();

	int tramosSizeStart = cotas.size();
	Statement stmtA = c.createStatement();
	ResultSet rsA = stmtA
		.executeQuery("SELECT COUNT(*) AS n_tramos FROM inventario.cotas");
	int tramosInDBStart = 0;
	while (rsA.next()) {
	    tramosInDBStart = rsA.getInt("n_tramos");
	}

	String idPositionA = "", idPositionB = "";
	boolean first = true;
	for (Tramo t : cotas) {
	    if (first) {
		idPositionA = t.getId();
		first = false;
	    } else {
		idPositionB = t.getId();
		break;
	    }
	}
	cotas.removeTramo(idPositionA);
	cotas.removeTramo(idPositionB);
	Tramos cotasNew = cotas.save();

	int tramosSizeEnd = cotasNew.size();
	Statement stmtB = c.createStatement();
	ResultSet rsB = stmtB
		.executeQuery("SELECT COUNT(*) AS n_tramos FROM inventario.cotas");
	int tramosInDBEnd = 0;
	while (rsB.next()) {
	    tramosInDBEnd = rsB.getInt("n_tramos");
	}

	assertEquals(tramosInDBStart, tramosSizeStart);
	assertEquals(tramosInDBEnd, tramosSizeEnd);
	assertEquals(tramosInDBEnd, tramosSizeStart - 2);
	assertEquals(tramosInDBEnd, tramosSizeEnd);
    }

    @Test
    public void testDeleteTwoAforosActuallyDeletesTwoAforos()
	    throws SQLException {
	Catalog.clear();
	Eventos aforos = Catalog.getEventosAforos();
	int eventosSizeStart= aforos.size();
	Statement stmtA = c.createStatement();
	ResultSet rsA = stmtA
		.executeQuery("SELECT COUNT(*) AS n_tramos FROM inventario.aforos");
	int eventosInDBStart = 0;
	while (rsA.next()) {
	    eventosInDBStart = rsA.getInt("n_tramos");
	}
	String idPositionA = "", idPositionB = "";
	boolean first = true;
	for (Evento e : aforos) {
	    if (first) {
		idPositionA = e.getId();
		first = false;
	    } else {
		idPositionB = e.getId();
		break;
	    }
	}
	aforos.removeEvento(idPositionA);
	aforos.removeEvento(idPositionB);
	Eventos aforosNew = aforos.save();

	int eventosSizeEnd = aforosNew.size();

	// check if the tramo B was deleted
	Statement stmtB = c.createStatement();
	ResultSet rsB = stmtB
		.executeQuery("SELECT COUNT(*) AS n_tramos FROM inventario.aforos");
	int eventosInDBEnd = 0;
	while (rsB.next()) {
	    eventosInDBEnd = rsB.getInt("n_tramos");
	}
	assertEquals(eventosInDBEnd, eventosInDBStart - 2);
	assertEquals(eventosSizeEnd, eventosSizeStart - 2);
    }

    @Test
    public void testDeleteTwoAccidentesActuallyDeletesTwoAccidentes()
	    throws SQLException {
	Catalog.clear();
	Eventos accidentes = Catalog.getEventosAccidentes();
	int eventosSizeStart = accidentes.size();
	Statement stmtA = c.createStatement();
	ResultSet rsA = stmtA
		.executeQuery("SELECT COUNT(*) AS n_tramos FROM inventario.accidentes");
	int eventosInDBStart = 0;
	while (rsA.next()) {
	    eventosInDBStart = rsA.getInt("n_tramos");
	}
	String idPositionA = "", idPositionB = "";
	boolean first = true;
	for (Evento e : accidentes) {
	    if (first) {
		idPositionA = e.getId();
		first = false;
	    } else {
		idPositionB = e.getId();
		break;
	    }
	}
	accidentes.removeEvento(idPositionA);
	accidentes.removeEvento(idPositionB);
	Eventos accidentesNew = accidentes.save();

	int eventosSizeEnd = accidentesNew.size();

	// check if the tramo B was deleted
	Statement stmtB = c.createStatement();
	ResultSet rsB = stmtB
		.executeQuery("SELECT COUNT(*) AS n_tramos FROM inventario.accidentes");
	int eventosInDBEnd = 0;
	while (rsB.next()) {
	    eventosInDBEnd = rsB.getInt("n_tramos");
	}
	assertEquals(eventosInDBEnd, eventosInDBStart - 2);
	assertEquals(eventosSizeEnd, eventosSizeStart - 2);
	assertEquals(eventosInDBEnd, eventosSizeEnd);
	assertEquals(eventosInDBStart, eventosSizeStart);
    }

    private String deletePlataforma(String carretera, String concello,
	    String gid) throws SQLException {

	// add new tramo
	Catalog.clear();
	Catalog.setCarretera(carretera);
	Catalog.setConcello(concello);
	Tramos tramos = Catalog.getTramosAnchoPlataforma();
	tramos.removeTramo(gid);
	tramos.save();
	return gid;
    }

    private String deletePavimento(String carretera, String concello, String gid)
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

    private String deleteCota(String carretera, String concello, String gid)
	    throws SQLException {

	// add new tramo
	Catalog.clear();
	Catalog.setCarretera(carretera);
	Catalog.setConcello(concello);
	Tramos tramos = Catalog.getTramosCotas();
	tramos.removeTramo(gid);
	tramos.save();
	return gid;
    }

    private String deleteAccidente(String carretera, String concello, String gid)
	    throws SQLException {
	// add new tramo
	Catalog.clear();
	Catalog.setCarretera(carretera);
	Catalog.setConcello(concello);
	Eventos eventos = Catalog.getEventosAccidentes();
	eventos.removeEvento(gid);
	eventos.save();
	return gid;
    }

    private String deleteAforo(String carretera, String concello, String gid)
	    throws SQLException {
	// add new tramo
	Catalog.clear();
	Catalog.setCarretera(carretera);
	Catalog.setConcello(concello);
	Eventos eventos = Catalog.getEventosAforos();
	eventos.removeEvento(gid);
	eventos.save();
	return gid;
    }

    private String getLastIdAforos(String carretera, String concello)
	    throws SQLException {
	Statement stmt = c.createStatement();
	String sqlQuery = "WITH p AS ("
		+ "SELECT codigo_carretera, codigo_municipio, tramo, MAX(fecha) AS fecha_ultimo_aforo "
		+ " FROM inventario.aforos "
		+ " GROUP BY codigo_carretera, codigo_municipio, tramo "
		+ "ORDER BY codigo_carretera, codigo_municipio, tramo) "
		+ "SELECT i.gid "
		+ " FROM inventario.aforos AS i, p "
		+ " WHERE i.codigo_carretera = p.codigo_carretera AND i.codigo_municipio = p.codigo_municipio "
		+ "	  AND i.tramo = p.tramo AND i.fecha = p.fecha_ultimo_aforo "
		+ "	  AND i.codigo_carretera = '" + carretera + "'"
		+ "	  AND i.codigo_municipio = '" + concello + "'"
		+ " ORDER BY gid DESC LIMIT 1";
	ResultSet rs = stmt.executeQuery(sqlQuery);
	rs.next();
	return Integer.toString(rs.getInt("gid"));
    }

    private String getLastId(String tableName, String carretera, String concello)
	    throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT gid FROM " + tableName
		+ " WHERE codigo_carretera = '" + carretera
		+ "' AND codigo_municipio = '" + concello
		+ "' ORDER BY gid DESC LIMIT 1");
	rs.next();
	return Integer.toString(rs.getInt("gid"));
    }

}
