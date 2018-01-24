package dae.fxcreator.node.graph;

import dae.fxcreator.node.templates.NodeTemplateLibrary;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 * This panel maintains a list of global nodes that can be used to create variables
 * that can be used in multiple stages. For non shader languages (such as Java, C# and C++) 
 * this translates into member variables. Only data types (such as singles, vectors, matrices, ...) 
 * can be added to this panel.
 * @author Koen Samyn
 */
public class GlobalNodeEditorPanel extends JPanel{
    private NodeTemplateLibrary library;
    
    /**
     * Creates a new global node editor panel.
     */
    public GlobalNodeEditorPanel(){
        setMinimumSize(new Dimension(100,100));
    }
    
    
}
