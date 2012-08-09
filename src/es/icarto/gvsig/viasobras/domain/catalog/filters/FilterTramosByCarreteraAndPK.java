package es.icarto.gvsig.viasobras.domain.catalog.filters;

import java.sql.SQLException;

import javax.sql.RowSet;
import javax.sql.rowset.Predicate;

import es.icarto.gvsig.viasobras.domain.catalog.Catalog;

public class FilterTramosByCarreteraAndPK implements Predicate {

    private static final int BAND_PASS_FILTER = 0;
    private static final int LOW_PASS_FILTER = 1;
    private static final int HIGH_PASS_FILTER = 2;

    private String carreteraFieldName;
    private String carretera;
    private String pkStartFieldName;
    private double pkStart;
    private String pkEndFieldName;
    private double pkEnd;
    private int filterType;

    public FilterTramosByCarreteraAndPK(String carreteraFieldname, String carretera,
	    String pkStartFieldname, double pkStart, String pkEndFieldname,
	    double pkEnd) {
	this.carreteraFieldName = carreteraFieldname;
	this.carretera = carretera;
	this.pkStartFieldName = pkStartFieldname;
	this.pkStart = pkStart;
	this.pkEndFieldName = pkEndFieldname;
	this.pkEnd = pkEnd;
	this.filterType = getFilterType();
    }

    private int getFilterType() {
	if ((this.pkStart != Catalog.PK_NONE)
		&& (this.pkEnd != Catalog.PK_NONE)) {
	    return BAND_PASS_FILTER;
	} else if (this.pkStart != Catalog.PK_NONE) {
	    return HIGH_PASS_FILTER;
	}
	return LOW_PASS_FILTER;
    }

    public boolean evaluate(RowSet rs) {
	try {
	    if ((rs.getRow() == 0) || (rs == null)) {
		return false;
	    }
	    if (this.carretera.equals(rs.getString(carreteraFieldName))) {
		boolean b;
		switch (filterType) {
		case BAND_PASS_FILTER:
		    b = (this.pkStart <= rs.getDouble(this.pkStartFieldName))
			    && (this.pkEnd >= rs.getDouble(this.pkEndFieldName));
		    break;
		case LOW_PASS_FILTER:
		    b = (this.pkEnd >= rs.getDouble(this.pkEndFieldName));
		    break;
		case HIGH_PASS_FILTER:
		    b = (this.pkStart <= rs.getDouble(this.pkStartFieldName));
		    break;
		default:
		    b = false;
		}
		return b;
	    }
	    return false;
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
