/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.io.templates;

/**
 *
 * @author Koen
 */
public class BooleanSetting extends Setting {

    private boolean[] components;

    /**
     * Creates a new Boolean setting.
     * @param id the id for the boolean setting.
     * @param label the label for the boolean setting.
     */
    public BooleanSetting(String id, String label) {
        super(id, label);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof BooleanSetting) {
            BooleanSetting bs = (BooleanSetting) other;
            return equals(bs.components);
        } else if (other instanceof String) {
            boolean[] toCompare = parseValue(other.toString());
            return equals(toCompare);
        }
        return false;
    }

    public boolean equals(boolean[] cs) {
        if (components.length != cs.length) {
            return false;
        }
        for (int i = 0; i < components.length; ++i) {
            if (components[i] != cs[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the number of components in the setting.
     * @return the number of booleans in the array.
     */
    public int getNrOfBooleans() {
        return components.length;
    }

    /**
     * Returns the setting of the float value.
     * @return the type of the setting.
     */
    @Override
    public String getType() {
        return "booleanvector";
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
                result += Boolean.toString(components[i]);
                if (i != components.length - 1) {
                    result += ",";
                } else {
                    result += "]";
                }
            }
            return result;
        } else {
            return "[false]";
        }
    }

    /**
     * Returns the value of the Setting as a float array Object.
     * @return the value of the Setting as a float array Object.
     */
    @Override
    public Object getSettingValueAsObject() {
        return components;
    }

    /**
     * Returns a value for display purposes.
     * @return the value of this float setting as a nicely formatted string.
     */
    public String getFormattedValue() {
        if (components != null) {
            if (components.length > 1) {
                String result = "(";
                for (int i = 0; i < components.length; ++i) {
                    result += Boolean.toString(components[i]);
                    if (i != components.length - 1) {
                        result += ",";
                    } else {
                        result += ")";
                    }
                }
                return result;
            } else {
                return Boolean.toString(components[0]);
            }
        } else {
            return "false";
        }
    }

    /**
     * Sets the value of the setting.
     * @param value the value of the setting.
     */
    @Override
    public void setSettingValue(String value) {
        components = parseValue(value);
    }

    /**
     * Sets the setting value of this setting as an object.
     * @param the new value for this setting.
     */
    public void setSettingValueAsObject(Object o){
        this.setSettingValue(o.toString());
    }

    private boolean[] parseValue(String value) {
        boolean result[] = null;
        if (value.startsWith("[") && value.endsWith("]")) {
            String scomponents = value.substring(1, value.length() - 1);
            String[] fvalues = scomponents.split(",");
            result = new boolean[fvalues.length];
            for (int i = 0; i < result.length; ++i) {
                result[i] = Boolean.parseBoolean(fvalues[i]);
            }
        }
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        BooleanSetting clone = new BooleanSetting(getId(), getLabel());
        clone.setSettingValue(this.getSettingValue());
        clone.setVisualized(this.isVisualized());
        clone.setLabelVisible(this.isLabelVisible());
        return clone;

    }

    /**
     * Returns the boolean at the specified index.
     * @param i the index of the float.
     * @return the boolean at the specified index.
     */
    public boolean getBoolean(int i) {
        return components[i];
    }

    /**
     * Sets the value for one of the componets.
     * @param index the component index to set.
     * @param value the value for the float variable.
     */
    public void setBoolean(int index, boolean value) {
        boolean clone[] = (boolean[]) components.clone();
        this.setOldValue(clone);
        components[index] = value;
        notifySettingChanged();
    }
}
