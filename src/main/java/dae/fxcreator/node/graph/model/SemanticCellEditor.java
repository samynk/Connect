package dae.fxcreator.node.graph.model;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Koen
 */
public class SemanticCellEditor  extends AbstractCellEditor implements TableCellEditor,ActionListener{
    SuggestionTextField tf;

    public SemanticCellEditor(){
        
        tf = new SuggestionTextField(new SemanticDataModel());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
        // 'value' is value contained in the cell located at (rowIndex, vColIndex)
        tf.setSelectedItem(value.toString());
        tf.addActionListener(this);
        return tf;
    }
    // This method is called when editing is completed.
    // It must return the new value to be stored in the cell.

    @Override
    public Object getCellEditorValue() {
        return tf.getSelectedItem();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
    }
}
