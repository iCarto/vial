package es.icarto.gvsig.viasobras.catalog.domain;


public class Catalog {

    private static String carreteraSelected;
    private static String concelloSelected;

    public static Carreteras getCarreteras() {
	return Carreteras.findAll();
    }

    public static Concellos getConcellos() {
	Concellos cs;
	if (carreteraSelected == null) {
	    cs = Concellos.findAll();
	} else {
	    cs = Concellos.findWhereCarretera(carreteraSelected);
	}
	return cs;
    }

    public static TipoPavimento getTramosTipoPavimento() {
	return TipoPavimento.findAll();
    }

    public static String getCarreteraSelected() {
	return carreteraSelected;
    }

    public static void setCarretera(String carretera) {
	carreteraSelected = carretera;
    }

    public static String getConcelloSelected() {
	return concelloSelected;
    }

    public static void setConcello(String concello) {
	concelloSelected = concello;
    }

}
