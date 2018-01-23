package dae.fxcreator.node;

import dae.fxcreator.io.templates.Setting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Groups settings together for a shader node.
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class SettingsGroup {
    /**
     * The name for the settings group.
     */
    private final String name;

    private final ArrayList<Setting> settings = new ArrayList<>();
    private final HashMap<String,Setting> settingsMap = new HashMap<>();

    /**
     * Creates a new SettingsGroup object with the given name.
     * @param name the name for the settings group.
     */
    public SettingsGroup(String name){
        this.name = name;
    }

    /**
     * Adds a new Setting object to the list of settings.
     * @param setting the new setting to add.
     */
    public void addSetting(Setting setting){
        if ( !settingsMap.containsKey(setting.getId())){
            settingsMap.put(setting.getId(), setting);
            setting.setGroup(name);
            settings.add(setting);
        }
    }

    /**
     * Adds a Setting object to the list of settings.
     * The replace parameter controls if the setting will replace an existing
     * setting or not.
     * @param setting the new setting
     * @param replace if true , the setting object will replace a previous setting,
     * if false the setting will only be added if it is not found in the settings.
     */
    public void addSetting(Setting setting, boolean replace)
    {
        if ( replace ){
            Setting s = this.getSetting(setting.getId());
            settings.remove(s);
            settingsMap.put(setting.getId(), setting);
            setting.setGroup(name);
            settings.add(setting);
        }else
            addSetting(setting);
    }

    /**
     * Returns the name for the SettingsGroup.
     * @return the name of the settings group.
     */
    public String getName(){
        return name;
    }

    /**
     * Return the list of settings for this settings group.
     * @return the list of settings.
     */
    public List<Setting> getSettings() {
        return this.settings;
    }

    /**
     * Returns a specific setting from this group of settings.
     * @param name the name of the setting
     * @return the Setting object, or null if no object exists.
     */
    public Setting getSetting(String name) {
        return settingsMap.get(name);
    }

    /**
     * Returns true if the setting with the name is present.
     * @param name the name of the setting.
     * @return true if the setting is present, false otherwise.
     */
    public boolean hasSetting(String name) {
        return settingsMap.containsKey(name);
    }
}
