/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.io.templates.IntSetting;
import dae.fxcreator.io.templates.Setting;
import dae.fxcreator.node.gui.GraphFont;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JLabel;

/**
 *
 * @author Koen
 */
public class IntVisualizer extends JLabel implements Visualizer {

    private IntSetting setting;
    private GraphFont f;
    private Dimension size = new Dimension();

    /**
     * Shows the float value.
     */
    public IntVisualizer() {
        f = FXSingleton.getSingleton().getFXSettings().getFont("constant");
        this.setFont(f.getFont());
        //setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white));
    }

    /**
     * Sets the float setting object to show.
     *
     * @param setting
     */
    public void setSetting(IntSetting setting) {
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