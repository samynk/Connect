package dae.fxcreator.node.graph;

import dae.fxcreator.node.project.FXProjectType;
import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.ShaderField;
import dae.fxcreator.node.gui.ImageLoader;
import java.awt.FlowLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This component visualizes the fields that are present in a struct.
 * @author Koen
 */
public class JField extends JPanel {
    private IONode parent;
    private final ShaderField field;
    private final JLabel semanticLabel;
    private final JLabel typeLabel;
    private final JLabel nameLabel;
    private final JLabel semiColumnLabel;

    /**
     * Creates a new JField object.
     * @param field the field to visualize in this component.
     */
    public JField(IONode parent, ShaderField field) {
        this.parent = parent;
        this.field = field;
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        typeLabel = new JLabel(field.getType().toString());
        add(typeLabel);
        
        nameLabel = new JLabel();
        add(nameLabel);

        semiColumnLabel = new JLabel(":");
        add(semiColumnLabel);

        semanticLabel = new JLabel();
        add(semanticLabel);

        syncWithModel();
        setOpaque(false);
    }

    /**
     * Synchronizes the user interface with the model.
     */
    public final void syncWithModel() {
        if (field == null)
            return;

        nameLabel.setText(field.getName());
        if ( field.getSemantic().isValid()){
            semanticLabel.setText(field.getSemantic().getValue());
            semiColumnLabel.setVisible(true);
            semanticLabel.setVisible(true);
        }else{
            semiColumnLabel.setVisible(false);
            semanticLabel.setVisible(false);
        }

        Image image = ImageLoader.getInstance().getImage(field.getType().getIcon());
        if (image != null) {
            typeLabel.setIcon(new ImageIcon(image));
            typeLabel.setText("");
        }
        repaint();
    }
}
