package dae.fxcreator.util;

import java.util.ArrayList;
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
     * Adds an item to this collection.
     * @param item the item to add.
     */
    public void addItem(T item) {
        items.add(item);
        itemMap.put(item.getKey(), item);
    }
    
    /**
     * Remove the item from the collection.
     * @param item the item to remove.
     * @return true if the item was removed, false otherwise.
     */
    public boolean removeItem(T item){
        boolean removed = items.remove(item);
        if ( removed ){
            itemMap.remove(item.getKey());
        }
        return removed;
    }
    
    /**
     * Add all the items in the list to this collection.
     * @param items a collection of T objects.
     */
    public void addAll(List<T> items){
        items.forEach((item) -> {
            addItem(item);
        });
    }
    
    /**
     * Find the object with the given key.
     * @param key the key of the object.
     * @return the object with the given key.
     */
    public T find(String key){
        return itemMap.get(key);
    }
}
