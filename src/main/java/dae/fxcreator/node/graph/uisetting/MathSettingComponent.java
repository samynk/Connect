package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.node.events.IOListener;
import dae.fxcreator.node.settings.MathSetting;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

/**
 * Allows the user to create a math formula.
 * @author Koen
 */
public class MathSettingComponent extends JPanel implements IOListener, DocumentListener {
    //private MathSetting setting;

    private JLabel lblLabel;
    private JEditorPane txtMath;
    private MathSetting setting;

    public MathSettingComponent() {
        initComponents();
    }

    private void initComponents() {
        lblLabel = new JLabel(" : ");
        setLayout(new GridBagLayout());
        txtMath = new JEditorPane();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        add(lblLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.ipadx = 100;
        gbc.ipady = 50;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(txtMath), gbc);

        txtMath.getDocument().addDocumentListener(this);
        txtMath.getDocument().putProperty(PlainDocument.tabSizeAttribute, 2);
    }

    void setMathSetting(MathSetting mathSetting) {
        if (setting != null) {
            setting.getSettingNode().removeIOListener(this);
        }
        if (mathSetting != null) {
            mathSetting.getSettingNode().addIOListener(this);
            this.setting = mathSetting;
            txtMath.setText(setting.getSettingValue());
            //updateIONames();
        }
    }

    @Override
    public void removeNotify() {
        if ( setting != null ){
            setting.getSettingNode().removeIOListener(this);
        }
    }

    /*
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
    }*/

    @Override
    public void ioChanged(String oldName, String newName) {
        // updateIONames();
    }

    @Override
    public void ioRemoved(String name) {
        // updateIONames();
    }

    @Override
    public void ioAdded(String name) {
        // updateIONames();
    }
    
    @Override
    public void changedUpdate(DocumentEvent e) {
        updateSetting();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateSetting();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateSetting();
    }
    
    private void updateSetting() {
        String text = txtMath.getText();
        setting.setSettingValue(text);
    }
}
