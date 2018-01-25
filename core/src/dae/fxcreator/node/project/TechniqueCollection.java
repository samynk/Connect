package dae.fxcreator.node.project;

import dae.fxcreator.util.List;
import dae.fxcreator.util.ListHashMap;

/**
 * This is a container for Technique objects and the passes that are
 * @author Koen
 */
public class TechniqueCollection implements List{
    private final  ListHashMap<Technique> techniques = new ListHashMap<>();
    private String label = "Techniques";

    /**
     * The parent project of this TechniqueCollection object
     */
    private final FXProject parent;
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
        techniques.addItem(technique);
        technique.setParent(this);
    }

    /**
     * Removes a technique from the list of techniques.
     * @param technique the technique to remove.
     */
    public void removeTechnique(Technique technique){
        techniques.removeItem(technique);
        technique.setParent(null);
    }

    /**
     * Returns the first technique of the collection, or null if it does not exist.
     *
     * @return the first technique of the collection or null.
     */
    public Technique getFirstTechnique(){
        if ( techniques.size()>0)
            return techniques.first();
        else
            return null;
    }

   

    /**
     * Returns an iterator of all the techniques in this collection;
     * @return the iterator of all the techniques.
     */
    Iterable<Technique> getTechniques() {
        return techniques.items();
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
        }while( techniques.containsKey(technique));
        return technique;
    }
    
     /**
     * A String representation of the techniques collection.
     * @return the string "Techniques"
     */
    @Override
    public String toString(){
        return "Techniques";
    }

    @Override
    public Object getChild(int index) {
        return techniques.getItem(index);
    }

    @Override
    public int getChildCount() {
        return techniques.size();
    }

    @Override
    public int getIndexOfChild(Object child) {
        if ( child instanceof Technique )
        {
            return techniques.indexOf((Technique)child);
        }else{
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
}
