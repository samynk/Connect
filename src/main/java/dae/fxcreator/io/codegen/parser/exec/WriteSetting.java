package dae.fxcreator.io.codegen.parser.exec;

import dae.fxcreator.io.codegen.ExportTask;
import dae.fxcreator.node.settings.Setting;
import dae.fxcreator.node.IONode;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class WriteSetting implements Executable {

    private final String object;
    private final String settingGroup;
    private final String settingProperty;

    public WriteSetting(String object, String group, String property) {
        this.object = object;
        this.settingGroup = group;
        this.settingProperty = property;
    }

    @Override
    public void execute(Object codeObject, ExportTask context) {
        Object o = context.getVar(object);
        if (o == null && !(o instanceof IONode)) {
            return;
        }
        IONode node = (IONode) o;
        Setting value = node.getSetting(settingGroup, settingProperty);
        if (value != null) {
            context.peekBuffer().append(value.toString());
        }
    }
}
