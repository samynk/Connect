package dae.fxcreator.node.graph;

import dae.fxcreator.gui.model.FXSettings;
import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.node.ShaderIO;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderOutput;
import dae.fxcreator.gui.model.GraphFont;
import dae.fxcreator.gui.model.ImageLoader;
import dae.fxcreator.gui.model.NodeStyle;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Koen
 */
public class JConnectorPoint extends JPanel implements ChangeListener {

    private final JLabel lblPortName = new JLabel();
    private final JLabel lblSemicolumn = new JLabel(":");
    private final JLabel lblSemantic = new JLabel();
    private final JTerminal lblIcon = new JTerminal();
    private final JGraphNode parent;

    private Color borderColor;

    public void updateStyle(NodeStyle style) {
        FXSettings settings = FXSingleton.getSingleton().getFXSettings();
        GraphFont port = settings.getFont(style.getPortFontName());
        lblPortName.setFont(port.getFont());
        lblPortName.setForeground(port.getColor());

        lblSemicolumn.setFont(port.getFont());
        lblSemicolumn.setForeground(port.getColor());

        GraphFont semantic = settings.getFont(style.getSemanticFontName());
        lblSemantic.setFont(semantic.getFont());
        lblSemantic.setForeground(semantic.getColor());

        borderColor = settings.getGradient("node").getC2();
        lblIcon.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, borderColor));
    }

    public void highlight() {
        lblIcon.highlight();
        this.setBackground(Color.white);
        setOpaque(true);
        repaint();
    }

    public void unhighlight() {
        setOpaque(false);
        lblIcon.unhighlight();
        repaint();
    }

    /**
     * Defines the possible positions of a connector point.
     */
    public enum POSITION {

        LEFT, RIGHT, BOTTOM, OBJECT
    };
    /**
     * The position of the connector point.
     */
    private final POSITION position;

    public enum TYPE {

        SOURCE, DESTINATION, BOTH
    };
    /**
     * The type of connector point.
     */
    private TYPE type;
    /**
     * The ShaderIO object.
     */
    private final ShaderIO userObject;
    /**
     * The connector objects to draw (only for input JConnectorPoints)
     */
    private Connector connector;

    /**
     * Creates a new JConnector point.
     *
     * @param parent the parent graphnode of this connector.
     * @param io the object with the input or output definition.
     * @param position the position of the connector 5coo
     */
    public JConnectorPoint(JGraphNode parent, ShaderIO io, POSITION position) {
        setOpaque(false);
        this.position = position;
        this.parent = parent;
        this.userObject = io;

        if (io.isInput()) {
            type = TYPE.DESTINATION;
            connector = new Connector((ShaderInput) io);
        } else if (io.isOutput()) {
            type = TYPE.SOURCE;
        }
        if (null == position) {
            setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
            add(lblIcon);
            add(lblPortName);
            add(lblSemicolumn);
            add(lblSemantic);
        } else {
            switch (position) {
                case LEFT:
                    setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
                    add(lblIcon);
                    add(lblPortName);
                    add(lblSemicolumn);
                    add(lblSemantic);
                    break;
                case RIGHT:
                    setLayout(new FlowLayout(FlowLayout.RIGHT, 1, 1));
                    add(lblPortName);
                    add(lblSemicolumn);
                    add(lblSemantic);
                    add(lblIcon);
                    break;
                default:
                    setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
                    add(lblIcon);
                    add(lblPortName);
                    add(lblSemicolumn);
                    add(lblSemantic);
                    break;
            }
        }
        //lblPortName.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.red));
        lblIcon.setConnectorPoint(this);

        io.addChangeListener(this);
        syncWithModel();
    }

    public final void syncWithModel() {
        lblPortName.setText(userObject.getName() + " ");
        if (userObject.getSemantic().isValid()) {
            lblSemantic.setText(userObject.getSemantic().getValue() + " ");
            lblSemantic.setVisible(true);
            lblSemicolumn.setVisible(true);
        } else {
            lblSemantic.setVisible(false);
            lblSemicolumn.setVisible(false);
        }

        if (userObject.getType() != null) {
            String icon = userObject.getType().getIcon();
            Image image = ImageLoader.getInstance().getImage(icon);
            if (image == null) {
                if (isOutput()) {
                    lblIcon.setText(">");
                } else {
                    lblIcon.setText("<");
                }
            } else {
                lblIcon.setIcon(new ImageIcon(image));
            }
        }else{
            Logger.getLogger("graphtool").log(Level.WARNING, "The type for the userobject " + userObject.getName() + " is null.");
        }

//         System.out.println("portname : " +lblPortName.getPreferredSize() + "," + lblPortName.getSize());
//        System.out.println(lblSemantic.getPreferredSize());
//        System.out.println(lblSemicolumn.getPreferredSize());
//        System.out.println("icon :" + lblIcon.getPreferredSize()+","+lblIcon.getSize());
//
//        Dimension d = lblPortName.getMinimumSize();
//        d.width += 2;
//        lblPortName.setMinimumSize( d);
//        lblPortName.setPreferredSize( d);
    }

    /**
     * Returns the JGraphNode object that is the parent of this JConnectorPoint.
     *
     * @return the parent object of this IO point.
     */
    public JGraphNode getGraphNode() {
        return parent;
    }

    /**
     * Get start of connector line.
     */
    public Point getConnectorLocation() {
        Point labelLocation = lblIcon.getLocation();
        int width = lblIcon.getWidth();
        int height = lblIcon.getHeight();

        if (position == POSITION.RIGHT) {
            labelLocation.x += width;
            labelLocation.y += height / 2;
        } else if (position == POSITION.LEFT || position == POSITION.OBJECT) {
            labelLocation.y += height / 2;
        } else if (position == POSITION.BOTTOM) {
            labelLocation.y += height;
            labelLocation.x += width / 2;
        }

        Component c = this.lblIcon;
        do {

            c = c.getParent();
            if (c != null) {
                labelLocation.x += c.getX();
                labelLocation.y += c.getY();
            }

        } while (c != parent && c != null);

        //labelLocation.x += parent.getX();
        //labelLocation.y += parent.getY();
        return labelLocation;
    }

    /**
     * Draw the connectors
     *
     * @param g2d the graphic context.
     */
    public void drawConnector(GraphEditor editor, Graphics2D g2d) {
        if (this.userObject.isInput()) {
            ShaderInput si = (ShaderInput) userObject;
            if (si.getConnected()) {
                ShaderOutput so = si.getConnectedInput();
                Point end = parent.getLocation(so);
                connector.updatePath(getConnectorLocation(), end);
                connector.paintPath(g2d);
                editor.addConnector(connector);
            }
        }
    }

    public void drawConnectorAttachment(GraphEditor editor, Graphics2D g2d) {
        g2d.setColor(Color.white);
        Point location = getConnectorLocation();
        if (position == POSITION.LEFT) {
            g2d.fillArc(location.x - 4, location.y - 5, 10, 10, +90, 180);
        } else if (position == POSITION.RIGHT) {
            g2d.fillArc(location.x - 6, location.y - 5, 10, 10, 90, -180);
        }
        //g2d.fillOval(location.x - 5, location.y - 5, 10, 10);
    }

    /**
     * Checks if this is an input connector.
     *
     * @return true if this is an input, false otherwise.
     */
    public boolean isInput() {
        return this.userObject.isInput();
    }

    /**
     * Checks if this is an output connector.
     *
     * @return true if this is an output, false otherwise.
     */
    public boolean isOutput() {
        return this.userObject.isOutput();
    }

    /**
     * Returns the user object of this connector point.
     *
     * @return the userobject.
     */
    public ShaderIO getUserObject() {
        return userObject;
    }

    /**
     * Called when the ShaderIO object has changed.
     *
     * @param e the state change event.
     */
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == this.userObject) {
            this.syncWithModel();
        }
    }
}
