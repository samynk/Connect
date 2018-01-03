/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.io.templates;

/**
 * This class allows the user to set values for a
 * float, float2, float3 or float4.
 * @author Koen
 */
public class DoubleSetting extends Setting {

    private double[] components;
   

    /**
     *
     * @param name The name of this setting.
     * @param label the label to use in the user interface.
     */
    public DoubleSetting(String name, String label) {
        super(name, label);
    }

    /**
     * Returns the number of components in the setting.
     * @return the number of floats in the array.
     */
    public int getNrOfFloats() {
        return components.length;
    }

    /**
     * Returns the setting of the float value.
     * @return the type of the setting.
     */
    @Override
    public String getType() {
        return "floatvector";
    }

    /**
     * Return the setting of the value.
     * @return the setting of the value.
     */
    @Override
    public String getSettingValue() {
        if (components != null) {
            String result = "[";
            for (int i = 0; i < components.length; ++i) {
                result += Double.toString(components[i]);
                if (i != components.length - 1) {
                    result += ",";
                } else {
                    result += "]";
                }
            }
            return result;
        } else {
            return "[0.0]";
        }
    }

     /**
     * Returns the value of the Setting as a float array Object.
     * @return the value of the Setting as a float array Object.
     */
    @Override
    public Object getSettingValueAsObject(){
        return components;
    }

    /**
     * Returns a value for display purposes.
     * @return the value of this float setting as a nicely formatted string.
     */
    @Override
    public String getFormattedValue() {
        if (components != null) {
            if (components.length > 1) {
                String result = "(";
                for (int i = 0; i < components.length; ++i) {
                    result += Double.toString(components[i]);
                    if (i != components.length - 1) {
                        result += ",";
                    } else {
                        result += ")";
                    }
                }
                return result;
            } else {
                return Double.toString(components[0]);
            }
        } else {
            return "0.0";
        }
    }

    /**
     * Sets the value of the setting.
     * @param value the value of the setting.
     */
    @Override
    public void setSettingValue(String value) {
        if (value.startsWith("[") && value.endsWith("]")) {
            String scomponents = value.substring(1, value.length() - 2);
            String[] fvalues = scomponents.split(",");
            components = new double[fvalues.length];
            for (int i = 0; i < components.length; ++i) {
                components[i] = Float.parseFloat(fvalues[i]);
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        DoubleSetting clone = new DoubleSetting(getId(),getLabel());
        clone.setSettingValue(this.getSettingValue());
        clone.setVisualized(this.isVisualized());
        clone.setLabelVisible(this.isLabelVisible());
        return clone;

    }

    /**
     * Returns the float at the specified index.
     * @param i the index of the float.
     * @return the float at the specified index.
     */
    public double getDouble(int i) {
        return components[i];
    }

    /**
     * Sets the value for one of the components.
     * @param index the component index to set.
     * @param value the value for the float variable.
     */
    public void setDouble(int index, double value) {
        double clone[] = (double[])components.clone();
        this.setOldValue(clone);
        components[index] = value;
        notifySettingChanged();
    }

    /**
     * Sets the setting value of this setting as an object.
     * @param o the new value for this setting.
     */
    @Override
    public void setSettingValueAsObject(Object o){
        this.setSettingValue(o.toString());
    }
}
