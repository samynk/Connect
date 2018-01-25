package dae.fxcreator.util;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public interface List extends Label{
    /**
     * Gets the object at the given index.
     * @param index the index of the child.
     * @return the object at the given index, or null if no such element is present.
     */
     public Object getChild(int index);
     /**
      * Returns the number of children in this list.
      * @return  the number of children in this list.
      */
     public int getChildCount();
     /**
      * Returns the index of the child. or -1 if the child is not present.
      * @param child the child to find.
      * @return the index of the child.
      */
     public int getIndexOfChild(Object child);
}
