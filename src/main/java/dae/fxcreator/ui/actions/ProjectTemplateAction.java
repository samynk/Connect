package dae.fxcreator.ui.actions;

import dae.fxcreator.io.templates.FXProjectTemplate;
import dae.fxcreator.io.FXSingleton;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * 
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ProjectTemplateAction extends AbstractAction{
    private final FXProjectTemplate template;
    
    public ProjectTemplateAction(FXProjectTemplate template){
        super(template.getUILabel());
        putValue(Action.SHORT_DESCRIPTION, template.getDescription());
        this.template = template;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NewProjectEvent npe = new NewProjectEvent(template);
        FXSingleton.getSingleton().postEvent(npe);
    }
}
