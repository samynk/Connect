package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.IONode;
import dae.fxcreator.node.settings.Setting;
import dae.fxcreator.node.transform.ExportTask;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ObjectSetting implements Expression {

    private final String id;
    private final String group;
    private final String key;
    private final boolean root;

    public ObjectSetting(String id, String group, String key, boolean root) {
        this.id = id;
        this.group = group;
        this.key = key;
        this.root = root;
    }

    @Override
    public Object evaluate(ExportTask context) {
        Object top;
        if (root) {
            top = context.getVar("node");
        } else {
            top = context.popChainStack();
        }
        if (top instanceof IONode) {
            IONode node = (IONode) top;
            Setting setting = node.getSetting(group, key);
            if (setting != null) {
                return setting.getSettingValueAsObject();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
