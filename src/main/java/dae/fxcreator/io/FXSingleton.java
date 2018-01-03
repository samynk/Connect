package dae.fxcreator.io;

import dae.fxcreator.ui.usersettings.UserSettings;
import java.util.ArrayList;

/**
 * This class provides the project with all the general objects.
 * Available for now are :
 * 1) the FXSettings object : provides the code template libraries and the semantics.
 * 2) the UserSettings object : provides the user settings for projects.
 * @author Koen
 */
public class FXSingleton {

    private static FXSingleton singleton;
    private FXSettings fxSettings;
    private UserSettings userSettings;
    private ArrayList<FXSettingListener> listeners = new ArrayList<>();
    private FXProjectType currentProjectType;

    private FXSingleton() {
    }

    /**
     * Returns the FXSingleton object.
     * @return the FXSingleton object.
     */
    public static FXSingleton getSingleton() {
        if (singleton == null) {
            singleton = new FXSingleton();
        }
        return singleton;
    }

    /**
     * Returns the FXSettings for this project.
     * @return the FXSettings object.
     */
    public FXSettings getFXSettings() {
        return fxSettings;
    }

    /**
     * Sets the FXSettings object for this project.
     * @param fxSettings the new fxSettings object.
     */
    public void setFxSettings(FXSettings fxSettings) {
        if (fxSettings != null) {
            this.fxSettings = fxSettings;
            this.notifyFXSettingsChanged();
        }
    }
    
    public void setCurrentProjectType(FXProjectType projectType){
        this.currentProjectType = projectType;
    }
    
    public FXProjectType getCurrentProjectType(){
        return this.currentProjectType;
    }

    /**
     * Gets the user settings object for this project.
     * @return the user settings object.
     */
    public UserSettings getUserSettings() {
        return userSettings;
    }

    /**
     * Sets the user settings object for this project.
     * @param userSettings the usersettings object.
     */
    public void setUserSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    /**
     * Adds a listener for the FXSetting object.
     * @param FXSettingListener listener.
     */
    public void addFXSettingListener(FXSettingListener listener) {
        this.listeners.add(listener);
    }

    private void notifyFXSettingsChanged() {
        for (FXSettingListener l : listeners) {
            l.fxSettingChanged(fxSettings);
        }
    }
}
