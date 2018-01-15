package dae.fxcreator.io.codegen;

import dae.fxcreator.io.TypedNode;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Pattern;
import javax.tools.JavaCompiler;

/**
 * Creates a new CodeTemplate object. The template text can contain variables
 * that will be evaluated during the code generation process. Variables that are
 * currently supported are : $id : the id of a given node or shader element.
 * $type : the type of a given node or shader element.
 * $getSetting("group.settingid") : gets a seting for the node.
 *
 * @author Koen
 */
public class CodeTemplate {

    /**
     * The id for the code template.
     */
    private String id;
    /**
     * The hash map that stores the subtemplates for the code definitions.
     */
    private final HashMap<String, TemplateSubtypeMap> subTypes = new HashMap<>();
    /**
     * The base template for this Code Template object.
     */
    private CodeTemplate baseTemplate;
    /**
     * The hashmap that stores the templates.
     */
    //private HashMap<String, String> templateMap = new HashMap<String, String>();
    /**
     * The hashmap that stores the buffernames for each template.
     */
    //private HashMap<String,String> bufferMap = new HashMap<String,String>();
    /**
     * The hashmap with the generated code.
     */
    //private HashMap<String, CodeGeneratorClass> codeMap =
    //        new HashMap<String,CodeGeneratorClass>();
    /**
     * The codegenerators for the template.
     */
    //private HashMap<String, CodeGenerator> codeGeneratorMap =
    //        new HashMap<String, CodeGenerator>();
    /**
     * If set, this buffer will be used for the output.
     */
    private String defaultBuffer;

    /**
     * Creates an empty CodeTemplate object.
     */
    public CodeTemplate() {
    }

    /**
     * Gets the id for this CodeTemplate object.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id for this CodeTemplate object.
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Generates the code for this CodeTemplate object.
     *
     * @param timeStamp the timeStamp of the original codegenerator file.
     * @param baseDir the base directory to store the generated class filed.
     * @param packageName the package name for the class.
     * @param className the name of the class the template is written for.
     * @param insertPattern the pattern that defines java code to insert into
     * the output.
     * @param propertyPattern the pattern that searches for access to
     * properties.
     */
    public void compile(
            long timeStamp,
            File baseDir,
            String packageName,
            String className,
            Collection<String> classesToImport,
            Pattern insertPattern,
            Pattern propertyPattern,
            Pattern settingPattern,
            Pattern ioPattern,
            Pattern ifPattern,
            JavaCompiler compiler) {
        for (TemplateSubtypeMap tsm : subTypes.values()) {
            tsm.compile(timeStamp, baseDir, packageName, className, id, classesToImport, insertPattern, propertyPattern, settingPattern, ioPattern, ifPattern, compiler);
        }
    }

    /**
     * Adds code to the template subtype map.
     *
     * @param codeName the name for the code
     * @param type the subtype for the code.
     * @param bufferName the name for the default buffer , null if the default
     * buffer doesn't need to change.
     * @param writeOnce if true, objects can only use the codegenerator once,
     * useful for definition elements.
     * @param xmlSource the xml source as a string.
     */
    public void addCode(String codeName, String type, String bufferName, boolean writeOnce, String writeOnceProperty, String xmlSource) {
        TemplateSubtypeMap tsm = this.subTypes.get(codeName);
        if (tsm == null) {
            tsm = new TemplateSubtypeMap(codeName);
            subTypes.put(codeName, tsm);
        }
        CodeGeneratorDefinition cgd = new CodeGeneratorDefinition(type, bufferName, writeOnce, writeOnceProperty, xmlSource);
        tsm.addCodeGeneratorDefinition(cgd);
    }

    /**
     * Executes a "method" of this code template.
     *
     * @param method the method to execute.
     * @param nodeObject the object to run the template for.
     * @param context the export task object.
     */
    public void generateCode(TypedNode nodeObject, String method, ExportTask context) {
        generateCode(nodeObject, method, nodeObject.getType(), context);
    }

    /**
     * Executes a "method" of this code template.
     *
     * @param method the method to execute.
     * @param nodeObject the object to run the template for.
     * @param type the type to use for this call.
     * @param CodeTemplateLibrary the context for this library.
     */
    void generateCode(TypedNode nodeObject, String method, String type, ExportTask context) {
        TemplateSubtypeMap tsm = this.subTypes.get(method);
        if (tsm != null) {
            CodeGeneratorDefinition cg = null;
            if (nodeObject.getType() != null) {
                cg = tsm.getCodeGeneratorDefinition(type);
                if (cg == null) {
                    cg = tsm.getDefault();
                }
            } else {
                cg = tsm.getDefault();
            }
            if (cg != null && cg.getCodeGenerator() != null) {
                cg.getCodeGenerator().generateCode(nodeObject, context);
            } else {
                if (baseTemplate != null) {
                    baseTemplate.generateCode(nodeObject, method, context);
                } else {
                    System.out.println("CodeGenerator for " + nodeObject + " not found !");
                    System.out.println("The type was : " + type);
                }
            }
        } else {
            if (baseTemplate != null) {
                System.out.println("Call the base template");
                baseTemplate.generateCode(nodeObject, method, context);
            } else {
                System.out.println(method + " for " + nodeObject + " not found !");
                System.out.println("The type was : " + type);
            }
        }
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
     * Sets the base template for this CodeTemplate.
     *
     * @param baseTemplate the base template.
     */
    public void setBaseTemplate(CodeTemplate baseTemplate) {
        if (baseTemplate != this.baseTemplate) {
            System.out.println("Setting baseTemplate");
            this.baseTemplate = baseTemplate;
        }
    }

}
