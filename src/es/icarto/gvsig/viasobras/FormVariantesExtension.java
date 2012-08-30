package es.icarto.gvsig.viasobras;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

public class FormVariantesExtension extends Extension {

    public void initialize() {
	registerIcons();
    }

    private void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"viasobras-variantes",
		this.getClass().getClassLoader()
		.getResource("images/variantes.png"));
    }

    public void execute(String actionCommand) {
    }

    public boolean isEnabled() {
	return false;
    }

    public boolean isVisible() {
	return true;
    }

}
