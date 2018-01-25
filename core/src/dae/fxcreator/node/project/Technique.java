package dae.fxcreator.node.project;

import dae.fxcreator.node.TypedNode;
import dae.fxcreator.node.ShaderNode;
import dae.fxcreator.util.Key;
import dae.fxcreator.util.List;
import java.util.ArrayList;

/**
 * The shader class that contains the information about shader passes.
 *
 * @author Koen
 */
public class Technique implements TypedNode, Key, List{

    private final ArrayList<Pass> passes = new ArrayList<>();
    private String name;
    private FXProject project;
    /**
     * The TechniqueCollection that contains this technique;
     */
    private TechniqueCollection parent;

    /**
     * Constructs a new shader object with a given name.
     *
     * @param parent the FXProject parent.
     * @param name the name for this shader.
     */
    public Technique(FXProject parent, String name) {
        this.project = parent;
        this.name = name;
    }

    @Override
    public String getKey() {
        return name;
    }

    /**
     * Set the technique collection that is the parent of this Technique.
     *
     * @param parent the techniquecollection that is the parent.
     */
    public void setParent(TechniqueCollection parent) {
        this.parent = parent;
    }

    /**
     * Returns the name of this shader.
     *
     * @return the name of the shader.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for this shader.
     *
     * @param name the new name for the shader.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the id of the pass, which is the same as the name.
     *
     * @return the id, a synonum for the name.
     */
    public String getId() {
        return name;
    }

    /**
     * Adds a pass to this shader object.
     *
     * @param pass the pass to add.
     */
    public void addPass(Pass pass) {
        this.passes.add(pass);
        pass.setParent(this);
    }

    /**
     * Removes a pass from this pass object.
     *
     * @param p the pass to remove.
     */
    public void removePass(Pass p) {
        this.passes.remove(p);
        p.setParent(null);
    }
    
    public boolean hasPasses(){
        return passes.size() > 0;
    }

    /**
     * Return the FXProject parent of this pass.
     *
     * @return the FXProject that is the parent of this pass.
     */
    public FXProject getFXProject() {
        return this.project;
    }

    /**
     * Returns the passes in this technique.
     *
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
     *
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
     *
     * @param id the global node.
     * @return the global node with the specified id.
     */
    public ShaderNode findGlobalNode(String id) {
        return project.findGlobalNode(id);
    }
    
     @Override
    public Object getChild(int index) {
        return passes.get(index);
    }

    @Override
    public int getChildCount() {
        return passes.size();
    }

    @Override
    public int getIndexOfChild(Object child) {
        return passes.indexOf(child);
    }

    @Override
    public void setLabel(String name) {
        this.name = name;
    }

    @Override
    public String getLabel() {
        return name;
    }

    /**
     * Returns the type of this object.
     *
     * @return
     */
    public String getType() {
        return "technique";
    }

    /**
     * Returns a string representation of this technique.
     *
     * @return the string representation of the technique.
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
