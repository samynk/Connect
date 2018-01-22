package dae.fxcreator.io.templates;

import dae.fxcreator.node.graph.math.MathFormula;
import java.awt.Image;


/**
 *
 * @author Koen
 */
public class MathSetting extends Setting{
    private MathFormula formula;
    private String xml;
    private Image icon;

    public MathSetting(String id, String label){
        super(id,label);
    }

    @Override
    public String getType() {
        return "math";
    }

    @Override
    public String getSettingValue() {
        return xml;
    }

    @Override
    public String getFormattedValue() {
        return xml;
    }

    @Override
    public Object getSettingValueAsObject() {
        return formula;
    }

    @Override
    public void setSettingValueAsObject(Object o) {
        if ( o instanceof MathFormula ){
            this.formula = (MathFormula)o;
        }
        notifySettingChanged();
    }

    public MathFormula getMathFormula(){
        return formula;
    }

    @Override
    public void setSettingValue(String value) {
        this.xml = value;
        notifySettingChanged();
    }

    public void setIcon(Image icon){
        this.icon = icon;
    }

    public Image getImage(){
        return icon;
    }

}
