package dae.fxcreator.node;

import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This interface defines the common property of the ShaderInput and
 * ShaderOutput class.
 *
 * @author Koen
 */
public abstract class ShaderIO {
    private IONode parent;
    private String name;
    private Semantic semantic = new Semantic();
    private ShaderType type;
    private IOAnchor anchor = IOAnchor.UNDEFINED;

    /**
     * The list of change listeners
     */
    private final ArrayList<ChangeListener> listeners = new ArrayList<>();

    public ShaderIO(IONode parent, String name, String semantic, ShaderType type) {
        this.parent = parent;
        this.name = name;
        this.semantic.setValue(semantic);
        this.type = type;
    }
    
    /**
     * Returns the parent IONode object.
     * @return the parent IONode object.
     */
    public IONode getParent(){
        return parent;
    }
    
    /**
     * Sets the parent IONode object of this ShaderIO object.
     * @param parent the new parent of this object.
     */
    public void setParent(IONode parent){
        this.parent = parent;
    }

    /**
     * Returns the name for the ShaderInput object.
     *
     * @return the name of the shader input object.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for the shader io object.
     *
     * @param name the name for the shader input object.
     */
    public void setName(String name) {
        
        if (!this.name.equals(name)) {
            String oldName = this.name;
           this.name = name;
            if (this.parent != null) {
                if ( isInput() ){
                    parent.remapInputName(oldName, (ShaderInput)this);
                }else{
                    parent.remapOutputName(oldName, (ShaderOutput)this);
                }
            }
        }
    }

    /**
     * Returns the semantic for the input or output object.
     *
     * @return the semantic.
     */
    public Semantic getSemantic() {
        return semantic;
    }
    
        /**
     * Sets the semantic of the ShaderInput object.
     *
     * @param semantic the new sementic for this input object.
     */
    public void setSemantic(String semantic) {
        if (!semantic.equals(this.semantic.getValue())) {
            String oldValue = this.semantic.getValue();
            this.semantic.setValue(semantic);
            if ( isInput() ){
                parent.remapInputSemantic(oldValue, (ShaderInput)this);
            }else{
                parent.remapOutputSemantic(oldValue,(ShaderOutput)this);
            }
        }
    }

    /**
     * Returns the shader type for the ShaderIO object.
     *
     * @return the shadertype.
     */
    public ShaderType getType() {
        return type;
    }

    /**
     * Sets the shader type for this ShaderIO object.
     *
     * @param type the new shader type for this ShaderIO object.
     */
    public void setType(ShaderType type) {
        this.type = type;
    }

    /**
     * Checks if the type of this input is an actual value.
     *
     * @return true if this input connects to a value type, false otherwise.
     */
    public boolean getValueType() {
        return type != null && type.isValueType();
    }

    /**
     * Returns a label for this ShaderInput object.
     *
     * @return the label.
     */
    public String getLabel() {
        if (semantic.isValid()) {
            return this.name + " : " + semantic;
        } else {
            return this.name;
        }
    }

    /**
     * Gets the variable name for this ShaderIO object.
     *
     * @return the variable name for this ShaderIO object.
     */
    public abstract String getRef();

    /**
     * Returns the variable declaration name for the ShaderIO object.
     *
     * @return the variable declaration name.
     */
    public abstract String getVar();

    /**
     * Checks if this ShaderIO object is connected.
     *
     * @return true if the ShaderIO object is connected, false otherwise.
     */
    public abstract boolean getConnected();

    /**
     * Checks if this is an input node.
     *
     * @return true if this is an input node, false otherwise.
     */
    public abstract boolean isInput();

    /**
     * Checks if this is an output node.
     *
     * @return true if this is an output node, false otherwise.
     */
    public abstract boolean isOutput();

    /**
     * Checks if this input or output is compatible with the provided shader io
     * object.
     *
     * @param io the input or output to check for acceptance.
     * @return true if this object is compatible (can connect) with the provided
     * object.
     */
    public abstract boolean accepts(ShaderIO io);

    /**
     * Adds a change listener to this io object.
     *
     * @param cl the change listener to add to the list of listeners.
     */
    public void addChangeListener(ChangeListener cl) {
        listeners.add(cl);
    }

    /**
     * Removes the change listener from this io objecT.
     *
     * @param cl the change listener to remove from the list of listeners.
     */
    public void removeChangeListener(ChangeListener cl) {
        listeners.remove(cl);
    }
    
    /**
     * Sets the default anchor for outputs.
     *
     * @param a the anchor for inputs in the form of (N|S) (W|E)
     */
    public void setAnchor(String a) {
        switch (a.toUpperCase()) {
            case "NE":
                anchor = IOAnchor.NORTHEAST;
                break;
            case "NW":
                anchor = IOAnchor.NORTHWEST;
                break;
            case "SE":
                anchor = IOAnchor.SOUTHEAST;
                break;
            case "SW":
                anchor = IOAnchor.SOUTHWEST;
                break;
            default:
                anchor = IOAnchor.UNDEFINED;
        }
    }
    
    /**
     * Return the anchor for this shader io object.
     * @return the NodeAnchor enum value.
     */
    public IOAnchor getAnchor(){
        return anchor;
    }
    
    /**
     * Sets the anchor to a new value..
     * @param anchor the new value for the anchor.
     */
    public void setAnchor(IOAnchor anchor){
        this.anchor = anchor;
    }
    
    /**
     * checks if this input or output has a defined anchor.
     * @return true if this object has an anchor, false otherwise.
     */
    public boolean hasAnchor() {
        return anchor != IOAnchor.UNDEFINED;
    }

    /**
     * Notifies the listeners that this object has changed.
     */
    protected void notifyListeners() {
        ChangeEvent ce = new ChangeEvent(this);
        for (ChangeListener cl : listeners) {
            cl.stateChanged(ce);
        }
    }
}
