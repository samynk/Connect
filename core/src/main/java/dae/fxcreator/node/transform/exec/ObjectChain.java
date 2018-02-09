package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.transform.ExportTask;
import java.util.ArrayList;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ObjectChain implements Executable, Expression{
    private final ArrayList<Expression> expressions = new ArrayList<>();
    
    public void addExpression(Expression e){
        expressions.add(e);
    }
    
    @Override
    public Object evaluate(ExportTask context) {
        Object result = null;
        context.pushChainStack(context);
        for ( Expression e : expressions){
            result = e.evaluate(context);
            if (result != null){
                context.pushChainStack(result);
            }else{
                break;
            }
        }
        context.popChainStack();
        return result;
    }

    @Override
    public void execute(Object codeObject, ExportTask context) {
        evaluate(context);
    }
}
