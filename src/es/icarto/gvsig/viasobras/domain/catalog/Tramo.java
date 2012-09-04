package es.icarto.gvsig.viasobras.domain.catalog;

import java.sql.Date;

public class Tramo {

    // will define the order in which will appear in table
    public static final int PROPERTY_CARRETERA = 0;
    public static final int PROPERTY_ORDEN_TRAMO = 1;
    public static final int PROPERTY_CONCELLO = 2;
    public static final int PROPERTY_PK_START = 3;
    public static final int PROPERTY_PK_END = 4;
    public static final int PROPERTY_VALUE = 5;
    public static final int PROPERTY_UPDATING_DATE = 6;
    private static final int NUMBER_OF_PROPERTIES = 7;

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

    private String carretera;
    private String ordenTramo;
    private String concello;
    private double pkStart;
    private double pkEnd;
    private Object value;
    private Class valueClass = Object.class;
    private Date updatingDate;

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

    public String getOrdenTramo() {
	return ordenTramo;
    }

    public void setOrdenTramo(String ordenTramo) {
	this.ordenTramo = ordenTramo;
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

    public void setValueClass(Class valueClass) {
	this.valueClass = valueClass;
    }

    public Date getUpdatingDate() {
	return updatingDate;
    }

    public void setUpdatingDate(Date updatingDate) {
	this.updatingDate = updatingDate;
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
	case PROPERTY_ORDEN_TRAMO:
	    return "Orden tramo";
	case PROPERTY_CARRETERA:
	    return "Carretera";
	case PROPERTY_VALUE:
	    return "Valor";
	case PROPERTY_UPDATING_DATE:
	    return "Fecha";
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
	case PROPERTY_ORDEN_TRAMO:
	    return getOrdenTramo();
	case PROPERTY_CARRETERA:
	    return getCarretera();
	case PROPERTY_VALUE:
	    return getValue();
	case PROPERTY_UPDATING_DATE:
	    return getUpdatingDate();
	default:
	    return null;
	}
    }

    public boolean setProperty(int index, Object value) {
	switch (index) {
	case PROPERTY_PK_START:
	    if (value != null) {
		setPkStart((Double) value);
		return true;
	    }
	    return false;
	case PROPERTY_PK_END:
	    if (value != null) {
		setPkEnd((Double) value);
		return true;
	    }
	    return false;
	case PROPERTY_CONCELLO:
	    setConcello((String) value);
	    return true;
	case PROPERTY_ORDEN_TRAMO:
	    setOrdenTramo((String) value);
	    return true;
	case PROPERTY_CARRETERA:
	    setCarretera((String) value);
	    return true;
	case PROPERTY_VALUE:
	    setValue(value);
	    return true;
	case PROPERTY_UPDATING_DATE:
	    try {
		setUpdatingDate(Date.valueOf((String) value));
		return true;
	    } catch (IllegalArgumentException e) {
		return false;
	    }
	default:
	    // do nothing
	    return false;
	}
    }

    public Class getClass(int index) {
	switch (index) {
	case PROPERTY_PK_START:
	    return Double.class;
	case PROPERTY_PK_END:
	    return Double.class;
	case PROPERTY_CONCELLO:
	    return String.class;
	case PROPERTY_ORDEN_TRAMO:
	    return String.class;
	case PROPERTY_CARRETERA:
	    return String.class;
	case PROPERTY_VALUE:
	    // should be set when retrieving the value from database
	    // see TramosRecordsetAdapter.toList()
	    return valueClass;
	case PROPERTY_UPDATING_DATE:
	    return Date.class;
	default:
	    return Object.class;
	}
    }

    public String toString() {
	String s = "Carretera: " + getCarretera() + " - Orden tramo: "
		+ getOrdenTramo() + " - Concello: " + getConcello()
		+ " - PK inicial " + getPkStart() + " - PK final: "
		+ getPkEnd() + " - Valor: " + getValue()
		+ " - Fecha actualización: " + getUpdatingDate();
	return s;
    }

}
