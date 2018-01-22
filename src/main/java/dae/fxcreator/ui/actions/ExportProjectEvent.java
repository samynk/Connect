package dae.fxcreator.ui.actions;

import dae.fxcreator.io.codegen.parser.TemplateClassLibrary;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ExportProjectEvent {

    private TemplateClassLibrary tcl;

    public ExportProjectEvent(TemplateClassLibrary tcl) {
        this.tcl = tcl;
    }

    public TemplateClassLibrary getTemplateClassLibrary() {
        return tcl;
    }
}
