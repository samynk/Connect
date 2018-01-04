package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.io.templates.IntSetting;
import dae.fxcreator.io.templates.OptionSetting;
import dae.fxcreator.io.templates.Setting;
import dae.fxcreator.node.gui.GraphFont;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 *
 * @author Koen
 */
public class OptionVisualizer extends JLabel implements Visualizer {

    private OptionSetting setting;
    private GraphFont f;
    private Dimension size = new Dimension();

    /**
     * Shows the float value.
     */
    public OptionVisualizer() {
        f = FXSingleton.getSingleton().getFXSettings().getFont("constant");
        this.setFont(f.getFont());
        //setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white));
    }

    /**
     * Sets the float setting object to show.
     *
     * @param setting
     */
    public void setSetting(OptionSetting setting) {
        this.setting = setting;
        this.setText(setting.getSettingValue());
    }

    /**
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {

        String value = setting.getSettingValue();
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