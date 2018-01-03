package dae.fxcreator.io;

import java.io.File;

/**
 * This class bundles the properties for the export of a file.
 * @author Koen
 */
public class ExportFile {
    private File directory;
    private String filename;
    private String extension;

    /**
     * Creates a new ExportFile object.
     * @param directory the directory for the export.
     * @param filename the filename for the export.
     * @param extension the extension for the exported file.
     */
    public ExportFile(File directory, String filename,String extension){
        this.directory = directory;
        this.filename = filename;
        this.extension = extension;
    }

    /**
     * Creates a new ExportFile object.
     * @param name the name for the export file.
     * @param extension the extension for the export file.
     */
    public ExportFile(String name, String extension) {
        this.filename = name;
        this.extension = extension;
    }

    /**
     * Returns the directory for the export.
     * @return the directory for the export.
     */
    public File getDirectory() {
        return directory;
    }

    /**
     * Sets the directory for the export.
     * @param directory the new directory for the export.
     */
    public void setDirectory(File directory) {
        this.directory = directory;
    }

    /**
     * Returns the filename for the export.
     * @return the filename for the export.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the filename for the export.
     * @param filename the filename for the export.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Sets the extension for the generated files.
     * @param extension the new extension for the generated files.
     */
    public void setExtension(String extension){
        this.extension = extension;
    }

    /**
     * Gets the extension for the generated files.
     * @return the extension for the generated files.
     */
    public String getExtension(){
        return extension;
    }
}