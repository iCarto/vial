package es.icarto.gvsig.viasobras.queries.tests;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import es.icarto.gvsig.viasobras.domain.catalog.Catalog;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.DBFacade;
import es.icarto.gvsig.viasobras.queries.utils.WhereFactory;

public class QueriesTests {

    private static Connection c;

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

    @AfterClass
    public static void closeConnection() throws SQLException {
	c.close();
    }

    @Test
    public void testBare() throws SQLException {
	List<String> codigos = getCodigos();
	for (String codigo : codigos) {
	    String values[] = getValues(codigo);
	    boolean hasWhere = values[0].equals("SI");
	    String consulta = values[1];
	    try {
		String carreteraCode = Catalog.CARRETERA_ALL;
		String municipioCode = Catalog.CONCELLO_ALL;
		String mayorValue = "";
		String menorValue = "";
		String textValue = "";
		String where = WhereFactory.create(hasWhere, codigo,
			carreteraCode, municipioCode, mayorValue, menorValue,
			textValue);
		consulta = consulta.replaceAll("\\[\\[WHERE\\]\\]", where);
		executeQuery(consulta);
	    } catch (SQLException e) {
		System.out.println("");
		System.out.println("");
		System.out.println("Test Bare - Failed query: " + codigo);
		System.out.println(consulta);
		assertTrue(false);
	    }
	}
	assertTrue(true);
    }

    @Test
    public void testWithCarretera() throws SQLException {
	List<String> codigos = getCodigos();
	for (String codigo : codigos) {
	    String values[] = getValues(codigo);
	    boolean hasWhere = values[0].equals("SI");
	    String consulta = values[1];

	    try {
		String carreteraCode = "27001";
		String municipioCode = Catalog.CONCELLO_ALL;
		String mayorValue = "";
		String menorValue = "";
		String textValue = "";
		String where = WhereFactory.create(hasWhere, codigo,
			carreteraCode, municipioCode, mayorValue, menorValue,
			textValue);
		consulta = consulta.replaceAll("\\[\\[WHERE\\]\\]", where);
		executeQuery(consulta);
	    } catch (SQLException e) {
		System.out.println("");
		System.out.println("");
		System.out.println("Test with carretera - Failed query: "
			+ codigo);
		System.out.println(consulta);
		assertTrue(false);
	    }
	}
	assertTrue(true);
    }

    @Test
    public void testWithMunicipio() throws SQLException {
	List<String> codigos = getCodigos();
	for (String codigo : codigos) {
	    String values[] = getValues(codigo);
	    boolean hasWhere = values[0].equals("SI");
	    String consulta = values[1];
	    try {
		String carreteraCode = Catalog.CARRETERA_ALL;
		String municipioCode = "1";
		String mayorValue = "";
		String menorValue = "";
		String textValue = "";
		String where = WhereFactory.create(hasWhere, codigo,
			carreteraCode, municipioCode, mayorValue, menorValue,
			textValue);
		consulta = consulta.replaceAll("\\[\\[WHERE\\]\\]", where);
		executeQuery(consulta);
	    } catch (SQLException e) {
		System.out.println("");
		System.out.println("");
		System.out.println("Test with municipio - Failed query: "
			+ codigo);
		System.out.println(consulta);
		assertTrue(false);
	    }
	}
	assertTrue(true);
    }

    @Test
    public void testWithCarreteraAndMunicipio() throws SQLException {
	List<String> codigos = getCodigos();
	for (String codigo : codigos) {
	    String values[] = getValues(codigo);
	    boolean hasWhere = values[0].equals("SI");
	    String consulta = values[1];
	    try {
		String carreteraCode = "27001";
		String municipioCode = "1";
		String mayorValue = "";
		String menorValue = "";
		String textValue = "";
		String where = WhereFactory.create(hasWhere, codigo,
			carreteraCode, municipioCode, mayorValue, menorValue,
			textValue);
		consulta = consulta.replaceAll("\\[\\[WHERE\\]\\]", where);
		executeQuery(consulta);
	    } catch (SQLException e) {
		System.out.println("");
		System.out.println("");
		System.out
		.println("Test with carretera and municipio - Failed query: "
			+ codigo);
		System.out.println(consulta);
		assertTrue(false);
	    }
	}
	assertTrue(true);
    }

    @Test
    public void testWithNumericFilter() throws SQLException {
	List<String> codigos = getCodigos();
	for (String codigo : codigos) {
	    String values[] = getValues(codigo);
	    boolean hasWhere = values[0].equals("SI");
	    String consulta = values[1];
	    try {
		String carreteraCode = Catalog.CARRETERA_ALL;
		String municipioCode = Catalog.CONCELLO_ALL;
		String mayorValue = "3";
		String menorValue = "1";
		String textValue = "";
		String where = WhereFactory.create(hasWhere, codigo,
			carreteraCode, municipioCode, mayorValue, menorValue,
			textValue);
		consulta = consulta.replaceAll("\\[\\[WHERE\\]\\]", where);
		executeQuery(consulta);
	    } catch (SQLException e) {
		System.out.println("");
		System.out.println("");
		System.out.println("Test with numeric filter - Failed query: "
			+ codigo);
		System.out.println(consulta);
		assertTrue(false);
	    }
	}
	assertTrue(true);
    }

    @Test
    public void testWithTextFilter() throws SQLException {
	List<String> codigos = getCodigos();
	for (String codigo : codigos) {
	    String values[] = getValues(codigo);
	    boolean hasWhere = values[0].equals("SI");
	    String consulta = values[1];

	    try {
		String carreteraCode = Catalog.CARRETERA_ALL;
		String municipioCode = Catalog.CONCELLO_ALL;
		String mayorValue = "";
		String menorValue = "";
		String textValue = "2011";
		String where = WhereFactory.create(hasWhere, codigo,
			carreteraCode, municipioCode, mayorValue, menorValue,
			textValue);
		consulta = consulta.replaceAll("\\[\\[WHERE\\]\\]", where);
		executeQuery(consulta);
	    } catch (SQLException e) {
		System.out.println("");
		System.out.println("");
		System.out.println("Test with text filter - Failed query: "
			+ codigo);
		System.out.println(consulta);
		assertTrue(false);
	    }
	}
	assertTrue(true);
    }

    @Test
    public void testWithNumericAndTextFilter() throws SQLException {
	List<String> codigos = getCodigos();
	for (String codigo : codigos) {
	    String values[] = getValues(codigo);
	    boolean hasWhere = values[0].equals("SI");
	    String consulta = values[1];
	    try {
		String carreteraCode = Catalog.CARRETERA_ALL;
		String municipioCode = Catalog.CONCELLO_ALL;
		String mayorValue = "1";
		String menorValue = "3";
		String textValue = "2011";
		String where = WhereFactory.create(hasWhere, codigo,
			carreteraCode,
			municipioCode, mayorValue, menorValue, textValue);
		consulta = consulta.replaceAll("\\[\\[WHERE\\]\\]", where);
		executeQuery(consulta);
	    } catch (SQLException e) {
		System.out.println("");
		System.out.println("");
		System.out.println("Test with text filter - Failed query: "
			+ codigo);
		System.out.println("Consulta: " + consulta);
		assertTrue(false);
	    }
	}
	assertTrue(true);
    }

    private void executeQuery(String consulta)
	    throws SQLException {
	Statement stmt2 = c.createStatement();
	ResultSet rs2 = stmt2.executeQuery(consulta);
	rs2.next();
	stmt2.close();
	rs2.close();
    }

    private String[] getValues(String codigoConsulta) throws SQLException {
	String sql = "SELECT hasWhere, consulta FROM consultas.consultas WHERE codigo = '"
		+ codigoConsulta + "'";
	Statement stmt = c.createStatement();
	ResultSet rs = stmt.executeQuery(sql);
	rs.next();
	String consulta = rs.getString("consulta");
	String hasWhere = rs.getString("hasWhere");
	stmt.close();
	rs.close();
	return new String[] { hasWhere, consulta };
    }

    private List<String> getCodigos() throws SQLException {
	String sql = "SELECT codigo FROM consultas.consultas";
	Statement stmt = c.createStatement();
	ResultSet rs = stmt.executeQuery(sql);
	List<String> codigos = new ArrayList<String>();
	while (rs.next()) {
	    codigos.add(rs.getString("codigo"));
	}
	stmt.close();
	rs.close();
	return codigos;
    }

}
