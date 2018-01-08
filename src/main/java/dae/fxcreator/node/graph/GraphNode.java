package dae.fxcreator.node.graph;

import dae.fxcreator.io.FXProjectType;
import dae.fxcreator.io.FXSettings;
import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderOutput;
import dae.fxcreator.node.graph.ConnectorPoint.POSITION;
import dae.fxcreator.node.gui.ControlPoint;
import dae.fxcreator.node.gui.GraphFont;
import dae.fxcreator.node.gui.ImageLoader;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.util.*;

/**
 *
 * @author samynk
 */
public class GraphNode {

    private int x;
    private int y;
    private int width = 40;
    private int height = 40;
    private GeneralPath border;
    private GeneralPath selBorder;
    private Color defaultFill = new Color(23, 23, 129, 128);
    private Color foreGround = new Color(123, 123, 255, 128);
    private GradientPaint gp;
    private static Font defaultFont = new Font("Verdana", Font.PLAIN, 12);
    private Font font;
    private static float[] dashpattern = {4, 4, 4, 4};
    private static BasicStroke selStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashpattern, 0);
    private static BasicStroke defaultStroke = new BasicStroke(1.0f);
    private boolean selected;
    private boolean mouseOver;
    private String label;
    private int xLabelStart = 5;
    private int yLabelStart;
    private int yConnectorStart = 10;
    private ControlPoint cps[] = new ControlPoint[4];
    // the arraylist with input and output connector points.
    private ArrayList<ConnectorPoint> connectPoints = new ArrayList<ConnectorPoint>();
    // the hashmap with the input connector points/
    private HashMap<String, ConnectorPoint> inputMap = new HashMap<String, ConnectorPoint>();
    // the hashmap with the output connector points.
    private HashMap<String, ConnectorPoint> outputMap = new HashMap<String, ConnectorPoint>();
    private Object userObject;
    private String id;

    // fonts
    private GraphFont titleFont;
   

    /** Creates a new instance of Node */
    public GraphNode(String label) {
        setLabel(label);
        FXSettings settings = FXSingleton.getSingleton().getFXSettings();
        titleFont = settings.getFont("title");
        init();
    }

    /**
     * Creates a new GraphNode object with a ShaderNode as user object.
     * @param node The shader node to visualize.
     */
    public GraphNode(IONode node) {
        this.label = node.getName();
        if (label == null) {
            this.label = "<set label>";
        }

        this.setX(node.getPosition().x);
        this.setY(node.getPosition().y);

        FXSettings settings = FXSingleton.getSingleton().getFXSettings();
        titleFont = settings.getFont("title");
        
        FXProjectType current = node.getProjectType();

        ArrayList<ShaderOutput> outputs = node.getOutputs();
        for (ShaderOutput output : outputs) {
            Image image = ImageLoader.getInstance().getImage(output.getType().getIcon());
            ConnectorPoint cp = new ConnectorPoint(this, image);
            cp.setExpectedType(output.getType());
            cp.setAllowedConnections(Integer.MAX_VALUE);
            cp.setType(ConnectorPoint.TYPE.SOURCE);
            cp.setLabel(output.getLabel());
            cp.setProperty1(output.getName());
            cp.setProperty2(output.getSemantic().getValue());
            addConnectorPoint( cp);
        }

        ArrayList<ShaderInput> inputs = node.getInputs();
        for (ShaderInput input : inputs) {
            Image image = ImageLoader.getInstance().getImage(input.getType().getIcon());
            ConnectorPoint cp = new ConnectorPoint(this, image);
            cp.setExpectedType(input.getType());
            cp.setAllowedConnections(1);
            cp.setType(ConnectorPoint.TYPE.DESTINATION);
            cp.setPosition(POSITION.LEFT);
            cp.setLabel(input.getLabel());
            cp.setProperty1(input.getName());
            cp.setProperty2(input.getSemantic().getValue());
            addConnectorPoint( cp);
        }
        setUserObject(node);
        setId(node.getId());
        init();
    }

    private void addConnectorPoint(ConnectorPoint cp) {
        if ( !connectPoints.contains(cp))
            connectPoints.add(cp);
        String key1 = cp.getProperty1();
        String key2 = cp.getProperty2();
        if (cp.getType() == ConnectorPoint.TYPE.SOURCE) {
            outputMap.put(key1, cp);
            if (key2 != null) {
                outputMap.put(key2, cp);
            }
        } else if (cp.getType() == ConnectorPoint.TYPE.DESTINATION) {
            inputMap.put(key1, cp);
            if (key2 != null) {
                inputMap.put(key2, cp);
            }
        } else if (cp.getType() == ConnectorPoint.TYPE.BOTH) {
            inputMap.put(key1, cp);
            outputMap.put(key1, cp);
            if (key2 != null) {
                outputMap.put(key2, cp);
                inputMap.put(key2, cp);
            }
        }
    }

    /**
     * Returns the unique id for this node.
     * @return the id for this node.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id for this node.
     * @param id the new id for this node
     */
    public void setId(String id) {
        this.id = id;
    }

    private void init() {
        border = new GeneralPath();
        selBorder = new GeneralPath();
        font = defaultFont;
        cps[0] = new ControlPoint("TopLeft", new TopLeftControl(this));
        cps[1] = new ControlPoint("TopRight", new TopLeftControl(this));
        cps[2] = new ControlPoint("BottomRight", new TopLeftControl(this));
        cps[3] = new ControlPoint("BottomLeft", new TopLeftControl(this));
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void calculateSize(FontRenderContext frc) {
        setLabel(((IONode) userObject).getName());
        int labelHeight = 5;
        if (label != null) {
            //Rectangle2D d = titleFont.getSize(label, frc);
            Rectangle2D d = new Rectangle2D.Float(0,0,0,0);
            setWidth((int)d.getWidth()+ xLabelStart);

            labelHeight += (int)d.getHeight();
        } else {
            setWidth(50);
            labelHeight += 20;
        }

        int leftHeight = 0;
        int leftWidth = 0;

        int rightHeight = 0;
        int rightWidth = 0;

        int bottomWidth = 0;
        int bottomHeight = 0;
        for (ConnectorPoint cp : connectPoints) {
            if (cp.isPartOfLayout()) {
                cp.calculateSize(frc);
                if (cp.getPosition() == ConnectorPoint.POSITION.RIGHT) {
                    rightHeight += cp.getHeight() + 5;
                    rightWidth = cp.getWidth() > rightWidth ? cp.getWidth() : rightWidth;
                } else if (cp.getPosition() == ConnectorPoint.POSITION.LEFT) {
                    leftHeight += cp.getHeight() + 5;
                    leftWidth = cp.getWidth() > leftWidth ? cp.getWidth() : leftWidth;
                } else if (cp.getPosition() == ConnectorPoint.POSITION.BOTTOM) {
                    bottomWidth += cp.getWidth() + 5;
                    bottomHeight = cp.getHeight() > bottomHeight ? cp.getHeight() : bottomHeight;
                } else if (cp.getPosition() == ConnectorPoint.POSITION.OBJECT) {
                    setWidth(getWidth() + (cp.getWidth() + 4));
                    labelHeight = cp.getHeight() > labelHeight ? cp.getHeight() : labelHeight;
                    xLabelStart = cp.getWidth() + 4;
                }
            }
        }
        setHeight(rightHeight > leftHeight ? rightHeight : leftHeight);
        setHeight(getHeight() + (labelHeight + bottomHeight));

        if (getWidth() < bottomWidth) {
            setWidth(bottomWidth);
        }

        if (getWidth() < leftWidth + rightWidth) {
            setWidth(leftWidth + rightWidth);
        }
        calculateConnectorLocations(labelHeight + yConnectorStart);
    }

    private void calculateConnectorLocations(int currentHeight) {
        int rightHeight = currentHeight, leftHeight = currentHeight;
        for (ConnectorPoint cp : this.connectPoints) {
            if (cp.isPartOfLayout()) {
                if (cp.getPosition() == ConnectorPoint.POSITION.RIGHT) {
                    cp.setLocation(getWidth() - cp.getImageWidth() / 2 - 4, rightHeight + cp.getImageHeight() / 2 + 2);
                    rightHeight += cp.getHeight() + 2;
                } else if (cp.getPosition() == ConnectorPoint.POSITION.LEFT) {
                    cp.setLocation(4, leftHeight + cp.getImageHeight() / 2 + 2);
                    leftHeight += cp.getHeight() + 2;
                } else if (cp.getPosition() == ConnectorPoint.POSITION.BOTTOM) {
                    cp.setLocation(cp.getImageWidth() / 2 + 4, getHeight() - cp.getImageHeight() / 2 - 2);
                } else if (cp.getPosition() == ConnectorPoint.POSITION.OBJECT) {
                    cp.setLocation(cp.getImageWidth() / 2 + 2, cp.getImageHeight() / 2 + 2);
                }
            }
        }
    }

    public void draw(Graphics2D g) {
        if (getUserObject() != null) {
            this.label = getUserObject().toString();
        }
        g.setFont(titleFont.getFont());
        
        int radius = 5;
        border.reset();
        border.moveTo(radius, 0);
        border.lineTo(getWidth() - radius, 0);
        border.quadTo(getWidth(), 0, getWidth(), radius);
        border.lineTo(getWidth(), getHeight() - radius);
        border.quadTo(getWidth(), getHeight(), getWidth() - radius, getHeight());
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
        gp = new GradientPaint(0, 0, foreGround, 0, getHeight(), defaultFill);
        g.setPaint(gp);
        g.fill(border);
        g.setStroke(defaultStroke);

        if (isSelected()) {
            g.setStroke(selStroke);
            g.setColor(Color.blue);
            g.draw(selBorder);
        }
        g.setStroke(defaultStroke);
        g.setColor(Color.BLACK);
        g.draw(border);
        if (label != null) {
            g.setColor(Color.white);

            g.drawString(label, xLabelStart, g.getFontMetrics().getMaxAscent() + 2);
        }
        drawNode(g);
        if (isSelected()) {
            drawControlPoints(g);
        }
        adjustConnectorPoints();
        drawConnectorLinks(g);
        if (isMouseOver()) {
            drawConnectorPoints(g, false);
        } else {
            drawConnectorPoints(g, true);
        }
    }

    public void drawNode(Graphics2D g) {
    }

    private void drawControlPoints(Graphics2D g) {
        cps[0].setLocation(0, 0);
        cps[1].setLocation(getWidth(), 0);
        cps[2].setLocation(getWidth(), getHeight());
        cps[3].setLocation(0, getHeight());
        for (ControlPoint cp : cps) {
            cp.draw(g);
        }
    }

    private void adjustConnectorPoints() {
        /*
        ConnectorPoint cptl = inputMap.get("TOPLEFT");
        if (cptl != null) {
        cptl.setLocation(getWidth() / 2, 0);
        }
        ConnectorPoint cptr = inputMap.get("TOPRIGHT");
        if (cptr != null) {
        cptr.setLocation(getWidth(), getHeight() / 2);
        }
        ConnectorPoint cpbr = inputMap.get("BOTTOMRIGHT");
        if (cpbr != null) {
        cpbr.setLocation(getWidth() / 2, getHeight());
        }
        ConnectorPoint cpbl = connectPoints.get("BOTTOMLEFT");
        if (cpbl != null) {
        connectPoints.get("BOTTOMLEFT").setLocation(0, getHeight() / 2);
        }
         */
    }

    private void drawConnectorPoints(Graphics2D g, boolean onlyLayoutComponents) {

        for (ConnectorPoint cp : connectPoints) {
            if (onlyLayoutComponents && cp.isPartOfLayout()) {
                cp.draw(g, false);
            } else if (!onlyLayoutComponents) {
                cp.draw(g, true);
            }
        }
    }

    private void drawConnectorLinks(Graphics2D g) {
        for (ConnectorPoint cp : connectPoints) {
            cp.drawLinks(g);
        }
    }

    public boolean isInside(Point p) {
        return selBorder.contains(p.x - x, p.y - y);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ControlPoint getControlPoint(Point location) {
        Point toCheck = new Point(location.x - x, location.y - y);
        for (ControlPoint cp : cps) {
            if (cp.isOver(toCheck)) {
                return cp;
            }
        }
        return null;
    }

    public ConnectorPoint getConnectorPoint(Point location) {
        Point toCheck = new Point(location.x - x, location.y - y);
        for (ConnectorPoint cp : connectPoints) {
            if (cp.isOver(toCheck)) {
                return cp;
            }
        }
        return null;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")=(width=" + getWidth() + ",heigh=" + getHeight() + ")";
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public void setMouseOverOnConnectorPoints(boolean mouseOver) {
        for (ConnectorPoint cp : connectPoints) {
            cp.setMouseOver(mouseOver);
        }
    }

    public void clear() {
        for (ConnectorPoint cp : connectPoints) {
            cp.clear();
        }
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setPosition(Point location) {
        this.x = location.x;
        this.y = location.y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ConnectorPoint getInputConnectorPoint(String inputId) {
        return this.inputMap.get(inputId);
    }

    public ConnectorPoint getOutputConnectorPoint(String outputId) {
        return this.outputMap.get(outputId);
    }

    public GraphNodeLink findGraphNodeLink(Point point) {
        GraphNodeLink found = null;
        for (ConnectorPoint cp : this.connectPoints) {
            if (cp.getType() == ConnectorPoint.TYPE.SOURCE) {
                GraphNodeLink result = cp.findGraphNodeLink(point);
                if (result != null) {
                    found = result;
                }
            }
        }
        return found;
    }

    /**
     * Call this when the properties of an input change. The name itself of the
     * property could have changed, but the type or semantic could  have changed too.
     * @param oldInputName the old name for the input.
     * @param newInputName the new name for the input.
     */
    public void updateInput(String oldInputName, String newInputName) {
        IONode ioNode = (IONode) getUserObject();
        ShaderInput input = ioNode.findInput(newInputName);
        ConnectorPoint cp = this.inputMap.get(oldInputName);
        cp.setExpectedType(input.getType());
        cp.setLabel(input.getLabel());
        inputMap.remove(cp.getProperty1());
        inputMap.remove(cp.getProperty2());
        cp.setProperty1(input.getName());
        cp.setProperty2(input.getSemantic().getValue());
        addConnectorPoint(cp);
    }

    /**
     * Call this to notify this GraphNode that an input was added.
     * @param inputName the name of the input.
     */
    public void addInput(String inputName) {
        ShaderInput input = ((IONode) getUserObject()).findInput(inputName);
        if (input != null) {
            ConnectorPoint cp = new ConnectorPoint(this, "/dae/images/object.png");
            cp.setExpectedType(input.getType());
            cp.setAllowedConnections(1);
            cp.setType(ConnectorPoint.TYPE.DESTINATION);
            cp.setPosition(POSITION.LEFT);
            cp.setLabel(input.getLabel());
            cp.setProperty1(input.getName());
            cp.setProperty2(input.getSemantic().getValue());
            addConnectorPoint(cp);
        }
    }

    /**
     * Call this when an input was removed.
     * @param inputName the input to remove.
     */
    public void removeInput(String inputName) {
        ConnectorPoint cp = inputMap.get(inputName);
        if (cp != null) {
            inputMap.remove(cp.getProperty1());
            inputMap.remove(cp.getProperty2());
            this.connectPoints.remove(cp);
            cp.removeAllLinks();
        }
    }

    /**
     * Call this when the properties of an output change. The name itself of the
     * property could have changed, but the type or semantic could  have changed too.
     * @param oldOutputName the old name for the output.
     * @param newOutputName the new name for the output.
     */
    public void updateOutput(String oldOutputName, String newOutputName) {
        IONode ioNode = (IONode) getUserObject();
        ShaderOutput output = ioNode.findOutput(newOutputName);
        ConnectorPoint cp = this.outputMap.get(oldOutputName);

        cp.setExpectedType(output.getType());
        outputMap.remove(cp.getProperty1());
        outputMap.remove(cp.getProperty2());
        cp.setLabel(output.getLabel());
        cp.setProperty1(output.getName());
        cp.setProperty2(output.getSemantic().getValue());
        addConnectorPoint(cp);
    }

    /**
     * Call this to notify this GraphNode that an output was added.
     * @param outputName the name of the input.
     */
    public void addOutput(String outputName) {
        ShaderOutput output = ((IONode) getUserObject()).findOutput(outputName);
        if (output != null) {
            ConnectorPoint cp = new ConnectorPoint(this, "/dae/images/object.png");
            cp.setExpectedType(output.getType());
            cp.setAllowedConnections(1);
            cp.setType(ConnectorPoint.TYPE.SOURCE);
            cp.setPosition(POSITION.RIGHT);
            cp.setLabel(output.getLabel());
            cp.setProperty1(output.getSemantic().getValue());
            cp.setProperty2(output.getName());
            addConnectorPoint( cp);
        }
    }

    /**
     * Call this when an output was removed.
     * @param outputName the input to remove.
     */
    public void removeOutput(String outputName) {
        ConnectorPoint cp = outputMap.get(outputName);
        if (cp != null) {
            outputMap.remove(cp.getProperty1());
            outputMap.remove(cp.getProperty2());
            this.connectPoints.remove(cp);
        }
    }
}
