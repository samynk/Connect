package dae.fxcreator.io.type;

import dae.fxcreator.node.IOType;
import dae.fxcreator.util.ListHashMap;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Maintains the types for a specific template library
 * @author Koen
 */
public class ShaderTypeLibrary {
    private final ListHashMap<IOType> types = new ListHashMap<>();
    private final HashMap<String,ArrayList<IOType>> typeSets = new HashMap<>();

    /**
     * Creates a new ShaderType library object.
     */
    public ShaderTypeLibrary(){

    }

    /**
     * Returns the type that is associate with the key.
     * @param key the key for the shader type.
     * @return the ShaderType object.
     */
    public IOType getType(String key){
        return types.find(key);
    }

    /**
     * Adds a type to the library.
     * @param type the type to add.
     */
    public void addType(IOType type){
        types.addItem(type);
    }

    /**
     * Returns the number of available types.
     * @return the number of available types.
     */
    public int getSize() {
        return types.size();
    }

    /**
     * Gets the type at the specific location.
     * @param index the index for the type.
     * @return the ShaderType at the specified location.
     */
    public IOType getType(int index){
        return types.getItem(index);
    }

    /**
     * Adds a type to a set.
     * @param typeName the type to add.
     * @param typeSetName the name for the typeset.
     */
    public void addTypeToSet(String typeSetName, String typeName){
        ArrayList<IOType> sts = typeSets.get(typeSetName);
        if ( sts == null ){
            sts = new ArrayList<>();
            typeSets.put(typeSetName,sts);
        }
        IOType type = this.getType(typeName);
        sts.add(type);
    }

    /**
     * Checks if a shader type is a type from the given set.
     * @param typeSetName the name for the subset of types.
     * @param type the type to check.
     * @return true if the type is present in the subset, false otherwise.
     */
    public boolean isTypeFromSet(String typeSetName, IOType type) {
        ArrayList<IOType> sts = typeSets.get(typeSetName);
        if ( sts != null ){
            return sts.contains(type);
        }else
            return false;
    }
}
