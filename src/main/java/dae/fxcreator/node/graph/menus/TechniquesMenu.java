package dae.fxcreator.node.graph.menus;

import dae.fxcreator.io.FXProject;
import dae.fxcreator.io.Technique;
import dae.fxcreator.io.TechniqueCollection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 *
 * @author Koen
 */
public class TechniquesMenu extends JPopupMenu implements ActionListener {

    /**
     * The FXProject to adapt.
     */
    private FXProject project;
    /**
     * The tree object to get the selected technqiue from.
     */
    private JTree tree;

    /**
     * Creates a new TechniqueMenu that will allow the user to change the name
     * of a technique , or delete a technique.
     * @param project the project to edit.
     * @param tree the JTree object.
     */
    public TechniquesMenu(JTree tree) {
        this.tree = tree;

        JMenuItem mnuAddTechnique = new JMenuItem("Add technique");
        mnuAddTechnique.setActionCommand("addtechnique");
        this.add(mnuAddTechnique);
        mnuAddTechnique.addActionListener(this);
    }

    /**
     * Sets the FXProject to edit.
     * @param project the project to edit.
     */
    public void setProject(FXProject project) {
        this.project = project;
    }

    /**
     * Called when a menu was clicked.
     * @param e the ActionEvent object.
     */
    public void actionPerformed(ActionEvent e) {
        if (project != null) {
            TreePath selectionPath = tree.getSelectionPath();
            Object collection = selectionPath.getLastPathComponent();
            if (collection instanceof TechniqueCollection) {
                TechniqueCollection tc = (TechniqueCollection)collection;
                String name = tc.createUniqueTechniqueName();
                Technique tech = new Technique(project, name);
                tc.addTechnique(tech);
                project.notifyNodeAdded(tech, selectionPath,tc.getIndex(tech));
            }
        }
    }
}
