package dae.fxcreator.io.codegen.parser.exec;

import dae.fxcreator.io.codegen.ExportTask;

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
