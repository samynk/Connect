package dae.fxcreator.node.events;

import java.util.Iterator;

/**
 * This interfaces allows observers to be notified when a shadertype has
 * been created or destroyed.
 * @author Koen
 */
public interface SymbolListener {
    /**
     * Returns the symbols this listener wants to to listen for.
     * @return the keys this object wants to listen for.
     */
    public Iterator<String> getListenerSymbols();
    /**
     * Notifies that a symbol was added to the project. A symbol can be a
     * ShaderStage, Struct, Technique or Pass.
     * @param type the type of symbol that was added.
     * @param symbol the symbol that was added.
     */
    public void symbolAdded(String type, Object symbol);
    /**
     * Notifies that a symbol was removed from the project. A symbol can be
     * a ShaderStage, Struct, Techniqu or Pass.
     * @param type the type of symbol that was removed.
     * @param symbol the symbol that was removed.
     */
    public void symbolRemoved(String type, Object symbol);
}
