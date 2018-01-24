package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.node.settings.CodeSetting;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

/**
 * A component that allows the user to edit the code for a custom code node.
 * @author Koen
 */
public class CodeSettingComponent extends JPanel implements DocumentListener {

    private CodeSetting setting;
    private JLabel lblLabel;
    private JEditorPane txtCode;

    public CodeSettingComponent() {
        lblLabel = new JLabel(" : ");
        setLayout(new GridBagLayout());
        txtCode = new JEditorPane();

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
        add(new JScrollPane(txtCode), gbc);

        txtCode.getDocument().addDocumentListener(this);
        txtCode.getDocument().putProperty(PlainDocument.tabSizeAttribute, 2);
    }

    public void setCodeSetting(CodeSetting setting) {
        lblLabel.setText(setting.getLabel());
        this.setting = setting;
        txtCode.setText(setting.getSettingValue());


    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        setting.setSettingValue(txtCode.getText());
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        String text = txtCode.getText();
        setting.setSettingValue(text);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        String text = txtCode.getText();
        setting.setSettingValue(text);
    }
}
