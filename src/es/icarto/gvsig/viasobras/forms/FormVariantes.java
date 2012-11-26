package es.icarto.gvsig.viasobras.forms;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.AbstractForm;

public class FormVariantes extends AbstractForm implements IWindow {

    private FormPanel form;

    public static final String VARIANTES_LAYERNAME = "Tramos antiguos";

    public FormVariantes(FLyrVect layer) {
	super(layer);
	initWindow();
    }

    @Override
    public FormPanel getFormBody() {
	if (form == null) {
	    form = new FormPanel("variantes-ui.xml");
	}
	return form;
    }

    public void initWindow() {
	viewInfo.setTitle("Vías y Obras: tramos antiguos");
	viewInfo.setHeight(570);
	viewInfo.setWidth(560);
    }

    @Override
    protected void enableSaveButton(boolean bool) {
	if (!isChangedValues()) {
	    saveB.setEnabled(false);
	} else {
	    saveB.setEnabled(bool);
	}
    }

    public Object getWindowProfile() {
	return null;
    }

    @Override
    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.viasobras")
		.getClassLoader().getResource("variantes-metadata.xml")
		.getPath();
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger("VariantesForm");
    }

    @Override
    protected void fillSpecificValues() {
    }

}
