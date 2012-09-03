package es.icarto.gvsig.viasobras.domain.catalog.utils;

import java.util.Comparator;

import es.icarto.gvsig.viasobras.domain.catalog.Evento;

public class EventosComparator implements Comparator<Evento> {

    private static final int FIRST_IS_UPPER = 1;
    private static final int SECOND_IS_UPPER = -1;
    private static final int BOTH_TRAMOS_EQUALS = 0;

    public int compare(Evento e1, Evento e2) {

	int e1Carretera = Integer.parseInt(e1.getCarretera());
	int e2Carretera = Integer.parseInt(e2.getCarretera());
	if (e1Carretera > e2Carretera) {
	    return FIRST_IS_UPPER;
	} else if (e1Carretera < e2Carretera) {
	    return SECOND_IS_UPPER;
	}

	//carretera code t1 = t2
	int e1Concello = Integer.parseInt(e1.getConcello());
	int e2Concello = Integer.parseInt(e2.getConcello());
	if (e1Concello > e2Concello) {
	    return FIRST_IS_UPPER;
	} else if (e1Concello < e2Concello) {
	    return SECOND_IS_UPPER;
	}

	//concello code t1 = t2
	double e1PkStart = e1.getPk();
	double e2PkStart = e2.getPk();
	if (e1PkStart > e2PkStart) {
	    return FIRST_IS_UPPER;
	} else if (e1PkStart < e2PkStart) {
	    return SECOND_IS_UPPER;
	}

	// pk t1 = t2
	return BOTH_TRAMOS_EQUALS;
    }

}
