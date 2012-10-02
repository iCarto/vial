package es.icarto.gvsig.viasobras.domain.catalog.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domain.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.DBFacade;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class RecalculatorForm extends JPanel implements IWindow {

    private WindowInfo windowInfo;
    private JButton executeButton;
    private JScrollPane scrollPane;
    private FormPanel form;
    private JComboBox codigoCarretera;
    private JCheckBox reajustar;
    private JTextField pkInicial;
    private JTextField pkFinal;
    private JTextField offset;

    public RecalculatorForm() {
	initPanel();
    }

    private void initPanel() {
	form = new FormPanel("recalcular-caracteristicas-ui.xml");
	scrollPane = new JScrollPane(form);
	this.add(scrollPane);
	initWidgets();
	this.setFocusCycleRoot(true);
    }

    private void initWidgets() {
	codigoCarretera = (JComboBox) form.getComboBox("codigo_carretera");
	codigoCarretera.removeAllItems();
	ORMLite ormlite = new ORMLite();
	DomainValues dv = ormlite.getAplicationDomainObject(getXMLPath())
		.getDomainValuesForComponent("codigo_carretera");
	if (dv != null) {
	    for (KeyValue kv : dv.getValues()) {
		codigoCarretera.addItem(kv);
	    }
	}

	reajustar = (JCheckBox) form.getCheckBox("reajustar");
	pkInicial = (JTextField) form.getTextField("pk_inicial");
	pkInicial.setEnabled(false);
	pkFinal = (JTextField) form.getTextField("pk_final");
	pkFinal.setEnabled(false);
	offset = (JTextField) form.getTextField("offset");
	offset.setEnabled(false);
	reajustar.addActionListener(new ReajustarActivation());

	executeButton = (JButton) AbeilleParser.getButtonsFromContainer(form)
		.get("ejecutar");
	executeButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		doMagic();
	    }
	});
    }

    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.viasobras")
		.getClassLoader().getResource("viasobras-metadata.xml")
		.getPath();
    }

    public boolean doMagic() {
	initDomainMapper();
	try {
	    Connection c = DBFacade.getConnection();
	    Statement st = c.createStatement();
	    st.executeQuery("SELECT inventario.recalculate_caracteristicas('"
		    + ((KeyValue) codigoCarretera.getSelectedItem()).getKey()
		    + "')");
	    st.close();
	    System.out.println("Recalculando "
		    + ((KeyValue) codigoCarretera.getSelectedItem()).getKey());
	    return true;
	} catch (SQLException e) {
	    e.printStackTrace();
	    NotificationManager.addError(e);
	    return false;
	}
    }

    private void initDomainMapper() {
	try {
	    DBSession dbs = DBSession.getCurrentSession();
	    Properties p = new Properties();
	    p.setProperty(DBFacade.URL, dbs.getJavaConnection().getMetaData()
		    .getURL());
	    p.setProperty(DBFacade.USERNAME, dbs.getUserName());
	    p.setProperty(DBFacade.PASSWORD, dbs.getPassword());
	    // Create the connection ourselves, as at this moment, gvSIG has an
	    // old driver (lower than jdbc4) which doesn't implement the methods
	    // we need. So, take care that the jar in
	    // lib/postgresql-8.4-jdbc4.jar is used instead of gvSIG ones.
	    Class.forName("org.postgresql.Driver");
	    Connection c = DriverManager.getConnection(
		    p.getProperty(DBFacade.URL),
		    p.getProperty(DBFacade.USERNAME),
		    p.getProperty(DBFacade.PASSWORD));
	    DBFacade.setConnection(c, p);
	} catch (Exception e) {
	    NotificationManager.addError(e);
	}
    }

    public WindowInfo getWindowInfo() {
	if (windowInfo == null) {
	    windowInfo = new WindowInfo(WindowInfo.MODALDIALOG
		    | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	    windowInfo.setTitle("Vías y Obras: recalcular características");
	    windowInfo.setHeight(230);
	    windowInfo.setWidth(350);
	}
	return windowInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

    private final class ReajustarActivation implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
	    pkInicial.setEnabled(reajustar.isSelected());
	    pkFinal.setEnabled(reajustar.isSelected());
	    offset.setEnabled(reajustar.isSelected());
	}
    }

}
