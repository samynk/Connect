package dae.fxcreator.node.graph;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Helper class that lays out component in a vertical or horizontal box
 * while using the minimum width for horizontal layouts, and the minimum height
 * for vertical layouts.
 * @author Koen
 */
public class BoxPanel extends JPanel{
    int axis;
    private JLabel last;
    private GridBagConstraints lastgbc;
    public BoxPanel(int axis){
        setLayout(new GridBagLayout());
        this.axis = axis;
        last = new JLabel();
        lastgbc = new GridBagConstraints();
        
        if ( axis == BoxLayout.X_AXIS){
            lastgbc.gridx = GridBagConstraints.RELATIVE;
            lastgbc.weightx=1.0;
        }else if ( axis == BoxLayout.Y_AXIS){
            lastgbc.fill = GridBagConstraints.BOTH;
            lastgbc.weighty=1.0;
            lastgbc.gridy = GridBagConstraints.RELATIVE;
        }
        //last.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.yellow));
        add(last,lastgbc);
        
    }

    @Override
    public Component add(Component c){
        GridBagConstraints gbc = new GridBagConstraints();
        if ( axis == BoxLayout.X_AXIS){
            gbc.gridx = this.getComponentCount();
            gbc.fill = GridBagConstraints.VERTICAL;
            gbc.weightx = 0.0;
            lastgbc.gridx = this.getComponentCount()+1;
        }else{
            gbc.gridy = this.getComponentCount();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.weighty= 0.0;
            lastgbc.gridy = this.getComponentCount()+1;
        }
        remove(last); 
        super.add(c,gbc);
        super.add(last,lastgbc);
        this.invalidate();
        return c;
    }

    @Override
    public void removeAll(){
        super.removeAll();
        lastgbc = new GridBagConstraints();

        if ( axis == BoxLayout.X_AXIS){
            lastgbc.gridx = GridBagConstraints.RELATIVE;
            lastgbc.weightx=1.0;
        }else if ( axis == BoxLayout.Y_AXIS){
            lastgbc.weighty=1.0;
            lastgbc.gridy = GridBagConstraints.RELATIVE;
        }
        add(last,lastgbc);
    }
}
