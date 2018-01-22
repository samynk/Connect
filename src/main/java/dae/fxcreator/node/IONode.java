package dae.fxcreator.node;

import dae.fxcreator.io.FXProject;
import dae.fxcreator.io.FXProjectType;
import dae.fxcreator.io.NodeGroupWalker;
import dae.fxcreator.io.TypedNode;
import dae.fxcreator.io.events.IOListener;
import dae.fxcreator.io.events.SettingListener;
import dae.fxcreator.io.templates.Setting;
import dae.fxcreator.io.templates.TextSetting;
import dae.fxcreator.io.type.ShaderTypeLibrary;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import javax.swing.tree.TreeNode;

/**
 * This class describes the properties of an Input Output node.
 *
 * @author Koen
 */
public class IONode implements TreeNode, TypedNode, StructListener, Cloneable {

    /**
     * The ShaderTypeLibrary with the type information.
     */
    protected ShaderTypeLibrary shaderTypeLib;
    /**
     * The ShaderStruct that describes the input nodes.
     */
    private ShaderStruct inputStruct;
    /**
     * The ShaderStruct that describes the output node.
     */
    private ShaderStruct outputStruct;
    /**
     * The inputs for this ShaderNode.
     */
    private final ArrayList<ShaderInput> inputs = new ArrayList<>();
    /**
     * The hashmap for the inputs of the shader node by name.
     */
    private final HashMap<String, ShaderInput> inputMap = new HashMap<>();
    /**
     * The hashmap for the inputs of the shader node by semantic.
     */
    private final HashMap<String, ShaderInput> semanticInputMap = new HashMap<>();
    /**
     * The outputs for this ShaderNode.
     */
    private final ArrayList<ShaderOutput> outputs = new ArrayList<>();
    /**
     * The hashmap for the outputs of the shader node.
     */
    private final HashMap<String, ShaderOutput> outputMap = new HashMap<>();
    /**
     * The hashmap for the inputs of the shader node by semantic.
     */
    private final HashMap<String, ShaderOutput> semanticOutputMap = new HashMap<>();

    /**
     * The list of templated inputs.
     */
    private final ArrayList<ShaderInput> templateInputs = new ArrayList<>();

    /**
     * The ui name for this node.
     */
    private String name;
    /**
     * The unique id for this node.
     */
    private String id;
    /**
     * The position for this node in the user interface.
     */
    private final Point position = new Point();
    /**
     * The type of shader node.
     */
    private String type;
    /**
     * The separator for variable name generation for this node.
     */
    private String separator = "_";
    /**
     * ShaderSettings can be grouped together.
     */
    private final ArrayList<SettingsGroup> settingsGroups = new ArrayList<>();
    /**
     * The hashmap with the SettingsGroups and their names.
     */
    private final HashMap<String, SettingsGroup> settingsGroupsMap = new HashMap<>();
    /**
     * The list with settings that need to be visualized.
     */
    private final ArrayList<Setting> visibleSettings = new ArrayList<>();
    /**
     * The ArrayList with setting listeners.
     */
    private final ArrayList<SettingListener> settingListeners = new ArrayList<>(1);
    /**
     * The editable state of the input outputs.
     */
    private boolean ioEditable = false;
    /**
     * The FXProject object this node is a part of.
     */
    private FXProject fxProject;
    /**
     * The IO listener objects for this node.
     */
    private final ArrayList<IOListener> ioListeners = new ArrayList<>();
    /**
     * The internal id for the node.
     */
    private final int internalID;
    /**
     * Is this node the input node for a collection of nodes.
     */
    private boolean inputNode = false;
    /**
     * Is this node the output node for a collection of nodes.
     */
    private boolean outputNode = false;
    /**
     *
     */
    private static int idcount = 0;
    /**
     * by default , nodes are removable, but sometimes nodes should not be
     * removed.
     */
    private boolean removable = true;
    /**
     * Generate an output pin for this io node. The output pin can be used to
     * specify execution order.
     */
    private boolean outputPin = false;

    /**
     * The default input anchor.
     */
    private IOAnchor inputAnchor = IOAnchor.NORTHWEST;
    /**
     * The default output anchor.
     */
    private IOAnchor outputAnchor = IOAnchor.NORTHEAST;

    /**
     * Creates a new IONode object with an id and a name. The id will be used
     * for code generation purposes and the name will be used for display in the
     * user interface.
     *
     * @param id the id of the node.
     * @param name the name for the node.
     * @param project the project this node is part of.
     */
    public IONode(String id, String name, FXProject project) {
        this.id = id;
        this.name = name;

        addSetting("Node", new TextSetting("id", "node id", id));
        addSetting("Node", new TextSetting("name", "node name", name));

        setFXProject(project);

        internalID = idcount++;
    }

    public int getInternalID() {
        return internalID;
    }

    /**
     * Creates a clone of this IONOde object.
     *
     * @return the cloned object.
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        IONode node = new IONode(this.getId(), this.getName(), fxProject);
        node.setInputOutputEditable(this.isInputOutputEditable());
        node.setIcon(this.getIcon());
        // copy inputs
        List<ShaderInput> cinputs = this.getInputs();
        for (ShaderInput input : cinputs) {
            ShaderInput copy = new ShaderInput(node, input.getName(), input.getSemantic().getValue(), input.getType(), input.getAcceptTypeSet());
            copy.setConnectionString(input.getConnectionString());
            node.addInput(copy);
        }
        List<ShaderOutput> coutputs = this.getOutputs();
        for (ShaderOutput output : coutputs) {
            ShaderOutput copy = new ShaderOutput(node, output.getName(), output.getSemantic().getValue(), output.getType(), output.getTypeRule());
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
        node.outputPin = outputPin;
        return node;
    }

    /**
     * Remove all the listeners from this node.
     */
    public void clearListeners() {
        ioListeners.clear();
        settingListeners.clear();
    }

    /**
     * Returns the ShaderTypeLibrary that is used by this node.
     *
     * @return the ShaderTypeLibrary object.
     */
    public ShaderTypeLibrary getShaderTypeLibrary() {
        return shaderTypeLib;
    }

    /**
     * Check if the inputs and outputs of this node can be edited. Default
     * false.
     *
     * @return true if inputs and outputs of this node can be edited, false
     * otherwise.
     */
    public boolean isInputOutputEditable() {
        return ioEditable;
    }

    /**
     * Sets the editability of the inputs and outputs of the node.
     *
     * @param editable set to true if the inputs / outputs are editable,false
     * otherwise.
     */
    public void setInputOutputEditable(boolean editable) {
        this.ioEditable = editable;
    }

    /**
     * Checks if the input struct is set.
     *
     * @return true if the input struct is set, false otherwise.
     */
    public boolean isInputStructSet() {
        return inputStruct != null;
    }

    /**
     * Checks if the input struct is set.
     *
     * @return true if the input struct is set, false otherwise.
     */
    public boolean getInputStructSet() {
        return inputStruct != null;
    }

    /**
     * Returns the input struct.
     *
     * @return the input struct.
     */
    public ShaderStruct getInputStruct() {
        return inputStruct;
    }

    /**
     * Checks if the output struct is set.
     *
     * @return true if the output struct is set, false otherwise.
     */
    public boolean isOutputStructSet() {
        return outputStruct != null;
    }

    /**
     * Checks if the output struct is set.
     *
     * @return true if the output struct is set, false otherwise.
     */
    public boolean getOutputStructSet() {
        return outputStruct != null;
    }

    /**
     * Returns the output struct.
     *
     * @return the output struct.
     */
    public ShaderStruct getOutputStruct() {
        return outputStruct;
    }

    /**
     * Adds an input to this node.
     *
     * @param input the input to add.
     */
    public boolean addInput(ShaderInput input) {
        if (!inputMap.containsKey(input.getName())) {
            input.setParent(this);
            inputs.add(input);
            inputMap.put(input.getName(), input);
            if (input.getSemantic().isValid()) {
                semanticInputMap.put(input.getSemantic().getValue(), input);
            }
            notifyIOAdded(input.getName());
            return true;
        } else {
            // take over the values from the input.
            ShaderInput current = inputMap.get(input.getName());
            current.setConnectionString(input.getConnectionString());
            current.setAcceptTypeSet(input.getAcceptTypeSet());
            current.setType(input.getType());

            if (!current.getSemantic().equals(input.getSemantic())) {
                String oldsemantic = current.getSemantic().toString();
                current.setSemantic(input.getSemantic().toString());
                this.remapInputSemantic(oldsemantic, current);
            }
            return true;
        }
    }

    /**
     * Returns the number of inputs in this node.
     *
     * @return the number of inputs.
     */
    public int getNrOfInputs() {
        return inputs.size();
    }

    /**
     * Finds an input in this node.
     *
     * @param inputName the name for the input.
     */
    public ShaderInput findInput(String inputName) {
        ShaderInput si = inputMap.get(inputName);
        if (si == null) {
            return semanticInputMap.get(inputName);
        } else {
            return si;
        }
    }

    /**
     * Adds an output to this node.
     *
     * @param output the output to add.
     * @return true if the output was added, false otherwise.
     */
    public boolean addOutput(ShaderOutput output) {
        if (!outputMap.containsKey(output.getName())) {
            output.setParent(this);
            outputs.add(output);
            outputMap.put(output.getName(), output);
            if (output.getSemantic().isValid()) {
                semanticOutputMap.put(output.getSemantic().getValue(), output);
            }
            notifyIOAdded(output.getName());
            return true;
        } else {
            // take over the values from the input.
            ShaderOutput current = outputMap.get(output.getName());
            current.setType(output.getType());
            current.setTypeRule(output.getTypeRule());

            if (!current.getSemantic().equals(output.getSemantic())) {
                String oldsemantic = current.getSemantic().toString();
                current.setSemantic(output.getSemantic().toString());
                this.remapOutputSemantic(oldsemantic, current);
            }
            return true;
        }
    }

    /**
     * Returns the number of outputs.
     *
     * @return the number of outputs.
     */
    public int getNrOfOutputs() {
        return outputs.size();
    }

    /**
     * Returns the number of outputs that are value types.
     *
     * @return the number of value outputs.
     */
    public int getNrOfValueOutputs() {
        int nrOfValueOutputs = 0;
        for (ShaderOutput output : this.outputs) {
            if (output.getType().isValueType()) {
                nrOfValueOutputs++;
            }
        }
        return nrOfValueOutputs;
    }

    /**
     * Finds an output in this shader node
     *
     * @param outputId the if for the ShaderOutput object.
     * @return the ShaderOutput object.
     */
    public ShaderOutput findOutput(String outputId) {
        ShaderOutput output = outputMap.get(outputId);
        if (output == null) {
            return semanticOutputMap.get(outputId);
        } else {
            return output;
        }

    }

    /**
     * Returns the first output of the node if one exist, otherwise null.
     *
     * @return the first output of the node.
     */
    public ShaderOutput getFirstOutput() {
        if (outputs.size() > 0) {
            return outputs.get(0);
        } else {
            return null;
        }
    }

    /**
     * Returns the first output of the node if one exist, otherwise null.
     *
     * @return the first output of the node.
     */
    public ShaderInput getFirstInput() {
        if (inputs.size() > 0) {
            return inputs.get(0);
        } else {
            return null;
        }
    }

    /**
     * Returns the list of ShaderOutput objects.
     *
     * @return the list of ShaderOutput objects.
     */
    public ArrayList<ShaderOutput> getOutputs() {
        return outputs;
    }

    /**
     * Returns the list of ShaderInput objects.
     *
     * @return the list of ShaderOutput objects.
     */
    public ArrayList<ShaderInput> getInputs() {
        return inputs;
    }

    /**
     * Returns the name for this shader node.
     *
     * @return the name for this shader node.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for this shader node.
     *
     * @param name the name for this shader node.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the node.
     *
     * @return the name of the node.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns the unique id for this shader node.
     *
     * @return the id for this shader node.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id for this shader node.
     *
     * @param id the id for this shader node.
     */
    public void setId(String id) {
        if (!this.id.equals(id)) {
            this.id = id;
            // update the connection strings 
            for (ShaderOutput output : this.outputs) {
                for (ShaderInput input : output.getInputs()) {
                    input.updateConnectionString();
                }
            }
            SettingsGroup sg = this.getSettingsGroups(("Node"));
            Setting s = sg.getSetting("id");
            s.setSettingValue(id);
        }
    }

    /**
     * Sets the position for this node.
     *
     * @param x the x coordinate for the positon.
     * @param y the y coordinate for the position.
     */
    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }

    /**
     * Returns the position for this node.
     *
     * @return the position for this node.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Add a Setting to this ShaderNode
     *
     * @param groupName the name of the setting group.
     * @param setting the setting to add.
     */
    public final void addSetting(String groupName, Setting setting) {
        if (setting.isVisualized() && !hasSetting(groupName, setting.getId())) {
            this.visibleSettings.add(setting);
        }

        SettingsGroup sg = this.settingsGroupsMap.get(groupName);
        if (sg == null) {
            sg = new SettingsGroup(groupName);
            settingsGroupsMap.put(groupName, sg);
            settingsGroups.add(sg);
        }
        sg.addSetting(setting);
        setting.setSettingNode(this);

    }

    /**
     * Add a Setting to this ShaderNode. The replace parameter controls if the
     * setting will replace a previous setting or not.
     */
    public void addSetting(String groupName, Setting setting, boolean replace) {
        if (!replace) {
            addSetting(groupName, setting);
        } else {
            if (setting.isVisualized()) {
                if (visibleSettings.contains(setting)) {
                    visibleSettings.remove(setting);
                }
                visibleSettings.add(setting);
            }
            SettingsGroup sg = this.settingsGroupsMap.get(groupName);
            if (sg == null) {
                sg = new SettingsGroup(groupName);
                settingsGroupsMap.put(groupName, sg);
                settingsGroups.add(sg);
            }
            sg.addSetting(setting, replace);
            setting.setSettingNode(this);
        }
    }

    /**
     * Returns the SettingsGroup for the given group name.
     *
     * @param group the name for the settings group.
     * @return the SettingsGroup.
     */
    public SettingsGroup getSettingsGroups(String group) {
        return settingsGroupsMap.get(group);
    }

    /**
     * Returns a Setting object, given a group name and a name for the setting.
     *
     * @param group the name of the group.
     * @param name the name of the setting.
     */
    public Setting getSetting(String group, String name) {
        SettingsGroup sg = settingsGroupsMap.get(group);
        if (sg == null) {
            return null;
        } else {
            Setting s = sg.getSetting(name);
            return s;
        }
    }

    /**
     * Checks if a setting is present in this node object.
     *
     * @param group the name of the group.
     * @param name the name of the setting.
     * @return true if the setting is present, false otherwise.
     */
    public boolean hasSetting(String group, String name) {
        SettingsGroup sg = settingsGroupsMap.get(group);
        if (sg == null) {
            return false;
        } else {
            return sg.hasSetting(name);
        }
    }

    /**
     * Allows this node to synchronize with the settings values.
     *
     * @param groupName the name of the settings group.
     * @param setting the Setting object that was changed.
     */
    public void updateSetting(String groupName, Setting setting) {
        if (groupName.equals("Node")) {
            if (setting.getId().equals("id")) {
                setId(((TextSetting) setting).getValue());
            } else if (setting.getId().equals("name")) {
                setName(((TextSetting) setting).getValue());
            }
        }
        notifySettingListeners(setting);
    }

    /**
     * Returns the setting value for an identifier
     *
     * @param settingGroup the group of the setting.
     * @param id the id of the setting.
     */
    public String getSettingValue(String settingGroup, String id) {
        SettingsGroup sg = settingsGroupsMap.get(settingGroup);
        if (sg != null) {
            Setting s = sg.getSetting(id);
            if (s != null) {
                return s.getFormattedValue();
            } else {
                return "<!" + settingGroup + "." + id + "not found>";
            }
        } else {
            return "<!" + settingGroup + "." + id + "not found>";
        }
    }

    /**
     * Returns the list of GroupSettings objects for this ShaderNode.
     *
     * @return the list of GroupSettings objects.
     */
    public List<SettingsGroup> getSettingsGroups() {
        return settingsGroups;
    }

    /**
     * Adds a setting listener to the list of listeners.
     *
     * @param listener the listener to add.
     */
    public final void addSettingListener(SettingListener listener) {
        settingListeners.add(listener);
    }

    /**
     * Removes a setting listener from the list of listeners.
     *
     * @param listener the listener to remove.
     */
    public void removeSettingListener(SettingListener listener) {
        settingListeners.remove(listener);
    }

    /**
     * Notifies the listeners that a setting was changed.
     *
     * @param s the setting that was changed.
     */
    public void notifySettingListeners(Setting s) {
        for (SettingListener listener : settingListeners) {
            listener.settingChanged(this, s);
        }
    }

    /**
     * Adds an IOListener object to this node.
     *
     * @param listener the listener to add.
     */
    public void addIOListener(IOListener listener) {
        this.ioListeners.add(listener);
    }

    /**
     * Adds an IOListener object to this node.
     *
     * @param listener the listener to add.
     */
    public void removeIOListener(IOListener listener) {
        this.ioListeners.remove(listener);
    }

    private void notifyIORemoved(String name) {
        for (IOListener listener : ioListeners) {
            listener.ioRemoved(name);
        }
    }

    private void notifyIOAdded(String name) {
        for (IOListener listener : ioListeners) {
            listener.ioAdded(name);
        }
    }

    private void notifyIOChanged(String oldname, String newname) {
        for (IOListener listener : ioListeners) {
            listener.ioChanged(oldname, newname);
        }
    }

    public void removeInput(int index) {
        if (index >= 0 && index < inputs.size()) {
            ShaderInput input = this.inputs.get(index);
            this.removeInput(input);
        }
    }

    public void removeOutput(int index) {
        if (index >= 0 && index < outputs.size()) {
            ShaderOutput output = this.outputs.get(index);
            this.removeOutput(output);
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * Called when the name of a ShaderInput object has changed.
     *
     * @param oldName the old name of the input.
     * @param input the ShaderInput that was changed.
     */
    public void remapInputName(String oldName, ShaderInput input) {
        inputMap.remove(oldName);
        inputMap.put(input.getName(), input);
        notifyIOChanged(oldName, input.getName());
    }

    /**
     * Called when the semantic of a ShaderInput object has changed.
     *
     * @param oldSemantic the old semantic of the input.
     * @param input the ShaderInput that was changed.
     */
    public void remapInputSemantic(String oldSemantic, ShaderInput input) {
        inputMap.remove(oldSemantic);
        inputMap.put(input.getSemantic().getValue(), input);
        notifyIOChanged(input.getName(), input.getName());
    }

    /**
     * Called when the name of a ShaderOutput object has changed.
     *
     * @param oldName the old name of the output.
     * @param output the ShaderOutput that was changed.
     */
    public void remapOutputName(String oldName, ShaderOutput output) {
        outputMap.remove(oldName);
        outputMap.put(output.getName(), output);
        notifyIOChanged(oldName, output.getName());
    }

    /**
     * Called when the semantic of a ShaderOutput object has changed.
     *
     * @param oldSemantic the old semantic of the output.
     * @param output the ShaderOutput that was changed.
     */
    public void remapOutputSemantic(String oldSemantic, ShaderOutput output) {
        outputMap.remove(oldSemantic);
        outputMap.put(output.getSemantic().getValue(), output);
        notifyIOChanged(output.getName(), output.getName());
    }

    /**
     * Called when the type of an input or output changes.
     *
     * @param io the input or output that was changed.
     */
    public void typeChanged(ShaderIO io) {
        if (io.isInput()) {
            this.calculateOutputTypes();
        }
    }

    /**
     * Checks if an input exists with the given name.
     *
     * @param name the input name to check.
     * @return true if the input exists, false otherwise.
     */
    public boolean hasInput(String name) {
        return inputMap.containsKey(name);
    }

    /**
     * Checks if an output exists with the given name.
     *
     * @param name the output name to check.
     * @return true if the output exists, false otherwise.
     */
    public boolean hasOutput(String name) {
        return outputMap.containsKey(name);
    }

    /**
     * Returns a shader input or output.
     *
     * @param portName the name of the port.
     * @return the ShaderIO object.
     */
    public ShaderIO getPort(String portName) {
        if (inputMap.containsKey(portName)) {
            return inputMap.get(portName);
        } else if (outputMap.containsKey(portName)) {
            return outputMap.get(portName);
        } else {
            return null;
        }
    }

    /**
     * Returns the separator to use for the generation of variable names.
     *
     * @return the separator
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Sets the separator to use for the generation of variable names.
     *
     * @param separator the separator to set
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    /**
     * Collect the inputs of the NodeGroupWalker object and add them.
     *
     * @param walker the walker object to add the nodes to.
     */
    public void collectInputs(NodeGroupWalker walker) {
        for (ShaderInput input : this.inputs) {
            if (input.getConnected()) {
                ShaderOutput output = input.getConnectedInput();

                IONode outputParent = output.getParent();
                if (!outputParent.getId().equals("input")) {
                    walker.addNode(outputParent);
                    outputParent.collectInputs(walker);
                }
            }
        }
    }

    /**
     * Remove all the connections from this node.
     */
    public void removeConnections() {
        for (ShaderInput input : this.inputs) {
            ShaderOutput output = input.getConnectedInput();
            if (output != null) {
                output.removeInput(input);
            }
        }
        ArrayList<ShaderOutput> outputsCopy = (ArrayList) outputs.clone();
        for (ShaderOutput output : outputsCopy) {
            output.removeAllInputs();
        }
    }

    /**
     * Returns the list of visible settings.
     *
     * @return the list of visible settings.
     */
    public Iterable<Setting> getVisualizedSettings() {
        return this.visibleSettings;
    }

    /**
     * Sets a struct as the output for this node. This is only possibly for
     * nodes that allow the input and output to be edited. An attempt is made to
     * reuse existing inputs if they have the same name.
     *
     * @param output the struct to use as output struct
     */
    public void setOutputStruct(ShaderStruct output) {
        if (output == null) {
            // remove ShaderOutput objects that are not in the struct.
            if (this.outputStruct != null) {
                ArrayList<ShaderOutput> clone = (ArrayList<ShaderOutput>) outputs.clone();
                for (ShaderOutput so : clone) {
                    if (outputStruct.containsField(so.getName())) {
                        this.removeOutput(so);
                    }
                }
            }
            this.outputStruct = null;
            return;
        }
        if (this.outputStruct != null) {
            outputStruct.removeStructListener(this);
        }
        this.outputStruct = output;
        outputStruct.addStructListener(this);
        for (ShaderField field : output.getFields()) {
            if (field.getSemantic().isValid()) {
                ShaderOutput so = this.findOutput(field.getSemantic().getValue());
                if (so != null) {
                    so.setType(field.getType());
                    so.setName(field.getName());

                }
            } else {
                ShaderOutput so = this.findOutput(field.getName());
                if (so != null) {
                    so.setType(field.getType());
                    so.setSemantic(field.getSemantic().getValue());
                } else {
                    ShaderOutput nso = new ShaderOutput(this, field.getName(), field.getSemantic().getValue(), field.getType(), null);
                    this.addOutput(nso);
                }
            }
        }
        // remove ShaderOutput objects that are not in the struct.
        ArrayList<ShaderOutput> clone = (ArrayList<ShaderOutput>) outputs.clone();
        for (ShaderOutput so : clone) {
            if (!output.containsField(so.getName())) {
                this.removeOutput(so);
            }
        }
    }

    /**
     * Sets a struct as the output for this node. This is only possibly for
     * nodes that allow the input and output to be edited. An attempt is made to
     * reuse existing inputs if they have the same name.
     *
     * @param input the struct to use as input struct
     */
    public void setInputStruct(ShaderStruct input) {
        if (input == null) {
            return;
        }

        if (inputStruct != null) {
            inputStruct.removeStructListener(this);
        }
        this.inputStruct = input;
        inputStruct.addStructListener(this);
        for (ShaderField field : input.getFields()) {
            if (field.getSemantic().isValid()) {
                ShaderInput si = this.findInput(field.getSemantic().getValue());
                if (si != null) {
                    si.setType(field.getType());
                    si.setName(field.getName());

                }
            } else {
                ShaderInput si = this.findInput(field.getName());
                if (si != null) {
                    si.setType(field.getType());
                    si.setSemantic(field.getSemantic().getValue());
                } else {
                    ShaderInput nsi = new ShaderInput(this, field.getName(), field.getSemantic().getValue(), field.getType());
                    this.addInput(nsi);
                }

            }
        }
        // remove ShaderOutput objects that are not in the struct.
        ArrayList<ShaderInput> clone = (ArrayList<ShaderInput>) inputs.clone();
        for (ShaderInput si : clone) {
            if (!input.containsField(si.getName())) {
                this.removeInput(si);
            }
        }
    }

    /**
     * Removes the output from the list of outputs.
     *
     * @param so the ShaderOutput to remove.
     */
    public void removeOutput(ShaderOutput so) {
        this.outputs.remove(so);
        this.outputMap.remove(so.getName());
        if (so.getSemantic().isValid()) {
            this.outputMap.remove(so.getSemantic().getValue());
        }
        ArrayList<ShaderInput> inputClones = new ArrayList<ShaderInput>();
        for (ShaderInput input : so.getInputs()) {
            inputClones.add(input);
        }
        for (ShaderInput input : inputClones) {
            input.disconnectInput();
        }
        notifyIORemoved(so.getName());
    }

    /**
     * Removes the output with the given id.
     *
     * @param id the id of the output to remove.
     */
    public void removeOutput(String id) {
        ShaderOutput output = this.findOutput(id);
        if (output != null) {
            removeOutput(output);
        }
    }

    /**
     * Removes the input from the list of inputs.
     *
     * @param si the ShaderInput to remove.
     */
    public void removeInput(ShaderInput si) {
        this.inputs.remove(si);
        this.inputMap.remove(si.getName());
        if (si.getSemantic().isValid()) {
            this.inputMap.remove(si.getSemantic().getValue());
        }
        if (si.getConnected()) {
            ShaderOutput connected = si.getConnectedInput();
            connected.removeInput(si);
        }
        notifyIORemoved(si.getName());
    }

    /**
     * Removes the output with the given id.
     *
     * @param id the id of the output to remove.
     */
    public void removeInput(String id) {
        ShaderInput input = this.findInput(id);
        if (input != null) {
            removeInput(input);
        }
    }

    /**
     * This method is called when the field of a struct was changed.
     *
     * @param struct the struct that was changed.
     * @param field the field that was changed.
     * @param oldValue an object that contains the old values of the field.
     */
    public void structFieldUpdated(ShaderStruct struct, ShaderField field, ShaderField oldValue) {
        if (struct == inputStruct) {
            if (ShaderField.nameChanged(field, oldValue)) {
                ShaderInput input = this.findInput(oldValue.getName());
                input.setName(field.getName());
                //this.remapInputName(oldValue.getName(), input);
            } else if (ShaderField.semanticChanged(field, oldValue)) {
                ShaderInput input = this.findInput(field.getName());
                input.setSemantic(field.getSemantic().getValue());
                //this.remapInputSemantic(oldValue.getSemantic().getValue(), input);
            } else {
                ShaderInput input = this.findInput(field.getName());
                input.setType(field.getType());
                this.typeChanged(input);
            }
        } else if (struct == outputStruct) {
            if (ShaderField.nameChanged(field, oldValue)) {
                ShaderOutput output = this.findOutput(oldValue.getName());
                output.setName(field.getName());
                //this.remapOutputName(oldValue.getName(), output);
            } else if (ShaderField.semanticChanged(field, oldValue)) {
                ShaderOutput output = this.findOutput(field.getName());
                output.setSemantic(field.getSemantic().getValue());
                //this.remapOutputSemantic(oldValue.getSemantic().getValue(), output);
            } else {
                ShaderOutput output = this.findOutput(field.getName());
                output.setType(field.getType());
                this.typeChanged(output);
            }
        } else {
            struct.removeStructListener(this);
        }
    }

    /**
     * This method is called when a new field is inserted in the struct.
     *
     * @param field the field that was inserted.
     */
    public void structFieldInserted(ShaderStruct struct, ShaderField field) {
        if (struct == inputStruct) {
            ShaderInput input = new ShaderInput(this, field.getName(), field.getSemantic().getValue(), field.getType());
            this.addInput(input);
        } else if (struct == outputStruct) {
            ShaderOutput output = new ShaderOutput(this, field.getName(), field.getSemantic().getValue(), field.getType(), null);
            this.addOutput(output);
        }
    }

    /**
     * This method is called when a field was remove from the struct.
     *
     * @param field the field that was removed.
     */
    public void structFieldRemoved(ShaderStruct struct, ShaderField field) {
        if (struct == inputStruct) {
            ShaderInput input = this.findInput(field.getName());
            this.removeInput(input);
        } else if (struct == outputStruct) {
            ShaderOutput output = this.findOutput(field.getName());
            this.removeOutput(output);
        }
    }

    /**
     * This method is called when the id of the struct was changed.
     *
     * @param struct the struct whose id was changed.
     */
    @Override
    public void structIdChanged(ShaderStruct struct) {
    }

    /**
     * The ShaderStage object has no children.
     *
     * @param childIndex the index of the child.
     * @return always null.
     */
    @Override
    public TreeNode getChildAt(int childIndex) {
        return null;
    }

    /**
     * The ShaderStage object has no children.
     *
     * @return always 0
     */
    @Override
    public int getChildCount() {
        return 0;
    }

    /**
     * This node can have different parents.
     *
     * @return the parent of this object, always null.
     */
    @Override
    public TreeNode getParent() {
        return null;
    }

    /**
     * Node has no children, returns -1.
     *
     * @param node the node to get the index of.
     * @return always -1.
     */
    @Override
    public int getIndex(TreeNode node) {
        return -1;
    }

    /**
     * This node does not have child elements, returns null.
     *
     * @return always false.
     */
    @Override
    public boolean getAllowsChildren() {
        return false;
    }

    /**
     * This node is a leaf.
     *
     * @return always true.
     */
    @Override
    public boolean isLeaf() {
        return true;
    }

    /**
     * not supported, enumerations are evil.
     *
     * @return always null.
     */
    @Override
    public Enumeration children() {
        return null;
    }

    // input output rules.
    /**
     * Adapt the output types to the input types.
     */
    public void calculateOutputTypes() {
        ArrayList<ShaderOutput> outputsClone = (ArrayList<ShaderOutput>) this.getOutputs().clone();
        for (ShaderOutput output : outputsClone) {
            String rule = output.getTypeRule();
            if (rule == null) {
                continue;
            } else {
                // parse the rule type and the operands.
                int leftIndex = rule.indexOf('(');
                int rightIndex = rule.indexOf(')', leftIndex);

                if (leftIndex > -1 && rightIndex > -1) {
                    String command = rule.substring(0, leftIndex);
                    String[] operands = rule.substring(leftIndex + 1, rightIndex).split(",");
                    if ("max".equals(command)) {
                        output.setType(max(operands));
                    } else if ("type".equals(command) && operands.length == 1) {
                        output.setType(type(operands[0]));
                    }
                }
            }
        }
    }

    /**
     *
     */
    private ShaderType max(String[] inputs) {
        if (inputs.length == 0) {
            return null;
        } else if (inputs.length == 1) {
            ShaderInput input = this.findInput(inputs[0]);
            return input.getType();
        } else {
            int order = -1;
            ShaderType result = null;
            for (int i = 0; i < inputs.length; ++i) {
                ShaderInput input = this.findInput(inputs[i]);
                if (input == null) {
                    continue;
                }
                ShaderType st = input.getType();
                if (st.getOrder() > order) {
                    result = st;
                    order = st.getOrder();
                }
            }
            return result;
        }
    }

    private ShaderType type(String input) {
        ShaderInput si = this.findInput(input);
        if (si != null) {
            return si.getType();
        } else {
            return this.shaderTypeLib.getType("FLOAT");
        }
    }

    public static void main(String[] args) {
        

    }

    /**
     * Sets the fx project this node belongs to.
     *
     * @param fxProject the fx project of this node.
     */
    public final void setFXProject(FXProject fxProject) {
        this.fxProject = fxProject;

        if (fxProject != null) {
            this.shaderTypeLib = fxProject.getShaderTypeLibrary();
            for (SettingsGroup settingGroup : this.getSettingsGroups()) {
                for (Setting s : settingGroup.getSettings()) {
                    fxProject.addSymbolListener(s);
                }
            }
        }
    }

    /**
     * Sets the fx project this node belongs to.
     *
     * @return the FXProject object this node is a part of.
     */
    public FXProject getFXProject() {
        return fxProject;
    }
    private String icon = "";

    /**
     * Sets the icon for this shader node.
     *
     * @param icon the icon for this shadernode.
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    public boolean isRemovable() {
        return removable;
    }

    public boolean isInputNode() {
        return inputNode;
    }

    public boolean isOutputNode() {
        return outputNode;
    }

    public void setInputNode(boolean inputNode) {
        this.inputNode = inputNode;
    }

    public void setOutputNode(boolean outputNode) {
        this.outputNode = outputNode;
    }

    public void clearSettings() {
        this.settingsGroupsMap.clear();
        this.settingsGroups.clear();
    }

    /**
     * Adds a template input to the list of inputs.
     *
     * @param input the input object.
     */
    public void addTemplateInput(ShaderInput input) {
        this.templateInputs.add(input);
    }

    /**
     * Checks if this node has template inputs.
     *
     * @return true if this node has template inputs, false otherwise.
     */
    public boolean hasTemplateInputs() {
        return templateInputs.size() > 0;
    }

    /**
     * Returns the list of template inputs.
     *
     * @return the list of template inputs.
     */
    public List<ShaderInput> getTemplateInputs() {
        return templateInputs;
    }

    /**
     * Realizes the input template, by adding the template inputs to the list of
     * inputs.
     */
    public void realizeInputTemplates() {
        for (ShaderInput input : templateInputs) {
            String iname = input.getName();
            int i = 1;
            while (this.hasInput(iname + i)) {
                ++i;
            }
            ShaderInput newInput = new ShaderInput(this, iname + i, input.getSemantic().getValue(), input.getType(), input.getAcceptTypeSet());
            this.addInput(newInput);
        }
    }

    /**
     * The project type for this ionode.
     *
     * @return the project type.
     */
    public FXProjectType getProjectType() {
        return fxProject.getProjectType();
    }

    /**
     * Sets the default anchor for inputs.
     *
     * @param anchor the anchor for inputs in the form of (N|S) (W|E)
     */
    public void setInputAnchor(String anchor) {
        switch (anchor.toUpperCase()) {
            case "NE":
                inputAnchor = IOAnchor.NORTHEAST;
                break;
            case "NW":
                inputAnchor = IOAnchor.NORTHWEST;
                break;
            case "SE":
                inputAnchor = IOAnchor.SOUTHEAST;
                break;
            case "SW":
                inputAnchor = IOAnchor.SOUTHWEST;
                break;
            default:
                inputAnchor = IOAnchor.NORTHWEST;
        }
    }

    /**
     * Sets the default input anchor for this ionode object.
     *
     * @param anchor the default anchor.
     */
    public void setInputAnchor(IOAnchor anchor) {
        inputAnchor = anchor;
    }

    /**
     * Returns the default input anchor for this node.
     *
     * @return the default input anchor for the node.
     */
    public IOAnchor getInputAnchor() {
        return inputAnchor;
    }

    /**
     * Sets the default anchor for outputs.
     *
     * @param anchor the anchor for inputs in the form of (N|S) (W|E)
     */
    public void setOutputAnchor(String anchor) {
        switch (anchor.toUpperCase()) {
            case "NE":
                outputAnchor = IOAnchor.NORTHEAST;
                break;
            case "NW":
                outputAnchor = IOAnchor.NORTHWEST;
                break;
            case "SE":
                outputAnchor = IOAnchor.SOUTHEAST;
                break;
            case "SW":
                outputAnchor = IOAnchor.SOUTHWEST;
                break;
            default:
                outputAnchor = IOAnchor.NORTHWEST;
        }
    }

    /**
     * Sets the default output anchor for this ionode object.
     *
     * @param anchor the default anchor.
     */
    public void setOutputAnchor(IOAnchor anchor) {
        outputAnchor = anchor;
    }

    /**
     * Returns the default output anchor for this node.
     *
     * @return the default output anchor.
     */
    public IOAnchor getOutputAnchor() {
        return outputAnchor;
    }
}
