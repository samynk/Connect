package dae.fxcreator.node;

import java.awt.Point;

/**
 * This is a node that is a reference to another node.
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ReferenceNode {

    private final IONode reference;
    private final Point position;

    /**
     * Creates a reference node that can be used to sanitize the
     * node graphical structre.
     * @param reference the node that is referenced.
     * @param position the position of the node.
     */
    public ReferenceNode(IONode reference, Point position) {
        this.reference = reference;
        this.position = position;
    }

    /**
     * Returns the referenced node.
     *
     * @return the referenced node.
     */
    public IONode getReferencedNode() {
        return reference;
    }

    /**
     * Sets the position for this node.
     *
     * @param x the x coordinate for the positon.
     * @param y the y coordinate for the position.
     */
    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }

    /**
     * Returns the position for this node.
     *
     * @return the position for this node.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Return the x position for the node.
     *
     * @return the x position.
     */
    public int getX() {
        return position.x;
    }

    /**
     * Returns the y position for the node.
     *
     * @return the y position.
     */
    public int getY() {
        return position.y;
    }
}
