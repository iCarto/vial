package es.icarto.gvsig.viasobras.catalog.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.iver.cit.gvsig.fmap.featureiterators.FeatureIteratorTest;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;

import es.icarto.gvsig.viasobras.catalog.view.MapLoader;
import es.udc.cartolab.gvsig.elle.utils.MapDAO;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class CatalogLoadTests {

    static final String fwAndamiDriverPath = "../_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/drivers";
    private static File baseDataPath;
    private static File baseDriversPath;

    @Before
    public void connectToDatabase() throws Exception {
	doSetup();
	DBSession.createConnection("localhost", 5432, "vias_obras",
		"inventario", "postgres", "postgres");
    }

    @Test
    public void createMap() throws SQLException {
	MapLoader.createMap();
	assertEquals(true, MapDAO.getInstance().mapExists(MapLoader.MAP_NAME));
    }

    private void doSetup() throws Exception {
	URL url = FeatureIteratorTest.class.getResource("testdata");
	if (url == null) {
	    throw new Exception(
		    "No se encuentra el directorio con datos de prueba");
	}

	baseDataPath = new File(url.getFile());
	if (!baseDataPath.exists()) {
	    throw new Exception(
		    "No se encuentra el directorio con datos de prueba");
	}

	baseDriversPath = new File(fwAndamiDriverPath);
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
