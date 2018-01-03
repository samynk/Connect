/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dae.fxcreator.node;

import javax.swing.event.ChangeListener;

/**
 * This interface defines the common property of the
 * ShaderInput and ShaderOutput class.
 * @author Koen
 */
public interface ShaderIO {
    /**
     * Returns the name for the ShaderInput object.
     * @return the name of the shader input object.
     */
    public String getName();
    /**
     * Sets the name for the shader input object.
     * @param name the name for the shader input object.
     */
    public void setName(String name);
    /**
     * Returns the semantic for the input or output object.
     * @return the semantic.
     */
    public Semantic getSemantic();
    /**
     * Returns the shader type for the ShaderIO object.
     * @return the shadertype.
     */
    public ShaderType getType();
    /**
     * Gets the variable name for this ShaderIO object.
     * @return the variable name for this ShaderIO object.
     */
    public String getRef();
    /**
     *Returns the variable declaration name for the ShaderIO object.
     * @return the variable declaration name.
     */
    public String getVar();
    /**
     * Checks if this ShaderIO object is connected.
     * @return true if the ShaderIO object is connected, false otherwise.
     */
    public boolean getConnected();
    /**
     * Checks if this is an input node.
     * @return true if this is an input node, false otherwise.
     */
    public boolean isInput();
     /**
     * Checks if this is an output node.
     * @return true if this is an output node, false otherwise.
     */
    public boolean isOutput();
    /**
     * Checks if this input or output is compatible with the provided shader io object.
     * @return true if this object is compatible (can connect) with the provided object.
     */
    public boolean accepts(ShaderIO io);
    /**
     * Adds a change listener to this io object.
     * @param cl the change listener to add to the list of listeners.
     */
    public void addChangeListener(ChangeListener cl);
    /**
     * Removes the change listener from this io objecT.
     * @param cl the change listener to remove from the list of listeners.
     */
    public void removeChangeListener(ChangeListener cl);
}
