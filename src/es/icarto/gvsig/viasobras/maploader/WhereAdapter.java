package es.icarto.gvsig.viasobras.maploader;

import es.icarto.gvsig.viasobras.domain.catalog.Catalog;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.EventosMapperAbstract;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.TramosMapperAbstract;

public class WhereAdapter {

    public static final int CARRETERAS = 0;
    public static final int MUNICIPIOS = 1;
    public static final int TRAMOS = 2;
    public static final int ACCIDENTES = 3;
    public static final int AFOROS = 4;
    public static final int PKS = 5;

    private static String CODE_CARRETERA_FIELDNAME = "numero";
    private static String CODE_MUNICIPIO_FIELDNAME = "cod_mun_lu";
    private static final String NONE_WHERE = "";

    public static String getClause(int layer) {
	switch (layer) {
	case CARRETERAS:
	    return getWhereCarreteras();
	case MUNICIPIOS:
	    return getWhereConcellos();
	case TRAMOS:
	    return getWhereTramos();
	case ACCIDENTES:
	case PKS:
	    return getWhereEventos();
	case AFOROS:
	    return getWhereAforos();
	default:
	    return NONE_WHERE;
	}
    }

    private static String getWhereAforos() {
	String carretera = Catalog.getCarreteraSelected();
	String concello = Catalog.getConcelloSelected();
	Double pkStart = Catalog.getPKStart();
	Double pkEnd = Catalog.getPKEnd();
	String subQuery = "(SELECT c.codigo_carretera AS c_carretera, c.codigo_municipio AS c_municipio, c.tramo AS c_tramo, MAX(c.fecha) AS c_fecha_max "
		+ " FROM inventario.aforos c"
		+ " GROUP BY c_carretera, c_municipio, c_tramo "
		+ " ORDER BY c_carretera, c_municipio, c_tramo"
		+ ") AS b";
	if ((concello != Catalog.CONCELLO_ALL)
		&& (carretera != Catalog.CARRETERA_ALL)) {
	    // both carretera & concello selected
	    return " AS a, " + subQuery + " WHERE "
	    + " a.codigo_carretera = b.c_carretera "
	    + " AND a.codigo_municipio = b.c_municipio "
	    + " AND a.tramo = b.c_tramo "
	    + " AND a.fecha = b.c_fecha_max"
	    + " AND a." + EventosMapperAbstract.CONCELLO_FIELDNAME + " = '" + concello + "'"
	    + " AND a." + EventosMapperAbstract.CARRETERA_FIELDNAME + " = '" + carretera + "'";
	} else if (concello != Catalog.CONCELLO_ALL) {
	    // only concello selected
	    return " AS a, " + subQuery + " WHERE "
	    + " a.codigo_carretera = b.c_carretera "
	    + " AND a.codigo_municipio = b.c_municipio "
	    + " AND a.tramo = b.c_tramo "
	    + " AND a.fecha = b.c_fecha_max"
	    + " AND a." + EventosMapperAbstract.CONCELLO_FIELDNAME + " = '" + concello + "'";
	} else if (carretera != Catalog.CARRETERA_ALL) {
	    // only carretera selected
	    if ((pkStart == Catalog.PK_NONE) && (pkEnd == Catalog.PK_NONE)) {
		return " AS a, " + subQuery + " WHERE "
			+ " a.codigo_carretera = b.c_carretera "
			+ " AND a.codigo_municipio = b.c_municipio "
			+ " AND a.tramo = b.c_tramo "
			+ " AND a.fecha = b.c_fecha_max"
			+ " AND a." + EventosMapperAbstract.CARRETERA_FIELDNAME
			+ " = '" + carretera + "'";
	    } else if ((pkStart != Catalog.PK_NONE)
		    && (pkEnd == Catalog.PK_NONE)) {
		// carretera & pkStart
		return " AS a, " + subQuery + " WHERE "
		+ " a.codigo_carretera = b.c_carretera "
		+ " AND a.codigo_municipio = b.c_municipio "
		+ " AND a.tramo = b.c_tramo "
		+ " AND a.fecha = b.c_fecha_max" 
		+ " AND a." + EventosMapperAbstract.CARRETERA_FIELDNAME + " = '" + carretera + "' "
		+ " AND a." + EventosMapperAbstract.PK_FIELDNAME + " >= '"+ Double.toString(pkStart) + "'";
	    } else if ((pkStart == Catalog.PK_NONE)
		    && (pkEnd != Catalog.PK_NONE)) {
		// carretera & pkEnd
		return " AS a, " + subQuery + " WHERE "
		+ " a.codigo_carretera = b.c_carretera "
		+ " AND a.codigo_municipio = b.c_municipio "
		+ " AND a.tramo = b.c_tramo "
		+ " AND a.fecha = b.c_fecha_max" 
		+ " AND a." + EventosMapperAbstract.CARRETERA_FIELDNAME + " = '" + carretera + "' "
		+ " AND a." + EventosMapperAbstract.PK_FIELDNAME + " <= '" + Double.toString(pkEnd) + "'";
	    } else {
		// carretera & pkStart & pkEnd
		return " AS a, " + subQuery + " WHERE "
		+ " a.codigo_carretera = b.c_carretera "
		+ " AND a.codigo_municipio = b.c_municipio "
		+ " AND a.tramo = b.c_tramo "
		+ " AND a.fecha = b.c_fecha_max" 
		+ " AND a." + EventosMapperAbstract.CARRETERA_FIELDNAME + " = '" + carretera + "' "
		+ " AND a." + EventosMapperAbstract.PK_FIELDNAME + " >= '" + Double.toString(pkStart) + "' "
		+ " AND a." + EventosMapperAbstract.PK_FIELDNAME + " <= '" + Double.toString(pkEnd) + "'";
	    }
	} else { // none selected
	    return " AS a, " + subQuery + " WHERE "
	    + " a.codigo_carretera = b.c_carretera "
	    + " AND a.codigo_municipio = b.c_municipio "
	    + " AND a.tramo = b.c_tramo "
	    + " AND a.fecha = b.c_fecha_max";
	}
    }

    private static String getWhereEventos() {
	String carretera = Catalog.getCarreteraSelected();
	String concello = Catalog.getConcelloSelected();
	Double pkStart = Catalog.getPKStart();
	Double pkEnd = Catalog.getPKEnd();
	if ((concello != Catalog.CONCELLO_ALL)
		&& (carretera != Catalog.CARRETERA_ALL)) {
	    // both carretera & concello selected
	    return "WHERE " + EventosMapperAbstract.CONCELLO_FIELDNAME + " = '"
	    + concello + "' AND "
	    + EventosMapperAbstract.CARRETERA_FIELDNAME + " = '"
	    + carretera + "'";
	} else if (concello != Catalog.CONCELLO_ALL) {
	    // only concello selected
	    return "WHERE " + EventosMapperAbstract.CONCELLO_FIELDNAME + " = '"
	    + concello + "'";
	} else if (carretera != Catalog.CARRETERA_ALL) {
	    // only carretera selected
	    if ((pkStart == Catalog.PK_NONE) && (pkEnd == Catalog.PK_NONE)) {
		return "WHERE " + EventosMapperAbstract.CARRETERA_FIELDNAME
			+ " = '"
			+ carretera + "'";
	    } else if ((pkStart != Catalog.PK_NONE)
		    && (pkEnd == Catalog.PK_NONE)) {
		// carretera & pkStart
		return "WHERE " + EventosMapperAbstract.CARRETERA_FIELDNAME
			+ " = '" + carretera + "' AND "
			+ EventosMapperAbstract.PK_FIELDNAME + " >= '"
			+ Double.toString(pkStart) + "'";
	    } else if ((pkStart == Catalog.PK_NONE)
		    && (pkEnd != Catalog.PK_NONE)) {
		// carretera & pkEnd
		return "WHERE " + EventosMapperAbstract.CARRETERA_FIELDNAME
			+ " = '" + carretera + "' AND "
			+ EventosMapperAbstract.PK_FIELDNAME + " <= '"
			+ Double.toString(pkEnd) + "'";
	    } else {
		// carretera & pkStart & pkEnd
		return "WHERE " + EventosMapperAbstract.CARRETERA_FIELDNAME
			+ " = '" + carretera + "' AND "
			+ EventosMapperAbstract.PK_FIELDNAME + " >= '"
			+ Double.toString(pkStart) + "' AND "
			+ EventosMapperAbstract.PK_FIELDNAME + " <= '"
			+ Double.toString(pkEnd) + "'";
	    }
	} else{ // none selected
	    return NONE_WHERE;
	}
    }

    private static String getWhereTramos() {
	String carretera = Catalog.getCarreteraSelected();
	String concello = Catalog.getConcelloSelected();
	Double pkStart = Catalog.getPKStart();
	Double pkEnd = Catalog.getPKEnd();
	if ((concello != Catalog.CONCELLO_ALL)
		&& (carretera != Catalog.CARRETERA_ALL)) {
	    // both concello & carretera selected
	    return "WHERE " + TramosMapperAbstract.CONCELLO_FIELDNAME + " = '"
	    + concello + "' AND "
	    + TramosMapperAbstract.CARRETERA_FIELDNAME + " = '"
	    + carretera + "'";
	} else if (concello != Catalog.CONCELLO_ALL) {
	    // concello selected
	    return "WHERE " + TramosMapperAbstract.CONCELLO_FIELDNAME + " = '"
	    + concello + "'";
	} else if ((carretera != Catalog.CARRETERA_ALL)) {
	    // only carretera selected
	    if ((pkStart == Catalog.PK_NONE) && (pkEnd == Catalog.PK_NONE)) {
		return "WHERE " + TramosMapperAbstract.CARRETERA_FIELDNAME
			+ " = '" + carretera + "'";
	    } else if ((pkStart != Catalog.PK_NONE)
		    && (pkEnd == Catalog.PK_NONE)) {
		// carretera & pkStart
		return "WHERE " + TramosMapperAbstract.CARRETERA_FIELDNAME
			+ " = '" + carretera + "' AND "
			+ TramosMapperAbstract.PK_START_FIELDNAME + " >= '"
			+ Double.toString(pkStart) + "'";
	    } else if ((pkStart == Catalog.PK_NONE)
		    && (pkEnd != Catalog.PK_NONE)) {
		// carretera & pkEnd
		return "WHERE " + TramosMapperAbstract.CARRETERA_FIELDNAME
			+ " = '" + carretera + "' AND "
			+ TramosMapperAbstract.PK_END_FIELDNAME + " <= '"
			+ Double.toString(pkEnd) + "'";
	    } else {
		// carretera & pkStart & pkEnd
		return "WHERE " + EventosMapperAbstract.CARRETERA_FIELDNAME
			+ " = '" + carretera + "' AND "
			+ TramosMapperAbstract.PK_START_FIELDNAME + " >= '"
			+ Double.toString(pkStart) + "' AND "
			+ TramosMapperAbstract.PK_END_FIELDNAME + " <= '"
			+ Double.toString(pkEnd) + "'";
	    }
	} else { // none selected
	    return NONE_WHERE;
	}
    }

    private static String getWhereCarreteras() {
	String carretera = Catalog.getCarreteraSelected();
	String concello = Catalog.getConcelloSelected();
	if ((concello != Catalog.CONCELLO_ALL)
		&& (carretera != Catalog.CARRETERA_ALL)) {
	    // both selected
	    //
	    // Hack to filter layer carretera depending on other layers,
	    // see PostGisDriver.setData, seems not possible to filter rows
	    // depending on other layers or columns (as it takes into account
	    // all fields in table - see getTotalFields()):
	    return ", inventario.carretera_municipio AS link WHERE link.codigo_carretera = " + CODE_CARRETERA_FIELDNAME
		    + " AND link.codigo_municipio = '" + concello + "' "
		    + " AND link.codigo_carretera = '" + carretera + "'";
	} else if (concello != Catalog.CONCELLO_ALL) {
	    // only concello selected
	    //
	    // Hack to filter layer carretera depending on other layers
	    // (concello).
	    // See PostGisDriver.setData: seems not possible to filter rows
	    // depending on other layers or columns (as it takes into account
	    // all fields in table - see getTotalFields()):
	    return ", inventario.carretera_municipio AS link WHERE link.codigo_carretera = " + CODE_CARRETERA_FIELDNAME
		    + " AND link.codigo_municipio = '" + concello + "'";
	} else if (carretera != Catalog.CARRETERA_ALL) {
	    // only carretera selected
	    return "WHERE numero = '" + carretera + "'";
	} else {
	    // none selected
	    return NONE_WHERE;
	}
    }

    private static String getWhereConcellos() {
	String concello = Catalog.getConcelloSelected();
	String carretera = Catalog.getCarreteraSelected();
	if ((concello != Catalog.CONCELLO_ALL)
		&& (carretera != Catalog.CARRETERA_ALL)) {
	    // both carretera & concello selected
	    //
	    // Hack to filter layer concello depending on other layers
	    // (carretera).
	    // See PostGisDriver.setData: seems not possible to filter rows
	    // depending on other layers or columns (as it takes into account
	    // all fields in table - see getTotalFields()):
	    return ", inventario.carretera_municipio AS link WHERE link.codigo_municipio = "
	    + CODE_MUNICIPIO_FIELDNAME
	    + " AND link.codigo_carretera = '"
	    + carretera
	    + "' "
	    + " AND link.codigo_municipio = '" + concello + "'";
	} else if (carretera != Catalog.CARRETERA_ALL) {
	    // only carretera selected
	    //
	    // Hack to filter layer concello depending on other layers
	    // (carretera).
	    // See PostGisDriver.setData: seems not possible to filter rows
	    // depending on other layers or columns (as it takes into account
	    // all fields in table - see getTotalFields()):
	    return ", inventario.carretera_municipio AS link WHERE link.codigo_municipio = "
	    + CODE_MUNICIPIO_FIELDNAME
	    + " AND link.codigo_carretera = '" + carretera + "'";
	} else if (concello != Catalog.CONCELLO_ALL) {
	    // only concello selected
	    return "WHERE " + CODE_MUNICIPIO_FIELDNAME + " = '" + concello
		    + "'";
	} else {
	    // none selected
	    return NONE_WHERE;
	}
    }

}
