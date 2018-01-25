package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.transform.ExportTask;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class BooleanValue extends BooleanExpression{
    private final boolean value;
    
    public BooleanValue(boolean value){
        this.value = value;
    }

    @Override
    public boolean evaluate(ExportTask context) {
        return not?!value:value;
    }
}
