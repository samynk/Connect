/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.io.templates;

import dae.fxcreator.node.NodeContainer;

/**
 * This objects stores the information about a node group (such as the
 * file location.
 * @author Koen
 */
public class NodeContainerProxy {
    /**
     * The prefix for nodes of this type.
     */
    public String prefix;
    /**
     * The icon for the node container
     */
    private String icon;
    /**
     * The label for the node group definition.
     */
    private String label;
    /**
     * The type name for the node container.
     */
    private String type;
    /**
     * The description for the node container.
     */
    private String description;
    /**
     * Set to true if the NodeContainer object is loaded.
     */
    private boolean loaded = false;
    /**
     * The real NodeContainer object.
     */
    private NodeContainer container;

    /**
     * Creates a new NodeContainerProxy object
     * @param fileLocation
     * @param icon
     * @param label
     */
    public NodeContainerProxy(String prefix, String type, String icon, String label,String description) {
        this.prefix = prefix;
        this.type = type;
        this.icon = icon;
        this.label = label;
        this.description = description;
    }

    /**
     * Returns the prefix for new nodes of this type.
     * @return the prefix for the node.
     */
    public String getPrefix(){
        return prefix;
    }

    /**
     * Return the type for the node container.
     * @return the type.
     */
    public String getType() {
        return type;
    }

    /**
     * Return the icon for the node group.
     * @return the location for the icon.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Return the label for the node container.
     * @return the label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the description for the node container.
     * @return the description
     */
    public String getDescription(){
        return description;
    }

    /**
     * Checks if the NodeContainer object was loaded from file.
     * @return true if the NodeContainer object was loaded, false otherwise.
     */
    boolean isLoaded() {
        return loaded;
    }

    /**
     * Sets the NodeContainer object for this proxy. If the container is not null,
     * loaded will be set to true.
     * @param container the container to set.
     */
    public void setNodeContainer(NodeContainer container) {
        this.container = container;
        loaded = (container != null);
        container.setSubType(type);
    }

    /**
     * Creates a new NodeContainer for use in a project.
     * @return the new NodeContainer object.
     */
    public NodeContainer createNodeContainer() {
        try {
            NodeContainer nc = (NodeContainer) container.clone();
            nc.setSubType(this.type);
            nc.setInputOutputEditable(true);
            return nc;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}
