package dae.fxcreator.ui.usersettings;

import dae.fxcreator.gui.model.FXSettings;
import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.node.gui.GradientCellRenderer;
import dae.fxcreator.gui.model.GraphGradient;
import java.awt.Color;
import javax.swing.JOptionPane;

/**
 *
 * @author Koen
 */
public class GradientSettingPanel extends javax.swing.JPanel {

    /** Creates new form GradientSettingPanel */
    public GradientSettingPanel() {
        initComponents();
        FXSettings settings = FXSingleton.getSingleton().getFXSettings();
        lstGradients.setModel(settings.getAllGradients());
        lstGradients.setCellRenderer(new GradientCellRenderer());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrGradients = new javax.swing.JScrollPane();
        lstGradients = new javax.swing.JList();
        lblGradients = new javax.swing.JLabel();
        btnNewGradient = new javax.swing.JButton();
        gradientSettingComponent1 = new dae.fxcreator.node.graph.uisetting.GradientSettingComponent();

        lstGradients.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstGradients.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstGradientsValueChanged(evt);
            }
        });
        scrGradients.setViewportView(lstGradients);

        lblGradients.setText("Gradients : ");

        btnNewGradient.setText("New Gradient ...");
        btnNewGradient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewGradientActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGradients)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnNewGradient)
                            .addComponent(scrGradients, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gradientSettingComponent1, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblGradients)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gradientSettingComponent1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scrGradients, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNewGradient)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewGradientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewGradientActionPerformed
        String gradientName = JOptionPane.showInputDialog(btnNewGradient, "Name for the new gradient : ");
        if (gradientName != null && gradientName.length() > 0) {
            GraphGradient gg = new GraphGradient(gradientName, Color.white, Color.black);
            FXSettings settings = FXSingleton.getSingleton().getFXSettings();
            settings.addGradient(gradientName, gg);

            lstGradients.setModel(settings.getAllGradients());
        }
    }//GEN-LAST:event_btnNewGradientActionPerformed

    private void lstGradientsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstGradientsValueChanged
        // TODO add your handling code here:
        Object selected = lstGradients.getSelectedValue();
        if (selected != null && selected instanceof GraphGradient) {
            GraphGradient gg = (GraphGradient) selected;
            gradientSettingComponent1.setGraphGradient(gg);
        }
    }//GEN-LAST:event_lstGradientsValueChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewGradient;
    private dae.fxcreator.node.graph.uisetting.GradientSettingComponent gradientSettingComponent1;
    private javax.swing.JLabel lblGradients;
    private javax.swing.JList lstGradients;
    private javax.swing.JScrollPane scrGradients;
    // End of variables declaration//GEN-END:variables
}
