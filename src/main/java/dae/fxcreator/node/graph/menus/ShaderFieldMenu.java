package dae.fxcreator.node.graph.menus;

import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.node.StructField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * The menu for the ShaderField objects.
 *
 * @author Koen
 */
public class ShaderFieldMenu extends JPopupMenu implements ActionListener {

    /**
     * The FXProject to adapt.
     */
    private FXProject project;
    /**
     * The tree object to get the selected technqiue from.
     */
    private JTree tree;

    /**
     * Creates a new ShaderFieldMenu object that is bound to the JTree object.
     *
     * @param tree the tree that displays the popup menu.
     */
    public ShaderFieldMenu(JTree tree) {
        this.tree = tree;

        JMenuItem mnuChangeField = new JMenuItem("Change Field");
        mnuChangeField.addActionListener(this);
        mnuChangeField.setActionCommand("changefield");
        add(mnuChangeField);

        JMenuItem mnuDeleteField = new JMenuItem("Delete Field");
        mnuDeleteField.addActionListener(this);
        mnuDeleteField.setActionCommand("deletefield");
        add(mnuDeleteField);
    }

    /**
     * Sets the project to edit with this menu.
     *
     * @param project
     */
    public void setProject(FXProject project) {
        this.project = project;
    }

    /**
     * Called when the menu was clicked.
     *
     * @param ae the ActionEvent object.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        String ac = ae.getActionCommand();
        if (ac.equals("changefield")) {
            TreePath selection = tree.getSelectionPath();
            if (selection != null) {
                tree.startEditingAtPath(selection);
            }
        } else if (ac.equals("deletefield")) {
            TreePath selection = tree.getSelectionPath();
            if (selection != null && selection.getLastPathComponent() instanceof StructField) {
                StructField field = (StructField) selection.getLastPathComponent();
                field.deleteFromStruct();
                //project.notifyNodeRemoved(field, selection.getParentPath(), index);
            }
        }
    }
}
