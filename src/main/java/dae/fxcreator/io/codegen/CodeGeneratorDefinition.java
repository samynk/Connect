package dae.fxcreator.io.codegen;

/**
 * This class contains all the definitions of the code generator :
 * 1) xmlSource the  xml source of the code generator.
 * 2)
 * @author Koen
 */
public class CodeGeneratorDefinition {
    /**
     * The default buffer to use for this
     * code generator.
     */
    private String bufferName;
    /**
     * The xml source for this code generator.
     */
    private String xmlSource;
    /**
     * boolean that indicates that objects can only use the codegenerator once
     */
    private boolean writeOnce;
    /**
     * The property to use in combination with the writeOnce boolean.
     */
    private String writeOnceProperty;
    /**
     * The generated Java source for the code generator.
     */
    private CodeGeneratorClass javaClass;
    /**
     * The compiled Java object for the code generator.
     */
    private CodeGenerator codeGenerator;
    /**
     * The subtype of this code generator object.
     */
    private String type;

    /**
     * Creates a new CodeGeneratorDefinition object.
     * @param type the type of the code generator definition object.
     * @param bufferName the name of the buffer.
     * @param xmlSource the original xml source for the code generator.
     */
    public CodeGeneratorDefinition(String type,String bufferName,boolean writeOnce, String writeOnceProperty, String xmlSource){
        this.type = type;
        this.bufferName = bufferName;
        this.xmlSource = xmlSource;
        this.writeOnce = writeOnce;
        this.writeOnceProperty = writeOnceProperty;
    }

    /**
     * Returns the status of the writeOnce variable.
     * @return true if an object can use the CodeGenerator only once.
     */
    public boolean isWriteOnce(){
        return writeOnce;
    }

    /**
     * Returns the write once property to use. If no property is defined , the object
     * itself will be used.
     * @return the property for the write once check.
     */
    public String getWriteOnceProperty(){
        return writeOnceProperty;
    }
    
    /**
     * @return the bufferName
     */
    public String getBufferName() {
        return bufferName;
    }

    /**
     * @param bufferName the bufferName to set
     */
    public void setBufferName(String bufferName) {
        this.bufferName = bufferName;
    }

    /**
     * @return the xmlSource
     */
    public String getXmlSource() {
        return xmlSource;
    }

    /**
     * @param xmlSource the xmlSource to set
     */
    public void setXmlSource(String xmlSource) {
        this.xmlSource = xmlSource;
    }

    /**
     * @return the javaClass
     */
    public CodeGeneratorClass getJavaClass() {
        return javaClass;
    }

    /**
     * @param javaClass the javaClass to set
     */
    public void setJavaClass(CodeGeneratorClass javaClass) {
        this.javaClass = javaClass;
    }

    /**
     * @return the codeGenerator
     */
    public CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }

    /**
     * @param codeGenerator the codeGenerator to set
     */
    public void setCodeGenerator(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the type as a class name by replacing the dots with underscores.
     * @return the type as a class name.
     */
    public String getTypeAsClassName() {
        return type.replace(".", "_");
    }
}