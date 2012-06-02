package es.icarto.gvsig.viasobras;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.plugins.IExtension;

import es.icarto.gvsig.viasobras.catalog.InventarioForm;
import es.udc.cartolab.gvsig.users.DBConnectionExtension;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class InventarioExtension extends Extension {

    public void execute(String actionCommand) {
	InventarioForm dialog = new InventarioForm();
	PluginServices.getMDIManager().addWindow(dialog);
    }

    protected void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"viasobras-inventario",
		this.getClass().getClassLoader()
			.getResource("images/inventario.png"));
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
