package dae.fxcreator.node.graphmath;

import dae.fxcreator.io.type.ShaderTypeLibrary;
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
public class MathIntValue extends MathElement {

    private final int value;

    public MathIntValue(int value) {
        this.value = value;
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
    public IOType getResultType(IONode node, ShaderTypeLibrary library) {
        return library.getType("INT");
    }

    public int getValue() {
        return value;
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
        g2d.drawString(Integer.toString(value), x, y + baseLine);
    }

    @Override
    public Dimension calculateSize(Graphics2D g2d) {
        FontMetrics fm = g2d.getFontMetrics();
        String text = Integer.toString(value);
        LineMetrics lm = fm.getLineMetrics(text, g2d);
        size.width = fm.stringWidth(text);
        size.height = (int) lm.getHeight();
        baseLine = (int) lm.getAscent() + 1;
        return size;
    }
    
     @Override
    public String getId() {
        return "int";
    }

    @Override
    public String getType() {
        return "value.INT";
    }
}
