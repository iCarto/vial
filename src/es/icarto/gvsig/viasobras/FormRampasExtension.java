package es.icarto.gvsig.viasobras;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.viasobras.forms.FormRampas;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormRampasExtension extends Extension {

    public void initialize() {
	registerIcons();
    }

    private void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"viasobras-rampas",
		this.getClass().getClassLoader()
		.getResource("images/rampas.png"));
    }

    public void execute(String actionCommand) {
	FormRampas dialog = new FormRampas();
	PluginServices.getMDIManager().addCentredWindow(dialog);
    }

    public boolean isEnabled() {
	DBSession dbs = DBSession.getCurrentSession();
	return (dbs != null) && (dbs.getJavaConnection() != null);
    }

    public boolean isVisible() {
	return true;
    }

}
