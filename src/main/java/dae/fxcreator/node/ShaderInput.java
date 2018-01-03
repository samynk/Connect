/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.node;

import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class defines the possible inputs for a shader node. A shader input has
 * a type and a name.
 * @author Koen
 */
public class ShaderInput implements ShaderIO {

    private String name;
    private ShaderType type;
    /**
     * The parent node of this ShaderInput object
     */
    private IONode parent;
    /**
     * The node that is connected to this input.
     */
    private ShaderOutput input;
    /**
     * The connection string to find the connected ShaderOutput object.
     */
    private String connectionString;
    /**
     * The semantic of this input. This is only useful when declaring global
     * variables or input/output structs for shader stages.
     */
    private Semantic semantic = new Semantic();
    /**
     * A list of accepted types by this ShaderInput object.
     */
    public String acceptTypeSet;
    /**
     * The list of change listeners
     */
    private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();

    /**
     * Constructs a new ShaderInput object with a name and a type.
     * @param parent the parent node for this ShaderInput object.
     * @param name the name for the shaderinput.
     * @param semantic the semantic for the shaderinput, can be null.
     * @param type the type of the shaderinput as string.
     * @param typeSet the name for the collection of input types that are allowed.
     */
    public ShaderInput(IONode parent, String name, String semantic, String type) {
        this.parent = parent;
        this.type = parent.shaderTypeLib.getType(type);
        this.name = name;
        this.semantic.setValue(semantic);
        this.acceptTypeSet = null;
    }

    /**
     * Constructs a new ShaderInput object with a name and a type.
     * @param parent the parent node for this ShaderInput object.
     * @param name the name for the shaderinput.
     * @param semantic the semantic for the shaderinput, can be null.
     * @param type the type of the shaderinput as string.
     * @param typeSet the name for the collection of input types that are allowed.
     */
    public ShaderInput(IONode parent, String name, String semantic, String type, String typeSet) {
        this.parent = parent;
        this.type = parent.shaderTypeLib.getType(type);
        this.name = name;
        this.semantic.setValue(semantic);
        this.acceptTypeSet = typeSet;
    }
    
    /**
     * Constructs a new ShaderInput object with a name and a type.
     * @param parent the parent node for this ShaderInput object.
     * @param name the name for the shaderinput.
     * @param semantic the semantic for the shaderinput, can be null.
     * @param type the type of the shaderinput.
     */
    public ShaderInput(IONode parent, String name, String semantic, ShaderType type) {
        this.parent = parent;
        this.type = type;
        this.name = name;
        this.semantic.setValue(semantic);
        this.acceptTypeSet = null;
    }

    /**
     * Constructs a new ShaderInput object with a name and a type.
     * @param parent the parent node for this ShaderInput object.
     * @param name the name for the shaderinput.
     * @param semantic the semantic for the shaderinput, can be null.
     * @param type the type of the shaderinput.
     * @param typeSet the name for the collection of input types that are allowed.
     */
    public ShaderInput(IONode parent, String name, String semantic, ShaderType type, String typeSet) {
        this.parent = parent;
        this.type = type;
        this.name = name;
        this.semantic.setValue(semantic);
        this.acceptTypeSet = typeSet;
    }

    /**
     * Sets the parent node object of this input node.
     * @param parent the parent node for this ShaderInput object.
     */
    public void setParent(IONode parent) {
        this.parent = parent;
    }

    /**
     * Returns the parent node of this input object.
     * @return the parent node for this ShaderInput object.
     */
    public IONode getParent(){
        return parent;
    }

    /**
     * Connect this ShaderInput object to the provided ShaderOutput object.
     * @param output the output to connect this ShaderInput object to.
     */
    public void setConnectedInput(ShaderOutput output) {
        if (this.input != null) {
            input.removeInput(this);
        }
        this.input = output;
        if (this.input != null) {
            output.addInput(this);
            IONode outputNode = output.getParent();
            this.setConnectionString(outputNode.getId() + "." + output.getName());
            setType(output.getType());
            notifyListeners();
        }else{
            this.setConnectionString("");
        }
    }

    /**
     * Checks if this input is connected to an output.
     * @return true if this input is connected , false otherwise.
     */
    public boolean getConnected() {
        return input != null && input.getParent() != null;
    }
    
    /**
     * Checks if the type of this input is an actual value.
     * @return true if this input connects to a value type, false otherwise.
     */
    public boolean getValueType(){
        return type != null && type.isValueType();
    }

    /**
     * Disconnects the ShaderOutput object from this input object.
     */
    public void disconnectInput() {
        if (this.input != null) {
            input.removeInput(this);
            input = null;
            this.setConnectionString("");
        }
    }

    /**
     * Gets the ShaderOutput object that this ShaderInput is connected to.
     * @return the ShaderOutput object.
     */
    public ShaderOutput getConnectedInput() {
        return this.input;
    }

    /**
     * Returns the name for the ShaderInput object.
     * @return the name of the shader input object.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for the shader input object.
     * @param name the name for the shader input object.
     */
    public void setName(String name) {
        if (!this.name.equals(name)) {
            String oldName = this.name;
            this.name = name;
            if (this.parent != null) {
                parent.remapInputName(oldName, this);
            }
        }
    }

    /**
     * Gets the variable name for this input.
     * @return the variable name for this input.
     */
    public String getVar() {
        if (parent != null) {
            return parent.getId() + parent.getSeparator() + name;
        } else {
            return "parentnotfound_" + name;
        }
    }

    /**
     * The reference of an input is created by asking the variable
     * name of the connected output port.
     * @return the variable name.
     */
    public String getRef() {
        if (this.input != null) {
            return input.getRef();
        } else {
            if (parent != null) {
                return parent.getId() + parent.getSeparator() + this.name + "notfound";
            } else {
                return this.name + "_" + "not found";
            }
        }
    }

    /**
     * Returns the type for the shader input object.
     * @return the type for the shader input object.
     */
    public ShaderType getType() {
        return type;
    }

    /**
     * Sets the type for the shader input object.
     * @param type the type for the shader input object.
     */
    public void setType(ShaderType type) {
        this.type = type;
        notifyListeners();
        parent.typeChanged(this);
    }

    /**
     * Connects this shader input to the ShaderOutput of another node.
     * @param output the output to connect to this input.
     */
    public void setConnection(ShaderOutput output) {
        this.input = output;
    }

    /**
     * Sets the connection string. This method exists to enable lazy connection
     * settings. This connection string will only be evaluated :
     * 1) when the project file is validated.
     * 2) when the shader structure is rendered.
     * 3) when the shader structure is exported to an fx file.
     * @param connectionString
     */
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    /**
     * Returns the connection string for input.
     * @return the connection string.
     */
    public String getConnectionString() {
        return connectionString;
    }

    /**
     * Returns the semantic of this ShaderInput object.
     * @return the semantic of this shader input object.
     */
    public Semantic getSemantic() {
        return semantic;
    }

    /**
     * Sets the semantic of the ShaderInput object.
     * @param semantic the new sementic for this input object.
     */
    public void setSemantic(String semantic) {
        if (!semantic.equals(this.semantic.getValue())) {
            String oldValue = this.semantic.getValue();
            this.semantic.setValue(semantic);
            parent.remapInputSemantic(oldValue, this);
        }
    }

    /**
     * Updates the connection string of this ShaderInput object.
     */
    void updateConnectionString() {
        if (this.input != null) {
            IONode inputNodeParent = input.getParent();
            this.setConnectionString(inputNodeParent.getId() + "." + input.getName());
        }
    }

    /**
     * Returns a label for this ShaderInput object.
     */
    public String getLabel() {
        if (semantic.isValid()) {
            return this.name + ":" + semantic;
        } else {
            return this.name;
        }
    }

    /**
     * Checks if this is an input node.
     * @return true if this is an input node, false otherwise.
     */
    public boolean isInput() {
        return true;
    }

    /**
     * Checks if this is an output node.
     * @return true if this is an output node, false otherwise.
     */
    public boolean isOutput() {
        return false;
    }

    /**
     * Checks if this input or output is compatible with the provided shader io object.
     * @param io the input or output object to check.
     * @return true if this object is compatible (can connect) with the provided object.
     */
    @Override
    public boolean accepts(ShaderIO io) {
        if (io.isInput()) {
            return false;
        }
        if (acceptTypeSet != null) {
            return (parent.getShaderTypeLibrary().isTypeFromSet(acceptTypeSet, io.getType()));
        } else {
            return io.getType().equals(this.type);
        }
    }

    /**
     * Returns the accept type set for this shader input.
     * @return the accept type set.
     */
    public String getAcceptTypeSet() {
        return acceptTypeSet;
    }

    /**
     * Sets the accept type set for this shader input.
     * @param acceptTypeSet the new acceptTypeSet value.
     */
    public void setAcceptTypeSet(String acceptTypeSet) {
        this.acceptTypeSet = acceptTypeSet;
    }

    /**
     * Adds a change listener to this io object.
     * @param cl the change listener to add to the list of listeners.
     */
    public void addChangeListener(ChangeListener cl) {
        listeners.add(cl);
    }

    /**
     * Removes the change listener from this io objecT.
     * @param cl the change listener to remove from the list of listeners.
     */
    public void removeChangeListener(ChangeListener cl) {
        listeners.add(cl);
    }

    /**
     * Notifies the listeners that this object has changed.
     */
    private void notifyListeners() {

        ChangeEvent ce = new ChangeEvent(this);
        for (ChangeListener cl : listeners) {
            cl.stateChanged(ce);
        }
    }
}
