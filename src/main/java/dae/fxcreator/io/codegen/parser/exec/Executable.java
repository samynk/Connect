package dae.fxcreator.io.codegen.parser.exec;

import dae.fxcreator.io.codegen.ExportTask;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public interface Executable {
    public void execute(Object codeObject, ExportTask context);
}
