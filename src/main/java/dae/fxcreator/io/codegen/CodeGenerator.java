package dae.fxcreator.io.codegen;

import dae.fxcreator.node.transform.ExportTask;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.ShaderIO;
import dae.fxcreator.node.ShaderType;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 *
 * @author Koen
 */
public class CodeGenerator {

    private ArrayList codeObjects = new ArrayList();

    /**
     * Generates code for the object in the project file.
     * @param codeObject the object to generate shader code for.
     * @param context the ExportTask context for this CodeGenerator object.
     */
    public void generateCode(Object codeObject, ExportTask context) {
    }

    public boolean checkObject(Object o, String property) {
        if (property == null || property.length() == 0) {
            if (codeObjects.contains(o)) {
                return true;
            } else {
                codeObjects.add(o);
                return false;
            }
        }else{
            String method = "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
            try{
                Method propertyMethod = o.getClass().getMethod(method);
                String value = (String)propertyMethod.invoke(o);
                if ( codeObjects.contains(value))
                    return true;
                else{
                    codeObjects.add(value);
                    return false;
                }
            }catch(Exception ex){
                return false;
            }

        }
    }

    /**
     * Gets an input or output object from the current node.
     * @param node the node to get the input or output from.
     * @param portName the name of the input or output.
     * @return the ShaderInput or ShaderOutput object.
     */
    public ShaderIO getIO(IONode node, String portName) {
        return node.getPort(portName);
    }

    public boolean compareType(String type, ShaderType st) {
        return type.equals(st.getType());
    }
}
