/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dae.fxcreator.node.graph.model;

/**
 * An interface that makes it easier to edit inputs/outpus for
 * a shaderstage or group node.
 * @author Koen
 */
public interface IOTableModel {
    /**
     * Add an empty row to the inputs/outputs.
     */
    public void add();
    /**
     * Delete a row from the table.
     * @param index the row to remove.
     */
    public void delete(int index);
}
