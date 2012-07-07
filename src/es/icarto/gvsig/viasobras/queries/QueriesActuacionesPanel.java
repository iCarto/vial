package es.icarto.gvsig.viasobras.queries;

import javax.swing.JScrollPane;

import com.jeta.forms.components.panel.FormPanel;

public class QueriesActuacionesPanel extends gvWindow {

    private FormPanel form;

    public QueriesActuacionesPanel(boolean b) {
	super(600, 400, true);
	form = new FormPanel("queries-actuaciones.xml");
	JScrollPane scrolledForm = new JScrollPane(form);
	this.add(scrolledForm);
    }

}
