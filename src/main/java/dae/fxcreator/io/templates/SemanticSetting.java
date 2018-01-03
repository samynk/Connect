/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dae.fxcreator.io.templates;

import java.util.jar.Attributes.Name;

/**
 * A semantic setting for a node. This can be useful for
 * constants where a semantic needs to be set.
 * @author Koen
 */
public class SemanticSetting extends Setting{
    private String semantic;

    /**
     * Creates a new SemanticSetting object.
     * @param name the name for the setting.
     * @param label the user interface label.
     */
    public SemanticSetting(String name, String label){
        super(name,label);
    }

    /**
     * Returns the type of setting, in this case the semantic type.
     * @return the type of this setting object.
     */
    @Override
    public String getType() {
        return "semantic";
    }

    /**
     * Returns the value currently set in this SemanticSetting object.
     * @return the value of this SemanticSetting.
     */
    @Override
    public String getSettingValue() {
        return semantic;
    }

    /**
     * Returns the value currently set in this SemanticSetting object.
     * @return the value of this SemanticSetting.
     */
    @Override
    public String getFormattedValue() {
        return semantic;
    }

    /**
     * Returns the value currently set in this SemanticSetting object.
     * @return the value of this SemanticSetting.
     */
    @Override
    public String getSettingValueAsObject() {
        return semantic;
    }

    /**
     * Sets the value for this setting (this should be a semantic).
     * @param value the value for the semantic.
     */
    @Override
    public void setSettingValue(String value) {
        this.setOldValue(this.semantic);
        this.semantic=value;
        notifySettingChanged();
    }

    /**
     * Sets the setting value of this setting as an object.
     * @param the new value for this setting.
     */
    public void setSettingValueAsObject(Object o){
        this.setSettingValue(o.toString());
    }
}