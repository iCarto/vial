package es.icarto.gvsig.viasobras.catalog.domain;

public class Concello {

    private int code;
    private String name;

    public Concello(int code, String name) {
	this.code = code;
	this.name = name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setCode(int code) {
	this.code = code;
    }

    public String getName() {
	return this.name;
    }

    public int getCode() {
	return this.code;
    }
}
