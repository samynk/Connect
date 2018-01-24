package dae.fxcreator.node.project;

import dae.fxcreator.node.NodeGroup;
import dae.fxcreator.io.PathUtil;
import dae.fxcreator.node.TypedNode;
//import dae.fxcreator.io.codegen.parser.TemplateClassLibrary;
import dae.fxcreator.io.events.StageListener;
import dae.fxcreator.io.events.SymbolListener;
import dae.fxcreator.node.templates.NodeTemplateLibrary;
import dae.fxcreator.io.templates.NodeTemplateLoader;
import dae.fxcreator.io.type.ShaderTypeLibrary;
import dae.fxcreator.node.ShaderField;
import dae.fxcreator.node.ShaderNode;
import dae.fxcreator.node.ShaderStruct;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * This class describes an fx project.
 *
 * @author Koen
 */
public class FXProject implements TypedNode, TreeModel, TreeNode {

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
    private final ArrayList<ShaderNode> globalNodes = new ArrayList<>();
    private final HashMap<String, ShaderNode> globalNodeMap = new HashMap<>();
    private final TechniqueCollection techniques;
    private final ShaderStageCollection stages;
    private StatesCollection states;
    private final HashMap<String, ExportFile> exportSettings = new HashMap<>();
    private final ShaderStructCollection shaderStructs;
    private final ArrayList<TreeModelListener> listeners = new ArrayList<>();
    private final ArrayList<StageListener> stageListeners = new ArrayList<>();
    private final HashMap<String, ArrayList<SymbolListener>> symbolListeners = new HashMap<>();

    /**
     * Creates a new FXProject object.
     *
     * @param location the location of the project on the file system.
     * @param type defines the project type.
     */
    public FXProject(String location, FXProjectType type) {
        this(Paths.get(location),type);
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
     * @param type the current project type for this project.
     */
    public final void setProjectType(FXProjectType type) {
        if (type != null) {
            this.type = type;
            loadNodeTemplateLibrary(type);
        }
    }
    
    /**
     * Returns the type of the project.
     * @return the type of the project.
     */
    public final FXProjectType getProjectType(){
        return type;
    }
    
    /**
     * Implementation of the typed node interface.
     * @return the type of this node.
     */
    @Override
    public String getType(){
        return name;
    }

    private void loadNodeTemplateLibrary(FXProjectType type1) {
        String sNodesFile = type1.getNodesFile();
        Path nodesFile = PathUtil.createUserDirPath(sNodesFile);
        NodeTemplateLoader ntl = new NodeTemplateLoader(nodesFile);
        library = ntl.load();
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
        
        return library!=null?library.getTypeLibrary():null;
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
    public void addGlobalNode(ShaderNode node) {
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
    public Iterable<ShaderNode> getGlobalNodes() {
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
    public ShaderNode findGlobalNode(String id) {
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
     * Returns the root of the tree.
     *
     * @return
     */
    @Override
    public Object getRoot() {
        return this;
    }

    /**
     * Gets a child of the specified parent. Either the parent is this object,
     * or it is an instance of of the TreeNode class.
     *
     * @param parent the parent object to get the child from.
     * @param index the index of the child.
     * @return the child object at the specific index.
     */
    @Override
    public Object getChild(Object parent, int index) {
        if (parent == this) {
            switch (index) {
                case 0:
                    return shaderStructs;
                case 1:
                    return states;
                case 2:
                    return stages;
                case 3:
                    return techniques;
                default:
                    break;
            }
        } else if (parent instanceof TreeNode) {
            TreeNode node = (TreeNode) parent;
            return node.getChildAt(index);
        }
        return null;
    }

    /**
     * Gets the child count of the provided tree node object.
     *
     * @param parent the parent to get the child node from.
     * @return the child count.
     */
    @Override
    public int getChildCount(Object parent) {
        if (parent == this) {
            return 4;
        } else if (parent instanceof TreeNode) {
            TreeNode node = (TreeNode) parent;
            return node.getChildCount();
        }
        return 0;
    }

    /**
     * Checks if this node is a leaf.
     *
     * @param node the node to check.
     * @return true if the node is a leaf, false if the node is not a leaf.
     */
    @Override
    public boolean isLeaf(Object node) {
        if (node == this) {
            return false;
        } else if (node instanceof TreeNode) {
            TreeNode tn = (TreeNode) node;
            return tn.isLeaf();
        } else {
            return true;
        }
    }

    /**
     * Called when a cell was edited.
     *
     * @param path the path that was changed.
     * @param newValue the new value for the display name.
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // change the name.
        Object o = path.getLastPathComponent();
        if (o instanceof Technique) {
            Technique t = (Technique) o;
            t.setName(newValue.toString());
        } else if (o instanceof Pass) {
            Pass p = (Pass) o;
            p.setName(newValue.toString());
        } else if (o instanceof ShaderStage) {
            ShaderStage ss = (ShaderStage) o;
            ss.setId(newValue.toString());
        } else if (o instanceof ShaderField) {
            ShaderField sf = (ShaderField) o;
            sf.decode(newValue.toString());
        }
    }

    /**
     * Gets the index of the child object.
     *
     * @param parent the parent of the child object.
     * @param child the child object.
     * @return the index.
     */
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof TreeNode) {
            TreeNode node = (TreeNode) parent;
            return node.getIndex((TreeNode) child);
        } else {
            return -1;
        }
    }

    /**
     * Adds a TreeModel listener to the list of listeners.
     *
     * @param l the listener to add.
     */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(l);
    }

    /**
     * Removes the treemodel listener from the list of listeners.
     *
     * @param l the listener to remove.
     */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        switch (childIndex) {
            case 0:
                return shaderStructs;
            case 1:
                return states;
            case 2:
                return stages;
            default:
                return null;
        }
    }

    @Override
    public int getChildCount() {
        return 4;
    }

    @Override
    public TreeNode getParent() {
        return null;
    }

    @Override
    public int getIndex(TreeNode node) {
        if (node == shaderStructs) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Enumeration children() {
        return null;
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
     * Notifies the listeners that a row was removed from the model.
     *
     * @param toRemove the tree node that was removed.
     * @param path the path to the tree node.
     * @param index the index of the node in the parent path.
     */
    public void notifyNodeRemoved(TreeNode toRemove, TreePath path, int index) {
        int indices[] = {index};
        Object removed[] = {toRemove};
        TreeModelEvent tme = new TreeModelEvent(this, path, indices, removed);
        for (TreeModelListener listener : listeners) {
            listener.treeNodesRemoved(tme);
        }
    }

    /**
     * Notifies the listeners that a row was added to the model.
     *
     * @param toAdd the node that was added to the tree.
     * @param path the parent path.
     * @param index the index in the parent path.
     */
    public void notifyNodeAdded(TreeNode toAdd, TreePath path, int index) {
        int indices[] = {index};
        Object added[] = {toAdd};

        TreeModelEvent tme = new TreeModelEvent(this, path, indices, added);
        for (TreeModelListener listener : listeners) {
            listener.treeNodesInserted(tme);
        }
    }

    /**
     * Notifies the listeners that the struct from a certain point was changed.
     *
     * @param selectionPath
     */
    public void notifyNodeChanged(TreePath selectionPath) {
        TreeModelEvent tme = new TreeModelEvent(this, selectionPath);
        for (TreeModelListener listener : listeners) {
            listener.treeStructureChanged(tme);
        }
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
    public void addState(ShaderNode state) {
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
    public Iterable<ShaderNode> getStates() {
        return states.getStates();
    }

    /**
     * Finds a node with the rasterizer state settings.
     *
     * @param id the id of the node.
     * @return the ShaderNode with the rasterizer state settings, or null if not
     * found.
     */
    public ShaderNode findState(String id) {
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
    /*
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
    */

    public NodeGroup getFirstStage() {
        return this.stages.getFirstStage();
    }
}
