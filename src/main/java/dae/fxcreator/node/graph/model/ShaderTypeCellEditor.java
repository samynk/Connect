package dae.fxcreator.node.graph.model;

import dae.fxcreator.node.templates.NodeTemplateLibrary;
import dae.fxcreator.io.type.IOTypeLibrary;
import dae.fxcreator.node.IOType;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.AbstractCellEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.ListDataListener;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Koen
 */
public class ShaderTypeCellEditor extends AbstractCellEditor implements TableCellEditor, ComboBoxModel, ActionListener {
    // This is the component that will handle the editing of the cell value

    private IOType selected;
    private ArrayList<ListDataListener> listeners = new ArrayList<ListDataListener>();

    private IOTypeLibrary library;

    public ShaderTypeCellEditor(NodeTemplateLibrary library) {
        this.library = library.getTypeLibrary();
        component.setModel(this);
        component.addActionListener(this);
    }
    JComboBox component = new JComboBox();
    // This method is called when a cell value is edited by the user.

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
        // 'value' is value contained in the cell located at (rowIndex, vColIndex)

        component.setSelectedItem(value);
        return component;

    }
    // This method is called when editing is completed.
    // It must return the new value to be stored in the cell.

    public Object getCellEditorValue() {
        return selected;
    }

    public void setSelectedItem(Object anItem) {
        this.selected = (IOType)anItem;
    }

    public Object getSelectedItem() {
        return selected;
    }

    public int getSize() {
        return library.getSize();
    }

    public Object getElementAt(int index) {
        return library.getType(index);
    }

    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    public void actionPerformed(ActionEvent e) {
        this.fireEditingStopped();
    }
}
