package dae.fxcreator.node.graph.menus;

import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.node.project.Pass;
import dae.fxcreator.node.project.ShaderStage;
import dae.fxcreator.io.events.StageListener;
import dae.fxcreator.node.templates.NodeTemplate;
import dae.fxcreator.node.templates.NodeTemplateLibrary;
import dae.fxcreator.node.ShaderNode;
import dae.fxcreator.node.graph.TechniqueEditorPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

/**
 * The menu for the TechniquePanel.
 * This menu allows to user to insert an existing shader stage into
 * a pass, or to create a standard shader stage.
 * @author Koen
 */
public class TechniquePanelMenu extends JPopupMenu implements StageListener, ActionListener {

    private FXProject project;
    private JMenu vertexStageMenu;
    private JMenu geometryStageMenu;
    private JMenu pixelStageMenu;
    private JMenuItem newVertexStage;
    private JMenuItem newGeometryStage;
    private JMenuItem newPixelStage;
    private JMenuItem newRasterizerState;
    private NodeTemplateLibrary library;
    private TechniqueEditorPanel techniquePanel;

    private Pass currentPass;

    /**
     * Creates a new TechniquePanel menu.
     */
    public TechniquePanelMenu(TechniqueEditorPanel panel) {
        vertexStageMenu = new JMenu("Insert vertex stage");
        geometryStageMenu = new JMenu("Insert geometry stage");
        pixelStageMenu = new JMenu("Insert pixel stage");

        add(vertexStageMenu);
        add(geometryStageMenu);
        add(pixelStageMenu);
        add(new JSeparator());
        newVertexStage = new JMenuItem("New vertex stage");
        newGeometryStage = new JMenuItem("New geometry stage");
        newPixelStage = new JMenuItem("New pixel stage");

        newVertexStage.addActionListener(new NewVertexStageAction());
        newGeometryStage.addActionListener(new NewGeometryStageAction());
        newPixelStage.addActionListener(new NewPixelStageAction());

        newRasterizerState = new JMenuItem("New Rasterizer state");
        newRasterizerState.addActionListener(new NewRasterizerStateAction());
        add(newRasterizerState);

        this.techniquePanel = panel;
    }

    public void setCurrentPass(Pass pass){
        this.currentPass = pass;
    }

    private void removeListeners(JMenu menu) {
        int count = menu.getItemCount();
        for (int i = 0; i < count; ++i) {
            menu.getItem(i).removeActionListener(this);
        }
    }

    private void refreshMenuItems() {
        removeListeners(vertexStageMenu);
        removeListeners(geometryStageMenu);
        removeListeners(pixelStageMenu);

        vertexStageMenu.removeAll();
        geometryStageMenu.removeAll();
        pixelStageMenu.removeAll();
        
        vertexStageMenu.add(newVertexStage);
        geometryStageMenu.add(newGeometryStage);
        pixelStageMenu.add(newPixelStage);

    }

    /**
     * Sets the project , to be able to query the list of shader stages.
     * @param project the project with the information about the shaderstages.
     */
    public void setProject(FXProject project) {
        if (this.project != null) {
            project.removeStageListener(this);
        }
        this.project = project;
        if (this.project != null) {
            project.addStageListener(this);
            refreshMenuItems();

            for (ShaderStage stage : project.getStages()) {
                addStage(stage);
            }
        }
    }

    private void addStageToMenu(JMenu menu, ShaderStage stage) {
        JMenuItem item = new JMenuItem(stage.getId());
        item.setActionCommand(stage.getId());
        item.addActionListener(this);
        menu.add(item);
    }

    /**
     * Adds a stage to the correct menu.
     */
    private void addStage(ShaderStage stage) {
        if (stage.getType().equals("pixel")) {
            addStageToMenu(pixelStageMenu, stage);
        } else if (stage.getType().equals("vertex")) {
            addStageToMenu(vertexStageMenu, stage);
        } else if (stage.getType().equals("geometry")) {
            addStageToMenu(geometryStageMenu, stage);
        }
    }

    private void removeStageFromMenu(JMenu menu, ShaderStage stage) {
        for (int i = 0; i < menu.getItemCount(); ++i) {
            JMenuItem item = menu.getItem(i);
            if (stage.getId().equals(item.getActionCommand())) {
                menu.remove(item);
                item.removeActionListener(this);
            }
        }
    }

    private void removeStage(ShaderStage stage) {
        if (stage.getType().equals("pixel")) {
            removeStageFromMenu(pixelStageMenu, stage);
        } else if (stage.getType().equals("vertex")) {
            removeStageFromMenu(vertexStageMenu, stage);
        } else if (stage.getType().equals("geometry")) {
            removeStageFromMenu(geometryStageMenu, stage);
        }
        project.removeShaderStage(currentPass,stage, true);
    }

    /**
     * Called when a new stage was added to the FXProject.
     * @param project the project the stage was added to.
     * @param stage the added stage.
     */
    public void stageAdded(FXProject project, ShaderStage stage) {
        addStage(stage);
    }

    /**
     * Called when a stage was removed from the FXProject.
     * @param project the project the stage was removed from.
     * @param stage the removed stage.
     */
    public void stageRemoved(FXProject project, ShaderStage stage) {
        removeStage(stage);
    }

    /**
     * Called when a menu item was clicked.
     * @param ae the event object.
     */
    public void actionPerformed(ActionEvent ae) {
    }

    /**
     * Sets the library that will help to create new shader stages.
     * @param library
     */
    public void setLibrary(NodeTemplateLibrary library) {
        this.library = library;
    }

    public void addShaderStage(NodeTemplate template) {
        ShaderStage stage = template.createShaderStage(project);
        String newid = project.createUniqueShaderStageName(stage.getId());
        stage.setId(newid);
        stage.setName(newid);
        project.addShaderStage(stage);
        techniquePanel.getPass().addShaderStage(stage);
        techniquePanel.reloadPass();
    }

    /**
     * Action that creates a new vertex stage.
     */
    class NewVertexStageAction implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            NodeTemplate template = library.getNodeTemplate("stages.vertex");
            addShaderStage(template);
        }
    }

    /**
     *  Action that creates a new pixel stage.
     */
    class NewPixelStageAction implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            NodeTemplate template = library.getNodeTemplate("stages.pixel");
            addShaderStage(template);
        }
    }

    /**
     * Action that creates a new geometry stage.
     */
    class NewGeometryStageAction implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            NodeTemplate template = library.getNodeTemplate("stages.geometry");
            addShaderStage(template);
        }
    }

    /**
     * Action that creates a new geometry stage.
     */
    class NewRasterizerStateAction implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            NodeTemplate template = library.getNodeTemplate("stages.rasterizerstate");
            ShaderNode state = template.createShaderNode(project);
            String newid = project.createUniqueStateName(state.getId());
            state.setId(newid);
            state.setName(newid);
            techniquePanel.getPass().setRasterizerState(state);
            //project.addShaderStage(state);
            //techniquePanel.getPass().addShaderStage(stage);
            techniquePanel.reloadPass();
            project.addState(state);
        }
    }
}
