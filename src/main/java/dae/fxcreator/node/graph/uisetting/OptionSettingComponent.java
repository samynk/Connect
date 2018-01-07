package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.templates.OptionSetting;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Koen
 */
public class OptionSettingComponent extends JPanel {
    private OptionSetting optionSetting;
    private JLabel label = new JLabel();
    private JComboBox cboOptions = new JComboBox();

    public OptionSettingComponent() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets =new Insets(3,0,0,0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(cboOptions,gbc);
    }

    public void setOptionSetting(OptionSetting setting){
        this.optionSetting = setting;
        label.setText(optionSetting.getLabel()+" : ");
        cboOptions.setModel(optionSetting);
    }
}
