package es.icarto.gvsig.viasobras.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkButton;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;

public class FormActuaciones extends AbstractForm {

    private FormPanel form;
    private JButton ayuntamientos;
    private JTextField codigoActuacion;

    public FormActuaciones(FLyrVect layer) {
	super(layer);
	initWindow();
    }

    public void initWindow() {
	viewInfo.setHeight(560);
	viewInfo.setWidth(550);
	viewInfo.setTitle("Vías y Obras: actuaciones");

	this.getActionsToolBar().add(
		new FilesLinkButton(this, new FormActuacionesFilesLinkData()));
    }

    @Override
    public FormPanel getFormBody() {
	if (form == null) {
	    form = new FormPanel("actuaciones-ui.xml");
	}
	return form;
    }

    @Override
    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.viasobras")
		.getClassLoader().getResource("viasobras-metadata.xml")
		.getPath();
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger("ActuacionesForm");
    }

    @Override
    protected void fillSpecificValues() {
    }

    @Override
    public void setListeners() {
	super.setListeners();

	ayuntamientos = (JButton) AbeilleParser.getButtonsFromContainer(form)
		.get("ayuntamientos");
	ayuntamientos.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		openConcellosPanel();
	    }
	});

	codigoActuacion = (JTextField) this.getWidgetComponents().get(
		"codigo_actuacion");
    }

    private void openConcellosPanel() {
	if (!codigoActuacion.getText().equals("")) {
	    FormActuacionesMunicipios cp = new FormActuacionesMunicipios(
		    codigoActuacion.getText());
	    PluginServices.getMDIManager().addWindow(cp);
	}
    }

}
