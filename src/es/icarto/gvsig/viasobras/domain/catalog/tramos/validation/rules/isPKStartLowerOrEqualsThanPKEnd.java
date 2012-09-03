package es.icarto.gvsig.viasobras.domain.catalog.tramos.validation.rules;

import es.icarto.gvsig.viasobras.domain.catalog.Tramo;
import es.icarto.gvsig.viasobras.domain.catalog.tramos.validation.TramoRule;

public class isPKStartLowerOrEqualsThanPKEnd implements TramoRule {

    public boolean validate(Tramo tramo) {
	if (tramo.getPkStart() > tramo.getPkEnd()) {
	    return false;
	}
	return true;
    }

}
