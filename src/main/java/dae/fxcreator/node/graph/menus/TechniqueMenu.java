package dae.fxcreator.node.graph.menus;

import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.node.project.Pass;
import dae.fxcreator.node.project.Technique;
import dae.fxcreator.node.project.TechniqueCollection;
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
public class TechniqueMenu extends JPopupMenu implements ActionListener {

    /**
     * The FXProject to adapt.
     */
    private FXProject project;
    /**
     * The tree object to get the selected technqiue from.
     */
    private JTree tree;
    private JMenuItem mnuChangeName;
    private JMenuItem mnuDeleteTechnique;
    private JMenuItem mnuAddPass;

    /**
     * Creates a new TechniqueMenu that will allow the user to change the name
     * of a technique , or delete a technique.
     * @param tree the JTree object.
     */
    public TechniqueMenu(JTree tree) {
        this.tree = tree;

        mnuChangeName = new JMenuItem("Change name");
        mnuChangeName.setActionCommand("changename");
        this.add(mnuChangeName);

        mnuDeleteTechnique = new JMenuItem("Delete technique");
        mnuDeleteTechnique.setActionCommand("deletetechnique");
        this.add(mnuDeleteTechnique);

        mnuAddPass = new JMenuItem("Add Pass");
        mnuAddPass.setActionCommand("addpass");
        this.add(mnuAddPass);

        mnuChangeName.addActionListener(this);
        mnuDeleteTechnique.addActionListener(this);
        mnuAddPass.addActionListener(this);

    }

    /**
     * Set the enabled state of some of the items.
     * @param invoker
     * @param x
     * @param y
     */
    @Override
    public void show(Component invoker, int x, int y) {
        mnuDeleteTechnique.setEnabled(project.getNrOfTechniques() != 1);
        super.show(invoker, x, y);
    }

    /**
     * Sets the FXProject to edit.
     * @param project the project to edit.
     */
    public void setProject(FXProject project) {
        this.project = project;
    }

    private TechniqueCollection getTCFromPath(TreePath path)
    {
        int index = path.getPathCount()-2;
        if ( index >= 0 ){
            Object o = path.getPathComponent(index);
            if ( o != null && o instanceof TechniqueCollection){
                return (TechniqueCollection)o;
            }else
                return null;
        }else
            return null;
    }

    /**
     * Called when the user clicks the menu.
     * @param e the event with the action.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("changename")) {
            tree.startEditingAtPath(tree.getSelectionPath());
        } else if (e.getActionCommand().equals("deletetechnique")) {
            TreePath selectedPath = tree.getSelectionPath();
            if (selectedPath.getLastPathComponent() instanceof Technique) {
                Technique tech = (Technique) selectedPath.getLastPathComponent();
                TechniqueCollection tc = getTCFromPath(selectedPath);
                int removedIndex = tc.getIndex(tech);
                project.removeTechnique(tech);
                project.notifyNodeRemoved(tech, selectedPath.getParentPath(),removedIndex);
                tree.repaint();
            }
        }else if ( e.getActionCommand().equals("addpass")){
            TreePath selectedPath = tree.getSelectionPath();
            if (selectedPath.getLastPathComponent() instanceof Technique) {
                Technique tech = (Technique) selectedPath.getLastPathComponent();
                String passName = tech.createUniquePassName();
                Pass pass =new Pass(passName);
                tech.addPass(pass);
                project.notifyNodeAdded(pass, selectedPath,tech.getIndex(pass));
                tree.repaint();
            }
        }
    }
}
