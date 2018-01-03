/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dae.fxcreator.io.type;

import dae.fxcreator.node.ShaderType;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Maintains the types for a specific template library
 * @author Koen
 */
public class ShaderTypeLibrary {
    private HashMap<String,ShaderType> typeMap = new HashMap<String,ShaderType>();
    private ArrayList<ShaderType> types = new ArrayList<ShaderType>();

    private HashMap<String,ArrayList<ShaderType>> typeSets = new HashMap<String,ArrayList<ShaderType>>();

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
    public ShaderType getType(String key){
        return typeMap.get(key);
    }

    /**
     * Adds a type to the library.
     * @param type the type to add.
     */
    public void addType(ShaderType type){
        typeMap.put(type.getType(), type);
        types.add(type);
    }

    /**
     * Returns the number of available types.
     * @return the number of available types.
     */
    public int getSize() {
        return typeMap.size();
    }

    /**
     * Gets the type at the specific location.
     * @param index the index for the type.
     */
    public ShaderType getType(int index){
        return types.get(index);
    }

    /**
     * Adds a type to a set.
     * @param typeName the type to add.
     * @param typeSetName the name for the typeset.
     */
    public void addTypeToSet(String typeSetName, String typeName){
        ArrayList<ShaderType> sts = typeSets.get(typeSetName);
        if ( sts == null ){
            sts = new ArrayList<ShaderType>();
            typeSets.put(typeSetName,sts);
        }
        ShaderType type = this.getType(typeName);
        sts.add(type);
    }

    /**
     * Checks if a shader type is a type from the given set.
     * @param typeSetName the name for the subset of types.
     * @param type the type to check.
     * @return true if the type is present in the subset, false otherwise.
     */
    public boolean isTypeFromSet(String typeSetName, ShaderType type) {
        ArrayList<ShaderType> sts = typeSets.get(typeSetName);
        if ( sts != null ){
            return sts.contains(type);
        }else
            return false;
    }
}
