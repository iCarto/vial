package es.icarto.gvsig.viasobras.forms;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.AbstractForm;

public class FormRampas extends AbstractForm implements IWindow {

    public static final String RAMPAS_LAYERNAME = "Rampas";

    private FormPanel form;

    public FormRampas(FLyrVect layer) {
	super(layer);
	initWindow();
    }

    public void initWindow() {
	viewInfo.setTitle("Vías y Obras: rampas");
	viewInfo.setHeight(560);
	viewInfo.setWidth(550);
    }

    @Override
    public FormPanel getFormBody() {
	if (form == null) {
	    form = new FormPanel("rampas-ui.xml");
	}
	return form;
    }

    public Object getWindowProfile() {
	return null;
    }

    @Override
    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.viasobras")
		.getClassLoader().getResource("viasobras-metadata.xml")
		.getPath();
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger("RampasForm");
    }

    @Override
    protected void fillSpecificValues() {
    }

}
