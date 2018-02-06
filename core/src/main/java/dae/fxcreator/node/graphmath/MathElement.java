
package dae.fxcreator.node.graphmath;

import dae.fxcreator.node.TypedNode;
import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 *
 * @author Koen
 */
public abstract class MathElement implements TypedNode{
    protected Dimension size = new Dimension();
    protected int baseLine;
    private boolean locked = false;
    
    public boolean isLeaf(){
        return true;
    }
    
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

    //public abstract void build(StringBuilder result, MathFormulaCodeGenerator codeGenerator);
}
