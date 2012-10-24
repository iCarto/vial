package es.icarto.gvsig.viasobras.queries.utils;

import java.util.ArrayList;
import java.util.List;

import es.icarto.gvsig.viasobras.domain.catalog.Catalog;

public class WhereFactory {

    public static String create(boolean hasWhere, String queryCode,
	    String carreteraCode, String municipioCode,
	    String mayorValue, String menorValue, String textValue) {

	List<String> numericQueries = new ArrayList<String>();
	numericQueries.add("C10");
	numericQueries.add("C30");
	numericQueries.add("C31");
	numericQueries.add("C32");
	numericQueries.add("C33");
	numericQueries.add("C34");
	numericQueries.add("C35");
	numericQueries.add("C36");
	numericQueries.add("C37");
	numericQueries.add("C40");
	List<String> textQueries = new ArrayList<String>();
	textQueries.add("C20");
	List<String> specialQueries = new ArrayList<String>();
	specialQueries.add("C02");
	specialQueries.add("C41");

	String whereSQL;
	whereSQL = checkIfHasWhere(hasWhere);
	if (numericQueries.contains(queryCode)) {
	    whereSQL = getWhereCarretera(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipio(whereSQL, municipioCode);
	    whereSQL = getWhereCaracteristicaCompare(whereSQL, mayorValue, menorValue);
	} else if (textQueries.contains(queryCode)) {
	    whereSQL = getWhereCarretera(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipio(whereSQL, municipioCode);
	    whereSQL = getWhereCaracteristicaEquals(whereSQL, textValue);
	} else if (specialQueries.contains(queryCode)) {
	    if (queryCode.equals("C02")) {
		whereSQL = getWhereCategoriaCarretera(whereSQL, carreteraCode,
			municipioCode, textValue);
	    } else if (queryCode.equals("C41")) {
		whereSQL = getWhereCotasMinimasMaximas(whereSQL, carreteraCode,
			municipioCode,
			mayorValue,
			menorValue);
	    }
	} else {
	    whereSQL = getWhereCarretera(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipio(whereSQL, municipioCode);
	}
	whereSQL = checkIfIsVoid(whereSQL);
	return whereSQL;
    }

    private static String getWhereCotasMinimasMaximas(String whereSQL,
	    String carreteraCode, String municipioCode, String mayorValue,
	    String menorValue) {
	// carretera
	if (!carreteraCode.equalsIgnoreCase(Catalog.CARRETERA_ALL)) {
	    whereSQL = whereSQL + " c.codigo_carretera = '" + carreteraCode
		    + "'";
	} else {
	    whereSQL = whereSQL + " 1=1 ";
	}
	// municipio
	if (!municipioCode.equalsIgnoreCase(Catalog.CONCELLO_ALL)) {
	    whereSQL = whereSQL + " AND c.codigo_municipio = '" + municipioCode
		    + "'";
	}
	// cotas
	if (!mayorValue.equalsIgnoreCase("")) {
	    whereSQL = whereSQL + " AND c.cota_maxima >= '" + mayorValue + "'";
	}
	if (!menorValue.equals("")) {
	    whereSQL = whereSQL + " AND c.cota_minima <= '" + menorValue + "'";
	}
	return whereSQL;
    }

    private static String getWhereCategoriaCarretera(String whereSQL,
	    String carreteraCode, String municipioCode, String textValue) {
	// carretera
	if (!carreteraCode.equalsIgnoreCase(Catalog.CARRETERA_ALL)) {
	    whereSQL = whereSQL + " c.numero = '" + carreteraCode
		    + "'";
	} else {
	    whereSQL = whereSQL + " 1=1 ";
	}
	// municipio
	if (!municipioCode.equalsIgnoreCase(Catalog.CONCELLO_ALL)) {
	    whereSQL = whereSQL
		    + " AND c.numero ~ "
		    + " (SELECT '('||array_to_string(array_agg(codigo_carretera),'|')||')' "
		    + "  FROM inventario.carretera_municipio "
		    + "  WHERE codigo_municipio = '" + municipioCode + "') ";
	}
	// categoria
	if (!textValue.equals("")) {
	    whereSQL = whereSQL + " AND c.categoria = '" + textValue + "'";
	}
	return whereSQL;
    }

    private static String getWhereCaracteristicaEquals(String whereSQL, String textValue) {
	if (!textValue.equals("")) {
	    whereSQL = whereSQL + " AND p.valor = '" + textValue + "'";
	}
	return whereSQL;
    }

    private static String getWhereCaracteristicaCompare(String where, String mayorValue,
	    String menorValue) {
	if (!mayorValue.equalsIgnoreCase("")) {
	    where = where + " AND p.valor >= '" + mayorValue + "'";
	}
	if (!menorValue.equals("")) {
	    where = where + " AND p.valor <= '" + menorValue + "'";
	}
	return where;
    }

    private static String checkIfHasWhere(boolean hasWhere) {
	String whereSQL;
	if (!hasWhere) {
	    whereSQL = "WHERE";
	} else {
	    whereSQL = " AND ";
	}
	return whereSQL;
    }

    private static String checkIfIsVoid(String whereSQL) {
	if (whereSQL.equalsIgnoreCase("WHERE 1=1 ")
		|| whereSQL.equalsIgnoreCase(" AND 1=1 ")) {
	    whereSQL = "";
	}
	return whereSQL;
    }

    private static String getWhereMunicipio(String where, String municipioCode) {
	if (!municipioCode.equalsIgnoreCase(Catalog.CONCELLO_ALL)) {
	    where = where + " AND i.codigo_municipio = '" + municipioCode
		    + "'";
	}
	return where;
    }

    private static String getWhereCarretera(String where, String carreteraCode) {
	if (!carreteraCode.equalsIgnoreCase(Catalog.CARRETERA_ALL)) {
	    where = where + " i.codigo_carretera = '" + carreteraCode
		    + "'";
	} else {
	    where = where + " 1=1 ";
	}
	return where;
    }

}
