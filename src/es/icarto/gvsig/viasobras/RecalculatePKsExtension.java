package es.icarto.gvsig.viasobras;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.viasobras.forms.FormRecalculatePKs;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class RecalculatePKsExtension extends Extension {

    public void execute(String actionCommand) {
	FormRecalculatePKs form = new FormRecalculatePKs();
	PluginServices.getMDIManager().addCentredWindow(form);
    }

    protected void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"viasobras-recalcular-pks",
		this.getClass().getClassLoader()
		.getResource("images/recalcular.png"));
    }

    public void initialize() {
	registerIcons();
    }

    public boolean isEnabled() {
	DBSession dbs = DBSession.getCurrentSession();
	return (dbs != null) && (dbs.getJavaConnection() != null);
    }

    public boolean isVisible() {
	return true;
    }

}
