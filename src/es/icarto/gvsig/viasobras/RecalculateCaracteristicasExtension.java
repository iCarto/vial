package es.icarto.gvsig.viasobras;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.viasobras.domain.catalog.utils.RecalculatorForm;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class RecalculateCaracteristicasExtension extends Extension {

    public void execute(String actionCommand) {
	RecalculatorForm form = new RecalculatorForm();
	PluginServices.getMDIManager().addCentredWindow(form);
    }

    protected void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"viasobras-recalcular-caracteristicas",
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
