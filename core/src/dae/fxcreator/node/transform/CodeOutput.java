package dae.fxcreator.node.transform;

import dae.fxcreator.node.transform.TemplateClassLibrary;
import dae.fxcreator.node.Semantic;
import dae.fxcreator.node.ShaderType;
import java.util.HashMap;

/**
 * This class provides an infrastructure to create buffers for the final output.
 * For example , for a directx10 shader the cbPerObject constants can be collected
 * in a separate buffer, before they are finally written to the fx shader.
 * The standard buffers are :
 * 1) header : this buffers stores everything in the header file.
 * 2) struct : this buffer stores all the struct definitions.
 * 3) function : this buffers stores all the function definition.
 * @author Koen
 */
public class CodeOutput {

    /**
     * A hashmap with the buffer objects.
     */
    private final HashMap<String, StringBuilder> buffers = new HashMap<>();
    /**
     * The default buffer to use.
     */
    private String defaultBufferName;
    /**
     * The default StringBuilder object.
     */
    private StringBuilder defaultBuffer;
    /**
     * The CodeTemplateLibrary that provides extra functionality.
     */
    private final TemplateClassLibrary library;

    /**
     * Creates a new code output object. Three buffers will be created :
     * 1) the header buffer.
     * 2) the struct buffer.
     * 3) the function buffer.
     * 4) the default buffer where all the standard elements will be stored.
     * @param library the library to create a CodeOutput object for.
     */
    public CodeOutput(TemplateClassLibrary library) {
        createBuffer("header");
        createBuffer("struct");
        createBuffer("function");
        createBuffer("default");
        this.library = library;
    }

    /**
     * Creates a new Buffer with the given name.
     * @param name the name for the buffer.
     * @return a new empty stringbuffer object.
     */
    public final StringBuilder createBuffer(String name) {
        StringBuilder sb = new StringBuilder();
        buffers.put(name, sb);
        return sb;
    }

    /**
     * Returns the StringBuilder object.
     * @param name the name of the StringBuilder object.
     * @return the StringBuilder object.
     */
    public StringBuilder getBuffer(String name) {
        if ( buffers.containsKey(name))
            return buffers.get(name);
        else{
            return createBuffer(name);
        }
    }

    /**
     * Sets the default buffer to use. If the buffer does not exist, it will
     * be created.
     * @param bufferName the name of the default buffer.
     */
    public void setDefaultBuffer(String bufferName) {
        if (bufferName == null)
            return;
        if (!bufferName.equals(defaultBufferName)) {
            StringBuilder sb = buffers.get(bufferName);
            if (sb == null) {
                sb = createBuffer(bufferName);
                buffers.put(bufferName, sb);
            }
            defaultBuffer = sb;
            defaultBufferName = bufferName;
        }
    }

    /**
     * Returns the name of the default buffer.
     * @return the name of the default buffer.
     */
    public String getDefaultBuffer(){
        return defaultBufferName;
    }

    /**
     * Write to the default buffer.
     * @param buffer the buffer to append to the default buffer.
     */
    public void write(StringBuilder buffer){
        defaultBuffer.append(buffer);
    }

    /**
     * Write to the default buffer.
     * @param text the buffer to append to the default buffer.
     */
    public void write(String text){
        defaultBuffer.append(text);
    }

    /**
     * Write the type to the default buffer.
     * @param type the type to write to the default buffer.
     */
    public void write(ShaderType type){
        defaultBuffer.append(library.getShaderType(type));
    }

    /**
     * Write a semantic to the output
     * @param semantic the semantic to write.
     */
    public void write(Semantic semantic){
        if ( semantic.isValid()){
            defaultBuffer.append(semantic.toString());
        }
    }

    /**
     * Write to the buffer with the provided name.
     * @param bufferName the name of the buffer to write to.
     * @param text the buffer to append to the default buffer.
     */
    public void write(String bufferName,StringBuilder text){
        StringBuilder buffer = this.getBuffer(bufferName);
        buffer.append(text);
    }

    /**
     * Write to the buffer with the provided name.
     * @param bufferName the name of the buffer to write to.
     * @param text the string to append to the default buffer.
     */
    public void write(String bufferName,String text){
        StringBuilder buffer = this.getBuffer(bufferName);
        buffer.append(text);
    }

    /**
     * Add the provided string to the beginning of the buffer.
     * @param bufferName the name of the buffer.
     * @param text the string to prepend to the buffer.
     */
    public void prepend(String bufferName, String text){
        StringBuilder buffer = this.getBuffer(bufferName);
        buffer.insert(0, text);
    }

    /**
     * Write to the buffer with the provided name.
     * @param bufferName the name of the buffer.
     * @param type the shader type to append to the default buffer.
     */
    public void write(String bufferName,ShaderType type){
        StringBuilder buffer = this.getBuffer(bufferName);
        buffer.append(library.getShaderType(type));
    }

    /**
     * Write a semantic to the buffer with the provided name.
     * @param buffername the name of the buffer.
     * @param semantic the semantic to write.
     */
    public void write(String buffername,Semantic semantic){
        if ( semantic.isValid()){
            StringBuilder buffer = this.getBuffer(buffername);
            buffer.append(semantic.toString());
        }
    }


    /**
     * Deletes all the StringBuilder objects in this CodeOutput object.
     */
    public void reset() {
        for ( StringBuilder sb : this.buffers.values()){
            sb.delete(0,sb.length());
        }
    }

    /**
     * Sets the contents of a named buffer.
     * @param bufferName the name of the buffer.
     * @param contents the new contents for the buffer.
     */
    public void setBuffer(String bufferName, String contents) {
        buffers.put(bufferName, new StringBuilder(contents));
    }
}
