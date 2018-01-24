package dae.fxcreator.node.settings;

/**
 * This Setting object defines a text property for a node.
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class TextSetting extends Setting {

    private String value="";

    /**
     * Creates a new TextSetting object with a label and value.
     * @param name the name for the setting.
     * @param label the label for the text setting.
     * @param value the value for the text setting.
     */
    public TextSetting(String name, String label, String value) {
        super(name, label);
        this.value = value;
        if ( this.value == null)
            this.value="";
    }

    /**
     * Returns the value for the textsetting.
     * @return the value for the text setting.
     */
    public String getValue() {
        return value;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Sets the value for this Setting object. The old value will be backed up
     * into the oldValue field of the parent object.
     * @param text the new value for this text setting.
     */
    public void setValue(String text) {
        if (this.value == null || !this.value.equals(text)) {
            setOldValue(this.value);
            this.value = text;
            notifySettingChanged();
        }
    }

    /**
     * Returns the type of the setting.
     * @return the type of the setting.
     */
    @Override
    public String getType() {
        return "text";
    }

    /**
     * Returns the setting value of this setting as a string.
     * @return the setting value as string.
     */
    @Override
    public String getSettingValue() {
        return value;
    }

    /**
     * Returns the setting value of this setting as a string.
     * @return the setting value as string.
     */
    @Override
    public String getFormattedValue() {
        return value;
    }

    /**
     * Returns the value currently set in this SemanticSetting object.
     * @return the value of this SemanticSetting.
     */
    @Override
    public String getSettingValueAsObject() {
        return value;
    }

    /**
     * Sets the value for this setting as a string.
     * @param text the new value for the setting object.
     */
    @Override
    public void setSettingValue(String text) {
        if (text == null  ){
            return;
        }
        if ( this.value == null ){
            System.out.println(this.getSettingNode().getName());
            System.out.println(this.getGroup() + "." +this.getLabel());
        }
        if (!text.equals(this.value)) {
            setOldValue(this.value);
            this.value = text;
        }
    }

    /**
     * Sets the setting value of this setting as an object.
     * @param o the new value for this setting.
     */
    @Override
    public void setSettingValueAsObject(Object o){
        this.setSettingValue(o.toString());
    }
}
