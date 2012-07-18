package es.icarto.gvsig.viasobras.catalog.domain.filters;

import java.sql.SQLException;

import javax.sql.RowSet;
import javax.sql.rowset.Predicate;

public class FilterTramosByCarreteraAndConcello implements Predicate {

    private String carretera;
    private String concello;
    private String carreteraFieldName;
    private String concelloFieldName;

    public FilterTramosByCarreteraAndConcello(String carreteraFieldName,
	    String carretera, String concelloFieldName, String concello) {
	this.carretera = carretera;
	this.concello = concello;
	this.carreteraFieldName = carreteraFieldName;
	this.concelloFieldName = concelloFieldName;
    }

    public boolean evaluate(RowSet rs) {
	try {
	    if ((rs.getRow() == 0) || (rs == null)) {
		return false;
	    }
	    if ((this.carretera.equals(rs.getString(carreteraFieldName)))
		    && (this.concello.equals(rs.getString(concelloFieldName)))) {
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
