package es.icarto.gvsig.viasobras.catalog.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.udc.cartolab.gvsig.elle.utils.MapDAO;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class MapLoader {

    public static String MAP_NAME = "Vías Obras Lugo";

    public static void load() {
	List<Object[]> rows = new ArrayList<Object[]>();
	Object[] row = {"Carreteras",
		"rede_carreteras",
		"1",
		true,
		null,
		null,
		"",
	"inventario"};
	rows.add(row);
	try {
	    DBSession dbs = DBSession.getCurrentSession();
	    if (!dbs.tableExists(dbs.getSchema(), "_map")) {
		MapDAO.getInstance().createMapTables();
	    }
	    MapDAO.getInstance().saveMap(rows.toArray(new Object[0][0]),
		    MAP_NAME);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

}
