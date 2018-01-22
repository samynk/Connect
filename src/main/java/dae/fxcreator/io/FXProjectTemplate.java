package dae.fxcreator.io;

import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.io.loaders.FXProjectLoader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

/**
 *
 * @author Koen
 */
public class FXProjectTemplate implements ActionListener {

    /**
     * The name of the project template.
     */
    private String name;
    /**
     * The description of the project template.
     */
    private String description;
    /**
     * The ui label of the project template.
     */
    private String uiLabel;
    /**
     * The name for the project template.
     */
    private Path sourceFile;
    /**
     * The relative path of the project template.
     */
    private Path relativePath;
    /**
     * The group of the FXProjectTemplate object.
     */
    private String group;

    /**
     * Creates a new FXProjectTemplate object.
     */
    public FXProjectTemplate() {
    }

    /**
     * Gets the name of the project template.
     *
     * @return the name of the project template.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the project template.
     *
     * @param name the name of the project template.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the location of the sourcefile for this project template.
     *
     * @return the location of the sourcefile.
     */
    public Path getSourceFile() {
        return sourceFile;
    }

    /**
     * Sets the location of the sourcefile for this project template.
     *
     * @param sourceFile the location of the source file.
     */
    public void setSourceFile(Path sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * Returns the relative path for this template.
     *
     * @return the relative path for the template.
     */
    public Path getRelativePath() {
        return relativePath;
    }

    /**
     * Sets the relative path for this template.
     *
     * @param relativePath the new relative path for this template.
     */
    public void setRelativePath(Path relativePath) {
        this.relativePath = relativePath;
    }

    /**
     * Returns the description for this FXProjectTemplate.
     *
     * @return the description for the FXProjectTemplate.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for this FXProjectTemplate.
     *
     * @param description the new description for the FXProjectTemplate.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the UI Label for the FXProjectTemplate.
     *
     * @return the label for the FXProjectTemplate.
     */
    public String getUILabel() {
        return uiLabel;
    }

    /**
     * Sets the label for the FXProjectTemplate.
     *
     * @param uiLabel the new label for the FXProjectTemplate.
     */
    public void setUILabel(String uiLabel) {
        this.uiLabel = uiLabel;
    }

    /**
     * Loads the file into a new project.
     * @return returns a new FXProject instantiated from the given template.
     */
    public FXProject createNewProject() {
        Path file = getSourceFile();
        FXProjectLoader loader = new FXProjectLoader(file);
        FXProject project = loader.load();
        project.setLoadedFromTemplate(true);
        return project;
    }

    /**
     * Returns the group this template belongs to.
     *
     * @return the group of this template
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the group this template belongs to.
     *
     * @param group the group of this template.
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Checks if this FXProjectTemplate has a relative path setting.
     *
     * @return true if the FXProjectTemplate has a relative path,false
     * otherwise.
     */
    public boolean hasRelativePath() {
        return relativePath != null && relativePath.getNameCount() > 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
