package dae.fxcreator.node.settings;

import dae.fxcreator.node.graphmath.MathFormula;
import dae.fxcreator.node.graphmath.parser.MathParser;

/**
 * A setting that can contain mathematical formulas. This setting can be used to
 * clarify the implemented formula or to create a mathematical node that avoids
 * a proliferation of nodes.
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class MathSetting extends Setting {

    private MathFormula formula;
    private String code;

    public MathSetting(String id, String label) {
        super(id, label);
    }

    private void parseCode() {
        this.formula = MathParser.parseMathCode(code);
    }

    @Override
    public String getType() {
        return "math";
    }

    @Override
    public String getSettingValue() {
        return code;
    }

    @Override
    public String getFormattedValue() {
        return code;
    }

    @Override
    public Object getSettingValueAsObject() {
        return formula;
    }

    @Override
    public void setSettingValueAsObject(Object o) {
        if (o instanceof MathFormula) {
            this.formula = (MathFormula) o;
        }
        notifySettingChanged();
    }

    public MathFormula getMathFormula() {
        return formula;
    }

    @Override
    public void setSettingValue(String value) {
        this.code = value;
        parseCode();
        notifySettingChanged();
    }
    
    @Override
    public String toString(){
        return code;
    }
}
