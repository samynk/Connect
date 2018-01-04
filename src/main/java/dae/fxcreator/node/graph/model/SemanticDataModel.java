package dae.fxcreator.node.graph.model;

import dae.fxcreator.io.FXSettingListener;
import dae.fxcreator.io.FXSettings;
import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.node.Semantic;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Koen
 */
public class SemanticDataModel implements ComboBoxModel, FXSettingListener {

    private String selectedItem;
    private ArrayList<String> suggestions;
    private ArrayList<ListDataListener> listeners = new ArrayList<ListDataListener>();

    public SemanticDataModel() {
        suggestions = new ArrayList<String>();
        FXSingleton.getSingleton().addFXSettingListener(this);

        FXSettings fxSettings = FXSingleton.getSingleton().getFXSettings();
        if (fxSettings != null) {
            for (Semantic s : fxSettings.getSemantics()) {
                suggestions.add(s.getValue());
            }
            Collections.sort(suggestions);
        }
    }

    public SemanticDataModel(ArrayList<String> suggestions) {
        this.suggestions = suggestions;
        Collections.sort(suggestions);
    }

    public void setSelectedItem(Object anItem) {
        this.selectedItem = anItem.toString();
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public int getSize() {
        return suggestions.size();
    }

    public Object getElementAt(int index) {
        return suggestions.get(index);
    }

    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    public void fxSettingChanged(FXSettings fxSetting) {
        suggestions.clear();
        for (Semantic s : fxSetting.getSemantics()) {
            suggestions.add(s.getValue());
        }
        
        System.out.println("Suggestions are : "+suggestions.isEmpty());
        if (!suggestions.isEmpty()) {
            Collections.sort(suggestions);
            ListDataEvent lde = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, suggestions.size() - 1);
            for (ListDataListener l : listeners) {
                l.intervalAdded(lde);
            }
        }
        
    }
}
