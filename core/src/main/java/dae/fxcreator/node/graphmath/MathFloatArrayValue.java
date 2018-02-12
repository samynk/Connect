package dae.fxcreator.node.graphmath;

import dae.fxcreator.io.type.IOTypeLibrary;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.IOType;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class MathFloatArrayValue extends MathElement {

    private final float[] values;
    private String text = "";

    public MathFloatArrayValue(float[] values) {
        this.values = values;
        if (values != null) {
            createText();
        }
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
        return library.getType("BOOLEAN");
    }

    public float[] getValues() {
        return values;
    }

    private void createText() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < values.length; ++i) {
            sb.append(Float.toString(values[i]));
            sb.append(",");
        }
        sb.setCharAt(sb.length() - 1, ']');
    }

    @Override
    public void addMathElement(MathElement element) {
        // no children.
    }

    @Override
    public void addOperator(Operation op) {

    }

    @Override
    public void render(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.drawString(text, x, y + baseLine);
    }

    @Override
    public Dimension calculateSize(Graphics2D g2d) {
        FontMetrics fm = g2d.getFontMetrics();
        LineMetrics lm = fm.getLineMetrics(text, g2d);
        size.width = fm.stringWidth(text);
        size.height = (int) lm.getHeight();
        baseLine = (int) lm.getAscent() + 1;
        return size;
    }
    
    @Override
    public String getId() {
        return "floatarray";
    }

    @Override
    public String getType() {
        return "floatarray";
    }

   
}
