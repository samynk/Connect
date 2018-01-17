package dae.fxcreator.io.codegen.parser.exec;

import dae.fxcreator.io.TypedNode;
import dae.fxcreator.io.codegen.ExportTask;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class MethodCall implements Executable {

    private final String method;
    private final Object[] parameters;

    public MethodCall(String method, Object... parameters) {
        this.method = method;
        this.parameters = parameters;
    }

    @Override
    public void execute(Object codeObject, ExportTask context) {

        if ("call".equals(method)) {
            if (parameters[0] instanceof VarID && parameters[1] instanceof String) {
                Object o = context.getVar(parameters[0].toString());
                if (o instanceof TypedNode) {
                    context.template((TypedNode) o, (String) parameters[1]);
                    // restore the node parameter
                    context.addVar("node", codeObject);
                }
            }
        }else if ("writeBufferToStream".equals(method)){
            if (parameters[0] instanceof String && parameters[1] instanceof String) {
                context.writeBufferToStream(parameters[0].toString(), parameters[1].toString());
            }
        }
    }
}
