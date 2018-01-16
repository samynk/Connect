package dae.fxcreator.io.codegen.parser.exec;

import dae.fxcreator.io.codegen.ExportTask;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class WriteProperty implements Executable {

    private final String object;
    private final String property;

    public WriteProperty(String object, String property) {
        this.object = object;
        this.property = "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
    }

    @Override
    public void execute(Object codeObject, ExportTask context) {
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
            Logger.getLogger(WriteProperty.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
