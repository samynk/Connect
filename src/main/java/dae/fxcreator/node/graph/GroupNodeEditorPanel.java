package dae.fxcreator.node.graph;

import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.node.NodeGroup;
import dae.fxcreator.io.templates.NodeContainerProxy;
import dae.fxcreator.io.templates.NodeTemplate;
import dae.fxcreator.io.templates.NodeTemplateLibrary;
import dae.fxcreator.io.templates.TemplateGroup;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.NodeContainer;
import dae.fxcreator.node.ReferenceNode;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderNode;
import dae.fxcreator.node.ShaderOutput;
import dae.fxcreator.node.event.NodeEvent;
import dae.fxcreator.node.gui.ImageLoader;
import dae.fxcreator.node.gui.NodeStyle;
import dae.fxcreator.ui.FXCreator;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

/**
 *
 * @author Koen
 */
public class GroupNodeEditorPanel extends javax.swing.JPanel implements GraphListener, ActionListener, MouseListener, MouseMotionListener, ZoomListener {

    private FXProject project;
    private NodeTemplateLibrary library;
    /**
     * The NodeGroup object that contains all the nodes to show in the user
     * interface.
     */
    private NodeGroup nodeGroup;
    private FXCreator parent;
    private MethodDialog dialog;
    private JToolBar groupBar;
    private final JPopupMenu mnuCreateNode = new JPopupMenu();

    /**
     * Creates new form GroupNodeEditorPanel
     */
    public GroupNodeEditorPanel() {
        initComponents();
        graphEditor1.addGraphListener(this);
        this.setDoubleBuffered(false);
        ToolbarPanel.setLayout(new WrappingLayout(FlowLayout.LEFT));
        graphEditor1.addMouseListener(this);
        graphEditor1.addZoomListener(this);
        this.setOpaque(false);
    }

    public void setParent(FXCreator parent) {
        this.parent = parent;
    }

    /**
     * Updates the style of all the graph nodes.
     */
    public void updateStyles() {
        this.graphEditor1.updateStyles();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        ToolbarPanel = new javax.swing.JPanel();
        buttonToolbar = new javax.swing.JToolBar();
        btnDelete = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        pnlZoom = new javax.swing.JPanel();
        btn100 = new javax.swing.JButton();
        sliderZoom = new javax.swing.JSlider();
        btnZoom = new javax.swing.JButton();
        graphEditor1 = new dae.fxcreator.node.graph.GraphEditor();

        setPreferredSize(new java.awt.Dimension(640, 480));
        setLayout(new java.awt.BorderLayout());

        ToolbarPanel.setLayout(null);

        buttonToolbar.setFloatable(false);
        buttonToolbar.setRollover(true);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dae/images/destroyaction.png"))); // NOI18N
        btnDelete.setFocusable(false);
        btnDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        buttonToolbar.add(btnDelete);

        ToolbarPanel.add(buttonToolbar);
        buttonToolbar.setBounds(322, 0, 25, 25);

        add(ToolbarPanel, java.awt.BorderLayout.NORTH);

        statusPanel.setMinimumSize(new java.awt.Dimension(0, 20));
        statusPanel.setLayout(new java.awt.BorderLayout());

        pnlZoom.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.windowBorder));
        pnlZoom.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btn100.setText("100%");
        btn100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn100ActionPerformed(evt);
            }
        });
        pnlZoom.add(btn100);

        sliderZoom.setMajorTickSpacing(50);
        sliderZoom.setMaximum(800);
        sliderZoom.setMinimum(10);
        sliderZoom.setMinorTickSpacing(-1);
        sliderZoom.setSnapToTicks(true);
        sliderZoom.setValue(100);
        sliderZoom.setPreferredSize(new java.awt.Dimension(200, 24));
        sliderZoom.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderZoomStateChanged(evt);
            }
        });
        pnlZoom.add(sliderZoom);

        btnZoom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dae/images/zoom.png"))); // NOI18N
        btnZoom.setToolTipText("Zoom extents");
        btnZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomActionPerformed(evt);
            }
        });
        pnlZoom.add(btnZoom);

        statusPanel.add(pnlZoom, java.awt.BorderLayout.NORTH);

        add(statusPanel, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout graphEditor1Layout = new javax.swing.GroupLayout(graphEditor1);
        graphEditor1.setLayout(graphEditor1Layout);
        graphEditor1Layout.setHorizontalGroup(
            graphEditor1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        graphEditor1Layout.setVerticalGroup(
            graphEditor1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 262, Short.MAX_VALUE)
        );

        add(graphEditor1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if (graphEditor1.getSelectedNode() != null) {
            graphEditor1.removeSelectedNodes();
        } else if (graphEditor1.getSelectedConnector() != null) {
            graphEditor1.removeLink(graphEditor1.getSelectedConnector());
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void sliderZoomStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderZoomStateChanged
        // TODO add your handling code here:
        //graphEditor1.
        if (!ignoreChangeEvent) {
            graphEditor1.setScale(sliderZoom.getValue());
        }
    }//GEN-LAST:event_sliderZoomStateChanged

    private void btn100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn100ActionPerformed
        // TODO add your handling code here:
        graphEditor1.setScale(100);
        sliderZoom.setValue(100);
    }//GEN-LAST:event_btn100ActionPerformed

    private void btnZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomActionPerformed
        int minx = Integer.MAX_VALUE;
        int maxx = Integer.MIN_VALUE;
        int miny = Integer.MAX_VALUE;
        int maxy = Integer.MIN_VALUE;

        for (JGraphNode node : graphEditor1.getGraphNodes()) {
            if (node.getX() + node.getWidth() > maxx) {
                maxx = node.getX() + node.getWidth();
            }
            if (node.getX() < minx) {
                minx = node.getX();
            }
            if (node.getY() + node.getHeight() > maxy) {
                maxy = node.getY() + node.getHeight();
            }
            if (node.getY() < miny) {
                miny = node.getY();
            }

        }

        int gwidth = graphEditor1.getWidth();
        int gheight = graphEditor1.getHeight();

        float xscale = (1.0f * gwidth) / (maxx - minx);
        float yscale = (1.0f * gheight) / (maxy - miny);
        float scale = 1.0f;
        if (xscale * (maxy - miny) < gheight) {
            scale = xscale;
        } else {
            scale = yscale;
        }

        //System.out.println("Scale : " + scale);
        int transx = Math.round(minx * scale);
        int transy = Math.round(miny * scale);

        sliderZoom.setValue(Math.round(scale * 100.0f));
        graphEditor1.setTranslation(-transx, -transy);
    }//GEN-LAST:event_btnZoomActionPerformed

    public void setGroupNode(NodeGroup collection) {
        this.nodeGroup = collection;
        graphEditor1.clearNodes();
        for (IONode node : collection.getNodes()) {
            JGraphNode gn = new JGraphNode(graphEditor1, node);
            gn.setLocation(node.getPosition());
            graphEditor1.addNode(gn);
        }

        JGraphNode inputNode = new JGraphNode(graphEditor1, collection.getInputNode());
        JGraphNode outputNode = new JGraphNode(graphEditor1, collection.getOutputNode());
        graphEditor1.addNode(inputNode);
        graphEditor1.addNode(outputNode);
        graphEditor1.setFullRepaint();

        for (ReferenceNode rn : collection.getReferenceNodes()) {
            JGraphNode gn = new JGraphNode(graphEditor1, rn);
            NodeStyle style = FXSingleton.getSingleton().getFXSettings().getStyle("refnode");
            gn.updateStyle(style);
            graphEditor1.addNode(gn);
        }

        graphEditor1.paintImmediately(0, 0, getWidth(), getHeight());

        //graphEditor1.repaint();
        /*
         for (IONode node : collection.getNodes()) {
         connectLinks(node);
         }
         connectLinks(collection.getOutputNode());
         */
    }

    private void connectLinks(IONode node) {
        for (ShaderInput input : node.getInputs()) {
            String connectionString = input.getConnectionString();
            if (connectionString == null || connectionString.length() == 0) {
                continue;
            }
            int dotIndex = connectionString.indexOf('.');
            String nodeId = connectionString.substring(0, dotIndex);
            String outputId = connectionString.substring(dotIndex + 1);
            //System.out.println("Trying to find : " + nodeId );
            IONode found = this.nodeGroup.findNode(nodeId);
            JGraphNode outputNode = graphEditor1.findNode(found.getInternalID());
            if (outputNode == null) {
                continue;
            }
            /*
             ConnectorPoint outputCp = outputNode.getOutputConnectorPoint(outputId);

             JGraphNode inputNode = graphEditor1.findNode(node.getNodeId());
             ConnectorPoint inputCp = null;

             if (input.getSemantic().isValid()) {
             inputCp = inputNode.getInputConnectorPoint(input.getSemantic().getValue());
             } else {
             inputCp = inputNode.getInputConnectorPoint(input.getName());
             }
             if (outputCp != null && inputCp != null) {
             GraphNodeLink gnl = new GraphNodeLink(outputCp, inputCp);
             }
             */
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ToolbarPanel;
    private javax.swing.JButton btn100;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnZoom;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JToolBar buttonToolbar;
    private dae.fxcreator.node.graph.GraphEditor graphEditor1;
    private javax.swing.JPanel pnlZoom;
    private javax.swing.JSlider sliderZoom;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void nodeSelected(JGraphNode node) {
        FXSingleton.getSingleton().postEvent(new NodeEvent(node.getUserObject(),NodeEvent.Type.SELECTED));
    }

    @Override
    public void nodeAdded(JGraphNode node) {
    }

    @Override
    public void nodeRemoved(JGraphNode node) {
        if (node.isRefNode()) {
            ReferenceNode rn = node.getReferencedNode();
            nodeGroup.removeReferenceNode(rn);
        } else {
            IONode ionode = (IONode) node.getUserObject();
            nodeGroup.removeNode(ionode);
        }
    }

    @Override
    public void nodeMoved(JGraphNode node) {
        try {
            if (node.isRefNode() && node.getReferencedNode() != null) {
                ReferenceNode rn = node.getReferencedNode();
                rn.setPosition(node.getX(), node.getY());
            } else {
                IONode ionode = (IONode) node.getUserObject();
                ionode.setPosition(node.getX(), node.getY());
            }
        } catch (Exception ex) {
            System.out.println("getMessage:" + ex.getMessage());
        }
    }

    @Override
    public void linkSelected(GraphNodeLink link) {
    }

    protected void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            graphEditor1.setInsertPosition(e.getPoint());
            mnuCreateNode.show(e.getComponent(),
                    e.getX(), e.getY());
        }
    }

    /**
     * Sets the template library to use.
     *
     * @param library
     */
    public void setLibrary(NodeTemplateLibrary library) {
        this.library = library;
        Iterable<TemplateGroup> groups = library.getTemplateGroups("module");
        for (TemplateGroup group : groups) {
            //System.out.println("Creating toolbar for :" + group.getName());
            JToolBar toolbar = new JToolBar(group.getName());
            List<NodeTemplate> templates = group.getNodeTemplates();
            JMenu menu = new JMenu(group.getName());
            this.mnuCreateNode.add(menu);
            for (NodeTemplate template : templates) {
                String icon = template.getIcon();
                //System.out.println("Trying to find : " + icon);

                JButton button = null;
                try {
                    button = new JButton(new ImageIcon(ImageLoader.getInstance().getImage(icon)));
                } catch (Exception ex) {
                    button = new JButton(template.getTypeName());
                }
                button.setActionCommand(template.getType());
                toolbar.add(button);
                button.addActionListener(this);
                //System.out.println("After : " + icon);
                JMenuItem item = new JMenuItem(template.getTypeName());
                menu.add(item);
                item.setActionCommand(template.getType());
                item.addActionListener(this);
            }
            ToolbarPanel.add(toolbar);
            dialog = new MethodDialog(parent, library, true);
        }

        groupBar = new JToolBar("Group");
        JButton button = new JButton("New Group ...");
        groupBar.add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                createGroup();
            }
        });

        for (NodeContainerProxy proxy : library.getMethods()) {
            JButton method = new JButton(proxy.getLabel());
            method.setActionCommand(proxy.getType());
            groupBar.add(method);
            method.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    createGroup(ae.getActionCommand());
                }
            });
        }

        ToolbarPanel.add(groupBar);
        this.nodeGroup = new NodeContainer("test","driver");
    }

    private void createGroup(String command) {
        NodeContainer container = library.createNodeContainer(command);

        String id = this.nodeGroup.getUniqueId("group");
        container.setId(id);
        container.setName(id);
        container.setType("code.method");
        JGraphNode gn = new JGraphNode(graphEditor1, container);
        graphEditor1.setInsertionNode(gn);

        nodeGroup.addNode(container);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (nodeGroup != null) {
            String command = e.getActionCommand();
            NodeTemplate template = library.getNodeTemplate(command);

            ShaderNode node = template.createShaderNode(project);
            String newId = this.nodeGroup.getUniqueId(node.getId());
            node.setId(newId);
            nodeGroup.addNode(node);

            JGraphNode gn = new JGraphNode(graphEditor1, node);
            graphEditor1.setInsertionNode(gn);
        }
    }

    @Override
    public void linkAdded(Connector connector) {
    }

    @Override
    public void linkRemoved(Connector connector) {
    }

    @Override
    public void nodeEntered(JGraphNode node) {
        if (node.getUserObject() instanceof NodeGroup) {
            NodeGroup ng = (NodeGroup) node.getUserObject();
            this.setGroupNode(ng);
        }
    }

    private ShaderOutput copyOutput(IONode newParent, ShaderOutput output) {
        String semantic = "";
        if (output.getSemantic().isValid()) {
            semantic = output.getSemantic().toString();
        }
        ShaderOutput copy = new ShaderOutput(newParent, output.getName(), semantic, output.getType(), "");
        return copy;
    }

    private ShaderInput copyInput(IONode newParent, ShaderInput input) {
        String semantic = "";
        if (input.getSemantic().isValid()) {
            semantic = input.getSemantic().toString();
        }
        ShaderInput copy = new ShaderInput(newParent, input.getName(), semantic, input.getType());
        return copy;
    }

    private void copyConnection(NodeContainer nc, ShaderInput input) {
        if (input.getConnected()) {
            IONode node = input.getParent();

            IONode fromCloned = nc.findNode(node.getId());
            ShaderInput clonedInput = fromCloned.findInput(input.getName());
            ShaderOutput toOutput = input.getConnectedInput();

            IONode toNode = toOutput.getParent();
            IONode toCloned = nc.findNode(toNode.getId());
            ShaderOutput toOutputCloned = toCloned.findOutput(toOutput.getName());

            clonedInput.setConnectedInput(toOutputCloned);
        }
    }

    private void copyInputConnection(NodeContainer nc, ShaderInput input, String newName) {
        if (input.getConnected()) {
            IONode inputNode = nc.getInputNode();
            ShaderOutput output = inputNode.findOutput(newName);

            IONode toCloned = nc.findNode(input.getParent().getId());
            ShaderInput toClonedInput = toCloned.findInput(input.getName());

            toClonedInput.setConnectedInput(output);
        }
    }

    private void copyOutputConnection(NodeContainer nc, ShaderOutput output, String newName) {
        String outputName = output.getName();
        String outputId = output.getParent().getId();

        System.out.println("trying to find  :" + outputName + " from " + outputId);

        IONode outputNode = nc.getOutputNode();
        ShaderInput input = outputNode.findInput(newName);

        IONode clonedFromNode = nc.findNode(output.getParent().getId());
        ShaderOutput from = clonedFromNode.findOutput(output.getName());

        input.setConnectedInput(from);
    }

    /**
     * Create a new group from the currently selected nodes.
     */
    private void createGroup() {
        this.dialog.setVisible(true);
        if (dialog.getReturnStatus() == MethodDialog.RET_OK) {
            NodeContainerProxy proxy = dialog.getResult();
            List<IONode> nodes = graphEditor1.getSelectedNodes();
            // only create a group if the list of groups is bigger then one.
            if (nodes.size() < 2) {
                return;
            }

            String id = this.nodeGroup.getUniqueId(proxy.getPrefix());
            NodeContainer nc = new NodeContainer(proxy.getPrefix(), "code.method");
            nc.setSubType(proxy.getType());
            nc.setInputOutputEditable(true);
            // 1) find unconnected inputs ( unconnected + connected to node outside of the group)
            // 2) find unconnected outputs ( unconnected + connected to node outside of the group)
            for (IONode node : nodes) {
                if (node instanceof Cloneable) {
                    try {
                        IONode clone = (IONode) node.clone();
                        clone.setPosition(node.getPosition().x, node.getPosition().y);
                        nc.addNode(clone);
                    } catch (CloneNotSupportedException ex) {
                        continue;
                    }
                }
            }

            for (IONode node : nodes) {
                for (ShaderInput input : node.getInputs()) {
                    if (!input.getConnected()) {
                        ShaderInput copy = copyInput(nc, input);
                        String iname = input.getName();
                        copy.setName(node.getId() + "_" + iname);
                        nc.addInput(copy);
                        // not connected so name change is possible without problems.

                    } else {
                        ShaderOutput output = input.getConnectedInput();
                        IONode outputNode = output.getParent();
                        if (!nodes.contains(outputNode)) {
                            ShaderInput copy = copyInput(nc, input);
                            nc.addInput(copy);
                            copy.setName(node.getId() + "_" + copy.getName());
                            copyInputConnection(nc, input, copy.getName());
                        } else {
                            copyConnection(nc, input);
                        }

                    }
                }
                for (ShaderOutput output : node.getOutputs()) {
                    if (!output.getConnected()) {
                        ShaderOutput copy = copyOutput(nc, output);
                        nc.addOutput(copy);
                        copy.setName(node.getId() + "_" + copy.getName());
                    } else {
                        boolean connected = false;
                        for (IONode node2 : nodes) {
                            if (node2 == node) {
                                continue;
                            }
                            if (output.isOutputConnectedTo(node2)) {
                                connected = true;
                            }
                        }
                        if (!connected) {
                            ShaderOutput copy = copyOutput(nc, output);
                            nc.addOutput(copy);
                            copy.setName(node.getId() + "_" + copy.getName());
                            copyOutputConnection(nc, output, copy.getName());
                        }
                    }
                }
            }
            nodeGroup.addNode(nc);

            JGraphNode gn = new JGraphNode(graphEditor1, nc);
            graphEditor1.setInsertionNode(gn);

            try {
                this.library.saveMethod(nc, proxy);
                JButton method = new JButton(proxy.getLabel());
                method.setActionCommand(proxy.getType());
                groupBar.add(method);
                method.addActionListener((ActionEvent ae) -> {
                    createGroup(ae.getActionCommand());
                });
            } catch (IOException ex) {
                Logger.getLogger(GroupNodeEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    private boolean ignoreChangeEvent = false;

    public void zoomChanged(int zoomFactor) {
        ignoreChangeEvent = true;
        this.sliderZoom.setValue(zoomFactor);
        ignoreChangeEvent = false;
    }
    /**
     * The node that was the source for a copy or cut event.
     */
    private NodeGroup sourceCopy;

    public void copySelectionAsReference() {
        for (IONode node : graphEditor1.getSelectedNodes()) {
            Point position = node.getPosition();
            Point newPosition = new Point(position.x + 20, position.y + 20);
            ReferenceNode rn = new ReferenceNode(node, newPosition);
            JGraphNode refNode = new JGraphNode(graphEditor1, rn);
            NodeStyle style = FXSingleton.getSingleton().getFXSettings().getStyle("refnode");
            refNode.updateStyle(style);
            graphEditor1.addNode(refNode);
            this.nodeGroup.addReferenceNode(rn);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //System.out.println("Mouse dragged : " + e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //System.out.println("Mouse moved : " + e);
    }

    public void setProject(FXProject project) {
        this.project = project;
        setLibrary(project.getNodeTemplateLibrary());
        this.setGroupNode(project.getFirstStage());        
    }

    private enum CopyType {

        CUT, COPY
    };
    private CopyType copyType;
    private ArrayList<IONode> copyNodes = new ArrayList<>();

    /**
     * Deletes the selected nodes from the scene.
     */
    public void cut() {
        sourceCopy = this.nodeGroup;
        copyType = CopyType.CUT;

        copyNodes.clear();
        for (IONode node : graphEditor1.getSelectedNodes()) {
            copyNodes.add(node);
        }
        graphEditor1.removeSelectedNodes();
    }

    public void copy() {
        sourceCopy = this.nodeGroup;
        copyType = CopyType.COPY;

        copyNodes.clear();
        for (IONode node : graphEditor1.getSelectedNodes()) {
            copyNodes.add(node);
        }
    }

    public void paste() {
        ArrayList<IONode> toAdd = new ArrayList();
        // create new ids and connections.
        for (IONode node : copyNodes) {
            try {
                IONode clone = (IONode) node.clone();
                Point position = node.getPosition();
                clone.setPosition(position.x + 10, position.y + 10);

                NodeTemplate template = library.getNodeTemplate(clone.getType());
                String templateId = template.getShaderNode().getId();
                String newId = this.nodeGroup.getUniqueId(templateId);
                clone.setId(newId);
                nodeGroup.addNode(clone);
                toAdd.add(clone);

            } catch (CloneNotSupportedException ex) {
                ex.printStackTrace();
            }
        }
        // copy the connections
        if (toAdd.size() == copyNodes.size()) {
            for (int i = 0; i < toAdd.size(); ++i) {
                IONode toCloned = toAdd.get(i);
                IONode toOriginal = copyNodes.get(i);

                for (ShaderInput input : toOriginal.getInputs()) {
                    if (input.getConnected()) {
                        ShaderOutput output = input.getConnectedInput();
                        IONode fromOriginal = output.getParent();

                        int fromIndex = copyNodes.indexOf(fromOriginal);
                        if (fromIndex < 0) {
                            continue;
                        }
                        IONode fromCloned = toAdd.get(fromIndex);

                        ShaderInput clonedInput = toCloned.findInput(input.getName());
                        if (clonedInput == null) {
                            continue;
                        }

                        ShaderOutput clonedOutput = fromCloned.findOutput(output.getName());
                        if (clonedOutput == null) {
                            continue;
                        }

                        clonedInput.setConnection(clonedOutput);
                    }
                }
            }
        }
        for (int i = 0; i < toAdd.size(); ++i) {
        }
        for (IONode node : toAdd) {
            graphEditor1.addNode(new JGraphNode(graphEditor1, node));
        }
    }
}
