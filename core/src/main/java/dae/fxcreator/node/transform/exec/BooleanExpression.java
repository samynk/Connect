package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.transform.ExportTask;


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
