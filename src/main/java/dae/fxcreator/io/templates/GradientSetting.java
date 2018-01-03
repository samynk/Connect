/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dae.fxcreator.io.templates;

import dae.fxcreator.node.gui.GraphGradient;
import java.awt.Color;

/**
 * This class stores colors that can be used as a gradient.
 * @author Koen
 */
public class GradientSetting extends Setting {
    private GraphGradient gradient = new GraphGradient();
    /*
     * Creates a new GradientSetting object.
     * @param name the name for the setting.
     * @param label the label for the setting.
     */
    public GradientSetting(String name, String label){
        super(name,label);
    }

    @Override
    public String getType() {
        return "type";
    }

    @Override
    public String getSettingValue() {
        return gradient.toString();
    }

    @Override
    public String getFormattedValue() {
        return gradient.toString();
    }

    @Override
    public Object getSettingValueAsObject() {
        return gradient;
    }

    @Override
    public void setSettingValue(String value) {
        int start = value.indexOf('[');
        int middle = value.indexOf(",");
        int end = value.indexOf("]");
        if ( start > -1 && middle > start){
            Color c1 = Color.decode(value.substring(start+1,middle));
            gradient.setC1(c1);

        }
        if ( middle > -1 && end > middle){
            Color c2 = Color.decode(value.substring(middle+1,end));
            gradient.setC2(c2);
        }
    }

    public static void main(String[] args) {
        String gradient = "[#0A00ff,#1200c1]";
        GradientSetting setting = new GradientSetting("test","test");
        setting.setSettingValue(gradient);
        System.out.println(setting.getFormattedValue());
    }

    public Color getColor1(){
        return this.gradient.getC1();
    }

    public Color getColor2(){
        return this.gradient.getC2();
    }

    public void setColor1(Color selected) {
        gradient.setC1(selected);
        setDefaultValue(false);
        notifySettingChanged();
    }

    public void setColor2(Color selected){
        gradient.setC2(selected);
        setDefaultValue(false);
        notifySettingChanged();
    }

    public GraphGradient getGradient() {
        return this.gradient;
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
        GradientSetting clone = new GradientSetting(getId(),getLabel());
        clone.setSettingValue(this.getSettingValue());
        clone.setVisualized(this.isVisualized());
        clone.setLabelVisible(this.isLabelVisible());
        return clone;
    }
}
