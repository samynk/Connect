package dae.fxcreator.io;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contains all the shader stages in the project.
 * @author Koen
 */
public class ShaderStageCollection {
    private ArrayList<ShaderStage> stages = new ArrayList<ShaderStage>();
    private HashMap<String, ShaderStage> stageMap = new HashMap<String,ShaderStage>();
    /**
     * Creates a new ShaderStageCollection object.
     */
    public ShaderStageCollection(){

    }

    /**
     * Adds a ShaderStage object to this collection.
     * @param shaderstage the shader stage to add.
     */
    public void addShaderStage(ShaderStage stage){
        stages.add(stage);
        stageMap.put(stage.getId(),stage);
    }

    /**
     * Finds a ShaderStage in the project.
     * @param id the id of the shader stage.
     * @return the ShaderStage object.
     */
    public ShaderStage findShaderStage(String id){
        return stageMap.get(id);
    }

    /**
     * Removes a ShaderStage object from the collection.
     * @param shaderstage the shader stage to remove.
     */
    public void removeShaderStage(ShaderStage stage){
        stages.remove(stage);
        stageMap.remove(stage.getId());
    }

    /**
     * Creates a unique shader stage name.
     * @param prefix the prefix for the shader stage name.
     */
    public String createUniqueShaderStageName(String prefix){
        String name = prefix;
        int i = 0;
        do{
            name = prefix + i;
            ++i;
        }while (stageMap.containsKey(name));
        return name;
    }

    /**
     * Returns an iterator to walk through all the stages.
     * @return the iterator.
     */
    public Iterable<ShaderStage> getList() {
        return stages;
    }
}
