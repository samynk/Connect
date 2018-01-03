package dae.fxcreator.node.graph;

import dae.fxcreator.io.FXProjectType;
import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.node.ShaderField;
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
    private final ShaderField field;
    private final JLabel semanticLabel;
    private final JLabel typeLabel;
    private final JLabel nameLabel;
    private final JLabel semiColumnLabel;

    /**
     * Creates a new JField object.
     * @param field the field to visualize in this component.
     */
    public JField(ShaderField field) {
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

        FXProjectType current = FXSingleton.getSingleton().getCurrentProjectType();
        Image image = current.getIconForType(field.getType());
        if (image != null) {
            typeLabel.setIcon(new ImageIcon(image));
            typeLabel.setText("");
        }
        repaint();
    }
}
