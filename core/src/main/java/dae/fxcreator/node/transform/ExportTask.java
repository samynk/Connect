package dae.fxcreator.node.transform;

import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.node.TypedNode;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderOutput;
import dae.fxcreator.node.annotations.Export;
import dae.fxcreator.node.transform.exec.Expression;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Export Task collects all the objects that are necessary to export a
 * FXProject object to for example a shader file.
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ExportTask {

    private final FXProject project;
    private final TemplateClassLibrary library;
    private final CodeOutput output;
    private final HashMap<String, BufferedWriter> streamMap = new HashMap<>();
    private final HashMap<String, Path> fileMap = new HashMap<>();

    private final Deque<StringBuilder> bufferStack = new ArrayDeque<>();
    private final HashMap<String, Object> varMap = new HashMap<>();

    private final Stack<Object> chainStack = new Stack<>();

    public ExportTask(FXProject project, String exporterId, TemplateClassLibrary library) {
        this.project = project;
        this.library = library;
        this.output = new CodeOutput(library);

        // to do fix exporting.
        /*
        ExportFile ef = this.project.getExportDestination(exporterId);
        Path file = Paths.get(ef.getDirectory().getPath(), ef.getFilename() + "." + ef.getExtension());

        createPathStream("default", file);
         */
    }

    public ExportTask(FXProject project, Path exportLocation, TemplateClassLibrary library) {
        this.project = project;
        this.library = library;
        this.output = new CodeOutput(library);

        createPathStream("default", exportLocation);
    }

    public FXProject getFXProject() {
        return project;
    }

    public TemplateClassLibrary getLibrary() {
        return library;
    }

    public CodeOutput getCodeOutput() {
        return output;
    }

    /**
     * Convenience method to replace output & input names in a string with the
     * specific node names.
     *
     * @param io the node that contains the input and output ports.
     * @param code the code that contains references to inputs and outputs.
     * @return a string with the replaced input/output names.
     */
    public String replaceIONames(IONode io, String code) {
        String result = code;
        for (ShaderInput input : io.getInputs()) {
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
     * @param io the node with the outputs.
     * @return the string with the definitions.
     */
    public String createOutputDefinitions(IONode io) {
        StringBuilder sb = new StringBuilder();
        for (ShaderOutput so : io.getOutputs()) {
            sb.append(library.getShaderType(so.getIOType()));
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
        return createInputParameterList(io, separator, true, " ");
    }

    /**
     * Creates a parameter list on the basis of the input elements of the given
     * node.
     *
     * @param io the node to generate an input parameter list for.
     * @param separator the separator of the parameter list.
     * @param typeAsPrefix if true the type will be added before the parameter,
     * if false the type will added as post fix to the parameter.
     * @return
     */
    public String createInputParameterList(IONode io, String separator, boolean typeAsPrefix, String infixSeparator) {
        StringBuilder sb = new StringBuilder();
        for (ShaderInput si : io.getInputs()) {
            if (typeAsPrefix) {
                sb.append(library.getShaderType(si.getIOType()));
                sb.append(infixSeparator);
                sb.append(si.getName());
            } else {
                sb.append(si.getName());
                sb.append(infixSeparator);
                sb.append(library.getShaderType(si.getIOType()));
            }
            sb.append(separator);
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    /**
     * Calls a template for another CodeGenerator object.
     *
     * @param object the object to call the buffer for.
     * @param method the method in the template to use.
     */
    @Export(name = "call")
    public void template(TypedNode object, String method) {
        String className = object.getClass().getName();
        TemplateClass template = library.getTemplateForClassName(className);
        if (template != null) {
            Object backup = getVar("node");
            setVar("node", object);
            template.generateCode(object, method, this);
            setVar("node", backup);
        } else {
            Logger.getLogger(ExportTask.class.getName()).log(Level.INFO, "Could not find {0} for {1}", new Object[]{method, className});
        }
    }

    private void createPathStream(String streamName, Path location) {
        try {
            if (!Files.exists(location.getParent())) {
                Files.createDirectories(location.getParent());
            }
            if (Files.exists(location.getParent())) {
                this.streamMap.put(streamName, Files.newBufferedWriter(location));
                this.fileMap.put(streamName, location);
            }
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
        Path base = this.fileMap.get(baseStreamName);
        if (base != null) {
            Path relativePath = base.resolveSibling(path);
            createPathStream(newStreamName, relativePath);
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

    /**
     * Close the stream with the given name.
     *
     * @param streamName the name of the stream to close.
     */
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
        Path file = Paths.get(location);
        this.createPathStream(streamName, file);
    }

    /**
     * Writes a named buffer to a named stream. If a CodeFormatter object is
     * active for the CodeTemplateLibrary, the formatter will be applied to the
     * contents of the buffer.
     *
     * @param streamName the name of the stream.
     * @param bufferName the name of the buffer.
     */
    @Export(name = "writeBufferToStream")
    public void writeBufferToStream(String streamName, String bufferName) {
        BufferedWriter bw = streamMap.get(streamName);
        StringBuilder sb = this.output.getBuffer(bufferName);
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

    /**
     * Clears the buffer of all its contents.
     *
     * @param bufferName the buffer to clear.
     */
    public void clearBuffer(String bufferName) {
        StringBuilder sb = this.output.getBuffer(bufferName);
        if (sb != null) {
            sb.setLength(0);
        }
    }

    /**
     * Export the project to the output buffers and streams.
     */
    public void export() {
        pushBuffer(this.output.getBuffer("default"));
        this.template(project, "main");
        closeStreams();
    }

    /**
     * Returns the list of generated files.
     *
     * @return the list of files.
     */
    public Iterable<Path> getFiles() {
        return fileMap.values();
    }

    /**
     * Pushes a buffer onto the stack of buffers.
     *
     * @param buffer the buffer to push onto the stack. This buffer will be the
     * current buffer.
     */
    public void pushBuffer(StringBuilder buffer) {
        bufferStack.add(buffer);
    }

    /**
     * Pops a buffer from the stack of buffers.
     *
     * @return the buffer that was popped from the stack.
     */
    public StringBuilder popBuffer() {
        return bufferStack.removeLast();
    }

    /**
     * Gets the top of the buffer without removing it. This method retrieves the
     * current buffer.
     *
     * @return the current buffer to write to.
     */
    public StringBuilder peekBuffer() {
        return bufferStack.peekLast();
    }

    /**
     * Adds a variable to the export task context with the given name.
     *
     * @param varName the name of the variable.
     * @param o the variable.
     */
    public void setVar(String varName, Object o) {
        varMap.put(varName, o);
    }

    /**
     * Adds the evaluation of an expression to the export task context.
     *
     * @param varName the name of the variable.
     * @param o the variable.
     */
    @Export(name = "push")
    public void push(String varName, Object o) {
        varMap.put(varName, o);
    }

    /**
     * Gets a variable from the hashmap.
     *
     * @param varName the name of the variable.
     * @return the object with the given varName or null if not present.
     */
    public Object getVar(String varName) {
        return varMap.get(varName);
    }

    /**
     * Removes a variable from the context.
     *
     * @param varName the variable name to remove.
     */
    @Export(name = "pop")
    public void pop(String varName) {
        varMap.remove(varName);
    }

    public Object popChainStack() {
        return chainStack.pop();
    }

    public void pushChainStack(Object o) {
        chainStack.push(o);
    }
}
