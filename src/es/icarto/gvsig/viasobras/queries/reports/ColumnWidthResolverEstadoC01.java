package es.icarto.gvsig.viasobras.queries.reports;

import com.lowagie.text.pdf.PdfPTable;


public class ColumnWidthResolverEstadoC01 implements ColumnWidthResolver {


    public float[] getColumnsWidth(PdfPTable table, int columnCount) {
	float[] columnsWidth     = new float[columnCount];
	float widthTotal         = table.getTotalWidth();

	float widthCM            = widthTotal * 0.03f;
	float widthMunicipio     = widthTotal * 0.08f;
	float widthCodigoLUP     = widthTotal * 0.08f;
	float widthTramo         = widthTotal * 0.04f;
	float widthDenominacion  = widthTotal * 0.24f;
	float widthObservaciones = widthTotal * 0.16f;

	float widthAvailable = widthTotal
		- widthCM
		- widthMunicipio
		- widthCodigoLUP
		- widthTramo
		- widthDenominacion
		- widthObservaciones;
	float widthAverage = widthAvailable / (columnCount - 6);

	columnsWidth[0] = widthCM;
	columnsWidth[1] = widthMunicipio;
	columnsWidth[2] = widthCodigoLUP;
	columnsWidth[3] = widthTramo;
	columnsWidth[4] = widthDenominacion;
	columnsWidth[5] = widthAverage;
	columnsWidth[6] = widthAverage;
	columnsWidth[7] = widthAverage;
	columnsWidth[8] = widthAverage;
	columnsWidth[9] = widthObservaciones;

	return columnsWidth;
    }

}
