package dae.fxcreator.io.codegen.parser.exec;

import dae.fxcreator.io.TypedNode;
import dae.fxcreator.io.codegen.ExportTask;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class MethodCall implements Executable{
    private final String object;
    private final String method;
    private final Object[] parameters;
    
    public MethodCall(String object, String method, Object ... parameters){
        this.object = object;
        this.method = method;
        this.parameters = parameters;
    }

    @Override
    public void execute(Object codeObject, ExportTask context) {
        Object o = context.getVar(object);
        if ( "call".equals(method))
        {
            if ( o instanceof TypedNode && parameters[0] instanceof String){
                context.template((TypedNode)o,(String)parameters[0]);
            }
        }
    }

}
