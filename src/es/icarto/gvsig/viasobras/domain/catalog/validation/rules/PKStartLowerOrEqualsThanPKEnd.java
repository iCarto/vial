package es.icarto.gvsig.viasobras.domain.catalog.validation.rules;

import es.icarto.gvsig.viasobras.domain.catalog.Tramo;
import es.icarto.gvsig.viasobras.domain.catalog.validation.TramoRule;

public class PKStartLowerOrEqualsThanPKEnd implements TramoRule {

    public boolean validate(Tramo tramo) {
	if (tramo.getPkStart() > tramo.getPkEnd()) {
	    return false;
	}
	return true;
    }

}
