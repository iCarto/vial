package es.icarto.gvsig.viasobras.maploader;

import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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

    private static final String ANCHO_DE_PLATAFORMA = "Ancho plataforma";
    private static final String TIPO_DE_PAVIMENTO = "Tipo pavimento";
    private static final String MUNICIPIOS = "Municipios";

    public static String DEFAULT_MAP_NAME = "General";

    public static void loadMap(String mapName) throws Exception {
	View view = createView(mapName);
	ELLEMap map = MapDAO.getInstance().getMap(view, mapName,
		LoadLegend.DB_LEGEND, mapName);
	// set filters for layers
	String whereConcellos = WhereAdapter.getClause(WhereAdapter.MUNICIPIOS);
	String whereTramos = WhereAdapter.getClause(WhereAdapter.TRAMOS);
	if (map.layerInMap(MUNICIPIOS)) {
	    map.getLayer(MUNICIPIOS).setWhere(whereConcellos);
	}
	if (map.layerInMap(TIPO_DE_PAVIMENTO)) {
	    map.getLayer(TIPO_DE_PAVIMENTO).setWhere(whereTramos);
	}
	if (map.layerInMap(ANCHO_DE_PLATAFORMA)) {
	    map.getLayer(ANCHO_DE_PLATAFORMA).setWhere(whereTramos);
	}
	map.load(view.getProjection());
	if (map.layerInMap(MUNICIPIOS)) {
	    Rectangle2D concellosExtent = view.getMapControl().getMapContext()
		    .getLayers().getLayer(MUNICIPIOS).getFullExtent();
	    view.getMapControl().getMapContext().zoomToExtent(concellosExtent);
	    view.getMapControl().getMapContext().getLayers().getLayer(MUNICIPIOS);
	}
	PluginServices.getMDIManager().addWindow(view);
    }

    public static void loadDefaultMap() throws Exception {
	createMap(DEFAULT_MAP_NAME);
	loadMap(DEFAULT_MAP_NAME);
    }

    private static boolean createMap(String mapName) {
	List<Object[]> rows = new ArrayList<Object[]>();
	Object[] oceano = { "Océano", "oceano", 1, true, null, null,
		"LIMITES", "info_base" };
	Object[] portugal = { "Portugal", "portugal", 2, true, null, null,
		"LIMITES", "info_base" };
	Object[] provincias_limitrofes = { "Provincias Limítrofes",
		"provincias_limitrofes", 3, true, null, null,
		"LIMITES", "info_base" };
	Object[] provincias_galicia = { "Provincias Galicia",
		"provincias_galicia", 4, true, null, null,
		"LIMITES", "info_base" };
	Object[] municipios_lugo = { "Municipios Lugo", "municipios_lugo", 5,
		true, null, null,
		"LIMITES", "info_base" };
	Object[] rio_l = { "Río L", "rio_l", 6, true, null, null,
		"HIDROGRAFIA", "info_base" };
	Object[] embalses = { "Embalses", "embalse", 7, true, null, null,
		"HIDROGRAFIA", "info_base" };
	Object[] rio_a = { "Río A", "rio_a", 8, true, null, null,
		"HIDROGRAFIA", "info_base" };
	Object[] red_carreteras = { "Red Carreteras", "red_carreteras", 9,
		true, null, null,
		"INFRAESTRUCTURAS", "info_base" };
	Object[] red_provincial = { "Red Provincial", "carreteras", 10,
		true, null, null,
		"INFRAESTRUCTURAS", "inventario" };
	Object[] municipios = { MUNICIPIOS, "municipios_lugo", 11, true, null,
		null, "", "info_base" };
	Object[] carreteras = { "Carreteras", "carreteras", 12, true, null,
		null, "", "inventario" };
	Object[] actuaciones = { "Actuaciones", "actuaciones", 13,
		true, null, null, "", "inventario" };
	Object[] cotas_maximas = { "Cotas máximas", "cotas", 14, true, null,
		null, "", "inventario" };
	Object[] pavimento = { TIPO_DE_PAVIMENTO, "tipo_pavimento", 15,
		true, null, null, "", "inventario" };
	Object[] plataforma = { ANCHO_DE_PLATAFORMA, "ancho_plataforma", 16,
		true, null, null, "", "inventario" };
	Object[] pks = { "PKs", "PKs_geometricos", 17, true, null, null,
		"", "info_base" };
	rows.add(oceano);
	rows.add(portugal);
	rows.add(provincias_limitrofes);
	rows.add(provincias_galicia);
	rows.add(municipios_lugo);
	rows.add(rio_l);
	rows.add(embalses);
	rows.add(rio_a);
	rows.add(red_carreteras);
	rows.add(red_provincial);
	rows.add(municipios);
	rows.add(carreteras);
	rows.add(actuaciones);
	rows.add(cotas_maximas);
	rows.add(pavimento);
	rows.add(plataforma);
	rows.add(pks);
	try {
	    DBSession dbs = DBSession.getCurrentSession();
	    if (!dbs.tableExists(DBStructure.getSchema(),
		    DBStructure.getMapTable())) {
		MapDAO.getInstance().createMapTables();
	    }
	    MapDAO.getInstance().saveMap(rows.toArray(new Object[0][0]),
		    mapName);
	    return true;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return false;
	}
    }

    private static View createView(String mapName) {
	View view = null;
	Project project = ((ProjectExtension) PluginServices
		.getExtension(ProjectExtension.class)).getProject();
	ProjectDocumentFactory viewFactory = Project
		.getProjectDocumentFactory(ProjectViewFactory.registerName);
	ProjectDocument projectDocument = viewFactory.create(project);
	projectDocument.setName(mapName);
	project.addDocument(projectDocument);
	view = (View) projectDocument.createWindow();
	view.getWindowInfo().setMaximized(true);
	return view;
    }

    public static List<String> getAllMapNames() {
	String[] maps;
	try {
	    maps = MapDAO.getInstance().getMaps();
	    return Arrays.asList(maps);
	} catch (SQLException e) {
	    maps = new String[] { "" };
	    return Arrays.asList(maps);
	}
    }

}
