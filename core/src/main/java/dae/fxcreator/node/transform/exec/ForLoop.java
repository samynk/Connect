package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.transform.ExportTask;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ForLoop extends ExecuteBlock {

    private final String varName;
    private final ObjectChain collection;

    public ForLoop(String varName,ObjectChain collection) {
        super(null);
        this.varName = varName;
        this.collection = collection;
    }

    @Override
    public void execute(Object codeObject, ExportTask context) {
        Object value = collection.evaluate(context);
        if (value != null && value instanceof List) {
            List l = (List) value;
            for (Object o : l) {
                context.setVar(varName, o);
                super.execute(codeObject, context);
            }
            context.pop(varName);
        }
    }
}
