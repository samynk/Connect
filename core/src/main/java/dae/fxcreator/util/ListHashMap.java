package dae.fxcreator.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 * A combination of a hashmap and a list.
 *
 * @param <T> The type of object which will be stored in this collection, must
 * implement the Key interface.
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ListHashMap<T extends Key> {

    private final ArrayList<T> items = new ArrayList<>();
    private final HashMap<String, T> itemMap = new HashMap<>();

    /**
     * Creates a new empty list hash map.
     */
    public ListHashMap() {

    }
    
    /**
     * Returns the size of the items.
     * @return the size of the items.
     */
    public int size(){
        return items.size();
    }

    /**
     * Adds an item to this collection.
     *
     * @param item the item to add.
     */
    public void addItem(T item) {
        items.add(item);
        itemMap.put(item.getKey(), item);
    }

    /**
     * Remove the item from the collection.
     *
     * @param item the item to remove.
     * @return true if the item was removed, false otherwise.
     */
    public boolean removeItem(T item) {
        boolean removed = items.remove(item);
        if (removed) {
            itemMap.remove(item.getKey());
        }
        return removed;
    }

    /**
     * Add all the items in the list to this collection.
     *
     * @param items a collection of T objects.
     */
    public void addAll(List<T> items) {
        items.forEach((item) -> {
            addItem(item);
        });
    }

    /**
     * Find the object with the given key.
     *
     * @param key the key of the object.
     * @return the object with the given key.
     */
    public T find(String key) {
        return itemMap.get(key);
    }

    /**
     * Return the first item.
     * @return the first item.
     */
    public T first() {
        return items.get(0);
    }
    
    /**
     * Return the item with the given index.
     * @param index the index into the list.
     * @return the object of type T
     */
    public T getItem(int index){
        return items.get(index);
    }

    /**
     * Check if the key is present in the hash map.
     * @param name the name of the key.
     * @return true if the key is in the hash map, false otherwise.
     */
    public boolean containsKey(String name) {
        return itemMap.containsKey(name);
    }

    /**
     * Returns an iterable to iterate over the items.
     * @return the iterable.
     */
    public Iterable<T> items() {
        return items;
    }

    /**
     * Returns the index of the item.
     * @param item the item to get the index of.
     * @return the index of the item, or -1 if no item was found.
     */
    public int indexOf(T item) {
        return items.indexOf(item);
    }

    /**
     * Returns this collection as an enumeration.
     * @return the list as an Enumeration object.
     */
    public Enumeration enumeration() {
        return Collections.enumeration(items);
    }
}
