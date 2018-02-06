package dae.fxcreator.node.project;

import dae.fxcreator.node.IONode;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This object contains the collection of states in a project.
 * @author Koen
 */
public class StatesCollection{

    private final HashMap<String, IONode> stateMap = new HashMap<>();
    private final ArrayList<IONode> states = new ArrayList<>();
    private FXProject parent;

    /**
     * Creates a new StatesCollection object.
     * @param parent the parent FXProject object.
     */
    public StatesCollection(FXProject parent) {
        this.parent = parent;
    }
    
    /**
     * Creates a unique shader stage name.
     * @param prefix the prefix for the shader stage name.
     */
    public String createUniqueStateName(String prefix){
        String name = prefix;
        int i = 0;
        do{
            name = prefix + i;
            ++i;
        }while (stateMap.containsKey(name));
        return name;
    }

    /**
     * Adds a state node to the list of nodes.
     * @param node the node to add to the list of states.
     */
    public void addState(IONode node){
        states.add(node);
        stateMap.put(node.getId(),node);
    }

    /**
     * Removes a state node from the list of nodes.
     * @param node the node to remove.
     */
    public void removeState(IONode node){
        states.remove(node);
        stateMap.remove(node.getId());
    }

    /**
     * Returns the list of state objects.
     * @param return the list of state objects.
     */
    public Iterable<IONode> getStates(){
        return states;
    }

    /**
     * Finds a state with the specified id.
     * @param id the id to find a node for.
     * @return the IONode with the specified id.
     */
    public IONode findState(String id) {
        return stateMap.get(id);
    }

    /**
     * Returns a string representation for this collection.
     * @return the states
     */
    public String toString(){
        return "states";
    }
}
