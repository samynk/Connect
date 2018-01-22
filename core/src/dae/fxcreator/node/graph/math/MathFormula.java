package dae.fxcreator.node.graph.math;

//import dae.fxcreator.io.codegen.MathFormulaCodeGenerator;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * The root node for a math formula.
 * @author Koen
 */
public class MathFormula extends MathElement {

    private ArrayList<MathElement> roots = new ArrayList<MathElement>();
    

    public MathFormula() {
    }

    MathFormula(MathElement root) {
        roots.add(root);
    }

    /**
     * Returns the root element.
     * @return
     */
    public Iterable<MathElement> getRoots() {
        return roots;
    }

    /**
     * Sets the root element for this MathFormula.
     * @param root the root element for the formula.
     */
    public void addRoot(MathElement root) {
        roots.add(root);
    }

    @Override
    public void addMathElement(MathElement element) {
        roots.add(element);
    }

    @Override
    public void addOperator(Operation op) {
        // no operator allowed.
    }

    public Dimension calculateSize(Graphics2D g2d) {
        // mathformula is vertically oriented.
        size.setSize(0,0);
        for ( MathElement element: roots)
        {
            Dimension eSize = element.calculateSize(g2d);
            if ( eSize.width > size.width){
                size.width = eSize.width;
                size.height += eSize.height;
            }
        }
        return size;
    }

    @Override
    public void render(Graphics2D g2d, int x, int y, int width, int height) {
        
        // mathformula is vertically oriented.
        for ( MathElement element: roots)
        {
            int offsetX = (width - element.size.width)/2;
            element.render(g2d, x + offsetX , y, element.size.width, element.size.height);
            
            y+= element.size.height;
        }
    }

    public void paint(Graphics2D g2d){
        Dimension size = calculateSize(g2d);
        render(g2d,0,0,size.width,size.height);
    }

    /**
     * Generates the code for the math formula, using a specific MathFormulaCodeGenerator object.
     * @param result
     * @param aThis
     * @return 
     */
    /*
    public void build(StringBuilder result, MathFormulaCodeGenerator codeGenerator) {
        for(MathElement element: this.roots)
        {
            element.build(result,codeGenerator);
            result.append('\n');
        }
    }
    */
}
