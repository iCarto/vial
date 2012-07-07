package es.icarto.gvsig.viasobras;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.viasobras.info.FormActuaciones;

public class FormActuacionesExtension extends Extension {

    public void initialize() {
    }

    public void execute(String actionCommand) {
	FormActuaciones dialog = new FormActuaciones();
	PluginServices.getMDIManager().addCentredWindow(dialog);
    }

    public boolean isEnabled() {
	return true;
    }

    public boolean isVisible() {
	return true;
    }

}
