package dae.fxcreator.node.project;

import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.node.ShaderStruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.tree.TreeNode;

/**
 * The ShaderStruct collection provides support to manage ShaderStructs and makes it
 * possible to create and use ShaderStruct objects.
 * @author Koen
 */
public class ShaderStructCollection implements javax.swing.tree.TreeNode{
    private HashMap<String,ShaderStruct> structMap= new HashMap<String,ShaderStruct>();
    private ArrayList<ShaderStruct> structs = new ArrayList<ShaderStruct>();
    private FXProject parent;

    /**
     * Maintains the collection of shader structs.
     */
    public ShaderStructCollection(FXProject parent){
        this.parent = parent;
    }

    /**
     * Creates a unique name for a new struct.
     * @return a unique name for the new struct.
     */
    public String createUniqueName(){
        String prefix = "struct";
        String name = prefix;
        int i =0 ;
        do{
            name = prefix + (i++);
        }while (structMap.containsKey(name));
        return name;
    }

    /**
     * Adds a ShaderStruct to the list of structs.
     * @param struct the struct to add.
     */
    public void addShaderStruct(ShaderStruct struct){
        this.structs.add(struct);
        structMap.put(struct.getId(), struct);
    }

    /**
     * Removes a ShaderStruct from the list of structs.
     * @param struct
     */
    public void removeShaderStruct(ShaderStruct struct){
        this.structs.remove(struct);
        structMap.remove(struct.getId());
    }

    /**
     * Returns the child object at the specific index.
     * @param childIndex the index of the child.
     * @return the TreeNode at the specified index
     */
    public TreeNode getChildAt(int childIndex) {
        return structs.get(childIndex);
    }

    /**
     * Returns the number of children.
     * @return the number of children.
     */
    public int getChildCount() {
       return structs.size();
    }

    /**
     * Returns the parent node.
     * @return the parent node.
     */
    public TreeNode getParent() {
       return parent;
    }

    /**
     * Returns the index of the specified node.
     * @param node the node to get the index of.
     * @return the index of the node.
     */
    public int getIndex(TreeNode node) {
        return structs.indexOf(node);
    }

    /**
     * This node allows child objects.
     * @return always true.
     */
    public boolean getAllowsChildren() {
        return true;
    }

    /**
     * This node is not a leaf.
     * @return always false.
     */
    public boolean isLeaf() {
        return false;
    }

    /**
     * Returns an enumeration of the child objects.
     * @return an enumeration of the child objects.
     */
    public Enumeration children() {
        return Collections.enumeration(structs);
    }

    /**
     * Returns a string representation of this ShaderStructCollection object.
     * @return the string representation of the ShaderStructCollection.
     */
    @Override
    public String toString(){
        return "Structs";
    }

    Iterable<ShaderStruct> getStructs() {
        return structs;
    }

    ShaderStruct getStruct(String structid) {
        return structMap.get(structid);
    }
}
