/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dae.fxcreator.node;

/**
 * Provides support for semantics.
 * @author Koen
 */
public class Semantic {
    private String semantic;
    private boolean isValid = false;

    /**
     * Creates a new empty semantic object.
     */
    public Semantic(){
        isValid = false;
    }

    /**
     * Creates a new semantic object.
     * @param semantic the value for the semantic.
     */
    public Semantic(String semantic){
        setValue(semantic);
       
    }

    /**
     * Returns the value for the semantic.
     * @return the value for the semantic.
     */
    public String getValue(){
        return semantic;
    }

    /**
     * Sets the value for the semantic.
     * @param semantic the value for the semantic.
     */
    public void setValue(String semantic){
        this.semantic = semantic;
        if (this.semantic == null || semantic.length() == 0){
            isValid = false;
        }else
            isValid = true;
    }

    /**
     * Returns the validity of this semantic.
     * @return true if this semantic is valid, false otherwise.
     */
    public boolean isValid(){
        return isValid;
    }

    /**
     * Checks if this Semantic is the same as another semantic.
     * @param semantic the semantic to check.
     * @return true if the semantics are the same,false otherwise.
     */
    @Override
    public boolean equals(Object semantic){
        if (semantic instanceof Semantic){
            Semantic s = (Semantic)semantic;
            if (isValid && s.isValid )
                return s.semantic.equals(this.semantic);
            else
                return false;
        }else{
            return false;
        }
    }

    /**
     * Returns a hash code for this semantic object.
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.semantic != null ? this.semantic.hashCode() : 0);
        return hash;
    }

    /**
     * Returns the value for the semantic.
     */
    @Override
    public String toString(){
        if ( isValid )
            return semantic;
        else
            return "<not set>";
    }

    /**
     * Checks if the semantic of this Semantic object is equal to
     * the given string.
     * @param semantic the semantic value.
     * @return true if the semantic is the same, false otherwise.
     */
    public boolean eq(String semantic) {
        if ( ! isValid)
            return false;
        else
            return this.semantic.equals(semantic);
    }
}
