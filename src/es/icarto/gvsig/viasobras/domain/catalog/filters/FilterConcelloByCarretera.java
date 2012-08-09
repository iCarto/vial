package es.icarto.gvsig.viasobras.domain.catalog.filters;

import java.sql.SQLException;

import javax.sql.RowSet;
import javax.sql.rowset.Predicate;

import es.icarto.gvsig.viasobras.domain.catalog.Carreteras;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.CarreterasMapper;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.ConcellosMapper;

public class FilterConcelloByCarretera implements Predicate {

    private String carretera;
    private Carreteras carreteras;

    public FilterConcelloByCarretera(String carretera)
	    throws SQLException {
	this.carretera = carretera;
	carreteras = CarreterasMapper.findAll();
    }

    public boolean evaluate(RowSet rs) {
	try {
	    if ((rs.getRow() == 0) || (rs == null)) {
		return false;
	    }
	    String concelloCode = rs.getString(ConcellosMapper.CODE);
	    if (carreteras.getCarretera(carretera).isIn(concelloCode)) {
		return true;
	    }
	    return false;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return false;
	}
    }

    public boolean evaluate(Object value, int colIndex) throws SQLException {
	return true;
    }

    public boolean evaluate(Object value, String colName) throws SQLException {
	return true;
    }

}
