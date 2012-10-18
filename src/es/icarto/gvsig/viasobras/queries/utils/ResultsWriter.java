package es.icarto.gvsig.viasobras.queries.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.table.TableModel;

public class ResultsWriter {

    private static final String SEPARATOR_ROW = "\n";
    private static final String SEPARATOR_FIELD = ";";
    public static int CSV = 0;

    public static void saveToFile(File f, int format, TableModel model)
	    throws FileNotFoundException {
	FileOutputStream fos = null;
	PrintStream ps = null;
	fos = new FileOutputStream(f);
	ps = new PrintStream(fos);
	String csvFile = "";
	csvFile = csvFile + model.getColumnName(0);
	for (int col = 1; col < model.getColumnCount(); col++) {
	    csvFile = csvFile + SEPARATOR_FIELD;
	    csvFile = csvFile + model.getColumnName(col);
	}
	csvFile = csvFile + SEPARATOR_ROW;
	for (int row = 0; row < model.getRowCount(); row++) {
	    csvFile = csvFile + model.getValueAt(row, 0);
	    for (int col = 1; col < model.getColumnCount(); col++) {
		csvFile = csvFile + SEPARATOR_FIELD;
		Object value = model.getValueAt(row, col);
		if(value instanceof String) {
		    value = ((String) value).replaceAll("(\\r|\\n)", "");
		}
		csvFile = csvFile + value;
	    }
	    csvFile = csvFile + SEPARATOR_ROW;
	}
	ps.print(csvFile);
    }

}
