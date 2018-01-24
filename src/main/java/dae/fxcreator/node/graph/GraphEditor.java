package dae.fxcreator.node.graph;

import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.node.project.ShaderStage;
import dae.fxcreator.io.events.SettingListener;
import dae.fxcreator.node.settings.Setting;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.ShaderIO;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderOutput;
import dae.fxcreator.node.ShaderStruct;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.RepaintManager;

/**
 * This class allows the user to place components in a graph layout.
 * @author samynk
 */
public class GraphEditor extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, DropTargetListener, SettingListener, KeyListener {

    private ArrayList<JGraphNode> nodes = new ArrayList<JGraphNode>();
    private HashMap<Integer, JGraphNode> nodeMap = new HashMap<Integer, JGraphNode>();
    private HashMap<Integer, ArrayList<JGraphNode>> refNodeMap = new HashMap<Integer, ArrayList<JGraphNode>>();
    private ArrayList<Connector> connectors = new ArrayList<Connector>();
    // selection
    private JGraphNode selectedNode;
    private ArrayList<JGraphNode> selectedNodes = new ArrayList<JGraphNode>();
    private Connector selectedConnector;
    private JGraphNode insertionNode;
    private JTerminal startTerminal;
    private JTerminal endTerminal;
    private GraphState currentState = GraphState.IDLE;
    private FXProject project;
    final static float dash1[] = {10.0f, 3.0f, 3.0f, 3.0f};
    final static BasicStroke dashed = new BasicStroke(2.0f,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 5.0f);
    private float currentScale = 100.0f;
    private Point offset = new Point(0, 0);
    private Point insertLocation = new Point(100, 100);
    private AffineTransform currentInverseTransform;
    private ArrayList<ZoomListener> zoomListeners = new ArrayList<ZoomListener>();
    private boolean fullRepaint = false;
    
    /** Creates a new instance of GraphEditor */
    public GraphEditor() {
        addMouseMotionListener(this);
        addMouseListener(this);
        
        addMouseWheelListener(this);
        setGraphLayout(Layout.FREE);
        currentInverseTransform = new AffineTransform();
        //setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.lightGray));
        setBackground(Color.darkGray);
        setDoubleBuffered(true);
    }

    public GraphEditor(Layout layout) {
        addMouseMotionListener(this);
        addMouseListener(this);
        //addMouseMotionListener(this);
        addMouseWheelListener(this);
        setGraphLayout(layout);
        currentInverseTransform = new AffineTransform();
        setBackground(Color.darkGray);
        setDoubleBuffered(true);
        //setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.lightGray));
    }


    void setProject(FXProject project) {
        this.project = project;
    }

    public void addZoomListener(ZoomListener listener) {
        this.zoomListeners.add(listener);
    }

    public void removeZoomListener(ZoomListener listener) {
        this.zoomListeners.remove(listener);
    }

    private void notifyZoomChanged() {
        for (ZoomListener listener : zoomListeners) {
            listener.zoomChanged(Math.round(currentScale));
        }
    }

    /**
     * Select the first node, so we do not get into an invalid state.
     */
    public void selectFirstNode() {
        selectedNodes.clear();
        if (nodes.size() > 0) {
            JGraphNode first = nodes.get(0);
            first.setSelected(true);
            for (int i = 1; i < nodes.size(); ++i) {
                nodes.get(i).setSelected(false);
            }
            this.selectedNodes.add(first);
            this.nodeSelected(first);
        }
        repaint();
    }

    /**
     * Returns the list of selected nodes.
     * @return the list of selected nodes.
     */
    public java.util.List<IONode> getSelectedNodes() {
        ArrayList<IONode> result = new ArrayList<IONode>();
        for (JGraphNode node : this.selectedNodes) {
            result.add(node.getUserObject());
        }
        return result;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        int amount = e.getScrollAmount() * 2;
        if (e.getWheelRotation() > 0) {
            if (currentScale - amount > 10) {
                currentScale -= amount;
            }
        } else {
            if (currentScale + amount < 800) {
                currentScale += amount;
            }
        }
        repaint();
        notifyZoomChanged();
    }

    /**
     * Removes the selected nodes in this graph editor object.
     */
    public void removeSelectedNodes() {
        for (JGraphNode node : this.selectedNodes) {
            if (node.isRemovable() || node.isRefNode()) {
                this.removeNode(node);
            }
        }
        selectedNodes.clear();
        this.selectedNode = null;
    }

    public void setInsertPosition(Point point) {
        this.insertLocation = point;
    }

    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void keyPressed(KeyEvent e) {
        //System.out.println("key pressed");
        //throw new UnsupportedOperationException("Not supported yet.");
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            this.currentState = GraphState.PAN;
        }
    }

    public void keyReleased(KeyEvent e) {
        //System.out.println("Key typed");
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            this.removeSelectedNodes();
            if (this.selectedConnector != null) {
                this.removeLink(selectedConnector);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            this.currentState = GraphState.IDLE;
        }
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setScale(int i) {
        this.currentScale = i;
        repaint();
    }

    public Iterable<JGraphNode> getGraphNodes() {
        return this.nodes;
    }

    void setTranslation(int minx, int miny) {
        this.offset.x = minx;
        this.offset.y = miny;
    }

    /**
     * Updates the styles of all the nodes.
     */
    public void updateStyles() {
        for (JGraphNode node : this.nodes) {
            node.updateStyle();
        }
        repaint();
    }

    public void setFullRepaint() {
        fullRepaint = true;
    }

    public enum Layout {

        HORIZONTAL, VERTICAL, FREE
    };
    private Layout layout = Layout.FREE;

    public enum GraphState {

        IDLE, CONTROLDRAG, NODEINSERTION, LINKINSERTION, LINKDRAG, PAN
    };
    private Point currentMousePosition;

    /**
     * The current selection state.
     */
    public enum SelectionState {

        NOTHING, NODE, TERMINAL, CONNECTOR;
    }
    private SelectionState selectionState;
    /**
     * The menu to use for right click events.
     */
    private JPopupMenu generalMenu;

    
    /**
     * Return false because components can be on top of each other.
     * @return always false.
     */
    @Override
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    /**
     * Sets the popupmenu to use in this graphEditor when the user rightclicks on
     * the background.
     * @param menu
     */
    public void setGeneralPopupMenu(JPopupMenu menu) {
        this.generalMenu = menu;
    }

    public void enableDrop() {
        this.setDropTarget(new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this, true, null));
    }

    public void setGraphLayout(Layout layout) {
        this.layout = layout;
        if (null != layout) switch (layout) {
            case FREE:
                setLayout(null);
                break;
            case HORIZONTAL:
                setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));
                break;
            case VERTICAL:
                setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
                break;
            default:
                break;
        }
    }

    /**
     * Remove all the nodes from this GraphEditor.
     */
    public void clearNodes() {
        nodes.clear();
        refNodeMap.clear();
        nodeMap.clear();
        selectedNode = null;
        selectedNodes.clear();
        currentState = GraphState.IDLE;
        this.removeAll();
        //this.repaint();
        insertionNode = null;
        startTerminal = null;
        endTerminal = null;

        this.dragStruct = null;
        this.selectedConnector = null;
        this.selectionState = SelectionState.NOTHING;
    }

    /**
     * Clears all the connector objects.
     */
    private void clearConnectors() {
        connectors.clear();
    }

    /**
     * Add a connector so the selection can be checked.
     * @param c the connector to add
     */
    protected void addConnector(Connector c) {
        connectors.add(c);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle r = g.getClipBounds();
        g.setColor(Color.darkGray);

        g.fillRect(r.x, r.y, r.width, r.height);

        //g.clearRect(r.x, r.y, r.width, r.height);
        if (layout == Layout.FREE) {
            g2.translate(this.offset.x, this.offset.y);
            g2.scale(currentScale / 100.0f, currentScale / 100.0f);
            try {
                currentInverseTransform = g2.getTransform().createInverse();
            } catch (NoninvertibleTransformException ex) {
                System.out.println(offset);
                System.out.println(currentScale);
                System.out.println("should not happen");
            }
        }
        
       
//        Rectangle r = g.getClipBounds();
//        g.setColor(Color.darkGray);
//        g.fillRect(r.x, r.y, r.width, r.height);
//        if (layout == Layout.FREE) {
//            g2.setTransform(backup);
//        }
        super.paint(g);

    }

    @Override
    public void paintComponent(Graphics g) {

        //this.paintChildren(g);
        //super.paintComponent(g);
        //g.drawString("number of selected nodes : " + selectedNodes.size(),0,20);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        clearConnectors();
        for (JGraphNode node : nodes) {
            node.drawConnectors(this, (Graphics2D) g);
            minX = Math.min(minX, node.getX());
            minY = Math.min(minY, node.getY());
            maxX = Math.max(maxX, node.getX() + node.getWidth());
            maxY = Math.max(maxY, node.getY() + node.getHeight());
        }

        g2.setColor(Color.white);
        if (currentState == GraphState.LINKDRAG) {
            Point start = startTerminal.getConnectorPoint().getConnectorLocation();
            Point end = this.transformMousePositionToWorld(currentMousePosition);
            g2.drawLine(start.x, start.y, end.x, end.y);
        }
        // draw selection border around every selected node.
        for (JGraphNode selected : selectedNodes) {
            Point startPoint = selected.getLocation();
            Dimension size = selected.getSize();
            startPoint.x -= 3;
            startPoint.y -= 3;
            size.width += 6;
            size.height += 6;

            g2.setStroke(dashed);
            g2.draw(new RoundRectangle2D.Double(startPoint.x, startPoint.y, size.width, size.height, 10, 10));
        }
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    @Override
    public void paintChildren(Graphics g) {
        super.paintChildren(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        for (JGraphNode node : nodes) {
            node.drawConnectorAttachment(this, (Graphics2D) g);
        }
    }

    public Connector getSelectedConnector() {
        return selectedConnector;
    }

    public void setSelectedConnector(Connector connector) {
        this.selectedConnector = connector;
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame("Graph test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(400, 400));
        frame.add(new TechniqueEditorPanel());
        frame.setVisible(true);
    }

    private void addKeyListener() {
        if (this.layout == Layout.FREE) {
            this.requestFocus();
            if (this.getKeyListeners().length == 0) {
                this.addKeyListener(this);
            }
        }
    }
    Point2D.Float tmpSrcPoint = new Point2D.Float();
    Point2D.Float tmpDstPoint = new Point2D.Float();

    public Point transformMousePositionToWorld(Point mp) {
        tmpSrcPoint.setLocation(mp.x, mp.y);
        this.currentInverseTransform.transform(tmpSrcPoint, tmpDstPoint);
        return new Point(Math.round(tmpDstPoint.x), Math.round(tmpDstPoint.y));
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            if (getCurrentState() == GraphState.NODEINSERTION) {
                System.out.println("Inserting node!");
                Point p = transformMousePositionToWorld(mouseEvent.getPoint());
                insertionNode.setLocation(p.x, p.y);
                nodeMoved(insertionNode);
                insertionNode = null;
                setCurrentState(GraphState.IDLE);
            } else if (mouseEvent.getClickCount() == 2 || (mouseEvent.getModifiersEx() & MouseEvent.ALT_DOWN_MASK) > 0) {
                Component c = this.findComponentAt(mouseEvent.getPoint());
                System.out.println("doubleclicked on : " + c);
                if (c != null) {
                    JGraphNode jnode = (JGraphNode) findGraphNode(c);

                    if (jnode != null) {
                        System.out.println("doubleclicked on : " + jnode.getUserObject().getName());
                        nodeEntered(jnode);
                    }
                }
            } else {
                checkSelection(true, mouseEvent.getPoint(), mouseEvent.getModifiersEx());
            }
            repaint();
        }

    }

    private JGraphNode findGraphNode(Component c) {
        if (c instanceof JGraphNode) {
            return (JGraphNode) c;
        }
        while (c.getParent() != null) {
            if (c.getParent() instanceof JGraphNode) {
                return (JGraphNode) c.getParent();
            } else {
                c = c.getParent();
            }
        }
        return null;
    }

    @Override
    public void repaint() {
        //System.out.println("repainting");

        RepaintManager.currentManager(this).markCompletelyDirty(this);

        super.repaint();
    }

    /**
     * Checks which component was selected.
     * @param p the location of the mouse.
     * @param modifier the modifier for the selection.
     */
    private void checkSelection(boolean clearSelection, Point p, int modifier) {
        p = this.transformMousePositionToWorld(p);
        int ctrl = MouseEvent.CTRL_DOWN_MASK;
        boolean ctrlClick = (modifier & ctrl) > 0;
        if (clearSelection) {
            if (!ctrlClick) {
                for (JGraphNode node : this.nodes) {
                    node.setSelected(false);
                }
                selectedNodes.clear();
            }
            selectedConnector = null;
        }
        Component c = this.findComponentAt(p);
        JGraphNode sNode = null;
        JTerminal sTerminal = null;
        if (c != null) {
            if (c instanceof JGraphNode) {
                sNode = (JGraphNode) c;
            } else if (c instanceof JConnectorPoint) {
                JConnectorPoint jc = (JConnectorPoint) c;
                sNode = jc.getGraphNode();
            } else if (c instanceof JTerminal) {
                sTerminal = (JTerminal) c;
            } else if (c.getParent() instanceof JGraphNode) {
                sNode = (JGraphNode) c.getParent();
            } else if (c.getParent() instanceof JConnectorPoint) {
                JConnectorPoint jc = (JConnectorPoint) c.getParent();
                sNode = jc.getGraphNode();
            } else {
                c = c.getParent();
                while (c != null && c != this) {
                    c = c.getParent();
                    if (c instanceof JGraphNode) {
                        sNode = (JGraphNode) c;
                        break;
                    }
                }
            }
        }
        if (sNode != null) {
            sNode.setSelected(true);
            selectionState = SelectionState.NODE;
            setSelectedNode(sNode);

            if (!selectedNodes.contains(sNode)) {
                if (clearSelection) {
                    selectedNodes.add(sNode);
                } else {
                    if (!ctrlClick) {
                        for (JGraphNode node : this.nodes) {
                            node.setSelected(false);
                        }
                        selectedNodes.clear();
                    }
                    selectedNodes.add(sNode);
                }
            }
        } else if (sTerminal != null) {
            selectionState = SelectionState.TERMINAL;
            setStartTerminal(sTerminal);
        } else {
            for (Connector connector : this.connectors) {
                connector.setSelected(false);
                if (connector.isOnConnector(p)) {
                    connector.setSelected(true);
                    selectedNode = null;
                    selectionState = SelectionState.CONNECTOR;
                    selectedConnector = connector;
                }
            }
            selectionState = SelectionState.NOTHING;
        }
    }

    /**
     * Sets the start terminal for a connection.
     * @param sTerminal the terminal to start with.
     */
    private void setStartTerminal(JTerminal sTerminal) {
        this.startTerminal = sTerminal;
        if (startTerminal != null) {
            for (JGraphNode node : this.nodes) {
                node.highlightPossibleEndTerminals(startTerminal);
            }
        } else {
            for (JGraphNode node : this.nodes) {
                node.resetPossibleEndTerminals();
            }
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        //System.out.println("Mouse dragging");
        if (currentState != GraphState.PAN) {
            if (selectionState == SelectionState.NODE) {
                if (layout == Layout.FREE) {
                    int dx = mouseEvent.getX() - (int) currentMousePosition.getX();
                    int dy = mouseEvent.getY() - (int) currentMousePosition.getY();
                    float fdx = dx / (currentScale / 100.0f);
                    float fdy = dy / (currentScale / 100.0f);
                    for (JGraphNode node : this.selectedNodes) {
                        node.addLocation(fdx, fdy);
                        this.nodeMoved(node);

                    }
                    currentMousePosition = mouseEvent.getPoint();
                    repaint();
                }
            } else if (selectionState == SelectionState.TERMINAL) {
                currentState = GraphState.LINKDRAG;
                currentMousePosition = mouseEvent.getPoint();
                Point p = transformMousePositionToWorld(mouseEvent.getPoint());
                //p.x /= (currentScale / 100.0f);
                //p.y /= (currentScale / 100.0f);
                Component c = this.findComponentAt(p);
                if (c instanceof JTerminal) {
                    JTerminal endT = (JTerminal) c;
                    if (endTerminal != null && endT != endTerminal) {
                        endTerminal.setSelected(false);
                        endTerminal.repaint();
                    }
                    if (endT != endTerminal) {
                        endTerminal = endT;
                        endTerminal.setSelected(true);
                        endTerminal.repaint();
                    }
                    JConnectorPoint startCp = startTerminal.getConnectorPoint();
                    JConnectorPoint endCp = endTerminal.getConnectorPoint();
                    if (endT != startTerminal && startCp != endCp) {
                        ShaderIO startIO = startCp.getUserObject();
                        ShaderIO endIO = endCp.getUserObject();

                        if (endIO.accepts(startIO)) {
                            endTerminal.setSelected(true);
                            this.setCursor(Cursor.getDefaultCursor());
                        } else {
                            try {
                                this.setCursor(Cursor.getSystemCustomCursor("Invalid.32x32"));
                            } catch (AWTException ex) {
                                Logger.getLogger(GraphEditor.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (HeadlessException ex) {
                                Logger.getLogger(GraphEditor.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                } else {
                    endTerminal = null;
                    this.setCursor(Cursor.getDefaultCursor());
                }
                repaint();
            }
        } else {

            this.offset.x += mouseEvent.getX() - currentMousePosition.x;
            this.offset.y += mouseEvent.getY() - currentMousePosition.y;
            currentMousePosition = mouseEvent.getPoint();
            repaint();
        }
    }

    @Override
    public boolean contains(int x, int y) {
        return true;
    }

    public void mouseEntered(MouseEvent mouseEvent) {
       // System.out.println("Mouse entered");
    }

    public void mouseExited(MouseEvent mouseEvent) {
       // System.out.println("Mouse exited");
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        //System.out.println("Mouse moving");
        if (getCurrentState() == GraphState.NODEINSERTION) {
            if (insertionNode != null) {
                
                Point p = transformMousePositionToWorld(mouseEvent.getPoint());
                //System.out.println("mouse position:" + p);
                insertionNode.setLocation(p.x, p.y);
                this.nodeMoved(insertionNode);
                repaint();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON2) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            this.currentState = GraphState.PAN;
        }

        addKeyListener();
        checkSelection(false, mouseEvent.getPoint(), mouseEvent.getModifiersEx());
        currentMousePosition = mouseEvent.getPoint();
    }

    public void mouseReleased(MouseEvent mouseEvent) {

        /*
        if (currentState == GraphState.PAN) {
        return;
        }*/
        if (selectionState == SelectionState.TERMINAL) {
            if (startTerminal != null && endTerminal != null) {
                ShaderIO start = startTerminal.getConnectorPoint().getUserObject();
                ShaderIO end = endTerminal.getConnectorPoint().getUserObject();

                if (start.accepts(end)) {
                    if (start.isInput()) {
                        ShaderInput input = (ShaderInput) start;
                        ShaderOutput output = (ShaderOutput) end;
                        input.setConnectedInput(output);
                    } else {
                        ShaderInput input = (ShaderInput) end;
                        ShaderOutput output = (ShaderOutput) start;
                        input.setConnectedInput(output);
                    }
                }
            }
            if (startTerminal != null) {
                startTerminal.setSelected(false);
            }
            if (endTerminal != null) {
                endTerminal.setSelected(false);
            }
            repaint();
            setStartTerminal(null);
            endTerminal = null;
        } else {
            if (generalMenu != null) {
                Component c = getComponentAt(mouseEvent.getPoint());

                if (c == this && mouseEvent.isPopupTrigger()) {
                    generalMenu.show(this, mouseEvent.getX(), mouseEvent.getY());
                }
            }
        }
//        if (currentState == GraphState.LINKDRAG) {
//            currentState = GraphState.IDLE;
//        }
//
//        if ( currentState == GraphState.PAN && mouseEvent.getButton() == MouseEvent.BUTTON2) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        this.currentState = GraphState.IDLE;
    }

    /**
     * Adds a node to this GraphEditor panel. The node is added
     * to the list of nodes and to the nodeMap structure that allows you to
     * find a node based on its id.
     * @param node the node to add.
     */
    public void addNode(JGraphNode node) {
        nodes.add(node);
        if (node.isRefNode()) {
            ArrayList<JGraphNode> refNodes = refNodeMap.get(node.getInternalID());
            if ( refNodes == null ){
                refNodes =new ArrayList<JGraphNode>();
                refNodeMap.put(node.getInternalID(),refNodes);
            }
            refNodes.add(node);
        } else {
            nodeMap.put(node.getInternalID(), node);
            node.getUserObject().addSettingListener(this);
        }
        this.add(node);
        node.adjustSize();
        this.nodeAdded(node);
        this.repaint();
    }

    /**
     * Called when the setting was changed.
     * @param node the node that was changed.
     * @param s the setting that was changed.
     */
    public void settingChanged(IONode node, Setting s) {
        if (s.getGroup().equals("Node")) {
            if (s.getId().equals("id")) {
                repaint();
            } else if (s.getId().equals("name")) {
                JGraphNode jnode = this.findNode(node.getInternalID());
                if (jnode == null) {
                    return;
                }
                jnode.updateTitle();
                repaint();
            }
        } else {
            JGraphNode jnode = this.findNode(node.getInternalID());
            if (jnode != null) {
                jnode.repaint();
            }
            repaint();
        }
    }

    /**
     * Finds a node in this GraphEditor object;
     * @param id the id of the node.
     * @return the JGraphNode with the same id.
     */
    public JGraphNode findNode(int id) {
        return nodeMap.get(id);
    }

    /**
     * Finds the closest node to the given source node.
     * @param source the source node.
     * @param id the id of the node.
     * @return the closest node.
     */
    public JGraphNode findClosestNode(JGraphNode source, int id){
        ArrayList<JGraphNode> refNodes = refNodeMap.get(id);
        if (refNodeMap.get(id) == null ){
            return findNode(id);
        }else{
            JGraphNode original = findNode(id);

            double distance = distance(source,original);
            JGraphNode current = original;

            for ( JGraphNode refNode : refNodes){
                double newDistance = distance(source,refNode);
                if ( newDistance < distance ){
                    distance = newDistance;
                    current = refNode;
                }
            }
            return current;
        }
    }

    private double distance(JGraphNode first, JGraphNode second){
        float dx = first.getX() - second.getX();
        float dy = first.getY() - second.getY();

        return Math.sqrt(dx*dx + dy*dy);
    }
    
    /**
     * Removes a node from this GraphEditor.
     * @param node the node to remove.
     */
    public void removeNode(JGraphNode node) {

        if (node.isRefNode()){
            ArrayList<JGraphNode> refNodes = refNodeMap.get(node.getInternalID());
            refNodes.remove(node);
            nodes.remove(node);
        }else{
            node.removeConnections();
            nodes.remove(node);
            nodeMap.remove(node.getInternalID());

            ArrayList<JGraphNode> refNodes = refNodeMap.get(node.getInternalID());
            if ( refNodes != null ){
                for (JGraphNode refNode : refNodes) {
                    nodes.remove(refNode);
                    this.remove(refNode);
                }
                refNodes.clear();
            }
        }
         this.remove(node);
        //node.clear();
        repaint();
        nodeRemoved(node);
    }

    public void removeLink(Connector connector) {
        connector.disconnect();
        linkRemoved(connector);
    }

    public JGraphNode getInsertionNode() {
        return insertionNode;
    }

    public void setInsertionNode(JGraphNode insertionNode) {
        setCurrentState(GraphState.NODEINSERTION);
        this.insertionNode = insertionNode;
        insertionNode.setLocation(this.insertLocation);
        addNode(insertionNode);
    }

    public GraphState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GraphState currentState) {
        this.currentState = currentState;
    }

    public JGraphNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(JGraphNode selectedNode) {
        if (selectedNode != null) {
            nodeSelected(selectedNode);
        }

        this.selectedNode = selectedNode;
    }
    private ArrayList<GraphListener> listeners = new ArrayList<GraphListener>();

    public void addGraphListener(GraphListener gl) {
        listeners.add(gl);
    }

    public void removeGraphListener(GraphListener gl) {
        listeners.remove(gl);
    }

    private void nodeSelected(JGraphNode node) {
        for (GraphListener gl : listeners) {
            gl.nodeSelected(node);
        }

    }

    private void nodeAdded(JGraphNode node) {
        for (GraphListener gl : listeners) {
            gl.nodeAdded(node);
        }

    }

    private void nodeEntered(JGraphNode node) {
        for (GraphListener gl : listeners) {
            gl.nodeEntered(node);
        }
    }

    protected void nodeRemoved(JGraphNode node) {
        for (GraphListener gl : listeners) {
            gl.nodeRemoved(node);
        }

    }

    protected void linkRemoved(Connector connector) {
        for (GraphListener gl : listeners) {
            gl.linkRemoved(connector);
        }

        repaint();
    }

    /**
     * Called when a node has moved.
     * @param node the node that was moved.
     */
    protected void nodeMoved(JGraphNode node) {
        for (GraphListener gl : listeners) {
            gl.nodeMoved(node);
        }

    }

    protected void linkAdded(Connector connector) {
        for (GraphListener gl : listeners) {
            gl.linkAdded(connector);
        }

    }
    private JGraphStruct dragStruct;

    public void dragEnter(DropTargetDragEvent dtde) {
        System.out.println("drag enter");
    }

    /**
     * Called when the user drags a struct into a ShaderNode.
     * @param dtde
     */
    public void dragOver(DropTargetDragEvent dtde) {
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
        System.out.println(dtde.getTransferable());
    }

    public void dragExit(DropTargetEvent dte) {
    }

    public void drop(DropTargetDropEvent dtde) {
        try {
            String object = dtde.getTransferable().getTransferData(DataFlavor.stringFlavor).toString();
            int dotIndex = object.indexOf('.');
            String type = object.substring(0, dotIndex);
            String id = object.substring(dotIndex + 1);

            //System.out.println(dtde.getLocation());
            Component c = this.getComponentAt(dtde.getLocation());
            if (!(c instanceof JGraphNode)) {
                ShaderStage stage = project.findShaderStage(id);
                if (stage != null) {

                    JGraphNode node = new JGraphNode(this, stage);
                    addNode(node);
                    validate();

                    repaint();

                }


            } else {
                JGraphNode node = (JGraphNode) this.getComponentAt(dtde.getLocation());
                c = this.findComponentAt(dtde.getLocation());
                ShaderStruct ss = null;
                if (object.startsWith("struct")) {
                    while (c != this && c != null) {
                        String name = c.getName();
                        if ("input".equals(name)) {
                            ss = project.getStruct(id);
                            if (ss != null) {
                                node.setInputStruct(ss);
                            }

                        } else if ("output".equals(name)) {
                            ss = project.getStruct(id);
                            if (ss != null) {
                                node.setOutputStruct(ss);
                            }

                        }
                        c = c.getParent();
                    }

                }
                if (ss != null) {
                    validate();
                    repaint();

                }
            }
        } catch (UnsupportedFlavorException ex) {
            Logger.getLogger(GraphEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GraphEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
