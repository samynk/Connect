package dae.fxcreator.gui.model;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class GradientListModel implements ListModel {

    private final ArrayList<GraphGradient> gradients = new ArrayList<>();
    private final ArrayList<ListDataListener> listeners = new ArrayList<>();

    public GradientListModel(Collection<GraphGradient> gradients) {
        this.gradients.addAll(gradients);
    }

    /**
     * Returns the number of fonts.
     *
     * @return the number of fonts.
     */
    public int getSize() {
        return gradients.size();
    }

    /**
     * Returns the element at the specified index.
     *
     * @param index the index of the GraphFont.
     * @return the GraphFont.
     */
    @Override
    public Object getElementAt(int index) {
        return gradients.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
}
