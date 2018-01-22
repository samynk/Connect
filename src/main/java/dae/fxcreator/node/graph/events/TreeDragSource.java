package dae.fxcreator.node.graph.events;

//TreeDragSource.java

import dae.fxcreator.node.TypedNode;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

//A drag source wrapper for a JTree. This class can be used to make
//a rearrangeable DnD tree with the TransferableTreeNode class as the
//transfer data type.

public class TreeDragSource implements DragSourceListener, DragGestureListener {

  DragSource source;

  DragGestureRecognizer recognizer;



  DefaultMutableTreeNode oldNode;
  Class[] draggableClasses;

  JTree sourceTree;

  public TreeDragSource(JTree tree, int actions,Class<? extends TypedNode> ... draggableClasses) {
    sourceTree = tree;
    source = new DragSource();
    recognizer = source.createDefaultDragGestureRecognizer(sourceTree,
        actions, this);
    this.draggableClasses = draggableClasses;
  }

  /*
   * Drag Gesture Handler
   */
  public void dragGestureRecognized(DragGestureEvent dge) {
    TreePath path = sourceTree.getSelectionPath();
    if ((path == null) || (path.getPathCount() <= 1)) {
      // We can't move the root node or an empty selection
      return;
    }
    Object selected = path.getLastPathComponent();
    for ( Class c : draggableClasses){
        if (  c.isInstance(selected)){
            TypedNode tn = (TypedNode)selected;
            StringSelection transfer = new StringSelection(tn.getType()+"."+tn.getId());
            source.startDrag(dge, DragSource.DefaultMoveDrop, transfer, this);
        }
    }
    
    

    // If you support dropping the node anywhere, you should probably
    // start with a valid move cursor:
    //source.startDrag(dge, DragSource.DefaultMoveDrop, transferable,
    // this);
  }

  /*
   * Drag Event Handlers
   */
  public void dragEnter(DragSourceDragEvent dsde) {
  }

  public void dragExit(DragSourceEvent dse) {
  }

  public void dragOver(DragSourceDragEvent dsde) {
  }

  public void dropActionChanged(DragSourceDragEvent dsde) {
    System.out.println("Action: " + dsde.getDropAction());
    System.out.println("Target Action: " + dsde.getTargetActions());
    System.out.println("User Action: " + dsde.getUserAction());
  }

  public void dragDropEnd(DragSourceDropEvent dsde) {
    /*
     * to support move or copy, we have to check which occurred:
     */
    System.out.println("Drop Action: " + dsde.getDropAction());
    if (dsde.getDropSuccess()
        && (dsde.getDropAction() == DnDConstants.ACTION_MOVE)) {
      ((DefaultTreeModel) sourceTree.getModel())
          .removeNodeFromParent(oldNode);
    }

    /*
     * to support move only... if (dsde.getDropSuccess()) {
     * ((DefaultTreeModel)sourceTree.getModel()).removeNodeFromParent(oldNode); }
     */
  }
}
