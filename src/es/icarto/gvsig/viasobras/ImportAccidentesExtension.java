package es.icarto.gvsig.viasobras;

import java.sql.SQLException;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.viasobras.forms.FormImportAccidents;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class ImportAccidentesExtension extends Extension {

    public void execute(String actionCommand) {
	FormImportAccidents form = new FormImportAccidents();
	PluginServices.getMDIManager().addCentredWindow(form);
    }

    protected void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"viasobras-accidentes",
		this.getClass().getClassLoader()
		.getResource("images/importar_accidentes.png"));
    }

    public void initialize() {
	registerIcons();
    }

    public boolean isEnabled() {
	DBSession dbs = DBSession.getCurrentSession();
	return (dbs != null) 
		&& (dbs.getJavaConnection() != null)
		&& userCanWrite();
    }

    private boolean userCanWrite() {
	DBSession dbs = DBSession.getCurrentSession();
	try {
	    return dbs.getDBUser().canWrite("inventario", "tipo_pavimento");
	} catch (SQLException e) {
	    return false;
	}
    }

    public boolean isVisible() {
	return true;
    }

}
