package es.icarto.gvsig.viasobras.domain.catalog;

import java.sql.Date;

public class Evento {

    // will define the order in which will appear in table
    public static final int PROPERTY_CARRETERA = 0;
    public static final int PROPERTY_ORDEN = 1;
    public static final int PROPERTY_CONCELLO = 2;
    public static final int PROPERTY_PK = 3;
    public static final int PROPERTY_VALUE = 4;
    public static final int PROPERTY_DATE = 5;
    private static final int NUMBER_OF_PROPERTIES = 6;

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
    private String orden;
    private String concello;
    private double pk;
    private Object value;
    private Date updatingDate;

    public Evento() {
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

    public double getPk() {
	return pk;
    }

    public void setPk(double pk) {
	this.pk = pk;
    }

    public String getConcello() {
	return concello;
    }

    public void setConcello(String concello) {
	this.concello = concello;
    }

    public String getOrden() {
	return orden;
    }

    public void setOrden(String orden) {
	this.orden = orden;
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
	case PROPERTY_PK:
	    return "PK evento";
	case PROPERTY_CONCELLO:
	    return "Municipio";
	case PROPERTY_ORDEN:
	    return "Orden tramo";
	case PROPERTY_CARRETERA:
	    return "Carretera";
	case PROPERTY_VALUE:
	    return "Valor";
	case PROPERTY_DATE:
	    return "Fecha";
	default:
	    return "None";
	}
    }

    public Object getPropertyValue(int index) {
	switch (index) {
	case PROPERTY_PK:
	    return getPk();
	case PROPERTY_CONCELLO:
	    return getConcello();
	case PROPERTY_ORDEN:
	    return getOrden();
	case PROPERTY_CARRETERA:
	    return getCarretera();
	case PROPERTY_VALUE:
	    return getValue();
	case PROPERTY_DATE:
	    return getUpdatingDate();
	default:
	    return null;
	}
    }

    public void setProperty(int index, Object value) {
	switch (index) {
	case PROPERTY_PK:
	    setPk((Double) value);
	    break;
	case PROPERTY_CONCELLO:
	    setConcello((String) value);
	    break;
	case PROPERTY_ORDEN:
	    setOrden((String) value);
	    break;
	case PROPERTY_CARRETERA:
	    setCarretera((String) value);
	    break;
	case PROPERTY_VALUE:
	    setValue(value);
	    break;
	case PROPERTY_DATE:
	    setUpdatingDate((Date) value);
	    break;
	default:
	    // do nothing
	}
    }

    public Class getClass(int index) {
	switch (index) {
	case PROPERTY_PK:
	    return Double.class;
	case PROPERTY_CONCELLO:
	    return String.class;
	case PROPERTY_ORDEN:
	    return String.class;
	case PROPERTY_CARRETERA:
	    return String.class;
	case PROPERTY_VALUE:
	    return Double.class;
	case PROPERTY_DATE:
	    return Date.class;
	default:
	    return Object.class;
	}
    }

    public String toString() {
	String s = "Carretera: " + getCarretera()
		+ " - Orden tramo: " + getOrden()
		+ " - Concello: " + getConcello()
		+ " - PK inicial " + getPk()
		+ " - Valor: " + getValue()
		+ " - Fecha actualización: " + getUpdatingDate();
	return s;
    }

}
