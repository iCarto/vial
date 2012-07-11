package es.icarto.gvsig.viasobras.info;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;

public class FormCarreteras extends JPanel implements IWindow {

    private FormPanel form;
    protected WindowInfo viewInfo = null;

    public FormCarreteras() {
	form = new FormPanel("form-carreteras.xml");
	JScrollPane scrolledForm = new JScrollPane(form);
	this.add(scrolledForm);
    }

    public WindowInfo getWindowInfo() {
	if (viewInfo == null) {
	    viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG
		    | WindowInfo.PALETTE);
	    viewInfo.setTitle("Vias Obras");
	    viewInfo.setWidth(450);
	    viewInfo.setHeight(300);
	}
	return viewInfo;

    }

    public Object getWindowProfile() {
	return null;
    }

}
