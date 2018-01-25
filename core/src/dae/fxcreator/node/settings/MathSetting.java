package dae.fxcreator.node.settings;

import dae.fxcreator.node.graphmath.MathFormula;
import java.awt.Image;


/**
 * A setting that can contain mathematical formulas. This setting
 * can be used to clarify the implemented formula or to create a mathematical
 * node that avoids a proliferation of nodes.
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class MathSetting extends Setting{
    private MathFormula formula;
    private String xml;

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
}
