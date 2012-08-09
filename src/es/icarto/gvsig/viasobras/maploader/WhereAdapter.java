package es.icarto.gvsig.viasobras.maploader;

import es.icarto.gvsig.viasobras.domain.catalog.Catalog;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.CarreterasMapper;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.ConcellosMapper;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.TramosMapperAbstract;

public class WhereAdapter {

    public static final int CARRETERAS = 0;
    public static final int CONCELLOS = 1;
    public static final int TRAMOS = 2;

    private static final String NONE_WHERE = "";

    public static String getClause(int layer) {
	switch (layer) {
	case CARRETERAS:
	    return getWhereCarreteras();
	case CONCELLOS:
	    return getWhereConcellos();
	case TRAMOS:
	    return getWhereTramos();
	default:
	    return NONE_WHERE;
	}
    }

    private static String getWhereTramos() {
	String carretera = Catalog.getCarreteraSelected();
	String concello = Catalog.getConcelloSelected();
	if ((concello != Catalog.CONCELLO_ALL)
		&& (carretera != Catalog.CARRETERA_ALL)) { // both
	    // selected
	    return "WHERE " + TramosMapperAbstract.CONCELLO_FIELDNAME + " = '"
	    + concello + "' AND "
	    + TramosMapperAbstract.CARRETERA_FIELDNAME + " = '"
	    + carretera + "'";
	} else if ((carretera != Catalog.CARRETERA_ALL)) { // carretera selected
	    return "WHERE " + TramosMapperAbstract.CARRETERA_FIELDNAME + " = '"
	    + carretera + "'";
	} else if ((concello != Catalog.CONCELLO_ALL)) { // concello selected
	    return "WHERE " + TramosMapperAbstract.CONCELLO_FIELDNAME + " = '"
	    + concello + "'";
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
	    return ", inventario.carreteras_concellos AS link WHERE link.codigo_carretera = codigo AND link.codigo_concello = '"
	    + concello
	    + "' AND link.codigo_carretera = '"
	    + carretera
	    + "'";
	} else if (concello != Catalog.CONCELLO_ALL) {
	    // only concello selected
	    //
	    // Hack to filter layer carretera depending on other layers
	    // (concello).
	    // See PostGisDriver.setData: seems not possible to filter rows
	    // depending on other layers or columns (as it takes into account
	    // all fields in table - see getTotalFields()):
	    return ", inventario.carreteras_concellos AS link WHERE link.codigo_carretera = codigo AND link.codigo_concello = '"
	    + concello + "'";
	} else if (carretera != Catalog.CARRETERA_ALL) {
	    // only carretera selected
	    return "WHERE " + CarreterasMapper.CODE + " = '" + carretera + "'";
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
	    return ", inventario.carreteras_concellos AS link WHERE link.codigo_concello = codigo AND link.codigo_carretera = '"
	    + carretera
	    + "' AND link.codigo_concello = '"
	    + concello
	    + "'";
	} else if (carretera != Catalog.CARRETERA_ALL) {
	    // only carretera selected
	    //
	    // Hack to filter layer concello depending on other layers
	    // (carretera).
	    // See PostGisDriver.setData: seems not possible to filter rows
	    // depending on other layers or columns (as it takes into account
	    // all fields in table - see getTotalFields()):
	    return ", inventario.carreteras_concellos AS link WHERE link.codigo_concello = codigo AND link.codigo_carretera = '"
	    + carretera + "'";
	} else if (concello != Catalog.CONCELLO_ALL) {
	    // only concello selected
	    return "WHERE " + ConcellosMapper.CODE + " = '" + concello + "'";
	} else {
	    // none selected
	    return NONE_WHERE;
	}
    }

}
