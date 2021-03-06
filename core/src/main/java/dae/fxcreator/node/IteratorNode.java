package dae.fxcreator.node;

import dae.fxcreator.node.events.SettingListener;
import dae.fxcreator.node.settings.Setting;
import dae.fxcreator.io.type.IOTypeLibrary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is a specialization of the NodeContainer class.
 * This specialization is needed because a loop variable has
 * to be introduced.
 * @author  Koen Samyn (samyn.koen@gmail.com)
 */
public class IteratorNode extends IONode implements NodeGroup,SettingListener {
    private final ArrayList<IONode> nodes = new ArrayList<>();
    private final ArrayList<ReferenceNode> referenceNodes = new ArrayList<>();
    private final HashMap<String, IONode> nodeMap = new HashMap<>();
    private final IONode inputNode;
    private final IONode outputNode;


    private NodeOutput arrayVariable;
    private NodeOutput loopVariable;
    private final IONode loopNode;
    private IOStruct outputStruct;

    private String subType="";



    /**
     * Creates a new IteratorNode object with an id and type.
     * @param id the id for the iterator node.
     * @param type the type of the iterator node.
     * @param library the shader type library.
     */
    public IteratorNode(String id, String type, IOTypeLibrary library) {
        super(id, id, type,null);
        this.addSettingListener(this);

        inputNode = new IONode("input", "input", "group.input",null);
        outputNode = new IONode("output", "output", "group.output",null);
        inputNode.setSeparator(".");
        outputNode.setSeparator(".");

        nodeMap.put(inputNode.getName(),inputNode);
        nodeMap.put(outputNode.getName(), outputNode);
        
        loopNode = new IONode("loop", "loop", "group.loop", null);
        loopNode.setSeparator(".");
        loopNode.setPosition(20, 20);

        //loopVariable = new ShaderOutput(loopNode,"loopVar",null, library.getType("FLOAT"),"");
        //loopNode.addOutput(loopVariable);

        getInputNode().setPosition(20, 100);
        getOutputNode().setPosition(400,20);

        addNode(loopNode);
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
        for (NodeInput input : node.getInputs()) {
            String connection = input.getConnectionString();
            if (connection != null && connection.length() > 0) {
                int dotIndex = connection.indexOf('.');
                String nodeId = connection.substring(0, dotIndex);
                String outputId = connection.substring(dotIndex + 1);

                NodeOutput output;
                if (nodeId.equals("input")) {
                    output = this.inputNode.findOutput(outputId);
                } else {
                    IONode on = this.findNode(nodeId);
                    output = on.findOutput(outputId);
                }
                input.setConnectedInput(output);
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        IteratorNode node = new IteratorNode(this.getId(), this.getType(),shaderTypeLib);
        node.setInputOutputEditable(this.isInputOutputEditable());
        // copy inputs
        List<NodeInput> inputs = this.getInputs();
        for (NodeInput input : inputs) {
            NodeInput copy = new NodeInput(node, input.getName(), input.getSemantic().getValue(),input.getIOType(),input.getAcceptTypeSet());
            copy.setConnectionString(input.getConnectionString());
            node.addInput(copy);
        }
        List<NodeOutput> outputs = this.getOutputs();
        for (NodeOutput output : outputs) {
            NodeOutput copy = new NodeOutput(node, output.getName(),output.getSemantic().getValue(), output.getIOType(),output.getTypeRule());
            copy.setConnectionString(output.getConnectionString());
            node.addOutput(copy);
        }

        List<SettingsGroup> settingsGroup = this.getSettingsGroups();
        for (SettingsGroup group : settingsGroup) {
            List<Setting> settings = group.getSettings();
            for (Setting setting : settings) {
                node.addSetting(group.getName(), (Setting) setting.clone());
            }
        }
        return node;
    }

    /**
     * Adds an input to this ShaderStage object and also adds it to the
     * inputNode object that is a member of this ShaderStage.
     * @param input the new input to add.
     */
    @Override
    public boolean addInput(NodeInput input) {
        boolean success = super.addInput(input);
        
        /*
        if (success && input.getType() == shaderTypeLib.getType("ARRAY"));
        {
            // input will be remapped as an output of the input node.
           arrayVariable = loopNode.findOutput("loopVar");
        }
        */
        return success;
    }

    /**
     * Called when the type of an input or output changes. This call is used to
     * change the output of the input node to the correct singular type.
     * @param io the input or output that was changed.
     */
    @Override
    public void typeChanged(IOPort io) {
        super.typeChanged(io);
        if (io.isInput()) {
            NodeOutput output = getInputNode().findOutput(io.getName());
            output.setIOType(io.getIOType());
        } else {
            NodeInput input = getOutputNode().findInput(io.getName());
            input.setIOType(io.getIOType());
        }
    }

    /**
     * Check to see if the type setting for the loop has changed. If so,
     * the input node has to be changed.
     * @param node the node that has changed (this node).
     * @param s the setting that has changed
     */
    public void settingChanged(IONode node, Setting s) {
        if (s.getGroup().equals("Iterator")) {
            if (s.getId().equals("Type")) {
                Object type = s.getSettingValueAsObject();
                loopNode.removeConnections();
                loopNode.removeOutputs();
                System.out.println("Type is :"+type);
                if ( type instanceof IOStruct) {
                    IOStruct struct = (IOStruct)type;
                    outputStruct = struct;
                    for (StructField field : struct.getFields()){
                        String semantic = "";
                        if ( field.getSemantic().isValid()){
                            semantic = field.getSemantic().toString();
                        }
                        NodeOutput output = new NodeOutput(loopNode,field.getName(),semantic,field.getType(),"");
                        loopNode.addOutput(output);
                    }
                }
                else if (type instanceof String ){
                    System.out.println("Type is a ShaderType !");
                    outputStruct = null;
                    loopVariable.setIOType(shaderTypeLib.getType(type.toString()));
                    loopNode.addOutput(loopVariable);
                }
            }
        }
    }

    public boolean isLoopNodeOutputStructSet(){
        return this.outputStruct != null;
    }

    /**
     * Removes an input from this ShaderStage object.
     * @param input the input to remove.
     */
    @Override
    public void removeInput(NodeInput input) {
        super.removeInput(input);
        inputNode.removeOutput(input.getName());
    }

     /**
     * Adds an output to this ShaderStage object and also adds it to the
     * outputNode object that is a member of this ShaderStage.
     * @param output the new input to add.
     */
    @Override
    public boolean addOutput(NodeOutput output) {
        if (super.addOutput(output)) {
            NodeInput i = new NodeInput(outputNode, output.getName(), output.getSemantic().getValue(), output.getIOType());
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
    public void removeOutput(NodeOutput output) {
        super.removeOutput(output);
        outputNode.removeInput(output.getName());
    }


    /**
     * Adds a node to this NodeContainer object.
     * @param node the node to add.
     */
    @Override
    public final void addNode(IONode node) {
        nodes.add(node);
        nodeMap.put(node.getId(), node);
        node.setFXProject(getFXProject());
    }

    /**
     * Removes a node from this NodeContainer object.
     * @param node the node to remove.
     */
    @Override
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
    @Override
    public IONode findNode(String id) {
       return nodeMap.get(id);
    }

    /**
     * Returns an Iterable to loop over the nodes.
     * @return an Iterable to loop over the nodes.
     */
    @Override
    public Iterable<IONode> getNodes() {
        return this.nodes;
    }

    /**
     * Returns the shader node that is the input of this block.
     * @return the input node object.
     */
    @Override
    public final IONode getInputNode() {
        return inputNode;
    }

    /**
     * Returns the shader node that is the output of this block.
     * @return the output node object.
     */
    @Override
    public final IONode getOutputNode() {
        return outputNode;
    }

    @Override
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
    public void remapInputName(String oldName, NodeInput input) {
        super.remapInputName(oldName, input);
        NodeOutput output = inputNode.findOutput(oldName);
        output.setName(input.getName());
        inputNode.remapOutputName(oldName, output);
    }

    /**
     * Called when the semantic of a ShaderInput object has changed.
     * @param oldSemantic the old semantic of the input.
     * @param input the ShaderInput that was changed.
     */
    @Override
    public void remapInputSemantic(String oldSemantic, NodeInput input) {
        super.remapInputSemantic(oldSemantic, input);
        NodeOutput output = inputNode.findOutput(input.getName());
        output.setSemantic(input.getSemantic().getValue());
        inputNode.remapOutputSemantic(oldSemantic, output);
    }

    /**
     * Called when the name of a ShaderOutput object has changed.
     * @param oldName the old name of the output.
     * @param output the ShaderOutput that was changed.
     */
    @Override
    public void remapOutputName(String oldName, NodeOutput output) {
        super.remapOutputName(oldName, output);
        NodeInput input = outputNode.findInput(oldName);
        input.setName(output.getName());
        outputNode.remapInputName(oldName, input);
    }

    /**
     * Called when the semantic of a ShaderOutput object has changed.
     * @param oldSemantic the old semantic of the output.
     * @param output the ShaderOutput that was changed.
     */
    @Override
    public void remapOutputSemantic(String oldSemantic, NodeOutput output) {
        super.remapOutputSemantic(oldSemantic, output);
        NodeInput input = outputNode.findInput(output.getName());
        input.setSemantic(output.getSemantic().getValue());
        outputNode.remapInputSemantic(oldSemantic, input);
    }

    @Override
    public String getContainerType() {
        return "iterator";
    }

     /**
     * Gets the subtype of this node container. The subtype is used to get the correct
     * method definition file.
     * @return the subtype.
     */
    @Override
    public String getSubType() {
       return subType;
    }

     /**
     * Sets the subtype for this node container.
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
        do{
            id = prefix + i;
        }while (this.nodeMap.containsKey(id));
        return id;
    }

    /**
     * Add a node that is a reference to another node
     * @param rn the reference node to add.
     */
    @Override
    public void addReferenceNode(ReferenceNode rn){
        referenceNodes.add(rn);
    }
    /**
     * Removes the reference node from the list of reference nodes.
     */
    @Override
    public void removeReferenceNode(ReferenceNode rn){
        referenceNodes.remove(rn);
    }
    /**
     * Returns the reference nodes.
     */
    @Override
    public Iterable<ReferenceNode> getReferenceNodes(){
        return referenceNodes;
    }
}
