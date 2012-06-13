package es.icarto.gvsig.viasobras.catalog.domain;

public class Tramo {

    // will define the order in which will appear in table
    private static final int PK_START = 0;
    private static final int PK_END = 1;
    private static final int CONCELLO = 2;
    private static final int CARRETERA = 3;
    private static final int VALUE = 4;

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

    // indicates the position in RecordSet
    private int index = 0;

    private int id;// gid field
    private double pkStart;
    private double pkEnd;
    private String concello;
    private String carretera;
    private String value;

    public void setIndex(int index) {
	this.index = index;
    }

    public int getIndex() {
	return index;
    }

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

    public void setProperty(int index, Object value) {
	switch (index) {
	case PK_START:
	    setPkStart((Double) value);
	    break;
	case PK_END:
	    setPkEnd((Double) value);
	    break;
	case CONCELLO:
	    setConcello((String) value);
	    break;
	case CARRETERA:
	    setCarretera((String) value);
	    break;
	case VALUE:
	    setValue((String) value);
	    break;
	default:
	    // do nothing
	}
    }

}
