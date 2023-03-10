package es.icarto.gvsig.viasobras.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.icarto.gvsig.viasobras.domain.catalog.mappers.DBFacade;
import es.icarto.gvsig.viasobras.forms.utils.CatalogUpdater;
import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormRecalculateCaracteristicas extends JPanel implements IWindow {

    private WindowInfo windowInfo;
    private JButton executeButton;
    private FormPanel form;
    private JComboBox codigoCarretera;
    private JCheckBox reajustar;
    private JTextField pkInicial;
    private JTextField pkFinal;
    private JTextField offset;

    public FormRecalculateCaracteristicas() {
	initPanel();
    }

    private void initPanel() {
	form = new FormPanel("recalcular-caracteristicas-ui.xml");
	this.add(form);
	initWidgets();
	this.setFocusCycleRoot(true);
    }

    private void initWidgets() {
	codigoCarretera = (JComboBox) form.getComboBox("codigo_carretera");
	codigoCarretera.removeAllItems();
	ORMLite ormlite = new ORMLite(getXMLPath());
	DomainValues dv = ormlite.getAppDomain().getDomainValuesForComponent(
		"numero");
	if (dv != null) {
	    for (KeyValue kv : dv.getValues()) {
		codigoCarretera.addItem(kv);
	    }
	}

	reajustar = (JCheckBox) form.getCheckBox("reajustar");
	pkInicial = (JTextField) form.getTextField("pk_inicial");
	pkInicial.setEnabled(false);
	pkFinal = (JTextField) form.getTextField("pk_final");
	pkFinal.setEnabled(false);
	offset = (JTextField) form.getTextField("offset");
	offset.setEnabled(false);
	reajustar.addActionListener(new ReajustarActivation());

	executeButton = (JButton) AbeilleParser.getButtonsFromContainer(form)
		.get("ejecutar");
	executeButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		doMagic();
	    }
	});
    }

    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.viasobras")
		.getClassLoader()
		.getResource("recalcular-caracteristicas-metadata.xml")
		.getPath();
    }

    public boolean doMagic() {
	initDomainMapper();
	try {
	    Connection c = DBFacade.getConnection();
	    c.setAutoCommit(false);
	    PreparedStatement st;
	    String sqlQuery;
	    if (reajustar.isSelected()) {
		if (isPKInicialOK()) {
		    showWarningPanel(
			    "V?as y Obras: recalcular caracter?sticas",
			    "El PK inicial debe contener un valor v?lido, por ejemplo: 2,5");
		    return false;
		}
		if(isPKFinalOK()) {
		    showWarningPanel(
			    "V?as y Obras: recalcular caracter?sticas",
			    "El PK final debe contener un valor v?lido, por ejemplo: 3,2");
		    return false;
		}
		if(isOffsetOK()) {
		    showWarningPanel(
			    "V?as y Obras: recalcular caracter?sticas",
			    "El PK inicial debe contener un valor v?lido, por ejemplo: 0,5");
		    return false;
		}
		NumberFormat doubleFormat = DoubleFormatNT
			.getDisplayingFormat();
		double pkInicialValue, pkFinalValue, offsetValue;
		try {
		    pkInicialValue = (Double) doubleFormat.parse(
			    pkInicial.getText()).doubleValue();
		    pkFinalValue = (Double) doubleFormat.parse(
			    pkFinal.getText()).doubleValue();
		    offsetValue = (Double) doubleFormat.parse(
			    offset.getText()).doubleValue();
		} catch (ParseException e) {
		    showWarningPanel("Aviso: PKs",
			    "Revise los valores en los campos PKs");
		    return false;
		}
		sqlQuery = "SELECT inventario.readjust_tramos(?, ?, ?, ?)";
		st = c.prepareStatement(sqlQuery);
		st.setString(1,
			((KeyValue) codigoCarretera.getSelectedItem()).getKey());
		st.setDouble(2, pkInicialValue);
		st.setDouble(3, pkFinalValue);
		st.setDouble(4, offsetValue);
	    } else {
		sqlQuery = "SELECT inventario.recalculate_caracteristicas(?)";
		st = c.prepareStatement(sqlQuery);
		st.setString(1,
			((KeyValue) codigoCarretera.getSelectedItem()).getKey());
	    }
	    st.execute();
	    c.commit();
	    showWarningPanel("Aviso: recalcular",
		    "Reajuste realizado con ?xito");
	    CatalogUpdater.update();
	    return true;
	} catch (SQLException e) {
	    e.printStackTrace();
	    NotificationManager.addError(e);
	    return false;
	}
    }

    private boolean isPKInicialOK() {
	if (pkInicial.getText().equals("")) {
	    return true;
	}
	try {
	    NumberFormat doubleFormat = DoubleFormatNT.getDisplayingFormat();
	    doubleFormat.parse(pkInicial.getText());
	    return false;
	} catch (ParseException e) {
	    e.printStackTrace();
	    return true;
	}
    }

    private boolean isPKFinalOK() {
	if (pkFinal.getText().equals("")) {
	    return true;
	}
	try {
	    NumberFormat doubleFormat = DoubleFormatNT.getDisplayingFormat();
	    doubleFormat.parse(pkFinal.getText());
	    return false;
	} catch (ParseException e) {
	    e.printStackTrace();
	    return true;
	}
    }

    private boolean isOffsetOK() {
	if (offset.getText().equals("")) {
	    return true;
	}
	try {
	    NumberFormat doubleFormat = DoubleFormatNT.getDisplayingFormat();
	    doubleFormat.parse(offset.getText());
	    return false;
	} catch (ParseException e) {
	    e.printStackTrace();
	    return true;
	}
    }

    private void showWarningPanel(String title, String message) {
	JOptionPane.showMessageDialog(this, message,
		PluginServices.getText(this, title),
		JOptionPane.WARNING_MESSAGE);
    }

    private void initDomainMapper() {
	try {
	    DBSession dbs = DBSession.getCurrentSession();
	    Properties p = new Properties();
	    p.setProperty(DBFacade.URL, dbs.getJavaConnection().getMetaData()
		    .getURL());
	    p.setProperty(DBFacade.USERNAME, dbs.getUserName());
	    p.setProperty(DBFacade.PASSWORD, dbs.getPassword());
	    // Create the connection ourselves, as at this moment, gvSIG has an
	    // old driver (lower than jdbc4) which doesn't implement the methods
	    // we need. So, take care that the jar in
	    // lib/postgresql-8.4-jdbc4.jar is used instead of gvSIG ones.
	    Class.forName("org.postgresql.Driver");
	    Connection c = DriverManager.getConnection(
		    p.getProperty(DBFacade.URL),
		    p.getProperty(DBFacade.USERNAME),
		    p.getProperty(DBFacade.PASSWORD));
	    DBFacade.setConnection(c, p);
	} catch (Exception e) {
	    NotificationManager.addError(e);
	}
    }

    public WindowInfo getWindowInfo() {
	if (windowInfo == null) {
	    windowInfo = new WindowInfo(WindowInfo.MODALDIALOG
		    | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	    windowInfo.setTitle("V?as y Obras: recalcular caracter?sticas");
	    windowInfo.setHeight(200);
	    windowInfo.setWidth(300);
	}
	return windowInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

    private final class ReajustarActivation implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
	    pkInicial.setEnabled(reajustar.isSelected());
	    pkFinal.setEnabled(reajustar.isSelected());
	    offset.setEnabled(reajustar.isSelected());
	}
    }

}
