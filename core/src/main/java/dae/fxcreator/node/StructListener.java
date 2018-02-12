package dae.fxcreator.node;

/**
 * This interface makes it possible to listen to struct changes.
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public interface StructListener {

    /**
     * This method is called when the field of a struct was changed.
     *
     * @param struct the struct that was changed.
     * @param field the field that was changed.
     * @param oldValue the old value for the field.
     */
    public void structFieldUpdated(IOStruct struct, StructField field, StructField oldValue);

    /**
     * This method is called when a new field is inserted in the struct.
     *
     * @param struct the struct where a field was inserted.
     * @param field the field that was inserted.
     */
    public void structFieldInserted(IOStruct struct, StructField field);

    /**
     * This method is called when a field was remove from the struct.
     *
     * @param struct the struct where a field was removed.
     * @param field the field that was removed.
     */
    public void structFieldRemoved(IOStruct struct, StructField field);

    /**
     * This method is called when the id of the struct was changed.
     *
     * @param struct the struct whose id was changed.
     */
    public void structIdChanged(IOStruct struct);
}
