package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.SQLException;

import es.icarto.gvsig.viasobras.catalog.domain.mappers.CarreterasMapper;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.ConcellosMapper;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.TramosPavimentoMapper;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.TramosPlataformaMapper;


public class Catalog {

    private static String carretera;
    private static String concello;

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
	Tramos tp;
	if ((carretera == null) && (concello == null)) {
	    tp = TramosPavimentoMapper.findAll();
	} else if ((carretera == null) && (concello != null)) {
	    tp = TramosPavimentoMapper.findWhereConcello(concello);
	} else if ((carretera != null) && (concello == null)) {
	    tp = TramosPavimentoMapper.findWhereCarretera(carretera);
	} else {
	    tp = TramosPavimentoMapper.findWhereCarreteraAndConcello(
		    carretera, concello);
	}
	return tp;
    }

    public static Tramos getTramosAnchoPlataforma()
	    throws SQLException {
	Tramos ap;
	if ((carretera == null) && (concello == null)) {
	    ap = TramosPlataformaMapper.findAll();
	} else if ((carretera == null) && (concello != null)) {
	    ap = TramosPlataformaMapper.findWhereConcello(concello);
	} else if ((carretera != null) && (concello == null)) {
	    ap = TramosPlataformaMapper.findWhereCarretera(carretera);
	} else {
	    ap = TramosPlataformaMapper.findWhereCarreteraAndConcello(
		    carretera, concello);
	}
	return ap;
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

    public static void clear() {
	carretera = null;
	concello = null;
    }

}
