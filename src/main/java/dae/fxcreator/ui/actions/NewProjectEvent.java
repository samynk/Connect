package dae.fxcreator.ui.actions;

import dae.fxcreator.io.FXProjectTemplate;

/**
 * Event object to signal the creation of a new event.
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class NewProjectEvent {
    private final FXProjectTemplate toCreate;
    public NewProjectEvent(FXProjectTemplate toCreate){
        this.toCreate = toCreate;
    }
    
    public FXProjectTemplate getProjectTemplate(){
        return toCreate;
    }
}
