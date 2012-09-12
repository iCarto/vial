package es.icarto.gvsig.viasobras.preferences;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.iver.andami.Launcher;
import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;
import com.iver.utiles.XMLEntity;
import com.jeta.forms.components.panel.FormPanel;


public class ViasObrasPreferences extends AbstractPreferencePage implements
ActionListener {

    public static final String PROPERTY_NAME = "ViasObrasFilesDirectory";
    public static final String DEFAULT_DIRECTORY = Launcher.getAppHomeDir();

    private String id;
    private String title;

    private boolean panelInit = false;

    private JTextField filesDirField;
    private JButton filesDirButton;

    public ViasObrasPreferences() {
	id = this.getClass().getName();
	title = "Vías Obras";
    }

    public String getID() {
	return id;
    }

    public String getTitle() {
	return title;
    }

    public JPanel getPanel() {
	if (!panelInit) {
	    FormPanel form = new FormPanel("preferences.xml");
	    form.setFocusTraversalPolicyProvider(true);

	    filesDirField = form.getTextField("filesField");
	    filesDirButton = (JButton) form.getButton("filesButton");
	    filesDirButton.addActionListener(this);

	    this.addComponent(form);

	    panelInit = true;
	}
	return this;
    }

    public void initializeValues() {
	if (!panelInit) {
	    getPanel();
	}

	String filesDir = getFilesDirProperty();
	filesDirField.setText(filesDir);
    }

    private String getFilesDirProperty() {
	String filesDir;
	PluginServices ps = PluginServices.getPluginServices(this);
	XMLEntity xml = ps.getPersistentXML();
	if (xml.contains(PROPERTY_NAME)) {
	    filesDir = xml.getStringProperty(PROPERTY_NAME);
	} else {
	    filesDir = DEFAULT_DIRECTORY;
	}
	return filesDir;
    }

    public void initializeDefaults() {
	filesDirField.setText(DEFAULT_DIRECTORY);
    }

    public ImageIcon getIcon() {
	return null;
    }

    public boolean isValueChanged() {
	return super.hasChanged();
    }

    @Override
    public void storeValues() throws StoreException {
	String baseDirectory = filesDirField.getText();
	PluginServices ps = PluginServices.getPluginServices(this);
	XMLEntity xml = ps.getPersistentXML();
	File f = new File(baseDirectory);
	if (f.exists() && f.isDirectory() && f.canRead()) {
	    xml.putProperty(PROPERTY_NAME, baseDirectory);
	} else {
	    String message = String.format("%s no es un directorio válido",
		    baseDirectory);
	    throw new StoreException(message);
	}
    }

    @Override
    public void setChangesApplied() {
	super.setChanged(false);
    }

    public void actionPerformed(ActionEvent event) {
	if(event.getSource() == filesDirButton) {
	    File currentDirectory = new File(filesDirField.getText());
	    JFileChooser chooser;
	    if (!(currentDirectory.exists() && currentDirectory.isDirectory() && currentDirectory
		    .canRead())) {
		currentDirectory = new File(DEFAULT_DIRECTORY);
	    }
	    chooser = new JFileChooser(currentDirectory);

	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    int returnVal = chooser.showOpenDialog(filesDirField);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		filesDirField.setText(chooser.getSelectedFile()
			.getAbsolutePath());
	    }
	}
    }

}
