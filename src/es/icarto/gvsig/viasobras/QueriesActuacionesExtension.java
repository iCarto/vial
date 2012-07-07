package es.icarto.gvsig.viasobras;

import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.viasobras.queries.QueriesPanel;

public class QueriesActuacionesExtension extends Extension {

    public void initialize() {
    }

    public void execute(String actionCommand) {
	QueriesPanel p = new QueriesPanel(false);
	p.open();
    }

    public boolean isEnabled() {
	return false;
    }

    public boolean isVisible() {
	return true;
    }

}
