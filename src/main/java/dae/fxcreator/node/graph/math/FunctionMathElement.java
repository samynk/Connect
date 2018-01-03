/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.node.graph.math;

import dae.fxcreator.io.codegen.MathFormulaCodeGenerator;
import dae.fxcreator.io.codegen.MathInstruction;
import dae.fxcreator.io.codegen.MathTemplate;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Koen
 */
public class FunctionMathElement extends MathElement {

    private String functionName;
    private ArrayList<MathElement> parameters = new ArrayList<MathElement>();

    public FunctionMathElement(String functionName) {
        this.functionName = functionName;
    }

    /**
     * Add a math element as a parameter for this function math element.
     *
     * @param element
     */
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

    @Override
    public void build(StringBuilder result, MathFormulaCodeGenerator codeGenerator) {
        MathTemplate template = codeGenerator.getMathTemplate(this.getFunctionName());
        for (MathInstruction mi : template.getMathInstructions()) {
            if (mi.isLiteral()) {
                result.append(mi.getText());
            } else if (mi.isOperand()) {
                MathElement child = this.parameters.get(mi.getOpIndex() - 1);
                child.build(result, codeGenerator);
            }
        }
    }
}