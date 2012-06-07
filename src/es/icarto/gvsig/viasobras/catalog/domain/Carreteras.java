package es.icarto.gvsig.viasobras.catalog.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Carreteras implements Iterable<Carretera> {

    private List<Carretera> carreteras;
    private Map<String, Integer> indexes = new HashMap<String, Integer>();

    public Carreteras(List<Carretera> carreteras, Map<String, Integer> indexes) {
	this.carreteras = carreteras;
	this.indexes = indexes;
    }

    public Iterator<Carretera> iterator() {
	return carreteras.iterator();
    }

    public int size() {
	return carreteras.size();
    }

    public Carretera getCarretera(String codeCarretera) {
	int cIndex = indexes.get(codeCarretera);
	return carreteras.get(cIndex);
    }

}
