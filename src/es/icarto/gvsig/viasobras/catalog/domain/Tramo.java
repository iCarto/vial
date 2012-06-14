package es.icarto.gvsig.viasobras.catalog.domain;

public class Tramo {

    // will define the order in which will appear in table
    public static final int PROPERTY_CARRETERA = 0;
    public static final int PROPERTY_CONCELLO = 1;
    public static final int PROPERTY_PK_START = 2;
    public static final int PROPERTY_PK_END = 3;
    public static final int PROPERTY_VALUE = 4;
    private static final int NUMBER_OF_PROPERTIES = 5;

    /*
     * Indicates how this tramo will be processed when savin data into DB:
     * 
     * - STATUS_ORIGINAL: it will be discarded
     * - STATUS_INSERT: an INSERT query will be built
     * - STATUS_UPDATE: an UPDATE query will be built
     * - STATUS_DELETE: an DELETE query will be built
     */
    public static final int STATUS_ORIGINAL = 0;
    public static final int STATUS_INSERT = 1;
    public static final int STATUS_UPDATE = 2;
    public static final int STATUS_DELETE = 3;

    private int status = STATUS_ORIGINAL;

    private int id;// gid field
    private double pkStart;
    private double pkEnd;
    private String concello;
    private String carretera;
    private String value;

    public int getStatus() {
	return this.status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

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
	return NUMBER_OF_PROPERTIES;
    }

    public String getPropertyName(int index) {
	switch (index) {
	case PROPERTY_PK_START:
	    return "PK origen";
	case PROPERTY_PK_END:
	    return "PK final";
	case PROPERTY_CONCELLO:
	    return "Concello";
	case PROPERTY_CARRETERA:
	    return "Carretera";
	case PROPERTY_VALUE:
	    return "Característica";
	default:
	    return "None";
	}
    }

    public Object getPropertyValue(int index) {
	switch (index) {
	case PROPERTY_PK_START:
	    return getPkStart();
	case PROPERTY_PK_END:
	    return getPkEnd();
	case PROPERTY_CONCELLO:
	    return getConcello();
	case PROPERTY_CARRETERA:
	    return getCarretera();
	case PROPERTY_VALUE:
	    return getValue();
	default:
	    return null;
	}
    }

    public void setProperty(int index, Object value) {
	switch (index) {
	case PROPERTY_PK_START:
	    setPkStart((Double) value);
	    break;
	case PROPERTY_PK_END:
	    setPkEnd((Double) value);
	    break;
	case PROPERTY_CONCELLO:
	    setConcello((String) value);
	    break;
	case PROPERTY_CARRETERA:
	    setCarretera((String) value);
	    break;
	case PROPERTY_VALUE:
	    setValue((String) value);
	    break;
	default:
	    // do nothing
	}
    }

}
