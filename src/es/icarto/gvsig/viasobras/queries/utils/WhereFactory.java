package es.icarto.gvsig.viasobras.queries.utils;

import java.util.ArrayList;
import java.util.List;

import es.icarto.gvsig.viasobras.domain.catalog.Catalog;

public class WhereFactory {

    public static String create(boolean hasWhere, String queryCode,
	    String mayorValue, String menorValue, String textValue) {

	List<String> numericQueries = new ArrayList<String>();
	numericQueries.add("C10");
	numericQueries.add("C30");
	numericQueries.add("C40");
	List<String> textQueries = new ArrayList<String>();
	textQueries.add("C20");
	List<String> specialQueries = new ArrayList<String>();
	specialQueries.add("C02");

	String whereSQL;
	whereSQL = checkIfHasWhere(hasWhere);
	whereSQL = getWhereCarretera(whereSQL);
	whereSQL = getWhereMunicipio(whereSQL);
	if (numericQueries.contains(queryCode)) {
	    whereSQL = getWhereCaracteristicaCompare(whereSQL, mayorValue, menorValue);
	} else if (textQueries.contains(queryCode)) {
	    whereSQL = getWhereCaracteristicaEquals(whereSQL, textValue);
	} else if (specialQueries.contains(queryCode)) {
	    whereSQL = getWhereCategoriaCarretera(whereSQL, textValue);
	}
	whereSQL = checkIfIsVoid(whereSQL);
	return whereSQL;
    }

    private static String getWhereCategoriaCarretera(String whereSQL,
	    String textValue) {
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
	if (whereSQL.equalsIgnoreCase("WHERE 1=1 ")) {
	    whereSQL = "";
	}
	return whereSQL;
    }

    private static String getWhereMunicipio(String where) {
	if (!Catalog.getConcelloSelected().equalsIgnoreCase(
		Catalog.CONCELLO_ALL)) {
	    String municipioValue = Catalog.getConcelloSelected();
	    where = where + " AND i.codigo_municipio = '"
		    + municipioValue
		    + "'";
	}
	return where;
    }

    private static String getWhereCarretera(String where) {
	if (!Catalog.getCarreteraSelected().equalsIgnoreCase(
		Catalog.CARRETERA_ALL)) {
	    String carreteraValue = Catalog.getCarreteraSelected();
	    where = where + " i.codigo_carretera = '"
		    + carreteraValue
		    + "'";
	} else {
	    where = where + " 1=1 ";
	}
	return where;
    }

}
