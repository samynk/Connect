package dae.fxcreator.io.codegen.parser.exec;

import dae.fxcreator.io.codegen.ExportTask;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class WriteValue implements Executable {

    private final Object value;

    public WriteValue(Object value) {
        this.value = value;
    }

    @Override
    public void execute(Object codeObject, ExportTask context) {
        StringBuilder sb = context.peekBuffer();
        sb.append(value.toString());
    }
}
