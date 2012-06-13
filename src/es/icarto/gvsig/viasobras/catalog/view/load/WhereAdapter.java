package es.icarto.gvsig.viasobras.catalog.view.load;

import es.icarto.gvsig.viasobras.catalog.domain.Catalog;

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
	if ((concello != null) && (carretera != null)) { // both selected
	    return "WHERE municipio = '" + concello + "' AND carretera = '"
	    + carretera + "'";
	} else if ((carretera != null)) { // carretera selected
	    return "WHERE carretera = '" + carretera + "'";
	} else if ((concello != null)) { // concello selected
	    return "WHERE municipio = '" + concello + "'";
	} else { // none selected
	    return NONE_WHERE;
	}
    }

    private static String getWhereCarreteras() {
	String carretera = Catalog.getCarreteraSelected();
	String concello = Catalog.getConcelloSelected();
	if ((concello != null) && (carretera != null)) {
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
	} else if (concello != null) {
	    // only concello selected
	    //
	    // Hack to filter layer carretera depending on other layers
	    // (concello).
	    // See PostGisDriver.setData: seems not possible to filter rows
	    // depending on other layers or columns (as it takes into account
	    // all fields in table - see getTotalFields()):
	    return ", inventario.carreteras_concellos AS link WHERE link.codigo_carretera = codigo AND link.codigo_concello = '"
	    + concello + "'";
	} else if (carretera != null) {
	    // only carretera selected
	    return "WHERE codigo = '" + carretera + "'";
	} else {
	    // none selected
	    return NONE_WHERE;
	}
    }

    private static String getWhereConcellos() {
	String concello = Catalog.getConcelloSelected();
	String carretera = Catalog.getCarreteraSelected();
	if ((concello != null) && (carretera != null)) {
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
	} else if (carretera != null) {
	    // only carretera selected
	    //
	    // Hack to filter layer concello depending on other layers
	    // (carretera).
	    // See PostGisDriver.setData: seems not possible to filter rows
	    // depending on other layers or columns (as it takes into account
	    // all fields in table - see getTotalFields()):
	    return ", inventario.carreteras_concellos AS link WHERE link.codigo_concello = codigo AND link.codigo_carretera = '"
	    + carretera + "'";
	} else if (concello != null) {
	    // only concello selected
	    return "WHERE codigo = '" + concello + "'";
	} else {
	    // none selected
	    return NONE_WHERE;
	}
    }

}
