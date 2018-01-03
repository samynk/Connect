/*
 * TopLeftControl.java
 *
 * Created on 18 december 2005, 15:14
 */

package dae.fxcreator.node.graph;

import dae.fxcreator.node.gui.TransferPosition;


/**
 *
 * @author samynk
 */
public class TopLeftControl implements TransferPosition{
    private GraphNode toControl;
    /** Creates a new instance of TopLeftControl */
    public TopLeftControl(GraphNode toControl) {
        this.toControl = toControl;
    }

    public void transferDX(float dx) {
        if ( toControl == null )
            return;
        toControl.setX(toControl.getX()+ Math.round(dx));
    }

    public void transferDY(float dy) {
        if ( toControl == null )
            return;
        toControl.setY(toControl.getY()+ Math.round(dy));
    }

    public void transferDP(float dx, float dy) {
        int idx = Math.round(dx);
        int idy = Math.round(dy);
        toControl.setPosition(toControl.getX()+idx,toControl.getY()+idy);
    }


}