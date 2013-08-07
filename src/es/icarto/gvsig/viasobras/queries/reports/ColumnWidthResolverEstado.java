package es.icarto.gvsig.viasobras.queries.reports;

import com.lowagie.text.pdf.PdfPTable;


public class ColumnWidthResolverEstado implements ColumnWidthResolver {

    private static final float DENOMINACION_COLUMN_WIDTH = 200f;
    private static final float MUNICIPIOS_COLUMN_WIDTH = 200f;

    public float[] getColumnsWidth(PdfPTable table, int columnCount) {
	float[] columnsWidth = new float[columnCount];
	for (int i = 0; i < columnCount; i++) {
	    if (i == 3) {
		columnsWidth[i] = DENOMINACION_COLUMN_WIDTH;
	    } else if (i == 8) {
		columnsWidth[i] = MUNICIPIOS_COLUMN_WIDTH;
	    } else {
		columnsWidth[i] = (table.getTotalWidth()
			- DENOMINACION_COLUMN_WIDTH - MUNICIPIOS_COLUMN_WIDTH)
			/ (columnCount - 2);
	    }
	}
	return columnsWidth;
    }

}
