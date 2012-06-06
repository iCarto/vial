package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.SQLException;

import javax.sql.RowSet;
import javax.sql.rowset.Predicate;

public class CarreteraFilter implements Predicate {

    private String carretera;
    public CarreteraFilter(String carretera) {
	this.carretera = carretera;
    }

    public boolean evaluate(RowSet rs) {
	try {
	    if ((rs.getRow() == 0) || (rs == null)) {
		return false;
	    }
	    String concelloCode = rs.getString(Concello.getDBNameCode());
	    if (Carreteras.getCarretera(carretera).isIn(concelloCode)) {
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
