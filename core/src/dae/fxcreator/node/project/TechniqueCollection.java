package dae.fxcreator.node.project;

import dae.fxcreator.node.project.FXProject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.tree.TreeNode;

/**
 * This is a container for Technique objects and the passes that are
 * @author Koen
 */
public class TechniqueCollection implements TreeNode{
    private ArrayList<Technique> techniques = new ArrayList<Technique>();
    private HashMap<String, Technique> techniqueMap = new HashMap<String, Technique>();

    /**
     * The parent project of this TechniqueCollection object
     */
    private FXProject parent;
    /**
     * Creates a new TechniqueCollection object.
     */
    public TechniqueCollection(FXProject project){
        this.parent = project;
    }

    /**
     * Adds a technique to the list of techniques.
     * @param technique the technique to add.
     */
    public void addTechnique(Technique technique){
        techniques.add(technique);
        techniqueMap.put(technique.getName(),technique);
        technique.setParent(this);
    }

    /**
     * Removes a technique from the list of techniques.
     * @param technique the technique to remove.
     */
    public void removeTechnique(Technique technique){
        techniques.remove(technique);
        techniqueMap.remove(technique.getName());
        technique.setParent(null);
    }

    /**
     * Returns the first technique of the collection, or null if it does not exist.
     *
     * @return the first technique of the collection or null.
     */
    public Technique getFirstTechnique(){
        if ( techniques.size()>0)
            return techniques.get(0);
        else
            return null;
    }

    /**
     * A String representation of the techniques collection.
     * @return the string "Techniques"
     */
    @Override
    public String toString(){
        return "Techniques";
    }

    /**
     * Returns an iterator of all the techniques in this collection;
     * @return the iterator of all the techniques.
     */
    Iterable<Technique> getTechniques() {
        return techniques;
    }

    /**
     * Returns the child at the specific index.
     * @param childIndex the index of the child.
     * @return the TreeNode object that is the child.
     */
    public TreeNode getChildAt(int childIndex) {
        return techniques.get(childIndex);
    }

    /**
     * Returns the number of children in this node.
     * @return the number of children.
     */
    public int getChildCount() {
        return techniques.size();
    }

    /**
     * Returns the parent of this treenode object.
     * @return null;
     */
    public TreeNode getParent() {
        return parent;
    }

    /**
     * Gets the index of a node in this Technique collection object.
     * @param node the number of nodes in the Technique collection object.
     * @return the number of nodes.
     */
    public int getIndex(TreeNode node) {
        return techniques.indexOf(node);
    }

    /**
     * Checks if this node allows children.
     * @return always true.
     */
    public boolean getAllowsChildren() {
        return true;
    }

    /**
     * Checks if this node is a leaf object.
     * @return always false.
     */
    public boolean isLeaf() {
        return false;
    }

    /**
     * Always return null, this functionality is not used anymore.
     * @return null
     */
    public Enumeration children() {
        return null;
    }

    /**
     * Create a unique technique name, this will start with "technique".
     * @return a unique technique name.
     */
    public String createUniqueTechniqueName() {
        String prefix = "technique";
        int i =0;
        String technique = "technique";
        do{
            technique = prefix + i;
            ++i;
        }while( techniqueMap.containsKey(technique));
        return technique;
    }
}
