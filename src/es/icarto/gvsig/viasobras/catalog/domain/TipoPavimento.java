package es.icarto.gvsig.viasobras.catalog.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.TableModel;

public class TipoPavimento extends DomainMapper {

    private ResultSet rs;
    private TableModel tm;

    public TipoPavimento(ResultSet rs) {
	this.rs = rs;
    }

    public static TipoPavimento findAll() {
	Connection c = DomainMapper.getConnection();
	Statement stmt;
	try {
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT gid, tipopavime, origenpavi, finalpavim FROM inventario.tipo_pavimento");
	    return new TipoPavimento(rs);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static TipoPavimento findWhereCarretera(String carretera) {
	Connection c = DomainMapper.getConnection();
	PreparedStatement stmt;
	try {
	    String sql = "SELECT gid, tipopavime, origenpavi, finalpavim FROM inventario.tipo_pavimento WHERE numeroinve = ?";
	    stmt = c.prepareStatement(sql);
	    stmt.setString(1, carretera);
	    ResultSet rs = stmt.executeQuery();
	    return new TipoPavimento(rs);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static TipoPavimento findWhereConcello(String concello) {
	Connection c = DomainMapper.getConnection();
	PreparedStatement stmt;
	try {
	    String sql = "SELECT gid, tipopavime, origenpavi, finalpavim FROM inventario.tipo_pavimento WHERE numeromuni = ?";
	    stmt = c.prepareStatement(sql);
	    stmt.setString(1, concello);
	    ResultSet rs = stmt.executeQuery();
	    return new TipoPavimento(rs);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static TipoPavimento findWhereCarreteraAndConcello(String carretera,
	    String concello) {
	Connection c = DomainMapper.getConnection();
	PreparedStatement stmt;
	try {
	    String sql = "SELECT gid, tipopavime, origenpavi, finalpavim FROM inventario.tipo_pavimento WHERE numeroinve = ? AND numeromuni = ?";
	    stmt = c.prepareStatement(sql);
	    stmt.setString(1, carretera);
	    stmt.setString(2, concello);
	    ResultSet rs = stmt.executeQuery();
	    return new TipoPavimento(rs);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public TableModel getTableModel() {
	if (this.tm == null) {
	    this.tm = new TipoPavimentoTableModel(this.rs);
	}
	return this.tm;
    }

}
