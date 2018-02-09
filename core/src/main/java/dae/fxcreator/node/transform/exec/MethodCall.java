package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.TypedNode;
import dae.fxcreator.node.annotations.Export;
import dae.fxcreator.node.transform.ExportTask;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class MethodCall implements Executable, Expression {

    private final String methodName;
    private final Expression[] parameters;
    private final Object[] values;
    private final boolean root;

    private static final HashMap<String, Method> methodCache = new HashMap<>();

    public MethodCall(String methodName, boolean root, Expression... parameters) {
        this.methodName = methodName;
        this.parameters = parameters;
        this.root = root;
        this.values = new Object[parameters.length];
    }

    private Method findMethod(Class clazz, String methodName) {
        String key = clazz.getName() + "_" + methodName;
        if (methodCache.containsKey(key)) {
            return methodCache.get(key);
        } else {

            Method[] methods = clazz.getMethods();
            Method toInvoke = null;

            for (Method m : methods) {
                if (m.isAnnotationPresent(Export.class)) {
                    Export e = m.getAnnotation(Export.class);
                    if (e.name().equals(methodName)) {
                        toInvoke = m;
                        break;
                    }
                }
                /*
            Class<?>[] paramTypes = m.getParameterTypes();
            if (params == null && paramTypes == null) {
                toInvoke = m;
                break;
            } else if (params == null || paramTypes == null
                    || paramTypes.length != params.length) {
                continue;
            }

            for (int i = 0; i < params.length; ++i) {
                if (!paramTypes[i].isAssignableFrom(params[i].getClass())) {
                    continue methodLoop;
                }
            }
            toInvoke = m;
                 */
            }
            if (toInvoke != null) {
                methodCache.put(key, toInvoke);
            }
            return toInvoke;
        }
    }

    @Override
    public void execute(Object codeObject, ExportTask context) {
        evaluate(context);
    }

    @Override
    public Object evaluate(ExportTask context) {
        Object toCall;
        if (root) {
            toCall = context;
        } else {
            toCall = context.popChainStack();
        }
        Method objectMethod = findMethod(toCall.getClass(), methodName);
        if (objectMethod != null) {
            for (int i = 0; i < parameters.length; ++i) {
                values[i] = parameters[i].evaluate(context);
            }
            try {
                return objectMethod.invoke(toCall, values);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                String result = MessageFormat.format(
                    "Could not execute {0} on object of type {1} with {2} parameters",
                    methodName, toCall.getClass(), parameters.length);
                // Logger.getLogger(MethodCall.class.getName()).log(Level.SEVERE, null, ex);
                Logger.getLogger(MethodCall.class.getName()).log(Level.INFO, result);
                
                
            }
        }
        return null;
    }
}
