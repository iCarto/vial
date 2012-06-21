package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.SQLException;

import es.icarto.gvsig.viasobras.catalog.domain.mappers.CarreterasMapper;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.ConcellosMapper;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.TramosPavimentoMapper;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.TramosPlataformaMapper;

public class Catalog {

    private static final double NO_PK = -1;
    private static String carretera;
    private static String concello;
    private static double pkStart = NO_PK;
    private static double pkEnd = NO_PK;

    public static Carreteras getCarreteras() throws SQLException {
	return CarreterasMapper.findAll();
    }

    public static Concellos getConcellos() throws SQLException {
	Concellos cs;
	if (carretera == null) {
	    cs = ConcellosMapper.findAll();
	} else {
	    cs = ConcellosMapper.findWhereCarretera(carretera);
	}
	return cs;
    }

    public static Tramos getTramosTipoPavimento() throws SQLException {
	Tramos tramos;
	if ((carretera == null) && (concello == null)) {
	    tramos = TramosPavimentoMapper.findAll();
	} else if ((carretera == null) && (concello != null)) {
	    tramos = TramosPavimentoMapper.findWhereConcello(concello);
	} else if ((carretera != null) && (concello == null)
		&& ((pkStart != NO_PK) || (pkEnd != NO_PK))) {
	    tramos = TramosPavimentoMapper.findWhereCarretera(carretera,
		    pkStart, pkEnd);
	} else if ((carretera != null) && (concello == null)) {
	    tramos = TramosPavimentoMapper.findWhereCarretera(carretera);
	} else {
	    tramos = TramosPavimentoMapper.findWhereCarreteraAndConcello(
		    carretera, concello);
	}
	return tramos;
    }

    public static Tramos getTramosAnchoPlataforma() throws SQLException {
	Tramos tramos;
	if ((carretera == null) && (concello == null)) {
	    tramos = TramosPlataformaMapper.findAll();
	} else if ((carretera == null) && (concello != null)) {
	    tramos = TramosPlataformaMapper.findWhereConcello(concello);
	} else if ((carretera != null) && (concello == null)
		&& ((pkStart != NO_PK) || (pkEnd != NO_PK))) {
	    tramos = TramosPlataformaMapper.findWhereCarretera(carretera,
		    pkStart, pkEnd);
	} else if ((carretera != null) && (concello == null)) {
	    tramos = TramosPlataformaMapper.findWhereCarretera(carretera);
	} else {
	    tramos = TramosPlataformaMapper.findWhereCarreteraAndConcello(
		    carretera, concello);
	}
	return tramos;
    }

    public static String getCarreteraSelected() {
	return carretera;
    }

    public static void setCarretera(String c) {
	carretera = c;
    }

    public static String getConcelloSelected() {
	return concello;
    }

    public static void setConcello(String c) {
	concello = c;
    }

    public static void setPKStart(double d) {
	pkStart = d;
    }

    public static double getPKStart() {
	return pkStart;
    }

    public static void setPKEnd(double d) {
	pkEnd = d;
    }

    public static double getPKEnd() {
	return pkEnd;
    }

    public static void clear() {
	carretera = null;
	concello = null;
    }

}
