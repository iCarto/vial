package es.icarto.gvsig.viasobras.queries.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.document.RtfDocumentSettings;
import com.lowagie.text.rtf.style.RtfParagraphStyle;
import com.lowagie.text.rtf.table.RtfCell;

public class Report {

    protected static final int RTF = 0;
    protected static final int PDF = 1;

    private Font cellBoldStyle = FontFactory.getFont("arial", 6, Font.BOLD);
    private Font bodyBoldStyle = FontFactory.getFont("arial", 8, Font.BOLD);

    private static final float DENOMINACION_COLUMN_WIDTH = 200f;
    private static final float MUNICIPIOS_COLUMN_WIDTH = 200f;

    private Locale loc = new Locale("es");

    private String[] tableHeader;
    private boolean startNewReport;

    public Report(int reportType, String fileName,
	    ArrayList<TableModelResults> resultMap, String[] filters) {
	if (reportType == RTF) {
	    writeRtfReport(fileName, resultMap, filters);
	}
	if (reportType == PDF) {
	    writePdfReport(fileName, resultMap, filters);
	}
    }

    // private Image getHeaderImage() {
    // Image image = null;
    // try {
    // image = Image
    // .getInstance("gvSIG/extensiones/es.icarto.gvsig.viasobras/images/logo.gif");
    // image.scalePercent((float) 15.00);
    // image.setAbsolutePosition(0, 0);
    // image.setAlignment(Chunk.ALIGN_RIGHT);
    // } catch (BadElementException e) {
    // e.printStackTrace();
    // } catch (MalformedURLException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // return image;
    // }

    private void writeFilters(Document document, String[] filters) {
	try {
	    Paragraph tramoP = new Paragraph("Carretera: " + filters[0],
		    bodyBoldStyle);
	    document.add(tramoP);
	    Paragraph ucP = new Paragraph("Concello: " + filters[1],
		    bodyBoldStyle);
	    document.add(ucP);
	    document.add(Chunk.NEWLINE);
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }

    private String writeFiltersInHeader(String[] filters) {
	return "\n" + "Carretera: " + filters[0] + " Concello: " + filters[1];
    }

    private void writeTitleAndSubtitle(Document document, String title,
	    String subtitle) {

	Paragraph titleP = new Paragraph(title,
		RtfParagraphStyle.STYLE_HEADING_1);
	titleP.setAlignment(Paragraph.ALIGN_CENTER);
	try {
	    document.add(titleP);
	} catch (DocumentException e) {
	    e.printStackTrace();
	}

	Paragraph subtitleP = new Paragraph(subtitle,
		RtfParagraphStyle.STYLE_HEADING_2);
	subtitleP.setAlignment(Paragraph.ALIGN_CENTER);
	try {
	    document.add(subtitleP);
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }

    private String getDateFormated() {
	Calendar calendar = Calendar.getInstance();
	DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, loc);
	Date d = calendar.getTime();
	String date = df.format(d);
	return date;
    }

    private void writeDate(Document document) {
	Paragraph dateP = new Paragraph(getDateFormated(), bodyBoldStyle);
	dateP.setAlignment(Paragraph.ALIGN_CENTER);
	try {
	    document.add(dateP);
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }

    private void writeNumberOfRegisters(Document document, int numRegisters) {
	Paragraph numRegistersP = new Paragraph("N�mero de registros: "
		+ numRegisters, bodyBoldStyle);
	try {
	    document.add(numRegistersP);
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }

    private float[] getColumnsWidth(PdfPTable table, int columnCount) {
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

    private void writeRtfReportContent(Document document,
	    ArrayList<TableModelResults> resultMap, String[] filters) {
	try {
	    // Header
	    // Image image = getHeaderImage();
	    // document.add(image);

	    for (TableModelResults result : resultMap) {

		// Write title,subtitle and date report
		String title = result.getTitle();
		String subtitle = result.getSubtitle();
		writeTitleAndSubtitle(document, title, subtitle);
		writeDate(document);
		document.add(Chunk.NEWLINE);

		// write filters
		writeFilters(document, filters);

		int columnCount = result.getColumnCount();
		Table table = new Table(columnCount);

		// Column names
		for (int i = 0; i < columnCount; i++) {
		    Paragraph column = new Paragraph(result.getColumnName(i),
			    bodyBoldStyle);
		    RtfCell columnCell = new RtfCell(column);
		    columnCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(columnCell);
		}

		// Values
		Paragraph value;
		for (int row = 0; row < result.getRowCount(); row++) {
		    for (int column = 0; column < columnCount; column++) {
			if (result.getValueAt(row, column) != null) {
			    value = new Paragraph(result
				    .getValueAt(row, column).toString(),
				    cellBoldStyle);
			} else {
			    value = new Paragraph("");
			}
			RtfCell valueCell = new RtfCell(value);
			valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(valueCell);
		    }
		}
		document.add(table);
		document.add(Chunk.NEWLINE);
		writeNumberOfRegisters(document, result.getRowCount());
		document.newPage();
		// document.add(image);
	    }
	    // Close file
	    document.close();

	} catch (DocumentException e) {
	    e.printStackTrace();
	}

    }

    private void writePdfReportContent(PdfWriter writer, Document document,
	    ArrayList<TableModelResults> resultMap, String[] filters) {
	try {
	    boolean isFirstPage = true;

	    for (TableModelResults result : resultMap) {
		document.setPageCount(0);
		startNewReport = true;
		if (!isFirstPage) {
		    document.setHeader(null);
		    document.setFooter(null);
		    document.newPage();

		}
		// Write title,subtitle and date report
		String title = result.getTitle();
		String subtitle = result.getSubtitle();
		writeTitleAndSubtitle(document, title, subtitle);

		// Header
		// Image image = getHeaderImage();

		// PdfContentByte cbhead = writer.getDirectContent();
		// PdfTemplate tp = cbhead.createTemplate(image.getWidth(),
		// image.getHeight());
		// tp.addImage(image);
		//
		// cbhead.addTemplate(tp, 520, 775);

		Phrase headPhrase = new Phrase(title + " - "
			+ getDateFormated() + writeFiltersInHeader(filters),
			bodyBoldStyle);
		Phrase footerPhrase = new Phrase("P�gina: ", bodyBoldStyle);

		HeaderFooter header = new HeaderFooter(headPhrase, false);
		HeaderFooter footer = new HeaderFooter(footerPhrase, true);
		footer.setBorder(Rectangle.NO_BORDER);

		document.setHeader(header);
		document.setFooter(footer);

		writeDate(document);
		document.add(Chunk.NEWLINE);

		// write filters
		writeFilters(document, filters);

		int columnCount = result.getColumnCount();
		PdfPTable table = new PdfPTable(columnCount);
		table.setTotalWidth(document.getPageSize().getWidth()
			- document.leftMargin() - document.rightMargin());
		table.setLockedWidth(true);
		if (!title.equalsIgnoreCase("listado de pagos")) {
		    table.setWidths(getColumnsWidth(table, columnCount));
		}

		String[] headerCells = new String[columnCount];

		// Column names
		for (int i = 0; i < columnCount; i++) {
		    Paragraph column = new Paragraph(result.getColumnName(i),
			    bodyBoldStyle);
		    PdfPCell columnCell = new PdfPCell(column);
		    columnCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(columnCell);
		    headerCells[i] = result.getColumnName(i);
		}

		startNewReport = false;
		tableHeader = headerCells;
		NumberFormat nf = NumberFormat.getInstance(loc);
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,
			loc);

		// Values
		Paragraph value;
		String valueFormatted;
		for (int row = 0; row < result.getRowCount(); row++) {
		    for (int column = 0; column < columnCount; column++) {
			if (result.getValueAt(row, column) != null) {
			    if (result.getValueAt(row, column).getClass()
				    .getName()
				    .equalsIgnoreCase("java.lang.Integer")
				    || result
				    .getValueAt(row, column)
				    .getClass()
				    .getName()
				    .equalsIgnoreCase(
					    "java.lang.Double")) {
				valueFormatted = nf.format(result.getValueAt(
					row, column));
				value = new Paragraph(valueFormatted,
					cellBoldStyle);
			    } else if (result.getValueAt(row, column)
				    .getClass().getName()
				    .equalsIgnoreCase("java.sql.Date")) {
				valueFormatted = df.format(result.getValueAt(
					row, column));
				value = new Paragraph(valueFormatted,
					cellBoldStyle);
			    } else {
				value = new Paragraph(result.getValueAt(row,
					column).toString(), cellBoldStyle);
			    }
			} else {
			    value = new Paragraph("");
			}
			PdfPCell valueCell = new PdfPCell(value);
			valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(valueCell);
		    }
		}
		// table.setHorizontalAlignment(Element.ALIGN_LEFT);
		document.add(table);
		writeNumberOfRegisters(document, result.getRowCount());
		isFirstPage = false;
	    }

	    // Close file
	    document.close();

	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }

    public void writeRtfReport(String fileName,
	    ArrayList<TableModelResults> resultMap, String[] filters) {
	Document document = new Document(PageSize.A4.rotate());
	RtfWriter2 writer;
	try {
	    // Open RTF file and prepare it to write on
	    writer = RtfWriter2.getInstance(document, new FileOutputStream(
		    fileName));
	    document.open();
	    RtfDocumentSettings settings = writer.getDocumentSettings();
	    settings.setOutputTableRowDefinitionAfter(true);

	    // Write report into document
	    writeRtfReportContent(document, resultMap, filters);

	    // Close file
	    document.close();

	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
    }

    public void writePdfReport(String fileName,
	    ArrayList<TableModelResults> resultMap, String[] filters) {
	Document document = new Document(PageSize.A4.rotate());
	try {
	    PdfWriter writer = PdfWriter.getInstance(document,
		    new FileOutputStream(fileName));
	    writer.setPageEvent(new MyPageEvent(writer, document, resultMap));
	    document.open();

	    // Write report into document
	    writePdfReportContent(writer, document, resultMap, filters);
	    // Close file
	    document.close();

	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }

    public class MyPageEvent extends PdfPageEventHelper {
	private PdfWriter pdfWriter;
	private Document document;
	private ArrayList<TableModelResults> resultMap;

	public MyPageEvent(PdfWriter pdfWriter, Document document,
		ArrayList<TableModelResults> resultMap) {
	    this.pdfWriter = pdfWriter;
	    this.document = document;
	    this.resultMap = resultMap;
	}

	public void onStartPage(PdfWriter pdfWriter, Document document) {
	    try {
		if (!startNewReport) {
		    document.add(Chunk.NEWLINE);
		    if (tableHeader != null) {
			PdfPTable table = new PdfPTable(tableHeader.length);
			table.setTotalWidth(document.getPageSize().getWidth()
				- document.leftMargin()
				- document.rightMargin());
			table.setLockedWidth(true);
			for (TableModelResults result : resultMap) {
			    if (!result.getTitle().equalsIgnoreCase(
				    "listado de pagos")) {
				table.setWidths(getColumnsWidth(table,
					tableHeader.length));
			    }
			}
			for (int i = 0; i < tableHeader.length; i++) {
			    Paragraph column = new Paragraph(tableHeader[i],
				    bodyBoldStyle);
			    PdfPCell columnCell = new PdfPCell(column);
			    columnCell
			    .setHorizontalAlignment(Element.ALIGN_CENTER);
			    table.addCell(columnCell);
			}
			document.add(table);
		    }
		}
	    } catch (DocumentException e1) {
		e1.printStackTrace();
	    }
	}
    }

}