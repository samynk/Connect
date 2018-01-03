/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.io.templates.BooleanSetting;
import dae.fxcreator.io.templates.Setting;
import dae.fxcreator.node.gui.GraphFont;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

/**
 *
 * @author Koen
 */
public class BooleanVisualizer extends JCheckBox implements Visualizer{

    private BooleanSetting setting;
    private GraphFont f;

    /**
     * Shows the float value.
     */
    public BooleanVisualizer() {
        f = FXSingleton.getSingleton().getFXSettings().getFont("constant");
        this.setFont(f.getFont());
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white));
        setEnabled(false);
        setOpaque(false);
    }

    /**
     * Sets the float setting object to show.
     * @param setting
     */
    public void setSetting(BooleanSetting setting) {
        this.setting = setting;
        this.setText(setting.getLabel());
        this.setSelected(setting.getBoolean(0));
    }

    @Override
    public void paintComponent(Graphics g) {
        this.setSelected(setting.getBoolean(0));
        super.paintComponent(g);
    }

    public Setting getSetting(){
        return setting;
    }

    public void updateVisual(){
    
    }
    
    public void updateVisual(Setting s){
        
    }
}

