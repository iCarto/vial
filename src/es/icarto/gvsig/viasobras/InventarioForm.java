package es.icarto.gvsig.viasobras;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;

public class InventarioForm extends JPanel implements IWindow {

    private FormPanel form;
    protected WindowInfo viewInfo = null;

    public InventarioForm() {
	JScrollPane form = new JScrollPane(getFormBody());
	this.add(form);
    }

    public FormPanel getFormBody() {
	if (form == null) {
	    return new FormPanel("inventarioform.xml");
	}
	return form;
    }

    public WindowInfo getWindowInfo() {
	if (viewInfo == null) {
	    viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG
		    | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	    viewInfo.setTitle("Vias Obras");
	    viewInfo.setWidth(850);
	    viewInfo.setHeight(380);
	}
	return viewInfo;
    }

    public Object getWindowProfile() {
	return null;
    }

}
