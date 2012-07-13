package es.icarto.gvsig.viasobras.catalog.domain.mappers;

import java.sql.SQLException;

import es.icarto.gvsig.viasobras.catalog.domain.Tramos;

public interface TramosMapper {

    public Tramos save(Tramos t) throws SQLException;

}
