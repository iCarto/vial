package es.icarto.gvsig.viasobras.catalog.view.load;

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
	Object[] oceano = { "Océano", "oceano", "1", true, null, null, "",
	"info_base" };
	Object[] portugal = { "Portugal", "portugal", "2", true, null, null,
		"", "info_base" };
	Object[] provincias_galicia = { "Provincias Galicia",
		"provincias_galicia", "3", true, null, null, "", "info_base" };
	Object[] provincias_limitrofes = { "Provincias España",
		"provincias_limitrofes", "4", true, null, null, "", "info_base" };
	Object[] concellos = { "Concellos", "concellos", "5", true, null,
		null, "", "info_base" };
	Object[] carreteras = { "Carreteras", "rede_carreteras", "6",
		true, null, null, "", "inventario" };
	Object[] pavimento = { "Tipo de pavimento", "tipo_pavimento", "7",
		true, null, null, "", "inventario" };
	Object[] plataforma = { "Ancho de plataforma", "ancho_plataforma", "8",
		true, null, null, "", "inventario" };
	rows.add(portugal);
	rows.add(provincias_galicia);
	rows.add(provincias_limitrofes);
	rows.add(oceano);
	rows.add(concellos);
	rows.add(carreteras);
	rows.add(pavimento);
	rows.add(plataforma);
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
		    LoadLegend.DB_LEGEND, MAP_NAME);
	    // filter concellos & tramos
	    String whereConcellos = WhereAdapter
		    .getClause(WhereAdapter.CONCELLOS);
	    map.getLayer("Concellos").setWhere(whereConcellos);
	    String whereTramos = WhereAdapter.getClause(WhereAdapter.TRAMOS);
	    map.getLayer("Tipo de pavimento").setWhere(whereTramos);
	    map.getLayer("Ancho de plataforma").setWhere(whereTramos);
	    map.load(view.getProjection());
	    PluginServices.getMDIManager().addWindow(view);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
