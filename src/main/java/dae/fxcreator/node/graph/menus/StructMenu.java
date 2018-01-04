package dae.fxcreator.node.graph.menus;

import dae.fxcreator.io.FXProject;
import dae.fxcreator.io.ShaderStructCollection;
import dae.fxcreator.node.ShaderStruct;
import dae.fxcreator.node.graph.TechniqueEditorPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;

/**
 *
 * @author Koen
 */
public class StructMenu extends JPopupMenu implements ActionListener {

    /**
     * The FXProject to adapt.
     */
    private FXProject project;
    /**
     * The tree object to get the selected technqiue from.
     */
    private JTree tree;
    private TechniqueEditorPanel panel;
    private JMenuItem mnuAddStruct;

    /**
     * Creates a new pass menu
     * @param tree
     */
    public StructMenu(JTree tree, TechniqueEditorPanel panel) {
        this.tree = tree;
        this.panel = panel;

        mnuAddStruct = new JMenuItem("New Struct");
        mnuAddStruct.setActionCommand("newstruct");
        mnuAddStruct.addActionListener(this);

        this.add(mnuAddStruct);
    }

    /**
     * The project to edit.
     * @param project
     */
    public void setProject(FXProject project){
        this.project = project;
    }

    /**
     * Called when the item was clicked.
     * @param ae
     */
    public void actionPerformed(ActionEvent ae) {
        tree.getSelectionPath();
        ShaderStructCollection collection = project.getStructCollection();
        String name = collection.createUniqueName();
        ShaderStruct struct = new ShaderStruct(name,project.getShaderTypeLibrary());
        panel.editStruct(struct);
        project.addShaderStruct(struct);
        project.notifyNodeAdded(struct, tree.getSelectionPath(), collection.getIndex(struct));
        
    }
}
