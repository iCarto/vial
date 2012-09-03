package es.icarto.gvsig.viasobras.domain.catalog.validation.rules;

import es.icarto.gvsig.viasobras.domain.catalog.Evento;
import es.icarto.gvsig.viasobras.domain.catalog.Tramo;
import es.icarto.gvsig.viasobras.domain.catalog.validation.EventoRule;
import es.icarto.gvsig.viasobras.domain.catalog.validation.TramoRule;

public class MandatoryFieldsFilled implements TramoRule, EventoRule {

    public boolean validate(Tramo tramo) {
	if (tramo.getCarretera() == null
		|| tramo.getConcello() == null
		|| tramo.getOrdenTramo() == null
		|| tramo.getCarretera().equals("")
		|| tramo.getConcello().equals("")
		|| tramo.getOrdenTramo().equals("")) {
	    return false;
	}
	return true;
    }

    public boolean validate(Evento evento) {
	if (evento.getCarretera() == null
		|| evento.getConcello() == null
		|| evento.getOrden() == null
		|| evento.getCarretera().equals("")
		|| evento.getConcello().equals("")
		|| evento.getOrden().equals("")) {
	    return false;
	}
	return true;
    }

}
