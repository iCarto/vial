package es.icarto.gvsig.viasobras.forms.utils;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;

import es.icarto.gvsig.viasobras.domain.catalog.Catalog;
import es.icarto.gvsig.viasobras.forms.FormCatalog;

public class CatalogUpdater {

    public static void update() {
	Catalog.setInvalid();
	Catalog.clear();
	IWindow[] windows = PluginServices.getMDIManager().getAllWindows();
	for (IWindow w : windows) {
	    if (w instanceof FormCatalog) {
		PluginServices.getMDIManager().closeWindow(w);
	    }
	}
    }

}
