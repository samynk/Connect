package dae.fxcreator.node.gui;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Koen
 */
public class GraphFontListModel implements ListModel {
    private ArrayList<GraphFont> fonts = new ArrayList<GraphFont>();
    
    private ArrayList<ListDataListener> listeners = new ArrayList<ListDataListener>();

    public GraphFontListModel(Collection<GraphFont> fonts){
        this.fonts.addAll(fonts);
    }
    
    /**
     * Returns the number of fonts.
     * @return the number of fonts.
     */
    public int getSize() {
        return fonts.size();
    }

    /**
     * Returns the element at the specified index.
     * @param index the index of the GraphFont.
     * @return the GraphFont.
     */
    public Object getElementAt(int index) {
        return fonts.get(index);
    }

    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

}
