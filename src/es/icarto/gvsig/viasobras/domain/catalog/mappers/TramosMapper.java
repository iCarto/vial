package es.icarto.gvsig.viasobras.domain.catalog.mappers;

import java.sql.SQLException;

import es.icarto.gvsig.viasobras.domain.catalog.Tramos;

public interface TramosMapper {

    public Tramos findAll() throws SQLException;

    public Tramos findWhereCarretera(String carretera) throws SQLException;

    public Tramos findWhereConcello(String concello) throws SQLException;

    public Tramos findWhereCarreteraAndConcello(String carretera,
	    String concello) throws SQLException;

    public Tramos findWhereCarreteraAndPK(String carretera, double pkInicial,
	    double pkFinal) throws SQLException;

    public Tramos save(Tramos t) throws SQLException;

}
