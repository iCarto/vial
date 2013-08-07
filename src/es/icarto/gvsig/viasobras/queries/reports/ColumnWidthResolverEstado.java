package es.icarto.gvsig.viasobras.queries.reports;

import com.lowagie.text.pdf.PdfPTable;


public class ColumnWidthResolverEstado implements ColumnWidthResolver {


    public float[] getColumnsWidth(PdfPTable table, int columnCount) {
	float[] columnsWidth = new float[columnCount];
	float widthTotal        = table.getTotalWidth();

	float widthCM           = widthTotal * 0.03f;
	float widthMunicipio    = widthTotal * 0.08f;
	float widthCodigoLUP    = widthTotal * 0.08f;
	float widthTramo        = widthTotal * 0.04f;
	float widthDenominacion = widthTotal * 0.24f;
	// float widthObservaciones = widthTotal * 0.1f;

	float widthAvailable = widthTotal - widthCM - widthMunicipio
		- widthCodigoLUP - widthTramo - widthDenominacion;
	float widthAverage = widthAvailable / (columnCount - 5);

	for (int i = 0; i < columnCount; i++) {
	    if (i == 0) {
		columnsWidth[i] = widthCM;
	    } else if (i == 1) {
		columnsWidth[i] = widthMunicipio;
	    } else if (i == 2) {
		columnsWidth[i] = widthCodigoLUP;
	    } else if (i == 3) {
		columnsWidth[i] = widthTramo;
	    } else if (i == 4) {
		columnsWidth[i] = widthDenominacion;
	    } else {
		columnsWidth[i] = widthAverage;
	    }
	}

	return columnsWidth;
    }

}
