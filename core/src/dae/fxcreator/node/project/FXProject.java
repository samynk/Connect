package dae.fxcreator.node.project;

import dae.fxcreator.node.NodeGroup;
import dae.fxcreator.node.TypedNode;
import dae.fxcreator.node.events.StageListener;
import dae.fxcreator.node.events.SymbolListener;
import dae.fxcreator.node.templates.NodeTemplateLibrary;
import dae.fxcreator.io.type.ShaderTypeLibrary;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.ShaderStruct;
import dae.fxcreator.node.transform.TemplateClassLibrary;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class describes an fx project.
 *
 * @author Koen
 */
public class FXProject implements TypedNode {

    /**
     * The name for the project
     */
    private String name;
    /**
     * The file where the project is stored.
     */
    private Path file;
    /**
     * The type of project. This identifier defines the nodes that can be used
     * in the project.
     */
    private FXProjectType type;
    /**
     * Indicates that the file was loaded from a template. If this is the case a
     * new file location needs to be chosen when the file is saved.
     */
    private boolean loadedFromTemplate;
    /**
     * The NodeTemplateLibrary
     */
    private NodeTemplateLibrary library = null;
    private final ArrayList<IONode> globalNodes = new ArrayList<>();
    private final HashMap<String, IONode> globalNodeMap = new HashMap<>();
    private final TechniqueCollection techniques;
    private final ShaderStageCollection stages;
    private StatesCollection states;
    private final HashMap<String, ExportFile> exportSettings = new HashMap<>();
    private final ShaderStructCollection shaderStructs;
    private final ArrayList<StageListener> stageListeners = new ArrayList<>();
    private final HashMap<String, ArrayList<SymbolListener>> symbolListeners = new HashMap<>();

    /**
     * Creates a new FXProject object.
     *
     * @param location the location of the project on the file system.
     * @param type defines the project type.
     */
    public FXProject(String location, FXProjectType type) {
        this(Paths.get(location), type);
    }

    /**
     * Creates a new FXProject object without a project type.
     *
     * @param path the location of the project on the file system.
     */
    public FXProject(Path path) {
        this(path, null);
    }

    /**
     * Creates a new FXProject object.
     *
     * @param location the location of the project on the file system.
     * @param type defines the project type.
     */
    public FXProject(Path location, FXProjectType type) {
        file = location;
        techniques = new TechniqueCollection(this);
        shaderStructs = new ShaderStructCollection(this);
        states = new StatesCollection(this);
        stages = new ShaderStageCollection(this);
        setProjectType(type);
    }

    /**
     * Sets the type of the project.
     *
     * @param type the current project type for this project.
     */
    public final void setProjectType(FXProjectType type) {
        if (type != null) {
            this.type = type;
            this.library = type.getNodeTemplateLibrary();
        }
    }

    /**
     * Returns the type of the project.
     *
     * @return the type of the project.
     */
    public final FXProjectType getProjectType() {
        return type;
    }

    /**
     * Implementation of the typed node interface.
     *
     * @return the type of this node.
     */
    @Override
    public String getType() {
        return name;
    }

    /**
     * Returns the node template library for this project.
     *
     * @return the node template library.
     */
    public NodeTemplateLibrary getNodeTemplateLibrary() {
        return library;
    }

    /**
     * Returns the shader type library that is bound to this FXProject object.
     *
     * @return the shader type library.
     */
    public ShaderTypeLibrary getShaderTypeLibrary() {

        return library != null ? library.getTypeLibrary() : null;
    }
    
    

    /**
     * Gets the first technique in this FXProject
     *
     * @return the first technique.
     */
    public Technique getFirstTechnique() {
        return techniques.getFirstTechnique();
    }

    /**
     * Sets the file for saving and loading the project.
     *
     * @param file the location to save the file.
     */
    public void setFile(Path file) {
        this.file = file;
    }

    /**
     * Gets the file for saving and loading the project.
     *
     * @return the location of the project.
     */
    public Path getFile() {
        return file;
    }

    /**
     * Adds a node as a global node.
     *
     * @param node the global node to add.
     */
    public void addGlobalNode(IONode node) {
        globalNodes.add(node);
        globalNodeMap.put(node.getId(), node);
    }

    /**
     * Adds a technique to the list of techniques defined in the project file.
     *
     * @param technique the technique to add.
     */
    public void addTechnique(Technique technique) {
        this.techniques.addTechnique(technique);

    }

    /**
     * Adds a shader stage to this FXProject
     *
     * @param stage the stage to add.
     */
    public void addShaderStage(ShaderStage stage) {
        stages.addShaderStage(stage);
        stage.setFXProject(this);
    }

    /**
     * Returns the shader stages of the project.
     *
     * @return the shader stages of the project.
     */
    public Iterable<ShaderStage> getStages() {
        return stages.getList();
    }
    
    /**
     * Returns the shader stage collection.
     * @return the shader stage collection.
     */
    public ShaderStageCollection getStageCollection(){
        return stages;
    }

    /**
     * Finds a stage with the specific id.
     *
     * @param id the id of the stage.
     * @return the ShaderStage object.
     */
    public ShaderStage findShaderStage(String id) {
        return stages.findShaderStage(id);
    }

    /**
     * Returns the list of global nodes.
     *
     * @return the list of global nodes.
     */
    public Iterable<IONode> getGlobalNodes() {
        return this.globalNodes;
    }

    /**
     * Returns the list of techniques.
     *
     * @return the list of techniques.
     */
    public Iterable<Technique> getTechniques() {
        return techniques.getTechniques();
    }

    /**
     * Finds a global node in the list of nodes.
     *
     * @param id the id of the global node.
     * @return the found global node, or null.
     */
    public IONode findGlobalNode(String id) {
        return globalNodeMap.get(id);
    }

    /**
     * Adds an export file for a given exporter id to the exporter list.
     *
     * @param exporterId the id for the exporter.
     * @param file the File to export to.
     */
    public void addExportDestination(String exporterId, ExportFile file) {
        this.exportSettings.put(exporterId, file);
    }

    /**
     * Returns the export destination for a given exporter id.
     *
     * @param exporterId the exporter id.
     * @return null if the export destination is not set, the file object
     * otherwise.
     */
    public ExportFile getExportDestination(String exporterId) {
        return exportSettings.get(exporterId);
    }

    /**
     * Returns the set of exporter ids in this project.
     *
     * @return the set of exporter ids in this project.
     */
    public Iterable<String> getExportDestinations() {
        return exportSettings.keySet();
    }

    /**
     * Returns the name of the project.
     *
     * @return the name of the project.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the project.
     *
     * @param name the new name of the project.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the id of the pass, which is the same as the name.
     *
     * @return the id, a synonym for the name.
     */
    @Override
    public String getId() {
        return name;
    }

    /**
     * The status of this project. If the project was started from a template,
     * this returns true, otherwise false.
     *
     * @return the loadedFromTemplate
     */
    public boolean isLoadedFromTemplate() {
        return loadedFromTemplate;
    }

    /**
     * Sets the status of this project. Set to true if the project was loaded
     * from a template, false if the project was loaded from fil.
     *
     * @param loadedFromTemplate the loadedFromTemplate to set
     */
    public void setLoadedFromTemplate(boolean loadedFromTemplate) {
        this.loadedFromTemplate = loadedFromTemplate;
    }

    /**
     * Adds a shader struct to the list of ShaderStruct objects.
     *
     * @param shaderStruct the current ShaderStruct object
     */
    public void addShaderStruct(ShaderStruct shaderStruct) {
        this.shaderStructs.addShaderStruct(shaderStruct);
        ArrayList<SymbolListener> sls = symbolListeners.get("STRUCT");
        if (sls != null) {
            for (SymbolListener listener : sls) {
                listener.symbolAdded("STRUCT", shaderStruct);
            }
        }
    }

    public Iterable<ShaderStruct> getStructs() {
        return shaderStructs.getStructs();
    }

    /**
     * Returns a string representation of this object.
     *
     * @return the string representation of the object.
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Gets a specific struct from this struct.
     *
     * @param structid the id of the struct.
     * @return the ShaderStruct object.
     */
    public ShaderStruct getStruct(String structid) {
        return shaderStructs.getStruct(structid);
    }

    /**
     * Returns the number of techniques in the technique collection.
     *
     * @return the number of techniques.
     */
    public int getNrOfTechniques() {
        return techniques.getChildCount();
    }

    /**
     * Removes a technique from the technique collection.
     *
     * @param technique the technique to remove.
     */
    public void removeTechnique(Technique technique) {
        techniques.removeTechnique(technique);
    }

    /**
     * Creates a unique name for a technique.
     *
     * @return a unique name for a new technique.
     */
    public String createUniqueTechniqueName() {
        return techniques.createUniqueTechniqueName();
    }

    /**
     * Adds a listener for stage event.
     *
     * @param listener the new listener for the stage event.
     */
    public void addStageListener(StageListener listener) {
        this.stageListeners.add(listener);
    }

    /**
     * Removes a listener for stage events.
     *
     * @param listener the listener to remove.
     */
    public void removeStageListener(StageListener listener) {
        this.stageListeners.remove(listener);
    }

    /**
     * Creates a new ShaderStage name with the specified prefix.
     *
     * @param prefix the prefix for the name.
     * @return a unique namd for the shaderstage.
     */
    public String createUniqueShaderStageName(String prefix) {
        return stages.createUniqueShaderStageName(prefix);
    }

    /**
     * Creates a unique new struct name.
     *
     * @return
     */
    public String createUniqueStructName() {
        return this.shaderStructs.createUniqueName();
    }

    /**
     * Creates a unique new state name.
     *
     * @param prefix the prefix to use for this name.
     * @return a unique new state name.
     */
    public String createUniqueStateName(String prefix) {
        return states.createUniqueStateName(prefix);
    }

    /**
     * Returns the collection of shader structs.
     *
     * @return the collection of shader structs.
     */
    public ShaderStructCollection getStructCollection() {
        return this.shaderStructs;
    }

    /**
     * Adds a state object to the list of states.
     *
     * @param state the state object to add.
     */
    public void addState(IONode state) {
        this.states.addState(state);

    }

    /**
     * Returns the StateCollection object.
     *
     * @return the state collection object.
     */
    public StatesCollection getStateColllection() {
        return this.states;
    }

    /**
     * Returns the states of the StateCollection object.
     *
     * @return
     */
    public Iterable<IONode> getStates() {
        return states.getStates();
    }

    /**
     * Finds a node with the rasterizer state settings.
     *
     * @param id the id of the node.
     * @return the IONode with the rasterizer state settings, or null if not
     * found.
     */
    public IONode findState(String id) {
        return states.findState(id);
    }

    /**
     * Adds a symbol listener to this FXProject.
     *
     * @param listener the object that will receive a notification if symbols
     * were added to the project.
     */
    public void addSymbolListener(SymbolListener listener) {
        Iterator<String> symbols = listener.getListenerSymbols();
        while (symbols.hasNext()) {
            String symbol = symbols.next();
            ArrayList<SymbolListener> sls = symbolListeners.get(symbol);
            if (sls == null) {
                sls = new ArrayList<>();
                symbolListeners.put(symbol, sls);
            }
            if (!sls.contains(listener)) {
                sls.add(listener);
                // send the symbols that are allready in the project
                if (symbol.equals("STRUCT")) {
                    sendStructSymbols(listener);
                }
            }
        }
    }

    /**
     * Sends the struct symbols to the listener
     *
     * @param listener the listener to send the struct symbols to.
     */
    private void sendStructSymbols(SymbolListener listener) {
        for (ShaderStruct struct : this.shaderStructs.getStructs()) {
            listener.symbolAdded("STRUCT", struct);
        }
    }

    /**
     * Checks all the shader stages to see if an id is present.
     *
     * @param id the id to check.
     * @return true if the id is present, false otherwise
     */
    public boolean hasId(String id) {
        for (ShaderStage stage : this.getStages()) {
            if (stage.hasId(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the shader stage from this project.
     *
     * @param currentPass the current pass.
     * @param stage the stage to remove
     * @param checkForUsage if true, this method will check if the shader stage
     * is used in other passes. If the shader stage is only used in the current
     * pass, the shader stage will be removed, otherwise the shader stage will
     * remain in the project, and only be removed from the pass.
     */
    public void removeShaderStage(Pass currentPass, ShaderStage stage, boolean checkForUsage) {
        boolean otherFound = false;
        if (checkForUsage) {
            for (Technique t : this.getTechniques()) {
                for (Pass p : t.getPasses()) {
                    if (p == currentPass) {
                        continue;
                    }
                    if (p.hasStage(stage)) {
                        otherFound = true;
                    }
                }
            }
            if (!otherFound) {
                currentPass.removeShaderStage(stage);
                this.stages.removeShaderStage(stage);
            }
        }

    }

    /**
     * Gets the default extension to use for the given exporter.
     *
     * @param exporterId the id of the exporter.
     * @return the default extension for the exporter.
     */
    public String getExportExtension(String exporterId) {
        if (type != null) {
            TemplateClassLibrary codegen = type.getCodeTemplateLibrary(exporterId);
            if (codegen != null) {
                return codegen.getDefaultExportExtension();
            } else {
                return "fx";
            }
        } else {
            return "fx";
        }
    }
     
    public NodeGroup getFirstStage() {
        return this.stages.getFirstStage();
    }
}
