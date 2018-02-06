package dae.fxcreator.node.graphmath;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class MathBooleanValue extends MathElement {

    private final boolean value;

    public MathBooleanValue(boolean value) {
        this.value = value;
    }
    
    public boolean getValue(){
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
        g2d.drawString(Boolean.toString(value), x, y + baseLine);
    }

    @Override
    public Dimension calculateSize(Graphics2D g2d) {
        FontMetrics fm = g2d.getFontMetrics();
        String text = Boolean.toString(value);
        LineMetrics lm = fm.getLineMetrics(text, g2d);
        size.width = fm.stringWidth(text);
        size.height = (int) lm.getHeight();
        baseLine = (int) lm.getAscent() + 1;
        return size;
    }
    
     @Override
    public String getId() {
        return "boolean";
    }

    @Override
    public String getType() {
        return "boolean";
    }
}
