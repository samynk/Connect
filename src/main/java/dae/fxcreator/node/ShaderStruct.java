/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dae.fxcreator.node;

import dae.fxcreator.io.ShaderStructCollection;
import dae.fxcreator.io.TypedNode;
import dae.fxcreator.io.type.ShaderTypeLibrary;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;

/**
 * This class allows for the definition of a shader structs. Shader structs can
 * be used for global parameters in a shader definition, but off course also
 * to define the input / output structures for shader stages.
 * @author Koen
 */
public class ShaderStruct extends ShaderType implements TreeNode,Cloneable,TypedNode{
    private String id;
    private ArrayList<ShaderField> fields = new ArrayList<ShaderField>();

    private ShaderStructCollection parent;

    protected ShaderTypeLibrary typeLibrary;

    /**
     * A list of listeners.
     */
    private ArrayList<StructListener> listeners = new ArrayList<StructListener>();

    /**
     * Creates a new empty ShaderStruct object.
     * @param id the id for the ShaderStruct object, as used in code generation.
     */
    public ShaderStruct(String id,ShaderTypeLibrary typeLibrary){
        // no specified order for a ShaderStruct.
        super( id, -1);
        this.id = id;
        this.typeLibrary = typeLibrary;
    }

    /**
     * Returns the type library that is used by this struct.
     * @return the type library.
     */
    public ShaderTypeLibrary getTypeLibrary(){
        return typeLibrary;
    }

    /**
     * Returns the id for the ShaderStruct object.
     * @return the id for the shader struct object.
     */
    public String getId(){
        return id;
    }

    /**
     * Sets the id for the ShaderStruct object.
     * @param id the new id for the struct.
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Adds a shader field to the list of fields.
     * @param field the field to add.
     */
    public void addShaderField(ShaderField field){
        fields.add(field);
        field.setParent(this);
        this.notifyFieldInserted(field);
    }

    /**
     * Removes a shader field from the list of fields.
     * @param field the field to remove.
     */
    public void removeShaderField(ShaderField field){
        fields.remove(field);
    }

    /**
     * Checks if a field with the given name is present in
     * this ShaderStruct object.
     * @param name the name of the field.
     * @return true if the field is present, false otherwise.
     */
    public boolean containsField(String name){
        for (ShaderField field : fields){
            if ( field.getName().equals(name))
                return true;
        }
        return false;
    }

    /**
     * Returns the list of fields in this ShaderStruct definition.
     * @return the list of fields.
     */
    public Iterable<ShaderField> getFields() {
        return fields;
    }

    /**
     * Returns a String representation of this ShaderStruct.
     * @return the string representation of the ShaderStruct object.
     */
    @Override
    public String toString(){
        return id;
    }

    /**
     * Returns the child at the specific index.
     * @param childIndex the index
     * @return always
     */
    public TreeNode getChildAt(int childIndex) {
        return this.fields.get(childIndex);
    }

    /**
     * Returns the number of children in this ShaderStruct.
     * @return the number of children.
     */
    public int getChildCount() {
       return fields.size();
    }

    /**
     * The parent of this ShaderStruct object.
     * @return the parent.
     */
    public TreeNode getParent() {
        return parent;
    }

    /**
     * Gets the index of a specific child object.
     * @param node the node to get the index of.
     * @return the index of the node object
     */
    public int getIndex(TreeNode node) {
        return fields.indexOf(node);
    }

    /**
     * Checks if this node allows children.
     * @return always true.
     */
    public boolean getAllowsChildren() {
        return true;
    }

    /**
     * Checks if this node is a leaf object.
     * @return always false.
     */
    public boolean isLeaf() {
        return false;
    }

    public Enumeration children() {
        return null;
    }
    /**
     * Returns the field at the specific index.
     * @param index the index to use.
     * @return the ShaderField object.
     */
    public ShaderField getField(int index) {
        return fields.get(index);
    }

    /**
     * Removes a shader field from the list of fields.
     * @param index the index of the shader field to remove.
     */
    public void removeShaderField(int index) {
        fields.remove(index);
    }

    /**
     * Returns the number of fields in this ShaderStruct object.
     * @return the number of fields.
     */
    public int getNrOfFields() {
        return fields.size();
    }

    /**
     * Notifies the listeners that a field was changed.
     * @param oldValue a ShaderField object that contains the old values of the field.
     * @param newValue a ShaderField object that contains the new values of the field.
     */
    public void notifyFieldChanged(ShaderField newValue, ShaderField oldValue){
        for ( StructListener l : listeners){
            l.structFieldUpdated(this, newValue, oldValue);
        }
    }

    /**
     * Notifies the listeners that a field was removed.
     * @param field the field that was removed.
     */
    public void notifyFieldRemoved(ShaderField field){
        for ( StructListener l : listeners) {
            l.structFieldRemoved(this, field);
        }
    }

     /**
     * Notifies the listeners that a field was inserted.
     * @param field the field that was removed.
     */
    public void notifyFieldInserted(ShaderField field){
        for ( StructListener l : listeners) {
            l.structFieldInserted(this, field);
        }
    }

    /**
     * Notifies the listeners that the id of the struct was changed.
     */
    public void notifyStructIdChanged(){
        for ( StructListener l : listeners) {
            l.structIdChanged(this);
        }
    }

    /**
     * Adds a struct listener to the list of listeners.
     * @param listener the listener to add.
     */
    public void addStructListener(StructListener listener) {
        System.out.println("Adding listener : " + listener);
        listeners.add(listener);
    }

    /**
     * Removes a struct listener from the list of listeners.
     * @param listener the listener to remove.
     */
    public void removeStructListener(StructListener listener){
        listeners.remove(listener);
    }

    /**
     * Returns the type of object
     * @return always "struct".
     */
    public String getType() {
        return "struct";
    }
}