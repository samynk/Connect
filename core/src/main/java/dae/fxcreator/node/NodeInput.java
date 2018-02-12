package dae.fxcreator.node;

/**
 * This class defines the possible inputs for a shader node. A shader input has
 * a type and a name.
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class NodeInput extends NodeIO implements TypedNode{    
    /**
     * The node that is connected to this input.
     */
    private NodeOutput input;
    /**
     * The connection string to find the connected ShaderOutput object.
     */
    private String connectionString;

    /**
     * A list of accepted types by this ShaderInput object.
     */
    public String acceptTypeSet;

    /**
     * Constructs a new ShaderInput object with a name and a type.
     *
     * @param parent the parent node for this ShaderInput object.
     * @param name the name for the shaderinput.
     * @param semantic the semantic for the shaderinput, can be null.
     * @param type the type of the shaderinput as string.
     */
    public NodeInput(IONode parent, String name, String semantic, String type) {
        super(parent, name, semantic, parent.shaderTypeLib.getType(type));
        this.acceptTypeSet = null;
    }

    /**
     * Constructs a new ShaderInput object with a name and a type.
     *
     * @param parent the parent node for this ShaderInput object.
     * @param name the name for the shaderinput.
     * @param semantic the semantic for the shaderinput, can be null.
     * @param type the type of the shaderinput as string.
     * @param typeSet the name for the collection of input types that are
     * allowed.
     */
    public NodeInput(IONode parent, String name, String semantic, String type, String typeSet) {
        super(parent, name, semantic, parent.shaderTypeLib.getType(type));
        this.acceptTypeSet = typeSet;
    }

    /**
     * Constructs a new ShaderInput object with a name and a type.
     *
     * @param parent the parent node for this ShaderInput object.
     * @param name the name for the shaderinput.
     * @param semantic the semantic for the shaderinput, can be null.
     * @param type the type of the shaderinput.
     */
    public NodeInput(IONode parent, String name, String semantic, IOType type) {
        super(parent, name, semantic, type);
        this.acceptTypeSet = null;
    }

    /**
     * Constructs a new ShaderInput object with a name and a type.
     *
     * @param parent the parent node for this ShaderInput object.
     * @param name the name for the shaderinput.
     * @param semantic the semantic for the shaderinput, can be null.
     * @param type the type of the shaderinput.
     * @param typeSet the name for the collection of input types that are
     * allowed.
     */
    public NodeInput(IONode parent, String name, String semantic, IOType type, String typeSet) {
        super(parent,name, semantic, type);
        this.acceptTypeSet = typeSet;
    }

    /**
     * Connect this ShaderInput object to the provided ShaderOutput object.
     *
     * @param output the output to connect this ShaderInput object to.
     */
    public void setConnectedInput(NodeOutput output) {
        if (this.input != null) {
            input.removeInput(this);
        }
        this.input = output;
        if (this.input != null) {
            output.addInput(this);
            IONode outputNode = output.getParent();
            this.setConnectionString(outputNode.getId() + "." + output.getName());
            setIOType(output.getIOType());
            notifyListeners();
        } else {
            this.setConnectionString("");
        }
    }

    /**
     * Checks if this input is connected to an output.
     *
     * @return true if this input is connected , false otherwise.
     */
    @Override
    public boolean getConnected() {
        return input != null && input.getParent() != null;
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
     *
     * @return the ShaderOutput object.
     */
    public NodeOutput getConnectedInput() {
        return this.input;
    }



    /**
     * Gets the variable name for this input.
     *
     * @return the variable name for this input.
     */
    @Override
    public String getVar() {
        IONode parent = getParent();
        if (parent != null) {
            return parent.getId() + parent.getSeparator() + getName();
        } else {
            return "parentnotfound_" + getName();
        }
    }

    /**
     * The reference of an input is created by asking the variable name of the
     * connected output port.
     *
     * @return the variable name.
     */
    @Override
    public String getRef() {
        IONode parent = getParent();
        if (this.input != null) {
            return input.getRef();
        } else {
            if (parent != null) {
                return parent.getId() + parent.getSeparator() + getName() + "notfound";
            } else {
                return getName() + "_" + "not found";
            }
        }
    }

    /**
     * Sets the type for the shader input object.
     *
     * @param type the type for the shader input object.
     */
    @Override
    public void setIOType(IOType type) {
        super.setIOType(type);
        notifyListeners();
        getParent().typeChanged(this);
    }

    /**
     * Connects this shader input to the ShaderOutput of another node.
     *
     * @param output the output to connect to this input.
     */
    public void setConnection(NodeOutput output) {
        this.input = output;
    }

    /**
     * Sets the connection string. This method exists to enable lazy connection
     * settings. This connection string will only be evaluated : 1) when the
     * project file is validated. 2) when the shader structure is rendered. 3)
     * when the shader structure is exported to an fx file.
     *
     * @param connectionString
     */
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    /**
     * Returns the connection string for input.
     *
     * @return the connection string.
     */
    public String getConnectionString() {
        return connectionString;
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
     * Checks if this is an input node.
     *
     * @return true if this is an input node, false otherwise.
     */
    @Override
    public boolean isInput() {
        return true;
    }

    /**
     * Checks if this is an output node.
     *
     * @return true if this is an output node, false otherwise.
     */
    @Override
    public boolean isOutput() {
        return false;
    }

    /**
     * Checks if this input or output is compatible with the provided shader io
     * object.
     *
     * @param io the input or output object to check.
     * @return true if this object is compatible (can connect) with the provided
     * object.
     */
    @Override
    public boolean accepts(NodeIO io) {
        IONode parent = getParent();
        if (io.isInput()) {
            return false;
        }
        if (acceptTypeSet != null) {
            return (parent.getShaderTypeLibrary().isTypeFromSet(acceptTypeSet, io.getIOType()));
        } else {
            return io.getIOType().equals(getIOType());
        }
    }

    /**
     * Returns the accept type set for this shader input.
     *
     * @return the accept type set.
     */
    public String getAcceptTypeSet() {
        return acceptTypeSet;
    }

    /**
     * Sets the accept type set for this shader input.
     *
     * @param acceptTypeSet the new acceptTypeSet value.
     */
    public void setAcceptTypeSet(String acceptTypeSet) {
        this.acceptTypeSet = acceptTypeSet;
    }


    @Override
    public String getId() {
        return this.getName();
    }


    @Override
    public String getType() {
        return "node.input";
    }
}
