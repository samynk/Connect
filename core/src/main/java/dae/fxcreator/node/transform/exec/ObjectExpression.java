package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.transform.ExportTask;
import dae.fxcreator.node.transform.RuntimeUtil;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ObjectExpression implements Expression {

    private final boolean root;
    private final String id;
    private final String method;

    public ObjectExpression(String id, boolean root) {
        this.id = id;
        this.root = root;
        this.method = "get" + Character.toUpperCase(id.charAt(0)) + id.substring(1);
    }

    @Override
    public Object evaluate(ExportTask context) {
        if (root) {
            return context.getVar(id);
        } else {
            Object top = context.popChainStack();
            return RuntimeUtil.invokeGet(top, method);
        }
    }
}
