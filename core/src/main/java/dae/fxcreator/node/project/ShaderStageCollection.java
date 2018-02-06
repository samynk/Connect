package dae.fxcreator.node.project;

import dae.fxcreator.node.NodeGroup;
import dae.fxcreator.util.List;
import dae.fxcreator.util.ListHashMap;

/**
 * Contains all the shader stages in the project.
 *
 * @author Koen
 */
public class ShaderStageCollection implements List {

    private final ListHashMap<ShaderStage> stageMap = new ListHashMap<>();
    private final FXProject parent;
    private String label = "Modules";

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
     * @return a unique shader stage name.
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
    public Object getChild(int index) {
        return stageMap.getItem(index);
    }

    @Override
    public int getChildCount() {
        return stageMap.size();
    }

    @Override
    public int getIndexOfChild(Object child) {
        if (child instanceof ShaderStage) {
            return stageMap.indexOf((ShaderStage)child);
        } else {
            return -1;
        }
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
    
    /**
     * Returns the label of this ShaderStageCollection.
     * @return the label for this collection.
     */
    @Override
    public String toString(){
        return label;
    }
}
