package dae.fxcreator.node;

import java.awt.Point;

/**
 * This is a node that is a reference to another node.
 * @author Koen
 */
public class ReferenceNode {
    private IONode reference;
    private Point position;


    public ReferenceNode(IONode reference, Point position){
        this.reference = reference;
        this.position = position;
    }

    /**
     * Returns the referenced node.
     * @return the referenced node.
     */
    public IONode getReferencedNode(){
        return reference;
    }

    /**
     * Sets the position for this node.
     * @param x the x coordinate for the positon.
     * @param y the y coordinate for the position.
     */
    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }

    /**
     * Returns the position for this node.
     * @return the position for this node.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Return the x position for the node.
     * @return the x position.
     */
    public int getX(){
        return position.x;
    }

    /**
     * Returns the y position for the node.
     * @return the y position.
     */
    public int getY(){
        return position.y;
    }
}
