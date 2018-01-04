package dae.fxcreator.node;

import dae.fxcreator.io.templates.Setting;
import dae.fxcreator.io.type.ShaderTypeLibrary;
import java.util.ArrayList;
import java.util.List;

/**
 * This class describes the general form for a shader node. A shader node
 * has inputs/output and settings.
 *
 * @author Koen
 */
public class ShaderNode extends IONode implements Cloneable {
    /**
     * The group type .
     */
    private String typeGroup;
    /**
     * The type name.
     */
    private String typeName;

    /**
     * Creates a new ShaderNode object with an id, a name and a type. The
     * id is used for code generation purposes, the name is used for user
     * interface purposes.
     * @param id the id for the ShaderNode.
     * @param name the name for the ShaderNode.
     * @param type the type of node.
     * @param library the type library to use.
     */
    public ShaderNode(String id, String name, String type, ShaderTypeLibrary library) {
        super(id, name,library);
        super.setType(type);
        if (type != null) {
            int dotIndex = type.indexOf('.');
            if (dotIndex > -1) {
                typeGroup = type.substring(0, dotIndex);
                typeName = type.substring(dotIndex + 1);
            }
        }
    }

    /**
     * Returns the group of the node type
     * @return the group of the type of this node.
     */
    public String getTypeGroup() {
        return typeGroup;
    }

    /**
     * Returns the name of the node type without the group.
     * @return the name of type.
     */
    public String getTypeName() {
        return typeName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ShaderNode node = new ShaderNode(this.getId(), this.getName(), this.getType(),super.shaderTypeLib);
        node.setInputOutputEditable(this.isInputOutputEditable());
        node.setIcon(this.getIcon());
        // copy inputs
        List<ShaderInput> inputs = this.getInputs();
        for (ShaderInput input : inputs) {
            ShaderInput copy = new ShaderInput(node, input.getName(), input.getSemantic().getValue(),input.getType(),input.getAcceptTypeSet());
            copy.setConnectionString(input.getConnectionString());
            node.addInput(copy);
        }
        List<ShaderOutput> outputs = this.getOutputs();
        for (ShaderOutput output : outputs) {
            ShaderOutput copy = new ShaderOutput(node, output.getName(),output.getSemantic().getValue(), output.getType(),output.getTypeRule());
            copy.setConnectionString(output.getConnectionString());
            node.addOutput(copy);
        }

        List<ShaderInput> templateInputs = this.getTemplateInputs();
        for ( ShaderInput input : templateInputs){
            ShaderInput copy = new ShaderInput(node, input.getName(), input.getSemantic().getValue(),input.getType(),input.getAcceptTypeSet());
            copy.setConnectionString(input.getConnectionString());
            node.addTemplateInput(copy);
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
     * Remove all the outputs in this node.
     */
    public void removeOutputs() {
        ArrayList<ShaderOutput> outputs = (ArrayList<ShaderOutput>)this.getOutputs().clone();
        for(ShaderOutput output : outputs){
            this.removeOutput(output);
        }
    }


}