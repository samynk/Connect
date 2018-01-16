package dae.fxcreator.io.codegen.parser;

import dae.fxcreator.io.codegen.*;
import dae.fxcreator.io.codegen.parser.exec.Executable;
import dae.fxcreator.io.codegen.parser.exec.ExecuteBlock;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.ShaderIO;
import dae.fxcreator.node.ShaderType;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 *
 * @author Koen
 */
public class CodeServlet extends ExecuteBlock {

    public static final String DEFAULT_TYPE = "#default";
    private final ArrayList codeObjects = new ArrayList();
    private final String type;
    private String defaultBuffer;
    private boolean writeOnce;
    private String writeOnceProperty;

    private final ExecuteBlock block;

    /**
     * Creates a new CodeServlet without a type.
     *
     * @param defaultBuffer the default buffer this code servlet will write to.
     */
    public CodeServlet(String defaultBuffer) {
        this(DEFAULT_TYPE, defaultBuffer);
    }

    /**
     * Creates a new CodeServlet with the given type.
     *
     * @param type the type of the code servlet.
     * @param defaultBuffer the default buffer this code servlet will write to.
     */
    public CodeServlet(String type, String defaultBuffer) {
        super(defaultBuffer);
        if (type != null) {
            this.type = type;
        } else {
            this.type = DEFAULT_TYPE;
        }
        this.defaultBuffer = defaultBuffer;
        block = new ExecuteBlock(defaultBuffer);

    }

    /**
     * Returns the type of the servlet.
     *
     * @return the type of the servlet.
     */
    public String getType() {
        return type;
    }

    /**
     * Checks if this is a default type.
     *
     * @return true if the codeservlet is a default code servlet, false
     * otherwise.
     */
    public boolean isDefault() {
        return type.equals(DEFAULT_TYPE);
    }

    /**
     * @return the defaultBuffer
     */
    public String getDefaultBuffer() {
        return defaultBuffer;
    }

    /**
     * @param defaultBuffer the defaultBuffer to set
     */
    public void setDefaultBuffer(String defaultBuffer) {
        this.defaultBuffer = defaultBuffer;
    }

    /**
     * @return the writeOnce
     */
    public boolean isWriteOnce() {
        return writeOnce;
    }

    /**
     * @param writeOnce the writeOnce to set
     */
    public void setWriteOnce(boolean writeOnce) {
        this.writeOnce = writeOnce;
    }

    /**
     * @return the writeOnceProperty
     */
    public String getWriteOnceProperty() {
        return writeOnceProperty;
    }

    /**
     * @param writeOnceProperty the writeOnceProperty to set
     */
    public void setWriteOnceProperty(String writeOnceProperty) {
        this.writeOnceProperty = writeOnceProperty;
    }

    /**
     * Generates code for the object in the project file.
     *
     * @param codeObject the object to generate shader code for.
     * @param context the ExportTask context for this CodeGenerator object.
     */
    public void generateCode(Object codeObject, ExportTask context) {
        context.addVar("node", codeObject);
        super.execute(codeObject, context);
    }

    public boolean checkObject(Object o, String property) {
        if (property == null || property.length() == 0) {
            if (codeObjects.contains(o)) {
                return true;
            } else {
                codeObjects.add(o);
                return false;
            }
        } else {
            String method = "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
            try {
                Method propertyMethod = o.getClass().getMethod(method);
                String value = (String) propertyMethod.invoke(o);
                if (codeObjects.contains(value)) {
                    return true;
                } else {
                    codeObjects.add(value);
                    return false;
                }
            } catch (Exception ex) {
                return false;
            }

        }
    }

    /**
     * Gets an input or output object from the current node.
     *
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
