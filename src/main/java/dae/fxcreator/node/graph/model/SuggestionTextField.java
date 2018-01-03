/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.node.graph.model;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;

/**
 * Completes a word from the dictionary based on the position of the caret.
 * @author Koen Samyn
 */
public class SuggestionTextField extends JComboBox implements KeyListener {

    private ArrayList<String> suggestions;
    private SemanticDataModel model;

    /**
     * Creates a new suggestion text field with a list of words as suggestions.
     * @param suggestions the list of suggestions.
     */
    public SuggestionTextField(SemanticDataModel model) {
        this.model = model;
        this.setModel(model);
        this.addKeyListener(this);
    }
    
    public static void main(String[] args) {
        ArrayList<String> suggestions = new ArrayList<String>();

        suggestions.add("WORLD");
        suggestions.add("WORLDVIEW");
        suggestions.add("WORLDVIEWPROJECTION");
        suggestions.add("WORLDVIEWINVERSE");

        Collections.sort(suggestions);

        int index = Collections.binarySearch(suggestions, "WORLDVIEWP");
        System.out.println("index :" + index);
    }

    public void keyTyped(KeyEvent e) {
        String text = this.getEditor().getItem().toString();
        int index = Collections.binarySearch(suggestions, text);
        if (index < 0) {
            int insertionPoint = -(index + 1);
            if (insertionPoint >= 0 && insertionPoint < suggestions.size()) {
               this.setSelectedIndex(insertionPoint);
            }
        }else{
            this.setSelectedIndex(index);
        }
    }

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {
        
    }
}
