package dae.fxcreator.ui.actions;

import dae.fxcreator.util.Key;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public abstract class FXAction extends AbstractAction implements Key{
    private final String id;
    private final String i18nKeyLabel;
    private final String i18nTooltip;
    
    /**
     * Creates a new FXAction object. The id can be used to retrieve the
     * action from the action manager.
     * @param id the id that identifies this action uniquely.
     * @param i18nLabel the internationalization key for the short description.
     * @param i18nTooltip the internationalization key for the tool tip.
     */
    public FXAction(String id, String i18nLabel, String i18nTooltip){
        this.id = id;
        this.i18nKeyLabel = i18nLabel;
        this.i18nTooltip = i18nTooltip;
    }
    
    /**
     * Returns the internationalization key for the short description.
     * @return the internationalization key for the tool tip.
     */
    public String getI18NLabel(){
        return i18nKeyLabel;
    }
    
    /**
     * Returns the internationalization key for the tooltip.
     * @return the internationalization key for the tooltip.
     */
    public String getI18NTooltip(){
        return i18nTooltip;
    }
    
    /**
     * Sets the short description for this key.
     * @param shortDesc 
     */
    public void setLabel(String shortDesc){
        putValue(Action.NAME, shortDesc );
    }
    
    public void setTooltip(String tooltip){
        putValue(Action.SHORT_DESCRIPTION, tooltip);
    }
   
    @Override
    public String getKey() {
        return id;
    }
}
