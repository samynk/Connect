package dae.fxcreator.node.graph.math;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * Renders a MathFormula. The render process is a two step process. 1) Determine
 * the size and baseline of each element separately. 2) Determine the total size
 * needed to render the formula. 3) Render the formula.
 *
 * @author samyn_000
 */
public class MathFormulaRenderer {

    private BasicStroke lineStroke = new BasicStroke(1.0f);

    public void render(MathFormula formula, Graphics2D g) {
        Stroke backup = g.getStroke();
        g.setStroke(lineStroke);
        // determine the size needed for the formula.
        Dimension size = formula.calculateSize(g);
        formula.render(g, 0, 0, size.width, size.height);
        g.setStroke(backup);
    }
}
