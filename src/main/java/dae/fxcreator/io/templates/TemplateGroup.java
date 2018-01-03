package dae.fxcreator.io.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class creates a subgroup of node templates that are related to each other.
 * @author Koen
 */
public class TemplateGroup {
    private String name;
    private String icon;
    private String category;

    private ArrayList<NodeTemplate>  nodeTemplates = new ArrayList<NodeTemplate>();
    private HashMap<String,NodeTemplate> nodeTemplateMap = new HashMap<String,NodeTemplate>();

    /**
     * Creates a new template group
     * @param category
     * @param name
     * @param icon
     */
    public TemplateGroup(String category,String name,String icon){
        this.category = category;
        this.name = name;
        this.icon = icon;
    }

    /**
     * Returns the name of this template group.
     * @return the name of the template group.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the template group
     * @param name the new name for the template group.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the location of the icon for the template group.
     * @return the icon for the template group.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the location of the icon for the template group.
     * @param icon the new icon location.
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Adds a node template to the current list of templates.
     * @param currentTemplate the currentTemplate.
     */
    public void addNodeTemplate(NodeTemplate currentTemplate) {
       nodeTemplates.add(currentTemplate);
       nodeTemplateMap.put(currentTemplate.getTypeName(),currentTemplate);
    }

    public List<NodeTemplate> getNodeTemplates() {
        return this.nodeTemplates;
    }

    public NodeTemplate getNodeTemplateByType(String templateType) {
        return  nodeTemplateMap.get(templateType);
    }

     /**
     * Determines the visibility of this group.
     * @param visible true if this group should be included in toolbars and
     * popup menus, false otherw
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns the category of the group.
     * @return the category of the group.
     */
    public String getCategory(){
        return category;
    }

    /**
     * Returns a string representation of this template group.
     * @return
     */
    @Override
    public String toString(){
        return name;
    }
}