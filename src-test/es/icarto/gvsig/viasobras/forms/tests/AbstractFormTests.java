package es.icarto.gvsig.viasobras.forms.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.iver.cit.gvsig.fmap.layers.LayerFactory;

import es.icarto.gvsig.navtableforms.ormlite.ORMLiteAppDomain;
import es.icarto.gvsig.navtableforms.ormlite.XMLSAXParser;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorDomain;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public abstract class AbstractFormTests {

    protected static ORMLiteAppDomain ado;
    protected static HashMap<String, JComponent> widgets;

    protected static String schemaName;
    protected static String tableName;
    protected static String metadataFile;

    @Test
    public void testXMLIsValid() {
	boolean thrown = false;
	try {
	    new XMLSAXParser(metadataFile);
	} catch (ParserConfigurationException e) {
	    thrown = true;
	} catch (SAXException e) {
	    thrown = true;
	} catch (IOException e) {
	    thrown = true;
	}
	assertFalse(thrown);
    }

    @Test
    public void testAllWidgetsHaveName() {
	for (JComponent widget : widgets.values()) {
	    assertNotNull(widget.getName());
	    assertTrue(widget.getName().trim().length() > 0);
	}
    }

    @Test
    public void testAllWidgetsHaveProperName() throws SQLException {
	String[] columns = DBSession.getCurrentSession().getColumns(schemaName,
		tableName);
	List<String> fieldsInDB = Arrays.asList(columns);
	for (JComponent c : widgets.values()) {
	    if(!(c instanceof JTable)) {
		assertTrue(c.getName(), fieldsInDB.contains(c.getName()));
	    }
	}
    }

    @Test
    public void testDomainValidatorsMatchWidgetNames() throws Exception {
	HashMap<String, ValidatorDomain> domainValidators = ado
		.getDomainValidators();
	for (String domainValidator : domainValidators.keySet()) {
	    assertNotNull(domainValidator, widgets.get(domainValidator));
	}
    }

    @Test
    public void testDomainValuesMatchWidgetNames() throws Exception {
	HashMap<String, DomainValues> domainValues = ado.getDomainValues();
	for (String dv : domainValues.keySet()) {
	    assertNotNull(dv, widgets.get(dv));
	}
    }

    protected static void initgvSIGDrivers() throws Exception {
	String fwAndamiDriverPath = "../_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/drivers";
	File baseDriversPath = new File(fwAndamiDriverPath);
	if (!baseDriversPath.exists()) {
	    throw new Exception("Can't find drivers path: "
		    + fwAndamiDriverPath);
	}

	LayerFactory.setDriversPath(baseDriversPath.getAbsolutePath());
	if (LayerFactory.getDM().getDriverNames().length < 1) {
	    throw new Exception("Can't find drivers in path: "
		    + fwAndamiDriverPath);
	}
    }

}
