package dae.fxcreator.node.graphmath;

import dae.fxcreator.io.type.IOTypeLibrary;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.IOType;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Koen
 */
public class FunctionMathElement extends MathElement {

    private final String functionName;
    private final ArrayList<MathElement> parameters = new ArrayList<>();

    public FunctionMathElement(String functionName) {
        this.functionName = functionName;
    }

    /**
     * Returns the type of the result of this operation.
     *
     * @param node the node that hosts this MathElement object.
     * @param library the library with shader types and their priority when up
     * and down casting.
     * @return the type of the result.
     */
    @Override
    public IOType getResultType(IONode node, IOTypeLibrary library) {
        return null;
    }

    /**
     * Add a math element as a parameter for this function math element.
     *
     * @param element
     */
    @Override
    public void addMathElement(MathElement element) {
        parameters.add(element);
    }

    /**
     * Returns the function name of the math element.
     *
     * @return the function name.
     */
    public String getFunctionName() {
        return functionName;
    }

    @Override
    public void writeToXML(StringBuffer sb, int tabs) {
        writeTabs(sb, tabs);
        sb.append("<mathelement type=\"function\" methodName=\"");
        sb.append(this.functionName);
        sb.append("\">\n");
        for (MathElement element : parameters) {
            element.writeToXML(sb, tabs + 1);
        }
        writeTabs(sb, tabs);
        sb.append("</mathelement>\n");
    }

    @Override
    public void addOperator(Operation op) {
        // do nothing , function is the operation.
    }

    public int getNrOfArguments() {
        return parameters.size();
    }

    public Iterable<MathElement> getMathElements() {
        return parameters;
    }

    @Override
    public Dimension calculateSize(Graphics2D g2d) {
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(functionName);
        int height = fm.getMaxAscent() + fm.getMaxDescent();
        baseLine = fm.getMaxAscent();
        // function has a horizontal orientation.
        for (MathElement element : parameters) {
            Dimension esize = element.calculateSize(g2d);
            width += esize.width + 5;
            if (esize.height > height) {
                height = size.height;
            }
            if (element.baseLine > baseLine) {
                baseLine = element.baseLine;
            }

        }
        size.width = width;
        size.height = height;
        return size;
    }

    @Override
    public void render(Graphics2D g2d, int x, int y, int width, int height) {
        int rx = x;
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(functionName + "( ", x, y + baseLine);
        rx += fm.stringWidth(functionName + "( ");

        // horizontal orientation.
        for (int i = 0; i < parameters.size(); ++i) {
            MathElement element = parameters.get(i);
            element.render(g2d, rx, y + baseLine - element.baseLine, element.size.width, element.size.height);
            rx += element.size.width;
            if (i != parameters.size() - 1) {
                g2d.drawString(", ", rx, y + baseLine);
                rx += fm.stringWidth(", ");
            } else {
                g2d.drawString(" )", rx, y + baseLine);
                rx += fm.stringWidth(" )");
            }
        }
    }

    /*
    public void build(StringBuilder result, MathFormulaCodeGenerator codeGenerator) {
        MathTemplate template = codeGenerator.getMathTemplate(this.getFunctionName());
        for (MathInstruction mi : template.getMathInstructions()) {
            if (mi.isLiteral()) {
                result.append(mi.getText());
            } else if (mi.isOperand()) {
                MathElement child = this.parameters.get(mi.getOpIndex() - 1);
                //child.build(result, codeGenerator);
            }
        }
    }
     */
    @Override
    public String getId() {
        return "function";
    }

    @Override
    public String getType() {
        return "function";
    }
}
