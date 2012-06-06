package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Carreteras extends DomainMapper implements Iterable<Carretera> {

    private static List<Carretera> carreteras = new ArrayList<Carretera>();
    private static HashMap<String, Integer> cIndexes = new HashMap<String, Integer>();

    public static Carreteras findAll() {
	if (carreteras.size() > 0) {
	    return new Carreteras();
	}
	Connection c = DomainMapper.getConnection();
	Statement stmt;
	try {
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT codigo, código_pr FROM inventario.rede_carreteras ORDER BY codigo");
	    int cIndex = 0;
	    while (rs.next()) {
		String code = rs.getString("codigo");
		String value = rs.getString("código_pr");
		carreteras.add(new Carretera(code, value));
		cIndexes.put(code, cIndex);
		cIndex++;
	    }
	    ResultSet rs2 = stmt
		    .executeQuery("SELECT codigo_carretera, codigo_concello FROM inventario.carreteras_concellos ORDER BY codigo_carretera");
	    while (rs2.next()) {
		String codeCarretera = rs2.getString("codigo_carretera");
		String codeConcello = rs2.getString("codigo_concello");
		cIndex = cIndexes.get(codeCarretera);
		Carretera carretera = carreteras.get(cIndex);
		carretera.addConcelloAffected(codeConcello);
	    }
	    return new Carreteras();
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static Carretera getCarretera(String codeCarretera) {
	int cIndex = cIndexes.get(codeCarretera);
	return carreteras.get(cIndex);
    }

    public Iterator<Carretera> iterator() {
	return carreteras.iterator();
    }

    public int size() {
	return carreteras.size();
    }

}
