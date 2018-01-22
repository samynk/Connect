package dae.fxcreator.node.project;

import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.node.ShaderNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.tree.TreeNode;

/**
 * This object contains the collection of states in a project.
 * @author Koen
 */
public class StatesCollection implements TreeNode {

    private HashMap<String, ShaderNode> stateMap = new HashMap<String, ShaderNode>();
    private ArrayList<ShaderNode> states = new ArrayList<ShaderNode>();
    private FXProject parent;

    /**
     * Creates a new StatesCollection object.
     * @param parent the parent FXProject object.
     */
    public StatesCollection(FXProject parent) {
        this.parent = parent;
    }
    
    /**
     * Creates a unique shader stage name.
     * @param prefix the prefix for the shader stage name.
     */
    public String createUniqueStateName(String prefix){
        String name = prefix;
        int i = 0;
        do{
            name = prefix + i;
            ++i;
        }while (stateMap.containsKey(name));
        return name;
    }

    /**
     * Adds a state node to the list of nodes.
     * @param node the node to add to the list of states.
     */
    public void addState(ShaderNode node){
        states.add(node);
        stateMap.put(node.getId(),node);
    }

    /**
     * Removes a state node from the list of nodes.
     * @param node the node to remove.
     */
    public void removeState(ShaderNode node){
        states.remove(node);
        stateMap.remove(node.getId());
    }

    /**
     * Returns the list of state objects.
     * @param return the list of state objects.
     */
    public Iterable<ShaderNode> getStates(){
        return states;
    }

    /**
     * Returns the child node at the provided index.
     * @param childIndex the index of the child.
     * @return the object at the specific child node.
     */
    public TreeNode getChildAt(int childIndex) {
        return states.get(childIndex);
    }

    /**
     * Returns the number of children in this node.
     * @return the number of children.
     */
    public int getChildCount() {
        return states.size();
    }

    /**
     * Returns the parent of this node.
     * @return the node of the parent.
     */
    public TreeNode getParent() {
        return parent;
    }

    /**
     * Gets the index of a given child.
     * @param node the node to get the index of.
     * @return the index of the child node.
     */
    public int getIndex(TreeNode node) {
        return states.indexOf(node);
    }

    /**
     * Checks if this element allows children.
     * @return true
     */
    public boolean getAllowsChildren() {
        return true;
    }

    /**
     * Checks if this is a leaf element.
     * @return always true.
     */
    public boolean isLeaf() {
        return true;
    }

    /**
     * not implemented.
     * @return
     */
    public Enumeration children() {
        return null;
    }

    /**
     * Finds a state with the specified id.
     * @param id the id to find a node for.
     * @return the ShaderNode with the specified id.
     */
    public ShaderNode findState(String id) {
        return stateMap.get(id);
    }

    /**
     * Returns a string representation for this collection.
     * @return the states
     */
    public String toString(){
        return "states";
    }
}
