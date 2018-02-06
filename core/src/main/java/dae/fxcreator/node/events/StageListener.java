package dae.fxcreator.node.events;

import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.node.project.ShaderStage;

/**
 * Listener interface for objects that are interested in changes
 * to the ShaderStage objects in the project.
 * @author Koen
 */
public interface StageListener {
    /**
     * Called when a stage was added to the project.
     * @param project the project that the stage belongs to.
     * @param stage the stage that was added.
     */
    public void stageAdded(FXProject project, ShaderStage stage);
    /**
     * Called when a stage was removed from the project.
     * @param project the project that the stage belongs to.
     * @param stage the stage that was removed.
     */
    public void stageRemoved(FXProject project, ShaderStage stage);
}
