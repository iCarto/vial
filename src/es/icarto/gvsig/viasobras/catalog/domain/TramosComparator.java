package es.icarto.gvsig.viasobras.catalog.domain;

import java.util.Comparator;

public class TramosComparator implements Comparator<Tramo> {

    private static final int FIRST_IS_UPPER = 1;
    private static final int SECOND_IS_UPPER = -1;
    private static final int BOTH_TRAMOS_EQUALS = 0;

    public int compare(Tramo t1, Tramo t2) {

	int t1Carretera = Integer.parseInt(t1.getCarretera());
	int t2Carretera = Integer.parseInt(t2.getCarretera());
	if (t1Carretera > t2Carretera) {
	    return FIRST_IS_UPPER;
	} else if (t1Carretera < t2Carretera) {
	    return SECOND_IS_UPPER;
	}

	//carretera code t1 = t2
	int t1Concello = Integer.parseInt(t1.getConcello());
	int t2Concello = Integer.parseInt(t2.getConcello());
	if (t1Concello > t2Concello) {
	    return FIRST_IS_UPPER;
	} else if (t1Concello < t2Concello) {
	    return SECOND_IS_UPPER;
	}

	//concello code t1 = t2
	double t1PkStart = t1.getPkStart();
	double t2PkStart = t2.getPkStart();
	if (t1PkStart > t2PkStart) {
	    return FIRST_IS_UPPER;
	} else if (t1PkStart < t2PkStart) {
	    return SECOND_IS_UPPER;
	}

	// pk start t1 = t2
	double t1PkEnd = t1.getPkEnd();
	double t2PkEnd = t2.getPkEnd();
	if (t1PkEnd > t2PkEnd) {
	    return FIRST_IS_UPPER;
	} else if (t1PkEnd < t2PkEnd) {
	    return SECOND_IS_UPPER;
	}

	// pk end t1 = t2
	return BOTH_TRAMOS_EQUALS;
    }

}
