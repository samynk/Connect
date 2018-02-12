package dae.fxcreator.node.project;

import dae.fxcreator.node.IOStruct;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The ShaderStruct collection provides support to manage ShaderStructs and
 * makes it possible to create and use ShaderStruct objects.
 *
 * @author Koen
 */
public class ShaderStructCollection {

    private final HashMap<String, IOStruct> structMap = new HashMap<>();
    private final ArrayList<IOStruct> structs = new ArrayList<>();
    private final FXProject parent;

    /**
     * Maintains the collection of shader structs.
     * @param parent the parent project.
     */
    public ShaderStructCollection(FXProject parent) {
        this.parent = parent;
    }

    /**
     * Creates a unique name for a new struct.
     *
     * @return a unique name for the new struct.
     */
    public String createUniqueName() {
        String prefix = "struct";
        String name = prefix;
        int i = 0;
        do {
            name = prefix + (i++);
        } while (structMap.containsKey(name));
        return name;
    }

    /**
     * Adds a ShaderStruct to the list of structs.
     *
     * @param struct the struct to add.
     */
    public void addShaderStruct(IOStruct struct) {
        this.structs.add(struct);
        structMap.put(struct.getId(), struct);
    }

    /**
     * Removes a ShaderStruct from the list of structs.
     *
     * @param struct
     */
    public void removeShaderStruct(IOStruct struct) {
        this.structs.remove(struct);
        structMap.remove(struct.getId());
    }

    /**
     * Returns a string representation of this ShaderStructCollection object.
     *
     * @return the string representation of the ShaderStructCollection.
     */
    @Override
    public String toString() {
        return "Structs";
    }

    Iterable<IOStruct> getStructs() {
        return structs;
    }

    IOStruct getStruct(String structid) {
        return structMap.get(structid);
    }
}
