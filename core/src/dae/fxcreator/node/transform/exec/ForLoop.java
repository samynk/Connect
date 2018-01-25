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
    private final String object;
    private final String collection;
    
    public ForLoop(String varName,String object, String collection){
        super(null);
        this.varName = varName;
        this.object = object;
        this.collection = "get" + Character.toUpperCase(collection.charAt(0)) + collection.substring(1);
    }

    @Override
    public void execute(Object codeObject, ExportTask context) {
        if ( "node".equals(object)){
            try {
                Method m = codeObject.getClass().getMethod(collection);
                Object value = m.invoke(codeObject);
                if (value != null && value instanceof List) {
                    List l = (List)value;
                    for(Object o: l){
                        context.addVar(varName,o);
                        super.execute(codeObject, context);
                    }
                    context.removeVar(varName);
                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(ForLoop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
