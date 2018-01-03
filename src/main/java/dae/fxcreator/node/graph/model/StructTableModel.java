/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.node.graph.model;

import dae.fxcreator.node.Semantic;
import dae.fxcreator.node.ShaderField;
import dae.fxcreator.node.ShaderStruct;
import dae.fxcreator.node.ShaderType;
import dae.fxcreator.node.graph.JGraphNode;
import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Koen
 */
public class StructTableModel implements TableModel, IOTableModel {

    private ShaderStruct struct;
    private JGraphNode node;
    private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();

    /**
     * Creates a new StructTable model.
     * @param struct the struct to edit.
     */
    public StructTableModel(ShaderStruct struct) {
        this.struct = struct;
    }

    /**
     * Creates a new StructTable model.
     * @param struct the struct to edit.
     * @param node the node the struct is set into.
     */
    public StructTableModel(JGraphNode node,ShaderStruct struct) {
        this.struct = struct;
        this.node = node;
    }

    /**
     * Returns the number of fields that are defined in the struct.
     * @return the number of fields.
     */
    public int getRowCount() {
        if (struct == null) {
            return 0;

        }
        return struct.getNrOfFields();
    }

    /**
     * Returns the column count of 3 ( name, semantic and type of the field.
     * @return the number of columns, always 3.
     */
    public int getColumnCount() {
        return 3;
    }

    /**
     * Returns the column name of the column ( "name", "semantic", "type").
     * @param columnIndex the index of the column.
     * @return the header for the column.
     */
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Type";
            case 1:
                return "Name";
            case 2:
                return "Semantic";
        }
        return "Error";
    }

    /**
     * Returns the column class for the column.
     * @param columnIndex the index of the column.
     * @return the class which allows to set custom cell editors.
     */
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return ShaderType.class;
            case 1:
                return String.class;
            case 2:
                return Semantic.class;
        }
        return String.class;
    }

    /**
     * Checks if the cell is editable or not.
     * @param rowIndex the index of the row.
     * @param columnIndex the index of the column.
     * @return always true.
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /**
     * Returns the value of the specific index.
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        ShaderField field = struct.getField(rowIndex);

        switch (columnIndex) {
            case 0:
                return field.getType();
            case 1:
                return field.getName();
            case 2:
                return field.getSemantic();
        }
        return null;
    }

    /**
     * Change a value in the field.
     * @param aValue the new value for the field.
     * @param rowIndex the index of the field in the ShaderStruct object.
     * @param columnIndex the column index that indicates what element to change of
     * the field object.
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ShaderField field = struct.getField(rowIndex);
        switch (columnIndex) {
            case 0:
                ShaderType st = (ShaderType) aValue;
                field.setType(st);
                break;
            case 1:
                field.setName(aValue.toString());
                break;
            case 2:
                field.setSemantic(aValue.toString());
                break;
        }

        TableModelEvent tme = new TableModelEvent(this, rowIndex, rowIndex, columnIndex, TableModelEvent.UPDATE);
        notifyListeners(tme);
    }

    /**
     * Delete an element of the ShaderStruct object.
     * @param index the index to remove.
     */
    public void delete(int index) {
        struct.removeShaderField(index);

        TableModelEvent tme = new TableModelEvent(this, index, index, -1, TableModelEvent.DELETE);
        notifyListeners(tme);
    }

    /**
     * Adds an element to the ShaderStruct object.
     */
    public void add() {
        int index = -1;
        // TODO : change the default type to a configurable default per type of fx project.
        ShaderField field = new ShaderField("newfield","",struct.getTypeLibrary().getType("FLOAT3"));
        struct.addShaderField(field);
        TableModelEvent tme = new TableModelEvent(this, index, index, -1, TableModelEvent.INSERT);
        notifyListeners(tme);
    }

    /**
     * Notifies the listeners of this model that the ShaderStruct was changed.
     * @param e the event object to send to the listeners.
     */
    private void notifyListeners(TableModelEvent e) {
        for (TableModelListener l : listeners) {
            l.tableChanged(e);
        }
    }

    /**
     * Add a TableModelListener to this object.
     * @param l the listener to add.
     */
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    /**
     * Removes a TableModelListener from this object.
     * @param l the listener to remove.
     */
    public void removeTableModelListener(TableModelListener l) {
        listeners.add(l);
    }
}
