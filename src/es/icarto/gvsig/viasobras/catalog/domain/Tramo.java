package es.icarto.gvsig.viasobras.catalog.domain;

public class Tramo {

    // will define the order in which will appear in table
    private static final int PK_START = 0;
    private static final int PK_END = 1;
    private static final int CONCELLO = 2;
    private static final int CARRETERA = 3;
    private static final int VALUE = 4;

    private double pkStart;
    private double pkEnd;
    private String concello;
    private String carretera;
    private String value;

    public double getPkStart() {
	return pkStart;
    }

    public void setPkStart(double pkStart) {
	this.pkStart = pkStart;
    }

    public double getPkEnd() {
	return pkEnd;
    }

    public void setPkEnd(double pkEnd) {
	this.pkEnd = pkEnd;
    }

    public String getConcello() {
	return concello;
    }

    public void setConcello(String concello) {
	this.concello = concello;
    }

    public String getCarretera() {
	return carretera;
    }

    public void setCarretera(String carretera) {
	this.carretera = carretera;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public int getNumberOfProperties() {
	return 5; // number of properties defined
    }

    public String getPropertyName(int index) {
	switch (index) {
	case PK_START:
	    return "PK origen";
	case PK_END:
	    return "PK final";
	case CONCELLO:
	    return "Concello";
	case CARRETERA:
	    return "Carretera";
	case VALUE:
	    return "Característica";
	default:
	    return "None";
	}
    }

    public Object getPropertyValue(int index) {
	switch (index) {
	case PK_START:
	    return getPkStart();
	case PK_END:
	    return getPkEnd();
	case CONCELLO:
	    return getConcello();
	case CARRETERA:
	    return getCarretera();
	case VALUE:
	    return getValue();
	default:
	    return null;
	}
    }

}
