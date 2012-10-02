package es.icarto.gvsig.viasobras.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormActuacionesMunicipios extends JPanel implements IWindow {

    private WindowInfo viewInfo = null;

    private FormPanel form;
    private JScrollPane scrollPane;
    private JButton save;

    private String actuacion;
    private ArrayList<String> concellos;
    private HashMap<String, JComponent> concellosCHB;

    public FormActuacionesMunicipios(String actuacion) {
	this.actuacion = actuacion;
	initPanel();
	initConcellosAffected();
	setConcellosCheckBoxes();
    }

    private void initPanel() {
	form = new FormPanel("actuacion-municipio-ui.xml");
	scrollPane = new JScrollPane(form);
	this.add(scrollPane);
	JPanel panel = new JPanel();
	save = new JButton("Guardar");
	save.addActionListener(new SaveAction());
	panel.add(save);
	this.add(panel);
    }

    private void initConcellosAffected() {
	this.concellos = new ArrayList<String>();
	DBSession dbs = DBSession.getCurrentSession();
	Connection c = dbs.getJavaConnection();
	try {
	    c.setAutoCommit(false);
	    String sqlSelect = "SELECT codigo_municipio FROM inventario.actuacion_municipio WHERE codigo_actuacion = ?;";
	    PreparedStatement stSelect = c.prepareStatement(sqlSelect);
	    stSelect.setString(1, actuacion);
	    ResultSet rs = stSelect.executeQuery();
	    c.commit();
	    while (rs.next()) {
		concellos.add(rs.getString("codigo_municipio"));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    concellos.clear();
	}
    }

    public Object getWindowModel() {
	return "form-concellos";
    }

    public WindowInfo getWindowInfo() {
	if (viewInfo == null) {
	    viewInfo = new WindowInfo(WindowInfo.MODALDIALOG
		    | WindowInfo.PALETTE);
	    viewInfo.setTitle("Vías y Obras: actuación / municipio");
	    viewInfo.setHeight(300);
	    viewInfo.setWidth(800);
	}
	return viewInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

    private void setConcellosCheckBoxes() {
	concellosCHB = AbeilleParser
		.getWidgetsFromContainer(form);
	for (String concello : concellos) {
	    ((JCheckBox) concellosCHB.get(concello)).setSelected(true);
	}
    }

    private final class SaveAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    concellos.clear();
	    for (JComponent c : concellosCHB.values()) {
		if (((JCheckBox) c).isSelected()) {
		    concellos.add(c.getName());
		}
	    }
	    if(concellos.size() > 0) {
		DBSession dbs = DBSession.getCurrentSession();
		Connection c = dbs.getJavaConnection();
		try {
		    c.setAutoCommit(false);

		    String sqlDelete = "DELETE FROM inventario.actuacion_municipio WHERE codigo_actuacion = ?;";
		    String sqlInsert = "INSERT INTO inventario.actuacion_municipio (codigo_actuacion, codigo_municipio) VALUES (?, ?);";

		    PreparedStatement stDelete = c.prepareStatement(sqlDelete);
		    stDelete.setString(1, actuacion);
		    stDelete.execute();

		    for (String concello : concellos) {
			PreparedStatement stInsert = c
				.prepareStatement(sqlInsert);
			stInsert.setString(1, actuacion);
			stInsert.setString(2, concello);
			stInsert.execute();
		    }
		    c.commit();
		} catch (SQLException e1) {
		    e1.printStackTrace();
		    NotificationManager.addError(e1);
		}
	    }
	}
    }

}
