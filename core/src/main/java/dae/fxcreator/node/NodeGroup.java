package dae.fxcreator.node;

import java.util.List;

/**
 * Interface to describe objects that can contain shadernodes.
 * @author Koen Samnyn (samyn.koen@gmail.com)
 */
public interface NodeGroup extends TypedNode{

    /**
     * Connect the nodes in this nodegroup.
     */
    public void connectNodes();

    /**
     * Add a shader node to the collection.
     * @param node the node to add to the collection.
     */
    public void addNode(IONode node);

    /**
     * Remove a shader node from the collection.
     * @param node the node to remove from the collection.
     */
    public void removeNode(IONode node);

    /**
     * Find a shader node in the collection.
     * @param id the id of the shader node in the collection.
     */
    public IONode findNode(String id);

    /**
     * Returns the list of nodes that are present in this node group.
     * @return the list of shader nodes for this node group.
     */
    public Iterable<IONode> getNodes();

    /**
     * Returns the list of ShaderOutput objects for this group.
     * @return the list of ShaderOutput objects.
     */
    public List<NodeOutput> getOutputs();

    /**
     * Returns the list of ShaderInput objects for this group.
     * @return the list of ShaderOutput objects.
     */
    public List<NodeInput> getInputs();

     /**
     * Returns the input node associated with the ShaderStage.
     * @return the node that defines the inputs for the stage.
     */
    public IONode getInputNode();

    /**
     * Returns the output node associated with the ShaderStage.
     * @return the node that defines the outputs for the stage.
     */
    public IONode getOutputNode();
    /**
     * Returns a list of nodes that are sorted by dependency. In other
     * words if the outputs of a node are used by another node, the output node
     * will be first in the list, the node that consumes the outputs will be placed
     * after this first node.
     * @return a sorted list of IONode objects.
     */
    public Iterable<IONode> getSortedNodes();
    /**
     * Returns the type of node container.
     * @return the type of the container.
     */
    public String getContainerType();
     /**
     * Gets the subtype of this node container. The subtype is used to get the correct
     * method definition file.
     * @return the subtype.
     */
    public String getSubType();

    /**
     * Sets the subtype for this node container.
     * @param subType the new subtype.
     */
    public void setSubType(String subType);
    /**
     * Get a unique id with the prefix.
     * @param prefix the prefix for the new id.
     * @return the new id.
     */
    public String getUniqueId(String prefix);
    /**
     * Add a node that is a reference to another node
     * @param rn the reference node to add.
     */
    public void addReferenceNode(ReferenceNode rn);
    /**
     * Removes the reference node from the list of reference nodes.
     * @param rn the reference node to remove.
     */
    public void removeReferenceNode(ReferenceNode rn);
    /**
     * Returns the reference nodes.
     * @return an iterable with the reference nodes.
     */
    public Iterable<ReferenceNode> getReferenceNodes();
}
