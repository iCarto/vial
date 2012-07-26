package es.icarto.gvsig.viasobras.catalog.domain;

public class Tramo {

    // will define the order in which will appear in table
    public static final int PROPERTY_CARRETERA = 0;
    public static final int PROPERTY_CONCELLO = 1;
    public static final int PROPERTY_PK_START = 2;
    public static final int PROPERTY_PK_END = 3;
    public static final int PROPERTY_VALUE = 4;
    private static final int NUMBER_OF_PROPERTIES = 5;

    // Indicates how this tramo will be processed when saving data into DB
    public static final int STATUS_ORIGINAL = 0; // do nothing
    public static final int STATUS_INSERT = 1; // INSERT SQL query
    public static final int STATUS_UPDATE = 2; // UPDATE SQL query
    public static final int STATUS_DELETE = 3; // DELETE SQL query

    private int status = STATUS_ORIGINAL;

    public static final String NO_GID = "-1";
    // we needed to be a string to create quickly virtual tramos (those which
    // are not stored in the source yet). As we don't know the next ID in
    // source, we just create a random one. See @Tramos.addTramo()
    private String id = NO_GID;

    public static final int NO_POSITION = -1;
    private int position = NO_POSITION;

    private double pkStart;
    private double pkEnd;
    private String concello;
    private String carretera;
    private Object value;

    public Tramo() {
	this.id = NO_GID;
	this.position = NO_POSITION;
    }

    public int getStatus() {
	return this.status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    /**
     * Set the position in the source
     * 
     * @param position
     */
    public void setPosition(int position) {
	this.position = position;
    }

    /**
     * 
     * @return the position in the source, NO_POSITION if the tramo is not yet
     *         in the source (ie: is a brand new tramo)
     */
    public int getPosition() {
	return this.position;
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

    public Object getValue() {
	return value;
    }

    public void setValue(Object value) {
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
	    return "Municipio";
	case PROPERTY_CARRETERA:
	    return "Carretera";
	case PROPERTY_VALUE:
	    return "CaracterÝstica";
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
	    setPkStart(getDouble(value));
	    break;
	case PROPERTY_PK_END:
	    setPkEnd(getDouble(value));
	    break;
	case PROPERTY_CONCELLO:
	    setConcello((String) value);
	    break;
	case PROPERTY_CARRETERA:
	    setCarretera((String) value);
	    break;
	case PROPERTY_VALUE:
	    setValue(value);
	    break;
	default:
	    // do nothing
	}
    }

    private double getDouble(Object value) {
	double d;
	try {
	    d = Double.parseDouble((String) value);
	    return d;
	} catch (NumberFormatException e) {
	    return Catalog.PK_NONE;
	}
    }

    public String toString() {
	String s = "Carretera: " + getCarretera() + " - Concello: "
		+ getConcello() + " - PK inicial " + getPkStart()
		+ " - PK final: " + getPkEnd() + " - Valor: " + getValue();
	return s;
    }

}
