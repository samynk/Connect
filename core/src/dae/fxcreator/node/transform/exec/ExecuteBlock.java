package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.transform.ExportTask;
import java.util.ArrayList;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ExecuteBlock implements Executable {

    private final String bufferName;
    private final ArrayList<Executable> executables = new ArrayList<>();

    /**
     * Creates a new ExecuteBlock with a given buffer name.
     *
     * @param bufferName the name of the buffer.
     */
    public ExecuteBlock(String bufferName) {
        this.bufferName = bufferName;
    }

    /**
     * Add an executable element to the list of elements to execute.
     *
     * @param exec the executable element.
     */
    public void addExecutable(Executable exec) {
        executables.add(exec);
    }

    /**
     * Executes the current statement.
     *
     * @param codeObject the linked node of this execute block.
     * @param context the ExportTask object.
     */
    @Override
    public void execute(Object codeObject, ExportTask context) {
        if (bufferName != null) {
            StringBuilder buffer = context.getCodeOutput().getBuffer(bufferName);
            context.pushBuffer(buffer);

        }
        for (Executable exec : executables) {
            exec.execute(codeObject, context);
        }
    }
}
