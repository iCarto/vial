package es.icarto.gvsig.viasobras.queries.reports.columns;

import com.lowagie.text.pdf.PdfPTable;


public class ActuacionesColumnsWidthResolver implements ColumnsWidthResolver {

    private final static float MUNICIPIO = 60f;
    private final static float CODIGO_AND_PK_FIELDS = 40f;
    private final static float CODIGO_ACTUACION = 50f;
    private final static float TIPO = 80f;
    private final static float OBSERVACIONES = 120f;

    public float[] getColumnsWidth(PdfPTable table, int columnCount) {
	float[] columnsWidth = new float[columnCount];
	for (int i = 0; i < columnCount; i++) {
	    if (i == 0) {
		columnsWidth[i] = MUNICIPIO;
	    }else if (i == 1 || i == 2 || i == 3) {
		columnsWidth[i] = CODIGO_AND_PK_FIELDS;
	    }else if (i == 4) {
		columnsWidth[i] = CODIGO_ACTUACION;
	    }else if (i == 5) {
		columnsWidth[i] = TIPO;
	    }else if (i == columnCount-1) {
		columnsWidth[i] = OBSERVACIONES;
	    }else {
		columnsWidth[i] = ((table.getTotalWidth()
			-MUNICIPIO-(CODIGO_AND_PK_FIELDS*3)-CODIGO_ACTUACION-TIPO-OBSERVACIONES)
			/(columnCount-7));
	    }
	}
	return columnsWidth;
    }

}
