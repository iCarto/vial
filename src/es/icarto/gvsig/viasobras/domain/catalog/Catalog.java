package es.icarto.gvsig.viasobras.domain.catalog;

import java.sql.SQLException;

import es.icarto.gvsig.viasobras.domain.catalog.mappers.CarreterasMapper;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.ConcellosMapper;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.EventosMapperAccidentes;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.EventosMapperAforos;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.TramosMapperCotas;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.TramosMapperPavimento;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.TramosMapperPlataforma;

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
    private static TramosMapperCotas cotasMapper;
    private static EventosMapperAforos aforosMapper;
    private static EventosMapperAccidentes accidentesMapper;

    public static void setInvalid() {
	ConcellosMapper.setInvalid();
	CarreterasMapper.setInvalid();
	if (pavimentoMapper != null) {
	    pavimentoMapper.setInvalid();
	}
	if (plataformaMapper != null) {
	    plataformaMapper.setInvalid();
	}
	if (cotasMapper != null) {
	    cotasMapper.setInvalid();
	}
	if (aforosMapper != null) {
	    aforosMapper.setInvalid();
	}
	if (accidentesMapper != null) {
	    accidentesMapper.setInvalid();
	}
    }

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

    public static Tramos getTramosCotas() throws SQLException {
	Tramos tramos;
	cotasMapper = new TramosMapperCotas();
	if ((carretera == CARRETERA_ALL) && (concello == CONCELLO_ALL)) {
	    tramos = cotasMapper.findAll();
	} else if ((carretera == CARRETERA_ALL) && (concello != CONCELLO_ALL)) {
	    tramos = cotasMapper.findWhereConcello(concello);
	} else if ((carretera != CARRETERA_ALL) && (concello == CONCELLO_ALL)
		&& ((pkStart != PK_NONE) || (pkEnd != PK_NONE))) {
	    tramos = cotasMapper.findWhereCarreteraAndPK(carretera,
		    pkStart, pkEnd);
	} else if ((carretera != CARRETERA_ALL) && (concello == CONCELLO_ALL)) {
	    tramos = cotasMapper.findWhereCarretera(carretera);
	} else {
	    tramos = cotasMapper.findWhereCarreteraAndConcello(carretera,
		    concello);
	}
	return tramos;
    }

    public static Eventos getEventosAforos() throws SQLException {
	Eventos eventos;
	aforosMapper = new EventosMapperAforos();
	if ((carretera == CARRETERA_ALL) && (concello == CONCELLO_ALL)) {
	    eventos = aforosMapper.findAll();
	} else if ((carretera == CARRETERA_ALL) && (concello != CONCELLO_ALL)) {
	    eventos = aforosMapper.findWhereConcello(concello);
	} else if ((carretera != CARRETERA_ALL) && (concello == CONCELLO_ALL)
		&& ((pkStart != PK_NONE) || (pkEnd != PK_NONE))) {
	    eventos = aforosMapper.findWhereCarreteraAndPK(carretera, pkStart,
		    pkEnd);
	} else if ((carretera != CARRETERA_ALL) && (concello == CONCELLO_ALL)) {
	    eventos = aforosMapper.findWhereCarretera(carretera);
	} else {
	    eventos = aforosMapper.findWhereCarreteraAndConcello(carretera,
		    concello);
	}
	return eventos;
    }

    public static Eventos getEventosAccidentes() throws SQLException {
	Eventos eventos;
	accidentesMapper = new EventosMapperAccidentes();
	if ((carretera == CARRETERA_ALL) && (concello == CONCELLO_ALL)) {
	    eventos = accidentesMapper.findAll();
	} else if ((carretera == CARRETERA_ALL) && (concello != CONCELLO_ALL)) {
	    eventos = accidentesMapper.findWhereConcello(concello);
	} else if ((carretera != CARRETERA_ALL) && (concello == CONCELLO_ALL)
		&& ((pkStart != PK_NONE) || (pkEnd != PK_NONE))) {
	    eventos = accidentesMapper.findWhereCarreteraAndPK(carretera,
		    pkStart,
		    pkEnd);
	} else if ((carretera != CARRETERA_ALL) && (concello == CONCELLO_ALL)) {
	    eventos = accidentesMapper.findWhereCarretera(carretera);
	} else {
	    eventos = accidentesMapper.findWhereCarreteraAndConcello(carretera,
		    concello);
	}
	return eventos;
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
