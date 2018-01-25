package dae.fxcreator.io.savers;

import dae.fxcreator.io.savers.FXProjectSaver;
import dae.fxcreator.io.savers.XMLSaver;
import dae.fxcreator.node.templates.NodeContainerProxy;
import dae.fxcreator.node.templates.NodeTemplateLibrary;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Saves the list of methods to file.
 * @author Koen
 */
public class FXMethodListSaver extends XMLSaver{

    private final NodeTemplateLibrary library;
    private final Path location;

    /**
     * Creates a new FXMethodListSaver object.
     * @param file the file to write the method descriptions to.
     * @param library the library to s
     */
    public FXMethodListSaver(NodeTemplateLibrary library, Path file)
    {
        this.location = file;
        this.library = library;
    }

    public void save()
    {
        FileOutputStream fos = null;
        try {
            
            BufferedWriter bw = Files.newBufferedWriter( location );
            this.writeHeader(bw, "groups");

            for ( NodeContainerProxy proxy : library.getMethods())
            {
                bw.write("\t<group");
                this.writeAttribute(bw,"type", proxy.getType());
                this.writeAttribute(bw,"icon", proxy.getIcon() != null ? proxy.getIcon() : "");
                this.writeAttribute(bw,"label", proxy.getLabel() );
                this.writeAttribute(bw,"description", proxy.getDescription());
                this.writeAttribute(bw,"prefix",proxy.getPrefix());
                bw.write("/>\n");
            }
            bw.write("</groups>\n");
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(FXProjectSaver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(FXProjectSaver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
