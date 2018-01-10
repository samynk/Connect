package dae.fxcreator.ui.usersettings;

import dae.fxcreator.ui.FXCreator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import javax.swing.JMenuItem;

/**
 *
 * @author Koen
 */
public class OpenFileMenuItem extends JMenuItem implements ActionListener{
    private final Path file;
    private final FXCreator parent;

    public OpenFileMenuItem(Path file,FXCreator parent){
        this.file = file;
        this.parent = parent;
        init();
    }
    
    private void init(){
        this.setText(file.getFileName().toString());
        this.setToolTipText(file.toString());
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        parent.loadProject(file);
    }
}
