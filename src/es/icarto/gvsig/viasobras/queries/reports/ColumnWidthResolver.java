package es.icarto.gvsig.viasobras.queries.reports;

import com.lowagie.text.pdf.PdfPTable;

public interface ColumnWidthResolver {

    public float[] getColumnsWidth(PdfPTable table, int columnCount);

}
