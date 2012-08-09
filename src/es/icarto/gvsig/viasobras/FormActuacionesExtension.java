package es.icarto.gvsig.viasobras;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.icarto.gvsig.viasobras.info.AlphanumericTableLoader;
import es.icarto.gvsig.viasobras.info.FormActuaciones;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormActuacionesExtension extends Extension {

    private String actuacionesLayerName = "Actuaciones";
    public void initialize() {
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
