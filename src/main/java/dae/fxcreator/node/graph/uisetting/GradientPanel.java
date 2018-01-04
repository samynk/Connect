package dae.fxcreator.node.graph.uisetting;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JPanel;

public class GradientPanel extends JPanel {

    private Color color1 = Color.BLUE;
    private Color color2 = Color.CYAN;
    private GradientPaint gp;
    private int gpWidth;

    public GradientPanel() {
    }

    /**
     * Paints the gradient.
     * @param g the graphics object to use.
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (gp == null || gpWidth != getWidth()) {
            gp = new GradientPaint(0, 0, color1, getWidth(), 0, color2);
            gpWidth = getWidth();
        }
        g2d.setPaint(gp);
        Rectangle r = g.getClipBounds();
        r.x +=2;
        r.y +=2;
        r.width -=4;
        r.height -=4;
        g2d.fill(r);

    }

    public void setColor1(Color color1) {
        this.color1 = color1;
        gp = new GradientPaint(0, 0, color1, getWidth(), 0, color2);
    }

    public void setColor2(Color color2) {
        this.color2 = color2;
        gp = new GradientPaint(0, 0, color1, getWidth(), 0, color2);
    }
}

