package dae.fxcreator.node.project;

import dae.fxcreator.node.NodeGroup;
import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.util.ListHashMap;
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;

/**
 * Contains all the shader stages in the project.
 *
 * @author Koen
 */
public class ShaderStageCollection implements TreeNode {

    private final ListHashMap<ShaderStage> stageMap = new ListHashMap<>();
    private final FXProject parent;

    /**
     * Creates a new ShaderStageCollection object.
     *
     * @param parent the FXProject parent of this shader stage.
     */
    public ShaderStageCollection(FXProject parent) {
        this.parent = parent;
    }

    /**
     * Adds a ShaderStage object to this collection.
     *
     * @param stage the shader stage to add.
     */
    public void addShaderStage(ShaderStage stage) {
        stageMap.addItem(stage);
    }

    /**
     * Returns the first shader stage.
     *
     * @return the first shader stage.
     */
    public NodeGroup getFirstStage() {
        return stageMap.first();
    }

    /**
     * Finds a ShaderStage in the project.
     *
     * @param id the id of the shader stage.
     * @return the ShaderStage object.
     */
    public ShaderStage findShaderStage(String id) {
        return stageMap.find(id);
    }

    /**
     * Removes a ShaderStage object from the collection.
     *
     * @param stage the shader stage to remove.
     */
    public void removeShaderStage(ShaderStage stage) {
        stageMap.removeItem(stage);
    }

    /**
     * Creates a unique shader stage name.
     *
     * @param prefix the prefix for the shader stage name.
     */
    public String createUniqueShaderStageName(String prefix) {
        String name = prefix;
        int i = 0;
        do {
            name = prefix + i;
            ++i;
        } while (stageMap.containsKey(name));
        return name;
    }

    /**
     * Returns an iterator to walk through all the stages.
     *
     * @return the iterator.
     */
    public Iterable<ShaderStage> getList() {
        return stageMap.items();
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return stageMap.getItem(childIndex);
    }

    @Override
    public int getChildCount() {
        return stageMap.size();
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        if (node instanceof ShaderStage) {
            return stageMap.indexOf((ShaderStage)node);
        } else {
            return -1;
        }
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Enumeration children() {
        return stageMap.enumeration();
    }
}
