package dae.fxcreator.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
    private File sourceFile;
    /**
     * The relative path of the project template.
     */
    private String relativePath;
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
     * @return the name of the project template.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the project template.
     * @param name the name of the project template.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the location of the sourcefile for this project template.
     * @return the location of the sourcefile.
     */
    public File getSourceFile() {
        return sourceFile;
    }

    /**
     * Sets the location of the sourcefile for this project template.
     * @param sourceFile the location of the source file.
     */
    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * Returns the relative path for this template.
     * @return the relative path for the template.
     */
    public String getRelativePath(){
        return relativePath;
    }

    /**
     * Sets the relative path for this template.
     * @param relativePath the new relative path for this template.
     */
    public void setRelativePath(String relativePath){
        this.relativePath = relativePath;
    }

    /**
     * Returns the description for this FXProjectTemplate.
     * @return the description for the FXProjectTemplate.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for this FXProjectTemplate.
     * @param description the new description for the FXProjectTemplate.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the UI Label for the FXProjectTemplate.
     * @return the label for the FXProjectTemplate.
     */
    public String getUILabel() {
        return uiLabel;
    }

    /**
     * Sets the label for the FXProjectTemplate.
     * @param uiLabel the new label for the FXProjectTemplate.
     */
    public void setUILabel(String uiLabel) {
        this.uiLabel = uiLabel;
    }

    /**
     * Called when the user clicks on an action item that is connected with this
     * FXProjectTemplate object.
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e) {
        loadFile();
    }

    /**
     * Loads the file into a new project.
     */
    private void loadFile() {
    }

    /**
     *  Returns the group this template belongs to.
     * @return the group of this template
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the group this template belongs to.
     * @param group the group of this template.
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Checks if this FXProjectTemplate has a relative path setting.
     * @return true if the FXProjectTemplate has relative path,false otherwise.
     */
    public boolean hasRelativePath() {
        return relativePath != null || relativePath.length() == 0;
    }
}
