package dae.fxcreator.node.graph;

import dae.fxcreator.io.FXSettings;
import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.node.ShaderType;
import dae.fxcreator.node.gui.GraphFont;
import dae.fxcreator.node.gui.ImageLoader;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * This point allows the user to connect a line to a shape.
 * @author samynk
 */
public class ConnectorPoint {

    private GraphNode parent;
    private Point location;
    private boolean mouseOver;
    private int width = 8;
    private int height = 8;
    private int markerSize = 3;
    private int imageWidth = 8;
    private int imageHeight = 8;
    private int textSpacing = 5;
    private ArrayList<GraphNodeLink> links = new ArrayList<GraphNodeLink>();
    private Image image;
    private boolean partOfLayout = false;

    public enum POSITION {

        LEFT, RIGHT, BOTTOM, OBJECT
    };
    // The position to layout this connectorpoint, default = RIGHT
    private POSITION position = POSITION.RIGHT;
    private ShaderType expectedType;
    private String label;
    private int allowedConnections = 1;
    private String property1;
    private String property2;

    public enum TYPE {

        SOURCE, DESTINATION, BOTH
    };
    private TYPE type = TYPE.BOTH;
    private GraphFont portNameFont;
    private GraphFont semanticFont;

    /** Creates a new instance of ConnectorPoint */
    public ConnectorPoint(GraphNode parent) {
        location = new Point(0, 0);
        setParent(parent);
        FXSettings settings = FXSingleton.getSingleton().getFXSettings();
        portNameFont = settings.getFont("portname");
        semanticFont = settings.getFont("semantic");
    }

    public ConnectorPoint(GraphNode parent, String imageResource) {
        this(parent);
        image = ImageLoader.getInstance().getImage(imageResource);
        //System.out.println(image);
        if (image != null) {
            setPartOfLayout(true);
            width = image.getWidth(null);
            height = image.getHeight(null);
            imageWidth = width;
            imageHeight = height;
            //System.out.println("Width :" + width+ ",height :" + height);
        }
    }

    public ConnectorPoint(GraphNode parent, Image image) {
        this(parent);
        this.image = image;
        if (image != null) {
            setPartOfLayout(true);
            width = image.getWidth(null);
            height = image.getHeight(null);
            imageWidth = width;
            imageHeight = height;
        }
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setLocation(int x, int y) {
        location.x = x;
        location.y = y;
    }

    public Point getRealLocation() {
        return new Point(getParent().getX() + location.x, getParent().getY() + location.y);
    }

    public GraphNode getParent() {
        return parent;
    }

    public void setParent(GraphNode parent) {
        this.parent = parent;
    }

    public boolean isOver(Point p) {
        int dx = Math.abs(p.x - location.x);
        int dy = Math.abs(p.y - location.y);
        return dx < (width / 2) && dy < (height / 2);
    }

    public void draw(Graphics2D g, boolean drawHighLight) {
        g.setColor(Color.blue);
        int halfWidthSize = imageWidth / 2 - 1;
        int halfHeightSize = imageHeight / 2 - 1;
        if (image != null) {
            g.drawImage(this.image, location.x - halfWidthSize, location.y - halfHeightSize, null);
        }
        if (drawHighLight) {
            g.fillRect(location.x - halfWidthSize, location.y - halfHeightSize, markerSize, markerSize);
            g.fillRect(location.x + halfWidthSize - markerSize / 2, location.y + halfHeightSize - markerSize / 2, markerSize, markerSize);
            g.fillRect(location.x - halfWidthSize, location.y + halfHeightSize - markerSize / 2, markerSize, markerSize);
            g.fillRect(location.x + halfWidthSize - markerSize / 2, location.y - halfHeightSize, markerSize, markerSize);
            if (mouseOver) {
                g.drawRect(location.x - halfWidthSize - 2, location.y - halfHeightSize - 2, width + 4, height + 4);
            }
        }

        if (this.label != null) {
            int xloc = location.x;
            if (position == POSITION.RIGHT) {
                xloc = location.x - halfWidthSize - textSpacing;
            } else {
                xloc = location.x + halfWidthSize + textSpacing;
            }

            g.setColor(portNameFont.getColor());
            g.setFont(portNameFont.getFont());
            FontMetrics fm = g.getFontMetrics();
           
            Dimension size= new Dimension();
            portNameFont.drawText(g, this.getProperty1(), xloc, location.y-halfHeightSize, null);

            if (getProperty2() != null) {
                g.setColor(semanticFont.getColor());
                g.setFont(semanticFont.getFont());
                xloc += (int) size.getWidth();
                fm = g.getFontMetrics();
                g.drawString(this.getProperty2(), xloc, location.y - halfHeightSize + fm.getAscent());
            }
        }

    }

    public void calculateSize(FontRenderContext frc) {
//        if (this.label != null) {
//            //int portNameWidth = (int) (portNameFont.getSize(this.getProperty1() + " : ", frc).getWidth());
//            int semanticWidth = 0;
//            if (this.getProperty2() != null) {
//                semanticWidth = (int) (semanticFont.getSize(this.getProperty2(), frc).getWidth());
//            }
//            width = imageWidth + portNameWidth + semanticWidth + textSpacing;
//        } else {
//            width = imageWidth;
//        }
    }

    public void drawLinks(Graphics g) {
        for (GraphNodeLink link : this.links) {
            if (link.getFrom() == this) {
                link.draw(g);
            }
        }
    }

    public void addLink(GraphNodeLink link) {
        links.add(link);
    }

    public void removeLink(GraphNodeLink link) {
        links.remove(link);
    }

    /**
     * Remove all the links going to and from this ConnectorPoint.
     */
    public void removeAllLinks() {
        for (GraphNodeLink link : links) {
            ConnectorPoint from = link.getFrom();
            if (from != this) {
                from.removeLink(link);
            } else {
                ConnectorPoint to = link.getTo();
                to.removeLink(link);
            }
        }
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;

    }

    public void clear() {
        for (GraphNodeLink link : links) {
            ConnectorPoint to = link.getTo();
            if (to != this) {
                to.removeLink(link);
            } else {
                ConnectorPoint from = link.getFrom();
                from.removeLink(link);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public boolean isPartOfLayout() {
        return partOfLayout;
    }

    public void setPartOfLayout(boolean partOfLayout) {
        this.partOfLayout = partOfLayout;
    }

    public POSITION getPosition() {
        return position;
    }

    public void setPosition(POSITION position) {
        this.position = position;
    }

    public boolean isConnectorWith(GraphNode node) {
        if (expectedType != null && node.getUserObject() != null) {
            return node.getUserObject().getClass().equals(expectedType);
        } else {
            return false;
        }
    }

    public ShaderType getExpectedType() {
        return expectedType;
    }

    public void setExpectedType(ShaderType expectedType) {
        this.expectedType = expectedType;
    }

    public int getAllowedConnections() {
        return allowedConnections;
    }

    public void setAllowedConnections(int allowedConnections) {
        this.allowedConnections = allowedConnections;
    }

    public int getNrOfConnections() {
        return links.size();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public GraphNodeLink findGraphNodeLink(Point point) {
        GraphNodeLink result = null;
        for (GraphNodeLink link : this.links) {
            if (link.pointIsOnLink(point)) {
                result = link;
            } else {
                link.setSelected(false);
            }
        }
        return result;
    }

    /**
     * @return the property1
     */
    public String getProperty1() {
        return property1;
    }

    /**
     * @param property1 the property1 to set
     */
    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    /**
     * @return the property2
     */
    public String getProperty2() {
        return property2;
    }

    /**
     * @param property2 the property2 to set
     */
    public void setProperty2(String property2) {
        this.property2 = property2;
    }
}
