/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dae.fxcreator.node;

/**
 * This interface makes it possible to listen to struct changes.
 * @author Koen
 */
public interface StructListener {
    /**
     * This method is called when the field of a struct was changed.
     * @param struct the struct that was changed.
     * @param field the field that was changed.
     */
    public void structFieldUpdated(ShaderStruct struct,ShaderField field, ShaderField oldValue);

    /**
     * This method is called when a new field is inserted in the struct.
     * @param field the field that was inserted.
     */
    public void structFieldInserted(ShaderStruct struct,ShaderField field);
    /**
     * This method is called when a field was remove from the struct.
     * @param field the field that was removed.
     */
    public void structFieldRemoved(ShaderStruct struct, ShaderField field);
    /**
     * This method is called when the id of the struct was changed.
     * @param struct the struct whose id was changed.
     */
    public void structIdChanged(ShaderStruct struct);
}
