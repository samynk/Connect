package dae.fxcreator.io.templates;

import java.awt.Color;

/**
 * This setting allows you to set a color.
 * @author Koen
 */
public class ColorSetting extends Setting {

    private Color color;
    private boolean transparancySet;

    /**
     * Creates a new ColorSetting object.
     * @param name the name for the ColorSetting object.
     * @param label the label for the ColorSetting object.
     */
    public ColorSetting(String name, String label) {
        super(name, label);
        color = new Color(1,1,1);
    }

    /**
     * Returns the color of this ColorSetting object.
     * @return the color object.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of this ColorSetting object.
     * @param color the new color for this setting object.
     */
    public void setColor(Color color) {
        this.setOldValue(this.color);
        this.color = color;
        notifySettingChanged();
    }

    /**
     * Returns the type of the setting object.
     * @return the type of setting object, always "color".
     */
    @Override
    public String getType() {
        return "color";
    }

    /**
     * Returns the string representation of a color.
     * @return the color as as a string.
     */
    @Override
    public String getSettingValue() {
        int length = 3;
        if (transparancySet) {
            length = 4;
        }
        float[] components = new float[4];
        color.getRGBComponents(components);
        String result = "[";
        for (int i = 0; i < length; ++i) {
            result += components[i];
            if (i != length - 1) {
                result += ",";
            }
        }
        result += "]";
        return result;
    }

     /**
     * Returns a value for display purposes.
     * @return the value of this float setting as a nicely formatted string.
     */
    public String getFormattedValue() {

        if (color != null) {
            float[] components = new float[4];
            color.getRGBComponents(components);
            return "(" + components[0] + "," + components[1]+"," +components[2]+",1.0)";
        } else {
            return "(0.0,0.0,0.0,1.0)";
        }
    }

     /**
     * Returns the value of the Setting as a color Object.
     * @return the value of the Setting as a color Object.
     */
    @Override
    public Object getSettingValueAsObject(){
        return color;
    }

    /**
     * Parses the contents of a color variable in the form
     * [r,g,b,a] with the color components in the range  [0,1]
     * @param value the value of the color variable.
     */
    @Override
    public void setSettingValue(String value) {
        if (value.startsWith("[") && value.endsWith("]")) {
            String scomponents = value.substring(1, value.length() - 2);
            String[] fvalues = scomponents.split(",");
            if (fvalues.length == 4) {
                transparancySet = true;
            }
            float[] cs = new float[fvalues.length];
            for (int i = 0; i < fvalues.length; ++i) {
                cs[i] = Float.parseFloat(fvalues[i]);
            }
            if (fvalues.length == 4) {
                this.color = new Color(cs[0], cs[1], cs[2], cs[3]);
            } else if (fvalues.length == 3) {
                this.color = new Color(cs[0], cs[1], cs[2]);
            }
        }
    }

    /**
     * Sets the setting value of this setting as an object.
     * @param the new value for this setting.
     */
    public void setSettingValueAsObject(Object o){
        this.setSettingValue(o.toString());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ColorSetting clone = new ColorSetting(getId(),getLabel());
        clone.setSettingValue(this.getSettingValue());
        clone.setVisualized(this.isVisualized());
        clone.setLabelVisible(this.isLabelVisible());
        return clone;
    }
}
