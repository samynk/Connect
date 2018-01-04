package dae.fxcreator.node.graph.math;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 * This container contains a function and the arguments for that function.
 *
 * @author Koen
 */
public class FunctionMathContainer extends MathContainer {

    private String functionName;
    private int nrOfArguments;
    private int baseLine;
    private ArrayList<MathGUIElement> parameters = new ArrayList<MathGUIElement>();
    private Font labelFont;
    private int parameterIndex = 0;

    public FunctionMathContainer(MathContext context, String functionName, int nrOfArguments) {
        super(context);
        this.functionName = functionName;
        this.nrOfArguments = nrOfArguments;

        labelFont = this.getFont().deriveFont(Font.BOLD);

        gbc.weightx = 0.0f;
        gbc.weighty = 0.0f;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0, 2, 0, 0);

        JLabel lblFunctionName = new JLabel(functionName);
        lblFunctionName.setFont(labelFont);
        add(lblFunctionName, gbc);
        gbc.gridx++;
        JLabel lblBracket = new JLabel("(");
        add(lblBracket, gbc);
        gbc.gridx++;
        for (int i = nrOfArguments - 1; i > -1; --i) {
            MathField parameter = new MathField("param" + (nrOfArguments - i));
            this.addMathField(parameter, gbc);
            gbc.gridx++;
            parameters.add(parameter);

            if (i != 0) {
                JLabel comma = new JLabel(",");
                add(comma, gbc);
                gbc.gridx++;
            }
        }
        int gridxBackup = gbc.gridx;
        gbc.gridx = 20;
        JLabel lblRightBracket = new JLabel(")");
        add(lblRightBracket, gbc);

        gbc.gridx = gridxBackup;

        setIsGroup(true);
    }

    @Override
    public int getBaseLine() {
        baseLine = 0;
        for (Component c : this.getComponents()) {
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                int labelBase = label.getBaseline(label.getWidth(), label.getMinimumSize().height);
                baseLine = Math.max(baseLine, labelBase);
            } else if (c instanceof MathGUIElement) {
                MathGUIElement me = (MathGUIElement) c;
                baseLine = Math.max(baseLine, me.getBaseLine());
            }
        }
        //System.out.println("Functionmath container baseline : " + baseLine);
        return baseLine;
    }

    @Override
    public void adjustBaseLine() {
        getBaseLine();
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
//                System.out.println("Component " + i + " : "+ me.getClass().getName() + ":" + cgbc.insets.top + " , mathelement baseline : " + me.getBaseLine());
//                System.out.println("Gridx : " + cgbc.gridx);
            } else if (c instanceof JLabel) {
                int cbl = c.getBaseline(c.getWidth(), c.getMinimumSize().height);
                cgbc.insets.top = Math.max(baseLine - cbl, 0);
                JLabel jtf = (JLabel) c;
//                System.out.println("Component " + i + " : "+ jtf.getText() + ":" + cgbc.insets.top + " , operator baseline : " + cbl);
//                System.out.println("Gridx : " + cgbc.gridx);
            }
            i++;
            gbl.setConstraints(c, cgbc);
        }
        this.invalidate();
        super.adjustBaseLine();
    }

    @Override
    public void replaceField(MathGUIElement toReplace, MathGUIElement newElement) {

        int index = parameters.indexOf(toReplace);
        if (index > -1) {
            parameters.set(index, newElement);
        }
        super.replaceField(toReplace, newElement);
    }

    public void setFirstParameter(MathGUIElement parameter) {
        if (parameters.size() > 0) {
            MathGUIElement first = parameters.get(0);
            this.replaceField(first, parameter);
            parameters.set(0, parameter);
        }
    }

    @Override
    public void convertToCode(StringBuffer sb) {

        sb.append(functionName);
        sb.append("(");

        for (int i = 0; i < parameters.size(); i++) {
            parameters.get(i).convertToCode(sb);
            if (i != parameters.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")");

    }

    @Override
    public void addMathField(MathGUIElement element) {
        if (parameterIndex < parameters.size()) {
            MathGUIElement parameter = parameters.get(parameterIndex);
            this.replaceField(parameter, element);
            ++parameterIndex;
        }
    }

    @Override
    public MathElement createFormula() {
        FunctionMathElement element = new FunctionMathElement(functionName);
        for (MathGUIElement parameter : this.parameters) {
            MathElement param = parameter.createFormula();
            element.addMathElement(param);
        }
        return element;
    }
}
