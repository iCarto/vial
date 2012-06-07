package es.icarto.gvsig.viasobras.catalog.domain.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import es.icarto.gvsig.viasobras.catalog.domain.TramosPavimento;

public class TramosPavimentoMapper extends DomainMapper {

    public static TramosPavimento findAll() {
	Connection c = DomainMapper.getConnection();
	Statement stmt;
	try {
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT gid, tipopavime, origenpavi, finalpavim FROM inventario.tipo_pavimento");
	    return new TramosPavimento(rs);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static TramosPavimento findWhereCarretera(String carretera) {
	Connection c = DomainMapper.getConnection();
	PreparedStatement stmt;
	try {
	    String sql = "SELECT gid, tipopavime, origenpavi, finalpavim FROM inventario.tipo_pavimento WHERE numeroinve = ?";
	    stmt = c.prepareStatement(sql);
	    stmt.setString(1, carretera);
	    ResultSet rs = stmt.executeQuery();
	    return new TramosPavimento(rs);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static TramosPavimento findWhereConcello(String concello) {
	Connection c = DomainMapper.getConnection();
	PreparedStatement stmt;
	try {
	    String sql = "SELECT gid, tipopavime, origenpavi, finalpavim FROM inventario.tipo_pavimento WHERE numeromuni = ?";
	    stmt = c.prepareStatement(sql);
	    stmt.setString(1, concello);
	    ResultSet rs = stmt.executeQuery();
	    return new TramosPavimento(rs);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static TramosPavimento findWhereCarreteraAndConcello(String carretera,
	    String concello) {
	Connection c = DomainMapper.getConnection();
	PreparedStatement stmt;
	try {
	    String sql = "SELECT gid, tipopavime, origenpavi, finalpavim FROM inventario.tipo_pavimento WHERE numeroinve = ? AND numeromuni = ?";
	    stmt = c.prepareStatement(sql);
	    stmt.setString(1, carretera);
	    stmt.setString(2, concello);
	    ResultSet rs = stmt.executeQuery();
	    return new TramosPavimento(rs);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

}