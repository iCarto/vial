package es.icarto.gvsig.viasobras.catalog.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.iver.cit.gvsig.fmap.layers.LayerFactory;

import es.icarto.gvsig.viasobras.catalog.view.load.MapLoader;
import es.udc.cartolab.gvsig.elle.utils.MapDAO;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class CatalogLoadTests {

    @Before
    public void connectToDatabase() throws Exception {
	doSetup();
	DBSession.createConnection("localhost", 5432, "vias_obras",
		"inventario", "viasobras", "viasobras");
    }

    @Test
    public void createMap() throws SQLException {
	MapLoader.createMap();
	assertEquals(true, MapDAO.getInstance().mapExists(MapLoader.MAP_NAME));
    }

    private void doSetup() throws Exception {
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
