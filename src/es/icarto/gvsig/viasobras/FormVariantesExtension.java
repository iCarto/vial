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
			.getResource("images/variante.png"));
    }

    public void execute(String actionCommand) {
	// TODO Auto-generated method stub

    }

    public boolean isEnabled() {
	// TODO Auto-generated method stub
	return false;
    }

    public boolean isVisible() {
	// TODO Auto-generated method stub
	return true;
    }

}
