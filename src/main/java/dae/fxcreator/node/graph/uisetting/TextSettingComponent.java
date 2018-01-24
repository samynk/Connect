package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.node.settings.TextSetting;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Allows a text value to be set.
 * @author Koen
 */
public class TextSettingComponent extends JPanel implements DocumentListener,FocusListener{
    private TextSetting textSetting;
    private JTextField field = new JTextField();
    private JLabel label = new JLabel();

    /**
     * Creates a new TextSetting component.
     */
    public TextSettingComponent(){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(field,gbc);
        field.getDocument().addDocumentListener(this);
        field.addFocusListener(this);
    }

    /**
     * Sets the text setting associated with this TextSettingComponent.
     * @param setting the TextSetting to set.
     */
    public void setTextSetting(TextSetting setting){
        this.textSetting = setting;
        label.setText(textSetting.getId() + " : " );
        field.setText(textSetting.getValue());
    }

    public void insertUpdate(DocumentEvent e) {
        textSetting.setValue(field.getText());
    }

    public void removeUpdate(DocumentEvent e) {
        textSetting.setValue(field.getText());
    }

    public void changedUpdate(DocumentEvent e) {
        textSetting.setValue(field.getText());
    }

    public void focusGained(FocusEvent e) {
        field.selectAll();
    }

    public void focusLost(FocusEvent e) {
        
    }
}
