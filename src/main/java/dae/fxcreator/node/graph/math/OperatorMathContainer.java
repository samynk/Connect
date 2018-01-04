package dae.fxcreator.node.graph.math;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;

/**
 * This container , contains operands and vertical mathematical operators
 * such as +, - , * , . , cross product , / , %
 * @author Koen
 */
public class OperatorMathContainer extends MathContainer {

    private GridBagConstraints firstElement = new GridBagConstraints();
    private GridBagConstraints secondElement = new GridBagConstraints();
    private GridBagConstraints labelGBC = new GridBagConstraints();
    private JLabel lblOperator;
    private MathGUIElement left;
    private MathGUIElement right;
    private Operation operand;
    private int baseLine;

    public OperatorMathContainer(String operator, MathContext context) {
        super(context);
        labelGBC = new GridBagConstraints();
        labelGBC.weightx = 1.0f;
        labelGBC.weighty = 0.0f;
        labelGBC.fill = GridBagConstraints.HORIZONTAL;
        labelGBC.gridx = 1;
        labelGBC.gridy = 0;
        labelGBC.anchor = GridBagConstraints.NORTH;
        labelGBC.insets = new Insets(0, 2, 0, 2);
        lblOperator = new JLabel(operator);
        add(lblOperator, labelGBC);


        firstElement.weightx = 1.0f;
        firstElement.weighty = 0.0f;
        firstElement.fill = GridBagConstraints.HORIZONTAL;
        firstElement.gridx = 0;
        firstElement.gridy = 0;
        firstElement.anchor = GridBagConstraints.NORTH;
        firstElement.insets = new Insets(0, 2, 0, 2);


        secondElement.weightx = 1.0f;
        secondElement.weighty = 0.0f;
        secondElement.fill = GridBagConstraints.HORIZONTAL;
        secondElement.gridx = 2;
        secondElement.gridy = 0;
        secondElement.anchor = GridBagConstraints.NORTH;
        secondElement.insets = new Insets(0, 2, 0, 2);

        operand = new Operation(operator);
        setIsGroup(true);
    }

    public void addLeftElement(MathGUIElement element) {
        //if (left != null ){
        //    this.removeMathField(left);
        //}
        this.left = element;
        addMathField(element, firstElement);
        context.elementChanged(this);
    }

    public void addRightElement(MathGUIElement element) {
        //if ( right != null){
        //    this.removeMathField(right);
        //}
        this.right = element;
        addMathField(element, secondElement);
        context.elementChanged(this);
    }

    @Override
    public void replaceField(MathGUIElement toReplace, MathGUIElement newElement) {
        if (toReplace == left) {
            left = newElement;
        } else if (toReplace == right) {
            right = newElement;
        }

        super.replaceField(toReplace, newElement);
    }

    @Override
    public void addMathField(MathGUIElement field) {
        if (left == null) {
            addLeftElement(field);
        } else if (right == null) {
            addRightElement(field);
        }
    }

    @Override
    public void removeMathField(MathGUIElement field) {
        if (field == right) {
            right = null;
        } else if (field == left) {
            left = null;
        }
        super.removeMathField(field);
    }

    @Override
    public int getBaseLine() {
        return baseLine;
    }

    @Override
    public void adjustBaseLine() {
        if (left != null && right != null) {
            baseLine = 0;
            for (Component c : this.getComponents()) {
                if (c instanceof MathGUIElement) {
                    MathGUIElement me = (MathGUIElement) c;
                    int bl = me.getBaseLine();
                    if (bl > baseLine) {
                        baseLine = bl;
                    }
                } else {
                    int bl = c.getBaseline(c.getWidth(), c.getMinimumSize().height);
                    baseLine = Math.max(baseLine, bl);
                }
            }
            GridBagLayout gbl = (GridBagLayout) this.getLayout();
//            System.out.println("BaseLine is : " + baseLine);
//            System.out.println("---------------------");
            int i = 0;
            for (Component c : this.getComponents()) {
                GridBagConstraints cgbc = gbl.getConstraints(c);
                if (c instanceof MathGUIElement) {
                    MathGUIElement me = (MathGUIElement) c;
                    int mebl = me.getBaseLine();
                    mebl = mebl < 0 ? baseLine : mebl;
                    cgbc.insets.top = Math.max(baseLine - mebl, 0);
//                    System.out.println("Component " + i + " : " + me.getClass().getName() + ":" + cgbc.insets.top + " , mathelement baseline : " + me.getBaseLine());
//                    System.out.println("Gridx : " + cgbc.gridx);
                } else if (c instanceof JLabel) {
                    int cbl = c.getBaseline(c.getWidth(), c.getMinimumSize().height);
                    cgbc.insets.top = Math.max(baseLine - cbl, 0);
                    JLabel jtf = (JLabel) c;
//                    System.out.println("Component " + i + " : " + jtf.getText() + ":" + cgbc.insets.top + " , operator baseline : " + cbl);
//                    System.out.println("Gridx : " + cgbc.gridx);
                }
                i++;
                gbl.setConstraints(c, cgbc);
            }
        }
        super.adjustBaseLine();
    }

    @Override
    public void convertToCode(StringBuffer sb) {
        if (left != null) {
            left.convertToCode(sb);
        } else {
            sb.append("<err>");
        }
        sb.append(" ");
        sb.append(operand.getText());
        sb.append(" ");
        if (right != null) {
            right.convertToCode(sb);
        } else {
            sb.append("<err>");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        MathContainer parent = getParentContainer();
        if (useBrackets && !parent.isGroup()) {
            g.drawArc(1, 2, 5, getHeight() - 4, 90, 180);
            g.drawArc(getWidth() - 6, 2, 5, getHeight() - 4, 90, -180);
        }
//        g.setColor(Color.red);
//        g.drawLine(0, baseLine, getWidth(), baseLine);
//        g.setColor(Color.black);
    }
    private boolean useBrackets;

    public void setShowBrackets(boolean useBrackets) {
        this.useBrackets = useBrackets;
    }

    @Override
    public MathElement createFormula() {
        BinaryMathElement element = new BinaryMathElement();
        if (this.left != null) {
            MathElement leftElement = left.createFormula();
            element.setFirstElement(leftElement);
        }
        if (right != null) {
            MathElement rightElement = right.createFormula();
            element.setSecondElement(rightElement);
        }
        element.setOperation(new Operation(operand.getText()));
        return element;
    }
}
