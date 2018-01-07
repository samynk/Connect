package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.templates.ColorSetting;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Allows the user to set the color for a constant.
 * @author Koen
 */
public class ColorSettingComponent extends JPanel implements ActionListener{
    private JLabel lblLabel;
    private JPanel pnlColor;

    private ColorSetting setting;
    /**
     * Creates a new ColorSettingComponent object.
     */
    public ColorSettingComponent(){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        pnlColor = new JPanel();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx=1;
        gbc.weightx=1.0;
        gbc.ipadx = 50;
        gbc.insets.top= 2;
        gbc.insets.bottom = 2;
        gbc.insets.left = 2;
        gbc.insets.right = 2;
        add(pnlColor,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx=2;
        gbc.weightx=0.0;
        gbc.ipadx = 0;
        gbc.insets.top= 0;
        gbc.insets.bottom = 0;
        gbc.insets.left = 2;
        gbc.insets.right = 2;
        JButton change = new JButton("...");
        change.addActionListener(this);
        add(change,gbc);

        pnlColor.setOpaque(true);
    }

    /**
     * Sets the color
     * @param colorSetting the color setting to set as model.
     */
    public void setColorSetting(ColorSetting colorSetting){
        lblLabel.setText(colorSetting.getLabel()+" : ");
        this.setting = colorSetting;
        pnlColor.setBackground(colorSetting.getColor());
        pnlColor.repaint();
    }

    /**
     * Called when the color changer was selected.
     * @param e the ActionEvent object for the color changer.
     */
    public void actionPerformed(ActionEvent e) {
        Color selected = JColorChooser.showDialog(this, "select color",setting.getColor());
        if ( selected != null){
            setting.setColor(selected);
            pnlColor.setBackground(selected);
            pnlColor.repaint();
        }
    }
}
