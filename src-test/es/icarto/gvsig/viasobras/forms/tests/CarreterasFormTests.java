package es.icarto.gvsig.viasobras.forms.tests;

import org.junit.BeforeClass;

import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.users.utils.DBSession;



public class CarreterasFormTests extends AbstractFormTests {

    /**
     * new FormPanel(abeilleFile) needs abeilleFile to be in classpath
     * 
     * In eclipse: Debug Configuration -> TestForm -> Classpath -> Advanced ->
     * add extViasObras/forms
     * 
     * see this thread:
     * http://java.net/nonav/projects/abeille/lists/users/archive
     * /2005-06/message/7
     */
    @BeforeClass
    public static void doSetup() throws Exception {
	String uiFile = "carreteras-ui.xml";
	metadataFile = "forms/carreteras-metadata.xml";
	schemaName = "inventario";
	tableName = "carreteras";

	initgvSIGDrivers();
	DBSession.createConnection("localhost", 5432, "vias_obras",
		"inventario", "viasobras", "viasobras");
	ado = new ORMLite(metadataFile).getAppDomain();
	widgets = AbeilleParser.getWidgetsFromContainer(new FormPanel(uiFile));
    }

}
