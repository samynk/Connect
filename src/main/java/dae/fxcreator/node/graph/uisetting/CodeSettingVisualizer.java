package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.io.templates.CodeSetting;
import dae.fxcreator.io.templates.Setting;
import dae.fxcreator.node.gui.GraphFont;
import java.awt.Color;
import javax.swing.JLabel;


/**
 *
 * @author Koen
 */
public class CodeSettingVisualizer extends JLabel implements Visualizer {

    private GraphFont f;
    private CodeSetting setting;
    private String currentText;

    /**
     * Shows the float value.
     */
    public CodeSettingVisualizer() {
        f = FXSingleton.getSingleton().getFXSettings().getFont("constant");
        this.setFont(f.getFont());

        this.setForeground(f.getColor());
        this.setBackground(Color.black);
 
 
    }

    /**
     * Sets the float setting object to show.
     * @param setting
     */
    public void setSetting(CodeSetting setting) {
        this.setting = setting;
        currentText = setting.getFormattedValue();
        this.setText(currentText);
    }

    public Setting getSetting() {
        return setting;
    }

    public void updateVisual() {
        if (setting == null || setting.getFormattedValue() == null) {
            return;
        }
        String value = setting.getFormattedValue();
        if (setting.isLabelVisible()) {
            value = setting.getLabel() + " : " + value;
        }
        if (value != null && !value.equals(currentText)) {
            currentText = value;

            String htmlText = "<html>" + currentText;
            htmlText = htmlText.replace("\n", "<br>");
            this.setText(htmlText);
            setMinimumSize(this.getPreferredSize());
        }
    }
    
    public void updateVisual(Setting s){
        
    }
}
