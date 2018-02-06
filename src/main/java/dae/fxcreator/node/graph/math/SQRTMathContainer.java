package dae.fxcreator.node.graph.math;

import dae.fxcreator.node.graphmath.UnaryMathElement;
import dae.fxcreator.node.graphmath.Operation;
import dae.fxcreator.node.graphmath.MathElement;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 *
 * @author Koen
 */
public class SQRTMathContainer extends MathContainer{

    private MathGUIElement child;
    private int baseLine;

    /** Creates new form SQRTPanel */
    public SQRTMathContainer(MathContext context) {
        super(context);
        setLayout(new GridBagLayout());
        setIsGroup(true);
    }

    /**
     * Returns a base line to use for a row layout.
     * @return the baseline in pixels.
     */
    @Override
    public int getBaseLine() {
        baseLine = (int)(0.85*getHeight());
        return baseLine;
    }

    @Override
    public void addMathField(MathGUIElement field){
        //super.removeAll();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5,15,0,0);
        gbc.fill = GridBagConstraints.BOTH;
        this.child = field;
        super.addMathField(field);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int h = this.getHeight();
        int w = this.getWidth();

        int h1 = 2;
        int h2 = (int)(0.65*h);
        int h3 = (int)(0.85*h);
        int h4 = (int)(0.97*h);
        g.drawLine(4, h3, 6, h2);
        g.drawLine(6, h2, 10, h4);
        g.drawLine(10,h4,15,h1);
        g.drawLine(15,h1, w-2, h1);
        g.drawLine(w-2,h1, w-2, 5);

//        g.setColor(Color.red);
//        g.drawLine(0,baseLine,getWidth(),baseLine);
//        g.setColor(Color.black);
    }

     @Override
    public void convertToCode(StringBuffer sb) {
        sb.append("sqrt( ");
        if ( getFirstChild() != null)
            getFirstChild().convertToCode(sb);
        sb.append(" )");
    }

      @Override
    public MathElement createFormula()
    {
        UnaryMathElement element = new UnaryMathElement();
        if ( getFirstChild() != null )
            element.setChild(getFirstChild().createFormula());
        element.setOperation(Operation.SQUAREROOT);
        return element;
    }

}
