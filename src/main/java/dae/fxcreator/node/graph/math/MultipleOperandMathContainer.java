package dae.fxcreator.node.graph.math;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 * A container for multiple operators.
 * @author Koen
 */
public class MultipleOperandMathContainer extends MathContainer {

    private ArrayList<Object> elements = new ArrayList<Object>();
    private int numberOfElements = 0;
    private int numberOfOperands = 0;
    private GridBagConstraints elementGBC;
    private GridBagLayout layout;
    private int baseLine = 0;

    /**
     * Creates a new MultipleOperandMathContainer object.
     * @param context the context of the math container.
     */
    public MultipleOperandMathContainer(MathContext context) {
        super(context);
        layout = new GridBagLayout();
        setLayout(layout);

        elementGBC = new GridBagConstraints();
        elementGBC.weightx = 1.0f;
        elementGBC.weighty = 0.0f;
        elementGBC.fill = GridBagConstraints.HORIZONTAL;
        elementGBC.gridx = 0;
        elementGBC.gridy = 0;
        elementGBC.anchor = GridBagConstraints.NORTH;
        elementGBC.insets = new Insets(1, 1, 1, 1);
        setIsGroup(false);
    }

    /**
     * Inserts a new operand into this container. If two operands follow
     * an empty mathfield is automaticaly inserted.
     * @param index
     */
    public void insertOperand(int index) {
    }

    @Override
    public int getBaseLine() {
        return baseLine;
    }

    @Override
    public void replaceField(MathGUIElement toReplace, MathGUIElement newElement) {
        super.replaceField(toReplace, newElement);
        int index = elements.indexOf(toReplace);
        if (index > -1) {
            elements.set(index, newElement);
        }
    }

    private Object getLastElement() {
        if (elements.size() > 0) {
            return elements.get(elements.size() - 1);
        } else {
            return null;
        }
    }

    private boolean lastIsOperand() {
        Object o = getLastElement();
        return o instanceof Operation;
    }

    private boolean lastIsMathElement() {
        Object o = getLastElement();
        return o instanceof MathGUIElement;
    }

    public void addOperand(String operation) {
        Operation op = new Operation(operation);
        if (lastIsOperand()) {
            MathField field = new MathField("a");
            elementGBC.gridx = elements.size();
            addMathField(field, elementGBC);
            elements.add(field);
        }
        elementGBC.gridx = elements.size();
        elements.add(op);
        this.add(new JLabel(operation), elementGBC);
        numberOfOperands++;
    }

    @Override
    public void addMathField(MathGUIElement element) {
        if (lastIsMathElement()) {
            Operation op = new Operation("+");
            elementGBC.gridx = elements.size();
            this.add(new JLabel("+"), elementGBC);
            elements.add(op);
        }
        elementGBC.gridx = elements.size();
        super.addMathField(element, elementGBC);
        elements.add(element);
    }

    @Override
    public MathGUIElement getFirstChild() {
        for (Object o : elements) {
            if (o instanceof MathGUIElement) {
                return (MathGUIElement) o;
            }
        }
        return null;
    }

    @Override
    public void convertToCode(StringBuffer sb) {
        MathContainer parent = getParentContainer();
        if ( parent != null && !getParentContainer().isGroup()) {
            sb.append(" (");
        }
        for (Object o : elements) {
            if (o instanceof MathGUIElement) {
                MathGUIElement element = (MathGUIElement) o;
                element.convertToCode(sb);
            }
            if (o instanceof Operation) {
                sb.append(" ");
                Operation op = (Operation) o;
                sb.append(op.getText());
                sb.append(" ");
            }
        }
        if ( parent != null && !getParentContainer().isGroup()) {
            sb.append(" )");
        }
    }

    @Override
    public void adjustBaseLine() {
        baseLine = 0;
        for (Component c : this.getComponents()) {
            if (c instanceof MathGUIElement) {
                MathGUIElement me = (MathGUIElement) c;
                int bl = me.getBaseLine();
                // System.out.println("baseline : " + bl);
                if (bl > baseLine) {
                    baseLine = bl;
                }
            } else {
                int bl = c.getBaseline(c.getWidth(), c.getMinimumSize().height);
               // System.out.println("label baseline : " + bl);
                baseLine = Math.max(baseLine, bl);
            }
        }
        GridBagLayout gbl = (GridBagLayout) this.getLayout();
//        System.out.println("BaseLine is : "+ baseLine);
//        System.out.println("---------------------");
        int i = 0;
        for (Component c : this.getComponents()) {
            GridBagConstraints cgbc = gbl.getConstraints(c);
            if (c instanceof MathGUIElement) {
                MathGUIElement me = (MathGUIElement) c;
                int mebl = me.getBaseLine();
                mebl = mebl < 0 ? baseLine : mebl;
                cgbc.insets.top = Math.max(baseLine - mebl, 0);
                //System.out.println("Component " + i + " : "+ me.getClass().getName() + ":" + cgbc.insets.top + " , mathelement baseline : " + me.getBaseLine());
                //System.out.println("Gridx : " + cgbc.gridx);
            } else if (c instanceof JLabel) {
                int cbl = c.getBaseline(c.getWidth(), c.getMinimumSize().height);
                cgbc.insets.top = Math.max(baseLine - cbl, 0);
                JLabel jtf = (JLabel) c;
                //System.out.println("Component " + i + " : "+ jtf.getText() + ":" + cgbc.insets.top + " , operator baseline : " + cbl);
                //System.out.println("Gridx : " + cgbc.gridx);
            }
            i++;
            gbl.setConstraints(c, cgbc);
        }
        this.invalidate();
        super.adjustBaseLine();
    }

    public void addElementsAfter(MathGUIElement current, String operation, MathField field) {
        int index = elements.indexOf(current);
        if (index < 0) {
            return;
        }
        // change the gridx values of the following elements;
        GridBagLayout gbl = (GridBagLayout) this.getLayout();
        for (int i = index + 1; i < elements.size(); ++i) {
            Component c = (Component) elements.get(i);
            GridBagConstraints cgbc = gbl.getConstraints(c);
            cgbc.gridx += 2;
            gbl.setConstraints(c, gbc);
        }

        Operation op = new Operation(operation);
        elementGBC.gridx = index + 1;
        elements.add(index + 1, op);
        
        this.add(new JLabel(operation), elementGBC);
        numberOfOperands++;

        elementGBC.gridx = index + 2;
        elements.add(index + 2, field);
        super.addMathField(field, elementGBC);


        invalidate();
    }

    @Override
    public MathElement createFormula(){
        MultipleMathElement element = new MultipleMathElement();
        for ( Object elem : this.elements){
            if ( elem instanceof MathGUIElement){
                MathGUIElement math =(MathGUIElement)elem;
                MathElement me = math.createFormula();
                element.addMathElement(me);
            }else if ( elem instanceof Operation ){
                Operation op = (Operation)elem;
                element.addOperator(new Operation(op.getText()));
            }
        }
        return element;
    }
}
