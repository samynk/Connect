package dae.fxcreator.io.codegen;

import dae.fxcreator.io.ExportFile;
import dae.fxcreator.io.FXProject;
import dae.fxcreator.io.TypedNode;
import dae.fxcreator.io.templates.MathSetting;
import dae.fxcreator.io.templates.Setting;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderNode;
import dae.fxcreator.node.ShaderOutput;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Export Task collects all the objects that are necessary to export a
 * FXProject object to for example a shader file.
 *
 * @author Koen
 */
public class ExportTask {

    private final FXProject project;
    private final CodeTemplateLibrary library;
    private final CodeOutput output;
    private final HashMap<String, BufferedWriter> streamMap = new HashMap<>();
    private final HashMap<String, File> fileMap = new HashMap<>();

    public ExportTask(FXProject project, String exporterId, CodeTemplateLibrary library) {
        this.project = project;
        this.library = library;
        this.output = new CodeOutput(library);

        ExportFile ef = this.project.getExportDestination(exporterId);
        File file = new File(ef.getDirectory(), ef.getFilename() + "." + ef.getExtension());

        createStream("default", file);
    }

    public FXProject getFXProject() {
        return project;
    }

    public CodeTemplateLibrary getLibrary() {
        return library;
    }

    public CodeOutput getCodeOutput() {
        return output;
    }

    /**
     * Convenience method to replace output & input names in a string with the
     * specific node names.
     */
    public String replaceIONames(IONode io, String code) {
        String result = code;
        for (ShaderInput input : io.getInputs()) {
            String name = input.getName();
            String ref = input.getRef();
            result = result.replaceAll("\\b(" + input.getName() + ")\\b", ref);
        }
        for (ShaderOutput so : io.getOutputs()) {
            result = result.replaceAll("\\b(" + so.getName() + ")\\b", so.getRef());
        }
        result += "\n";
        return result;
    }

    /**
     * Creates the declarations for all the outputs in the node.
     *
     * @return the string with the definitions.
     */
    public String createOutputDefinitions(IONode io) {
        StringBuilder sb = new StringBuilder();
        for (ShaderOutput so : io.getOutputs()) {
            sb.append(library.getShaderType(so.getType()));
            sb.append(" ");
            sb.append(so.getVar());
            sb.append(";\n");
        }
        return sb.toString();
    }

    /**
     * Creates a C style parameter list on the basis of the input elements of
     * the given node.
     *
     * @param io the node to generate an input parameter list for.
     * @param separator the separator of the parameter list.
     * @return the string with the parameter list.
     */
    public String createInputParameterList(IONode io, String separator) {
        return createInputParameterList(io, separator, true," ");
    }

    /**
     * Creates a  parameter list on the basis of the input elements of
     * the given node.
     *
     * @param io the node to generate an input parameter list for.
     * @param separator the separator of the parameter list.
     * @param typeAsPrefix if true the type will be added before the parameter, if false the
     * type will added as post fix to the parameter.
     * @return 
     */
    public String createInputParameterList(IONode io, String separator, boolean typeAsPrefix, String infixSeparator) {
        StringBuilder sb = new StringBuilder();
        for (ShaderInput si : io.getInputs()) {
            if (typeAsPrefix) {
                sb.append(library.getShaderType(si.getType()));
                sb.append(infixSeparator);
                sb.append(si.getName());
            } else {
                sb.append(si.getName());
                sb.append(infixSeparator);
                sb.append(library.getShaderType(si.getType()));
            }
            sb.append(separator);
        }
        sb.delete(sb.length()-1, sb.length());
        return sb.toString();
    }

    /**
     * Calls a template for another CodeGenerator object.
     *
     * @param o the object to call the buffer for.
     * @param method the method in the template to use.
     * @param buffer the buffer to add the output to.
     */
    public void template(TypedNode object, String method) {
        String className = object.getClass().getName();

        CodeTemplate template = library.getTemplateForClassName(className);
        if (template != null) {
            template.generateCode(object, method, this);
        }
    }

    /**
     * Calls a specific template for this node. Be careful that all the
     * properties, inputs, outputs and settings that are needed for the template
     * to work are available in the template.
     *
     * @param object the object that contains the information for the template.
     * @param method the method to call.
     * @param type
     */
    public void template(TypedNode object, String method, String group, String subType) {
        String className = object.getClass().getName();

        CodeTemplate template = library.getTemplateForClassName(className);
        if (template != null) {
            template.generateCode(object, method, group + "." + subType, this);
        }
    }

    /**
     * Specific code to generate the math code.
     *
     * @param node the node that contains the math formula.
     * @param mathSetting the setting where the math formula is stored.
     */
    public String generateMathCode(ShaderNode node, String settingGroup, String settingId) {
        Setting math = node.getSetting(settingGroup, settingId);
        if (math != null && math instanceof MathSetting) {
            String result = library.generateMathCode(((MathSetting) math).getMathFormula());
            return result;
        }
        return "";
    }

    /**
     *
     * @param streamName
     * @param location
     */
    private void createStream(String streamName, File location) {
        FileWriter fos = null;
        try {
            // check if parent dirs exist
            File parent = location.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            fos = new FileWriter(location);
            BufferedWriter bw = new BufferedWriter(fos);
            this.streamMap.put(streamName, bw);
            this.fileMap.put(streamName, location);
        } catch (IOException ex) {
            Logger.getLogger(ExportTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates a new Stream relative to the location of the stream with as
     * identifier baseStreamName.
     *
     * @param baseStreamName the base stream.
     * @param newStreamName the name of the new stream.
     * @param path the path for the new stream.
     */
    public void createRelativeStream(String baseStreamName, String newStreamName, String path) {
        File base = this.fileMap.get(baseStreamName);
        if (base != null) {
            File parent = base.getParentFile();
            File newLocation = new File(parent, path);
            System.out.println("Creating new stream : " + newStreamName);
            System.out.println("location : " + newLocation);
            createStream(newStreamName, newLocation);
        }
    }

    /**
     * Close all the streams in this export task
     */
    public void closeStreams() {
        for (BufferedWriter bw : streamMap.values()) {
            try {
                bw.flush();
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(ExportTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void closeStream(String streamName) {
        BufferedWriter bw = streamMap.get(streamName);
        if (bw != null) {
            try {
                bw.flush();
                bw.close();
                streamMap.remove(streamName);
            } catch (IOException ex) {
                Logger.getLogger(ExportTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Creates a new named stream where the template can write to as a file in
     * the subdirectory of the ouitput directory.
     *
     * @param streamName the name of the stream for future reference.
     * @param location the location of the stream object.
     */
    public void createStream(String streamName, String location) {
        File file = new File(location);
        this.createStream(streamName, file);
    }

    /**
     * Writes a named buffer to a named stream. If a CodeFormatter object is
     * active for the CodeTemplateLibrary, the formatter will be applied to the
     * contents of the buffer.
     *
     * @param streamName the name of the stream.
     * @param bufferName the name of the buffer.
     */
    public void writeBufferToStream(String streamName, String bufferName) {
        BufferedWriter bw = streamMap.get(streamName);
        StringBuffer sb = this.output.getBuffer(bufferName);
        if (bw != null && sb != null) {
            try {
                String contents = sb.toString();
                if (library.hasCodeFormatter()) {
                    CodeFormatter formatter = library.getCodeFormatter();
                    contents = formatter.format(contents);
                    this.output.setBuffer(bufferName, contents);
                }
                //System.out.println(contents);
                bw.write(contents);
            } catch (IOException ex) {
                Logger.getLogger(ExportTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void clearBuffer(String bufferName) {
        StringBuffer sb = this.output.getBuffer(bufferName);
        if (sb != null) {
            sb.delete(0, sb.length());
        }
    }

    public void export() {
        this.template(project, "main");
        closeStreams();
    }

    /**
     * Returns the list of generated files.
     *
     * @return the list of files.
     */
    public Iterable<File> getFiles() {
        return fileMap.values();
    }
}
