package dae.fxcreator.node.graph.math;

import dae.fxcreator.node.graphmath.MathFormula;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * Renders a MathFormula. The render process is a two step process. 1) Determine
 * the size and baseline of each element separately. 2) Determine the total size
 * needed to render the formula. 3) Render the formula.
 *
 * @author Koen Samyn
 */
public class MathFormulaRenderer {

    private final BasicStroke lineStroke = new BasicStroke(1.0f);

    public Dimension calculateSize(MathFormula formula, Graphics2D g )
    {
        Dimension size = formula.calculateSize(g);
        return size;
    }
    
    public void render(MathFormula formula, Graphics2D g) {
        Stroke backup = g.getStroke();
        g.setStroke(lineStroke);
        // determine the size needed for the formula.
        Dimension size = formula.calculateSize(g);
        formula.render(g, 0, 0, size.width, size.height);
        g.setStroke(backup);
    }
}
