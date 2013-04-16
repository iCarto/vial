package es.icarto.gvsig.viasobras.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.DBFacade;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormRecalculatePKs extends JPanel implements IWindow {

    private WindowInfo windowInfo;
    private JButton executeButton;
    private FormPanel form;
    private JComboBox codigoCarretera;

    public FormRecalculatePKs() {
	initPanel();
    }

    private void initPanel() {
	form = new FormPanel("recalcular-pks-ui.xml");
	this.add(form);
	initWidgets();
	this.setFocusCycleRoot(true);
    }

    private void initWidgets() {
	codigoCarretera = (JComboBox) form.getComboBox("codigo_carretera");
	codigoCarretera.removeAllItems();
	ORMLite ormlite = new ORMLite(getXMLPath());
	DomainValues dv = ormlite.getAppDomain().getDomainValuesForComponent(
		"numero");
	if (dv != null) {
	    for (KeyValue kv : dv.getValues()) {
		codigoCarretera.addItem(kv);
	    }
	}

	executeButton = (JButton) AbeilleParser.getButtonsFromContainer(form)
		.get("ejecutar");
	executeButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		doMagic();
		try {
		    reloadPKLayer();
		} catch (ReloadLayerException e) {
		    showWarningPanel("Aviso: recargar capa PKs",
			    "No se ha podido recargar la capa de PKS. Por favor, hágalo manualmente");
		}
	    }
	});
    }

    private void reloadPKLayer() throws ReloadLayerException {
	TOCLayerManager toc = new TOCLayerManager();
	FLyrVect pkLayer = toc.getLayerByName("PKs");
	if (pkLayer != null) {
	    pkLayer.reload();
	}
    }

    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.viasobras")
		.getClassLoader()
		.getResource("recalcular-pks-metadata.xml")
		.getPath();
    }

    public boolean doMagic() {
	initDomainMapper();
	try {
	    Connection c = DBFacade.getConnection();
	    c.setAutoCommit(false);
	    PreparedStatement st;
	    String sqlQuery;
	    sqlQuery = "SELECT inventario.update_pks_1000_function(?)";
	    st = c.prepareStatement(sqlQuery);
	    st.setString(1,
		    ((KeyValue) codigoCarretera.getSelectedItem()).getKey());
	    st.execute();
	    c.commit();
	    showWarningPanel("Aviso: recalcular", "PKs recalculados con éxito");
	    return true;
	} catch (SQLException e) {
	    e.printStackTrace();
	    NotificationManager.addError(e);
	    return false;
	}
    }

    private void showWarningPanel(String title, String message) {
	JOptionPane.showMessageDialog(this, message,
		PluginServices.getText(this, title),
		JOptionPane.WARNING_MESSAGE);
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
	    windowInfo.setTitle("Vías y Obras: recalcular PKs");
	    windowInfo.setHeight(90);
	    windowInfo.setWidth(300);
	}
	return windowInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

}
