package dae.fxcreator.node.events;

import dae.fxcreator.node.settings.Setting;
import dae.fxcreator.node.IONode;

/**
 * Allows the user interface to listen to setting changes.
 * @author Koen
 */
public interface SettingListener {
    /**
     * Called when the setting of an object was changed.
     * @param node the node where a setting was changed.
     * @param s the setting that was changed.
     */
    public void settingChanged(IONode node, Setting s);
}
