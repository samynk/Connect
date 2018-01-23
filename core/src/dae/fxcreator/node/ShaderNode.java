package dae.fxcreator.node;

import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.io.templates.Setting;
import java.util.List;

/**
 * This class describes the general form for a shader node. A shader node has
 * inputs/output and settings.
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ShaderNode extends IONode implements Cloneable {



    /**
     * Creates a new ShaderNode object with an id, a name and a type. The id is
     * used for code generation purposes, the name is used for user interface
     * purposes.
     *
     * @param id the id for the ShaderNode.
     * @param name the name for the ShaderNode.
     * @param type the type of node.
     * @param project the project this node is a part of.
     */
    public ShaderNode(String id, String name, String type, FXProject project) {
        super(id, name, project);
        super.setType(type);
        
    }



    @Override
    public Object clone() throws CloneNotSupportedException {
        ShaderNode node = new ShaderNode(this.getId(), this.getName(), this.getType(), super.getFXProject());
        node.setInputOutputEditable(this.isInputOutputEditable());
        node.setIcon(this.getIcon());
        // copy inputs
        List<ShaderInput> inputs = this.getInputs();
        for (ShaderInput input : inputs) {
            ShaderInput copy = new ShaderInput(node, input.getName(), input.getSemantic().getValue(), input.getType(), input.getAcceptTypeSet());
            copy.setConnectionString(input.getConnectionString());
            copy.setAnchor(input.getAnchor());
            node.addInput(copy);
        }
        List<ShaderOutput> outputs = this.getOutputs();
        for (ShaderOutput output : outputs) {
            ShaderOutput copy = new ShaderOutput(node, output.getName(), output.getSemantic().getValue(), output.getType(), output.getTypeRule());
            copy.setConnectionString(output.getConnectionString());
            copy.setAnchor(output.getAnchor());
            node.addOutput(copy);
        }

        List<ShaderInput> templateInputs = this.getTemplateInputs();
        for (ShaderInput input : templateInputs) {
            ShaderInput copy = new ShaderInput(node, input.getName(), input.getSemantic().getValue(), input.getType(), input.getAcceptTypeSet());
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
        node.setInputAnchor( getInputAnchor() );
        node.setOutputAnchor( getOutputAnchor() );
        return node;
    }
}
