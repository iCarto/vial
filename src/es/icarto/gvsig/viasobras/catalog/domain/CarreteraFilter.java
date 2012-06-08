package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.SQLException;

import javax.sql.RowSet;
import javax.sql.rowset.Predicate;

import com.iver.cit.gvsig.fmap.drivers.DBException;

import es.icarto.gvsig.viasobras.catalog.domain.mappers.CarreterasMapper;
import es.icarto.gvsig.viasobras.catalog.domain.mappers.ConcellosMapper;

public class CarreteraFilter implements Predicate {

    private String carretera;
    private Carreteras carreteras;

    public CarreteraFilter(String carretera) throws SQLException, DBException {
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
