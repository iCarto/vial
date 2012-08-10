package es.icarto.gvsig.viasobras.forms;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.InternalFrameEvent;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.TableModelFactory;
import es.icarto.gvsig.navtableforms.launcher.AlphanumericNavTableLauncher;
import es.icarto.gvsig.navtableforms.launcher.ILauncherForm;
import es.icarto.gvsig.navtableforms.launcher.LauncherParams;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class FormCarreteras extends AbstractForm implements IWindow,
ILauncherForm {

    private FormPanel form;
    private JTable ayuntamientos;
    private AlphanumericNavTableLauncher antl;
    private JComboBox codigo;

    public FormCarreteras(FLyrVect layer) {
	super(layer);
	initWindow();
    }

    private void initWindow() {
	viewInfo.setHeight(500);
	viewInfo.setWidth(500);
	viewInfo.setTitle("Vías Obras: carreteras");
    }

    @Override
    protected void fillSpecificValues() {
	updateJTables();
    }

    private void updateJTables() {
	ArrayList<String> colNames = new ArrayList<String>();
	colNames.add("pk_inicial");
	colNames.add("pk_final");
	colNames.add("codigo_concello");
	colNames.add("observaciones");
	ArrayList<String> colAliases = new ArrayList<String>();
	colAliases.add("PK Inicial");
	colAliases.add("PK Final");
	colAliases.add("Código concello");
	colAliases.add("Observaciones");
	try {
	    ayuntamientos.setModel(TableModelFactory.createFromTable(
		    "carreteras_concellos", "codigo_carretera",
		    ((KeyValue) codigo.getSelectedItem()).getKey(), colNames,
		    colAliases));
	} catch (ReadDriverException e) {
	    NotificationManager.addError(e);
	}
    }

    @Override
    public void setListeners() {
	super.setListeners();

	LauncherParams lp = new LauncherParams(this, "carreteras_concellos",
		"Ayuntamientos", "Editar ayuntamientos");
	antl = new AlphanumericNavTableLauncher(
		this, lp);
	codigo = (JComboBox) this.getWidgetComponents().get("codigo");
	ayuntamientos = (JTable) this.getWidgetComponents()
		.get("ayuntamientos");
	ayuntamientos.addMouseListener(antl);
    }

    @Override
    public void removeListeners() {
	super.removeListeners();

	ayuntamientos.removeMouseListener(antl);
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

    public void internalFrameActivated(InternalFrameEvent arg0) {
    }

    public void internalFrameClosed(InternalFrameEvent arg0) {
    }

    public void internalFrameClosing(InternalFrameEvent arg0) {
    }

    public void internalFrameDeactivated(InternalFrameEvent arg0) {
	updateJTables();
    }

    public void internalFrameDeiconified(InternalFrameEvent arg0) {
    }

    public void internalFrameIconified(InternalFrameEvent arg0) {
    }

    public void internalFrameOpened(InternalFrameEvent arg0) {
    }

    public String getSQLQuery(String queryID) {
	return null;
    }

}
