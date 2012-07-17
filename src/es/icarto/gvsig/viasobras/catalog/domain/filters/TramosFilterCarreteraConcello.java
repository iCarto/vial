package es.icarto.gvsig.viasobras.catalog.domain.filters;

import java.sql.SQLException;

import javax.sql.RowSet;
import javax.sql.rowset.Predicate;

import es.icarto.gvsig.viasobras.catalog.domain.mappers.TramosMapperAbstract;

public class TramosFilterCarreteraConcello implements Predicate {

    private String carretera;
    private String concello;

    public TramosFilterCarreteraConcello(String carretera, String concello) {
	this.carretera = carretera;
	this.concello = concello;
    }

    public boolean evaluate(RowSet rs) {
	try {
	    if ((rs.getRow() == 0) || (rs == null)) {
		return false;
	    }
	    if ((this.carretera.equals(rs
		    .getString(TramosMapperAbstract.CARRETERA_FIELDNAME)))
		    && (this.concello
			    .equals(rs
				    .getString(TramosMapperAbstract.CONCELLO_FIELDNAME)))) {
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
