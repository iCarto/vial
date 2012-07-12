package es.icarto.gvsig.viasobras.info;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.AbstractForm;

public class FormCarreteras extends AbstractForm implements IWindow {

    private FormPanel form;

    public FormCarreteras(FLyrVect layer) {
	super(layer);
	initWindow();
    }

    private void initWindow() {
	viewInfo.setHeight(425);
	viewInfo.setWidth(475);
	viewInfo.setTitle("Vías Obras: carreteras");
    }

    @Override
    protected void fillSpecificValues() {
    }

    @Override
    public FormPanel getFormBody() {
	if (form == null) {
	    form = new FormPanel("form-carreteras.xml");
	}
	return form;
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger("CarreterasForm");
    }

    @Override
    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.viasobras")
		.getClassLoader().getResource("viasobras.xml").getPath();
    }

}
