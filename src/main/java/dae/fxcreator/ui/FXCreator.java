package dae.fxcreator.ui;

import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.io.templates.FXProjectTemplate;
import dae.fxcreator.io.templates.FXProjectTemplateGroup;
import dae.fxcreator.io.templates.FXProjectTemplates;
import dae.fxcreator.node.project.FXProjectType;
import dae.fxcreator.io.savers.FXProjectSaver;
import dae.fxcreator.gui.model.FXSettings;
import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.node.NodeGroup;
import dae.fxcreator.io.PathUtil;
import dae.fxcreator.io.codegen.CodeTemplateLibrary;
import dae.fxcreator.node.transform.ExportTask;
import dae.fxcreator.io.loaders.FXGroupLoader;
import dae.fxcreator.io.loaders.FXProjectLoader;
import dae.fxcreator.io.loaders.FXProjectTemplateLoader;
import dae.fxcreator.io.loaders.FXSettingLoader;
import dae.fxcreator.io.savers.FXProjectTemplateSaver;
import dae.fxcreator.io.savers.FXSettingSaver;
import dae.fxcreator.node.templates.NodeTemplateLibrary;
import dae.fxcreator.io.loaders.NodeTemplateLoader;
import dae.fxcreator.node.graph.Connector;
import dae.fxcreator.node.graph.GraphListener;
import dae.fxcreator.node.graph.GraphNodeLink;
import dae.fxcreator.node.graph.JGraphNode;
import dae.fxcreator.gui.model.ImageLoader;
import dae.fxcreator.ui.usersettings.OpenFileMenuItem;
import dae.fxcreator.ui.usersettings.UserSettings;
import dae.fxcreator.ui.usersettings.UserSettingsDialog;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Koen
 */
public class FXCreator extends javax.swing.JFrame implements GraphListener, ActionListener {

    private FXProject project;
    private NodeTemplateLibrary library;
    private UserSettings settings;
    private FXSettings fxSettings;
    private FXProjectTemplates projectTemplates;
    private FXProjectTemplatesDialog projectTemplatesDialog;
    private UserSettingsDialog userSettingsDialog;
    private final AboutDialog aboutDialog;

    /**
     * Creates new form FXCreator
     */
    public FXCreator() {
        initComponents();
        techniquePanel.setParent(this);
        techniquePanel.addGraphListener(this);
        exportDialog = new ExportDialog(this, true);

        projectTemplatesDialog = new FXProjectTemplatesDialog(this, true);

        this.groupNodeEditorPanel1.setParent(this);

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
                return "Umbra fx project files";
            }
        });
        //this.editorSplitPane.setResizeWeight(0.0);
        //this.splitGraphProperties.setResizeWeight(1.0);
        this.setBackground(Color.darkGray);
        this.iONodeSettingsPanel1.setBackground(Color.darkGray);

        aboutDialog = new AboutDialog(this, true);
        Image icon = ImageLoader.getInstance().getImage("/dae/images/dae.png");
        this.setIconImage(icon);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        tabEditor = new javax.swing.JTabbedPane();
        splitGraphProperties = new javax.swing.JSplitPane();
        editorSplitPane = new javax.swing.JSplitPane();
        groupNodeEditorPanel1 = new dae.fxcreator.node.graph.GroupNodeEditorPanel();
        techniquePanel = new dae.fxcreator.node.graph.TechniqueEditorPanel();
        iONodeSettingsPanel1 = new dae.fxcreator.node.graph.uisetting.IONodeSettingsPanel();
        fxMenu = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        mnuNew = new javax.swing.JMenu();
        mnuOpen = new javax.swing.JMenuItem();
        mnuSave = new javax.swing.JMenuItem();
        mnuSaveAs = new javax.swing.JMenuItem();
        mnuSaveAsTemplate = new javax.swing.JMenuItem();
        mnuRecentFiles = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JSeparator();
        mnuExport = new javax.swing.JMenu();
        editMenu = new javax.swing.JMenu();
        viewMenu = new javax.swing.JMenu();
        toolsMenu = new javax.swing.JMenu();

        JMenu helpMenu = new javax.swing.JMenu("Help");

        mnuCopy = new javax.swing.JMenuItem();
        mnuCut = new javax.swing.JMenuItem();
        mnuPaste = new javax.swing.JMenuItem();
        JMenuItem mnuCopyAsRef = new javax.swing.JMenuItem();

        mnuOptions = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Umbra Fx");
        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        splitGraphProperties.setDividerLocation(500);

        editorSplitPane.setDividerLocation(200);
        editorSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        editorSplitPane.setRightComponent(groupNodeEditorPanel1);
        editorSplitPane.setLeftComponent(techniquePanel);

        splitGraphProperties.setLeftComponent(editorSplitPane);
        //iONodeSettingsPanel1.setMaximumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        JScrollPane jsp = new JScrollPane();
        jsp.setViewportView(iONodeSettingsPanel1);
        splitGraphProperties.setRightComponent(jsp);

        tabEditor.addTab("Shader definition", splitGraphProperties);

        fileMenu.setText("File");

        mnuNew.setText("New Project");
        fileMenu.add(mnuNew);

        mnuOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mnuOpen.setText("Open");
        mnuOpen.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuOpenActionPerformed(evt);
            }
        });
        fileMenu.add(mnuOpen);

        mnuSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mnuSave.setText("Save");
        mnuSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSaveActionPerformed(evt);
            }
        });
        fileMenu.add(mnuSave);

        mnuSaveAs.setText("Save As ...");
        mnuSaveAs.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSaveAsActionPerformed(evt);
            }
        });

        mnuSaveAsTemplate.setText("Save As Template ...");
        mnuSaveAsTemplate.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mnuSaveAsTemplateActionPerformed(e);
            }
        });

        mnuCut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        mnuCut.setText("Cut");
        mnuCut.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCutActionPerformed(evt);
            }
        });

        mnuCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        mnuCopy.setText("Copy");
        mnuCopy.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCopyActionPerformed(evt);
            }
        });

        mnuPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        mnuPaste.setText("Paste");
        mnuPaste.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPasteActionPerformed(evt);
            }
        });

        mnuCopyAsRef.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        mnuCopyAsRef.setText("Copy as reference");
        mnuCopyAsRef.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCopyAsRefActionPerformed(evt);
            }
        });

        mnuOptions.setText("Options");
        mnuOptions.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuOpenToolsActionPerformed(evt);
            }
        });

        JMenuItem mnuAbout = new JMenuItem("About");
        mnuAbout.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aboutDialog.setVisible(true);
            }
        });
        helpMenu.add(mnuAbout);

        fileMenu.add(mnuSaveAs);
        fileMenu.add(mnuSaveAsTemplate);

        mnuRecentFiles.setText("Recent Files");
        fileMenu.add(mnuRecentFiles);
        fileMenu.add(jSeparator1);

        mnuExport.setText("Export");
        fileMenu.add(mnuExport);

        fxMenu.add(fileMenu);

        editMenu.setText("Edit");
        fxMenu.add(editMenu);

        editMenu.add(mnuCut);
        editMenu.add(mnuCopy);
        editMenu.add(mnuPaste);
        editMenu.addSeparator();
        editMenu.add(mnuCopyAsRef);

        toolsMenu.setText("Tools");
        fxMenu.add(toolsMenu);

        toolsMenu.add(mnuOptions);

        //viewMenu.setText("View");
        //fxMenu.add(viewMenu);
        fxMenu.add(helpMenu);
        setJMenuBar(fxMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(tabEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(tabEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 550, Short.MAX_VALUE));

        pack();
    }// </editor-fold>

    private void mnuOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuOpenActionPerformed
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Path p = file.toPath();
            settings.addRecentFile(p);

            loadProject(p);
            loadRecentFiles();
        }
    }//GEN-LAST:event_mnuOpenActionPerformed

    private void mnuSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSaveActionPerformed
        if (project != null) {
            if (!project.isLoadedFromTemplate()) {
                FXProjectSaver saver = new FXProjectSaver(project);
                saver.save(true);
            } else {
                saveAs();
            }
        }
    }//GEN-LAST:event_mnuSaveActionPerformed

    private void mnuSaveAsTemplateActionPerformed(ActionEvent e) {
        if (project != null) {
            projectTemplatesDialog.setFXProjectTemplates(projectTemplates);
            projectTemplatesDialog.setVisible(true);
            if (projectTemplatesDialog.getReturnStatus() == FXProjectTemplatesDialog.RET_OK) {
                FXProjectTemplate template = projectTemplatesDialog.getResult();
                String category = projectTemplatesDialog.getCategory();

                FXProjectTemplateGroup group = projectTemplates.getGroup(category);
                if (group != null) {
                    group.addTemplate(template);
                    // save the template library
                    FXProjectTemplateSaver saver = new FXProjectTemplateSaver(projectTemplates);
                    saver.save();

                    // save the project as a template in the correct location.
                    // TODO remove hardcoded directory.
                    String templateLocation = System.getProperty("user.dir") + "/fxtemplates/" + group.getName() + "/" + template.getName() + ".daefx";
                    Path fileLoc = Paths.get(templateLocation);
                    template.setSourceFile(fileLoc);
                    project.setFile(fileLoc);
                    project.setLoadedFromTemplate(true);
                    FXProjectSaver projectSaver = new FXProjectSaver(this.project);
                    projectSaver.save(false);
                    setTitle("Umbra FX");

                    mnuNew.removeAll();
                    this.loadTemplates();

                }
            }
        }
    }

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
            settings.addRecentFile(p);
            this.setTitle("Umbra FX - " + location.getPath());
            FXProjectSaver saver = new FXProjectSaver(project);
            saver.save(true);
        }
    }

    private void mnuCutActionPerformed(java.awt.event.ActionEvent evt) {
        if (project != null) {
            this.groupNodeEditorPanel1.cut();
        }
    }

    private void mnuCopyActionPerformed(java.awt.event.ActionEvent evt) {
        if (project != null) {
            this.groupNodeEditorPanel1.copy();
        }
    }

    private void mnuPasteActionPerformed(java.awt.event.ActionEvent evt) {
        if (project != null) {
            this.groupNodeEditorPanel1.paste();
        }
    }

    private void mnuCopyAsRefActionPerformed(ActionEvent evt) {
        this.groupNodeEditorPanel1.copySelectionAsReference();
    }

    private void mnuOpenToolsActionPerformed(java.awt.event.ActionEvent evt) {
        if (userSettingsDialog == null) {
            /*
            userSettingsDialog = new UserSettingsDialog(this, FXSingleton.getSingleton().getCurrentProjectType().getNodeTemplateLibrary().getTypeLibrary(), true);
            userSettingsDialog.setTitle("User settings");
            userSettingsDialog.pack();
             */
        }
        userSettingsDialog.setVisible(true);
        this.groupNodeEditorPanel1.updateStyles();
        this.techniquePanel.updateStyles();
    }

    private void mnuSaveAsActionPerformed(ActionEvent evt) {
        saveAs();
    }
    boolean templatesLoaded = false;

    public void loadProject(Path file) {
        /*
        FXProjectLoader fxProjectLoader = new FXProjectLoader(file);
        project = fxProjectLoader.load();
        techniquePanel.setProject(project);
        setTitle("Umbra FX - " + file.toString());
        */
    }

    public void loadRecentFiles() {
        mnuRecentFiles.removeAll();
        // todo replace with Path objects.
        for (Path file : settings.getRecentFiles()) {
            OpenFileMenuItem item = new OpenFileMenuItem(file, this);
            mnuRecentFiles.add(item);
        }
    }

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        if (!templatesLoaded) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            FXSettingLoader loader = new FXSettingLoader();
            fxSettings = loader.load();
            FXSingleton.getSingleton().setFxSettings(fxSettings);

            templatesLoaded = true;

            /*
            for (CodeTemplateLibrary codelib : fxSettings.exporterLibraries()) {
                JMenuItem export = new JMenuItem(codelib.getLabel());
                export.setActionCommand(codelib.getId());
                export.addActionListener(this);
                mnuExport.add(export);
            }
            */

            loadTemplates();
            FXProjectTemplate startProject = projectTemplates.getStartProject();
            if (startProject != null) {

                /*
                FXProjectType type = fxSettings.findLatestProjectType("dae game");
                //FXSingleton.getSingleton().setCurrentProjectType(type);
                FXProjectLoader projectLoader = new FXProjectLoader(startProject.getSourceFile());
                project = projectLoader.load();
                project.setLoadedFromTemplate(true);
                techniquePanel.setProject(project);

                this.groupNodeEditorPanel1.setLibrary(type.getNodeTemplateLibrary());
                groupNodeEditorPanel1.setProject(project);
                this.techniquePanel.setLibrary(type.getNodeTemplateLibrary());
                this.iONodeSettingsPanel1.setNodeTemplateLibrary(type.getNodeTemplateLibrary());
                */
            }

            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_formWindowActivated

    private void loadTemplates() {
        FXProjectTemplateLoader templateLoader = new FXProjectTemplateLoader("fxtemplates/basic.daefx");
        projectTemplates = templateLoader.load();

        for (FXProjectTemplateGroup group : projectTemplates.getGroups()) {
            JMenu menu = new JMenu(group.getUILabel());
            mnuNew.add(menu);

            for (FXProjectTemplate template : group.getTemplates()) {
                JMenuItem item = new JMenuItem(template.getUILabel());
                item.setToolTipText(template.getDescription());

                item.setActionCommand(group.getName() + "." + template.getName());
                item.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        String ac = ae.getActionCommand();
                        FXProjectTemplate t = projectTemplates.getTemplate(ac);
                        if (t != null) {
                            Path file = t.getSourceFile();
                            /*
                            FXProjectLoader loader = new FXProjectLoader(file);
                            project = loader.load();
                            project.setLoadedFromTemplate(true);
                            techniquePanel.setProject(project);
                            setTitle("Umbra Fx");
                            */
                        }
                    }
                });
                menu.add(item);
            }
        }
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        settings.save();
        //if (fxSettings.isChanged()) {
        FXSettingSaver saver = new FXSettingSaver(fxSettings);
        saver.save();
        //}
    }//GEN-LAST:event_formWindowClosing

    public void loadSettings() {
        settings = new UserSettings(this);
        FXSingleton.getSingleton().setUserSettings(settings);
        loadRecentFiles();
        settings.applySettings();
    }

    public int getSettingsDividerLocation() {
        return splitGraphProperties.getDividerLocation();
    }

    public void setSettingsDividerLocation(int location) {
        splitGraphProperties.setDividerLocation(location);
    }

    public void setSettingsDividerLocation(double d) {
        this.splitGraphProperties.setDividerLocation(d);
    }

    public int getEditorDividerLocation() {
        return editorSplitPane.getDividerLocation();
    }

    public void setEditorDividerLocation(int location) {
        editorSplitPane.setDividerLocation(location);
    }

    public void setEditorDividerLocation(double d) {
        this.editorSplitPane.setDividerLocation(d);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    LookAndFeelInfo[] lfs = UIManager.getInstalledLookAndFeels();
                    for (LookAndFeelInfo lf : lfs) {
                        System.out.println(lf.getClassName());
                    }
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

                    //UIManager.put("Panel.background", Color.darkGray);
                    UIManager.put("Label.foreground", Color.white);

                    UIManager.put("TaskPaneContainer.useGradient", Boolean.FALSE);
                    UIManager.put("TaskPaneContainer.background", Color.gray);
                    //UIManager.put("TaskPaneContainer.backgroundGradientStart",Color.gray);
                    //UIManager.put("TaskPaneContainer.backgroundGradientEnd",Color.darkGray);

                    UIManager.put("TaskPane.titleBackgroundGradientStart", Color.gray);
                    UIManager.put("TaskPane.titleBackgroundGradientEnd", Color.darkGray);
                    UIManager.put("TaskPane.titleForeground", Color.white);
                    UIManager.put("TaskPane.titleForegroundSpecial", Color.PINK);

//                    UIManager.put("TaskPaneContainer.backgroundPainter", new PainterUIResource<JXTaskPaneContainer>(
//                            new MattePainter(Color.darkGray)));
                    UIManager.put("TaskPaneContainer.background", Color.darkGray);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(FXCreator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(FXCreator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(FXCreator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(FXCreator.class.getName()).log(Level.SEVERE, null, ex);
                }
                FXCreator frame = new FXCreator();
                frame.loadSettings();
                frame.setVisible(true);

            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu editMenu;
    private javax.swing.JSplitPane editorSplitPane;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuBar fxMenu;
    private dae.fxcreator.node.graph.GroupNodeEditorPanel groupNodeEditorPanel1;
    private dae.fxcreator.node.graph.uisetting.IONodeSettingsPanel iONodeSettingsPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenu mnuExport;
    private javax.swing.JMenu mnuNew;
    private javax.swing.JMenuItem mnuOpen;
    private javax.swing.JMenu mnuRecentFiles;
    private javax.swing.JMenuItem mnuSave;
    private javax.swing.JSplitPane splitGraphProperties;
    private javax.swing.JTabbedPane tabEditor;
    private dae.fxcreator.node.graph.TechniqueEditorPanel techniquePanel;
    private javax.swing.JMenu viewMenu;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JMenuItem mnuSaveAs;
    private JMenuItem mnuSaveAsTemplate;
    private javax.swing.JMenuItem mnuCut;
    private javax.swing.JMenuItem mnuCopy;
    private javax.swing.JMenuItem mnuPaste;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JMenuItem mnuOptions;

    @Override
    public void nodeSelected(JGraphNode node) {

        iONodeSettingsPanel1.setGraphNode(node);
    }

    @Override
    public void nodeAdded(JGraphNode node) {
    }

    @Override
    public void nodeRemoved(JGraphNode node) {
    }

    @Override
    public void nodeMoved(JGraphNode node) {
    }

    @Override
    public void linkSelected(GraphNodeLink link) {
    }
    private ExportDialog exportDialog;
    private HashMap<String, ShaderViewer> exportViewers = new HashMap<>();

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        exportDialog.setFXProject(project, command);
        exportDialog.setVisible(true);
        int retStatus = exportDialog.getReturnStatus();
        if (retStatus == ExportDialog.RET_OK) {
            ExportTask task = new ExportTask(project, command, null); // fxSettings.getCodeTemplateLibrary(command));
            task.export();

            ShaderViewer sv = exportViewers.get(command);
            if (sv == null) {
                sv = new ShaderViewer();
                exportViewers.put(command, sv);
            }
            sv.setExportTask(task);
            tabEditor.addTab(command + " code", sv);
        }
    }

    @Override
    public void linkAdded(Connector connector) {
    }

    @Override
    public void linkRemoved(Connector connector) {
    }

    @Override
    public void nodeEntered(JGraphNode node) {
        if (node.getUserObject() instanceof NodeGroup) {
            groupNodeEditorPanel1.setGroupNode((NodeGroup) node.getUserObject());
            groupNodeEditorPanel1.repaint();
        }
    }

}
