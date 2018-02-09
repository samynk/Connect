package dae.fxcreator.node.graph.model;

import dae.fxcreator.node.IONode;
import dae.fxcreator.node.Semantic;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderOutput;
import dae.fxcreator.node.ShaderType;
import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * A model to view and edit the inputs / outputs of a ShaderStage, Custom code
 * node or GroupNode
 *
 * @author Koen
 */
public class IONodeInputTableModel implements TableModel, IOTableModel {

    private IONode node;

    public enum STATE {

        INPUT, OUTPUT
    };
    private final STATE state;
    private final ArrayList<TableModelListener> listeners = new ArrayList<>();

    /**
     * Creates a new IONodeInputTableModel.
     *
     * @param node the node to edit.
     * @param state edit the input or output nodes of the node.
     */
    public IONodeInputTableModel(IONode node, STATE state) {
        this.node = node;
        this.state = state;
    }

    /**
     * Returns the number of rows in this model.
     *
     * @return the number of rows.
     */
    @Override
    public int getRowCount() {
        if (node == null) {
            return 0;

        }
        switch (state) {
            case INPUT:
                return node.getInputs().size();
            case OUTPUT:
                return node.getOutputs().size();
        }
        return 0;
    }

    /**
     * Returns the number of columns.
     *
     * @return always 3.
     */
    @Override
    public int getColumnCount() {
        return 3;
    }

    /**
     * Return the column names.
     *
     * @param columnIndex the index of the column.
     * @return "Type", "Name" or "Semantic".
     */
    @Override
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
     * Returns the Class of the object, for usage with custom cell editors.
     *
     * @param columnIndex the column to get the class for.
     * @return ShaderType, String or Semantic class.
     */
    @Override
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
     * Checks if the cell is editable.
     *
     * @param rowIndex the row index of the cell.
     * @param columnIndex the column index of the cell.
     * @return always true.
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /**
     * Returns the value of the specific cell.
     *
     * @param rowIndex the row index of the cell.
     * @param columnIndex the column index of the cell.
     * @return the value of the cell.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (state == STATE.INPUT) {
            ShaderInput input = node.getInputs().get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return input.getIOType();
                case 1:
                    return input.getName();
                case 2:
                    return input.getSemantic();
            }
        } else if (state == STATE.OUTPUT) {
            ShaderOutput output = node.getOutputs().get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return output.getIOType();
                case 1:
                    return output.getName();
                case 2:
                    return output.getSemantic();
            }
        }
        return null;
    }

    /**
     * Sets the value of the cell.
     *
     * @param aValue the new value for the cell.
     * @param rowIndex the row index of the cell.
     * @param columnIndex the column index of the cell.
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (state == STATE.INPUT) {
            ShaderInput input = node.getInputs().get(rowIndex);

            switch (columnIndex) {
                case 0:
                    ShaderType st = (ShaderType) aValue;
                    input.setIOType(st);
                    node.typeChanged(input);
                    break;
                case 1:
                    input.setName(aValue.toString());
                    break;
                case 2:
                    input.setSemantic(aValue.toString());
                    break;
            }
            //graphNode.updateIO(oldName,input.getName());
        } else if (state == STATE.OUTPUT) {
            ShaderOutput output = node.getOutputs().get(rowIndex);
            String oldName = output.getName();
            switch (columnIndex) {
                case 0:
                    ShaderType st = (ShaderType) aValue;
                    output.setIOType(st);
                    node.typeChanged(output);
                    break;
                case 1:
                    output.setName(aValue.toString());
                    break;
                case 2:
                    output.setSemantic(aValue.toString());
                    break;
            }
            //graphNode.updateIO(oldName,output.getName());
        }
        TableModelEvent tme = new TableModelEvent(this, rowIndex, rowIndex, columnIndex, TableModelEvent.UPDATE);
        notifyListeners(tme);
    }

    /**
     * Deletes a row of the model.
     *
     * @param index the row to delete.
     */
    @Override
    public void delete(int index) {
        switch (state) {
            case INPUT:
                String inputName = node.getInputs().get(index).getName();
                node.removeInput(index);
                //graphNode.removeIO(inputName);
                break;
            case OUTPUT:
                String outputName = node.getOutputs().get(index).getName();
                node.removeOutput(index);
                //graphNode.removeIO(outputName);
                break;
        }

        TableModelEvent tme = new TableModelEvent(this, index, index, -1, TableModelEvent.DELETE);
        notifyListeners(tme);
    }

    /**
     * Adds an empty row to the model.
     */
    @Override
    public void add() {
        int index = -1;
        switch (state) {
            case INPUT: {
                String name = "input";
                int i = 1;
                do {
                    name = "input" + (i++);
                } while (node.hasInput(name));

                ShaderInput input = new ShaderInput(node, name, null, "FLOAT");
                node.addInput(input);
                //graphNode.addInput(name);
                break;
            }
            case OUTPUT: {
                String name = "output";
                int i = 1;
                do {
                    name = "output" + (i++);
                } while (node.hasOutput(name));

                ShaderOutput output = new ShaderOutput(node, name, null, "FLOAT", null);
                node.addOutput(output);
                //graphNode.addOutput(name);
                break;
            }
        }
        TableModelEvent tme = new TableModelEvent(this, index, index, -1, TableModelEvent.INSERT);
        notifyListeners(tme);
    }

    private void notifyListeners(TableModelEvent e) {
        for (TableModelListener l : listeners) {
            l.tableChanged(e);
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.add(l);
    }
}
