package es.icarto.gvsig.viasobras.domain.catalog.validation;

import java.util.ArrayList;
import java.util.List;

import es.icarto.gvsig.viasobras.domain.catalog.Tramo;
import es.icarto.gvsig.viasobras.domain.catalog.validation.rules.MandatoryFieldsFilled;
import es.icarto.gvsig.viasobras.domain.catalog.validation.rules.PKStartLowerOrEqualsThanPKEnd;


public class TramoValidator {

    private List<TramoRule> rules;

    public TramoValidator() {
	this.rules = new ArrayList<TramoRule>();
	this.rules.add(new PKStartLowerOrEqualsThanPKEnd());
	this.rules.add(new MandatoryFieldsFilled());
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
