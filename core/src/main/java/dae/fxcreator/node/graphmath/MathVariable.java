package dae.fxcreator.node.graphmath;

import dae.fxcreator.io.type.ShaderTypeLibrary;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.IOType;
import dae.fxcreator.node.NodeIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;

/**
 *
 * @author Koen
 */
public class MathVariable extends MathElement {

    private boolean isVector;
    private String varName;

    private final Color numberColor = new Color(35, 92, 85);

    public MathVariable() {
    }

    public MathVariable(String varName) {
        this.varName = varName;
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
    public IOType getResultType(IONode node, ShaderTypeLibrary library) {
        NodeIO port = node.getPort(varName);
        if ( port != null ){
            return port.getIOType();
        }else{
            return library.getType("VOID");
        }
    }

    /**
     * @return the isVector
     */
    public boolean isIsVector() {
        return isVector;
    }

    /**
     * @param isVector the isVector to set
     */
    public void setIsVector(boolean isVector) {
        this.isVector = isVector;
    }

    /**
     * @return the varName
     */
    public String getVarName() {
        return varName;
    }

    /**
     * @param varName the varName to set
     */
    public void setVarName(String varName) {
        this.varName = varName;
    }

    @Override
    public void writeToXML(StringBuffer sb, int tabs) {
        writeTabs(sb, tabs);
        sb.append("<mathelement type=\"variable\" isVector=\"");
        sb.append(this.isVector);
        sb.append("\" name=\"");
        sb.append(this.varName);
        sb.append("\"/>\n");
    }

    @Override
    public void addMathElement(MathElement element) {
        // no children allowed.
    }

    @Override
    public void addOperator(Operation op) {
        // no children allowed.
    }

    @Override
    public Dimension calculateSize(Graphics2D g2d) {
        FontMetrics fm = g2d.getFontMetrics();
        LineMetrics lm = fm.getLineMetrics(varName, g2d);
        size.width = fm.stringWidth(varName);
        size.height = (int) lm.getHeight();
        baseLine = (int) lm.getAscent() + 1;
        return size;
    }

    @Override
    public void render(Graphics2D g2d, int x, int y, int width, int height) {
        Color backup = g2d.getColor();
        try {
            Integer.parseInt(varName);
            g2d.setColor(numberColor);
        } catch (NumberFormatException ex) {

        }
        try {
            Float.parseFloat(varName);
            g2d.setColor(numberColor);
        } catch (NumberFormatException ex) {

        }
        g2d.drawString(varName, x, y + baseLine);
        g2d.setColor(backup);
    }

    //@Override
    /*
    public void build(StringBuilder result, MathFormulaCodeGenerator codeGenerator) {
        MathConstant mc = codeGenerator.getMathConstant(this.varName);
        if ( mc != null ){
            result.append(mc.getValue());
        }else{
            result.append(varName);
        }
    }
     */
    @Override
    public String getId() {
        return "var";
    }

    @Override
    public String getType() {
        return "value.VARNAME";
    }
}
