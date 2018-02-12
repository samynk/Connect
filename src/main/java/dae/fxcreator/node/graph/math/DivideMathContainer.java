package dae.fxcreator.node.graph.math;

import dae.fxcreator.node.graphmath.Operation;
import dae.fxcreator.node.graphmath.MathElement;
import dae.fxcreator.node.graphmath.BinaryMathElement;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Draws a division line
 * @author Koen
 */
public class DivideMathContainer extends MathContainer {

    private GridBagConstraints firstElement = new GridBagConstraints();
    private GridBagConstraints secondElement = new GridBagConstraints();
    private MathGUIElement dividend;
    private MathGUIElement divisor;
    private int baseLine;

    /**
     * Creates a new container to represent a division.
     * @param context the context for the math container.
     */
    public DivideMathContainer(MathContext context) {
        super(context);

        firstElement.weightx = 0.0f;
        firstElement.weighty = 0.0f;
        firstElement.fill = GridBagConstraints.NONE;
        firstElement.gridx = 0;
        firstElement.gridy = 0;
        firstElement.anchor = GridBagConstraints.CENTER;

        firstElement.insets = new Insets(0, 5, 0, 5);


        secondElement.weightx = 0.0f;
        secondElement.weighty = 0.0f;
        secondElement.fill = GridBagConstraints.NONE;
        secondElement.gridx = 0;
        secondElement.gridy = 1;
        secondElement.anchor = GridBagConstraints.CENTER;
        secondElement.insets = new Insets(0, 5, 0, 5);

        setIsGroup(true);
    }

    @Override
    public MathGUIElement getFirstChild() {
        return dividend;
    }

    /**
     * Returns a base line to use for a row layout.
     * @return the baseline in pixels.
     */
    @Override
    public int getBaseLine() {
        return baseLine;
    }

    private void calculateBaseLine() {
        Component[] children = this.getComponents();
        if (children.length >= 2) {
            int loc1y = children[0].getLocation().y;
            int loc2y = children[1].getLocation().y;
            baseLine = loc1y < loc2y ? (loc1y + children[0].getHeight() + loc2y) / 2 : (loc1y + loc2y + children[1].getHeight()) / 2;

        } else {
            baseLine = getHeight() / 2;
        }
    }

    @Override
    public void addMathField(MathGUIElement field) {
        if (dividend == null) {
            addDividendElement(field);
        } else if (divisor == null) {
            addDivisorElement(field);
        }
    }

    @Override
    public void removeMathField(MathGUIElement field) {
        if (field == dividend) {
            dividend = null;
        } else if (field == divisor) {
            divisor = null;
        }
        super.removeMathField(field);
    }

    public void addDividendElement(MathGUIElement element) {
        this.dividend = element;
        addMathField(element, firstElement);
        //adjustBaseLine();
        context.elementChanged(this);
        repaint();
    }

    public void addDivisorElement(MathGUIElement element) {
        this.divisor = element;
        addMathField(element, secondElement);

        //adjustBaseLine();
        context.elementChanged(this);
        repaint();
    }

    @Override
    public void replaceField(MathGUIElement toReplace, MathGUIElement newElement) {
        if (toReplace == dividend) {
            dividend = newElement;
        } else if (toReplace == divisor) {
            divisor = newElement;
        }
        this.invalidate();
        super.replaceField(toReplace, newElement);
    }

    /**
     * Paints the division line for this component.
     * @param g the graphics object to paint the division line on.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        calculateBaseLine();
        g.drawLine(0 + gbc.insets.left + 5, baseLine, getWidth() - gbc.insets.right - 5, baseLine);
        if (!getParentContainer().isGroup()) {
            g.drawArc(1, 2, 7, getHeight() - 4, 90, 180);
            g.drawArc(getWidth() - 8, 2, 7, getHeight() - 4, 90, -180);
        }
    }

    @Override
    public void adjustBaseLine() {
        calculateBaseLine();
        super.adjustBaseLine();
    }

    @Override
    public void convertToCode(StringBuffer sb) {
        MathContainer parent = getParentContainer();
        if (parent != null && !parent.isGroup()) {
                sb.append("( ");
        }
        if (dividend != null) {
            if ( dividend.isGroup() ){
                sb.append(" ( ");
            }
            dividend.convertToCode(sb);
            if ( dividend.isGroup() ){
                sb.append(" ) ");
            }
        }
        sb.append(" / ");
        if (divisor != null) {
            if ( divisor.isGroup())
                sb.append(" ( ");
            divisor.convertToCode(sb);
            if ( divisor.isGroup())
                sb.append(" ) ");
        }
        if (parent!= null && !parent.isGroup()) {
            sb.append(" )");
        }

    }

    @Override
    public MathElement createFormula()
    {
        BinaryMathElement element = new BinaryMathElement();
        if ( dividend != null ){
            MathElement divElement = dividend.createFormula();
            element.setFirst(divElement);
        }
        if ( divisor != null ){
            MathElement divisorElement = divisor.createFormula();
            element.setSecond(divisorElement);
        }
        element.setOperation(Operation.DIVIDE);
        return element;
    }
}
