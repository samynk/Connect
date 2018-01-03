/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.templates.ColorSetting;
import dae.fxcreator.io.templates.Setting;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * A class that shows the value of a color setting without allowing to edit
 * the setting for use in JGraphNode
 * @author Koen
 */
public class ColorVisualizer extends JPanel implements Visualizer{
    private ColorSetting setting;

    /**
     * Creates a new empty ColorVisualizer object.
     */
    public ColorVisualizer(){
        setOpaque(true);
        setMinimumSize(new Dimension(64,64));
        setPreferredSize(new Dimension(64,64));
        setBorder(BorderFactory.createLineBorder(Color.yellow));
    }

    /**
     * Sets the color setting to display.
     * @param cs the color setting to display.
     */
    public void setSetting(ColorSetting cs){
        this.setting = cs;
        setBackground(cs.getColor());

    }
    @Override
    public void paintComponent(Graphics g){
        g.setColor(setting.getColor());
        g.fillRect(0,0,getWidth(),getHeight());
    }

    public Setting getSetting(){
        return setting;
    }

    public void updateVisual(){
        
    }
    
    public void updateVisual(Setting s){
        
    }
}
