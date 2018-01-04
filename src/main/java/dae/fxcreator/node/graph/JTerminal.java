package dae.fxcreator.node.graph;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 * This class is a graphical representation of an input or output;
 * @author Koen
 */
public class JTerminal extends JLabel
{
    private JConnectorPoint parent;
    private boolean selected;

    private Border selectionBorder;
    private MatteBorder normalBorder;
    private MatteBorder highlightBorder;

    private Color normalBorderColor;

    public JTerminal(){
        selectionBorder = BorderFactory.createMatteBorder(1,1,1,1,Color.blue);
        normalBorder = BorderFactory.createMatteBorder(1,1,1,1,Color.white);
        highlightBorder = BorderFactory.createMatteBorder(1,1,1,1,Color.yellow);
        setBorder(normalBorder);
    }

    /**
     * Sets the connector point that is the parent of this JTerminal object.
     * @param parent the JTerminal object.
     */
    public void setConnectorPoint(JConnectorPoint parent){
        this.parent = parent;
    }

    public void setNormalBorderColor(Color color){
        this.normalBorderColor = color;
    }



   /**
    * Returns the connector point that is the parent of this JTerminal object.
    * @return the parent of this object.
    */
    public JConnectorPoint getConnectorPoint(){
        return parent;
    }

    /**
     * Sets the selection state of this JTerminal object.
     * @param selected the new selection state for this JTerminal object.
     */
    void setSelected(boolean selected) {
        this.selected = selected;
        if ( selected){
            setBorder(selectionBorder);
        }else
            setBorder(normalBorder);
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    public void highlight() {
        setBorder(this.highlightBorder);
    }

    public void unhighlight(){
        setBorder(normalBorder);
    }
}
