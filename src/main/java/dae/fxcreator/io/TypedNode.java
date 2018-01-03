package dae.fxcreator.io;

/**
 * Interface to adopt by every object that plays a role in outputting a nodestructure
 * to file.
 * @author Koen
 */
public interface TypedNode {
    /**
     * Get the id of the node.
     * @return the id of the node.
     */
    public String getId();
    /**
     * Returns the type of the node, can be null.
     * @return the type of the node.
     */
    public String getType();

}
