package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.node.settings.ImageFileSetting;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Allows the user to set an image file as setting for a node.
 *
 * @author Koen
 */
public class ImageFileSettingComponent extends JPanel implements ActionListener {

    private JLabel lblLabel;
    private JTextField txtFile;

    private static JFileChooser imageFileChooser = new JFileChooser(System.getProperty("user.dir"));

    static {

    }
    /**
     * The ImageFileSetting to show.
     */
    private ImageFileSetting setting;

    public ImageFileSettingComponent() {
        setLayout(new GridBagLayout());
        lblLabel = new JLabel(":");
        txtFile = new JTextField("");
        txtFile.setEnabled(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1.0;
        add(txtFile, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        JButton btnChange = new JButton("...");
        btnChange.addActionListener(this);
        add(btnChange, gbc);
    }

    public void setImageFileSetting(ImageFileSetting setting) {
        lblLabel.setText(setting.getLabel() + " : ");
        this.setting = setting;
        txtFile.setText(setting.getSettingValue());
    }

    public void actionPerformed(ActionEvent ae) {
        int option = imageFileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selected = imageFileChooser.getSelectedFile();
            setting.setSettingValue(selected.getPath());
            txtFile.setText(setting.getSettingValue());
        }
    }
}
