package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.templates.IntSetting;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Allows the user to set an int.
 * @author Koen
 */
public class IntSettingComponent extends JPanel implements ChangeListener {

    private JLabel[] lblFloats;
    private JSpinner[] txtFloats;
    private String[] labels = {"x", "y", "z", "w"};
    private IntSetting setting;

    /**
     * Creates a new float setting component.
     */
    public IntSettingComponent() {
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
    }

    /**
     * Sets the setting for this FloatSetingComponent object.
     * @param setting the setting for this FloatSettingComponent object.
     */
    public void setIntSetting(IntSetting setting) {
        this.setting = setting;
        int nrOfInts = setting.getNrOfInts();
        GridBagConstraints gbc = new GridBagConstraints();
        lblFloats = new JLabel[nrOfInts];
        txtFloats = new JSpinner[nrOfInts];
        //JLabel lblTitle = new JLabel(setting.getLabel() + ":");
        gbc.insets = new Insets(3, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        //add(lblTitle, gbc);
        gbc.gridwidth = 1;
        if (nrOfInts <= 4) {
            for (int i = 0; i < nrOfInts; i++) {
                gbc.gridx = 1;
                gbc.gridy = i + 1;
                gbc.weightx = 0.0;
                if (nrOfInts == 1) {
                    lblFloats[i] = new JLabel(setting.getLabel() + " : ");
                } else {
                    lblFloats[i] = new JLabel(labels[i] + " : ");
                }

                add(lblFloats[i], gbc);
                gbc.gridx = 2;
                gbc.weightx = 1.0;
                SpinnerNumberModel model = new SpinnerNumberModel();
                model.setValue(setting.getInt(i));
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

        int parseable = (Integer) source.getModel().getValue();
        setting.setInt(index, parseable);
    }
}
