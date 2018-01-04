package dae.fxcreator.node.gui;

import java.text.*;
import javax.swing.*;

/**
 *
 * @author samynk
 */
public class FloatEditor extends JFormattedTextField{
    /** Creates a new instance of FloatEditor */
    public FloatEditor() {
       // super(new DecimalFormat("#.0000"));
        setValue(new Double(0f));
    }
    
    public float getFloat(){
        Double value = (Double)getValue();
        return (float)value.floatValue();
    }
    
    public void setFloat(float value){
        this.setValue(new Double(value));
    }
}
