package dae.fxcreator.node.project;

import dae.fxcreator.node.TypedNode;
import dae.fxcreator.node.IONode;
import java.util.ArrayList;

/**
 * This object describes a pass declaration for a shader.
 * @author Koen Samyn
 */
public class Pass implements TypedNode {

    /**
     * The name for the pass object.
     */
    private String name;
    /**
     * The technique that is the parent of this pass object.
     */
    private Technique parent;
    /**
     * The list of ShaderStage objects.
     */
    private final ArrayList<ShaderStage> stages = new ArrayList<>();
    /**
     * The rasterizer state of this pass.
     */
    private IONode rasterizerState;

    /**
     * Creates a new Pass object.
     * @param name the name for the pass object.
     */
    public Pass(String name) {
        this.name = name;
    }

    /**
     * Sets the parent technique of this pass.
     * @param parent the new parent technique for this pass.
     */
    public void setParent(Technique parent) {
        this.parent = parent;
    }

    /**
     * Returns the name for the pass object.
     * @return the name for the pass object.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for the pass object.
     * @param name the new name for the pass object.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the id of the pass, which is the same
     * as the name.
     * @return the id, a synonum for the name.
     */
    @Override
    public String getId() {
        return name;
    }

    /**
     * Adds a shader stage to the list of shader stages.
     * @param toAdd the shader stage to add.
     */
    public void addShaderStage(ShaderStage toAdd) {
        String toAddType = toAdd.getType();
        String toAddId = toAdd.getId();
        ShaderStage toRemove = null;
        for (ShaderStage stage : stages) {
            String stageType = stage.getType();
            String stageId = stage.getId();
            if (toAddType.equals(stageType) && !toAddId.equals(stageId)) {
                toRemove = stage;
                stages.add(toAdd);
            }
        }
        if (toRemove != null) {
            int index = stages.indexOf(toRemove);
            if (index > -1) {
                stages.remove(toRemove);
                // notifyRemoved(toRemove, index);
            }
        } else {
            stages.add(toAdd);
            // notifyAdded(toAdd);
        }
    }

    /*
    private void notifyRemoved(ShaderStage toRemove, int index) {
        if (this.parent != null && parent.getFXProject() != null) {
            FXProject root = parent.getFXProject();
            if (index > -1) {
                Object[] path = new Object[4];
                path[0] = root;
                path[1] = parent;
                path[2] = this;
                path[3] = toRemove;
                TreePath tp = new TreePath(path);
                root.notifyNodeAdded(toRemove, tp, index);
            }
        }
    }

    private void notifyAdded(ShaderStage toAdd) {
        if (this.parent != null && parent.getFXProject() != null) {
            FXProject root = parent.getFXProject();
            int index = this.getIndex(toAdd);
            if (index > -1) {
                Object[] path = new Object[4];
                path[0] = root;
                path[1] = parent.getParent();
                path[2] = parent;
                path[3] = this;
                //System.out.println(path.getPath().length);
                root.notifyNodeAdded(toAdd, new TreePath(path), index);
            }
        }
    }
    */

    /**
     * Checks if a stage is present in this pass.
     * @param toCheck the ShaderStage object to check.
     * @return true if the stage is present , false otherwise.
     */
    public boolean hasStage(ShaderStage toCheck) {
        for (ShaderStage stage : stages) {
            if (stage.equals(toCheck)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the list of shader stages.
     * @return the list of shader stages.
     */
    public ArrayList<ShaderStage> getStages() {
        return stages;
    }

    /**
     * Returns the previous stage to this stage.
     * @param stage the stage you want to look
     * @return the previous shader stage, null if the ShaderStage is the first.
     */
    public ShaderStage previousStage(ShaderStage stage) {
        int index = stages.indexOf(stage);
        if (index > 0) {
            return stages.get(index - 1);
        } else {
            return null;
        }
    }

    /**
     * Finds a node inside this stage.
     * @param key the node to add.
     * @return the IONode object with the specified key.s
     */
    public IONode findNode(String key) {
        for (ShaderStage stage : stages) {
            IONode node = stage.findNode(key);
            if (node != null) {
                return node;
            }
        }
        return null;
    }

    /**
     * Returns the rasterizer state for this pass.
     * @return the rasterizerState
     */
    public IONode getRasterizerState() {
        return rasterizerState;
    }

    /**
     * Sets the rasterizer state for the pass.
     * @param rasterizerState the rasterizerState to set
     */
    public void setRasterizerState(IONode rasterizerState) {
        this.rasterizerState = rasterizerState;
    }

    /**
     * Checks if this pass has a rasterizer state.
     * @return true if this pass has a rasterizer state, false otherwiser.
     */
    public boolean hasRasterizerState() {
        return rasterizerState != null;
    }

    /**
     * Returns the type of the object.
     * @return the type of the object.
     */
    public String getType() {
        return "pass";
    }

    /**
     * Returns a String representation of the object.
     * @return the string representation of the objecT.
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Removes a shader stage from this pass.
     * @param stage
     */
    public void removeShaderStage(ShaderStage stage) {
        stages.remove(stage);
    }
}
