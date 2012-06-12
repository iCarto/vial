package es.icarto.gvsig.viasobras.catalog.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.ProjectDocumentFactory;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.elle.db.DBStructure;
import es.udc.cartolab.gvsig.elle.utils.ELLEMap;
import es.udc.cartolab.gvsig.elle.utils.LoadLegend;
import es.udc.cartolab.gvsig.elle.utils.MapDAO;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class MapLoader {

    public static String MAP_NAME = "Vías Obras Lugo";

    public static void load() {
	if (createMap()) {
	    loadMapInView();
	}
    }

    public static boolean createMap() {
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
	    if (!dbs.tableExists(DBStructure.getSchema(),
		    DBStructure.getMapTable())) {
		MapDAO.getInstance().createMapTables();
	    }
	    MapDAO.getInstance().saveMap(rows.toArray(new Object[0][0]),
		    MAP_NAME);
	    return true;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return false;
	}
    }

    private static View createView() {
	View view = null;
	Project project = ((ProjectExtension) PluginServices
		.getExtension(ProjectExtension.class)).getProject();
	ProjectDocumentFactory viewFactory = Project
		.getProjectDocumentFactory(ProjectViewFactory.registerName);
	ProjectDocument projectDocument = viewFactory.create(project);
	projectDocument.setName(MAP_NAME);
	project.addDocument(projectDocument);
	view = (View) projectDocument.createWindow();
	view.getWindowInfo().setMaximized(true);
	return view;
    }

    private static void loadMapInView() {
	View view = createView();
	try {
	    ELLEMap map = MapDAO.getInstance().getMap(view, MAP_NAME,
		    LoadLegend.NO_LEGEND, "");
	    map.getLayer("Carreteras").setWhere("WHERE codigo='0101'");
	    map.load(view.getProjection());
	    PluginServices.getMDIManager().addWindow(view);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
