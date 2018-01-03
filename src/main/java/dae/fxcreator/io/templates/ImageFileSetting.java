/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.io.templates;

import java.io.File;

/**
 *
 * @author Koen
 */
public class ImageFileSetting extends Setting {

    private File file;

    /**
     * Creates a new ImageFileSetting object.
     * @param name the name for the file setting.
     * @param label the label for the file setting.
     */
    public ImageFileSetting(String name, String label) {
        super(name, label);
    }

    /**
     * Returns the type of setting.
     * @return the type of setting.
     */
    @Override
    public String getType() {
        return "imagefile";
    }

    /**
     * Returns the value for the setting.
     * @return the value for the setting.
     */
    @Override
    public String getSettingValue() {
        return file.getPath();
    }

    /**
     * Returns a value for display purposes.
     * @return the value of this float setting as a nicely formatted string.
     */
    public String getFormattedValue() {
        return file.getPath();
    }

     /**
     * Returns the value of the Setting as a color Object.
     * @return the value of the Setting as a color Object.
     */
    @Override
    public Object getSettingValueAsObject(){
        return file;
    }

    /**
     * Sets the setting value of this setting as an object.
     * @param the new value for this setting.
     */
    public void setSettingValueAsObject(Object o){
        this.setSettingValue(o.toString());
    }

    /**
     * Sets the value for the setting.
     * @param value the value for the setting.
     */
    @Override
    public void setSettingValue(String value) {
        setOldValue(this.file);
        this.file = new File(value);
        notifySettingChanged();
    }
}
