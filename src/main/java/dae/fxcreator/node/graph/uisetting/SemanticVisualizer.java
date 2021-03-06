package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.node.settings.SemanticSetting;
import dae.fxcreator.node.settings.Setting;
import dae.fxcreator.gui.model.GraphFont;
import java.awt.Graphics;
import javax.swing.JLabel;

/**
 * This class visualizes a semantic.
 *
 * @author Koen
 */
public class SemanticVisualizer extends JLabel implements Visualizer {

    private GraphFont f;
    private SemanticSetting setting;

    /**
     * Shows the float value.
     */
    public SemanticVisualizer() {
        f = FXSingleton.getSingleton().getFXSettings().getFont("semantic");
        this.setFont(f.getFont());
        //setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white));
    }

    /**
     * Sets the float setting object to show.
     *
     * @param setting
     */
    public void setSetting(SemanticSetting setting) {
        this.setting = setting;
        this.setText(setting.getFormattedValue());
    }

    /**
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {

        String value = setting.getFormattedValue();
        if (setting.isLabelVisible()) {
            value = setting.getLabel() + " : " + value;
        }
        if (!value.equals(this.getText())) {
            this.setText(value);
        }
        super.paintComponent(g);
    }

    public Setting getSetting() {
        return setting;
    }

    public void updateVisual() {
    }

    public void updateVisual(Setting s) {
    }
}
