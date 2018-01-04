package dae.fxcreator.node.graph.math;

/**
 *
 * @author Koen
 */
public interface MathGUIElement {

    /**
     * Returns the parent of this math container
     * @return the parent of the mathcontainer
     */
    public MathContainer getParentContainer();
    /**
     * Sets the parent of this math container.
     * @param parent the parent of the math container.
     */
    public void setParentContainer(MathContainer parent);

    /**
     * Sets the selection status of this math element.
     * @param b true if the element should be selected , false otherwise.
     */
    public void setSelected(boolean b);

    /**
     * Sets the math context for this element.
     */
    public void setMathContext(MathContext context);
    /**
     * Removes the element from its parent container.
     */
    public void removeFromParent();

    /**
     * Returns the base line for this math element.
     * @return the base line for the math element.
     */
    public int getBaseLine();
    /**
     * Adjust the baselines of all the elements in the current formula.
     */
    public void adjustBaseLine();
    /**
     * Converts the element to code.
     */
    public void convertToCode(StringBuffer sb);
    /**
     * Checks if the math element is a group or not.
     * @return true if the math element is a group , false otherwise.
     */
    public boolean isGroup();

    /**
     * Create a MathElement for this MathGUIElement.
     * @return
     */
    public MathElement createFormula();
    /**
     * Set the gui element locked (useful for keeping the equals operation.
     */
    public void setLocked();
    /** 
     * Queries the state of the locked property. If this element is locked
     * no changes to the structure are allowed.
     */
    public boolean isLocked();
}
