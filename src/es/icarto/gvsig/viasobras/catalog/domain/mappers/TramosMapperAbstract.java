package es.icarto.gvsig.viasobras.catalog.domain.mappers;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import es.icarto.gvsig.viasobras.catalog.domain.Tramos;

public abstract class TramosMapperAbstract implements TramosMapper {

    public abstract CachedRowSet getTramos();

    public abstract CachedRowSet load() throws SQLException;

    public Tramos findAll() throws SQLException {
	CachedRowSet tramos = getTramos();
	if (tramos == null) {
	    tramos = load();
	}
	return new Tramos(this, Filter.findAll(tramos));
    }

    public Tramos findWhereCarretera(String carretera) throws SQLException {
	CachedRowSet tramos = getTramos();
	if (tramos == null) {
	    tramos = load();
	}
	return new Tramos(this, Filter.findWhereCarretera(tramos, carretera));
    }

    public Tramos findWhereCarreteraAndPK(String carretera, double pkStart,
	    double pkEnd) throws SQLException {
	CachedRowSet tramos = getTramos();
	if (tramos == null) {
	    tramos = load();
	}
	return new Tramos(this, Filter.findWhereCarreteraAndPK(tramos,
		carretera, pkStart, pkEnd));
    }

    public Tramos findWhereConcello(String concello) throws SQLException {
	CachedRowSet tramos = getTramos();
	if (tramos == null) {
	    tramos = load();
	}
	return new Tramos(this, Filter.findWhereConcello(tramos, concello));
    }

    public Tramos findWhereCarreteraAndConcello(String carretera,
	    String concello) throws SQLException {
	CachedRowSet tramos = getTramos();
	if (tramos == null) {
	    tramos = load();
	}
	return new Tramos(this, Filter.findWhereCarreteraAndConcello(tramos,
		carretera, concello));
    }

}
