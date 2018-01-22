package dae.fxcreator.node.project;

import dae.fxcreator.node.TypedNode;
import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.node.ShaderNode;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;

/**
 * The shader class that contains the information about shader passes.
 * @author Koen
 */
public class Technique implements TypedNode, TreeNode {

    private ArrayList<Pass> passes = new ArrayList<Pass>();
    private String name;
    private FXProject project;
    /**
     * The TechniqueCollection that contains this technique;
     */
    private TechniqueCollection parent;

    /**
     * Constructs a new shader object with a given name.
     * @param parent the FXProject parent.
     * @param name the name for this shader.
     */
    public Technique(FXProject parent, String name) {
        this.project = parent;
        this.name = name;
    }

    /**
     * Set the technique collection that is the parent of this Technique.
     * @param parent the techniquecollection that is the parent.
     */
    public void setParent(TechniqueCollection parent){
        this.parent = parent;
    }

    /**
     * Returns the name of this shader.
     * @return the name of the shader.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for this shader.
     * @param name the new name for the shader.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the id of the pass, which is the same
     * as the name.
     * @return the id, a synonum for the name.
     */
    public String getId() {
        return name;
    }

    /**
     * Adds a pass to this shader object.
     * @param pass the pass to add.
     */
    public void addPass(Pass pass) {
        this.passes.add(pass);
        pass.setParent(this);
    }

    /**
     * Removes a pass from this pass object.
     * @param p the pass to remove.
     */
    public void removePass(Pass p) {
        this.passes.remove(p);
        p.setParent(null);
    }

    /**
     * Return the FXProject parent of this pass.
     * @return the FXProject that is the parent of this pass.
     */
    public FXProject getFXProject() {
        return this.project;
    }

    /**
     * Returns the passes in this technique.
     * @return the passes.
     */
    public ArrayList<Pass> getPasses() {
        return passes;
    }

    /**
     *
     * @return
     */
    public String createUniquePassName() {
        String prefix = "pass";
        String passName = "pass0";
        int i = 0;
        do {
            passName = prefix + i;
            ++i;
        } while (findPass(passName) != null);
        return passName;
    }

    /**
     *
     * @param name
     * @return
     */
    public Pass findPass(String name) {
        for (Pass p : this.passes) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns the first pass of this Technique object.
     * @return the first pass.
     */
    public Pass getFirstPass() {
        if (passes.size() > 0) {
            return passes.get(0);
        } else {
            return null;
        }
    }

    /**
     * Finds a global node in the project.
     * @param id the global node.
     * @return the global node with the specified id.
     */
    public ShaderNode findGlobalNode(String id) {
        return project.findGlobalNode(id);
    }

    /**
     * Returns the type of this object.
     * @return
     */
    public String getType() {
        return "technique";
    }

    /**
     * Gets a child at the specific index.
     * @param childIndex the index of the child.
     * @return the child at the specific index.
     */
    public TreeNode getChildAt(int childIndex) {
        return passes.get(childIndex);
    }

    /**
     * Returns the number of passes in this technique.
     * @return the number of passes.
     */
    public int getChildCount() {
        return passes.size();
    }

    /**
     * Returns the parent of this object, not used.
     * @return always null.
     */
    public TreeNode getParent() {
        return parent;
    }

    /**
     * Returns the index of a child object.
     * @param node the node to get the index of.
     * @return the index.
     */
    public int getIndex(TreeNode node) {
        return passes.indexOf(node);
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
     * Returns an enumeration of all children, not implemented and not used.
     * @return null
     */
    public Enumeration children() {
        return null;
    }

    /**
     * Returns a string representation of this technique.
     * @return the string representation of the technique.
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
