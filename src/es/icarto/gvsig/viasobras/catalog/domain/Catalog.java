package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.SQLException;

import es.icarto.gvsig.viasobras.catalog.domain.mappers.CarreterasMapper;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.ConcellosMapper;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.TramosMapperPavimento;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.TramosMapperPlataforma;

public class Catalog {

    public static final double PK_NONE = -1;
    public static final String CONCELLO_ALL = "Todos";
    public static final String CARRETERA_ALL = "Todas";

    private static String carretera = CARRETERA_ALL;
    private static String concello = CONCELLO_ALL;
    private static double pkStart = PK_NONE;
    private static double pkEnd = PK_NONE;

    private static TramosMapperPavimento pavimentoMapper;
    private static TramosMapperPlataforma plataformaMapper;

    public static Carreteras getCarreteras() throws SQLException {
	return CarreterasMapper.findAll();
    }

    public static Concellos getConcellos() throws SQLException {
	Concellos cs;
	if (carretera == CARRETERA_ALL) {
	    cs = ConcellosMapper.findAll();
	} else {
	    cs = ConcellosMapper.findWhereCarretera(carretera);
	}
	return cs;
    }

    public static Tramos getTramosTipoPavimento() throws SQLException {
	Tramos tramos;
	pavimentoMapper = new TramosMapperPavimento();
	if ((carretera == CARRETERA_ALL) && (concello == CONCELLO_ALL)) {
	    tramos = pavimentoMapper.findAll();
	} else if ((carretera == CARRETERA_ALL) && (concello != CONCELLO_ALL)) {
	    tramos = pavimentoMapper.findWhereConcello(concello);
	} else if ((carretera != CARRETERA_ALL) && (concello == CONCELLO_ALL)
		&& ((pkStart != PK_NONE) || (pkEnd != PK_NONE))) {
	    tramos = pavimentoMapper.findWhereCarreteraAndPK(carretera, pkStart,
		    pkEnd);
	} else if ((carretera != CARRETERA_ALL) && (concello == CONCELLO_ALL)) {
	    tramos = pavimentoMapper.findWhereCarretera(carretera);
	} else {
	    tramos = pavimentoMapper.findWhereCarreteraAndConcello(carretera,
		    concello);
	}
	return tramos;
    }

    public static Tramos getTramosAnchoPlataforma() throws SQLException {
	Tramos tramos;
	plataformaMapper = new TramosMapperPlataforma();
	if ((carretera == CARRETERA_ALL) && (concello == CONCELLO_ALL)) {
	    tramos = plataformaMapper.findAll();
	} else if ((carretera == CARRETERA_ALL) && (concello != CONCELLO_ALL)) {
	    tramos = plataformaMapper.findWhereConcello(concello);
	} else if ((carretera != CARRETERA_ALL) && (concello == CONCELLO_ALL)
		&& ((pkStart != PK_NONE) || (pkEnd != PK_NONE))) {
	    tramos = plataformaMapper.findWhereCarreteraAndPK(carretera,
		    pkStart, pkEnd);
	} else if ((carretera != CARRETERA_ALL) && (concello == CONCELLO_ALL)) {
	    tramos = plataformaMapper.findWhereCarretera(carretera);
	} else {
	    tramos = plataformaMapper.findWhereCarreteraAndConcello(
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
	carretera = CARRETERA_ALL;
	concello = CONCELLO_ALL;
	pkStart = PK_NONE;
	pkEnd = PK_NONE;
    }

}
