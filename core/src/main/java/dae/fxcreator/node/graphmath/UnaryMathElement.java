package dae.fxcreator.node.graphmath;

import dae.fxcreator.io.type.IOTypeLibrary;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.IOType;
import dae.fxcreator.node.IOPort;
import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 *
 * @author Koen
 */
public class UnaryMathElement extends MathElement {

    private Operation operation;
    private MathElement child;

    public UnaryMathElement() {
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
        if (child != null) {
            return child.getResultType(node, library);
        } else {
            return null;
        }
    }

    public MathElement getChild() {
        return child;
    }

    public void setChild(MathElement child) {
        this.child = child;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public void writeToXML(StringBuffer sb, int tabs) {
        writeTabs(sb, tabs);
        sb.append("<mathelement type=\"unary\">\n");
        if (operation != null) {
            operation.writeToXML(sb, tabs + 1);
        }
        if (child != null) {
            child.writeToXML(sb, tabs + 1);
        }
        writeTabs(sb, tabs);
        sb.append("</mathelement>\n");
    }

    @Override
    public void addMathElement(MathElement element) {
        this.child = element;
    }

    @Override
    public void addOperator(Operation op) {
        System.out.println("UnaryMathElement : " + operation.getText());
        this.operation = op;
    }

    @Override
    public Dimension calculateSize(Graphics2D g2d) {
        if (child == null) {
            size.width = 0;
            size.height = 0;
            return size;
        }
        Dimension childSize = child.calculateSize(g2d);
        size.width = childSize.width;
        size.height = childSize.height;
        if ("SQRT".equals(operation.getText())) {
            size.width += 30;
            size.height += 5;
        } else if ("GROUP".equals(operation.getText())) {
            size.width += 16;
        } else if ("LENGTH".equals(operation.getText())) {
            size.width += 30;
        }
        baseLine = child.baseLine;
        return size;
    }

    @Override
    public void render(Graphics2D g2d, int x, int y, int width, int height) {
        if (child == null) {
            return;
        }
        if ("SQRT".equals(operation.getText())) {
            int h1 = 2;
            int h2 = (int) (0.65 * height);
            int h3 = (int) (0.85 * height);
            int h4 = (int) (0.97 * height);
            g2d.drawLine(x + 4, y + h3, x + 6, y + h2);
            g2d.drawLine(x + 6, y + h2, x + 10, y + h4);
            g2d.drawLine(x + 10, y + h4, x + 15, y + h1);
            g2d.drawLine(x + 15, y + h1, x + width - 2, y + h1);
            g2d.drawLine(x + width - 2, y + h1, x + width - 2, y + 5);
            child.render(g2d, x + 20, y + 5, child.size.width, child.size.height);
        } else if ("LENGTH".equals(operation.getText())) {
            g2d.drawLine(x + 5, y + 2, x + 5, y + height - 2);
            g2d.drawLine(x + 8, y + 2, x + 8, y + height - 2);

            g2d.drawLine(x + width - 5, y + 2, x + width - 5, y + height - 2);
            g2d.drawLine(x + width - 8, y + 2, x + width - 8, y + height - 2);
            child.render(g2d, x + 15, y, width - 30, height);
        } else if ("GROUP".equals(operation.getText())) {
            g2d.drawArc(x + 5, 2, 7, y + height - 2, 90, 180);
            g2d.drawArc(x + width - 5, 2, 7, y + height - 2, 90, -180);
            child.render(g2d, x + 8, y, width - 8, height);
        } else {
            child.render(g2d, x, y, width, height);
        }
    }

    //@Override
    /*
    public void build(StringBuilder result, MathFormulaCodeGenerator codeGenerator) {
        MathTemplate mt = codeGenerator.getMathTemplate(this.operation.getText());
        if ( mt != null)
        {
            for ( MathInstruction mi : mt.getMathInstructions())
            {
                if ( mi.isLiteral()){
                    result.append(mi.getText());
                }else{
                    //this.child.build(result, codeGenerator);
                }
            }
        }
    }
     */
    @Override
    public String getId() {
        return "unary";
    }

    @Override
    public String getType() {
        return "unary";
    }
}
