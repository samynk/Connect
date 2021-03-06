package dae.fxcreator.node.transform;

import dae.fxcreator.node.IONode;
import dae.fxcreator.node.IOPort;
import dae.fxcreator.node.IOType;
import dae.fxcreator.node.transform.exec.ExecuteBlock;

/**
 *
 * @author Koen
 */
public class CodeServlet extends ExecuteBlock {

    public static final String DEFAULT_TYPE = "#default";
    private final String type;
    private String defaultBuffer;
    private boolean writeOnce;
    private String writeOnceProperty;

    /**
     * Creates a new CodeServlet without a type.
     *
     * @param defaultBuffer the default buffer this code servlet will write to.
     */
    public CodeServlet(String defaultBuffer) {
        this(defaultBuffer, DEFAULT_TYPE);
    }

    /**
     * Creates a new CodeServlet with the given type.
     *
     * @param type the type of the code servlet.
     * @param defaultBuffer the default buffer this code servlet will write to.
     */
    public CodeServlet(String defaultBuffer, String type) {
        super(defaultBuffer);
        if (type != null) {
            this.type = type;
        } else {
            this.type = DEFAULT_TYPE;
        }
        this.defaultBuffer = defaultBuffer;
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
        super.execute(codeObject, context);
    }

    /**
     * Gets an input or output object from the current node.
     *
     * @param node the node to get the input or output from.
     * @param portName the name of the input or output.
     * @return the ShaderInput or ShaderOutput object.
     */
    public IOPort getIO(IONode node, String portName) {
        return node.getPort(portName);
    }

    public boolean compareType(String type, IOType st) {
        return type.equals(st.getType());
    }
}
