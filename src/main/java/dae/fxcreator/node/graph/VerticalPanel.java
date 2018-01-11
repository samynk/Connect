package dae.fxcreator.node.graph;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class VerticalPanel extends JPanel {

    private final ArrayList<Component> northComponents = new ArrayList<>();

    private final ArrayList<Component> southComponents = new ArrayList<>();
    private final GridBagConstraints constraints = new GridBagConstraints();

    private final JLabel spacer = new JLabel();

    /**
     * Creates a new VerticalPanel with the provided anchor location.
     *
     * @param anchor the anchor, as defined in the GridBagConstraints object.
     */
    public VerticalPanel(int anchor) {
        initComponents();
        constraints.anchor = anchor;
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
    }

    public void addNorthComponent(Component c) {
        northComponents.add(c);
        updateLayout();
    }

    public void addSouthComponent(Component c) {
        southComponents.add(c);
        updateLayout();
    }

    public void updateLayout() {
        removeAll();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        for (Component c : northComponents) {
            add(c, constraints);
            constraints.gridy++;
        }

        constraints.weighty = 1.0;
        add(spacer, constraints);
        constraints.weighty = 0;

        for (Component c : southComponents) {
            constraints.gridy++;
            add(c, constraints);
        }
        invalidate();
    }
}
