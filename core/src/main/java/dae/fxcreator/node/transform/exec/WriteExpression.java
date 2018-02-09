package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.transform.ExportTask;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class WriteExpression implements Executable {

    private final Expression e;

    public WriteExpression(Expression e) {
        this.e = e;
        /*this.object = object;
        this.property = "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1);*/
    }

    @Override
    public void execute(Object codeObject, ExportTask context) {
        Object o = e.evaluate(context);
        if (o != null) {
            context.peekBuffer().append(o.toString());
        }
        /*
        Object o;
        if ("node".equals(object)) {
            o = codeObject;
        } else {
            o = context.getVar(object);
        }
        if (o == null) {
            return;
        }

        try {
            Method m = o.getClass().getMethod(property);
            Object value = m.invoke(o);
            
            if (value != null) {
                context.peekBuffer().append(value.toString());
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(WriteExpression.class.getName()).log(Level.SEVERE, null, ex);
        }
         */
    }
}
