package es.icarto.gvsig.viasobras.forms;

import com.iver.andami.PluginServices;
import com.iver.utiles.XMLEntity;

import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkData;
import es.icarto.gvsig.viasobras.preferences.ViasObrasPreferences;

public class FormActuacionesFilesLinkData implements FilesLinkData {

    public String getBaseDirectory() {
	String filesDir;
	PluginServices ps = PluginServices.getPluginServices(this);
	XMLEntity xml = ps.getPersistentXML();
	if (xml.contains(ViasObrasPreferences.PROPERTY_NAME)) {
	    filesDir = xml
		    .getStringProperty(ViasObrasPreferences.PROPERTY_NAME);
	} else {
	    filesDir = ViasObrasPreferences.DEFAULT_DIRECTORY;
	}
	return filesDir;
    }

    public String getRegisterField() {
	return "codigo_actuacion";
    }

}
