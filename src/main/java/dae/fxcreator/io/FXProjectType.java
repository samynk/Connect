package dae.fxcreator.io;

import dae.fxcreator.io.codegen.parser.TemplateClassLibrary;
import dae.fxcreator.io.templates.NodeTemplateLibrary;
import dae.fxcreator.node.ShaderType;
import dae.fxcreator.node.gui.ImageLoader;
import dae.fxcreator.util.Key;
import java.awt.Image;
import java.util.HashMap;
import java.util.Set;

/**
 * This class describes the project type for the project.
 * The project type defines the following properties:
 * * the name of the project type.
 * * the nodes that can be used within the project.
 * * the exporters that can be used for the project.
 * * the location(s) where the group nodes are stored.
 * @author Koen Samyn
 */
public class FXProjectType implements Key{
    private final String name;
    private final int version;
    private final int minorVersion;
    
    private String description;
    
    private String nodesFile;
    private String groupsFile;
    private String templatesFile;
    
    private NodeTemplateLibrary nodeTemplateLibrary;
    
    private final HashMap<String,TemplateClassLibrary> codeGenerators =
            new HashMap<>();
    
     /**
     * The hash map with the shader type - icon association.
     */
    private final HashMap<ShaderType, Image> typeIconMap = new HashMap<>();
    private final HashMap<ShaderType, String> typeIconLocationMap = new HashMap<>();
    
    /**
     * Creates a new FXProjectType.
     * @param name the name of the project type (must be a unique identifier.
     * @param version the version of the project type.
     */
    public FXProjectType(String name, int version){
        this.name = name;
        this.version = version;
        this.minorVersion = 0;
        this.nodesFile = null;
    }
    
        
    /**
     * Creates a new FXProjectType.
     * @param name the name of the project type (must be a unique identifier.
     * @param version the version of the project type.
     * @param minorVersion the minor version of this project type.
     * @param nodesFile the file with the description of the available nodes.
     * @param templatesFile the file with available templates for this type of project.
     */
    public FXProjectType(String name, int version, int minorVersion, String nodesFile,String templatesFile){
        this.name = name;
        this.version = version;
        this.minorVersion = minorVersion;
        this.nodesFile = nodesFile;
        this.templatesFile = templatesFile;
    }
    
    /**
     * Returns the key  of this project type. The key is constructed
     * as the name of the project type with the version and minorversion
     * included.
     * @return the key for this project type.
     */
    @Override
    public String getKey(){
        return getName() + "." + version + "." + minorVersion;
    }
    
    /**
     * Returns the name of the project type.
     * @return the name of the project type.
     */
    public String getName(){
        return name;
    }
    
    /**
     * Returns the version of the project type.
     * @return the version of the project type.
     */
    public int getVersion(){
        return version;
    }
    
    /**
     * Returns the minor version of the project type.
     * @return the minor version of the project type.
     */
    public int getMinorVersion(){
        return minorVersion;
    }
    
    /**
     * Sets the description for this project type.
     * @param description the description for this project type.
     */
    public void setDescription(String description){
        this.description = description;
    }
    
    /**
     * Returns a description of the project type.
     * @return a description of the project type.
     */
    public String getDescription(){
        return description;
    }
    
    
   
    
    /**
     * Adds a TemplateClassLibrarary to the list of possible code generators.
     * @param tcl the library to add.
     */
    public void addTemplateClassLibrary(TemplateClassLibrary tcl) {
        codeGenerators.put(tcl.getLabel(), tcl);
    }
    
    /**
     * Returns the CodeTemplateLibrary object with the given id.
     * @param id
     * @return 
     */
    public TemplateClassLibrary getCodeTemplateLibrary(String id){
        return codeGenerators.get(id);
    }
    
    /**
     * Sets the node templates to use in combination with this project type.
     * @param library the library to use.
     */
    public void setNodeTemplateLibrary(NodeTemplateLibrary library){
        this.nodeTemplateLibrary = library;
    }

    public String getNodesFile() {
        return nodesFile;
    }
    
    public void setNodesFile(String nodesFile){
        this.nodesFile = nodesFile;
    }

    public String getGroupsFile() {
        return groupsFile;
    }
    
    public void setGroupsFile(String groupsFile){
        this.groupsFile = groupsFile;
    }
    
    public String getTemplates(){
        return templatesFile;
    }

    public Iterable<TemplateClassLibrary> getExporterLibraries() {
        return codeGenerators.values();
    }

    /**
     * Returns the node templates to use in combination with this library.
     * @return the library to use.
     */
    public NodeTemplateLibrary getNodeTemplateLibrary() {
       return this.nodeTemplateLibrary;
    }
    
    /**
     * Adds an icon to display for the given type.
     * @param shaderType the shader type to add an icon for
     * @param icon the location for the icon.
     */
    public void addIconForType(ShaderType shaderType, String icon) {
        Image image = ImageLoader.getInstance().getImage(icon);
        this.typeIconMap.put(shaderType, image);
        this.typeIconLocationMap.put(shaderType, icon);
    }
    
    /**
     * Gets an icon for the given shadertype.
     * @param type the ShaderType to get an icon for.
     * @return the image , or null if no image was found.
     */
    public Image getIconForType(ShaderType type) {
        return typeIconMap.get(type);
    }

    /**
     * Returns the list of defined shadertypes.
     * @return the list of defined shader types.
     */
    public Set<ShaderType> getShaderTypes() {
        return typeIconMap.keySet();
    }

    /**
     * Returns the location for the icon of a shadertype.
     * @param type the type to get the icon location for.
     * @return the icon location.
     */
    public String getShaderTypeIconLocation(ShaderType type) {
        return typeIconLocationMap.get(type);
    }

    
}
