package es.icarto.gvsig.viasobras.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.JTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.TableModelAlphanumeric;
import es.icarto.gvsig.navtableforms.gui.tables.TableModelFactory;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.udc.cartolab.gvsig.navtable.listeners.PositionEvent;

public class FormCarreteras extends AbstractForm implements IWindow {

    private FormPanel form;
    private JTable ayuntamientos;
    private FormCarreterasMunicipios carreterasConcellos;
    private JTableContextualMenu contextualMenu;
    private JButton rampas;
    private JButton variantes;

    public FormCarreteras(FLyrVect layer) {
	super(layer);
	initWindow();
    }

    private void initWindow() {
	viewInfo.setHeight(710);
	viewInfo.setWidth(560);
	viewInfo.setTitle("V?as y Obras: carreteras");
    }

    @Override
    protected void enableSaveButton(boolean bool) {
	if (!isChangedValues()) {
	    saveB.setEnabled(false);
	} else {
	    saveB.setEnabled(bool);
	}
    }

    @Override
    protected void fillSpecificValues() {
	updateJTables();
    }

    private void updateJTables() {
	ArrayList<String> colNames = new ArrayList<String>();
	colNames.add("codigo_municipio");
	colNames.add("orden_tramo");
	colNames.add("pk_inicial_tramo");
	colNames.add("pk_final_tramo");
	colNames.add("longitud_tramo");
	colNames.add("observaciones_tramo");
	ArrayList<String> colAliases = new ArrayList<String>();
	colAliases.add("Municipio");
	colAliases.add("Orden tramo");
	colAliases.add("PK Inicial");
	colAliases.add("PK Final");
	colAliases.add("Longitud");
	colAliases.add("Observaciones");
	TableModelAlphanumeric model;
	try {
	    model = TableModelFactory.createFromTable("carretera_municipio",
		    "codigo_carretera",
		    this.getValueFromLayer("numero"),
		    colNames,
		    colAliases);
	    ayuntamientos.setModel(model);
	    carreterasConcellos.setModel(model);
	    carreterasConcellos.setCarretera(this.getValueFromLayer("numero"));
	    this.repaint(); // will force embedded tables to refresh
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}

    }

    @Override
    public void setListeners() {
	super.setListeners();

	ayuntamientos = (JTable) this.getWidgetComponents()
		.get("ayuntamientos");
	carreterasConcellos = new FormCarreterasMunicipios(this);
	contextualMenu = new JTableContextualMenu(carreterasConcellos);
	ayuntamientos.addMouseListener(contextualMenu);
	// for the popUp to work on empty tables
	ayuntamientos.setFillsViewportHeight(true);

	variantes = (JButton) AbeilleParser.getButtonsFromContainer(form).get(
		"variantes");
	variantes.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		TOCLayerManager toc = new TOCLayerManager();
		FLyrVect l = toc
			.getLayerByName(FormVariantes.VARIANTES_LAYERNAME);
		if (l != null) {
		    FormVariantes dialog = new FormVariantes(l);
		    if (dialog.init()) {
			PluginServices.getMDIManager().addCentredWindow(dialog);
		    }
		}
	    }
	});
	rampas = (JButton) AbeilleParser.getButtonsFromContainer(form).get(
		"rampas");
	rampas.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		TOCLayerManager toc = new TOCLayerManager();
		FLyrVect l = toc.getLayerByName(FormRampas.RAMPAS_LAYERNAME);
		if (l != null) {
		    FormRampas dialog = new FormRampas(l);
		    if (dialog.init()) {
			PluginServices.getMDIManager().addCentredWindow(dialog);
		    }
		}
	    }
	});
    }

    @Override
    public void removeListeners() {
	super.removeListeners();

	ayuntamientos.removeMouseListener(contextualMenu);
    }

    @Override
    public FormPanel getFormBody() {
	if (form == null) {
	    form = new FormPanel("carreteras-ui.xml");
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
		.getClassLoader().getResource("carreteras-metadata.xml")
		.getPath();
    }

    @Override
    public void onPositionChange(PositionEvent e) {
	super.onPositionChange(e);
	updateJTables();
    }

}
