/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.node.graph.renderers;

import dae.fxcreator.io.FXProject;
import dae.fxcreator.io.Pass;
import dae.fxcreator.io.ShaderStage;
import dae.fxcreator.io.Technique;
import dae.fxcreator.node.ShaderField;
import dae.fxcreator.node.ShaderStruct;
import dae.fxcreator.node.gui.ImageLoader;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Koen
 */
public class ShaderTreeCellRenderer extends DefaultTreeCellRenderer {
    private Icon imgProject;
    private Icon imgStruct;
    private Icon imgField;
    private Icon imgTechnique;
    private Icon imgPass;
    private Icon imgVertexShader;
    private Icon imgPixelShader;
    private Icon imgGeometryShader;


    public ShaderTreeCellRenderer() {
        ImageLoader loader = ImageLoader.getInstance();

        imgProject =  new ImageIcon(loader.getImage("/dae/images/tree/fx.png"));
        imgStruct = new ImageIcon(loader.getImage("/dae/images/tree/struct.png"));
        imgField = new ImageIcon(loader.getImage("/dae/images/tree/field.png"));
        imgTechnique = new ImageIcon(loader.getImage("/dae/images/tree/technique.png"));
        imgPass = new ImageIcon(loader.getImage("/dae/images/tree/pass.png"));
        imgVertexShader = new ImageIcon(loader.getImage("/dae/images/tree/vertexstage.png"));
        imgPixelShader = new ImageIcon(loader.getImage("/dae/images/tree/pixelstage.png"));
        imgGeometryShader = new ImageIcon(loader.getImage("/dae/images/tree/geometrystage.png"));
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JLabel label = (JLabel)super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if ( value instanceof FXProject){
            label.setIcon(imgProject);
        }else if ( value instanceof ShaderStruct){
            label.setIcon(imgStruct);
        }else if ( value instanceof ShaderField){
            label.setIcon(imgField);
        }else if (value instanceof Technique){
            label.setIcon(imgTechnique);
        }else if (value instanceof Pass){
            label.setIcon(imgPass);
        }else if (value instanceof ShaderStage){
            ShaderStage stage = (ShaderStage)value;
            String type = stage.getType();
            if ("vertex".equals(type) || type.equals("stages.vertex")){
                label.setIcon(imgVertexShader);
            }else if ("pixel".equals(type) || "stages.pixel".equals(type)){
                label.setIcon(imgPixelShader);
            }else if ("geometry".equals(type) || "stages.geometry".equals(type)){
                label.setIcon(imgGeometryShader);
            }
        }
        //String text = label.getText();
        //FontMetrics fm = label.getFontMetrics(label.getFont());
        //Dimension d = label.getPreferredSize();
        //label.setMinimumSize(new Dimension(fm.stringWidth(text),d.height));
        return label;
    }
}
