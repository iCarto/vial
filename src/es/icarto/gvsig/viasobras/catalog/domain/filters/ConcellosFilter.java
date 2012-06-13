package es.icarto.gvsig.viasobras.catalog.domain.filters;

import java.sql.SQLException;

import javax.sql.RowSet;
import javax.sql.rowset.Predicate;

import es.icarto.gvsig.viasobras.catalog.domain.Carreteras;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.CarreterasMapper;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.ConcellosMapper;

public class ConcellosFilter implements Predicate {

    private String carretera;
    private Carreteras carreteras;

    public ConcellosFilter(String carretera) throws SQLException {
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
