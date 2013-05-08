package es.icarto.gvsig.viasobras.queries.utils;

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
    public static String create(boolean hasWhere,
	    int queryCode,
	    String carreteraCode,
	    String municipioCode,
	    String mayorValue,
	    String menorValue,
	    String textValue) {

	String whereSQL = checkIfHasWhere(hasWhere);

	switch (queryCode) {
	case 02:
	    whereSQL = getWhereCategoriaCarretera(whereSQL,
		    carreteraCode,
		    municipioCode,
		    textValue);
	    break;
	case 10:
	    whereSQL = getWhereCarretera(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipio(whereSQL, municipioCode);
	    whereSQL = getWhereCaracteristicaCompareC10(whereSQL,
		    mayorValue,
		    menorValue);
	    break;
	case 20:
	    whereSQL = getWhereCarretera(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipio(whereSQL, municipioCode);
	    whereSQL = getWhereCaracteristicaEquals(whereSQL, textValue);
	    break;
	case 30:
	case 31:
	case 32:
	case 33:
	case 34:
	case 35:
	case 36:
	case 37:
	    whereSQL = getWhereCarretera(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipio(whereSQL, municipioCode);
	    whereSQL = getWhereAforo(whereSQL, mayorValue, menorValue);
	    whereSQL = getWhereAnhoAforos(whereSQL, textValue);
	    break;
	case 38:
	    whereSQL = getWhereAnhoC38(whereSQL, textValue);
	    break;
	case 40:
	    whereSQL = getWhereCarretera(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipio(whereSQL, municipioCode);
	    whereSQL = getWhereCaracteristicaCompare(whereSQL,
		    mayorValue,
		    menorValue);
	    break;
	case 41:
	    whereSQL = getWhereCotasMinimasMaximas(whereSQL,
		    carreteraCode,
		    municipioCode,
		    mayorValue,
		    menorValue);
	    break;
	case 50:
	    whereSQL = getWhereCarretera(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipio(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoC50(whereSQL, textValue);
	    break;
	case 51:
	case 52:
	    whereSQL = getWhereCarretera(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipio(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoC51C52(whereSQL, textValue);
	    break;
	default:
	    // any other query
	    whereSQL = getWhereCarretera(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipio(whereSQL, municipioCode);
	    break;
	}

	whereSQL = checkIfIsVoid(whereSQL);
	return whereSQL;

    }

    public static String createActuaciones(boolean hasWhere,
	    int queryCode,
	    String carreteraCode,
	    String municipioCode,
	    String anho,
	    String valor) {
	String whereSQL = checkIfHasWhere(hasWhere);

	switch (queryCode) {
	case 01:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.accidente_fecha", anho);
	    whereSQL = getWhereValorActuaciones(whereSQL, "actuaciones.accidente_tipo", valor);
	    break;
	case 02:
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.accidente_fecha", anho);
	    whereSQL = getWhereValorActuaciones(whereSQL, "actuaciones.accidente_tipo", valor);
	    break;
	case 10:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL,
		    "actuaciones.autorizacion_fecha_autorizacion", anho);
	    whereSQL = getWhereValorActuaciones(whereSQL, "actuaciones.autorizacion_tipo", valor);
	    break;
	case 11:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL,
		    "actuaciones.autorizacion_fecha_autorizacion", anho);
	    whereSQL = getWhereValorActuaciones(whereSQL, "actuaciones.autorizacion_peticionario", valor);
	    break;
	case 12:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL,
		    "actuaciones.autorizacion_fecha_autorizacion", anho);
	    whereSQL = getWhereValorActuaciones(whereSQL, "actuaciones.autorizacion_beneficiario", valor);
	    break;
	case 13:
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.autorizacion_fecha_autorizacion", anho);
	    whereSQL = getWhereValorActuaciones(whereSQL, "actuaciones.autorizacion_tipo", valor);
	    break;
	case 20:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.conservacion_fecha_consignacion_aprobacion", anho);
	    whereSQL = getWhereValorActuaciones(whereSQL, "actuaciones.conservacion_tipo", valor);
	    break;
	case 21:
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.conservacion_fecha_consignacion_aprobacion", anho);
	    break;
	case 22:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.conservacion_fecha_inicio", anho);
	    break;
	case 23:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.conservacion_fecha_fin", anho);
	    break;
	case 30:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.conservacion_propuesta_fecha_consignacion_aprobacion", anho);
	    whereSQL = getWhereValorActuaciones(whereSQL, "actuaciones.conservacion_propuesta_tipo", valor);
	    break;
	case 31:
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.conservacion_propuesta_fecha_consignacion_aprobacion", anho);
	    break;
	case 40:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.construccion_fecha_consignacion_aprobacion", anho);
	    whereSQL = getWhereValorActuaciones(whereSQL, "actuaciones.construccion_tipo", valor);
	    break;
	case 41:
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.construccion_fecha_consignacion_aprobacion", anho);
	    break;
	case 50:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.denuncia_fecha_denuncia", anho);
	    whereSQL = getWhereValorActuaciones(whereSQL, "actuaciones.denuncia_tipo", valor);
	    break;
	case 51:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.denuncia_fecha_resolucion", anho);
	    whereSQL = getWhereValorActuaciones(whereSQL, "actuaciones.denuncia_tipo", valor);
	    break;
	case 52:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.denuncia_fecha_denuncia", anho);
	    whereSQL = getWhereValorDoubleEqualActuaciones(whereSQL, "actuaciones.denuncia_importe_sancion", valor);
	    break;
	case 53:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.denuncia_fecha_denuncia", anho);
	    whereSQL = getWhereValorDoubleHigherActuaciones(whereSQL, "actuaciones.denuncia_importe_sancion", valor);
	    break;
	case 54:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.denuncia_fecha_denuncia", anho);
	    whereSQL = getWhereValorDoubleLessActuaciones(whereSQL, "actuaciones.denuncia_importe_sancion", valor);
	    break;
	case 60:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.incidencia_resolucion_fecha", anho);
	    break;
	case 70:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereAnhoActuaciones(whereSQL, "actuaciones.inventario_ano", anho);
	    whereSQL = getWhereValorActuaciones(whereSQL, "actuaciones.inventario_tipo", valor);
	    break;
	case 80:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereValorActuaciones(whereSQL, "actuaciones.travesia_tipo_suelo", valor);
	    break;
	case 81:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereValorActuaciones(whereSQL, "actuaciones.travesia_nucleo_poblacion", valor);
	    break;
	case 90:
	    whereSQL = getWhereCarreteraActuaciones(whereSQL, carreteraCode);
	    whereSQL = getWhereMunicipioActuaciones(whereSQL, municipioCode);
	    whereSQL = getWhereValorActuaciones(whereSQL, "actuaciones.vialidad_codigo", valor);
	    break;
	}
	return whereSQL;
    }

    private static String getWhereAforo(String whereSQL, String mayorValue,
	    String menorValue) {
	if (!mayorValue.equalsIgnoreCase("")) {
	    whereSQL = whereSQL + " AND i.valor >= '" + mayorValue + "'";
	}
	if (!menorValue.equals("")) {
	    whereSQL = whereSQL + " AND i.valor <= '" + menorValue + "'";
	}
	return whereSQL;
    }

    private static String getWhereAnhoC38(String whereSQL, String textValue) {
	if (!textValue.equals("")) {
	    whereSQL = whereSQL + " EXTRACT(YEAR FROM a.fecha) <= "
		    + textValue;
	} else {
	    whereSQL = whereSQL + " 1=1 ";
	}
	return whereSQL;
    }

    private static String getWhereAnhoC50(String whereSQL,
	    String textValue) {
	if (!textValue.equals("")) {
	    whereSQL = whereSQL + " AND EXTRACT(YEAR FROM i.fecha) = "
		    + textValue;
	}
	return whereSQL;
    }

    private static String getWhereAnhoC51C52(String whereSQL, String textValue) {
	if (!textValue.equals("")) {
	    whereSQL = whereSQL + " AND EXTRACT(YEAR FROM p.fecha) = "
		    + textValue;
	}
	return whereSQL;
    }

    private static String getWhereAnhoAforos(String whereSQL, String textValue) {
	if (!textValue.equals("")) {
	    whereSQL = whereSQL + " AND EXTRACT(YEAR FROM i.fecha) <= "
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

    private static String getWhereCaracteristicaEquals(String whereSQL,
	    String textValue) {
	if (!textValue.equals("")) {
	    whereSQL = whereSQL + " AND i.valor = '" + textValue + "'";
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

    private static String getWhereCaracteristicaCompareC10(String where,
	    String mayorValue, String menorValue) {
	if (!mayorValue.equalsIgnoreCase("")) {
	    where = where + " AND i.ancho >= '" + mayorValue + "'";
	}
	if (!menorValue.equals("")) {
	    where = where + " AND i.ancho <= '" + menorValue + "'";
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

    private static String getWhereMunicipioActuaciones(String where, String municipioCode) {
	if (!where.endsWith("AND ")) {
	    if (!municipioCode.equalsIgnoreCase(Catalog.CONCELLO_ALL)) {
		where = where + " AND act_mun.codigo_municipio = '" + municipioCode
			+ "'";
	    }else {
		where = where + " AND 1=1 ";
	    }
	} else {
	    if (!municipioCode.equalsIgnoreCase(Catalog.CONCELLO_ALL)) {
		where = where + " act_mun.codigo_municipio = '" + municipioCode
			+ "'";
	    }else {
		where = where + " 1=1 ";
	    }
	}
	return where;
    }

    private static String getWhereCarreteraActuaciones(String where, String carreteraCode) {
	if (!carreteraCode.equalsIgnoreCase(Catalog.CARRETERA_ALL)) {
	    where = where + " actuaciones.codigo_carretera = '" + carreteraCode
		    + "'";
	} else {
	    where = where + " 1=1 ";
	}
	return where;
    }

    private static String getWhereAnhoActuaciones(String where, String field, String anho) {
	if (!anho.equals("")) {
	    try {
	    Integer.parseInt(anho);
	    where = where + " AND " + field + " BETWEEN '" + anho + "-01-01' AND '" + anho + "-12-31'";
	    }catch (NumberFormatException e) {
		return where;
	    }
	}
	return where;
    }

    private static String getWhereValorActuaciones(String where, String field, String valor) {
	if (!valor.equals("")) {
	    where = where + " AND " + field + " LIKE '%" + valor + "%'";
	}
	return where;
    }

    private static String getWhereValorDoubleEqualActuaciones(String where, String field, String valor) {
	if (!valor.equals("")) {
	    try {
		Double.parseDouble(valor);
		where = where + " AND " + field + " = " + valor;
	    }catch (NumberFormatException e) {
		return where;
	    }
	}
	return where;
    }

    private static String getWhereValorDoubleLessActuaciones(String where, String field, String valor) {
	if (!valor.equals("")) {
	    try {
		Double.parseDouble(valor);
		where = where + " AND " + field + " < " + valor;
	    }catch (NumberFormatException e) {
		return where;
	    }
	}
	return where;
    }

    private static String getWhereValorDoubleHigherActuaciones(String where, String field, String valor) {
	if (!valor.equals("")) {
	    try {
		Double.parseDouble(valor);
		where = where + " AND " + field + " > " + valor;
	    }catch (NumberFormatException e) {
		return where;
	    }
	}
	return where;
    }

}
