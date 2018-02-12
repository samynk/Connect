package dae.fxcreator.gui.model;

import dae.fxcreator.node.StructField;
import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.node.project.Pass;
import dae.fxcreator.node.project.ShaderStage;
import dae.fxcreator.node.project.Technique;
import dae.fxcreator.util.List;
import java.util.ArrayList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ProjectTreeModel implements TreeModel {

    private final FXProject root;
    private final ArrayList<TreeModelListener> listeners = new ArrayList<>();

    /**
     * Creates a new ProjectTreeModel object.
     *
     * @param root the root of the tree.
     */
    public ProjectTreeModel(FXProject root) {
        this.root = root;
    }

    /**
     * Returns the root of the tree.
     *
     * @return
     */
    @Override
    public Object getRoot() {
        return root;
    }

    /**
     * Gets a child of the specified parent. Either the parent is this object,
     * or it is an instance of of the TreeNode class.
     *
     * @param parent the parent object to get the child from.
     * @param index the index of the child.
     * @return the child object at the specific index.
     */
    @Override
    public Object getChild(Object parent, int index) {
        if (parent == root) {
            switch (index) {
                case 0:
                    return root.getStageCollection();
                case 1:
                    return root.getStateColllection();
                default:
                    break;
            }
        } else if (parent instanceof List) {
            List l = (List) parent;
            return l.getChild(index);
        }
        return null;
    }

    /**
     * Gets the child count of the provided tree node object.
     *
     * @param parent the parent to get the child node from.
     * @return the child count.
     */
    @Override
    public int getChildCount(Object parent) {
        if (parent == root) {
            return 2;
        } else if (parent instanceof List) {
            List l = (List) parent;
            return l.getChildCount();
        }
        return 0;
    }

    /**
     * Checks if this node is a leaf.
     *
     * @param node the node to check.
     * @return true if the node is a leaf, false if the node is not a leaf.
     */
    @Override
    public boolean isLeaf(Object node) {
        if (node == root) {
            return false;
        } else {
            return !(node instanceof List);
        }
    }

    /**
     * Called when a cell was edited.
     *
     * @param path the path that was changed.
     * @param newValue the new value for the display name.
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // change the name.
        Object o = path.getLastPathComponent();
        if (o instanceof Technique) {
            Technique t = (Technique) o;
            t.setName(newValue.toString());
        } else if (o instanceof Pass) {
            Pass p = (Pass) o;
            p.setName(newValue.toString());
        } else if (o instanceof ShaderStage) {
            ShaderStage ss = (ShaderStage) o;
            ss.setId(newValue.toString());
        } else if (o instanceof StructField) {
            StructField sf = (StructField) o;
            sf.decode(newValue.toString());
        }
    }

    /**
     * Gets the index of the child object.
     *
     * @param parent the parent of the child object.
     * @param child the child object.
     * @return the index.
     */
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (root == parent) {
            if (child == root.getGlobalNodes()) {
                return 0;
            } else if (child == root.getStageCollection()) {
                return 1;
            }
        }else if ( parent instanceof List ){
            List l = (List)parent;
            return l.getIndexOfChild(child);
        }
        return -1;
    }

    /**
     * Adds a TreeModel listener to the list of listeners.
     *
     * @param l the listener to add.
     */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(l);
    }

    /**
     * Removes the treemodel listener from the list of listeners.
     *
     * @param l the listener to remove.
     */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }

    /**
     * Notifies the listeners that a row was removed from the model.
     *
     * @param toRemove the tree node that was removed.
     * @param path the path to the tree node.
     * @param index the index of the node in the parent path.
     */
    public void notifyNodeRemoved(Object toRemove, TreePath path, int index) {
        int indices[] = {index};
        Object removed[] = {toRemove};
        TreeModelEvent tme = new TreeModelEvent(this, path, indices, removed);
        for (TreeModelListener listener : listeners) {
            listener.treeNodesRemoved(tme);
        }
    }

    /**
     * Notifies the listeners that a row was added to the model.
     *
     * @param toAdd the node that was added to the tree.
     * @param path the parent path.
     * @param index the index in the parent path.
     */
    public void notifyNodeAdded(Object toAdd, TreePath path, int index) {
        int indices[] = {index};
        Object added[] = {toAdd};

        TreeModelEvent tme = new TreeModelEvent(this, path, indices, added);
        for (TreeModelListener listener : listeners) {
            listener.treeNodesInserted(tme);
        }
    }

    /**
     * Notifies the listeners that the struct from a certain point was changed.
     *
     * @param selectionPath
     */
    public void notifyNodeChanged(TreePath selectionPath) {
        TreeModelEvent tme = new TreeModelEvent(this, selectionPath);
        for (TreeModelListener listener : listeners) {
            listener.treeStructureChanged(tme);
        }
    }
}
