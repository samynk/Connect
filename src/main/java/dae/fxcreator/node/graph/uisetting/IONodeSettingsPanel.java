package dae.fxcreator.node.graph.uisetting;

import com.google.common.eventbus.Subscribe;
import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.io.templates.NodeTemplateLibrary;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.SettingsGroup;
import dae.fxcreator.node.event.NodeEvent;
import dae.fxcreator.node.graph.IOEditorPanel;
import dae.fxcreator.node.graph.JGraphNode;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Koen
 */
public class IONodeSettingsPanel extends JScrollPane {

    private final JPanel ioPanel = new JPanel();
    private final IOEditorPanel ioEditorPanel = new IOEditorPanel();

    public IONodeSettingsPanel() {
        init();
    }

    private final void init() {
        Dimension d = new Dimension(250, 600);
        setMinimumSize(d);
        setPreferredSize(d);
        setBorder(new TitledBorder("Settings"));

        ioPanel.setLayout(new GridBagLayout());
        setViewportView(ioPanel);
        FXSingleton.getSingleton().registerListener(this);
    }

    /**
     * Sets the node template library to use for this IONodeSettingsPanel.
     *
     * @param library the library to use.
     */
    public void setNodeTemplateLibrary(NodeTemplateLibrary library) {
        ioEditorPanel.setNodeTemplateLibrary(library);
    }

    /**
     * Sets the IONode to show in this settings panel.
     *
     * @param node the node to show.
     */
    public void setIONode(IONode node) {
        ioPanel.removeAll();
        List<SettingsGroup> settingsGroups = node.getSettingsGroups();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;

        for (SettingsGroup group : settingsGroups) {
            SettingsGroupPanel panel = new SettingsGroupPanel();
            panel.setBorder(new TitledBorder(group.getName()));
            panel.setSettingsGroup(group);
            //panel.setMinimumSize(new Dimension(240,1));
            System.out.println(group.getName() + ":" + panel.getPreferredSize());
            ioPanel.add(panel, gbc);
            gbc.gridy++;
        }

        if (node.isInputOutputEditable()) {
            // todo ioEditorPanel should accept an IONode object.
            ioEditorPanel.setIONode(node);
            ioEditorPanel.invalidate();
            
            ioPanel.add(ioEditorPanel,gbc);

        }
        gbc.weighty = 1.0;
        ioPanel.add(new JLabel(), gbc);
        ioPanel.invalidate();
        this.validate();
    }

    public void setGraphNode(JGraphNode node) {
        if (node.getUserObject() instanceof IONode) {
            setIONode((IONode) node.getUserObject());

        }
        IONode n = (IONode) node.getUserObject();

//        Dimension d = this.getPreferredScrollableViewportSize();
//        System.out.println("Dimension: " + d);
//        this.setPreferredSize( d );
    }

    public @Subscribe
    void nodeEvent(NodeEvent event) {
        if (event.getType() == NodeEvent.Type.SELECTED) {
            System.out.println("selected: " + event.getNode().getName());
            setIONode(event.getNode());
        }
    }
}
