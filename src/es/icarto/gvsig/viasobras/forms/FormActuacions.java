package es.icarto.gvsig.viasobras.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;

public class FormActuacions extends AbstractForm {

    private FormPanel form;
    private JButton ayuntamientos;

    public FormActuacions(FLyrVect layer) {
	super(layer);
	initWindow();
    }

    public void initWindow() {
	viewInfo.setHeight(540);
	viewInfo.setWidth(450);
	viewInfo.setTitle("Vías y Obras: actuaciones");
    }

    @Override
    public FormPanel getFormBody() {
	if (form == null) {
	    form = new FormPanel("form-actuaciones.xml");
	}
	return form;
    }

    @Override
    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.viasobras")
		.getClassLoader().getResource("viasobras.xml").getPath();
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
    }

    private void openConcellosPanel() {
	FormActuacionsConcellos cp = new FormActuacionsConcellos("0000");
	PluginServices.getMDIManager().addWindow(cp);
    }

}
