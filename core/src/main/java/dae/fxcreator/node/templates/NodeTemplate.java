package dae.fxcreator.node.templates;

import dae.fxcreator.node.settings.Setting;
import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.node.project.ShaderStage;
import dae.fxcreator.io.type.IOTypeLibrary;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.IteratorNode;
import dae.fxcreator.node.NodeContainer;
import dae.fxcreator.node.SettingsGroup;
import dae.fxcreator.node.NodeInput;
import dae.fxcreator.node.NodeOutput;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is a template for the behaviour of a node in the user interface.
 * This class also determines the settings for a particular node and how these
 * settings are presented (for example : combobox, text, image file, constant).
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class NodeTemplate {

    /**
     * The node prototype that can be cloned to create new nodes.
     */
    private IONode nodePrototype;
    /**
     * The icon location for this template.
     */
    private final String icon;
    /**
     * The type library for the node template.
     */
    private IOTypeLibrary library;

    /**
     * Creates a new nodetemplate. The template also determines a prefix for the
     * created node.
     *
     * @param type the type of node.
     * @param prefix the prefix for id's and variable names for newly created
     * nodes.
     * @param icon the icon for the node.
     * @param ioEditable set this to true if the inputs/outputs of this node are
     * editable, false otherwise.
     * @param containerType the type of container, can be leaf (no children
     * allowed) or group. Default is leaf.
     * @param inputAnchor defines where to place the inputs.
     * @param outputAnchor defines where to place the outputs.
     */
    public NodeTemplate(String type, String prefix, String icon, boolean ioEditable, String containerType, String inputAnchor, String outputAnchor) {
        if (containerType == null || "leaf".equals(containerType)) {
            nodePrototype = new IONode(prefix, prefix, type, null);
        } else if ("iterator".equals(containerType)) {
            nodePrototype = new IteratorNode(prefix, type, null);
        } else if ("group".equals(containerType)) {
            nodePrototype = new NodeContainer(prefix, type);
        }
        nodePrototype.setInputOutputEditable(ioEditable);
        nodePrototype.setInputAnchor(inputAnchor);
        nodePrototype.setOutputAnchor(outputAnchor);
        this.icon = icon;
    }

    /**
     * Returns the type of the node without the group information.
     *
     * @return the type.
     */
    public String getTypeName() {
        return nodePrototype.getTypeName();
    }

    /**
     * Adds a setting to the list of settings for this template.
     *
     * @param group the group this setting belongs to.
     * @param setting the setting to add to the template.
     */
    public void addSetting(String group, Setting setting) {
        nodePrototype.addSetting(group, setting);
    }

    /**
     * Returns the shader node that is the prototype to create new nodes.
     *
     * @return the IONode prototype.
     */
    public IONode getShaderNode() {
        return nodePrototype;
    }

    /**
     * Add an output to the IONode.
     *
     * @param so the new output for the shader node.
     */
    public void addOutput(NodeOutput so) {
        nodePrototype.addOutput(so);
    }

    /**
     * Add an input to the ShaderNOde.
     *
     * @param si the new input for the shader node.
     */
    public void addInput(NodeInput si) {
        nodePrototype.addInput(si);
    }

    /**
     * Returns the location for the icon.
     *
     * @return the icon for this template.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Returns the type of the node.
     *
     * @return the type of the node.
     */
    public String getType() {
        return nodePrototype.getType();
    }

    /**
     * Creates a IONode, starting from the node prototype.
     *
     * @param project the project that the new node will belong to.
     * @return a new IONode, ready for use.
     */
    public IONode createShaderNode(FXProject project) {
        IONode result = new IONode(nodePrototype);
        result.setFXProject(project);
        result.setIcon(this.icon);
        return result;
    }

    /**
     * Creates a ShaderStage, starting from the node prototype.
     *
     * @param project the project to create a shader stage for.
     * @return a new ShaderStage, with an id that is set to the prefix for the
     * specific shaderstage.
     *
     */
    public ShaderStage createShaderStage(FXProject project) {
        ShaderStage stage = new ShaderStage(nodePrototype.getId(), nodePrototype.getType());
        stage.setFXProject(project);
        for (NodeInput input : nodePrototype.getInputs()) {
            NodeInput ci = new NodeInput(stage, input.getName(), input.getSemantic().getValue(), input.getIOType());
            stage.addInput(ci);
        }
        for (NodeOutput output : nodePrototype.getOutputs()) {
            NodeOutput co = new NodeOutput(stage, output.getName(), output.getSemantic().getValue(), output.getIOType(), output.getTypeRule());
            stage.addOutput(co);
        }
        stage.getInputNode().setPosition(20, 20);
        stage.getOutputNode().setPosition(300, 20);
        return stage;
    }

    /**
     * Adds the new settings in the template node to the settings in the shader
     * node.
     *
     * @param node the node to add the new settings to.
     */
    public void addNewSettings(IONode node) {
        for (SettingsGroup sg : nodePrototype.getSettingsGroups()) {
            for (Setting s : sg.getSettings()) {
                if (!node.hasSetting(sg.getName(), s.getId())) {
                    try {
                        Setting scopy = (Setting) s.clone();
                        node.addSetting(sg.getName(), scopy);
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(NodeTemplate.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    /**
     * Sets the list of accepted shadertypes in the node.
     *
     * @param currentNode the node to synchronize with the shader types.
     */
    public void addAcceptShaderTypes(IONode currentNode) {
        for (NodeInput si : nodePrototype.getInputs()) {
            NodeInput toSync = currentNode.findInput(si.getName());
            if (toSync != null) {
                toSync.setAcceptTypeSet(si.getAcceptTypeSet());
            }
        }
    }

    /**
     * Returns a String representation of this NodeTemplate
     *
     * @return the string representation of this node template.
     */
    @Override
    public String toString() {
        return nodePrototype.getName();
    }

    /**
     * Adds a templated input to list of template inputs.
     *
     * @param si the template input to add.
     */
    public void addTemplateInput(NodeInput si) {
        nodePrototype.addTemplateInput(si);
    }
}
