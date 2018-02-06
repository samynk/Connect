package dae.fxcreator.node.settings;

/**
 * This class allows the user to set values for a
 * int, int2, int3 or int4.
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class IntSetting extends Setting {

    private int[] components;

    /**
     *
     * @param name The name of this setting.
     * @param label the label to use in the user interface.
     */
    public IntSetting(String name, String label) {
        super(name, label);
    }

    /**
     * Returns the number of components in the setting.
     * @return the number of floats in the array.
     */
    public int getNrOfInts() {
        return components.length;
    }

    /**
     * Returns the setting of the float value.
     * @return the type of the setting.
     */
    @Override
    public String getType() {
        return "intvector";
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
                result += Integer.toString(components[i]);
                if (i != components.length - 1) {
                    result += ",";
                } else {
                    result += "]";
                }
            }
            return result;
        } else {
            return "[0]";
        }
    }

     /**
     * Returns the value of the Setting as an int array Object.
     * @return the value of the Setting as an int array Object.
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
                    result += Integer.toString(components[i]);
                    if (i != components.length - 1) {
                        result += ",";
                    } else {
                        result += ")";
                    }
                }
                return result;
            } else {
                return Float.toString(components[0]);
            }
        } else {
            return "0";
        }
    }

    /**
     * Sets the value of the setting.
     * @param value the value of the setting.
     */
    @Override
    public void setSettingValue(String value) {
        if (value.startsWith("[") && value.endsWith("]")) {
            String scomponents = value.substring(1, value.length() - 1);
            String[] fvalues;
            fvalues = scomponents.split(",");
            components = new int[fvalues.length];
            for (int i = 0; i < components.length; ++i) {
                try {
                    components[i] = Integer.parseInt(fvalues[i]);
                } catch (NumberFormatException ex) {
                    components[i] = 0;
                }
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        IntSetting clone = new IntSetting(getId(),getLabel());
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
    public int getInt(int i) {
        return components[i];
    }

    /**
     * Sets the setting value of this setting as an object.
     * @param the new value for this setting.
     */
    @Override
    public void setSettingValueAsObject(Object o){
        this.setSettingValue(o.toString());
    }

    /**
     * Sets the value for one of the componets.
     * @param index the component index to set.
     * @param value the value for the float variable.
     */
    public void setInt(int index, int value) {
        int[] clone = (int[]) components.clone();
        setOldValue(clone);
        components[index] = value;
        notifySettingChanged();
    }
}
