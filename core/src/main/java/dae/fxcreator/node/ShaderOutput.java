package dae.fxcreator.node;

import java.util.ArrayList;

/**
 * The output implementation of the ShaderIO 
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ShaderOutput extends ShaderIO implements TypedNode{
    /**
     * The nodes that are connected to this output.
     */
    private final ArrayList<ShaderInput> nodes = new ArrayList<>();
    /**
     * This property allows for outputs that have an external source, such as
     * variables that are defined by semantics.
     */
    private String connectionString;
   
    /**
     * The type inference rule for this shader output object.
     */
    private String typeRule;

    /**
     * Constructs a new ShaderOutput object with a name and a type.
     * @param parent the parent node for this ShaderOutput object.
     * @param name the name for the ShaderOutput.
     * @param semantic the semantic for the ShaderOutput (null is allowed).
     * @param type the type of the ShaderOutput.
     * @param typeRule a type rule can define the type of the output based on rules that will
     * be applied on the inputs.
     */
    public ShaderOutput(IONode parent, String name, String semantic, String type, String typeRule) {
        super(parent,name, semantic, parent.shaderTypeLib.getType(type));
        this.typeRule = typeRule;
    }

    /**
     * Constructs a new ShaderOutput object with a name and a type.
     * @param parent the parent node for this ShaderOutput object.
     * @param name the name for the ShaderOutput.
     * @param semantic the semantic for the ShaderOutput (null is allowed).
     * @param type the type of the ShaderOutput.
     * @param typeRule a type rule can define the type of the output based on rules that will
     * be applied on the inputs.
     */
    public ShaderOutput(IONode parent, String name, String semantic, ShaderType type, String typeRule) {
        super(parent, name, semantic, type);
        this.typeRule = typeRule;
    }

    /**
     * Sets the type inference rule for this ShaderOutput object.
     * @param typeRule the rule for this ShaderOutput object.
     */
    public void setTypeRule(String typeRule) {
        this.typeRule = typeRule;
    }

    /**
     * Gets the type inference rule for this ShaderOutput object.
     * @return the type rule for this ShaderOutput object.
     */
    public String getTypeRule() {
        return typeRule;
    }

    /**
     * Sets the type for the shader input object.
     * @param type the type for the shader input object.
     */
    @Override
    public void setIOType(ShaderType type) {
        if (!type.equals(getIOType())) {
            super.setIOType(type);
            notifyListeners();

            /**
             * If the types do not match , the link will be broken.
             */
            ArrayList<ShaderInput> inputs = (ArrayList<ShaderInput>)this.nodes.clone();
            for (ShaderInput input : inputs) {
                if ( input.accepts(this))
                {
                    input.setConnectedInput(this);
                }else{
                    input.setConnectedInput(null);
                }
            }
        }
    }

    /**
     * Returns the connection string for this output node.
     * @return the connection string for this output node.
     */
    public String getConnectionString() {
        return connectionString;
    }

    /**
     * Sets the connection string for this output node.
     * @param connectionString the connection string for this output node.
     */
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    /**
     * Adds an input to the list of inputs.
     * @param input the input to add.
     */
    public void addInput(ShaderInput input) {
        this.nodes.add(input);
    }

    /**
     * Removes an input from the list of inputs.
     * @param input the input to remove.
     */
    public void removeInput(ShaderInput input) {
        this.nodes.remove(input);
    }

    /**
     * Returns the list of inputs for this ShaderOutput object.
     * @return the list of outputs.
     */
    Iterable<ShaderInput> getInputs() {
        return this.nodes;
    }

    /**
     * Checks if a  node is connected to this ShaderOutput object.
     * @param node the node to check.
     * @return true if the node is connected , false otherwise.
     */
    public boolean isOutputConnectedTo(IONode node) {
        for (ShaderInput input : this.nodes) {
            if (input.getParent() == node) {
                return true;
            }
        }
        return false;
    }



    /**
     * The reference of an input is created by asking the variable
     * name of the connected output port.
     * @return the variable name.
     */
    @Override
    public String getRef() {
        IONode parent = getParent();
        if (parent != null) {
            // if this is the only output, use the name of the parent id as
            // reference.
            if (parent.getNrOfOutputs() == 1 && !parent.isInputNode()) {
                return parent.getId();
            } else {
                return parent.getId() + parent.getSeparator() + this.getName();
            }
        } else {
            return "nullparent" + "_" + this.getName();
        }
    }

    /**
     * Returns the variable name for this output object.
     * @return
     */
    @Override
    public String getVar() {
        return getRef();
    }

    /**
     * Checks if this ShaderIO object is connected.
     * @return true if the ShaderIO object is connected, false otherwise.
     */
    @Override
    public boolean getConnected() {
        return this.nodes.size() > 0;
    }

    /**
     * Checks if this is an input node.
     * @return true if this is an input node, false otherwise.
     */
    @Override
    public boolean isInput() {
        return false;
    }

    /**
     * Checks if this is an output node.
     * @return true if this is an output node, false otherwise.
     */
    @Override
    public boolean isOutput() {
        return true;
    }

    /**
     * Checks if this input or output is compatible with the provided shader io object.
     * @return true if this object is compatible (can connect) with the provided object.
     */
    @Override
    public boolean accepts(ShaderIO io) {
        if (io.isInput()) {
            return io.accepts(this);
        } else {
            return false;
        }
    }

    /**
     * Remove all the inputs from this node.
     */
    public void removeAllInputs() {
        ArrayList<ShaderInput> inputsClone = (ArrayList) this.nodes.clone();
        for (ShaderInput input : inputsClone) {
            input.disconnectInput();
        }
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
