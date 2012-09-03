package es.icarto.gvsig.viasobras.domain.catalog.validation;

import java.util.ArrayList;
import java.util.List;

import es.icarto.gvsig.viasobras.domain.catalog.Evento;
import es.icarto.gvsig.viasobras.domain.catalog.validation.rules.MandatoryFieldsFilled;


public class EventoValidator {

    private List<EventoRule> rules;

    public EventoValidator() {
	this.rules = new ArrayList<EventoRule>();
	this.rules.add(new MandatoryFieldsFilled());
    }

    public boolean validate(Evento evento) {
	for (EventoRule rule : rules) {
	    if (!rule.validate(evento)) {
		return false;
	    }
	}
	return true;
    }

}
