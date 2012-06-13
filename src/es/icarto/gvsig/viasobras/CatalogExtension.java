package es.icarto.gvsig.viasobras;

import java.sql.SQLException;
import java.util.Properties;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.plugins.IExtension;

import es.icarto.gvsig.viasobras.catalog.domain.mappers.DomainMapper;
import es.icarto.gvsig.viasobras.catalog.view.CatalogForm;
import es.udc.cartolab.gvsig.users.DBConnectionExtension;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class CatalogExtension extends Extension {

    public void execute(String actionCommand) {
	DBSession dbs = DBSession.getCurrentSession();
	try {
	    Properties p = new Properties();
	    p.setProperty("url", dbs.getJavaConnection().getMetaData().getURL());
	    p.setProperty("username", dbs.getUserName());
	    p.setProperty("password", dbs.getPassword());
	    DomainMapper.setConnection(dbs.getJavaConnection(), p);
	    CatalogForm dialog = new CatalogForm();
	    PluginServices.getMDIManager().addWindow(dialog);
	} catch (SQLException e) {
	    NotificationManager.addError(e);
	}
    }

    protected void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"viasobras-catalogo",
		this.getClass().getClassLoader()
		.getResource("images/catalogo.png"));
    }

    public void initialize() {
	registerIcons();
    }

    public void postInitialize() {
	PluginServices.getMDIManager().closeAllWindows();
	IExtension dbconnection = PluginServices
		.getExtension(DBConnectionExtension.class);
	dbconnection.execute(null);
    }

    public boolean isEnabled() {
	DBSession dbs = DBSession.getCurrentSession();
	return dbs != null;
    }

    public boolean isVisible() {
	return true;
    }

}
