package es.icarto.gvsig.viasobras.domain.catalog;

import java.util.ArrayList;
import java.util.List;

public class Carretera {

    private String code;
    private String name;
    private List<String> concellosAffected;

    public Carretera(String code, String name) {
	this.code = code;
	this.name = name;
	this.concellosAffected = new ArrayList<String>();
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    /**
     * This method allow us to use the object as a parameter to a combobox.
     */
    public String toString() {
	return this.name;
    }

    public void addConcelloAffected(String codeConcello) {
	concellosAffected.add(codeConcello);
    }

    public boolean isIn(String codeConcello) {
	return concellosAffected.contains(codeConcello);
    }

}
