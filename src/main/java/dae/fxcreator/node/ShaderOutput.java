package dae.fxcreator.node;

import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Koen
 */
public class ShaderOutput implements ShaderIO {

    private String name;
    private ShaderType type;
    /**
     * The parent node of this ShaderOutput node.
     */
    private IONode parent;
    /**
     * The nodes that are connected to this output.
     */
    private ArrayList<ShaderInput> nodes = new ArrayList<ShaderInput>();
    /**
     * This property allows for outputs that have an external source, such as
     * variables that are defined by semantics.
     */
    private String connectionString;
    /**
     * The semantic for this shader output. The semantic is useful for
     * outputs of global nodes and input and output structs for shader stages.
     */
    private Semantic semantic = new Semantic();
    /**
     * The list of change listeners
     */
    private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
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
     */
    public ShaderOutput(IONode parent, String name, String semantic, String type, String typeRule) {
        this.parent = parent;
        this.type = parent.shaderTypeLib.getType(type);
        this.name = name;
        this.semantic.setValue(semantic);
        this.typeRule = typeRule;
    }

    /**
     * Constructs a new ShaderOutput object with a name and a type.
     * @param parent the parent node for this ShaderOutput object.
     * @param name the name for the ShaderOutput.
     * @param semantic the semantic for the ShaderOutput (null is allowed).
     * @param type the type of the ShaderOutput.
     */
    public ShaderOutput(IONode parent, String name, String semantic, ShaderType type, String typeRule) {
        this.parent = parent;
        this.type = type;
        this.name = name;
        this.semantic.setValue(semantic);
        this.typeRule = typeRule;
    }
    
    /**
     * Checks if the type of this input is an actual value.
     * @return true if this input connects to a value type, false otherwise.
     */
    public boolean getValueType(){
        return type != null && type.isValueType();
    }

    /**
     * Sets the type inference rule for this ShaderOutput object.
     * @param rule the rule for this ShaderOutput object.
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
     * Returns the parent node for this shader.
     * @return the parent node.
     */
    public IONode getParent() {
        return parent;
    }

    /**
     * Sets the parent node of this ShaderOutput object.
     * @param node the node to set as parent.
     */
    public void setParent(IONode node) {
        this.parent = node;
    }

    /**
     * Returns the name for the ShaderOutput object.
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
                parent.remapOutputName(oldName, this);
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
        if (!type.equals(this.type)) {
            this.type = type;
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
     * Returns the semantic for this output object.
     * @return the semantic for this output object.
     */
    public Semantic getSemantic() {
        return semantic;
    }

    /**
     * Sets the semantic for this output object.
     * @param semantic the semantic for the output object.
     */
    public void setSemantic(String semantic) {
        if (!semantic.equals(this.semantic.getValue())) {
            String oldValue = this.semantic.getValue();
            this.semantic.setValue(semantic);
            parent.remapOutputSemantic(oldValue, this);
        }
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
     * Returns a label for this ShaderInput object.
     */
    public String getLabel() {
        if (semantic.isValid()) {
            return this.name + " : " + semantic;
        } else {
            return this.name;
        }
    }

    /**
     * The reference of an input is created by asking the variable
     * name of the connected output port.
     * @return the variable name.
     */
    public String getRef() {
        if (this.parent != null) {
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
    public String getVar() {
        return getRef();
    }

    /**
     * Checks if this ShaderIO object is connected.
     * @return true if the ShaderIO object is connected, false otherwise.
     */
    public boolean getConnected() {
        return this.nodes.size() > 0;
    }

    /**
     * Checks if this is an input node.
     * @return true if this is an input node, false otherwise.
     */
    public boolean isInput() {
        return false;
    }

    /**
     * Checks if this is an output node.
     * @return true if this is an output node, false otherwise.
     */
    public boolean isOutput() {
        return true;
    }

    /**
     * Checks if this input or output is compatible with the provided shader io object.
     * @return true if this object is compatible (can connect) with the provided object.
     */
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
