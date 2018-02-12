package dae.fxcreator.node.graphmath;

//import dae.fxcreator.io.codegen.MathFormulaCodeGenerator;
import dae.fxcreator.io.type.IOTypeLibrary;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.IOType;
import dae.fxcreator.node.TypedNode;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * The root node for a math formula.
 *
 * @author Koen
 */
public class MathFormula extends MathElement implements TypedNode {

    private final ArrayList<MathElement> roots = new ArrayList<>();

    public MathFormula() {
    }

    MathFormula(MathElement root) {
        roots.add(root);
    }
    
      /**
     * Returns the type of the result of this operation.
     *
     * @param node the node that hosts this MathElement object.
     * @param library the library with shader types and their priority when up
     * and down casting.
     * @return the type of the result.
     */
    @Override
    public IOType getResultType(IONode node, IOTypeLibrary library) {
        return library.getType("VOID");
    }

    /**
     * Returns the root element.
     *
     * @return
     */
    public Iterable<MathElement> getRoots() {
        return roots;
    }

    /**
     * Returns the number of roots.
     *
     * @return the number of roots.
     */
    public int getNrOfRoots() {
        return roots.size();
    }

    /**
     * Sets the root element for this MathFormula.
     *
     * @param root the root element for the formula.
     */
    public void addRoot(MathElement root) {
        roots.add(root);
    }

    /**
     * Returns the MathElement with the given index.
     *
     * @param index the index of the element.
     * @return a MathElement at the given index, or null if it does not exist.
     */
    public MathElement getRoot(int index) {
        if (index < roots.size()) {
            return roots.get(index);
        } else {
            return null;
        }
    }

    @Override
    public void addMathElement(MathElement element) {
        roots.add(element);
    }

    @Override
    public void addOperator(Operation op) {
        // no operator allowed.
    }

    @Override
    public Dimension calculateSize(Graphics2D g2d) {
        // mathformula is vertically oriented.
        size.setSize(0, 0);
        for (MathElement element : roots) {
            Dimension eSize = element.calculateSize(g2d);
            if (eSize.width > size.width) {
                size.width = eSize.width;
            }
            size.height += eSize.height;
        }
        size.width+=4;
        return size;
    }

    @Override
    public void render(Graphics2D g2d, int x, int y, int width, int height) {

        // mathformula is vertically oriented.
        for (MathElement element : roots) {
            int offsetX = 2; // (width - element.size.width) / 2;
            element.render(g2d, x + offsetX, y, element.size.width, element.size.height);

            y += element.size.height;
        }
    }

    public void paint(Graphics2D g2d) {
        Dimension size = calculateSize(g2d);
        render(g2d, 0, 0, size.width, size.height);
    }

    @Override
    public String getId() {
        return "math";
    }

    @Override
    public String getType() {
        return "math.formula";
    }
}
