package dae.fxcreator.util;

/**
 * The ListHashMap collection only supports object that implement the Key
 * interface. The key must be a string, which is the most common case.
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public interface Key {
    /**
     * Returns the key to use for this object.
     * @return the key.
     */
    public String getKey();
}
