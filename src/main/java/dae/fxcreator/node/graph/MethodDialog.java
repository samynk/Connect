/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MethodDialog.java
 *
 * Created on Sep 9, 2010, 3:20:21 PM
 */
package dae.fxcreator.node.graph;

import dae.fxcreator.io.templates.NodeContainerProxy;
import dae.fxcreator.io.templates.NodeTemplateLibrary;

/**
 *
 * @author Koen
 */
public class MethodDialog extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private NodeContainerProxy result;
    private NodeTemplateLibrary library;

    /** Creates new form MethodDialog */
    public MethodDialog(java.awt.Frame parent, NodeTemplateLibrary library, boolean modal) {
        super(parent, modal);
        setTitle("New group");
        this.library = library;
        initComponents();

    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            txtGroupName.setText("");
            txtLabel.setText("");
            txtDescription.setText("");
            txtPrefix.setText("");
            this.okButton.setEnabled(false);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        lblGroupName = new javax.swing.JLabel();
        txtGroupName = new javax.swing.JTextField();
        lblLabel = new javax.swing.JLabel();
        txtLabel = new javax.swing.JTextField();
        lblDescription = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextPane();
        lblWarning = new javax.swing.JLabel();
        lblPrefix = new javax.swing.JLabel();
        txtPrefix = new javax.swing.JTextField();

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

        lblGroupName.setText("Group name : ");

        txtGroupName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGroupNameKeyTyped(evt);
            }
        });

        lblLabel.setText("Label : ");

        lblDescription.setText("Description : ");

        jScrollPane1.setViewportView(txtDescription);

        lblWarning.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblWarning.setText("<html>Group name allready exits, <br>please choose another one.</html>");

        lblPrefix.setText("Prefix : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblWarning, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblPrefix)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrefix, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblGroupName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGroupName, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblDescription)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPrefix)
                    .addComponent(txtPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGroupName)
                    .addComponent(txtGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLabel)
                    .addComponent(txtLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDescription))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(okButton)
                        .addComponent(cancelButton))
                    .addComponent(lblWarning))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doClose(RET_OK);
        result = new NodeContainerProxy(txtPrefix.getText(), txtGroupName.getText(), "", txtLabel.getText(), txtDescription.getText());
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void txtGroupNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGroupNameKeyTyped

        String text = txtGroupName.getText();
        if (text.length() > 0 && !library.containsMethod(text)) {
            okButton.setEnabled(true);
            lblWarning.setVisible(false);
        } else {
            lblWarning.setVisible(true);
        }
        if (text.length() == 0) {
            okButton.setEnabled(false);
        }
    }//GEN-LAST:event_txtGroupNameKeyTyped

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**
     * Gets the result.
     * @return
     */
    public NodeContainerProxy getResult() {
        return result;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblGroupName;
    private javax.swing.JLabel lblLabel;
    private javax.swing.JLabel lblPrefix;
    private javax.swing.JLabel lblWarning;
    private javax.swing.JButton okButton;
    private javax.swing.JTextPane txtDescription;
    private javax.swing.JTextField txtGroupName;
    private javax.swing.JTextField txtLabel;
    private javax.swing.JTextField txtPrefix;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
