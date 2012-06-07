package es.icarto.gvsig.viasobras.catalog.domain.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import es.icarto.gvsig.viasobras.catalog.domain.TramosPlataforma;

public class TramosPlataformaMapper extends DomainMapper {

    public static TramosPlataforma findAll() {
	Connection c = DomainMapper.getConnection();
	Statement stmt;
	try {
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT gid, ancho_plataforma, origentram, finaltramo FROM inventario.ancho_plataforma");
	    return new TramosPlataforma(rs);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static TramosPlataforma findWhereCarretera(String carretera) {
	Connection c = DomainMapper.getConnection();
	PreparedStatement stmt;
	try {
	    String sql = "SELECT gid, ancho_plataforma, origentram, finaltramo FROM inventario.ancho_plataforma WHERE carretera = ?";
	    stmt = c.prepareStatement(sql);
	    stmt.setString(1, carretera);
	    ResultSet rs = stmt.executeQuery();
	    return new TramosPlataforma(rs);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static TramosPlataforma findWhereConcello(String concello) {
	Connection c = DomainMapper.getConnection();
	PreparedStatement stmt;
	try {
	    String sql = "SELECT gid, ancho_plataforma, origentram, finaltramo FROM inventario.ancho_plataforma WHERE municipio = ?";
	    stmt = c.prepareStatement(sql);
	    stmt.setString(1, concello);
	    ResultSet rs = stmt.executeQuery();
	    return new TramosPlataforma(rs);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static TramosPlataforma findWhereCarreteraAndConcello(
	    String carretera, String concello) {
	Connection c = DomainMapper.getConnection();
	PreparedStatement stmt;
	try {
	    String sql = "SELECT gid, ancho_plataforma, origentram, finaltramo FROM inventario.ancho_plataforma WHERE carretera = ? AND municipio = ?";
	    stmt = c.prepareStatement(sql);
	    stmt.setString(1, carretera);
	    stmt.setString(2, concello);
	    ResultSet rs = stmt.executeQuery();
	    return new TramosPlataforma(rs);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

}
