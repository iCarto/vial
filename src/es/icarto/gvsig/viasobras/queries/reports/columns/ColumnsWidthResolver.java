package es.icarto.gvsig.viasobras.queries.reports.columns;

import com.lowagie.text.pdf.PdfPTable;

public interface ColumnsWidthResolver {

    public float[] getColumnsWidth(PdfPTable table, int columnCount);

}
