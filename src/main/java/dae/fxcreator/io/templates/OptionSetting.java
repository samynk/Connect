/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.io.templates;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * This class describes a setting that can be selected from a list of options.
 * @author Koen
 */
public class OptionSetting extends Setting implements ComboBoxModel {

    private ArrayList options = new ArrayList();
    private int selectedIndex = 0;

    private ArrayList<ListDataListener> listDataListeners = new ArrayList<ListDataListener>();

    /**
     * Creates a new OptionSetting object.
     * @param name the identifier to use this object as a
     * @param label the label for the option as used in the user interface.
     */
    public OptionSetting(String name, String label) {
        super(name, label);
    }

    /**
     * Adds a new option to this option setting.
     * @param option the value for the option.
     * @param selected is the value selected or not.
     */
    void addOption(String option, boolean selected) {
        options.add(option);
        if (selected) {
            selectedIndex = options.size() - 1;
        }
    }

    /**
     * Returns the selected option.
     * @return the selected option.
     */
    public String getOption() {
        if (selectedIndex >= 0 && selectedIndex < options.size()) {
            return options.get(selectedIndex).toString();
        } else {
            return "";
        }
    }

    /**
     * Sets the selected item.
     * @param item the item that was selected.
     */
    public void setSelectedItem(Object item) {
        setOldValue(options.get(selectedIndex));
        String key = item.toString();
        for (int i = 0 ; i < options.size(); ++i)
        {
            if ( options.get(i).toString().equals(key) ){
                selectedIndex = i;
                break;
            }
        }
        if ( this.getSettingNode() != null )
            getSettingNode().updateSetting(this.getGroup(), this);
    }

    /**
     * Returns the selected item in this OptionSetting object.
     * @return the object that was selected.
     */
    public Object getSelectedItem() {
        return getOption();
    }

    /**
     * Returns the number of elements in this option.
     * @return the number of elements in this option.
     */
    public int getSize() {
        return options.size();
    }

    /**
     * Gets the element at the provided location.
     * @param index the index of the element.
     * @return the Object at that location.
     */
    public Object getElementAt(int index) {
        return options.get(index);
    }

    public void addListDataListener(ListDataListener l) {
        listDataListeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        listDataListeners.add(l);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        OptionSetting setting = (OptionSetting)super.clone();
        setting.setSettingValue(this.getSettingValue());
        return setting;
        /*
        OptionSetting clone = new OptionSetting(getId(),getLabel());
        
        clone.setVisualized(this.isVisualized());
        clone.setLabelVisible(this.isLabelVisible());
        for ( int i = 0; i< options.size();++i){
            clone.addOption(options.get(i).toString(),i==selectedIndex);
        }
        clone.setGroup(this.getGroup());
        Iterator<String> it = this.getListenerSymbols();
        while ( it.hasNext()){
            clone.addListenerFor(it.next());
        }
        return clone;
*/
    }

    /**
     * Returns the type of the setting.
     * @return always "optionlist"
     */
    @Override
    public String getType() {
        return "optionlist";
    }

    /**
     * Returns the setting as a string object.
     * @return the formatted value for the setting.
     */
    @Override
    public String getSettingValue() {
        return options.get(selectedIndex).toString();
    }

    /**
     * Returns the setting as a string object.
     * @return the formatted value for the setting.
     */
    @Override
    public String getFormattedValue() {
        return options.get(selectedIndex).toString();
    }

     /**
     * Returns the value of the Setting as an Object.
     * @return the value of the Setting as an Object.
     */
    @Override
    public Object getSettingValueAsObject(){
        return options.get(selectedIndex);
    }

    /**
     * Sets the setting value of this setting as an object.
     * @param the new value for this setting.
     */
    public void setSettingValueAsObject(Object o){
        this.setSettingValue(o.toString());
    }

    /**
     * Sets the setting as a string object.
     * @param value the new value for this setting.
     */
    @Override
    public void setSettingValue(String value) {
        if ( value == null )
            return;
        if (selectedIndex > -1) {
            setOldValue(getOption());
        }
        if (!options.contains(value))
        {
            options.add(value);
        }
        setSelectedItem(value);
        notifySettingChanged();
    }

    /**
     * Notifies that a symbol was added to the project. A symbol can be a
     * ShaderStage, Struct, Technique or Pass.
     * @param type the type of symbol that was added.
     * @param symbol the symbol that was added.
     */
    @Override
    public void symbolAdded(String type, Object symbol) {
        if (listensToSymbol(type)) {
            
            if (!options.contains(symbol)) {
                int index = options.size();
                this.options.add(symbol);
                ListDataEvent lde = new ListDataEvent(this,ListDataEvent.INTERVAL_ADDED, index, index);
                for(ListDataListener l:listDataListeners){
                    l.intervalAdded(lde);
                }
            }
        }
    }

    /**
     * Notifies that a symbol was removed from the project. A symbol can be
     * a ShaderStage, Struct, Techniqu or Pass.
     * @param type the type of symbol that was removed.
     * @param symbol the symbol that was removed.
     */
    @Override
    public void symbolRemoved(String type, Object symbol) {
        if (listensToSymbol(type)) {
            this.options.remove(symbol.toString());
        }
    }
}
