package es.icarto.gvsig.viasobras;

import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.viasobras.queries.QueriesActuacionesPanel;

public class QueriesActuacionesExtension extends Extension {

    public void initialize() {
    }

    public void execute(String actionCommand) {
	QueriesActuacionesPanel p = new QueriesActuacionesPanel(false);
	p.open();
    }

    public boolean isEnabled() {
	return true;
    }

    public boolean isVisible() {
	return true;
    }

}
