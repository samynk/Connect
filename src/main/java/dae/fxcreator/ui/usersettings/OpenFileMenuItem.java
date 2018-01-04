package dae.fxcreator.ui.usersettings;

import dae.fxcreator.ui.FXCreator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JMenuItem;

/**
 *
 * @author Koen
 */
public class OpenFileMenuItem extends JMenuItem implements ActionListener{
    private File file;
    private FXCreator parent;

    public OpenFileMenuItem(File file,FXCreator parent){
        this.file = file;
        this.parent = parent;

        this.setText(file.getName());
        this.setToolTipText(file.getPath());

        addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        parent.loadProject(file);
    }
}
