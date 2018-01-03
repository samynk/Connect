/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.node.graph.menus;

import dae.fxcreator.io.FXProject;
import dae.fxcreator.io.Pass;
import dae.fxcreator.io.Technique;
import java.awt.Component;
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
public class PassMenu extends JPopupMenu implements ActionListener {

    /**
     * The FXProject to adapt.
     */
    private FXProject project;
    /**
     * The tree object to get the selected technqiue from.
     */
    private JTree tree;
    private JMenuItem mnuDeletePass;
    private JMenuItem mnuChangeName;

    /**
     * Creates a new pass menu
     * @param tree
     */
    public PassMenu(JTree tree) {
        this.tree = tree;

        mnuDeletePass = new JMenuItem("Delete Pass");
        mnuDeletePass.setActionCommand("deletepass");
        mnuDeletePass.addActionListener(this);

        mnuChangeName = new JMenuItem("Change name");
        mnuChangeName.setActionCommand("changename");
        mnuChangeName.addActionListener(this);

        this.add(mnuDeletePass);
        this.add(mnuChangeName);
    }

    /**
     * Set the enabled state of some of the items.
     * @param invoker
     * @param x
     * @param y
     */
    @Override
    public void show(Component invoker, int x, int y) {
        TreePath path = tree.getSelectionPath();
        if (path.getLastPathComponent() instanceof Pass) {
            Pass p = (Pass) path.getLastPathComponent();
            Technique t = getTechniqueFromPath(path);
            if ( t != null )
                mnuDeletePass.setEnabled(t.getChildCount() > 1);
            else
                mnuDeletePass.setEnabled(false);
            mnuChangeName.setEnabled(true);
        } else {
            mnuDeletePass.setEnabled(false);
            mnuChangeName.setEnabled(false);
        }
        super.show(invoker, x, y);
    }

    private Technique getTechniqueFromPath(
            TreePath path) {
        int techIndex = path.getPathCount() - 2;
        if (techIndex >= 0) {
            Object node = path.getPathComponent(techIndex);
            if (node instanceof Technique) {
                Technique t = (Technique) node;
                return t;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Sets the FXProject to edit.
     * @param project the project to edit.
     */
    public void setProject(FXProject project) {
        this.project = project;
    }

    /**
     * Called when a menu is clicked.
     * @param ae
     */
    public void actionPerformed(ActionEvent ae) {
        if ( ae.getActionCommand().equals("deletepass")){
            Technique t = getTechniqueFromPath(tree.getSelectionPath());
            Object pass = tree.getSelectionPath().getLastPathComponent();
            if ( t!=null &&pass instanceof Pass){
                Pass p = (Pass)pass;
               int removeIndex = t.getIndex(p);
                t.removePass(p);
                project.notifyNodeRemoved(p, tree.getSelectionPath().getParentPath(),removeIndex);
            }
            tree.repaint();
        }else if ( ae.getActionCommand().equals("changename")){
            tree.startEditingAtPath(tree.getSelectionPath());
        }
    }
}
