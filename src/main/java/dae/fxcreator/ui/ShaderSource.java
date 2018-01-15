package dae.fxcreator.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Koen
 */
public class ShaderSource extends javax.swing.JPanel {

    /**
     * Creates new form ShaderSource
     */
    public ShaderSource() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrSource = new javax.swing.JScrollPane();
        txtSource = new javax.swing.JTextPane();

        txtSource.setEditable(false);
        scrSource.setViewportView(txtSource);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrSource, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrSource, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    void showFile(Path file) {
        if (Files.exists(file)) {
            BufferedReader br = null;
            try {
                br = Files.newBufferedReader(file);
                StringBuilder result = new StringBuilder();
                String line;
                String separator = System.getProperty("line.separator");
                while ((line = br.readLine()) != null) {
                    result.append(line);
                    result.append(separator);
                }
                txtSource.setText(result.toString());
            } catch (IOException ex) {
                Logger.getLogger(ShaderSource.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(ShaderSource.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrSource;
    private javax.swing.JTextPane txtSource;
    // End of variables declaration//GEN-END:variables
}
