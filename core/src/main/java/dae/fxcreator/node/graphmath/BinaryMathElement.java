package dae.fxcreator.node.graphmath;

import dae.fxcreator.node.IOType;
import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 * This class implements the MathElement and is used to represent binary 
 * operators. 
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class BinaryMathElement extends MathElement {

    private Operation operation;
    private MathElement firstElement;
    private MathElement secondElement;

    private final static Dimension ZERO_DIMENSION = new Dimension(0, 0);

    /**
     * Creates a new empty binary math element.
     */
    public BinaryMathElement() {
    }

    /**
     * Sets the operation of the binary math element.
     * @param operation the operation of this binary math element.
     */
    public void setOperation(Operation operation) {
        this.operation = operation;
    }
    
    /**
     * Returns the type of the result of this operation.
     * @return the type of the result.
     */
    public IOType getResultType() {
        if ( operation == Operation.ASSIGN) {
            // return secondElement.getResultType();
            return null;
        }else{
            return  null;
        }
    }

    /**
     * @return the operation
     */
    public Operation getOperation() {
        return operation;
    }
    
    /**
     * Sets the first element of this binary math element.
     * @param first the first element.
     */
    public void setFirst(MathElement first) {
        this.firstElement = first;
    }

    /**
     * @return the firstElement
     */
    public MathElement getFirst() {
        return firstElement;
    }
    
    /**
     * Sets the second element of this binary math element.
     * @param secondElement the second element of this binary math element.
     */
    public void setSecond(MathElement secondElement) {
        this.secondElement = secondElement;
    }

    /**
     * @return the secondElement
     */
    public MathElement getSecond() {
        return secondElement;
    }

    @Override
    public void writeToXML(StringBuffer sb, int tabs) {
        writeTabs(sb, tabs);
        sb.append("<mathelement type=\"binary\">\n");
        if (operation != null) {
            operation.writeToXML(sb, tabs + 1);
        }
        if (firstElement != null) {
            firstElement.writeToXML(sb, tabs + 1);
        }
        if (secondElement != null) {
            secondElement.writeToXML(sb, tabs + 1);
        }

        writeTabs(sb, tabs);
        sb.append("</mathelement>\n");
    }

    @Override
    public void addMathElement(MathElement element) {
        if (firstElement == null) {
            firstElement = element;
        } else if (secondElement == null) {
            secondElement = element;
        }
    }

    @Override
    public void addOperator(Operation op) {
        this.operation = op;
    }

    @Override
    public Dimension calculateSize(Graphics2D g2d) {
        String operator = operation.getText();
        Dimension firstSize = firstElement != null ? firstElement.calculateSize(g2d) : ZERO_DIMENSION;
        Dimension secondSize = secondElement != null ? secondElement.calculateSize(g2d) : ZERO_DIMENSION;

        int bl1 = firstElement != null ? firstElement.baseLine : 0;
        int bl2 = secondElement != null ? secondElement.baseLine : 0;

        if ("/".equals(operator)) {
            size.width = Math.max(firstSize.width, secondSize.width);
            size.height = firstSize.height + secondSize.height + 8;
            baseLine = firstSize.height + 4;
        } else if ("POWER".equals(operator)) {
            size.width = firstSize.width + secondSize.width;
            int yOffset = g2d.getFontMetrics().getAscent() / 2;
            size.height = firstSize.height + secondSize.height - yOffset;
            if (!(firstElement instanceof MathVariable) && !(firstElement instanceof UnaryMathElement)) {
                size.width += 20;
            }
            baseLine = secondSize.height + firstElement.baseLine;
        } else {
            size.width = firstSize.width + secondSize.width + g2d.getFontMetrics().stringWidth(operator) + 10;
            size.height = Math.max(firstSize.height, secondSize.height);
            baseLine = Math.max(bl1, bl2);
        }
        return size;
    }

    @Override
    public void render(Graphics2D g2d, int x, int y, int width, int height) {
        String operator = operation.getText();
        Dimension firstSize = firstElement != null ? firstElement.calculateSize(g2d) : ZERO_DIMENSION;
        Dimension secondSize = secondElement != null ? secondElement.calculateSize(g2d) : ZERO_DIMENSION;

        if ("/".equals(operator)) {
            // vertical orientation
            int xoffset1 = (width - firstSize.width) / 2;
            if (firstElement != null) {
                firstElement.render(g2d, x + xoffset1, y, firstSize.width, firstSize.height);
            }
            g2d.drawLine(x, y + baseLine, x + width, y + baseLine);
            int xoffset2 = (width - secondSize.width) / 2;
            if (secondElement != null) {
                secondElement.render(g2d, x + xoffset2, y + firstSize.height + 8, secondSize.width, secondSize.height);
            }

        } else if ("POWER".equals(operator)) {
            int yOffset = g2d.getFontMetrics().getAscent() / 2;
            int xOffset = 0;

            if (!(firstElement instanceof MathVariable) && !(firstElement instanceof UnaryMathElement)) {
                int yOffset2 = y + secondElement.size.height - yOffset;
                xOffset = 10;
                // draw brackets around first element
                g2d.drawArc(x, yOffset2 + 2, 7, firstSize.height - 4, 90, 180);
                g2d.drawArc(x + firstSize.width + xOffset * 2 - 6, yOffset2 + 2, 7, firstSize.height - 4, 90, -180);
            }
            if (firstElement != null) {
                firstElement.render(g2d, x + xOffset, y + secondElement.size.height - yOffset, firstSize.width, firstSize.height);
            }
            if (secondElement != null) {
                secondElement.render(g2d, x + firstSize.width + xOffset * 2, y, secondSize.width, secondSize.height);
            }
        } else {
            // horizontal orientation
            if (firstElement != null) {
                firstElement.render(g2d, x, y + baseLine - firstElement.baseLine, firstSize.width, firstSize.height);
            }
            g2d.drawString(this.operation.getText(), x + firstSize.width + 5, y + baseLine);
            if (secondElement != null) {
                secondElement.render(g2d, x + firstSize.width + g2d.getFontMetrics().stringWidth(operator) + 10,
                        y + baseLine - secondElement.baseLine, secondSize.width, secondSize.height);
            }
        }
    }

    /*
    public void build(StringBuilder result, MathFormulaCodeGenerator codeGenerator) {
        MathTemplate template = codeGenerator.getMathTemplate(this.operation.getText());
        for (MathInstruction mi : template.getMathInstructions())
        {
            if ( mi.isLiteral()){
                result.append(mi.getText());
            }else if ( mi.isOperand()){
                MathElement child = null;
                if ( mi.getOpIndex() == 1 ){
                    child = this.getFirstElement();
                }else{
                    child = this.getSecondElement();
                }
                //child.build(result, codeGenerator);
            }
        }
    }
     */
    @Override
    public String getId() {
        return "binary";
    }

    @Override
    public String getType() {
        return "binary." + operation.name();
    }
}
