/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.templates.FloatSetting;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Koen
 */
public class FloatSettingComponent extends JPanel implements ChangeListener {

    private JLabel[] lblFloats;
    private JSpinner[] txtFloats;
    private String[] labels = {"x", "y", "z", "w"};
    private FloatSetting setting;

    /**
     * Creates a new float setting component.
     */
    public FloatSettingComponent() {
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
    }

    /**
     * Sets the setting for this FloatSetingComponent object.
     * @param setting the setting for this FloatSettingComponent object.
     */
    public void setFloatSetting(FloatSetting setting) {
        this.setting = setting;
        int nrOfFloats = setting.getNrOfFloats();
        GridBagConstraints gbc = new GridBagConstraints();
        lblFloats = new JLabel[nrOfFloats];
        txtFloats = new JSpinner[nrOfFloats];
        //JLabel lblTitle = new JLabel(setting.getLabel() + ":");
        gbc.insets = new Insets(3, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        //add(lblTitle, gbc);
        gbc.gridwidth = 1;
        if (nrOfFloats <= 4) {
            for (int i = 0; i < nrOfFloats; i++) {
                gbc.gridx = 1;
                gbc.gridy = i + 1;
                gbc.weightx = 0.0;
                if (nrOfFloats == 1) {
                    lblFloats[i] = new JLabel(setting.getLabel() + " : ");
                } else {
                    lblFloats[i] = new JLabel(labels[i] + " : ");
                }
                add(lblFloats[i], gbc);
                gbc.gridx = 2;
                gbc.weightx = 1.0;

                SpinnerNumberModel model = new SpinnerNumberModel();
                model.setValue(setting.getFloat(i));
                txtFloats[i] = new JSpinner(model);
                txtFloats[i].addChangeListener(this);
                txtFloats[i].setName(Integer.toString(i));
                add(txtFloats[i], gbc);
            }
        }
    }

    public void stateChanged(ChangeEvent e) {
        JSpinner source = (JSpinner) e.getSource();
        int index = Integer.parseInt(source.getName());

        Float value = (Float) source.getModel().getValue();
        setting.setFloat(index, value);

    }
}
