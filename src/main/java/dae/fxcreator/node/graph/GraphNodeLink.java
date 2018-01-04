package dae.fxcreator.node.graph;

import java.awt.*;

/**
 *
 * @author samynk
 */
public class GraphNodeLink {
    private String label;
    private ConnectorPoint to;
    private ConnectorPoint from;

    private boolean selected;
    /** Creates a new instance of GraphNodeLink */
    public GraphNodeLink(ConnectorPoint from, ConnectorPoint to) {
        this.from = from;
        this.to = to;
        from.addLink(this);
        to.addLink(this);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ConnectorPoint getTo() {
        return to;
    }

    public void setTo(ConnectorPoint to) {
        this.to = to;
    }

    public ConnectorPoint getFrom() {
        return from;
    }

    public void setFrom(ConnectorPoint from) {
        this.from = from;
    }
    
    public Object getFromUserObject(){
        if ( from !=null)
            return from.getParent().getUserObject();
        return null;
    }
    
    public Object getToUserObject(){
        if ( to != null)
            return to.getParent().getUserObject();
        return null;
    }

    /**
     * Draw method takes place in the context of the from node
     * coordinate system.
     * @param g the graphics context to use.
     */
    public void draw(Graphics g){
        if ( selected) {
            g.setColor(Color.GREEN);
        }else{
            g.setColor(Color.blue);
        }
        Point pfrom = from.getLocation();
        Point start = from.getRealLocation();
        Point end = to.getRealLocation();
        g.drawLine(pfrom.x, pfrom.y, pfrom.x+(end.x-start.x),pfrom.y+(end.y-start.y));
    }

    public boolean pointIsOnLink(Point p) {
        Point pfrom = from.getRealLocation();
        Point pto = to.getRealLocation();

        if ( !valueInsideRange(p.x, pfrom.x, pto.x,5.0f)
                || !valueInsideRange(p.y,pfrom.y,pto.y,5.0f))
            return false;

        float dx = pto.x - pfrom.x;
        float dy = pto.y - pfrom.y;

        float diff= Float.MAX_VALUE;
        if ( Math.abs(dx) < 0.00001 ){
            diff = p.y - pfrom.x;

        }else{
            float rc = dy/dx;
            float y = (p.x - pfrom.x) * rc + pfrom.y;
            diff = p.y - y;
        }
        return diff < 5 && diff > -5;
    }

    private boolean valueInsideRange(float value, float range1,float range2,float tolerance){
        if ( range2 > range1 )
            return value > (range1-tolerance) && value < (range2+tolerance);
        else
            return value < (range1+tolerance) && value > (range2-tolerance);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected(){
        return selected;
    }
}