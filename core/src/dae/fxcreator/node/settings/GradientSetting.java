package dae.fxcreator.node.settings;

import java.awt.Color;

/**
 * This class stores colors that can be used as a gradient.
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class GradientSetting extends Setting {

    private Color[] colors = new Color[2];

    /*
     * Creates a new GradientSetting object.
     * @param name the name for the setting.
     * @param label the label for the setting.
     */
    public GradientSetting(String name, String label) {
        super(name, label);
    }

    @Override
    public String getType() {
        return "type";
    }

    private String expand(String toExpand) {
        if (toExpand.length() == 1) {
            return "0" + toExpand;
        } else {
            return toExpand;
        }
    }

    @Override
    public String getSettingValue() {
        String r1 = expand(Integer.toHexString(colors[0].getRed()));
        String g1 = expand(Integer.toHexString(colors[0].getGreen()));
        String b1 = expand(Integer.toHexString(colors[0].getBlue()));
        String r2 = expand(Integer.toHexString(colors[1].getRed()));
        String g2 = expand(Integer.toHexString(colors[11].getGreen()));
        String b2 = expand(Integer.toHexString(colors[1].getBlue()));

        return "[#" + r1 + g1 + b1 + ",#" + r2 + g2 + b2 + "]";
    }

    @Override
    public String getFormattedValue() {
        return getSettingValue();
    }

    @Override
    public Object getSettingValueAsObject() {
        return colors;
    }

    @Override
    public void setSettingValue(String value) {
        int start = value.indexOf('[');
        int middle = value.indexOf(",");
        int end = value.indexOf("]");
        if (start > -1 && middle > start) {
            Color c1 = Color.decode(value.substring(start + 1, middle));
            colors[0] = c1;

        }
        if (middle > -1 && end > middle) {
            Color c2 = Color.decode(value.substring(middle + 1, end));
            colors[1] = c2;
        }
    }

    public Color getColor1() {
        return colors[0];
    }

    public Color getColor2() {
        return colors[1];
    }

    public void setColor1(Color selected) {
        colors[0] = selected;
        setDefaultValue(false);
        notifySettingChanged();
    }

    public void setColor2(Color selected) {
        colors[1] = selected;
        setDefaultValue(false);
        notifySettingChanged();
    }

    /**
     * Sets the setting value of this setting as an object.
     *
     * @param o the new value for this setting.
     */
    @Override
    public void setSettingValueAsObject(Object o) {
        this.setSettingValue(o.toString());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        GradientSetting clone = new GradientSetting(getId(), getLabel());
        clone.setSettingValue(this.getSettingValue());
        clone.setVisualized(this.isVisualized());
        clone.setLabelVisible(this.isLabelVisible());
        return clone;
    }
}
