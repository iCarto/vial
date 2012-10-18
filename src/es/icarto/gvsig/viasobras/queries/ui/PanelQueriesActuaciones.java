package es.icarto.gvsig.viasobras.queries.ui;

import javax.swing.JScrollPane;

import com.jeta.forms.components.panel.FormPanel;


public class PanelQueriesActuaciones extends gvWindow {

    private FormPanel form;

    public PanelQueriesActuaciones(boolean b) {
	super(600, 400, true);
	form = new FormPanel("consultas-actuaciones-ui.xml");
	JScrollPane scrolledForm = new JScrollPane(form);
	this.add(scrolledForm);
    }

}
