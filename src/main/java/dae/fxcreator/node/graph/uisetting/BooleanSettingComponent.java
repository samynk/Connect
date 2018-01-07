package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.templates.BooleanSetting;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Koen
 */
public class BooleanSettingComponent extends JPanel implements ItemListener {

    private JLabel[] lblBooleans;
    private JCheckBox[] cboBooleans;
    private final String[] labels = {"x", "y", "z", "w"};
    private BooleanSetting setting;

    /**
     * Creates a new float setting component.
     */
    public BooleanSettingComponent() {
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
    }

    /**
     * Sets the setting for this FloatSetingComponent object.
     * @param setting the setting for this FloatSettingComponent object.
     */
    public void setBooleanSetting(BooleanSetting setting) {
        this.setting = setting;
        int nrOfBooleans = setting.getNrOfBooleans();
        GridBagConstraints gbc = new GridBagConstraints();
        lblBooleans = new JLabel[nrOfBooleans];
        cboBooleans = new JCheckBox[nrOfBooleans];
        //JLabel lblTitle = new JLabel(setting.getLabel() + ":");
        gbc.insets = new Insets(3, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        //add(lblTitle, gbc);
        gbc.gridwidth = 1;
        if (nrOfBooleans <= 4) {
            for (int i = 0; i < nrOfBooleans; i++) {
                gbc.gridx = 1;
                gbc.gridy = i + 1;
                gbc.weightx = 0.0;
                if (nrOfBooleans == 1) {
                    cboBooleans[i] = new JCheckBox(setting.getLabel(),setting.getBoolean(i));

                } else {
                    cboBooleans[i] = new JCheckBox(labels[i] + " : ",setting.getBoolean(i));
                }
                add(cboBooleans[i], gbc);
                cboBooleans[i].setName(""+i);
                cboBooleans[i].addItemListener(this);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        int id = Integer.parseInt( ((Component)e.getSource()).getName() );
        boolean value = e.getStateChange() == ItemEvent.SELECTED;
        setting.setBoolean(id,value);
    }
}
