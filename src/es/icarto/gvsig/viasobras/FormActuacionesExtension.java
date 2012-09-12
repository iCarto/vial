package es.icarto.gvsig.viasobras;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.preferences.IPreference;
import com.iver.andami.preferences.IPreferenceExtension;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.icarto.gvsig.viasobras.forms.FormActuaciones;
import es.icarto.gvsig.viasobras.forms.utils.AlphanumericTableLoader;
import es.icarto.gvsig.viasobras.preferences.ViasObrasPreferences;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormActuacionesExtension extends Extension implements
IPreferenceExtension {

    private String actuacionesLayerName = "Actuaciones";

    public void initialize() {
	registerIcons();
    }

    private void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"viasobras-actuaciones",
		this.getClass().getClassLoader()
		.getResource("images/actuaciones.png"));
    }

    public IPreference[] getPreferencesPages() {
	IPreference[] preferences = new IPreference[1];
	preferences[0] = new ViasObrasPreferences();
	return preferences;
    }

    public void execute(String actionCommand) {
	TOCLayerManager toc = new TOCLayerManager();
	FLyrVect l = toc.getLayerByName(actuacionesLayerName);
	try {
	    AlphanumericTableLoader.loadTables();
	    if (l != null) {
		FormActuaciones dialog = new FormActuaciones(l);
		if (dialog.init()) {
		    PluginServices.getMDIManager().addCentredWindow(dialog);
		}
	    }
	} catch (Exception e) {
	    NotificationManager.addError(e);
	}
    }

    public boolean isEnabled() {
	TOCLayerManager toc = new TOCLayerManager();
	DBSession dbs = DBSession.getCurrentSession();
	return (dbs != null) && (dbs.getJavaConnection() != null)
		&& (toc.getLayerByName(actuacionesLayerName) != null);
    }

    public boolean isVisible() {
	return true;
    }

}
