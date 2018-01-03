/*
 * PointTransform.java
 *
 * Created on 10 december 2005, 10:20
 */

package dae.fxcreator.node.gui;

import java.awt.geom.*;

/**
 *
 * @author samynk
 */
public class PointTransform {
    private float xScale;
    private float yScale;
    private float xTrans;
    private float yTrans;
    private float xPreTrans;
    private float yPreTrans;
    
    public PointTransform(){
    }
    
    public void setTransform(float xScale, float yScale, float xPreTrans, float yPreTrans,float xTrans, float yTrans){
        this.xScale = xScale;
        this.yScale = yScale;
        this.xTrans = xTrans;
        this.yTrans = yTrans;
        this.xPreTrans = xPreTrans;
        this.yPreTrans = yPreTrans;
    }
    
    public void transformPoint2D(Point2D.Float point){
        point.x = (point.x+xPreTrans) * xScale + xTrans;
        point.y = (point.y+yPreTrans) * yScale + yTrans;
    }
    
    public float xPixelDistToRealDist(int distance){
        return distance / xScale;
    }
}