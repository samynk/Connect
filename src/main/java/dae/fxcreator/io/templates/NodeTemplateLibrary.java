package dae.fxcreator.io.templates;

import dae.fxcreator.io.PathUtil;
import dae.fxcreator.io.loaders.FXMethodLoader;
import dae.fxcreator.io.savers.FXMethodListSaver;
import dae.fxcreator.io.savers.FXMethodSaver;
import dae.fxcreator.io.type.ShaderTypeLibrary;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.NodeContainer;
import dae.fxcreator.node.SettingsGroup;
import dae.fxcreator.node.ShaderType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * This library object contains all the templates that are available in a shader project.
 * This NodeTemplateLibrary can be used as a tree node in a tree control.
 * @author Koen
 */
public class NodeTemplateLibrary implements TreeModel {

    private final ArrayList<TemplateGroup> groups = new ArrayList<>();
    private final HashMap<String, TemplateGroup> groupsMap = new HashMap<>();
    private final HashMap<String, ArrayList<TemplateGroup>> categoryMap = new HashMap<>();
    private final HashMap<String, NodeContainerProxy> methodMap = new HashMap<>();
    private final Path location;
    /**
     * The types that can be used in the project.
     */
    private final ShaderTypeLibrary typeLibrary = new ShaderTypeLibrary();

    /**
     * Creates a new NodeTemplateLibrary object.
     * @param location the location of the node template library.
     */
    public NodeTemplateLibrary(Path location) {
        this.location = location;
        generalNode.clearSettings();
    }

    /**
     * Adds a new TemplateGroup to the list of groups.
     * @param group the group to add.
     */
    public void addGroup(TemplateGroup group) {
        groups.add(group);
        groupsMap.put(group.getName(), group);

        ArrayList<TemplateGroup> categoryList = categoryMap.get(group.getCategory());
        if (categoryList == null) {
            categoryList = new ArrayList<>();
            categoryMap.put(group.getCategory(), categoryList);
        }
        categoryList.add(group);
    }

    /**
     * Returns the list of template groups.
     * @return the list of template groups.
     */
    public List<TemplateGroup> getTemplateGroups() {
        return groups;
    }

    /**
     * Returns the list of TemplateGroups for a specific category.
     * @param category the category of the template group.
     * @return
     */
    public Iterable<TemplateGroup> getTemplateGroups(String category) {
        return categoryMap.get(category);
    }

    public NodeTemplate getNodeTemplate(String command) {
        if (command == null) {
            return null;
        }
        int dotIndex = command.indexOf('.');
        if (dotIndex > 0) {
            String group = command.substring(0, dotIndex);
            String templateType = command.substring(dotIndex + 1);

            TemplateGroup tg = groupsMap.get(group);
            if (tg != null) {
                return tg.getNodeTemplateByType(templateType);
            }
        }
        return null;
    }
    ArrayList<TreeModelListener> listeners = new ArrayList<>();

    /**
     * Returns the root of the tree.
     * @return the root of the tree object.
     */
    @Override
    public Object getRoot() {
        return this;
    }

    /**
     * Gets the child from the parent object.
     * @param parent the parent object, could be this object, but also a TemplateGroup
     * child object.
     * @param index the index of the child object.
     * @return the child object.
     */
    @Override
    public Object getChild(Object parent, int index) {
        if (parent == this) {
            return groups.get(index);
        } else {
            if (parent instanceof TemplateGroup) {
                TemplateGroup tg = (TemplateGroup) parent;
                return tg.getNodeTemplates().get(index);
            }
        }
        return null;
    }

    /**
     * Gets the child count of the parent object.
     * @param parent the object to get the child count from.
     * @return the child count.
     */
    @Override
    public int getChildCount(Object parent) {
        if (parent == this) {
            return groups.size();
        } else {
            if (parent instanceof TemplateGroup) {
                TemplateGroup tg = (TemplateGroup) parent;
                return tg.getNodeTemplates().size();
            }
        }
        return 0;
    }

    /**
     * Checks if the node is a leaf, or contains other nodes.
     * @param node the node to check.
     * @return true if the node is a leaf, false otherwise.
     */
    @Override
    public boolean isLeaf(Object node) {
        if (node == this) {
            return false;
        } else {
            return !(node instanceof TemplateGroup);
        }
    }

    /**
     * Called when the value for a path has changed.
     * @param path the path that was changed
     * @param newValue the new value for the path.
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        //
    }

    /**
     * Gets the index of a child object.
     * @param parent the parent that stores the child object.
     * @param child the child itself.
     * @return the index.
     */
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent == this) {
            return groups.indexOf(child);
        } else {
            if (parent instanceof TemplateGroup) {
                TemplateGroup tg = (TemplateGroup) parent;
                return tg.getNodeTemplates().indexOf(child);
            }
        }
        return -1;
    }

    /**
     * Adds a tree model listener to the list of listeners.
     * @param l the TreeModelListener to add.
     */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(l);
    }

    /**
     * Removes a tree model listener from the list of listeners.
     * @param l the TreeModelListener to remove.
     */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }

    /**
     * Returns the type that is associate with the key.
     * @param key the key for the shader type.
     * @return the ShaderType object.
     */
    public ShaderType getType(String key) {
        return typeLibrary.getType(key);
    }

    /**
     * Adds a type to the library.
     * @param type the type to add.
     */
    public void addType(ShaderType type) {
        typeLibrary.addType(type);
    }

    /**
     * Returns the shader type library.
     * @return the shader type library.
     */
    public ShaderTypeLibrary getTypeLibrary() {
        return typeLibrary;
    }

    /**
     * Adds a method block to the library.
     * @param proxy the node container proxy.
     */
    public void addMethod(NodeContainerProxy proxy) {
        this.methodMap.put(proxy.getType(), proxy);
    }

    /**
     * Gets the NodeContainerProxy object for the specified type.
     * @param type the type for the NodeContainerProxy object.
     * @return the NodeContainerProxy object.
     */
    public NodeContainerProxy getMethod(String type) {
        return methodMap.get(type);
    }

    /**
     * Returns the list of currently available methods.
     * @return the list of currently availabel methods.
     */
    public Iterable<NodeContainerProxy> getMethods() {
        return methodMap.values();
    }

    /**
     * Saves a NodeContainer to the storage.
     * @param container the container to store.
     * @param proxy
     * @throws java.io.IOException when the directories cannot be created.
     */
    public void saveMethod(NodeContainer container,NodeContainerProxy proxy) throws IOException {
        String type = container.getSubType();
        String[] paths = type.split("\\.");
        Path startDir = location.getParent().resolve("groups");

        String subdir = "";
        for (int i = 0; i < paths.length - 1; ++i) {
            subdir += "/";
            subdir += paths[i];
        }
        if (subdir.length() > 0) {
            startDir = startDir.resolve(subdir);
            Files.createDirectories(startDir);
        }

        Path saveFilePath = startDir.resolve( paths[paths.length - 1] + ".daefx");
       
        
       
        FXMethodSaver saver = new FXMethodSaver(PathUtil.createUserDirPath(saveFilePath), container);
        saver.save();
        this.addMethod(proxy);
        saveMethodList();
    }

    /**
     * Saves the list of available groups to the groups.xml file.
     */
    public void saveMethodList() {
        Path groupFile = location.getParent().resolve("groups.xml");
        

        FXMethodListSaver saver = new FXMethodListSaver(this, PathUtil.createUserDirPath(groupFile));
        saver.save();
    }

    /**
     * Creates a NodeContainer starting from the type.
     * @param type the type for the NodeContainer object.
     * @return the new NodeContainer object.
     */
    public NodeContainer createNodeContainer(String type) {
        NodeContainerProxy proxy = this.getMethod(type);
        if (proxy != null && proxy.isLoaded()) {
            return proxy.createNodeContainer();
        }
        if (proxy == null) {
            return null;
        } else {
            String[] paths = type.split("\\.");
            Path startDir = location.getParent().resolve("groups");
            String subdir = "";
            for (int i = 0; i < paths.length - 1; ++i) {
                subdir += "/";
                subdir += paths[i];
            }
            if (subdir.length() > 0) {
                startDir = startDir.resolve(subdir);
            }
            Path groupPath =startDir.resolve(paths[paths.length - 1] + ".daefx");
            
            if (!Files.exists(groupPath)) {
                return null;
            } else {
                FXMethodLoader loader = new FXMethodLoader(groupPath, this);
                loader.load();
                proxy.setNodeContainer(loader.getResult());
                return proxy.createNodeContainer();
            }
        }
    }

    /**
     * Checks if the library contains a method of the given type.
     * @param type the type to check for.
     * @return true if the method is allready defined, false otherwise.
     */
    public boolean containsMethod(String type) {
        return this.methodMap.containsKey(type);
    }

    IONode generalNode = new IONode("general","general",null);

    /**
     * Adds a general setting to the list of settings.
     * @param s the setting to add.
     */
    public void addGeneralSetting(Setting s){
        generalNode.addSetting(s.getGroup(), s);
    }

    /**
     * Tries to find a general setting with the specified group and name.
     * @param group the group of the setting.
     * @param name the name of the setting.
     * @return the found setting.
     */
    public Setting getGeneralSetting(String group, String name) {
        return generalNode.getSetting(group, name);
    }

    public Iterable<SettingsGroup> getGeneralSettingGroups()
    {
        return generalNode.getSettingsGroups();
    }
}
