package es.icarto.gvsig.viasobras.domain.catalog;

import java.util.Iterator;
import java.util.List;

public class Concellos implements Iterable<Concello> {

    private List<Concello> concellos;

    public Concellos(List<Concello> concellos) {
	this.concellos = concellos;
    }

    public Iterator<Concello> iterator() {
	return concellos.iterator();
    }

    public int size() {
	return concellos.size();
    }

}
