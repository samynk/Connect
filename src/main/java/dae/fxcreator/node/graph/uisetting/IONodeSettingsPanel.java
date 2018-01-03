/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.templates.NodeTemplateLibrary;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.SettingsGroup;
import dae.fxcreator.node.graph.IOEditorPanel;
import dae.fxcreator.node.graph.JGraphNode;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JPanel;
/**
 *
 * @author Koen
 */
public class IONodeSettingsPanel extends JPanel {

    private final JPanel ioPanel;
    private final IOEditorPanel ioEditorPanel;

    public IONodeSettingsPanel() {

        //Dimension d = new Dimension(250, 600);
        //setMinimumSize(d);
        //setPreferredSize(d);

        System.out.println(this.getLayout());

        ioPanel = new JPanel();
        ioPanel.setLayout(new BorderLayout());
        //ioPanel.setTitle("Input / Output");
        //ioPanel.setCollapsed(true);

        ioEditorPanel = new IOEditorPanel();
        ioPanel.add(ioEditorPanel, BorderLayout.CENTER);
        this.add(ioPanel);
        
        //this.setScrollableTracksViewportHeight(true);
        //this.setScrollableTracksViewportWidth(true);
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
        this.removeAll();
        List<SettingsGroup> settingsGroups = node.getSettingsGroups();
        for (SettingsGroup group : settingsGroups) {
            SettingsGroupPanel panel = new SettingsGroupPanel();
            panel.setSettingsGroup(group);

            JPanel pane = new JPanel();
            pane.setLayout(new BorderLayout());
            //pane.setTitle(group.getName());
            //pane.setCollapsed(false);
            pane.add(panel);
            this.add(pane);
        }
       
        //System.out.println("Dimension: " + d);
        //this.setPreferredSize( d );
        
    }

    public void setGraphNode(JGraphNode node) {
        if (node.getUserObject() instanceof IONode) {
            setIONode((IONode) node.getUserObject());

        }
        IONode n = (IONode) node.getUserObject();
        if (n.isInputOutputEditable()) {
            ioEditorPanel.setIONode(node);
            this.add(ioPanel);

        }
//        Dimension d = this.getPreferredScrollableViewportSize();
//        System.out.println("Dimension: " + d);
//        this.setPreferredSize( d );
    }
}
