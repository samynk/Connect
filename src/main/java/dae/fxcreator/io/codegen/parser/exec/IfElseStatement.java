package dae.fxcreator.io.codegen.parser.exec;

import dae.fxcreator.io.codegen.ExportTask;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class IfElseStatement implements Executable {

    private final ExecuteBlock ifBlock = new ExecuteBlock(null);
    private final ExecuteBlock elseBlock = new ExecuteBlock(null);

    private final BooleanExpression condition;

    public IfElseStatement(BooleanExpression condition) {
        this.condition = condition;
    }
    
    public ExecuteBlock getIfBlock(){
        return ifBlock;
    }
    
    public ExecuteBlock getElseBlock(){
        return elseBlock;
    }

    @Override
    public void execute(Object codeObject, ExportTask context) {
        boolean c = condition.evaluate(context);
        if (c) {
            ifBlock.execute(codeObject, context);
        } else {
            elseBlock.execute(codeObject, context);
        }
    }
}
