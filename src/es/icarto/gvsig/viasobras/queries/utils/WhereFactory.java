package es.icarto.gvsig.viasobras.queries.utils;

import java.util.ArrayList;
import java.util.List;

import es.icarto.gvsig.viasobras.domain.catalog.Catalog;

public class WhereFactory {

    /**
     * Params should be proper values, ie: mayorValue & menorValue are doubles,
     * textValue is year if the query is from aforos family, etc
     * 
     * @param hasWhere
     * @param queryCode
     * @param carreteraCode
     * @param municipioCode
     * @param mayorValue
     * @param menorValue
     * @param textValue
     * @return
     */
    public static String create(boolean hasWhere, String queryCode,
	    String carreteraCode, String municipioCode,
	    String mayorValue, String menorValue, String textValue) {

	List<String> numericQueries = new ArrayList<String>();
	List<String> textQueries = new ArrayList<String>();
	List<String> specialQueries = new ArrayList<String>();

	numericQueries.add("C10"); // ancho
	numericQueries.add("C40"); // cotas

	textQueries.add("C20"); // tipo firme

	specialQueries.add("C02"); // categoria
	specialQueries.add("C41"); // cotas min/max
	specialQueries.add("C30"); // aforos
	specialQueries.add("C31");
	specialQueries.add("C32");
	specialQueries.add("C33");
	specialQueries.add("C34");
	specialQueries.add("C35");
	specialQueries.add("C36");
	specialQueries.add("C37");
	specialQueries.add("C38");

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
	    } else { // any related to aforos
		whereSQL = getWhereCarretera(whereSQL, carreteraCode);
		whereSQL = getWhereMunicipio(whereSQL, municipioCode);
		whereSQL = getWhereCaracteristicaCompare(whereSQL, mayorValue,
			menorValue);
		whereSQL = getWhereAnho(whereSQL, textValue);
	    }
	} else {
	    whereSQL = getWhereCarretera(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipio(whereSQL, municipioCode);
	}
	whereSQL = checkIfIsVoid(whereSQL);
	return whereSQL;
    }

    private static String getWhereAnho(String whereSQL, String textValue) {
	if (!textValue.equals("")) {
	    whereSQL = whereSQL + " AND extract(year from p.fecha) = "
		    + textValue;
	}
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
