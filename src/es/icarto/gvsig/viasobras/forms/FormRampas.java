package es.icarto.gvsig.viasobras.forms;

import javax.swing.JPanel;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;

public class FormRampas extends JPanel implements IWindow {

    private FormPanel form;
    private WindowInfo viewInfo = null;

    public FormRampas() {
	form = getFormBody();
	super.add(form);
    }

    public FormPanel getFormBody() {
	if (form == null) {
	    form = new FormPanel("rampas.xml");
	}
	return form;
    }

    public WindowInfo getWindowInfo() {
	if (viewInfo == null) {
	    viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG
		    | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	    viewInfo.setTitle("Vías y Obras: rampas");
	    viewInfo.setWidth(325);
	    viewInfo.setHeight(425);
	}
	return viewInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

}
