package es.icarto.gvsig.viasobras.queries.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.table.TableModel;

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

    private Font cellBoldStyle = FontFactory.getFont("arial", 6, Font.BOLD);
    private Font bodyBoldStyle = FontFactory.getFont("arial", 8, Font.BOLD);

    private static final String SEPARATOR_ROW = "\n";
    private static final String SEPARATOR_FIELD = ";";

    private Locale loc = new Locale("es");

    private String[] tableHeader;
    private boolean startNewReport;
    private ColumnWidthResolver columnsWidthResolver;

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

    protected void writeFilters(Document document, String[] filters) {
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

    protected String writeFiltersInHeader(String[] filters) {
	return "\n" + "Carretera: " + filters[0] + " Concello: " + filters[1];
    }

    protected void writeTitleAndSubtitle(Document document, String title,
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

    protected String getDateFormated() {
	Calendar calendar = Calendar.getInstance();
	DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, loc);
	Date d = calendar.getTime();
	String date = df.format(d);
	return date;
    }

    protected void writeDate(Document document) {
	Paragraph dateP = new Paragraph(getDateFormated(), bodyBoldStyle);
	dateP.setAlignment(Paragraph.ALIGN_CENTER);
	try {
	    document.add(dateP);
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }

    protected void writeNumberOfRegisters(Document document, int numRegisters) {
	Paragraph numRegistersP = new Paragraph("Número de registros: "
		+ numRegisters, bodyBoldStyle);
	try {
	    document.add(numRegistersP);
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }

    protected float[] getColumnsWidth(PdfPTable table, int columnCount) {
	if (columnsWidthResolver != null) {
	    return columnsWidthResolver.getColumnsWidth(table, columnCount);
	} else {
	    // default algorithm will set all width equals
	    float[] columnsWidth = new float[columnCount];
	    for (int i = 0; i < columnCount; i++) {
		columnsWidth[i] = table.getTotalWidth() / columnCount;
	    }
	    return columnsWidth;
	}
    }

    protected void writeRtfReportContent(Document document,
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

    protected void writePdfReportContent(PdfWriter writer, Document document,
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
		Phrase footerPhrase = new Phrase("Página: ", bodyBoldStyle);

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

    public void toRTF(File f, ArrayList<TableModelResults> resultMap,
	    String[] filters, ColumnWidthResolver columnsWidthResolver)
		    throws FileNotFoundException {

	Document document = new Document(PageSize.A4.rotate());
	this.columnsWidthResolver = columnsWidthResolver;
	String fileName = f.getAbsolutePath();
	RtfWriter2 writer = RtfWriter2.getInstance(document,
		new FileOutputStream(fileName));
	document.open();
	RtfDocumentSettings settings = writer.getDocumentSettings();
	settings.setOutputTableRowDefinitionAfter(true);
	writeRtfReportContent(document, resultMap, filters);
	document.close();
    }

    public void toPDF(File f, ArrayList<TableModelResults> resultMap,
	    String[] filters, ColumnWidthResolver columns)
		    throws FileNotFoundException {
	String fileName = f.getAbsolutePath();
	Document document = new Document(PageSize.A4.rotate());
	this.columnsWidthResolver = columns;
	try {
	    PdfWriter writer = PdfWriter.getInstance(document,
		    new FileOutputStream(fileName));
	    writer.setPageEvent(new MyPageEvent(writer, document, resultMap));
	    document.open();
	    writePdfReportContent(writer, document, resultMap, filters);
	    document.close();
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }

    public void toCSV(File f, TableModel model) throws FileNotFoundException {
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
		if (value instanceof String) {
		    value = ((String) value).replaceAll("(\\r|\\n)", "");
		}
		csvFile = csvFile + value;
	    }
	    csvFile = csvFile + SEPARATOR_ROW;
	}
	ps.print(csvFile);
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
