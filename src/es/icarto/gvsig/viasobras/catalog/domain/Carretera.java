package es.icarto.gvsig.viasobras.catalog.domain;

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

    public String toString() {
	return this.name;
    }

    public static String getDBNameCode() {
	return "codigo";
    }

    public static String getDBNameName() {
	return "código_pr";
    }

    public void addConcelloAffected(String codeConcello) {
	concellosAffected.add(codeConcello);
    }

    public boolean isIn(String codeConcello) {
	return concellosAffected.contains(codeConcello);
    }

}
