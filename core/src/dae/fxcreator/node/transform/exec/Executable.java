package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.transform.ExportTask;
/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public interface Executable {
    public void execute(Object codeObject, ExportTask context);
}
