package dae.fxcreator.node;

import dae.fxcreator.io.NodeGroup;
import dae.fxcreator.io.NodeGroupWalker;
import dae.fxcreator.io.TypedNode;
import dae.fxcreator.io.templates.Setting;
import dae.fxcreator.io.type.ShaderTypeLibrary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents a grouping of nodes. This can be used to implement an
 * iterator block, or function.
 * @author Koen
 */
public class NodeContainer extends ShaderNode implements NodeGroup, TypedNode {
    private ArrayList<IONode> nodes = new ArrayList<IONode>();
    private ArrayList<ReferenceNode> referenceNodes =new ArrayList<ReferenceNode>();
    private HashMap<String, IONode> nodeMap = new HashMap<String, IONode>();
    private ShaderNode inputNode;
    private ShaderNode outputNode;

    private String subType;

    /**
     * Gets the subtype of this node container. The subtype is used to get the correct
     * method definition file.
     * @return the subtype.
     */
    public String getSubType() {
        return subType;
    }



    /**
     * Sets the subtype for this node container.
     * @param subType the new subtype.
     */
    public void setSubType(String subType) {
        this.subType = subType;
    }
    
    /**
     * Creates a new NodeContainer object.
     */
    public NodeContainer(String name, String type,ShaderTypeLibrary library)
    {
        super(name,name,type,library);
        inputNode = new ShaderNode("input", "input", "group.input",library);
        outputNode = new ShaderNode("output", "output", "group.output",library);
        inputNode.setSeparator(".");
        outputNode.setSeparator(".");

        nodeMap.put("input", inputNode);
        nodeMap.put("output", outputNode);

        inputNode.setRemovable(false);
        outputNode.setRemovable(false);
    }

    /**
     * Returns the input struct name for this node container.
     * @return the name for the input struct of the method.
     */
    public String getInputStructName(){
        return getMethodName() + "_INPUT";
    }

    /**
     * Returns the output struct name for this node container.
     * @return the name for the output struct of the method.
     */
    public String getOutputStructName(){
        return getMethodName() + "_OUTPUT";
    }

    /**
     * Returns the name of the method name.
     * @return the name of the method.
     */
    public String getMethodName(){
        return this.getSubType().replace(".", "_");
    }

    /**
     * Creates the node connections between the nodes.
     * This only works if all the nodes are loaded into the shader stage.
     */
    public void connectNodes() {
        for (IONode node : nodes) {
            connectNode(node);
        }
        connectNode(this.outputNode);
    }

    /**
     * Create the connections for a single node.
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
                    if (on != null)
                        output = on.findOutput(outputId);
                }
                if ( output != null)
                    input.setConnectedInput(output);
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        NodeContainer node = new NodeContainer("group", this.getType(),shaderTypeLib);
        node.setInputOutputEditable(this.isInputOutputEditable());
        // copy inputs
        List<ShaderInput> inputs = this.getInputs();
        for (ShaderInput input : inputs) {
            String semantic = null;
            if ( input.getSemantic().isValid())
                semantic= input.getSemantic().toString();
            ShaderInput copy = new ShaderInput(node, input.getName(), semantic,input.getType(),input.getAcceptTypeSet());
            copy.setConnectionString(input.getConnectionString());
            node.addInput(copy);
        }
        List<ShaderOutput> outputs = this.getOutputs();
        for (ShaderOutput output : outputs) {
            String semantic = null;
            if ( output.getSemantic().isValid())
                semantic= output.getSemantic().toString();
            ShaderOutput copy = new ShaderOutput(node, output.getName(),semantic, output.getType(),output.getTypeRule());
            copy.setConnectionString(output.getConnectionString());
            node.addOutput(copy);
        }
        for (IONode child : this.getNodes()){
            IONode clonedNode = (IONode)child.clone();
            node.addNode( clonedNode );
            clonedNode.setPosition(child.getPosition().x, child.getPosition().y);
        }

        List<SettingsGroup> settingsGroup = this.getSettingsGroups();
        for (SettingsGroup group : settingsGroup) {
            List<Setting> settings = group.getSettings();
            for (Setting setting : settings) {
                node.addSetting(group.getName(), (Setting) setting.clone());
            }
        }
        node.connectNodes();
        return node;
    }

     /**
     * Adds an input to this NodeContainer object and also adds it to the
     * inputNode object that is a member of this ShaderStage.
     * @param input the new input to add.
     */
    @Override
    public boolean addInput(ShaderInput input) {
        if (super.addInput(input)) {
            ShaderOutput o = new ShaderOutput(inputNode, input.getName(), input.getSemantic().getValue(), input.getType(),null);
            o.setConnectionString(input.getConnectionString());
            inputNode.addOutput(o);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes an input from this ShaderStage object.
     * @param input the input to remove.
     */
    @Override
    public void removeInput(ShaderInput input) {
        super.removeInput(input);
        inputNode.removeOutput(input.getName());
    }

     /**
     * Adds an output to this ShaderStage object and also adds it to the
     * outputNode object that is a member of this ShaderStage.
     * @param output the new input to add.
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
     * Removes an output from the output list and removes it as an input
     * from the output node.
     * @param output the ShaderOutput object to remove.
     */
    @Override
    public void removeOutput(ShaderOutput output) {
        super.removeOutput(output);
        outputNode.removeInput(output.getName());
    }


    /**
     * Adds a node to this NodeContainer object.
     * @param node the node to add.
     */
    public void addNode(IONode node) {
        nodes.add(node);
        nodeMap.put(node.getId(), node);
        node.setFXProject(getFXProject());
    }

    /**
     * Removes a node from this NodeContainer object.
     * @param node the node to remove.
     */
    public void removeNode(IONode node) {
        nodes.remove(node);
        nodeMap.remove(node.getId());
    }

     /**
     * Finds a node in the list of nodes.
     * @param id the id for the node.
     * @return the ShaderNode with the provided id, or null if no ShaderNode was
     * found.
     */
    public IONode findNode(String id) {
       return nodeMap.get(id);
    }

    public Iterable<IONode> getNodes() {
        return this.nodes;
    }

    public ShaderNode getInputNode() {
        return inputNode;
    }

    public ShaderNode getOutputNode() {
        return outputNode;
    }

    public Iterable<IONode> getSortedNodes() {
       NodeGroupWalker walker = new NodeGroupWalker(this);
       walker.collect();
       return walker.getNodes();
    }

     /**
     * Called when the name of a ShaderInput object has changed.
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

    public String getContainerType()
    {
        return "group";
    }

    public String getUniqueId(String prefix) {
        int i = 1;
        String id;
        do{
            id = prefix + i;
        }while (this.nodeMap.containsKey(id));
        return id;
    }

    /**
     * Add a node that is a reference to another node
     * @param rn the reference node to add.
     */
    public void addReferenceNode(ReferenceNode rn){
        referenceNodes.add(rn);
    }
    /**
     * Removes the reference node from the list of reference nodes.
     */
    public void removeReferenceNode(ReferenceNode rn){
        referenceNodes.remove(rn);
    }
    /**
     * Returns the reference nodes.
     */
    public Iterable<ReferenceNode> getReferenceNodes(){
        return referenceNodes;
    }
}
