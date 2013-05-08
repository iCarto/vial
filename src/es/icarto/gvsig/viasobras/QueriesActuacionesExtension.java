package es.icarto.gvsig.viasobras;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.viasobras.queries.ui.PanelQueriesActuaciones;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class QueriesActuacionesExtension extends Extension {

    public void initialize() {
	registerIcons();
    }

    public void execute(String actionCommand) {
	PanelQueriesActuaciones p = new PanelQueriesActuaciones(false);
	p.open();
    }

    public boolean isEnabled() {
	DBSession dbs = DBSession.getCurrentSession();
	return (dbs != null) && (dbs.getJavaConnection() != null);
    }

    public boolean isVisible() {
	return true;
    }

    protected void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"viasobras-consultas-actuaciones",
		this.getClass().getClassLoader()
		.getResource("images/actuacion_consulta.png"));
    }

}
