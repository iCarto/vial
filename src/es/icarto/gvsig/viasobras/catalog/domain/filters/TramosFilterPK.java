package es.icarto.gvsig.viasobras.catalog.domain.filters;

import java.sql.SQLException;

import javax.sql.RowSet;
import javax.sql.rowset.Predicate;

public class TramosFilterPK implements Predicate {

    private String carreteraFieldName;
    private String carretera;
    private String pkStartFieldName;
    private double pkStart;
    private String pkEndFieldName;
    private double pkEnd;

    public TramosFilterPK(String carreteraFieldname, String carretera,
	    String pkStartFieldname, double pkStart, String pkEndFieldname,
	    double pkEnd) {
	this.carreteraFieldName = carreteraFieldname;
	this.carretera = carretera;
	this.pkStartFieldName = pkStartFieldname;
	this.pkStart = pkStart;
	this.pkEndFieldName = pkEndFieldname;
	this.pkEnd = pkEnd;
    }

    public boolean evaluate(RowSet rs) {
	try {
	    if ((rs.getRow() == 0) || (rs == null)) {
		return false;
	    }
	    if ((this.carretera.equals(rs.getString(carreteraFieldName)))
		    && (this.pkStart <= rs.getDouble(this.pkStartFieldName))
		    && (this.pkEnd >= rs.getDouble(this.pkEndFieldName))) {
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
