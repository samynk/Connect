/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dae.fxcreator.io.templates;

/**
 * This class allows the user to set the code for the custom code node.
 * @author Koen
 */
public class CodeSetting  extends Setting{
    private String code;
    /**
     * Creates a new CodeSetting object.
     * @param name the name for the codesetting object.
     * @param label the label for the codesetting object.
     */
    public CodeSetting(String name, String label){
        super(name,label);
    }
    /**
     * Returns the type of setting
     * @return the type of the setting (code).
     */
    @Override
    public String getType() {
       return "code";
    }

    /**
     *  Returns the value for the code.
     * @return the value for the code.
     */
    @Override
    public String getSettingValue() {
        return code;
    }

     /**
     * Returns the value of this Setting as a formatted value for use in a
     * shader.
     * @return the value of this setting as a formatted value
     */
    public String getFormattedValue(){
        return code;
    }

    /**
     * Returns the value of the Setting as an Object.
     * @return the value of the Setting as an Object.
     */
    @Override
    public Object getSettingValueAsObject(){
        return code;
    }

    /**
     * Sets the value for the code.
     * @param value the value for the code.
     */
    @Override
    public void setSettingValue(String value) {
        setOldValue(this.code);
        this.code = value;
        notifySettingChanged();
    }

    /**
     * Sets the setting value of this setting as an object.
     * @param the new value for this setting.
     */
    public void setSettingValueAsObject(Object o){
        this.setSettingValue(o.toString());
    }
}