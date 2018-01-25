package dae.fxcreator.node.graph.math;

import dae.fxcreator.node.graphmath.UnaryMathElement;
import dae.fxcreator.node.graphmath.Operation;
import dae.fxcreator.node.graphmath.MathElement;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;

/**
 *
 * @author Koen
 */
public class GroupMathContainer extends MathContainer{

    private GroupType type;

    public GroupMathContainer(MathContext context,GroupType type){
        super(context);
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,15,0,15);
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weightx = 1.0f;
        gbc.weighty = 0.0f;

        this.type = type;
        setIsGroup(true);
    }
    @Override
    public void paintComponent(Graphics g)
    {
        switch(type){
            case ABS: paintABS(g);break;
            case GROUP: paintGroup(g);break;
            case LENGTH: paintLength(g);break;
            case MATRIX: paintMatrix(g);break;
        }
    }

    @Override
    public int getBaseLine(){
        int baseLine = 0;
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

    private void paintABS(Graphics g){
        g.drawLine(5,2,5,getHeight()-4);
        g.drawLine(getWidth()-5,2,getWidth()-5,getHeight()-4);
    }

    private void paintGroup(Graphics g){
        g.drawArc( 5, 2, 7, getHeight()-4,90,180);
        g.drawArc( getWidth()-12, 2, 7, getHeight()-4,90,-180);
    }

    private void paintLength(Graphics g){
        g.drawLine(5,2,5,getHeight()-4);
        g.drawLine(8,2,8,getHeight()-4);

        g.drawLine(getWidth()-5,2,getWidth()-5,getHeight()-4);
        g.drawLine(getWidth()-8,2,getWidth()-8,getHeight()-4);
    }

    private void paintMatrix(Graphics g){
        g.drawLine(5,2,5,getHeight()-4);
        g.drawLine(5,2,12,2);
        g.drawLine(5,getHeight()-4,12,getHeight()-4);

        g.drawLine(getWidth()-5,2,getWidth()-5,getHeight()-4);
        g.drawLine(getWidth()-5,2,getWidth()-12,2);
        g.drawLine(getWidth()-5,getHeight()-4,getWidth()-12,getHeight()-4);
    }

     @Override
    public void convertToCode(StringBuffer sb){
        if ( type == GroupType.LENGTH){
            sb.append("length(");
            if ( getFirstChild() != null )
                getFirstChild().convertToCode(sb);
            sb.append(")");
        }else if ( type == GroupType.GROUP){
            sb.append("(");
            if ( getFirstChild() != null )
                getFirstChild().convertToCode(sb);
            sb.append(")");
        }
    }

     @Override
     public MathElement createFormula(){
         UnaryMathElement element = new UnaryMathElement();
         if ( getFirstChild() != null ){
            MathElement child = getFirstChild().createFormula();
            element.setChild(child);

         }
         element.setOperation( new Operation(type.name()));
         return element;
     }
}