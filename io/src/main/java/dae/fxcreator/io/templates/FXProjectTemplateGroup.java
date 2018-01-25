package dae.fxcreator.io.templates;

import dae.fxcreator.io.templates.FXProjectTemplate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A project template group contains FXProjectTemplates that can be used
 * to start new projects.
 * @author Koen
 */
public class FXProjectTemplateGroup {
    /**
     * The name of the project template group.
     */
    private String name;
    /**
     * The label for the project template group.
     */
    private String uiLabel;
    /**
     * The category for the group.
     */
    private String category;
    /**
     * A hashmap with the names of the templates and
     *the project templates.
     */
     private HashMap<String,FXProjectTemplate> templateMap = new HashMap<String,FXProjectTemplate>();
     private ArrayList<FXProjectTemplate> templates = new ArrayList<FXProjectTemplate>();
    /**
     * Creates a new FXProjectTemplateGroup.
     */
    public FXProjectTemplateGroup() {

    }

    /**
     * Returns the name of the FXProjectTemplateGroup.
     * @return the name of the FXProjectTemplateGroup.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the FXProjectTemplateGroup.
     * @param name the name of the FXProjectTemplateGroup.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the User Interface label of the FXProjectTemplateGroup.
     * @return the label for the FXProjectTemplateGroup.
     */
    public String getUILabel() {
        return uiLabel;
    }

    /**
     * Sets the user interface label o fthe FXProjectTemplateGroup.
     * @param uiLabel the ui label of the FXProjectTemplateGroup.
     */
    public void setUILabel(String uiLabel) {
        this.uiLabel = uiLabel;
    }

    /**
     * Adds a template to the list of templates.
     * @param currentTemplate the current template.
     */
    public void addTemplate(FXProjectTemplate currentTemplate) {
        templateMap.put(currentTemplate.getName(),currentTemplate);
        templates.add(currentTemplate);
        currentTemplate.setGroup(this.name);
    }

    /**
     * Returns the list of FXProjectTemplate objects.
     * @return the list of FXProjectTemplate objects.
     */
    public Iterable<FXProjectTemplate> getTemplates() {
        return templates;
    }

    /**
     * Returns the template with the specific template name.
     * @param templateName the name of the template
     * @return the FXProjectTemplate object with the templateName, or null
     * if the template does not exist.
     */
    public FXProjectTemplate getTemplate(String templateName) {
        return templateMap.get(templateName);
    }

   
}
