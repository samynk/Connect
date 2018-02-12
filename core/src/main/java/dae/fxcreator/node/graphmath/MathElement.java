package dae.fxcreator.node.graphmath;

import dae.fxcreator.io.type.ShaderTypeLibrary;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.IOType;
import dae.fxcreator.node.TypedNode;
import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 * A math element that can be used to build complex mathematical
 * instructions.
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public abstract class MathElement implements TypedNode{
    /**
     * the size of the math element.
     */
    protected final Dimension size = new Dimension();
    /**
     * The baseline of the math element, useful for rendering.
     */
    protected int baseLine;
    /**
     * Indicates if the math element can be changed, true if the
     * math element can be changed.
     */
    private boolean locked = false;
    
    /**
     * Returns the type of the result of this operation.
     * @param node the node that hosts this MathElement object.
     * @param library the library of types.
     * @return the type of the result.
     * 
     */
    public abstract IOType getResultType(IONode node, ShaderTypeLibrary library);
    
    
    /**
     * Returns true if the element does not accept child elements.
     * @return true if the element is a leaf, false otherwise.
     */
    public boolean isLeaf(){
        return true;
    }
    
    /**
     * Checks if this element can be changed.
     * @return true if the element can be changed, false otherwise.
     */
    public boolean isLocked(){
        return locked;
    }
    
    public void setLocked(){
        locked = true;
    }
    
    public void setUnlocked(){
        locked = false;
    }

    public void writeToXML(StringBuffer sb, int tabs){
        sb.append("</mathelement>\n");
    }

    public abstract void addMathElement(MathElement element);
    public abstract void addOperator(Operation op);

    protected void writeTabs(StringBuffer sb, int tabs){
        for (int i = 0; i < tabs; ++i){
            sb.append("\t");
        }
    }
    
    public abstract void render(Graphics2D g2d, int x, int y, int width, int height);

    public abstract Dimension calculateSize(Graphics2D g2d);
}
