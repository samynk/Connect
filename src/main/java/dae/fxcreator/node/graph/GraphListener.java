/*
 * GraphListener.java
 *
 * Created on 12 januari 2006, 14:29
 */

package dae.fxcreator.node.graph;

/**
 * This interface makes it possible to listen to events in the GraphEditor
 * component.
 * @author samynk
 */
public interface GraphListener {
    /**
     * Called when a node is selected.
     * @param node that was selected.
     */
    public void nodeSelected(JGraphNode node);
    /**
     * Called when a new node is created.
     * @param node the node that was added.
     */
    public void nodeAdded(JGraphNode node);
    /**
     * Called when the node was removed.
     * @param node the node that was removed.
     */
    public void nodeRemoved(JGraphNode node);
    /**
     * Called when the node was entered.
     * @param node the node that was entered.
     */
    public void nodeEntered(JGraphNode node);
    /**
     * Called when a node was moved.
     * @param node the node that was moved.
     */
    public void nodeMoved(JGraphNode node);
    /**
     * Called when a link is selected.
     * @param link the link that was selected.
     */
    public void linkSelected(GraphNodeLink link);
    /**
     * Called when a link was added.
     * @param link the link that was added.
     */
    public void linkAdded(Connector connector);
    /**
     * Called when a link was removed.
     * @param link the link that was removed.
     */
    public void linkRemoved(Connector connector);
}
