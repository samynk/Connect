/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.node.graph.math;

import dae.fxcreator.io.codegen.MathFormulaCodeGenerator;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Koen
 */
public class MultipleMathElement extends MathElement {

    private ArrayList<MathElement> elements = new ArrayList<MathElement>();
    private ArrayList<Operation> operands = new ArrayList<Operation>();

    public MultipleMathElement() {
    }

    public void addMathElement(MathElement element) {
        elements.add(element);
    }

    @Override
    public void writeToXML(StringBuffer sb, int tabs) {
        writeTabs(sb, tabs);
        sb.append("<mathelement type=\"list\">\n");
        for (int i = 0; i < elements.size(); ++i) {
            MathElement element = elements.get(i);
            element.writeToXML(sb, tabs + 1);
            if (i < operands.size()) {
                Operation op = operands.get(i);
                op.writeToXML(sb, tabs + 1);
            }
        }
        writeTabs(sb, tabs);
        sb.append("</mathelement>\n");
    }

    @Override
    public void addOperator(Operation op) {
        operands.add(op);
    }

    public int getNrOfOperands() {
        return elements.size();
    }

    public MathElement getOperand(int i) {
        return elements.get(i);
    }

    public Operation getOperation(int i) {
        if (i < operands.size()) {
            return operands.get(i);
        } else {
            return null;
        }
    }

    @Override
    public Dimension calculateSize(Graphics2D g2d) {
        int width = 0;
        int aboveHeight = 0;
        int belowHeight = 0;
        // horizontal orientation
        for (MathElement elem : this.elements) {
            Dimension d = elem.calculateSize(g2d);
            width += d.width;

            int bl = elem.baseLine;
            int bh = d.height - bl;
            int ah = d.height - bh;

            if (ah > aboveHeight) {
                aboveHeight = ah;
                baseLine = bl;
            }
            if (bh > belowHeight) {
                belowHeight = bh;
            }
        }

        FontMetrics fm = g2d.getFontMetrics();
        for (Operation op : this.operands) {
            // 5px clearance left and right of each op.
            width += fm.stringWidth(op.getText()) + 10;
        }
        size.width = width;
        size.height = aboveHeight + belowHeight;
        return size;
    }

    @Override
    public void render(Graphics2D g2d, int x, int y, int width, int height) {
        int cx = x;
        FontMetrics fm = g2d.getFontMetrics();
        for (int i = 0; i < elements.size(); ++i) {
            MathElement current = elements.get(i);
            current.render(g2d, cx, y + baseLine - current.baseLine, current.size.width, current.size.height);
            cx += current.size.width + 5;
            
            if ( i != elements.size() - 1 ){
                Operation op = operands.get(i);
                g2d.drawString(op.getText(), cx, y + baseLine);
                cx+= fm.stringWidth(op.getText()+5);
            }
        }
    }

    @Override
    public void build(StringBuilder result, MathFormulaCodeGenerator codeGenerator) {
        result.append("<multiple elements not yet implemented>");
    }
}