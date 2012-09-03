package es.icarto.gvsig.viasobras.domain.catalog.validation;

import es.icarto.gvsig.viasobras.domain.catalog.Evento;

public interface EventoRule {

    public boolean validate(Evento evento);

}
