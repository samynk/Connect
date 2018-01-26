package dae.fxcreator.node.graph.renderers;

import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.node.project.Pass;
import dae.fxcreator.node.project.ShaderStage;
import dae.fxcreator.node.project.Technique;
import dae.fxcreator.node.ShaderField;
import dae.fxcreator.node.ShaderStruct;
import dae.fxcreator.gui.model.ImageLoader;
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
    private final Icon imgProject;
    private final Icon imgStruct;
    private final Icon imgField;
    private final Icon imgTechnique;
    private final Icon imgPass;
    private final Icon imgVertexShader;
    private final Icon imgPixelShader;
    private final Icon imgGeometryShader;


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
        return label;
    }
}
