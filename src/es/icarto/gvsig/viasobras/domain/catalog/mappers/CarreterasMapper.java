package es.icarto.gvsig.viasobras.domain.catalog.mappers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.icarto.gvsig.viasobras.domain.catalog.Carretera;
import es.icarto.gvsig.viasobras.domain.catalog.Carreteras;

public class CarreterasMapper extends DBFacade {

    public static String CODE = "codigo";

    /*
     * As carreteras is not going to change usually, is safe to asume that the
     * same carreteras are going to be the same during the whole session.
     */
    private static List<Carretera> carreteras = new ArrayList<Carretera>();
    private static Map<String, Integer> indexes = new HashMap<String, Integer>();

    public static Carreteras findAll() throws SQLException {
	if ((carreteras.size() > 0) && (carreteras.size() == indexes.size())) {
	    return new Carreteras(carreteras, indexes);
	}
	Connection c = DBFacade.getConnection();
	Statement stmt;
	try {
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT numero, codigo FROM inventario.carreteras ORDER BY numero");
	    int cIndex = 0;
	    while (rs.next()) {
		String code = rs.getString("numero");
		String value = rs.getString("codigo");
		carreteras.add(new Carretera(code, value));
		indexes.put(code, cIndex);
		cIndex++;
	    }
	    ResultSet rs2 = stmt
		    .executeQuery("SELECT codigo_carretera, codigo_municipio FROM inventario.carretera_municipio ORDER BY codigo_carretera");
	    while (rs2.next()) {
		String codeCarretera = rs2.getString("codigo_carretera");
		String codeConcello = rs2.getString("codigo_municipio");
		cIndex = indexes.get(codeCarretera);
		Carretera carretera = carreteras.get(cIndex);
		carretera.addConcelloAffected(codeConcello);
	    }
	    return new Carreteras(carreteras, indexes);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

}
