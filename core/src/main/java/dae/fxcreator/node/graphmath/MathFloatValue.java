package dae.fxcreator.node.graphmath;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class MathFloatValue extends MathElement{

    private final float value;

    public MathFloatValue(float value) {
        this.value = value;
    }
    
    public float getValue(){
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
        g2d.drawString(Float.toString(value), x, y + baseLine);
    }

    @Override
    public Dimension calculateSize(Graphics2D g2d) {
        FontMetrics fm = g2d.getFontMetrics();
        String text = Float.toString(value);
        LineMetrics lm = fm.getLineMetrics(text, g2d);
        size.width = fm.stringWidth(text);
        size.height = (int) lm.getHeight();
        baseLine = (int) lm.getAscent() + 1;
        return size;
    }
    
     @Override
    public String getId() {
        return "float";
    }

    @Override
    public String getType() {
        return "float";
    }
}
