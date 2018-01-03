package dae.fxcreator.io.codegen;

import dae.fxcreator.node.ShaderType;
import dae.fxcreator.node.graph.math.MathFormula;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 *
 * @author Koen
 */
public class CodeTemplateLibrary {

    /**
     * The id for the CodeTemplateLibrary.
     */
    private String id;
    /**
     * Maps a template id to a code generation template.
     */
    private HashMap<String, CodeTemplate> templateMap = new HashMap<String, CodeTemplate>();
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
    private CodeTemplateLibrary baseLibrary;
    /**
     * Create the matcher here to avoid recompiling the same matcher again and again.
     */
    Pattern insertPattern = java.util.regex.Pattern.compile("<\\%([=@])(.*?)\\%>",Pattern.MULTILINE|Pattern.DOTALL);

    private JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    /**
     * Find property patterns.
     */
    Pattern propertyPattern = java.util.regex.Pattern.compile("\\b(\\w*)\\.(\\w*)\\b");
    /**
     * Find setting patterns.
     */
    Pattern settingPattern;
    /**
     * Find input/output patterns
     */
    Pattern ioPattern;
    /**
     * Find if patterns.
     */
     Pattern ifPattern;

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
    private MathFormulaCodeGenerator mathFormulaCodeGenerator = new MathFormulaCodeGenerator();

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
    public CodeTemplateLibrary() {
        addTemplateClassNameMapping("pass", "dae.fxcreator.io.Pass");
        addTemplateClassNameMapping("technique", "dae.fxcreator.io.Technique");
        addTemplateClassNameMapping("project", "dae.fxcreator.io.FXProject");
        addTemplateClassNameMapping("shaderstage", "dae.fxcreator.io.ShaderStage");
        addTemplateClassNameMapping("node","dae.fxcreator.node.ShaderNode");
        addTemplateClassNameMapping("method","dae.fxcreator.node.NodeContainer");
        addTemplateClassNameMapping("struct","dae.fxcreator.node.ShaderStruct");

        String pattern = Pattern.quote("node[\"")+"(.*)"+Pattern.quote("\"]");
        settingPattern = Pattern.compile(pattern);

        String sIOPattern = Pattern.quote("$")+"(.*)"+Pattern.quote(".") + "(.*)\\b";
        ioPattern = Pattern.compile(sIOPattern);

        //String sIfPattern =  "<if((?:\\s+\\w+=\"[^\"]+?\"\\s?)*)>(.*?)</if>";
        String sIfPattern =  "<if((?:\\s+\\w+=\"[^\"]+?\"\\s?)*)>";
        ifPattern = Pattern.compile(sIfPattern,Pattern.MULTILINE|Pattern.DOTALL);
    }

    public static void main(String[] args) {
        String sIfPattern = Pattern.quote("<if")+"\\b"+Pattern.quote("condition=\"")+"(.*)"+Pattern.quote("\">")+"(.*?)"+Pattern.quote("</if>");
        Pattern pattern = Pattern.compile(sIfPattern,Pattern.MULTILINE);

        String sIfPattern2 = "<if((?:\\s+\\w+=\"[^\"]+?\"\\s?)*)>(.*?)</if>";
        Pattern pattern2 = Pattern.compile(sIfPattern2,Pattern.MULTILINE|Pattern.DOTALL);

        System.out.println(pattern.pattern());

        String test = "<if setting=\"Node.TransformType\" condition=\"test\" equals=\"TransformVector\" >\n";
        test+="another test\n";
        test+="</if>";
        System.out.println(test);

        Pattern attributePattern = Pattern.compile("(\\bcondition=\"[^\"]+?\"\\s?) | (\\bsetting=\"[^\"]+?\"\\s?) | (\\b?equals=\"[^\"]+?\"\\s?)");

        Matcher m = pattern2.matcher(test);
        while (m.find()){
            String attributes = m.group(1);
            Matcher am = attributePattern.matcher(attributes);
            while(am.find()){
                for (int i = 1 ; i<= am.groupCount();++i){
                    System.out.println(i+ am.group(i));
                }
            }
        }
        test ="conditon=\"test\"";
        int conditionIndex = test.indexOf("condition");
                int startQuote = test.indexOf("=\"",conditionIndex);
                int endQuote = test.indexOf("\"",startQuote+2);
                String value = test.substring(startQuote+2,endQuote);
                System.out.println(value);

    }

    private void addTemplateClassNameMapping(String templateName, String className){
        templateToClassNameMap.put(templateName,className);
        classNameToTemplateMap.put(className,templateName);
    }

  

    public CodeTemplate getTemplateForClassName(String className){
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
     * Compiles the code templates in the library.
     * @param sourceDir the location to store the java source files.
     * @param outputDir the location to compile the java files to.
     */
    public void compile(File sourceDir, File outputDir) {
        Collection<String> classesToImport = this.templateToClassNameMap.values();
        for (CodeTemplate template : templateMap.values()) {         
            String packageName = id ;
            File baseDir = new File(sourceDir, id );
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            String className = getClassName(template.getId());
            long lastmodified = file.lastModified();
            template.compile( lastmodified, baseDir,packageName, className,classesToImport, insertPattern, propertyPattern,settingPattern,ioPattern,ifPattern,compiler);
        }
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
    public void addTemplate(CodeTemplate template) {
        if ( baseLibrary != null ){
            System.out.println("Trying to find template : "+ template.getId());
            CodeTemplate baseTemplate = baseLibrary.getTemplate(template.getId());
            
            if ( baseTemplate != null ){
                template.setBaseTemplate( baseTemplate );
            }else{
                System.out.println("Template was null");
            }
        }else{
            System.out.println("base library was null");
        }
        
        templateMap.put(template.getId(), template);

    }

    /**
     * Gets the template from this library.
     * @param id the id for the template.
     * @return the CodeTemplate object if found.
     */
    public CodeTemplate getTemplate(String id) {
        CodeTemplate ct = templateMap.get(id);
        if ( ct == null  && baseLibrary != null)
            return baseLibrary.getTemplate(id);
        else
            return ct;
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
    public void setBaseLibrary(CodeTemplateLibrary baseLibrary) {
        if ( baseLibrary != this){
            this.baseLibrary = baseLibrary;
            for (CodeTemplate template : this.templateMap.values() ){
                CodeTemplate ct = baseLibrary.getTemplate(template.getId());
                template.setBaseTemplate(ct);
            }
        }
    }

    /**
     * Adds a math constant to the math library.
     * @param mc the math constant to add.
     */
    public void addMathConstant(MathConstant mc) {
        this.mathFormulaCodeGenerator.addMathConstant(mc);
    }

    /**
     * Adds a math template to the math library.
     * @param mathTemplate the math template to add.
     */
    public void addMathTemplate(MathTemplate mathTemplate) {
        this.mathFormulaCodeGenerator.addMathTemplate(mathTemplate);
    }
    
    public String generateMathCode(MathFormula math){
        return mathFormulaCodeGenerator.createFormula(math);
    }

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
}