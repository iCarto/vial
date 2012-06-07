package es.icarto.gvsig.viasobras.catalog.domain;

public class Concello {

    private String code;
    private String name;

    public Concello(String code, String name) {
	this.code = code;
	this.name = name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getName() {
	return this.name;
    }

    public String getCode() {
	return this.code;
    }

    /**
     * This method allow us to use the object as a parameter to a combobox.
     */
    public String toString() {
	return this.name;
    }

}
