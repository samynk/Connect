package dae.fxcreator.io.codegen.parser.exec;

import dae.fxcreator.io.codegen.ExportTask;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public abstract class BooleanExpression {
    protected boolean not = false;
    
    public abstract boolean evaluate(ExportTask context);

    public void not(){
        not = true;
    }
}
