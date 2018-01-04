package dae.fxcreator.node.gui;
import java.awt.*;

/**
 * This class describes a control point on a gui. The control point can be
 * identified by a label so that changes made by the location of the control
 * point by the gui can be transformed and put back into the original datamodel.
 * @author samynk
 */
public class ControlPoint {
    private String label;
    private Point location;
    // The transform that was used to transform the original location of the point
    private PointTransform currentTransform;
    private boolean visible=true;
    private int cursor=Cursor.DEFAULT_CURSOR;
    /**
     * A TransferPosition command object that will transfer the update
     * to the position to the set.
     */
    private TransferPosition tp;
    private Color dragColor;
    
    /** Creates a new instance of ControlPoint */
    public ControlPoint(String label,TransferPosition tp) {
        this(label,tp,true,Cursor.HAND_CURSOR);
    }
    
    public ControlPoint(String label, TransferPosition tp, boolean visible, int cursor){
        this.label = label;
        this.location = new Point();
        this.tp = tp;
        dragColor = new Color(0,0,255,255);
        currentTransform = new PointTransform();
        currentTransform.setTransform(1f,1f,0f,0f,0f,0f);
        this.cursor=cursor;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public Point getLocation() {
        return location;
    }
    
    public void setLocation(int x, int y){
        location.x = x;
        location.y = y;
    }
    
    public boolean isOver(Point p ){
        int dx = Math.abs(p.x-location.x);
        int dy = Math.abs(p.y-location.y);
        return dx < 4 && dy <4;
    }
    
    public void draw(Graphics g){
        g.setColor(dragColor);
        g.fillRect(location.x-3,location.y-3, 7,7);
    }    

    @Override
    public String toString(){
        return getLabel() + ": " + location;
    }


    public PointTransform getCurrentTransform() {
        return currentTransform;
    }

    public void setCurrentTransform(PointTransform currentTransform) {
        this.currentTransform = currentTransform;
    }
    
    public void transferDX(int dx){
        float fdx = currentTransform.xPixelDistToRealDist(dx);
        if (tp !=null)
            tp.transferDX(fdx);
    }
    
    public void transferDY(int dy){
        float fdy = currentTransform.xPixelDistToRealDist(dy);
        if (tp !=null)
            tp.transferDY(fdy);
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public void transferDP(int dx, int dy) {
        float fdx = currentTransform.xPixelDistToRealDist(dx);
        float fdy = currentTransform.xPixelDistToRealDist(dy);
        if ( tp != null){
            tp.transferDP(fdx, fdy);
        }
    }
}