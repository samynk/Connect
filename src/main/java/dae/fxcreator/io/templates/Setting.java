/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.io.templates;

import dae.fxcreator.io.events.SymbolListener;
import dae.fxcreator.node.IONode;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * General interface for the settings for a shader node.
 * @author Koen
 */
public abstract class Setting implements Cloneable, SymbolListener {

    private String id;
    private String label;
    private IONode node;
    private String group;
    private boolean writeValueAsAttribute = true;
    private boolean visualize = false;
    private boolean labelVisible = false;
    private boolean defaultValue = false;
    private boolean valueAsXML = false;
    /**
     * The list of globals this option setting should listen for.
     */
    private ArrayList<String> globalSymbols = new ArrayList<String>();
    /**
     * The old value for the setting.
     */
    private Object oldValue;

    public Setting(String name, String label) {
        this.id = name;
        this.label = label;
    }

    /**
     * Notifies the node object that the setting was changed.
     * checks if the node != null before calling the node method.
     */
    protected void notifySettingChanged() {
        if (node != null) {
            node.updateSetting(group, this);
        }
    }

    /**
     * Returns the label for this setting.
     * @return the label for this setting.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label for this setting.
     * @param label the label for this setting.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the id for this setting.
     * @return the id of the setting
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id for this setting.
     * @param id the new id for the setting.
     */
    public void setId(String name) {
        this.id = name;
    }

    /**
     * Gets the IONode this setting belongs to.
     * @return the IONode this setting is for.
     */
    public IONode getSettingNode() {
        return node;
    }

    /**
     * Sets the IONode this setting is for.
     * @param IONode the node that is bound to this setting.
     */
    public void setSettingNode(IONode node) {
        this.node = node;
    }

    /**
     * Creates a clone of this settings object.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Sets the group this setting belongs to.
     * @param id the id of the group.
     */
    public void setGroup(String name) {
        this.group = name;
    }

    /**
     * Returns the group this setting belongs to.
     * @return the group this setting belongs to.
     */
    public String getGroup() {
        return group;
    }

    /**
     * Returns the type of the Setting.
     * @return the type of setting.
     */
    public abstract String getType();

    /**
     * Returns the value of the Setting as a String.
     * @return the value of this setting as a string.
     */
    public abstract String getSettingValue();
    /**
     * Returns the value of this Setting as a formatted value for use in a
     * shader.
     * @return the value of this setting as a formatted value
     */
    public abstract String getFormattedValue();

    /**
     * Returns the value of the Setting as an Object.
     * @return the value of the Setting as an Object.
     */
    public abstract Object getSettingValueAsObject();
    /**
     * Sets the setting value of this setting as an object.
     * @param the new value for this setting.
     */
    public abstract void setSettingValueAsObject(Object o);

    /**
     * Sets the value for this Setting as a String.
     * @param value the value for this setting as a string.
     */
    public abstract void setSettingValue(String value);


    /**
     * Checks if the value of the setting is written as an attribute (with
     * the name value) or as a cdata section inside a value element. (default
     * is to write as an attribute).
     * @return the writeValueAsAttribute
     */
    public boolean isWriteValueAsAttribute() {
        return writeValueAsAttribute;
    }

    /**
     * If the value should be written as an attribute, pass true to this method
     * otherwise, pass false to write the value as a cdata section inside the
     * setting.
     * @param writeValueAsAttribute the writeValueAsAttribute to set
     */
    public void setWriteValueAsAttribute(boolean writeValueAsAttribute) {
        this.writeValueAsAttribute = writeValueAsAttribute;
    }

    /**
     * Check if the setting should be visualized in the node.
     * @return the visualized state (true if the setting should be visualized
     * in the node).
     */
    public boolean isVisualized() {
        return visualize;
    }

    /**
     * Set the state of the visualization. If set to true , this setting will
     * be visualized in the node.
     * The default is false.
     * @param visualize the visualize to set
     */
    public void setVisualized(boolean visualized) {
        this.visualize = visualized;
    }

    /**
     * Checks if the label if this setting is visible for a visualizer.
     * @return the labelVisible
     */
    public boolean isLabelVisible() {
        return labelVisible;
    }

    /**
     * Sets the label visibility for the visualizer.
     * @param labelVisible the labelVisible to set
     */
    public void setLabelVisible(boolean labelVisible) {
        this.labelVisible = labelVisible;
    }

    /**
     * Returns the previous value for the setting.
     * @return the oldValue
     */
    public Object getOldValue() {
        return oldValue;
    }

    /**
     * Sets the old value for the setting.
     * @param oldValue the oldValue to set
     */
    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    /**
     * Add a listener for the symbol. (possible values are currently STRUCT)
     * @param string the symbol to listen for in this setting.
     */
    public void addListenerFor(String string) {
        this.globalSymbols.add(string);
    }

    /**
     * Returns the symbols this listener wants to to listen for.
     * @return the keys this object wants to listen for.
     */
    public Iterator<String> getListenerSymbols() {
        return globalSymbols.iterator();
    }

    /**
     * Checks if this setting listens to the specific symbol.
     * @param type the symbol.
     * @return true if this setting listens for the specific symbol changes.
     */
     public boolean listensToSymbol(String type) {
        return globalSymbols.contains(type);
    }

    /**
     * Notifies that a symbol was added to the project. A symbol can be a
     * ShaderStage, Struct, Technique or Pass.
     * @param type the type of symbol that was added.
     * @param symbol the symbol that was added.
     */
    public void symbolAdded(String type, Object symbol) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Notifies that a symbol was removed from the project. A symbol can be
     * a ShaderStage, Struct, Techniqu or Pass.
     * @param type the type of symbol that was removed.
     * @param symbol the symbol that was removed.
     */
    public void symbolRemoved(String type, Object symbol) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Checks if this setting is a default value. Writers can use this to skip settings
     * that have default values.
     * @return true if the setting is set to a default, false otherwise.
     */
    public boolean isDefaultValue() {
        return defaultValue;
    }

    /**
     * Marks this setting as a default value.
     * @param defaultValue pass true if this setting is a default value, false otheriwse.
     */
    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * The value of the setting consists of well formed xml elements.
     * @param valueAsXML true if the value is an xml node, false otherwise.
     */
    public void setValueAsXML(boolean valueAsXML) {
        this.valueAsXML = valueAsXML;
    }

    /**
     * Checks if the value is written as an xml element.
     * @return true if the value is an xml element, false otherwise.
     */
    public boolean isValueAsXML(){
        return valueAsXML;
    }
}
