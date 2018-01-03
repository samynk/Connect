/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ExportDialog.java
 *
 * Created on Dec 31, 2009, 4:14:24 PM
 */

package dae.fxcreator.ui;

import dae.fxcreator.io.ExportFile;
import dae.fxcreator.io.FXProject;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author Koen
 */
public class ExportDialog extends javax.swing.JDialog {
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    private FXProject project;
    private String exporterId;

    private JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));

    /** Creates new form ExportDialog */
    public ExportDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    /**
     * Sets the FXProject to export and the exporter id.
     * @param project the project to export.
     * @param exporterId the exporter id.
     */
    public void setFXProject(FXProject project, String exporterId){
        this.project = project;
        this.exporterId = exporterId;

        if ( project == null){
            return;
        }

        ExportFile export = project.getExportDestination(exporterId);
       
        if ( export != null){
            this.chooseExportDirectory.setSelectedFile(export.getDirectory());
            txtExport.setText(export.getDirectory().getPath());
            txtShaderName.setText(export.getFilename());
            txtExtension.setText(export.getExtension());
        }else{
            File dir = project.getFile().getParentFile();
            this.chooseExportDirectory.setSelectedFile(dir);
            txtExport.setText(dir.getPath());
            txtShaderName.setText(project.getName());
            String extension = project.getExportExtension(exporterId);
            txtExtension.setText(extension);
            project.addExportDestination(exporterId, new ExportFile(dir,project.getName(),extension));
        }
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chooseExportDirectory = new javax.swing.JFileChooser();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        pnlExport = new javax.swing.JPanel();
        lblExport = new javax.swing.JLabel();
        txtExport = new javax.swing.JTextField();
        btnChooseDirectory = new javax.swing.JButton();
        lblShaderName = new javax.swing.JLabel();
        txtShaderName = new javax.swing.JTextField();
        cboShowCodeAfterExport = new javax.swing.JCheckBox();
        lblExtension = new javax.swing.JLabel();
        txtExtension = new javax.swing.JTextField();

        setTitle("Export");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        lblExport.setText("Export directory : ");

        txtExport.setEnabled(false);

        btnChooseDirectory.setText("...");
        btnChooseDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseDirectoryActionPerformed(evt);
            }
        });

        lblShaderName.setText("Shader Name : ");

        cboShowCodeAfterExport.setText("Show code after export");

        lblExtension.setText("Extension : ");

        javax.swing.GroupLayout pnlExportLayout = new javax.swing.GroupLayout(pnlExport);
        pnlExport.setLayout(pnlExportLayout);
        pnlExportLayout.setHorizontalGroup(
            pnlExportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlExportLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlExportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlExportLayout.createSequentialGroup()
                        .addComponent(lblShaderName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtShaderName))
                    .addGroup(pnlExportLayout.createSequentialGroup()
                        .addComponent(lblExport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtExport, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnChooseDirectory))
                    .addComponent(cboShowCodeAfterExport)
                    .addGroup(pnlExportLayout.createSequentialGroup()
                        .addComponent(lblExtension)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtExtension)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlExportLayout.setVerticalGroup(
            pnlExportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlExportLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlExportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblExport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtExport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChooseDirectory))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlExportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblShaderName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtShaderName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlExportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblExtension)
                    .addComponent(txtExtension, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(cboShowCodeAfterExport)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(pnlExport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlExport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void btnChooseDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseDirectoryActionPerformed
        int option = chooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION){
            ExportFile export = project.getExportDestination(exporterId);
            if ( export != null){
                
                txtExport.setText(chooser.getSelectedFile().getPath());
            }
        }
    }//GEN-LAST:event_btnChooseDirectoryActionPerformed

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        if ( returnStatus == RET_OK){
            ExportFile export = project.getExportDestination(exporterId);
            export.setDirectory(new File(txtExport.getText()));
            export.setFilename(txtShaderName.getText());
            export.setExtension(txtExtension.getText());
        }
        setVisible(false);
        dispose();
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ExportDialog dialog = new ExportDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChooseDirectory;
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox cboShowCodeAfterExport;
    private javax.swing.JFileChooser chooseExportDirectory;
    private javax.swing.JLabel lblExport;
    private javax.swing.JLabel lblExtension;
    private javax.swing.JLabel lblShaderName;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel pnlExport;
    private javax.swing.JTextField txtExport;
    private javax.swing.JTextField txtExtension;
    private javax.swing.JTextField txtShaderName;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
}
