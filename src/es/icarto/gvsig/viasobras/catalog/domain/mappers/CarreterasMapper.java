package es.icarto.gvsig.viasobras.catalog.domain.mappers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.icarto.gvsig.viasobras.catalog.domain.Carretera;
import es.icarto.gvsig.viasobras.catalog.domain.Carreteras;

public class CarreterasMapper extends DomainMapper {

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
		indexes.put(code, cIndex);
		cIndex++;
	    }
	    ResultSet rs2 = stmt
		    .executeQuery("SELECT codigo_carretera, codigo_concello FROM inventario.carreteras_concellos ORDER BY codigo_carretera");
	    while (rs2.next()) {
		String codeCarretera = rs2.getString("codigo_carretera");
		String codeConcello = rs2.getString("codigo_concello");
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
