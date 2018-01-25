package dae.fxcreator.node.project;

import dae.fxcreator.node.NodeGroup;
import dae.fxcreator.node.NodeGroupWalker;
import dae.fxcreator.node.TypedNode;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.ReferenceNode;
import dae.fxcreator.node.ShaderIO;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderNode;
import dae.fxcreator.node.ShaderOutput;
import dae.fxcreator.util.Key;
import dae.fxcreator.util.Label;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Koen
 */
public class ShaderStage extends IONode implements NodeGroup, TypedNode, Key, Label {

    private final ArrayList<IONode> nodes = new ArrayList<>();
    private final ArrayList<ReferenceNode> referenceNodes = new ArrayList<>();
    private final HashMap<String, IONode> nodeMap = new HashMap<>();
    private final ShaderNode inputNode;
    private final ShaderNode outputNode;
    /**
     * A possible subtype for this IONode.
     */
    private String subType = "";

    /**
     * Creates a new ShaderStage with a name and a type. The name will be used
     * as a name for a shader method. The type is used to differentiate between
     * a vertex, pixel or geometry shader stage.
     *
     * @param name the name for the shader stage.
     * @param type the type of the shader stage.
     */
    public ShaderStage(String name, String type) {
        super(name, name, null);
        super.setType(type);

        inputNode = new ShaderNode("input", "input", "group.input", null);
        outputNode = new ShaderNode("output", "output", "group.output", null);
        inputNode.setSeparator(".");
        outputNode.setSeparator(".");
        inputNode.setRemovable(false);
        outputNode.setRemovable(false);

        inputNode.setInputNode(true);
        outputNode.setOutputNode(true);

        nodeMap.put("input", inputNode);
        nodeMap.put("output", outputNode);
    }

    /**
     * Return the key for storage into hash maps.
     *
     * @return the key for this shaderstage.
     */
    @Override
    public String getKey() {
        return this.getId();
    }

    @Override
    public void setLabel(String name) {
        this.setName(name);
    }

    @Override
    public String getLabel() {
        return this.getName();
    }

    /**
     * Checks if the ShaderStage is the same as another stage. Only checks for
     * id and type.
     *
     * @param o the type to check.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof ShaderStage) {
            ShaderStage s = (ShaderStage) o;
            return s.getType().equals(getType()) && s.getId().equals(getId());
        } else {
            return false;
        }
    }

    /**
     * Check if the inputs and outputs of this node can be edited. Default true
     * for a ShaderStage object.
     *
     * @return true if inputs and outputs of this node can be edited, false
     * otherwise.
     */
    @Override
    public boolean isInputOutputEditable() {
        return true;
    }

    /**
     * Returns a suitable name for the input struct.
     *
     * @return a suitable name for the input struct.
     */
    public String getInputStructName() {
        if (this.isInputStructSet()) {
            return getInputStruct().getId();
        } else {
            return this.getId() + "_INPUT";
        }
    }

    /**
     * Returns a suitable name for the output struct.
     *
     * @return a suitable name for the output struct.
     */
    public String getOutputStructName() {
        if (this.isOutputStructSet()) {
            return getOutputStruct().getId();
        } else {
            return this.getId() + "_OUTPUT";
        }
    }

    /**
     * Adds an input to this ShaderStage object and also adds it to the
     * inputNode object that is a member of this ShaderStage.
     *
     * @param input the new input to add.
     * @return true if the input was added, false otherwise.
     */
    @Override
    public boolean addInput(ShaderInput input) {
        if (super.addInput(input)) {
            ShaderOutput o = new ShaderOutput(inputNode, input.getName(), input.getSemantic().getValue(), input.getType(), null);
            o.setConnectionString(input.getConnectionString());
            inputNode.addOutput(o);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes an input from this ShaderStage object.
     *
     * @param input the input to remove.
     */
    @Override
    public void removeInput(ShaderInput input) {
        super.removeInput(input);
        inputNode.removeOutput(input.getName());

    }

    /**
     * Returns the input node associated with the ShaderStage.
     *
     * @return the node that defines the inputs for the stage.
     */
    @Override
    public ShaderNode getInputNode() {
        return inputNode;
    }

    /**
     * Adds an output to this ShaderStage object and also adds it to the
     * outputNode object that is a member of this ShaderStage.
     *
     * @param output the new input to add.
     * @return true if the output was added, false otherwise.
     */
    @Override
    public boolean addOutput(ShaderOutput output) {
        if (super.addOutput(output)) {
            ShaderInput i = new ShaderInput(outputNode, output.getName(), output.getSemantic().getValue(), output.getType());
            i.setConnectionString(output.getConnectionString());
            outputNode.addInput(i);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes an output from the output list and removes it as an input from
     * the output node.
     *
     * @param output the ShaderOutput object to remove.
     */
    @Override
    public void removeOutput(ShaderOutput output) {
        super.removeOutput(output);
        outputNode.removeInput(output.getName());
    }

    /**
     * Returns the output node associated with the ShaderStage.
     *
     * @return the node that defines the outputs for the stage.
     */
    @Override
    public ShaderNode getOutputNode() {
        return outputNode;
    }

    /**
     * Adds a node to this ShaderStage object.
     *
     * @param node the node to add.
     */
    @Override
    public void addNode(IONode node) {
        nodes.add(node);
        nodeMap.put(node.getId(), node);
        node.setFXProject(getFXProject());
    }

    /**
     * Removes a node from this ShaderStage object.
     *
     * @param node the node to remove.
     */
    @Override
    public void removeNode(IONode node) {
        nodes.remove(node);
        nodeMap.remove(node.getId());
    }

    /**
     * Returns the list of nodes that are present in this shader stage.
     *
     * @return the list of ShaderNode objects.
     */
    @Override
    public Iterable<IONode> getNodes() {
        return nodes;
    }

    /**
     * Finds a node in the list of nodes.
     *
     * @param key the key for the node.
     * @return the ShaderNode with the provided id, or null if no ShaderNode was
     * found.
     */
    @Override
    public IONode findNode(String key) {
        return nodeMap.get(key);
    }

    /**
     * Creates the node connections between the nodes. This only works if all
     * the nodes are loaded into the shader stage.
     */
    @Override
    public void connectNodes() {
        for (IONode node : nodes) {
            connectNode(node);
        }
        connectNode(this.outputNode);
    }

    /**
     * Create the connections for a single node.
     *
     * @param node the node to connect.
     */
    private void connectNode(IONode node) {
        for (ShaderInput input : node.getInputs()) {
            String connection = input.getConnectionString();
            if (connection != null && connection.length() > 0) {
                int dotIndex = connection.indexOf('.');
                String nodeId = connection.substring(0, dotIndex);
                String outputId = connection.substring(dotIndex + 1);

                ShaderOutput output = null;
                if (nodeId.equals("input")) {
                    output = this.inputNode.findOutput(outputId);
                } else {
                    IONode on = this.findNode(nodeId);
                    if (on != null) {
                        output = on.findOutput(outputId);
                    }
                }
                if (output != null) {
                    input.setConnectedInput(output);
                }
            }
        }
    }

    /**
     * Returns a list of nodes that are sorted by dependency. In other words if
     * the outputs of a node are used by another node, the output node will be
     * first in the list, the node that consumes the outputs will be placed
     * after this first node.
     *
     * @return a sorted list of ShaderNode objects.
     */
    @Override
    public Iterable<IONode> getSortedNodes() {
        NodeGroupWalker walker = new NodeGroupWalker(this);
        walker.collect();
        return walker.getNodes();
    }

    /**
     * Called when the name of a ShaderInput object has changed.
     *
     * @param oldName the old name of the input.
     * @param input the ShaderInput that was changed.
     */
    @Override
    public void remapInputName(String oldName, ShaderInput input) {
        super.remapInputName(oldName, input);
        ShaderOutput output = inputNode.findOutput(oldName);
        output.setName(input.getName());
        inputNode.remapOutputName(oldName, output);
    }

    /**
     * Called when the semantic of a ShaderInput object has changed.
     *
     * @param oldSemantic the old semantic of the input.
     * @param input the ShaderInput that was changed.
     */
    @Override
    public void remapInputSemantic(String oldSemantic, ShaderInput input) {
        super.remapInputSemantic(oldSemantic, input);
        ShaderOutput output = inputNode.findOutput(input.getName());
        output.setSemantic(input.getSemantic().getValue());
        inputNode.remapOutputSemantic(oldSemantic, output);
    }

    /**
     * Called when the name of a ShaderOutput object has changed.
     *
     * @param oldName the old name of the output.
     * @param output the ShaderOutput that was changed.
     */
    @Override
    public void remapOutputName(String oldName, ShaderOutput output) {
        super.remapOutputName(oldName, output);
        ShaderInput input = outputNode.findInput(oldName);
        input.setName(output.getName());
        outputNode.remapInputName(oldName, input);
    }

    /**
     * Called when the semantic of a ShaderOutput object has changed.
     *
     * @param oldSemantic the old semantic of the output.
     * @param output the ShaderOutput that was changed.
     */
    @Override
    public void remapOutputSemantic(String oldSemantic, ShaderOutput output) {
        super.remapOutputSemantic(oldSemantic, output);
        ShaderInput input = outputNode.findInput(output.getName());
        input.setSemantic(output.getSemantic().getValue());
        outputNode.remapInputSemantic(oldSemantic, input);
    }

    /**
     * Called when the type of an input or output changes.
     *
     * @param io the input or output that was changed.
     */
    @Override
    public void typeChanged(ShaderIO io) {
        super.typeChanged(io);
        if (io.isInput()) {
            ShaderOutput output = inputNode.findOutput(io.getName());
            output.setType(io.getType());
        } else {
            ShaderInput input = outputNode.findInput(io.getName());
            input.setType(io.getType());
        }
    }

    @Override
    public String getContainerType() {
        return "ShaderStage";
    }

    /**
     * Gets the subtype of this node container. The subtype is used to get the
     * correct method definition file.
     *
     * @return the subtype.
     */
    @Override
    public String getSubType() {
        return subType;
    }

    /**
     * Sets the subtype for this node container.
     *
     * @param subType the new subtype.
     */
    @Override
    public void setSubType(String subType) {
        this.subType = subType;
    }

    @Override
    public String getUniqueId(String prefix) {
        int i = 1;
        String id;
        do {
            id = prefix + (i++);
        } while (this.getFXProject().hasId(id));
        return id;
    }

    /**
     * Add a node that is a reference to another node
     *
     * @param rn the reference node to add.
     */
    @Override
    public void addReferenceNode(ReferenceNode rn) {
        referenceNodes.add(rn);
    }

    /**
     * Removes the reference node from the list of reference nodes.
     *
     * @param rn the ReferenceNode to remove.
     */
    @Override
    public void removeReferenceNode(ReferenceNode rn) {
        referenceNodes.remove(rn);
    }

    /**
     * Returns the reference nodes.
     *
     * @return the list of ReferenceNodes.
     */
    @Override
    public Iterable<ReferenceNode> getReferenceNodes() {
        return referenceNodes;
    }

    /**
     * Checks if this stage has a node with the given id.
     *
     * @param id the id to check for.
     * @return true if a node exists with the given id, false otherwise.
     */
    public boolean hasId(String id) {
        return this.nodeMap.containsKey(id);
    }

}
