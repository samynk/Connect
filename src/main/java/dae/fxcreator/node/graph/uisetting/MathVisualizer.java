package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.node.settings.MathSetting;
import dae.fxcreator.node.settings.Setting;
import dae.fxcreator.node.graphmath.MathFormula;
import dae.fxcreator.node.graph.math.MathFormulaRenderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author Koen
 */
public class MathVisualizer extends JPanel implements Visualizer {

    private MathSetting setting;
    private MathFormulaRenderer renderer = new MathFormulaRenderer();

    public MathVisualizer() {
        setOpaque(true);
        setMinimumSize(new Dimension(128, 64));
        setPreferredSize(new Dimension(256, 128));
        setBorder(BorderFactory.createLineBorder(Color.yellow));
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting s) {
        this.setting = (MathSetting) s;
    }

    public void updateVisual() {
    }
    
    public void updateVisual(Setting s){
        this.setting = (MathSetting)s;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        if (setting != null ) {
            MathFormula formula =(MathFormula) setting.getMathFormula();
            if ( formula == null){
                System.out.println("Formula is null!!");
                return;
            }
            renderer.render(formula, (Graphics2D) g);
//            Image image = setting.getImage();
//            int width = getWidth();
//            int height = getHeight();
//            int iwidth = image.getWidth(this);
//            int iheight = image.getHeight(this);
//            if (iwidth > width || iheight > height) {
//                this.setPreferredSize(new Dimension(iwidth,iheight));
//                this.invalidate();
//            }
//            System.out.println("Drawing at : " + width + "," + height);
//            System.out.println("Image size : " + iwidth + "," + iheight);
//            g.drawImage(setting.getImage(), 0, 0,this);
        }
    }
}
