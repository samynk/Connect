package dae.fxcreator.node;

import dae.fxcreator.node.project.ShaderStructCollection;
import dae.fxcreator.io.type.IOTypeLibrary;
import java.util.ArrayList;

/**
 * This class allows for the definition of a shader structs. Shader structs can
 * be used for global parameters in a shader definition, but off course also to
 * define the input / output structures for shader stages.
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class IOStruct extends IOType implements Cloneable, TypedNode {

    private String id;
    private final ArrayList<StructField> fields = new ArrayList<>();

    private ShaderStructCollection parent;

    protected IOTypeLibrary typeLibrary;

    /**
     * A list of listeners.
     */
    private final ArrayList<StructListener> listeners = new ArrayList<>();

    /**
     * Creates a new empty ShaderStruct object.
     *
     * @param id the id for the ShaderStruct object, as used in code generation.
     * @param typeLibrary
     */
    public IOStruct(String id, IOTypeLibrary typeLibrary) {
        // no specified order for a ShaderStruct.
        super(id, -1);
        this.id = id;
        this.typeLibrary = typeLibrary;
    }

    /**
     * Returns the type library that is used by this struct.
     *
     * @return the type library.
     */
    public IOTypeLibrary getTypeLibrary() {
        return typeLibrary;
    }

    /**
     * Returns the id for the ShaderStruct object.
     *
     * @return the id for the shader struct object.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Sets the id for the ShaderStruct object.
     *
     * @param id the new id for the struct.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Adds a shader field to the list of fields.
     *
     * @param field the field to add.
     */
    public void addShaderField(StructField field) {
        fields.add(field);
        field.setParent(this);
        this.notifyFieldInserted(field);
    }

    /**
     * Removes a shader field from the list of fields.
     *
     * @param field the field to remove.
     */
    public void removeShaderField(StructField field) {
        fields.remove(field);
    }

    /**
     * Checks if a field with the given name is present in this ShaderStruct
     * object.
     *
     * @param name the name of the field.
     * @return true if the field is present, false otherwise.
     */
    public boolean containsField(String name) {
        for (StructField field : fields) {
            if (field.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the list of fields in this ShaderStruct definition.
     *
     * @return the list of fields.
     */
    public Iterable<StructField> getFields() {
        return fields;
    }

    /**
     * Returns the field at the specific index.
     *
     * @param index the index to use.
     * @return the ShaderField object.
     */
    public StructField getField(int index) {
        return fields.get(index);
    }

    /**
     * Removes a shader field from the list of fields.
     *
     * @param index the index of the shader field to remove.
     */
    public void removeShaderField(int index) {
        fields.remove(index);
    }

    /**
     * Returns the number of fields in this ShaderStruct object.
     *
     * @return the number of fields.
     */
    public int getNrOfFields() {
        return fields.size();
    }

    /**
     * Notifies the listeners that a field was changed.
     *
     * @param oldValue a ShaderField object that contains the old values of the
     * field.
     * @param newValue a ShaderField object that contains the new values of the
     * field.
     */
    public void notifyFieldChanged(StructField newValue, StructField oldValue) {
        for (StructListener l : listeners) {
            l.structFieldUpdated(this, newValue, oldValue);
        }
    }

    /**
     * Notifies the listeners that a field was removed.
     *
     * @param field the field that was removed.
     */
    public void notifyFieldRemoved(StructField field) {
        for (StructListener l : listeners) {
            l.structFieldRemoved(this, field);
        }
    }

    /**
     * Notifies the listeners that a field was inserted.
     *
     * @param field the field that was removed.
     */
    public void notifyFieldInserted(StructField field) {
        for (StructListener l : listeners) {
            l.structFieldInserted(this, field);
        }
    }

    /**
     * Notifies the listeners that the id of the struct was changed.
     */
    public void notifyStructIdChanged() {
        for (StructListener l : listeners) {
            l.structIdChanged(this);
        }
    }

    /**
     * Adds a struct listener to the list of listeners.
     *
     * @param listener the listener to add.
     */
    public void addStructListener(StructListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a struct listener from the list of listeners.
     *
     * @param listener the listener to remove.
     */
    public void removeStructListener(StructListener listener) {
        listeners.remove(listener);
    }

    /**
     * Returns the type of object
     *
     * @return always "struct".
     */
    @Override
    public String getType() {
        return "struct";
    }
    
        /**
     * Returns a String representation of this ShaderStruct.
     *
     * @return the string representation of the ShaderStruct object.
     */
    @Override
    public String toString() {
        return id;
    }
}
