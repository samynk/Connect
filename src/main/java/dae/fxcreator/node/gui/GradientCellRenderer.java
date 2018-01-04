package dae.fxcreator.node.gui;

import dae.fxcreator.node.graph.uisetting.GradientPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

/**
 *
 * @author Koen
 */
public class GradientCellRenderer implements ListCellRenderer{
    JPanel pnlComponent;
    JLabel lblText;
    GradientPanel gradientPanel;

    private Color selBackground;
    private Color selForeground;

    private Color background;
    private Color foreground;
    
    public GradientCellRenderer(){
        pnlComponent = new JPanel();
        pnlComponent.setLayout(new BorderLayout());
        lblText = new JLabel();
        gradientPanel = new GradientPanel();
        gradientPanel.setMinimumSize(new Dimension(50,1));
        gradientPanel.setPreferredSize(new Dimension(50,1));
        gradientPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

        pnlComponent.add(gradientPanel,BorderLayout.WEST);
        pnlComponent.add(lblText,BorderLayout.CENTER);

        selBackground = UIManager.getColor("List.selectionBackground");
        selForeground = UIManager.getColor("List.selectionForeground");

        background = UIManager.getColor("List.background");
        foreground = UIManager.getColor("List.foreground");

    }


    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        pnlComponent.setOpaque(isSelected);

        if ( isSelected){
            lblText.setForeground(selForeground);
            pnlComponent.setBackground(selBackground);
        } else{
            lblText.setForeground(foreground);
            pnlComponent.setBackground(background);
        }



        if ( value instanceof GraphGradient){
            GraphGradient gg = (GraphGradient)value;
            lblText.setText(gg.getName());
            gradientPanel.setColor1(gg.getC1());
            gradientPanel.setColor2(gg.getC2());
        }
        return pnlComponent;
    }

}
