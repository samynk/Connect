package dae.fxcreator.node.graph;

import java.util.ArrayList;



/**
 * This class contains a grouping of graph nodes and is itself a GroupNode.
 * GroupNodes can be used to represent :
 * 1) shader stages.
 * 2) shader funtions.
 * @author Koen
 */
public class GraphGroupNode extends GraphNode{
    
    public enum PaintState{COLLAPSED,EXPANDED};
    
    // default state is collapsed
    private PaintState state = PaintState.COLLAPSED;

    private ArrayList<GraphNode> graphNodes = new ArrayList<GraphNode>();

    /**
     * Creates a new GraphGroupNode with the provide name.
     * @param name the name for the GraphGroupNode.
     */
    public GraphGroupNode(String name){
        super(name);
    }

    /**
     * Returns the current paint state.
     * @return the current paint state.
     */
    public PaintState getState() {
        return state;
    }

    /**
     * Sets the current paint state
     * @param state the current state.
     */
    public void setState(PaintState state) {
        this.state = state;
    }

    /**
     * Adds a GraphNode object to the list of graph nodes.
     * @param node the node to add.
     */
    public void addGraphNode(GraphNode node){
        graphNodes.add(node);
    }

    /**
     * Removes a GraphNode object to the list of graph nodeS.
     * @param node the node to remove.
     */
    public void removeGraphNode(GraphNode node){
        graphNodes.remove(node);
    }

    /**
     * Returns the list of GraphNodes from this GraphGroupNode.
     * @return an arraylist with the GraphNodes.
     */
    public ArrayList<GraphNode> getGraphNodes(){
        return graphNodes;
    }
}