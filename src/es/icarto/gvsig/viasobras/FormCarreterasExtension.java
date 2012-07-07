package es.icarto.gvsig.viasobras;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.viasobras.info.FormCarreteras;

public class FormCarreterasExtension extends Extension {

    public void initialize() {
    }

    public void execute(String actionCommand) {
	FormCarreteras dialog = new FormCarreteras();
	PluginServices.getMDIManager().addWindow(dialog);
    }

    public boolean isEnabled() {
	return true;
    }

    public boolean isVisible() {
	return true;
    }

}
