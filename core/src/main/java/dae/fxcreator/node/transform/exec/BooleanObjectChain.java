package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.transform.ExportTask;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class BooleanObjectChain extends BooleanExpression {

    private final ObjectChain objectChain;

    public BooleanObjectChain(ObjectChain oc) {
        this.objectChain = oc;
    }

    @Override
    public boolean evaluate(ExportTask context) {
        Object value = objectChain.evaluate(context);
        if (value != null && value instanceof Boolean) {
            return (Boolean)value;
        } else {
            // todo raise runtime error
            return false;
        }
    }
}
