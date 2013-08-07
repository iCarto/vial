/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
 * 
 * This file is part of EIEL Validation
 * 
 * EIEL Validation is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 * 
 * EIEL Validation is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with EIEL Validation
 * If not, see <http://www.gnu.org/licenses/>.
 */

package es.icarto.gvsig.viasobras.queries.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import com.iver.andami.messages.NotificationManager;

import es.icarto.gvsig.viasobras.queries.reports.ColumnWidthResolver;
import es.icarto.gvsig.viasobras.queries.reports.Report;
import es.icarto.gvsig.viasobras.queries.reports.ResultsWriter;
import es.icarto.gvsig.viasobras.queries.reports.TableModelResults;

public class PanelQueriesResult extends gvWindow {

    public final static int ESTADO = 0;
    public final static int ACTUACIONES = 1;

    private JEditorPane resultTA;
    private JButton exportB;
    private JComboBox fileTypeCB;
    String[] fileFormats = { "CSV", "HTML", "PDF", "RTF" };
    private ArrayList<TableModelResults> resultsMap;
    private String[] filters;

    private ColumnWidthResolver columnsWidthResolver;

    public PanelQueriesResult() {
	this(null);
    }

    public PanelQueriesResult(String council) {

	super(800, 500, false);
	if (council != null && !council.equals("")) {
	    setTitle("Resultado de la consulta de " + council);
	} else {
	    setTitle("Resultado de la consulta");
	}

	MigLayout layout = new MigLayout("inset 0, align center", "[grow]",
		"[grow][]");

	setLayout(layout);

	resultTA = new JEditorPane();
	resultTA.setEditable(false);
	resultTA.setContentType("text/html");
	JScrollPane scrollPane = new JScrollPane(resultTA);
	JPanel panel = new JPanel();
	exportB = new JButton("Exportar");
	exportB.addActionListener(new ExportToListener());
	panel.add(exportB);
	fileTypeCB = new JComboBox(fileFormats);
	panel.add(fileTypeCB);

	add(scrollPane, "growx, growy, wrap");
	add(panel, "shrink, align right");

    }

    public void setResult(String result) {
	resultTA.setText(result);
    }

    public void setResultMap(ArrayList<TableModelResults> resultMap) {
	this.resultsMap = resultMap;
    }

    public void setFilters(String[] filters) {
	this.filters = filters;
    }

    private final class ExportToListener implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
	    if (fileTypeCB.getSelectedItem().equals("HTML")) {
		DialogSaveFile sfd = new DialogSaveFile("HTML files", "html",
			"htm");
		File f = sfd.showDialog();
		if (f != null) {
		    if (sfd.writeFileToDisk(resultTA.getText(), f)) {
			NotificationManager.showMessageError(
				"error_saving_file", null);
		    }
		}
	    } else if (fileTypeCB.getSelectedItem().equals("RTF")) {
		DialogSaveFile sfd = new DialogSaveFile("RTF files", "rtf");
		File f = sfd.showDialog();
		if (f != null) {
		    String fileName = f.getAbsolutePath();
		    Report r = new Report();
		    r.toRTF(fileName, resultsMap, filters, columnsWidthResolver);
		}
	    } else if (fileTypeCB.getSelectedItem().equals("PDF")) {
		DialogSaveFile sfd = new DialogSaveFile("PDF files", "pdf");
		File f = sfd.showDialog();
		if (f != null) {
		    String fileName = f.getAbsolutePath();
		    Report r = new Report();
		    r.toPDF(fileName, resultsMap, filters, columnsWidthResolver);
		}
	    } else if (fileTypeCB.getSelectedItem().equals("CSV")) {
		for (TableModelResults model : resultsMap) {
		    DialogSaveFile sfd = new DialogSaveFile("CSV files", "csv");
		    File f = sfd.showDialog();
		    if (f != null) {
			try {
			    ResultsWriter.saveToFile(f, ResultsWriter.CSV,
				    model);
			} catch (FileNotFoundException e1) {
			    NotificationManager.showMessageError(
				    "error_saving_file", e1);
			}
		    }
		}
	    }
	}
    }

    public void setColumnsWidthResolver(ColumnWidthResolver r) {
	this.columnsWidthResolver = r;
    }

}// Class
