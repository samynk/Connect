package dae.fxcreator.io.codegen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.tools.JavaCompiler;

/**
 * Stores the data about the subtypes of a template.
 * If there is no subtype defined, the template will be stored
 * with "default" as key.
 * @author Koen
 */
public class TemplateSubtypeMap {

    /**
     * The name for the code element.
     */
    private final String codeId;
    private final HashMap<String, CodeGeneratorDefinition> codeDefinitions = new HashMap<>();
    private CodeGeneratorDefinition defaultCGD;

    public TemplateSubtypeMap(String codeId) {
        this.codeId = codeId;
    }

    /**
     * Add a code generator defintion to this TemplateSubtypeMap.
     * @param cgd the CodeGeneratorDefinition object to add.
     */
    public void addCodeGeneratorDefinition(CodeGeneratorDefinition cgd) {
        if (cgd.getType() != null) {
            codeDefinitions.put(cgd.getType(), cgd);
        } else {
            defaultCGD = cgd;
            defaultCGD.setType("default");
        }
    }

    /**
     * Returns the default CodeGeneratorDefinition object.
     * @return the default code generator definition object.
     */
    public CodeGeneratorDefinition getDefault() {
        return defaultCGD;
    }

    /**
     * Returns the CodeGeneratorDefinition object that has the given type as
     * property.
     * @param type the type of the CodeGeneratorDefinition.
     */
    public CodeGeneratorDefinition getCodeGeneratorDefinition(String type) {
        return codeDefinitions.get(type);
    }

    private String getClassName(String id, CodeGeneratorDefinition cgd) {
        return id + "_" + codeId + "_" + cgd.getTypeAsClassName();
    }

    /**
     * Checks if the file to generate is out of date or not.
     * @param timeStamp the timeStamp to compare the class file with.
     * @param baseDir the baseDir for the code generator file.
     * @param cgd the CodeGeneratorDefinition object.
     * @param id the id for the classname.
     * @param packageName the packageName for the generated file.
     * @param className the className for the generated file.
     * @return true if the class file is out of date and should be regenerated.
     */
    private boolean isClassOutOfDate(long timeStamp, File baseDir, CodeGeneratorDefinition cgd, String id, String packageName) {
        String cn = this.getClassName(id, cgd);
        File classFile = new File(baseDir, cn + ".class");
        if (!classFile.exists()) {
            return true;
        }
        long cfTimeStamp = classFile.lastModified();
        return (cfTimeStamp < timeStamp);
    }

    /**
     * Loads the class definition into the CodeGeneratorDefinition object.
     * @param cgd the CodeGeneratorDefinition object.
     * @param packageName the name of the package.
     * @param className the name of the class.
     */
    private void loadClass(CodeGeneratorDefinition cgd, String packageName, String className) {

        try {
            String cn = packageName + "." + className;
            CodeGenerator cg = (CodeGenerator) Class.forName(cn).newInstance();
            cgd.setCodeGenerator(cg);
        } catch (InstantiationException ex) {
            Logger.getLogger(CodeTemplate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CodeTemplate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CodeTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Generates the code for this CodeTemplate object. The class will only be compiled if the
     * code generator definition has a more recent timestamp then the current class file.
     * @param baseDir the base directory to store the generated class file.
     * @param packageName  the package name for the class.
     * @param className the name of the class the template is written for.
     * @param insertPattern the pattern that defines java code to insert into the output.
     * @param propertyPattern the pattern that searches for access to properties.
     */
    public void compile(long timeStamp,
            File baseDir,
            String packageName,
            String className,
            String id,
            Collection<String> classesToImport,
            Pattern insertPattern,
            Pattern propertyPattern,
            Pattern settingPattern,
            Pattern ioPattern,
            Pattern ifPattern,
            JavaCompiler compiler) {
        for (String key : codeDefinitions.keySet()) {
            CodeGeneratorDefinition cgd = codeDefinitions.get(key);
            if (isClassOutOfDate(timeStamp, baseDir, cgd, id, packageName)) {
                System.out.println("Recompiling : " + getClassName(id, cgd));
                compileSubType(cgd, id, packageName, classesToImport, className, insertPattern, propertyPattern, settingPattern, ioPattern, ifPattern, baseDir, compiler);
            } else {
                System.out.println("Loading : " + getClassName(id, cgd));
                loadClass(cgd, packageName, getClassName(id, cgd));
            }
        }
        if (defaultCGD != null) {
            compileSubType(defaultCGD, id, packageName, classesToImport, className, insertPattern, propertyPattern, settingPattern, ioPattern, ifPattern, baseDir, compiler);
        }
    }

    void compileSubType(CodeGeneratorDefinition cgd, String id, String packageName, Collection<String> classesToImport, String className, Pattern insertPattern, Pattern propertyPattern, Pattern settingPattern, Pattern ioPattern, Pattern ifPattern, File baseDir, JavaCompiler compiler) {
        String code = cgd.getXmlSource();
        String cgdClassName = getClassName(id, cgd);
        StringBuffer output = new StringBuffer();
        writePackage(output, packageName);
        output.append("\n");
        for (String toImport : classesToImport) {
            writeImport(output, toImport);
        }
        writeImport(output, "dae.fxcreator.node.ShaderInput");
        writeImport(output, "dae.fxcreator.node.ShaderOutput");
        writeImport(output, "dae.fxcreator.node.ShaderType");
        writeImport(output, "dae.fxcreator.node.ShaderStruct");
        writeImport(output, "dae.fxcreator.node.ShaderField");
        writeImport(output, "dae.fxcreator.node.IONode");
        writeImport(output, "dae.fxcreator.io.codegen.CodeGenerator");
        writeImport(output, "dae.fxcreator.io.codegen.CodeOutput");
        writeImport(output, "dae.fxcreator.io.codegen.CodeTemplateLibrary");
        writeImport(output, "dae.fxcreator.io.codegen.ExportTask");
        output.append("public class ");
        output.append(cgdClassName);
        output.append(" extends CodeGenerator");
        output.append("{\n");
        output.append("\tpublic void generateCode(Object codeObject, ExportTask context){\n");
        if (cgd.isWriteOnce()) {
            // do not write if writeOnce is true and the output has been written.
            output.append("\t\tif ( checkObject  (codeObject,");
            if (cgd.getWriteOnceProperty() != null) {
                output.append("\"");
                output.append(cgd.getWriteOnceProperty());
                output.append("\"");
            } else {
                output.append("null");
            }
            output.append("))");
            output.append("\t\t\treturn;");
        }
        output.append("\t\t");

        output.append(className);
        output.append(" node = (");
        output.append(className);
        output.append(")codeObject;\n");
        output.append("\t\tCodeOutput output = context.getCodeOutput();\n");
        String bufferName = cgd.getBufferName();

        if (bufferName != null) {
            output.append("\t\tString bufferBackup = output.getDefaultBuffer();\n");
            output.append("\t\toutput.setDefaultBuffer(\"");
            output.append(bufferName);
            output.append("\");\n");
        }

        code = replaceIfPatterns(code, ifPattern);
        Matcher m = insertPattern.matcher(code);
        int startIndex = 0;
        int endIndex = 0;
        while (m.find()) {
            startIndex = m.start(0);
            String type = m.group(1);
            String contents = m.group(2);
            if (startIndex > 0) {
                writeQuotedOutput(output, code.substring(endIndex, startIndex));
            }
            endIndex = m.end(0);
            if (type.equals("=")) {
                contents = convertSettings(contents, settingPattern);
                contents = convertIOProperty(contents, ioPattern);
                contents = convertProperties(contents, propertyPattern);
                if (endIndex + 1 < code.length()) {
                    writeOutput(output, contents, code.charAt(endIndex + 1) == '\n');
                } else {
                    writeOutput(output, contents, false);
                }
            } else {
                output.append("\n");
                contents = convertSettings(contents, settingPattern);
                contents = convertIOProperty(contents, ioPattern);
                contents = convertProperties(contents, propertyPattern);

                //output.append("output.write(\"\\n\");\n");
                output.append(contents.trim());
                output.append("\n");
            }

        }
        if (endIndex != 0 && endIndex < (code.length())) {
            writeQuotedOutput(output, code.substring(endIndex));
        }
        if (startIndex == 0) {
            writeQuotedOutput(output, code);
        }
        if (bufferName != null) {
            output.append("\t\toutput.setDefaultBuffer(bufferBackup);\n");
        }

        output.append("\t}\n");
        output.append("}");
        String codeContents = output.toString();
        try {
            CodeGeneratorClass cgc = new CodeGeneratorClass(packageName, cgdClassName, codeContents);
            cgd.setJavaClass(cgc);
            writeJavaClass(new File(baseDir, cgdClassName + ".java"), codeContents);
            String[] options = new String[]{"-d", baseDir.getParent()};
            final JavaCompiler.CompilationTask task = compiler.getTask(null, null, null, Arrays.asList(options), null, Arrays.asList(cgc));
            Boolean result = task.call();
            if (result == Boolean.TRUE) {
                loadClass(cgd, packageName, cgdClassName);
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(CodeTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String replaceIfPatterns(String code, Pattern ifPattern) {
        Matcher m = ifPattern.matcher(code);
        StringBuffer result = new StringBuffer();
        int startIndex = 0;
        int endIndex = 0;
        while (m.find()) {
            startIndex = m.start(0);
            result.append(code, endIndex, startIndex);
            result.append("<%@\n");
            result.append("if (");
            String attributes = m.group(1);
            if (attributes.contains("condition")) {
                result.append(getAttribute(attributes, "condition"));
            } else if (attributes.contains("setting") && attributes.contains("equals")) {
                String setting = getAttribute(attributes, "setting");
                String compare = getAttribute(attributes, "equals");

                result.append("node[\"");
                result.append(setting);
                result.append("\"]");
                result.append(".equals(");
                result.append("\"");
                result.append(compare);
                result.append("\")");

            } else if (attributes.contains("type") && attributes.contains("equals")) {
                String type = getAttribute(attributes, "type");
                String compare = getAttribute(attributes, "equals");
                result.append("compareType( \"");
                result.append(compare);
                result.append("\",");
                result.append(type);
                result.append(".type ");

                result.append(" )");
            }

            result.append("){ %>");
            //result.append(m.group(2));
            //result.append("\n<%@}%>\n");

            endIndex = m.end(0);
        }
        if (endIndex < code.length()) {
            result.append(code, endIndex, code.length());
        }
        if (startIndex == 0) {
            return code;
        } else {
            String rs = result.toString();
            //System.out.println(rs);
            return rs.replace("</if>", "<%@}%>\n");
        }
    }

    private String getAttribute(String attributeString, String attributeName) {
        int conditionIndex = attributeString.indexOf(attributeName);
        int startQuote = attributeString.indexOf("=\"", conditionIndex);
        int endQuote = attributeString.indexOf("\"", startQuote + 2);
        String value = attributeString.substring(startQuote + 2, endQuote);
        return value;
    }

    private void writeJavaClass(File outputFile, String classContents) {
        OutputStreamWriter osw = null;
        try {
            FileOutputStream stream = new FileOutputStream(outputFile);
            osw = new OutputStreamWriter(stream);
            osw.write(classContents);
        } catch (IOException ex) {
            Logger.getLogger(CodeTemplateLibrary.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                osw.close();
            } catch (IOException ex) {
                Logger.getLogger(CodeTemplateLibrary.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String convertProperties(String command, Pattern pattern) {
        Matcher m = pattern.matcher(command);
        StringBuffer result = new StringBuffer();
        int startIndex = 0;
        int endIndex = 0;
        while (m.find()) {
            startIndex = m.start(1);
            result.append(command.substring(endIndex, startIndex));
            String variable = m.group(1);
            String property = m.group(2);
            endIndex = m.end(2);
            //System.out.println("endindex = " + command.charAt(endIndex));
            if (endIndex < command.length() && command.charAt(endIndex) == '(') {
                // method call
                result.append(variable);
                result.append(".");
                result.append(property);
            } else {
                // property
                result.append(variable);
                result.append(".");
                result.append("get");
                result.append(Character.toUpperCase(property.charAt(0)));
                result.append(property.substring(1));
                result.append("()");
            }
        }
        if (endIndex < command.length()) {
            result.append(command.substring(endIndex));
        }
        return result.toString();
    }

    private String convertSettings(String contents, Pattern pattern) {
        Matcher m = pattern.matcher(contents);
        StringBuffer result = new StringBuffer();
        int startIndex = 0;
        int endIndex = 0;
        while (m.find()) {
            startIndex = m.start(0);
            result.append(contents.substring(endIndex, startIndex));
            String setting = m.group(1);

            endIndex = m.end(0);
            //System.out.println("endindex = " + command.charAt(endIndex));
            int dotIndex = setting.indexOf('.');
            if (dotIndex != -1) {
                String group = setting.substring(0, dotIndex);
                String id = setting.substring(dotIndex + 1);
                result.append("node.getSettingValue(\"");
                result.append(group);
                result.append("\",\"");
                result.append(id);
                // TODO - quick hack to get this working.
                result.append("\")");
            }
        }
        if (endIndex < contents.length()) {
            result.append(contents.substring(endIndex));
        }
        return result.toString();
    }

    private String convertIOProperty(String contents, Pattern pattern) {
        Matcher m = pattern.matcher(contents);
        StringBuffer result = new StringBuffer();
        int startIndex = 0;
        int endIndex = 0;
        while (m.find()) {
            startIndex = m.start(0);
            result.append(contents.substring(endIndex, startIndex));
            String portName = m.group(1);
            String property = m.group(2);

            endIndex = m.end(0);
            //System.out.println("endindex = " + command.charAt(endIndex));
            result.append("getIO(node,\"");
            result.append(portName);
            result.append("\").get");

            result.append(Character.toUpperCase(property.charAt(0)));
            result.append(property, 1, property.length());
            result.append("()");
        }
        if (endIndex < contents.length()) {
            result.append(contents.substring(endIndex));
        }
        return result.toString();
    }

    public void writeOutput(StringBuffer sb, String output, boolean appendNewLine) {
        //sb.append("\t\toutput.write(\" \");\n");
        sb.append("\t\toutput.write(");
        sb.append(removeChars(output, "\n\t"));
        if (appendNewLine) {
            sb.append("\n");
        }

        sb.append(");\n");
    }

    private String removeChars(String content, String toRemove) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < content.length(); ++i) {
            boolean found = false;
            char currentChar = content.charAt(i);
            for (int j = 0; j < toRemove.length(); ++j) {
                if (toRemove.charAt(j) == currentChar) {
                    found = true;
                }
            }
            if (!found) {
                result.append(currentChar);
            }
        }
        return result.toString();
    }

    public void writeQuotedOutput(StringBuffer sb, String output) {
        if (output.length() == 0) {
            return;
        }
        String[] lines = output.split("\n");
        boolean appendNewLine = false;
        if (output.charAt(output.length() - 1) == '\n') {
            appendNewLine = true;
        }
        for (int i = 0; i < lines.length; ++i) {
            String trimmed = removeChars(lines[i], "\t\n");
            if (trimmed.length() == 0 && i == 0) {
                //sb.append("\t\toutput.write(\"\\n\");\n");
                continue;
            }
            sb.append("\t\toutput.write(\"");
            sb.append(trimmed.replace("\"", "\\\""));
            if (i != lines.length - 1) {
                sb.append("\\n");
            } else if (appendNewLine) {
                sb.append("\\n");
            }

            sb.append("\");\n");

        }
    }

    public void writePackage(StringBuffer sb, String packageName) {
        sb.append("package ");
        sb.append(packageName);
        sb.append(";\n");
    }

    public void writeImport(StringBuffer sb, String className) {
        sb.append("import ");
        sb.append(className);
        sb.append(";\n");
    }

    public static void main(String[] args) {
        String test = "\n\n test\nnog een test";

        test.replace("\n", "");
        System.out.println(test);
    }
}
