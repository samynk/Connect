package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.node.settings.FloatSetting;
import dae.fxcreator.node.settings.Setting;
import dae.fxcreator.gui.model.GraphFont;
import java.awt.Graphics;
import javax.swing.JLabel;

/**
 *
 * @author Koen
 */
public class FloatVisualizer extends JLabel implements Visualizer {

    private FloatSetting setting;
    private GraphFont f;

    /**
     * Shows the float value.
     */
    public FloatVisualizer() {
        f = FXSingleton.getSingleton().getFXSettings().getFont("constant");
        this.setFont(f.getFont());
        //setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white));
    }

    /**
     * Sets the float setting object to show.
     *
     * @param setting
     */
    public void setSetting(FloatSetting setting) {
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
