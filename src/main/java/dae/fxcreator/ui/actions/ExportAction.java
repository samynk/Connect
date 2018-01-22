package dae.fxcreator.ui.actions;

import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.io.codegen.parser.TemplateClassLibrary;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ExportAction extends AbstractAction {

    private final TemplateClassLibrary tcl;

    public ExportAction(TemplateClassLibrary tcl) {
        super(tcl.getLabel());
        this.tcl = tcl;
        init();
    }
    
    private void init(){
        putValue(Action.SHORT_DESCRIPTION, tcl.getId());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ExportProjectEvent epe = new ExportProjectEvent(tcl);
        FXSingleton.getSingleton().postEvent(epe);
    }
}
