/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dae.fxcreator.node.gui;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Koen
 */
public class GradientListModel implements ListModel{
private ArrayList<GraphGradient> gradients = new ArrayList<GraphGradient>();

    private ArrayList<ListDataListener> listeners = new ArrayList<ListDataListener>();

    public GradientListModel(Collection<GraphGradient> gradients){
        this.gradients.addAll(gradients);
    }

    /**
     * Returns the number of fonts.
     * @return the number of fonts.
     */
    public int getSize() {
        return gradients.size();
    }

    /**
     * Returns the element at the specified index.
     * @param index the index of the GraphFont.
     * @return the GraphFont.
     */
    public Object getElementAt(int index) {
        return gradients.get(index);
    }

    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
}
