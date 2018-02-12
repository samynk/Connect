package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.node.settings.MathSetting;
import dae.fxcreator.node.settings.Setting;
import dae.fxcreator.node.graphmath.MathFormula;
import dae.fxcreator.node.graph.math.MathFormulaRenderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author Koen
 */
public class MathVisualizer extends JPanel implements Visualizer {

    private MathSetting setting;
    private final MathFormulaRenderer renderer = new MathFormulaRenderer();

    public MathVisualizer() {
        init();
    }

    private void init() {
        setOpaque(true);
        setMinimumSize(new Dimension(128, 64));
        setPreferredSize(new Dimension(256, 128));
        setBorder(BorderFactory.createLineBorder(Color.yellow));
    }

    @Override
    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting s) {
        this.setting = (MathSetting) s;
    }

    @Override
    public void updateVisual() {
        System.out.println("updating visual");
        MathFormula formula = (MathFormula) setting.getMathFormula();
        if (formula == null) {
            return;
        }
        Dimension d  = renderer.calculateSize(formula, (Graphics2D)this.getGraphics());
        System.out.println("New dimension is:" + d);
        this.invalidate();
        this.getParent().getParent().revalidate();
        this.getParent().getParent().repaint();
    }
    
    @Override
    public Dimension getPreferredSize(){
        MathFormula formula = (MathFormula) setting.getMathFormula();
        if (formula == null || getGraphics() == null ) {
            return super.getPreferredSize();
        }else{
            return renderer.calculateSize(formula, (Graphics2D)this.getGraphics());
        }
    }

    @Override
    public void updateVisual(Setting s) {
        this.setting = (MathSetting) s;
        updateVisual();
    }

    @Override
    public void paint(Graphics g) {
        if (setting != null) {
            MathFormula formula = (MathFormula) setting.getMathFormula();
            if (formula == null) {
                return;
            }
            renderer.render(formula, (Graphics2D) g);

        }
    }
}
