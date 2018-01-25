/*
 * Digital Arts and Entertainment 2017
 */
package dae.fxcreator.node.event;

import dae.fxcreator.node.IONode;

/**
 * Event object for use within the different user interface panels.
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class NodeEvent {
    public enum Type{SELECTED,REMOVED,ADDED};
    
    private final Type eventType;
    private final IONode node;
    
    public NodeEvent(IONode node, Type type){
        this.node = node;
        this.eventType = type;
    }
    
    public IONode getNode(){
        return node;
    }
    
    public Type getType(){
        return eventType;
    }
}
