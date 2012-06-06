package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.SQLException;

import javax.sql.RowSet;
import javax.sql.rowset.Predicate;

public class CarreteraFilter implements Predicate {

    public CarreteraFilter(String carretera) {

    }

    public boolean evaluate(RowSet rs) {
	return true;
    }

    public boolean evaluate(Object value, int colIndex) throws SQLException {
	return true;
    }

    public boolean evaluate(Object value, String colName) throws SQLException {
	return true;
    }

}
