package dae.fxcreator.ui.actions;

import dae.fxcreator.util.ListHashMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;

/**
 * Manages the actions in the application.
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ActionManager {

    private static final ActionManager INSTANCE = new ActionManager();
    private final ListHashMap<FXAction> actions = new ListHashMap<>();
    private final ResourceBundle i18nBundle;

    private ActionManager() {
        i18nBundle = ResourceBundle.getBundle("dae.i18n.Bundle");
    }

    /**
     * Returns the singleton ActionManager instance.
     *
     * @return the ActionManager instance.
     */
    public static ActionManager getInstance() {
        return INSTANCE;
    }

    public void registerAction(FXAction action) {
        actions.addItem(action);
        String label = i18nBundle.getString(action.getI18NLabel());
        String tooltip = i18nBundle.getString(action.getI18NTooltip());
        action.setLabel(label);
        action.setTooltip(tooltip);
    }

    public Action getAction(String id) {
        return actions.find(id);
    }
}
