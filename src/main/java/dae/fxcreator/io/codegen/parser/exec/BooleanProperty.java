package dae.fxcreator.io.codegen.parser.exec;

import dae.fxcreator.io.codegen.ExportTask;
import dae.fxcreator.io.codegen.parser.RuntimeUtil;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class BooleanProperty extends BooleanExpression {

    private final String object;
    private final String property;

    public BooleanProperty(String object, String property) {
        this.object = object;
        this.property = RuntimeUtil.convertToBooleanMethod(property);
    }

    @Override
    public boolean evaluate(ExportTask context) {
        Object o = context.getVar(object);
        if (o != null) {
            Object value = RuntimeUtil.invokeGet(o, property);
            if (value instanceof Boolean) {
                boolean result = (Boolean)value;
                return not? !result:result;
            } else {
                // todo raise runtime error.
                return false;
            }
        } else {
            // todo raise runtime error.
            return false;
        }
    }
}
