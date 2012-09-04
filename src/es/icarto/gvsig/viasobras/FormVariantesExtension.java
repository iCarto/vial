package es.icarto.gvsig.viasobras;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.viasobras.forms.FormVariantes;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormVariantesExtension extends Extension {

    public void initialize() {
	registerIcons();
    }

    private void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"viasobras-variantes",
		this.getClass().getClassLoader()
		.getResource("images/variantes.png"));
    }

    public void execute(String actionCommand) {
	FormVariantes dialog = new FormVariantes();
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
