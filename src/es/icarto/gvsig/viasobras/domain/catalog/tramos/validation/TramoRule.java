package es.icarto.gvsig.viasobras.domain.catalog.tramos.validation;

import es.icarto.gvsig.viasobras.domain.catalog.Tramo;

public interface TramoRule {

    public boolean validate(Tramo tramo);

}
