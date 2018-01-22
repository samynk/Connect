package dae.fxcreator.ui;

import com.google.common.eventbus.Subscribe;
import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.io.FXProjectTemplate;
import dae.fxcreator.io.FXProjectTemplateGroup;
import dae.fxcreator.io.FXProjectTemplates;
import dae.fxcreator.node.project.FXProjectType;
import dae.fxcreator.io.FXSettings;
import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.io.PathUtil;
import dae.fxcreator.io.codegen.ExportTask;
import dae.fxcreator.io.codegen.parser.TemplateClassLibrary;
import dae.fxcreator.io.loaders.FXProjectTemplateLoader;
import dae.fxcreator.io.loaders.FXProjectTypeLoader;
import dae.fxcreator.io.loaders.FXSettingLoader;
import dae.fxcreator.io.savers.FXProjectSaver;
import dae.fxcreator.ui.actions.ExportAction;
import dae.fxcreator.ui.actions.ExportProjectEvent;
import dae.fxcreator.ui.actions.NewProjectEvent;
import dae.fxcreator.ui.actions.ProjectTemplateAction;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultTreeModel;

/**
 * This is the main user interface for designing visual application.
 *
 * @author Koen Samyn
 */
public class FXCreator2 extends javax.swing.JFrame {

    /**
     * The current project.
     */
    private FXProject project;
    /**
     * The file chooser
     */
    private javax.swing.JFileChooser fileChooser;

    /**
     * Creates new form FXCreator2
     */
    public FXCreator2() {
        initComponents();
        FXSettingLoader fsl = new FXSettingLoader();
        FXSettings settings = fsl.load();
        FXSingleton.getSingleton().setFxSettings(settings);

        initMenu();
        initDialogs();
    }

    private void initMenu() {
        FXProjectTypeLoader loader = new FXProjectTypeLoader("conf/fxcreator.json");
        loader.load();
        ArrayList<FXProjectType> projectTypes = loader.getProjectTypes();
        for (FXProjectType projectType : projectTypes) {
            JMenu projectTypeMenu = new JMenu(projectType.getName());
            mnuNewProject.add(projectTypeMenu);
            // load the templates.
            String templatesFile = projectType.getTemplates();
            FXProjectTemplateLoader tLoader = new FXProjectTemplateLoader(templatesFile);
            FXProjectTemplates ft = tLoader.load();

            if (ft.getNrOfGroups() == 1) {
                FXProjectTemplateGroup group = ft.getFirstGroup();
                if (group != null) {
                    for (FXProjectTemplate template : group.getTemplates()) {
                        JMenuItem item = new JMenuItem(template.getUILabel());
                        item.setAction(new ProjectTemplateAction(template));
                        projectTypeMenu.add(item);
                    }
                }
            } else if (ft.getNrOfGroups() > 1) {
                for (FXProjectTemplateGroup group : ft.getGroups()) {
                    JMenu groupMenu = new JMenu(group.getUILabel());
                    projectTypeMenu.add(groupMenu);
                    for (FXProjectTemplate template : group.getTemplates()) {
                        JMenuItem item = new JMenuItem(template.getUILabel());
                        item.setAction(new ProjectTemplateAction(template));
                        groupMenu.add(item);
                    }
                }
            }
        }
        FXSingleton.getSingleton().setSupportedProjectTypes(loader.getProjectTypes());
        //groupNodeEditorPanel2.setLibrary(project.getNodeTemplateLibrary());
        //groupNodeEditorPanel2.setProject(project);

        FXSingleton.getSingleton().registerListener(this);
    }

    private void initDialogs() {
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {

            public boolean accept(File path) {
                if (path.isDirectory()) {
                    return true;
                }
                String filename = path.getName().toLowerCase();
                return filename.endsWith("daefx");
            }

            @Override
            public String getDescription() {
                return "FX creator project files";
            }
        });
    }

    @Subscribe
    public void createNewProject(NewProjectEvent npe) {
        FXProjectTemplate template = npe.getProjectTemplate();
        project = template.createNewProject();
        groupNodeEditorPanel2.setProject(project);
        this.projectTree.setModel(new DefaultTreeModel(project));
        
        adjustExporterMenu();
    }
    
    @Subscribe
    public void exportProject(ExportProjectEvent epe){
        if ( project != null ){
            TemplateClassLibrary tcl = epe.getTemplateClassLibrary();
            ExportTask et = new ExportTask(project, PathUtil.createUserDirPath("test/test.rig"), tcl);
            et.export();
            ShaderViewer sv = new ShaderViewer();
            sv.setExportTask(et);
            outputTab.add(tcl.getLabel(),sv);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JSplitPane();
        settingsPanel = new javax.swing.JSplitPane();
        iONodeSettingsPanel1 = new dae.fxcreator.node.graph.uisetting.IONodeSettingsPanel();
        outputTab = new javax.swing.JTabbedPane();
        groupNodeEditorPanel2 = new dae.fxcreator.node.graph.GroupNodeEditorPanel();
        treeShader = new javax.swing.JScrollPane();
        projectTree = new javax.swing.JTree();
        mnuFXCreator = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mnuNewProject = new javax.swing.JMenu();
        separator1 = new javax.swing.JPopupMenu.Separator();
        mnuSaveProject = new javax.swing.JMenuItem();
        mnuEdit = new javax.swing.JMenu();
        mnuExport = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setDividerLocation(100);

        settingsPanel.setDividerLocation(640);
        settingsPanel.setRightComponent(iONodeSettingsPanel1);

        outputTab.addTab("Graph", groupNodeEditorPanel2);

        settingsPanel.setLeftComponent(outputTab);

        mainPanel.setRightComponent(settingsPanel);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Project");
        projectTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        projectTree.setRootVisible(false);
        treeShader.setViewportView(projectTree);

        mainPanel.setLeftComponent(treeShader);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        mnuFile.setText("File");

        mnuNewProject.setText("New Project");
        mnuFile.add(mnuNewProject);
        mnuFile.add(separator1);

        mnuSaveProject.setText("Save");
        mnuSaveProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSaveProjectActionPerformed(evt);
            }
        });
        mnuFile.add(mnuSaveProject);

        mnuFXCreator.add(mnuFile);

        mnuEdit.setText("Edit");
        mnuFXCreator.add(mnuEdit);

        mnuExport.setText("Export");
        mnuFXCreator.add(mnuExport);

        setJMenuBar(mnuFXCreator);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuSaveProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSaveProjectActionPerformed
        if (project != null) {

            if (!project.isLoadedFromTemplate()) {
                FXProjectSaver saver = new FXProjectSaver(project);
                saver.save(true);
            } else {
                saveAs();
            }
        }
    }//GEN-LAST:event_mnuSaveProjectActionPerformed

    /**
     * Saves the project as a new file.
     */
    private void saveAs() {
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File location = fileChooser.getSelectedFile();
            Path p = Paths.get(location.getPath());
            if (!PathUtil.checkExtension(p, "daefx")) {
                p = p.resolveSibling(p.getFileName().toString() + ".daefx");
            }
            project.setFile(p);
            project.setLoadedFromTemplate(false);
            FXSingleton.getSingleton().getUserSettings().addRecentFile(p);
            this.setTitle("Umbra FX - " + location.getPath());
            FXProjectSaver saver = new FXProjectSaver(project);
            saver.save(true);
        }
    }
    
    private void adjustExporterMenu(){
        mnuExport.removeAll();
        /*
        for ( TemplateClassLibrary tcl : project.getProjectType().getExporterLibraries()){
            ExportAction ea = new ExportAction(tcl);
            mnuExport.add(ea);
        }
        */
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
            }
            new FXCreator2().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private dae.fxcreator.node.graph.GroupNodeEditorPanel groupNodeEditorPanel2;
    private dae.fxcreator.node.graph.uisetting.IONodeSettingsPanel iONodeSettingsPanel1;
    private javax.swing.JSplitPane mainPanel;
    private javax.swing.JMenu mnuEdit;
    private javax.swing.JMenu mnuExport;
    private javax.swing.JMenuBar mnuFXCreator;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenu mnuNewProject;
    private javax.swing.JMenuItem mnuSaveProject;
    private javax.swing.JTabbedPane outputTab;
    private javax.swing.JTree projectTree;
    private javax.swing.JPopupMenu.Separator separator1;
    private javax.swing.JSplitPane settingsPanel;
    private javax.swing.JScrollPane treeShader;
    // End of variables declaration//GEN-END:variables

}
