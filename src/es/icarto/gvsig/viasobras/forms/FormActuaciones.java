package es.icarto.gvsig.viasobras.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkButton;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormActuaciones extends AbstractForm {

    private JButton ayuntamientos;
    private JTextField codigoActuacion;
    private JComboBox tipoActuacion;
    private JTabbedPane subForms;

    public FormActuaciones(FLyrVect layer) {
	super(layer);
	initWindow();
    }

    public void initWindow() {
	viewInfo.setHeight(710);
	viewInfo.setWidth(580);
	viewInfo.setTitle("Vías y Obras: actuaciones");

	this.getActionsToolBar().add(
		new FilesLinkButton(this, new FormActuacionesFilesLinkData()));
    }

    @Override
    public FormPanel getFormBody() {
	if (formBody == null) {
	    formBody = new FormPanel("actuaciones-ui.xml");
	    subForms = (JTabbedPane) formBody.getComponentByName("tipos");
	    disableTabs();
	}
	return formBody;
    }

    private void disableTabs() {
	for (int index = 0; index < subForms.getTabCount(); index++) {
	    subForms.setEnabledAt(index, false);
	}
	subForms.setSelectedIndex(-1);
    }

    @Override
    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.viasobras")
		.getClassLoader().getResource("actuaciones-metadata.xml")
		.getPath();
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger("ActuacionesForm");
    }

    @Override
    protected void fillSpecificValues() {
	refreshSubForms();
    }

    @Override
    public void setListeners() {
	super.setListeners();

	ayuntamientos = (JButton) formBody.getComponentByName("ayuntamientos");
	ayuntamientos.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		openConcellosPanel();
	    }
	});

	codigoActuacion = (JTextField) formBody
		.getComponentByName("codigo_actuacion");

	tipoActuacion = (JComboBox) formBody.getComponentByName("tipo");
	tipoActuacion.addActionListener(new ChangeFormTab());
    }

    private void openConcellosPanel() {
	if (!codigoActuacion.getText().equals("")) {
	    FormActuacionesMunicipios cp = new FormActuacionesMunicipios(
		    codigoActuacion.getText());
	    PluginServices.getMDIManager().addWindow(cp);
	}
    }

    private final class ChangeFormTab implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
	    if ((subForms != null) && !isFillingValues()) {
		refreshSubForms();
	    }
	}
    }

    private void refreshSubForms() {
	if (subForms.getSelectedIndex() != tipoActuacion.getSelectedIndex()) {
	    if (subForms.getSelectedIndex() != -1) {
		subForms.setEnabledAt(subForms.getSelectedIndex(), false);
		HashMap<String, JComponent> widgets = AbeilleParser
			.getWidgetsFromContainer(subForms);
		for (JComponent c : widgets.values()) {
		    if (c instanceof JTextField
			    || c instanceof JFormattedTextField) {
			c.requestFocus();
			((JTextField) c).setText("");
			c.dispatchEvent(new KeyEvent(c, KeyEvent.KEY_RELEASED,
				0, 0, KeyEvent.VK_SHIFT, ' '));
		    } else if (c instanceof JTextArea) {
			c.requestFocus();
			((JTextArea) c).setText("");
			c.dispatchEvent(new KeyEvent(c, KeyEvent.KEY_RELEASED,
				0, 0, KeyEvent.VK_SHIFT, ' '));
		    } else if (c instanceof JComboBox) {
			((JComboBox) super.getWidgetComponents().get(
				c.getName())).setSelectedIndex(0);
		    }
		}
	    }
	    subForms.setEnabledAt(tipoActuacion.getSelectedIndex(), true);
	    subForms.setSelectedIndex(tipoActuacion.getSelectedIndex());
	}
    }

    private boolean isCodigoActuacionAlreadyInDB() {
	DBSession dbs = DBSession.getCurrentSession();
	Connection c = dbs.getJavaConnection();
	try {
	    c.setAutoCommit(false);
	    String sqlSelect = "SELECT codigo_actuacion "
		    + " FROM inventario.actuaciones "
		    + " WHERE codigo_actuacion = ?;";
	    PreparedStatement stSelect = c.prepareStatement(sqlSelect);
	    stSelect.setString(1, codigoActuacion.getText());
	    stSelect.execute();
	    c.commit();
	    ResultSet rs = stSelect.executeQuery();
	    rs.last();
	    int numberOfRows = rs.getRow();
	    c.close();
	    if (numberOfRows > 0) {
		return true;
	    }
	    return false;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return true;
	}
    }

    @Override
    protected boolean isSaveable() {
	if (isNewValue() && isCodigoActuacionAlreadyInDB()) {
	    showWarningPanel("Aviso: campos únicos",
		    "El código de actuación introducido ya existe.");
	    return false;
	} else {
	    return super.isSaveable();
	}
    }

    private boolean isNewValue() {
	if (getFormController().getValueInLayer("codigo_actuacion").equals(
		codigoActuacion.getText())) {
	    return false;
	}
	return true;
    }

    private void showWarningPanel(String title, String message) {
	JOptionPane.showMessageDialog(this, message,
		PluginServices.getText(this, title),
		JOptionPane.WARNING_MESSAGE);
    }

}