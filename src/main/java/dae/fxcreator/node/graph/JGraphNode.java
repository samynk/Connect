package dae.fxcreator.node.graph;

import dae.fxcreator.io.FXSettings;
import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.io.events.IOListener;
import dae.fxcreator.io.events.SettingListener;
import dae.fxcreator.io.templates.BooleanSetting;
import dae.fxcreator.io.templates.CodeSetting;
import dae.fxcreator.io.templates.ColorSetting;
import dae.fxcreator.io.templates.FloatSetting;
import dae.fxcreator.io.templates.IntSetting;
import dae.fxcreator.io.templates.MathSetting;
import dae.fxcreator.io.templates.OptionSetting;
import dae.fxcreator.io.templates.SemanticSetting;
import dae.fxcreator.io.templates.Setting;
import dae.fxcreator.io.templates.TextSetting;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.IOAnchor;
import dae.fxcreator.node.ReferenceNode;
import dae.fxcreator.node.ShaderIO;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderOutput;
import dae.fxcreator.node.ShaderStruct;
import dae.fxcreator.node.graph.uisetting.BooleanVisualizer;
import dae.fxcreator.node.graph.uisetting.CodeSettingVisualizer;
import dae.fxcreator.node.graph.uisetting.ColorVisualizer;
import dae.fxcreator.node.graph.uisetting.FloatVisualizer;
import dae.fxcreator.node.graph.uisetting.IntVisualizer;
import dae.fxcreator.node.graph.uisetting.MathVisualizer;
import dae.fxcreator.node.graph.uisetting.OptionVisualizer;
import dae.fxcreator.node.graph.uisetting.SemanticVisualizer;
import dae.fxcreator.node.graph.uisetting.TextSettingVisualizer;
import dae.fxcreator.node.graph.uisetting.Visualizer;
import dae.fxcreator.node.gui.GraphFont;
import dae.fxcreator.node.gui.GraphGradient;
import dae.fxcreator.node.gui.ImageLoader;
import dae.fxcreator.node.gui.NodeStyle;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * An alternative to the GraphNode class that uses a JPanel
 *
 * @author Koen
 */
public class JGraphNode extends JPanel implements IOListener, SettingListener {

    private GridBagLayout gbl = new GridBagLayout();
    private final JLabel title = new JLabel("Empty");
    private final VerticalPanel westPanel = new VerticalPanel(GridBagConstraints.WEST);
    private final VerticalPanel eastPanel = new VerticalPanel(GridBagConstraints.EAST);
    private final BoxPanel visualizationPanel = new BoxPanel(BoxLayout.Y_AXIS);
    private GeneralPath border;
    private GeneralPath selBorder;
    private GraphGradient normalGG;
    private GradientPaint backgroundGradient;
    int currentWidth = -1;
    int currentHeight = -1;
    private boolean selected;
    private IONode node;
    private final ArrayList<JConnectorPoint> children = new ArrayList<>();
    private GraphEditor parent;
    private final HashMap<String, JConnectorPoint> connectorMap = new HashMap<>();
    private float realX, realY;
    private NodeStyle currentStyle;
    private final BasicStroke stroke = new BasicStroke(2.0f);

    private boolean refNode = false;
    private ReferenceNode referenceNode;

    public JGraphNode() {

        setLayout(gbl);
        border = new GeneralPath();
        selBorder = new GeneralPath();
        title.setText("Node style example");
        title.setOpaque(false);
        title.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        createPanels();

        updateStyle();
        validate();
        repaint();
    }

    /**
     * Creates a new JGraphNode object.
     *
     * @param parent the parent graph editor component.
     * @param node the IONode user object.
     */
    public JGraphNode(GraphEditor parent, IONode node) {
        this(parent, node, false);

        //adjustSize();
    }

    /**
     * Adds the possibility to create this JGraphNode as a ref node. The ref
     * node has the following properties : 1) A ref node only shows the outputs
     * of the node. 2) A ref node does not show the settings of the node. 3) the
     * visual style of a ref node is different.
     *
     * Connections will be laid out automatically.
     *
     * @param parent the GraphEditor this
     * @param node
     * @param isRefNode
     */
    public JGraphNode(GraphEditor parent, IONode node, boolean isRefNode) {
        this.parent = parent;
        this.node = node;
        refNode = isRefNode;

        setLayout(gbl);

        addUI(node);

        GraphFont gf = FXSingleton.getSingleton().getFXSettings().getFont("title");

        title.setText(node.getName());
        if (node.getIcon() != null) {
            String icon = node.getIcon();
            Image image = ImageLoader.getInstance().getImage(icon);
            title.setIcon(new ImageIcon(image));

        }
        title.setHorizontalAlignment(JLabel.LEFT);
        title.setFont(gf.getFont());
        title.setForeground(gf.getColor());
        title.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        //title.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.yellow));

        westPanel.setOpaque(false);
        westPanel.setName("input");

        eastPanel.setOpaque(false);
        eastPanel.setName("output");

        visualizationPanel.setOpaque(false);
        visualizationPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topPanel.setOpaque(false);
        if (isRefNode) {
            Image refImage = ImageLoader.getInstance().getImage("/dae/images/reference.png");
            JLabel refLabel = new JLabel(new ImageIcon(refImage));
            topPanel.add(refLabel);
        }
        topPanel.add(title);
        add(topPanel, gbc);

        if (!isRefNode) {
            if (node.isInputStructSet()) {
                ShaderStruct input = node.getInputStruct();
                JGraphStruct structPanel = new JGraphStruct(node);
                structPanel.setModel(input);
                westPanel.addNorthComponent(structPanel);
            } else {
                for (ShaderInput input : node.getInputs()) {
                    addIOComponent(node, input);
                }
            }
        }

        if (node.isOutputStructSet()) {
            ShaderStruct output = node.getOutputStruct();
            JGraphStruct structPanel = new JGraphStruct(node);
            structPanel.setModel(output);
            eastPanel.addNorthComponent(structPanel);
        } else {
            for (ShaderOutput output : node.getOutputs()) {
                addIOComponent(node, output);
            }
        }

        gbc.weightx = 0.5;
        gbc.weighty = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        if (!isRefNode) {
            add(westPanel, gbc);
        }
        gbc.gridx = 1;
        add(eastPanel, gbc);

        if (!isRefNode) {
            createSettingVisuals(node);
            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = GridBagConstraints.RELATIVE;
            gbc.weighty = 0;
            add(visualizationPanel, gbc);
        }

        setLocation(node.getPosition());
        setSize(this.getPreferredSize());

        border = new GeneralPath();
        selBorder = new GeneralPath();

        node.addIOListener(this);
        node.addSettingListener(this);

        updateStyle();
    }

    private JConnectorPoint addIOComponent(IONode node, ShaderIO io) {
        JConnectorPoint jcp = null;
        IOAnchor anchor;
        if (io.hasAnchor()) {
            anchor = io.getAnchor();
        } else {
            if (io.isInput()) {
                anchor = node.getInputAnchor();
            } else {
                anchor = node.getOutputAnchor();
            }
        }
        switch (anchor) {
            case NORTHEAST:
                jcp = new JConnectorPoint(this, io, JConnectorPoint.POSITION.RIGHT);
                eastPanel.addNorthComponent(jcp);
                break;
            case NORTHWEST:
                jcp = new JConnectorPoint(this, io, JConnectorPoint.POSITION.LEFT);
                westPanel.addNorthComponent(jcp);
                break;
            case SOUTHEAST:
                jcp = new JConnectorPoint(this, io, JConnectorPoint.POSITION.RIGHT);
                eastPanel.addSouthComponent(jcp);
                break;
            case SOUTHWEST:
                jcp = new JConnectorPoint(this, io, JConnectorPoint.POSITION.LEFT);
                westPanel.addSouthComponent(jcp);
                break;
        }
        if (jcp != null) {
            children.add(jcp);
            connectorMap.put(io.getName(), jcp);
        }
        return jcp;
    }

    /**
     * Create the panels are necessary.
     */
    private void createPanels() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);

        add(title, gbc);

        westPanel.setOpaque(false);
        westPanel.setName("input");

        eastPanel.setOpaque(false);
        eastPanel.setName("output");

        gbc.weightx = 0.5;
        gbc.weighty = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;

        add(westPanel, gbc);
        gbc.gridx = 1;
        add(eastPanel, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0;

        add(visualizationPanel, gbc);
    }

    /**
     * Creates a JGraphNode object that is a reference to another object.
     *
     * @param parent the parent graph editor object.
     * @param node the referenced node.
     */
    public JGraphNode(GraphEditor parent, ReferenceNode node) {
        this(parent, node.getReferencedNode(), true);
        this.referenceNode = node;
        setLocation(node.getPosition());
    }

    /**
     * Checks if this is a reference node.
     *
     * @return true if this is a reference node, false otherwise.
     */
    public boolean isRefNode() {
        return refNode;
    }

    public final void updateStyle() {
        FXSingleton singleton = FXSingleton.getSingleton();
        FXSettings settings = singleton.getFXSettings();
        if (currentStyle == null) {
            if (node.hasSetting("UI", "style")) {
                String style = node.getSettingValue("UI", "style");
                currentStyle = settings.getStyle(style);
                if (currentStyle == null) {
                    currentStyle = settings.getStyle("default");
                }
                this.updateStyle(currentStyle);
            } else {
                currentStyle = settings.getStyle("default");
                this.updateStyle(currentStyle);
            }
        } else {
            this.updateStyle(currentStyle);
        }
        this.setBorder(new RoundBorder(normalGG.getC2()));
        adjustSize();
    }

    private void addUI(IONode node) {
        if (node.hasSetting("UI", "style")) {
            String style = node.getSettingValue("UI", "style");
            FXSettings settings = FXSingleton.getSingleton().getFXSettings();
            NodeStyle nstyle = settings.getStyle(style);
            if (nstyle == null) {
                nstyle = settings.getStyle("default");
            }
            this.updateStyle(nstyle);
        }
    }

    public int getInternalID() {
        return this.node.getInternalID();
    }

    @Override
    public void paintComponent(Graphics g) {
        drawBackground((Graphics2D) g);
    }

    @Override
    public void paintChildren(Graphics g) {
        super.paintChildren(g);
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(normalGG.getC2());
        int y = title.getY() + title.getHeight() + 2;
        g2d.setStroke(stroke);
        g.drawLine(0, y, this.getWidth() - 1, y);

    }

    /**
     * Returns the model of this node.
     *
     * @return the model of this node.
     */
    public IONode getUserObject() {
        return node;
    }

    private void createSettingVisuals(IONode node) {
        for (Setting s : node.getVisualizedSettings()) {
            if (s instanceof ColorSetting) {
                ColorVisualizer cv = new ColorVisualizer();
                cv.setSetting((ColorSetting) s);
                visualizationPanel.add(cv);
            } else if (s instanceof FloatSetting) {
                FloatVisualizer fv = new FloatVisualizer();
                fv.setSetting((FloatSetting) s);
                visualizationPanel.add(fv);
            } else if (s instanceof IntSetting) {
                IntVisualizer iv = new IntVisualizer();
                iv.setSetting((IntSetting) s);
                visualizationPanel.add(iv);
            } else if (s instanceof OptionSetting) {
                OptionVisualizer ov = new OptionVisualizer();
                ov.setSetting((OptionSetting) s);
                visualizationPanel.add(ov);
            } else if (s instanceof BooleanSetting) {
                BooleanVisualizer bv = new BooleanVisualizer();
                bv.setSetting((BooleanSetting) s);
                visualizationPanel.add(bv);
            } else if (s instanceof SemanticSetting) {
                SemanticVisualizer sv = new SemanticVisualizer();
                sv.setSetting((SemanticSetting) s);
                visualizationPanel.add(sv);
            } else if (s instanceof TextSetting) {
                TextSettingVisualizer tsv = new TextSettingVisualizer();
                tsv.setSetting((TextSetting) s);
                visualizationPanel.add(tsv);
            } else if (s instanceof CodeSetting) {
                CodeSettingVisualizer csv = new CodeSettingVisualizer();
                csv.setSetting((CodeSetting) s);
                visualizationPanel.add(csv);
            } else if (s instanceof MathSetting) {
                MathVisualizer mv = new MathVisualizer();
                mv.setSetting(s);
                visualizationPanel.add(mv);
            }
        }
    }

    /**
     * Draws the background of this panel.
     */
    private void drawBackground(Graphics2D g) {
        int radius = 5;
        border.reset();
        border.moveTo(radius, 0);
        border.lineTo(getWidth() - radius - 1, 0);
        border.quadTo(getWidth() - 1, 0, getWidth() - 1, radius);
        border.lineTo(getWidth() - 1, getHeight() - radius);
        border.quadTo(getWidth() - 1, getHeight(), getWidth() - radius, getHeight());
        border.lineTo(radius, getHeight());
        border.quadTo(0, getHeight(), 0, getHeight() - radius);
        border.lineTo(0, radius);
        border.quadTo(0, 0, radius, 0);
        border.closePath();
        selBorder.reset();
        selBorder.moveTo(-2, -2);
        selBorder.lineTo(getWidth() + 2, -2);
        selBorder.lineTo(getWidth() + 2, getHeight() + 2);
        selBorder.lineTo(-2, getHeight() + 2);
        selBorder.closePath();
        //g.setPaint(defaultFill);

        if (backgroundGradient == null || (this.getWidth() != currentWidth && this.getHeight() != currentHeight)) {
            backgroundGradient = new GradientPaint(0, this.title.getY() + title.getHeight() + 2, normalGG.getC1(), 0, getHeight(), normalGG.getC2());
            currentWidth = this.getWidth();
            currentHeight = this.getHeight();
        }
        g.setPaint(backgroundGradient);
        g.fill(border);

    }

    /**
     * Sets the selection status of this JGraphNode.
     *
     * @param selected true if this JGraphNode is selected , false otherwise.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Checks if this JGraphNode is selected or not.
     *
     * @return true if the JGraphNode is selected, false otherwise.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Returns the id of this node.
     *
     * @return the id of this node.
     */
    public String getNodeId() {
        return this.node.getId();
    }

    /**
     * Moves this component with the specified amount.
     *
     * @param dx the x amount to move this component.
     * @param dy the y amount to move this component.
     */
    void addLocation(float dx, float dy) {
        realX += dx;
        realY += dy;
        super.setLocation(Math.round(realX), Math.round(realY));
    }

    @Override
    public void setLocation(int x, int y) {
        realX = x;
        realY = y;
        super.setLocation(x, y);
    }

    @Override
    public void setLocation(Point p) {
        realX = p.x;
        realY = p.y;
        super.setLocation(p);
    }

    /**
     * Draw the connectors.
     *
     * @param g2d the Graphics2D object.
     */
    public void drawConnectors(GraphEditor editor, Graphics2D g2d) {
        for (JConnectorPoint jcp : children) {
            jcp.drawConnector(editor, g2d);
        }
    }

    /**
     * Draw the attachments for the connectors.
     *
     * @param editor the parent panel object.
     * @param g2d the graphics context.
     */
    public void drawConnectorAttachment(GraphEditor editor, Graphics2D g2d) {
        for (JConnectorPoint jcp : children) {
            jcp.drawConnectorAttachment(editor, g2d);
        }
    }

    /**
     * Find the location for the connector of the ShaderOutput object.
     *
     * @param output the ShaderOutput object.
     * @return the location of the JConnectorPoint object.
     */
    public Point getLocation(ShaderOutput output) {
        IONode ionode = output.getParent();
        //JGraphNode jnode = parent.findNode(ionode.getInternalID());
        JGraphNode jnode = parent.findClosestNode(this, ionode.getInternalID());
        if (jnode == null) {
            //System.out.println("could not find the JGraphNode for : " + ionode.getId());
            return new Point(0, 0);
        }
        JConnectorPoint jcp = jnode.getConnectorPoint(output);
        if (jcp == null) {
            //System.out.println("output not found :" + jnode.getNodeId() + "," + output.getName());
            return new Point(0, 0);
        } else {
            return jcp.getConnectorLocation();
        }
    }

    /**
     * Returns a connector point for the ShaderOutput object.
     *
     * @param output the ShaderOutput object.
     * @return the JConnectorPoint that represents the output object.
     */
    private JConnectorPoint getConnectorPoint(ShaderOutput output) {
        return connectorMap.get(output.getName());
    }

    /**
     * Removes all the input/output connectors from the user object.
     */
    void removeConnections() {
        getUserObject().removeConnections();
    }

    /**
     * Updates the input or output of this JGraphNode.
     *
     * @param oldName the old name of the input or output, in case the name was
     * changed.
     * @param name the new name of the input/output.
     */
    public void updateIO(String oldName, String name) {
        JConnectorPoint jcp = connectorMap.get(oldName);
        if (jcp != null) {
            jcp.syncWithModel();
            if (!name.equals(oldName)) {
                connectorMap.remove(oldName);
                connectorMap.put(name, jcp);
            }
            adjustSize();
        }
    }

    /**
     * Removes the input or output from this panel.
     *
     * @param name the name of the input or output.
     */
    public void removeIO(String name) {
        JConnectorPoint jcp = connectorMap.get(name);
        this.westPanel.remove(jcp);
        this.eastPanel.remove(jcp);
        connectorMap.remove(name);
        children.remove(jcp);
        adjustSize();
    }

    /**
     * Adds an input to this JGraphNode.
     *
     * @param name the name of the input to add.
     */
    public void addInput(String name) {
        ShaderInput input = this.node.findInput(name);
        if (input != null) {
            JConnectorPoint jcp = addIOComponent(node, input);
            jcp.updateStyle(currentStyle);

            adjustSize();
        }
    }

    /**
     * Adds an input to this JGraphNode.
     *
     * @param name the name of the input to add.
     */
    public void addOutput(String name) {
        ShaderOutput output = this.node.findOutput(name);
        if (output != null) {
            JConnectorPoint jcp = addIOComponent(node, output);
            jcp.updateStyle(currentStyle);
            adjustSize();

        }
    }

    /**
     * Sets the output struct for this GraphNode. This implies that all the
     * output nodes will be removed and replaced with the fields in the shader
     * struct.
     *
     * @param struct the new output struct for this graphnode.
     */
    public void setOutputStruct(ShaderStruct struct) {
        JGraphStruct structPanel = new JGraphStruct(node);
        structPanel.setModel(struct);
        for (Component c : eastPanel.getComponents()) {
            if (c instanceof JConnectorPoint) {
                JConnectorPoint jcp = (JConnectorPoint) c;
                this.children.remove(jcp);
                this.connectorMap.remove(jcp.getUserObject().getName());
            }
        }
        eastPanel.removeAll();
        eastPanel.addNorthComponent(structPanel);
        node.setOutputStruct(struct);
        adjustSize();

    }

    public void setInputStruct(ShaderStruct struct) {
        JGraphStruct structPanel = new JGraphStruct(node);
        structPanel.setModel(struct);
        for (Component c : westPanel.getComponents()) {
            if (c instanceof JConnectorPoint) {
                JConnectorPoint jcp = (JConnectorPoint) c;
                this.children.remove(jcp);
                this.connectorMap.remove(jcp.getUserObject().getName());
            }
        }
        westPanel.removeAll();
        westPanel.addNorthComponent(structPanel);
        node.setInputStruct(struct);
        adjustSize();
    }

    /**
     * Updates the title label of the node
     */
    public void updateTitle() {
        title.setText(node.getName());
        adjustSize();
    }

    @Override
    public void ioChanged(String oldName, String newName) {
        this.updateIO(oldName, newName);
    }

    @Override
    public void ioRemoved(String name) {
        this.removeIO(name);
    }

    @Override
    public void ioAdded(String name) {
        ShaderIO io = node.getPort(name);
        if (io.isInput()) {
            this.addInput(name);
        } else {
            this.addOutput(name);
        }
    }

    @Override
    public void removeNotify() {
        if (this.node != null) {
            node.removeIOListener(this);
            node.removeSettingListener(this);
        }
    }

    public boolean isRemovable() {
        return node.isRemovable();
    }

    public void adjustSize() {
        this.invalidate();

        this.visualizationPanel.invalidate();
        this.validate();
        //this.setSize(this.getPreferredSize());
        this.doLayout();
        westPanel.doLayout();
        eastPanel.doLayout();
        visualizationPanel.doLayout();
        backgroundGradient = null;
        Dimension d = this.getPreferredSize();
        d.width += 5;
        d.height += 10;
        this.setSize(d);
        this.repaint();
        if (parent != null) {
            this.parent.repaint();
        }
    }

    @Override
    public void settingChanged(IONode node, Setting s) {
        if (s.getGroup().equals("UI") && s.getId().equals("style")) {
            String style = s.getSettingValue();
            NodeStyle ns = FXSingleton.getSingleton().getFXSettings().getStyle(style);
            if (ns != null) {
                this.updateStyle(ns);
            }
        }
        for (Component c : this.visualizationPanel.getComponents()) {
            if (c instanceof Visualizer) {
                Visualizer v = (Visualizer) c;
                v.updateVisual(s);
            }
        }
        this.title.setText(node.getName());
        visualizationPanel.invalidate();
        adjustSize();
    }

    /**
     * Add a bit of extra size to solve scaling problems with fonts.
     *
     * @return the preferred size for the node.
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.width += 10;
        return d;
    }

    public void updateStyle(NodeStyle style) {
        if (style == null) {
            return;
        }
        currentStyle = style;
        FXSingleton singleton = FXSingleton.getSingleton();
        FXSettings settings = singleton.getFXSettings();
        GraphFont font = settings.getFont(style.getTitleFontName());
        title.setFont(font.getFont());
        title.setForeground(font.getColor());

        this.normalGG = settings.getGradient(style.getGradientName());
        backgroundGradient = new GradientPaint(0, 0, normalGG.getC1(), 0, getHeight(), normalGG.getC2());

        for (Component c : westPanel.getComponents()) {
            if (c instanceof JConnectorPoint) {
                JConnectorPoint jcp = (JConnectorPoint) c;
                jcp.updateStyle(style);
            }
        }

        for (Component c : eastPanel.getComponents()) {
            if (c instanceof JConnectorPoint) {
                JConnectorPoint jcp = (JConnectorPoint) c;
                jcp.updateStyle(style);
            }
        }
        //System.out.println("normalGG : "+ normalGG);
        this.repaint();
    }

    public static void main(String args[]) throws Exception {

        UIDefaults defaults = UIManager.getDefaults();
        Enumeration newKeys = defaults.keys();

        FlowLayout fl;

        while (newKeys.hasMoreElements()) {
            Object obj = newKeys.nextElement();
            System.out.printf("%50s : %s\n", obj, UIManager.get(obj));
        }
    }

    public void highlightPossibleEndTerminals(JTerminal startTerminal) {
        ShaderIO start = startTerminal.getConnectorPoint().getUserObject();
        for (Component c : westPanel.getComponents()) {
            if (c instanceof JConnectorPoint) {
                JConnectorPoint jcp = (JConnectorPoint) c;
                ShaderIO end = jcp.getUserObject();
                if (!end.getConnected() && end.accepts(start)) {
                    jcp.highlight();
                }
            }
        }
    }

    void resetPossibleEndTerminals() {
        for (Component c : westPanel.getComponents()) {
            if (c instanceof JConnectorPoint) {
                JConnectorPoint jcp = (JConnectorPoint) c;
                jcp.unhighlight();
            }
        }
    }

    /**
     * Return the referenced node.
     *
     * @return the ReferencedNode object.
     */
    public ReferenceNode getReferencedNode() {
        return referenceNode;
    }
}

class RoundBorder implements Border {

    private final Color color;
    private final GeneralPath border = new GeneralPath();
    private final BasicStroke borderStroke = new BasicStroke(2.0f);

    public RoundBorder(Color color) {
        this.color = color;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        int radius = 6;
        border.reset();
        border.moveTo(radius, 0);
        border.lineTo(width - radius - 1, 0);
        border.quadTo(width - 1, 0, width - 1, radius);
        border.lineTo(width - 1, height - radius);
        border.quadTo(width - 1, height, width - radius, height);
        border.lineTo(radius, height);
        border.quadTo(0, height, 0, height - radius);
        border.lineTo(0, radius);
        border.quadTo(0, 0, radius, 0);
        border.closePath();

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(x, y);
        g2d.setPaint(color);
        g2d.setStroke(borderStroke);
        g2d.draw(border);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(2, 2, 2, 2);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}
