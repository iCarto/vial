package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.SQLException;

import es.icarto.gvsig.viasobras.catalog.domain.mappers.CarreterasMapper;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.ConcellosMapper;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.TramosPavimentoMapper;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.TramosPlataformaMapper;


public class Catalog {

    private static String carreteraSelected;
    private static String concelloSelected;

    public static Carreteras getCarreteras() throws SQLException {
	return CarreterasMapper.findAll();
    }

    public static Concellos getConcellos() throws SQLException {
	Concellos cs;
	if (carreteraSelected == null) {
	    cs = ConcellosMapper.findAll();
	} else {
	    cs = ConcellosMapper.findWhereCarretera(carreteraSelected);
	}
	return cs;
    }

    public static TramosPavimento getTramosTipoPavimento() throws SQLException {
	TramosPavimento pv;
	if ((carreteraSelected == null) && (concelloSelected == null)) {
	    pv = TramosPavimentoMapper.findAll();
	} else if ((carreteraSelected == null) && (concelloSelected != null)) {
	    pv = TramosPavimentoMapper.findWhereConcello(concelloSelected);
	} else if ((carreteraSelected != null) && (concelloSelected == null)) {
	    pv = TramosPavimentoMapper.findWhereCarretera(carreteraSelected);
	} else {
	    pv = TramosPavimentoMapper.findWhereCarreteraAndConcello(
		    carreteraSelected,
		    concelloSelected);
	}
	return pv;
    }

    public static TramosPlataforma getTramosAnchoPlataforma()
	    throws SQLException {
	TramosPlataforma tc;
	if ((carreteraSelected == null) && (concelloSelected == null)) {
	    tc = TramosPlataformaMapper.findAll();
	} else if ((carreteraSelected == null) && (concelloSelected != null)) {
	    tc = TramosPlataformaMapper.findWhereConcello(concelloSelected);
	} else if ((carreteraSelected != null) && (concelloSelected == null)) {
	    tc = TramosPlataformaMapper.findWhereCarretera(carreteraSelected);
	} else {
	    tc = TramosPlataformaMapper.findWhereCarreteraAndConcello(
		    carreteraSelected, concelloSelected);
	}
	return tc;
    }

    public static String getCarreteraSelected() {
	return carreteraSelected;
    }

    public static void setCarretera(String carretera) {
	carreteraSelected = carretera;
    }

    public static String getConcelloSelected() {
	return concelloSelected;
    }

    public static void setConcello(String concello) {
	concelloSelected = concello;
    }

    public static void clear() {
	carreteraSelected = null;
	concelloSelected = null;
    }

}
