package dae.fxcreator.node.graph.menus;

import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.ui.actions.ActionManager;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ModulesMenu extends JPopupMenu{
    /**
     * The FXProject to adapt.
     */
    private FXProject project;
    /**
     * The tree object to get the selected technqiue from.
     */
    private JTree tree;
    
    /**
     * Creates a new modules menu.
     */
    public ModulesMenu(JTree tree){
        this.tree = tree;
        initActions();
    }
    
    private void initActions(){
        Action addModule = ActionManager.getInstance().getAction("Modules.Add");
        add(new JMenuItem(addModule));
        Action removeModule = ActionManager.getInstance().getAction("Modules.Remove");
        add(new JMenuItem(removeModule));
    }
}
