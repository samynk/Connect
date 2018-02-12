package dae.fxcreator.node.graph.math;

import dae.fxcreator.node.graphmath.Operation;
import dae.fxcreator.node.graphmath.MathElement;
import dae.fxcreator.node.graphmath.BinaryMathElement;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;

/**
 * This class enables the user to define a power function.
 * @author Koen
 */
public class PowerMathContainer extends MathContainer {

    private GridBagConstraints firstElement = new GridBagConstraints();
    private GridBagConstraints secondElement = new GridBagConstraints();
    private MathGUIElement base;
    private MathGUIElement power;
    private int baseLine;

    /**
     * Creates a new container to represent a division.
     * @param context the context for the math container.
     */
    public PowerMathContainer(MathContext context) {
        super(context);

        firstElement.weightx = 1.0f;
        firstElement.weighty = 0.0f;
        firstElement.fill = GridBagConstraints.HORIZONTAL;
        firstElement.gridx = 0;
        firstElement.gridy = 0;
        firstElement.anchor = GridBagConstraints.SOUTH;
        firstElement.insets = new Insets(0, 2, 2, 0);


        secondElement.weightx = 1.0f;
        secondElement.weighty = 0.0f;
        secondElement.fill = GridBagConstraints.HORIZONTAL;
        secondElement.gridx = 1;
        secondElement.gridy = 0;
        secondElement.anchor = GridBagConstraints.SOUTH;
        secondElement.insets = new Insets(2, 0, 10, 2);

        setIsGroup(true);
    }

    /**
     * Returns a base line to use for a row layout.
     * @return the baseline in pixels.
     */
    @Override
    public int getBaseLine() {
        return baseLine;
    }

    public Point getPowerLocation() {
        if (base != null) {
            Component c = (Component) base;
            Point location = c.getLocation();
            location.x += c.getWidth();
            return location;
        } else {
            return new Point(0, 0);
        }
    }

    @Override
    public void replaceField(MathGUIElement toReplace, MathGUIElement newElement) {
        if (toReplace == base) {
            base = newElement;
        } else if (toReplace == power) {
            power = newElement;
        }
        super.replaceField(toReplace, newElement);
    }

    @Override
    public void addMathField(MathGUIElement field) {
        if (base == null) {
            addBaseElement(field);
        } else if (power == null) {
            addPowerElement(field);
        }
    }

    @Override
    public void removeMathField(MathGUIElement field) {
        if (field == base) {
            base = null;
        } else if (field == power) {
            power = null;
        }
        super.removeMathField(field);
        adjustBaseLine();
    }

    public void addBaseElement(MathGUIElement element) {
        this.base = element;
        addMathField(element, firstElement);

        adjustBaseLine();
        context.elementChanged(this);
        repaint();
    }

    public void addPowerElement(MathGUIElement element) {
        this.power = element;
        addMathField(element, secondElement);

        adjustBaseLine();
        context.elementChanged(this);
        repaint();
    }

    @Override
    public MathGUIElement getFirstChild() {
        return base;
    }

    @Override
    public void adjustBaseLine() {
        calculateBaseLine();
        if (base != null && power != null) {
            Component c = (Component) base;
            Dimension d = c.getSize();
            secondElement.insets.bottom = d.height - 4;
            GridBagLayout layout = (GridBagLayout) this.getLayout();
            layout.setConstraints((Component) power, secondElement);
            c.invalidate();
            ((Component) power).invalidate();
        }

        super.adjustBaseLine();
    }

    @Override
    public void convertToCode(StringBuffer sb) {
        sb.append("pow( ");
        if (base != null) {
            this.base.convertToCode(sb);
        } else {
            sb.append("<err>");
        }
        sb.append(" , ");
        if (power != null) {
            this.power.convertToCode(sb);
        } else {
            sb.append("<err>");
        }
        sb.append(" )");
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        calculateBaseLine();
        if (base instanceof MathContainer) {
            MathContainer mc = (MathContainer) base;

            g.drawArc(mc.getX(), mc.getY(), 7, mc.getHeight() - 4, 90, 180);
            g.drawArc(mc.getX() + mc.getWidth() - 6, mc.getY(), 7, mc.getHeight() - 4, 90, -180);

        }
//        g.setColor(Color.red);
//        g.drawLine(0,baseLine, getWidth(), baseLine);
//        g.setColor(Color.black);
    }

    private void calculateBaseLine() {
        //Component[] children = this.getComponents();
        if (base != null) {
            Component cbase = (Component) base;
            baseLine = cbase.getY() + base.getBaseLine();

        } else {
            baseLine = getHeight() / 2;
        }
    }

    @Override
    public MathElement createFormula() {
        BinaryMathElement element = new BinaryMathElement();
        if (base != null) {
            MathElement baseElement = base.createFormula();
            element.setFirst(baseElement);
        }
        if (power != null) {
            MathElement powerElement = power.createFormula();
            element.setSecond(powerElement);
        }
        element.setOperation(Operation.POWER);
        return element;
    }
}
