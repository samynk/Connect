package dae.fxcreator.node.transform;

import dae.fxcreator.node.ShaderType;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Koen
 */
public class TemplateClassLibrary {

    /**
     * The id for the TemplateClassLibrary.
     */
    private String id;
    /**
     * The description for the TemplateClassLibrary.
     */
    private String description;
    /**
     * Maps a template id to a code generation template.
     */
    private final HashMap<String, TemplateClass> templateMap = new HashMap<>();
    /**
     * The label for this CodeTemplateLibrary.
     */
    private String label;
    /**
     * The base class for this exporter.
     */
    private String base;
    /**
     * The base library for this code template library object.
     */
    private TemplateClassLibrary baseLibrary;
   

    private final HashMap<String, String> templateToClassNameMap =
            new HashMap<>();
    private final HashMap<String, String> classNameToTemplateMap =
            new HashMap<>();
    private final String defaultClassName = "dae.fxcreator.node.IONode";

    private final HashMap<ShaderType, String> shaderTypeMap =
            new HashMap<>();
    
    /**
     * The default export extension.
     */
    private String defaultExportExtension = "fx";
    /**
     * A specific code generator for mathematical nodes.
     */
    // todo define transformer for math formulas.
    // private final MathFormulaCodeGenerator mathFormulaCodeGenerator = new MathFormulaCodeGenerator();

    /**
     * The code formatter for the output of this code template library.
     */
    private CodeFormatter codeFormatter;

    /**
     * The location for the file.
     */
    private File file;
    /**
     * The relative file location.
     */
    private String relFile;

    /**
     * Creates a new CodeTemplateLibrary object.
     */
    public TemplateClassLibrary() {
        initMappings();
    }

    private void initMappings() {
        addTemplateClassNameMapping("pass", "dae.fxcreator.node.project.Pass");
        addTemplateClassNameMapping("technique", "dae.fxcreator.node.project.Technique");
        addTemplateClassNameMapping("project", "dae.fxcreator.node.project.FXProject");
        addTemplateClassNameMapping("shaderstage", "dae.fxcreator.node.project.ShaderStage");
        addTemplateClassNameMapping("node","dae.fxcreator.node.IONode");
        addTemplateClassNameMapping("method","dae.fxcreator.node.NodeContainer");
        addTemplateClassNameMapping("struct","dae.fxcreator.node.ShaderStruct");
    }
    
    

    public void addTemplateClassNameMapping(String templateName, String className){
        templateToClassNameMap.put(templateName,className);
        classNameToTemplateMap.put(className,templateName);
    }

  

    public TemplateClass getTemplateForClassName(String className){
        String templateName =  classNameToTemplateMap.get(className);
        return this.getTemplate(templateName);
    }
    
    /**
     * Returns the class name for a given template id.
     * @param id the template id.
     * @return the class name that is coupled with the template id.
     */
    public String getClassName(String id) {
        String className = templateToClassNameMap.get(id);
        return className != null ? className : defaultClassName;
    }

    /**
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Adds a new template to the CodeTemplateLibrary.
     * @param template the template to add.
     */
    public void addTemplate(TemplateClass template) {
        if ( baseLibrary != null ){
            TemplateClass baseTemplate = baseLibrary.getTemplate(template.getId());
            
            if ( baseTemplate != null ){
                template.setBaseTemplate( baseTemplate );
            }else{
                Logger.getLogger("Connect.Paser").log(Level.INFO, "No basetemplate found for {0}", template.getId());
            }
        }else{
            Logger.getLogger("Connect.Paser").log(Level.INFO, "No base library is set");
        }
        
        templateMap.put(template.getId(), template);

    }

    /**
     * Gets the template from this library.
     * @param id the id for the template.
     * @return the CodeTemplate object if found.
     */
    public TemplateClass getTemplate(String id) {
        TemplateClass tc = templateMap.get(id);
        if ( tc == null  && baseLibrary != null)
            return baseLibrary.getTemplate(id);
        else
            return tc;
    }

    /**
     * Returns the label of the
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescription(){
        return description;
    }

    /**
     * Adds a shader type to the list of ShaderType mappings.
     * @param type the general type in ShaderFX.
     * @param concreteType the concrete type for use in the shader. 
     */
    public void addShaderType(ShaderType type , String concreteType )
    {
        this.shaderTypeMap.put(type, concreteType);
    }

    /**
     * Gets a concrete shader type.
     * @return the shader type.
     */
    public String getShaderType(ShaderType type)
    {
        return shaderTypeMap.get(type);
    }

    /**
     * Sets the location this template library is loaded from.
     * @param file the location of the template library.
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Gets the location this template library is loaded from.
     * @return the file location.
     */
    public File getFile(){
        return file;
    }

    /**
     * Sets the formatter that will format the output of this CodeTemplateLibrary.
     * @param cf the CodeFormatter object.
     */
    public void setCodeFormatter(CodeFormatter cf) {
        this.codeFormatter = cf;
    }

    /**
     * Gets the CodeFormatter object for this library.
     * @return the CodeFormatter object.
     */
    public CodeFormatter getCodeFormatter(){
        return codeFormatter;
    }

    /**
     * Checks if a CodeFormatter object is present.
     * @return the CodeFormatter object.
     */
    public boolean hasCodeFormatter() {
        return codeFormatter != null;
    }

    /**
     * @return the relFile
     */
    public String getRelFile() {
        return relFile;
    }

    /**
     * @param relFile the relFile to set
     */
    public void setRelFile(String relFile) {
        this.relFile = relFile;
    }

    /**
     * Returns the base exporter of this exporter.
     * @return the base exporter of this exporter.
     */
    public String getBase(){
        return base;
    }

    /**
     *
     * @param base
     */
    public void setBase(String base) {
        this.base = base;
    }

    /**
     * Sets the base library for this code template library object.
     * @param baseLibrary 
     */
    public void setBaseLibrary(TemplateClassLibrary baseLibrary) {
        if ( baseLibrary != this){
            this.baseLibrary = baseLibrary;
            for (TemplateClass template : this.templateMap.values() ){
                TemplateClass tc = baseLibrary.getTemplate(template.getId());
                template.setBaseTemplate(tc);
            }
        }
    }

    
    /**
     * Adds a math constant to the math library.
     * @param mc the math constant to add.
     */
//    public void addMathConstant(MathConstant mc) {
//        this.mathFormulaCodeGenerator.addMathConstant(mc);
//    }

    /**
     * Adds a math template to the math library.
     * @param mathTemplate the math template to add.
     */
//    public void addMathTemplate(MathTemplate mathTemplate) {
//        this.mathFormulaCodeGenerator.addMathTemplate(mathTemplate);
//    }
    
//    public String generateMathCode(MathFormula math){
//        return mathFormulaCodeGenerator.createFormula(math);
//    }
//    */
    /**
     * Returns the default extension for the export of the code.
     * @return the default extension.
     */
    public String getDefaultExportExtension() {
        return defaultExportExtension;
    }
    
    /**
     * Sets the default export extension for this code template library.
     * @param exportExtension 
     */
    public void setDefaultExportExtension(String exportExtension){
        this.defaultExportExtension = exportExtension;
    }

    public void addMapping(String key, StringBuilder className) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}