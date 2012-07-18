package es.icarto.gvsig.viasobras.catalog.domain.filters;

import java.sql.SQLException;

import javax.sql.RowSet;
import javax.sql.rowset.Predicate;

public class FilterTramosByAnyField implements Predicate {

    private String fieldName;
    private String value;

    public FilterTramosByAnyField(String fieldName, String value) {
	this.fieldName = fieldName;
	this.value = value;
    }

    public boolean evaluate(RowSet rs) {
	try {
	    if ((rs.getRow() == 0) || (rs == null)) {
		return false;
	    }
	    if (this.value.equals(rs.getString(fieldName))) {
		return true;
	    } else {
		return false;
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    return false;
	}
    }

    public boolean evaluate(Object arg0, int arg1) throws SQLException {
	return true;
    }

    public boolean evaluate(Object arg0, String arg1) throws SQLException {
	return true;
    }

}
