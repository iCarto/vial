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

import es.udc.cartolab.gvsig.elle.utils.ELLEMap;
import es.udc.cartolab.gvsig.elle.utils.LoadLegend;
import es.udc.cartolab.gvsig.elle.utils.MapDAO;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class MapLoader {

    public static String MAP_NAME = "Vías Obras Lugo";

    public static void load() {
	createMap();
	loadMapInView();
    }

    private static void createMap() {
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
	    ELLEMap map = MapDAO.getInstance().getMap(view, MAP_NAME, "",
		    LoadLegend.NO_LEGEND, "");
	    map.load(view.getProjection());
	    PluginServices.getMDIManager().addWindow(view);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
