package dae.fxcreator.node.graph;

import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderOutput;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;

/**
 * This class contains the connector information and has
 * the following purposes :
 * 1) Allow the user to select a link
 * 2) Allow the user to delete a link
 * 3) Allow the user to add an annotation to the link.
 * @author Koen
 */
public class Connector {

    private Point start;
    private Point end;
    private boolean selected;
    /**
     * For selection purposes.
     */
    private GeneralPath path = new GeneralPath();
    /**
     * The stroke for the connector.
     */
    private final BasicStroke stroke = new BasicStroke(1.0f);
    private final BasicStroke strokea = new BasicStroke(3.0f);
    private Color w = Color.white;
    private Color dw = Color.white.darker();
    private ShaderInput shaderInput;
    private ShaderOutput output;

    public Connector(ShaderInput input, Point start, Point end, GeneralPath path) {
        this.start = start;
        this.end = end;
        path = new GeneralPath();
    }

    /**
     * Creates an empty connector object.
     */
    public Connector(ShaderInput input) {
        shaderInput = input;
        path = new GeneralPath();
    }

    /**
     * Update the path with the new positions.
     * @param start the start of the path.
     * @param end the end of the path.
     */
    public void updatePath(Point start, Point end) {
        this.start = start;
        this.end = end;
        path.reset();

        path.moveTo(start.x, start.y);
        path.curveTo(start.x + (end.x - start.x) / 2, start.y, start.x + (end.x - start.x) / 2, end.y, end.x, end.y);
    }

    /**
     * Paint the path
     * @param g2d the graphics2d object to use for painting.
     */
    public void paintPath(Graphics2D g2d) {
        if (!selected) {
            g2d.setStroke(strokea);
            g2d.setColor(dw);
            g2d.draw(path);

            g2d.setStroke(stroke);
            g2d.setColor(w);
            g2d.draw(path);
        }else{
            g2d.setStroke(strokea);
            g2d.setColor(Color.yellow);
            g2d.draw(path);

            g2d.setStroke(stroke);
            g2d.setColor(Color.gray);
            g2d.draw(path);
        }
    }

    /**
     * Checks if a point is on the path object.
     * @param p the location to check.
     * @return true if the point is on the path, false otherwise.
     */
    public boolean isOnConnector(Point p) {
        return path.intersects(p.x-3,p.y-3,6,6);
    }

    /**
     * @return the start
     */
    public Point getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(Point start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public Point getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(Point end) {
        this.end = end;
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setConnectedOutput(ShaderOutput output){
        this.output=output;
    }

    public ShaderOutput getConnectedOutput(){
        return output;
    }

    /**
     * Removes the link between the shaderinput and output object.
     */
    void disconnect() {
        this.shaderInput.disconnectInput();
    }
}
