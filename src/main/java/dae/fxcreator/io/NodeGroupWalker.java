package dae.fxcreator.io;

import dae.fxcreator.node.IONode;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class collects the nodes in a node group from output to input.
 * @author Koen
 */
public class NodeGroupWalker {
    private ArrayList<IONode> nodes = new ArrayList<IONode>();

    private NodeGroup nodeGroup;

    /**
     * Creates an empty node group walker.
     */
    public NodeGroupWalker(NodeGroup group){
        nodeGroup = group;
    }

    /**
     * Collects the nodes starting from the output node, and working back
     * to the input node of the node group by using the inputs of each node.
     * Each node will be written to the list of nodes only once.
     */
    public void collect(){
        IONode node = nodeGroup.getOutputNode();
        nodes.add(node);
        node.collectInputs(this);
        Collections.reverse(nodes);
    }

    /**
     * Returns the list of nodes.
     * @return the list of nodes.
     */
    public Iterable<IONode> getNodes(){
        return nodes;
    }

    /**
     * Checks if this NodeGroup walker allready has a node stored.
     * @param node the node to check.
     * @return true if the walker object contains the node, false otherwise.
     */
    public boolean containsNode(IONode node){
        return nodes.contains(node);
    }

    /**
     * Adds a node the list of nodes.
     * @param node the node to add.
     */
    public void addNode(IONode node){
        if ( containsNode(node )){
            // if the node is allready added delete it and add it to the end
            // of the list
            nodes.remove(node);
        }
        nodes.add(node);
    }
}
