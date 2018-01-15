package dae.fxcreator.io.codegen.parser;

import dae.fxcreator.io.codegen.*;
import dae.fxcreator.io.TypedNode;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class TemplateClass {

    /**
     * The id for the code template.
     */
    private String id;
    /**
     * The hash map that stores the subtemplates for the code definitions.
     */
    private final HashMap<String, TemplateClassSubtypes> subTypes = new HashMap<>();
    /**
     * The base template for this Code Template object.
     */
    private TemplateClass baseTemplate;
   
    /**
     * If set, this buffer will be used for the output.
     */
    private String defaultBuffer;

    /**
     * Creates an empty CodeTemplate object.
     */
    public TemplateClass() {
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
     * Adds code to the template subtype map.
     *
     * @param codeName the name for the code
     * @param type the subtype for the code.
     * @param bufferName the name for the default buffer , null if the default
     * buffer doesn't need to change.
     * @param writeOnce if true, objects can only use the code servlet once,
     * useful for definition elements.
     * @param writeOnceProperty if set, the code servlet will check this property
     * to see if the code should be written once or not.
     * @return The created CodeServlet object.
     */
    public CodeServlet addCode(String codeName, String type, String bufferName, boolean writeOnce, String writeOnceProperty){
        TemplateClassSubtypes tsm = this.subTypes.get(codeName);
        if (tsm == null) {
            tsm = new TemplateClassSubtypes(codeName);
            subTypes.put(codeName, tsm);
        }
        CodeServlet cs = new CodeServlet(type);
        cs.setDefaultBuffer(bufferName);
        cs.setWriteOnce(writeOnce);
        cs.setWriteOnceProperty(writeOnceProperty);
        
        tsm.addCodeGeneratorDefinition(cs);
        return cs;
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
     */
    public void generateCode(TypedNode nodeObject, String method, String type, ExportTask context) {
        TemplateClassSubtypes tsm = this.subTypes.get(method);
        if (tsm != null) {
            CodeServlet cs;
            if (nodeObject.getType() != null) {
                cs = tsm.getCodeGeneratorDefinition(type);
                if (cs == null) {
                    cs = tsm.getDefault();
                }
            } else {
                cs = tsm.getDefault();
            }
            if (cs != null) {
                cs.generateCode(nodeObject, context);
            } else {
                if (baseTemplate != null) {
                    baseTemplate.generateCode(nodeObject, method, context);
                } else {
                    Logger.getLogger("Connect.CodeServlet").log(
                            Level.SEVERE,
                            "CodeGenerator for {0} with type {1}'}' not found !", 
                            new Object[]{nodeObject, type});
                }
            }
        } else {
            if (baseTemplate != null) {
                baseTemplate.generateCode(nodeObject, method, context);
            } else {
                Logger.getLogger("Connect.CodeServlet").log(
                        Level.SEVERE, 
                        "CodeGenerator for {0} with type {1}'}' not found !", 
                        new Object[]{nodeObject, type});
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
    public void setBaseTemplate(TemplateClass baseTemplate) {
        if (baseTemplate != this.baseTemplate) {
            this.baseTemplate = baseTemplate;
        }
    }

}
