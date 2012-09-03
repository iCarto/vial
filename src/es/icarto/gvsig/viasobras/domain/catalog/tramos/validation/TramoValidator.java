package es.icarto.gvsig.viasobras.domain.catalog.tramos.validation;

import java.util.ArrayList;
import java.util.List;

import es.icarto.gvsig.viasobras.domain.catalog.Tramo;
import es.icarto.gvsig.viasobras.domain.catalog.tramos.validation.rules.areMandatoryFieldsFilled;
import es.icarto.gvsig.viasobras.domain.catalog.tramos.validation.rules.isPKStartLowerOrEqualsThanPKEnd;


public class TramoValidator {

    private List<TramoRule> rules;

    public TramoValidator() {
	this.rules = new ArrayList<TramoRule>();
	this.rules.add(new isPKStartLowerOrEqualsThanPKEnd());
	this.rules.add(new areMandatoryFieldsFilled());
    }

    public boolean validate(Tramo tramo) {
	for (TramoRule rule : rules) {
	    if (!rule.validate(tramo)) {
		return false;
	    }
	}
	return true;
    }

}
