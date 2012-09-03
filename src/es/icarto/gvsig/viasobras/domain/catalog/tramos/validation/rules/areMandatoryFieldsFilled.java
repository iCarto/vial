package es.icarto.gvsig.viasobras.domain.catalog.tramos.validation.rules;

import es.icarto.gvsig.viasobras.domain.catalog.Tramo;
import es.icarto.gvsig.viasobras.domain.catalog.tramos.validation.TramoRule;

public class areMandatoryFieldsFilled implements TramoRule {

    public boolean validate(Tramo tramo) {
	if(tramo.getCarretera() == null ||
		tramo.getConcello() == null ||
		tramo.getOrdenTramo() == null ||
		tramo.getCarretera().equals("") ||
		tramo.getConcello().equals("") ||
		tramo.getOrdenTramo().equals("")) {
	    return false;
	}
	return true;
    }

}
