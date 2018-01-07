package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.events.IOListener;
import dae.fxcreator.io.templates.MathSetting;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderOutput;
import dae.fxcreator.node.graph.math.MathEditor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Allows the user to create a math formula.
 * @author Koen
 */
public class MathSettingComponent extends JPanel implements ChangeListener, IOListener {
    //private MathSetting setting;

    private JLabel lblLabel;
    private MathEditor txtMath;
    private MathSetting setting;

    public MathSettingComponent() {
        lblLabel = new JLabel("Formulas : ");
        setLayout(new GridBagLayout());
        txtMath = new MathEditor();
        txtMath.setPreferredSize(new Dimension(400, 300));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.ipadx = 100;
        gbc.ipady = 50;
        gbc.fill = GridBagConstraints.BOTH;

        add(txtMath, gbc);

        txtMath.addChangeListener(this);
    }

    void setMathSetting(MathSetting mathSetting) {
        if (setting != null) {
            setting.getSettingNode().removeIOListener(this);
        }
        if (mathSetting != null) {
            mathSetting.getSettingNode().addIOListener(this);
            this.setting = mathSetting;
            txtMath.setMathFormula(setting.getMathFormula());
            updateIONames();
        }
    }

    @Override
    public void removeNotify() {
        txtMath.removeChangeListener(this);
        if ( setting != null ){
            setting.getSettingNode().removeIOListener(this);
        }
    }

    public void stateChanged(ChangeEvent e) {
        setting.setSettingValueAsObject(txtMath.getMathFormula());
        setting.setSettingValue(txtMath.getAsXML());
        //setting.setIcon(txtMath.getSnapShot());
    }

    private void updateIONames() {
        if (setting == null) {
            return;
        }
        IONode node = setting.getSettingNode();
        String[] inputs = new String[node.getNrOfInputs()];
        int i = 0;
        for (ShaderInput input : node.getInputs()) {
            inputs[i] = input.getName();
            ++i;
        }
        txtMath.addInputVariables(inputs);
        String[] outputs = new String[node.getNrOfOutputs()];
        i = 0;
        for (ShaderOutput output : node.getOutputs()) {
            outputs[i] = output.getName();
            ++i;
        }
        txtMath.addOutputVariables(outputs);
    }

    public void ioChanged(String oldName, String newName) {
        updateIONames();
    }

    public void ioRemoved(String name) {
        updateIONames();
    }

    public void ioAdded(String name) {
        updateIONames();
    }
}
