package es.icarto.gvsig.viasobras.domain.catalog.mappers;

import java.sql.SQLException;

import es.icarto.gvsig.viasobras.domain.catalog.Eventos;

public interface EventosMapper {

    public Eventos findAll() throws SQLException;

    public Eventos findWhereCarretera(String carretera) throws SQLException;

    public Eventos findWhereConcello(String concello) throws SQLException;

    public Eventos findWhereCarreteraAndConcello(String carretera,
	    String concello) throws SQLException;

    public Eventos findWhereCarreteraAndPK(String carretera, double pkInicial,
	    double pkFinal) throws SQLException;

    public Eventos save(Eventos e) throws SQLException;

}
