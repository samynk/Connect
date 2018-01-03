package dae.fxcreator.io.events;

/**
 * Interface to listen for IO events.
 * Possible events are :
 * 1) input renamed (name or semantic)
 * 2) output renamed (name or semantic)
 * 3) input added
 * 4) input removed
 * 5) output added
 * 6) output removed.
 * @author Koen
 */
public interface IOListener {
    /**
     * Called when the contents of an input or output has changed.
     * @param oldName the old name for the input or output.
     * @param newName the new name for the input or output.
     */
    public void ioChanged(String oldName, String newName);
    /**
     * Called when an input or output was removed.
     * @param name the name for the input or output.
     */
    public void ioRemoved(String name);
    /**
     * Called when an input or output was added.
     * @param name the name for the input or output.
     */
    public void ioAdded(String name);
}
