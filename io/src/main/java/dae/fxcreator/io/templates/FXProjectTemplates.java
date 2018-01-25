package dae.fxcreator.io.templates;

import dae.fxcreator.io.templates.FXProjectTemplateGroup;
import dae.fxcreator.io.templates.FXProjectTemplate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class keeps track of the project templates.
 *
 * @author Koen
 */
public class FXProjectTemplates {

    private HashMap<String, FXProjectTemplateGroup> templateGroupMap = new HashMap<String, FXProjectTemplateGroup>();
    private ArrayList<FXProjectTemplateGroup> templateGroups = new ArrayList<FXProjectTemplateGroup>();
    private String startTemplate;

    /**
     * Creates a new FXProjectTemplates object.
     */
    public FXProjectTemplates() {
    }

    /**
     * Adds a new group to the project templates.
     *
     * @param group the group to add to the list of FXProjectTemplateGroups.
     */
    public void addTemplateGroup(FXProjectTemplateGroup currentGroup) {
        templateGroupMap.put(currentGroup.getName(), currentGroup);
        templateGroups.add(currentGroup);
    }

    /**
     * Returns the groups in this FXProjectTemplates object.
     *
     * @return an iterable with the groups.
     */
    public Iterable<FXProjectTemplateGroup> getGroups() {
        return templateGroups;
    }

    /**
     * Returns the template or null if the template was not found.
     *
     * @param group the name of the template group.
     * @param templateName the name of the template.
     * @return
     */
    public FXProjectTemplate getTemplate(String group, String templateName) {
        FXProjectTemplateGroup g = this.templateGroupMap.get(group);
        if (g != null) {
            return g.getTemplate(templateName);
        } else {
            return null;
        }
    }

    /**
     * Sets the start project for the editor.
     *
     * @param templateName the name of the start template.
     */
    public void setStartProject(String templateName) {
        this.startTemplate = templateName;
    }

    /**
     * Returns the start project for the editor.
     *
     * @return the start project for the editor.
     */
    public FXProjectTemplate getStartProject() {
        int dotIndex = startTemplate.indexOf('.');
        if (dotIndex > -1) {
            String group = startTemplate.substring(0, dotIndex);
            String name = startTemplate.substring(dotIndex + 1);
            return getTemplate(group, name);
        } else {
            return null;
        }
    }

    public FXProjectTemplate getTemplate(String fullName) {
        int dotIndex = fullName.indexOf('.');
        if (dotIndex > -1) {
            String group = fullName.substring(0, dotIndex);
            String name = fullName.substring(dotIndex + 1);
            return getTemplate(group, name);
        } else {
            return null;
        }
    }

    /**
     * Checks if a group allready exists.
     *
     * @param groupName the name of the group
     * @return true if the group exists,false otherwise.
     */
    public boolean hasGroup(String groupName) {
        return this.templateGroupMap.containsKey(groupName);
    }

    /**
     * Returns an FXProjectTemplateGroup object.
     *
     * @param groupName the name of the group.
     * @return the FXProjectTemplateGroup if it exists or null otherwise.
     */
    public FXProjectTemplateGroup getGroup(String groupName) {
        return templateGroupMap.get(groupName);
    }

    /**
     * Returns the number of groups in this project template group.
     *
     * @return the number of groups.
     */
    public int getNrOfGroups() {
        return templateGroups.size();
    }

    /**
     * Returns the first group in the project templates if possible.
     * @return the first group or null if no groups are available.
     */
    public FXProjectTemplateGroup getFirstGroup() {
        if (templateGroups.size() > 0) {
            return templateGroups.get(0);
        } else {
            return null;
        }
    }
}
