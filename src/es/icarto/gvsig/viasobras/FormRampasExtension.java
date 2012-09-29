package es.icarto.gvsig.viasobras;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
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
	TOCLayerManager toc = new TOCLayerManager();
	FLyrVect l = toc.getLayerByName(FormRampas.RAMPAS_LAYERNAME);
	if (l != null) {
	    FormRampas dialog = new FormRampas(l);
	    if (dialog.init()) {
		PluginServices.getMDIManager().addCentredWindow(dialog);
	    }
	}
    }

    public boolean isEnabled() {
	TOCLayerManager toc = new TOCLayerManager();
	DBSession dbs = DBSession.getCurrentSession();
	return (dbs != null) && (dbs.getJavaConnection() != null)
		&& (toc.getLayerByName(FormRampas.RAMPAS_LAYERNAME) != null);
    }

    public boolean isVisible() {
	return true;
    }

}
