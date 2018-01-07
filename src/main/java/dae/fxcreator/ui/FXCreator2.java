package dae.fxcreator.ui;

import dae.fxcreator.io.FXProject;
import dae.fxcreator.io.FXProjectType;
import dae.fxcreator.io.FXSettings;
import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.io.loaders.FXProjectTypeLoader;
import dae.fxcreator.io.loaders.FXSettingLoader;
import java.io.File;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

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
     * Creates new form FXCreator2
     */
    public FXCreator2() {
        initComponents();
        System.out.println(System.getProperty("user.dir"));
        FXSettingLoader fsl = new FXSettingLoader();
        FXSettings settings = fsl.load();
        FXSingleton.getSingleton().setFxSettings(settings);

        FXProjectTypeLoader loader = new FXProjectTypeLoader("conf/fxcreator.json");
        loader.load();
        FXProjectType daegame = loader.getProjectTypes().get(0);
        project = new FXProject(new File(System.getProperty("user.home"), ".fxcreator/projects/test.fx"), daegame);
        project.load();
        groupNodeEditorPanel2.setLibrary(project.getNodeTemplateLibrary());
        groupNodeEditorPanel2.setProject(project);
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
        jTree1 = new javax.swing.JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setDividerLocation(100);

        settingsPanel.setDividerLocation(400);
        settingsPanel.setRightComponent(iONodeSettingsPanel1);

        outputTab.addTab("Graph", groupNodeEditorPanel2);

        settingsPanel.setLeftComponent(outputTab);

        mainPanel.setRightComponent(settingsPanel);

        treeShader.setViewportView(jTree1);

        mainPanel.setLeftComponent(treeShader);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JTree jTree1;
    private javax.swing.JSplitPane mainPanel;
    private javax.swing.JTabbedPane outputTab;
    private javax.swing.JSplitPane settingsPanel;
    private javax.swing.JScrollPane treeShader;
    // End of variables declaration//GEN-END:variables
}
