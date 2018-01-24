package dae.fxcreator.io;

import dae.fxcreator.node.project.FXProjectType;
import dae.fxcreator.io.codegen.CodeTemplateLibrary;
import dae.fxcreator.io.codegen.ExporterLibrary;
import dae.fxcreator.node.events.SymbolListener;
import dae.fxcreator.node.Semantic;
import dae.fxcreator.node.gui.GradientListModel;
import dae.fxcreator.node.gui.GraphFont;
import dae.fxcreator.node.gui.GraphFontListModel;
import dae.fxcreator.node.gui.GraphGradient;
import dae.fxcreator.node.gui.ImageLoader;
import dae.fxcreator.node.gui.NodeStyle;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.ListModel;

/**
 * This class contains all the general settings for an fx project
 * @author Koen
 */
public class FXSettings {

    /**
     * The available semantics for the project.
     */
    private final ArrayList<Semantic> semantics = new ArrayList<>();
    /**
     * The library with all the registered exporters.
     */
    private final ExporterLibrary library = new ExporterLibrary();
   
    /**
     * The hash map with the shader type - icon association.
     */
    private final HashMap<String, Image> semanticIconMap = new HashMap<>();
    /**
     * The hash map with the fonts.
     */
    private final HashMap<String, GraphFont> fontMap = new HashMap<>();
    /**
     * The hash map with the gradients.
     */
    private final HashMap<String, GraphGradient> gradientMap = new HashMap<>();
    /**
     * The hash map with all the styles.
     */
    private final HashMap<String, NodeStyle> styleMap = new HashMap<>();
    /**
     * The hash map with the colors.
     */
    private final HashMap<String, Color> colorMap = new HashMap<>();
    /**
     * Were the fxsettings changed since loading.
     */
    private boolean changed = false;
    /**
     * List of symbol listeners.
     */
    private final HashMap<String, ArrayList<SymbolListener>> symbolListeners = new HashMap<>();
    private boolean loaded =false;

    /**
     * The hash map with the list of ProjectType.
     */
    private final HashMap<String, FXProjectType> projectTypes = new HashMap<>();
    /**
     * Creates a new FXSettings object.
     */
    public FXSettings() {
        // create default styles, to keep the program working.
        addFont("title", "Times New Roman", 16, true, false, false, "#ffffff");
        addFont("portname", "Times New Roman", 12, false, false, false, "#ff00ff");
        addFont("semantic", "Times New Roman", 12, false, false, false, "#1010ff");
        addFont("constant", "Courier New", 12, false, true, false, "#ffffff");
        addFont("default", "Courier New",12,false,false,false,"#ffffff");

        Color background = new Color(255, 255, 255, 255);
        Color foreGround = new Color(123, 123, 255, 255);
        addGradient("node", foreGround, background);

        NodeStyle style = new NodeStyle("default");
        style.setTitleFontName("title");
        style.setGradientName("node");
        style.setSemanticFontName("semantic");
        style.setPortFontName("portname");

        this.addStyle(style);
    }

    private void addFont(String name, String fontFamily, int size, boolean bold, boolean italic, boolean underline, String color) {
        GraphFont gf = new GraphFont(name, fontFamily, size, bold, italic, underline, Color.decode(color));
        this.addFont(gf.getName(), gf);

    }

    private void addGradient(String name, Color color1, Color color2) {
        GraphGradient gg = new GraphGradient(name, color1, color2);
        this.addGradient(name, gg);
    }

    /**
     * Adds a semantic to the list of supported semantics.
     * @param s the semantic to add.
     */
    public void addSemantic(Semantic s) {
        if (semantics.add(s)) {
            changed = true;
        }

    }

    /**
     * Removes a semantic from the list of supported semantics.
     * @param s the semantic to remove.
     */
    public void removeSemantic(Semantic s) {
        if (semantics.remove(s)) {
            changed = true;
        }
    }

    /**
     * Returns the list of CodeTemplateLibrary objects.
     * @return the list of CodeTemplateLibrary objects.
     */
    public Iterable<CodeTemplateLibrary> exporterLibraries() {
        return library.getLibraries();
    }

    /**
     * Adds a CodeTemplateLibrary object to the list of code template library.
     * @param cgLibrary the library to add.
     */
    public void addCodeTemplateLibrary(CodeTemplateLibrary cgLibrary) {
        library.addCodeTemplateLibrary(cgLibrary);
    }

    /**
     * Gets a CodeTemplateLibrary object from the list of code template libraries.
     * @return the CodeTemplateLibrary object from the list of libraries.
     */
    public CodeTemplateLibrary getCodeTemplateLibrary(String id) {
        return library.getCodeTemplateLibrary(id);
    }

    /**
     * Returns the list of semantics.
     * @return the list of semantics.
     */
    public Iterable<Semantic> getSemantics() {
        return semantics;
    }

    /**
     * Is the settings object changed or not.
     * @return true if the settings were changed , false otherwise.
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Sets the changed state of this FXSettings object.
     * @param changed the new changed state for this FXSettings object.
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    

    /**
     * Adds an icon to display for the given semantic.
     * @param semantic the semantic to add an icon for.
     * @param icon the location for the icon.
     */
    public void addIconForSemantic(String semantic, String icon) {
        Image image = ImageLoader.getInstance().getImage(icon);
        this.semanticIconMap.put(semantic, image);
    }

    

    /**
     * Gets an icon for the given semantic.
     * @param semantic the semantic to get an icon for.
     * @return the image , or null if no image was found.
     */
    public Image getIconForSemantic(String semantic) {
        return semanticIconMap.get(semantic);
    }

    /**
     * Adds a font for use in the user interface.
     * @param key the key for the font.
     * @param font the font to store.
     */
    public void addFont(String key, GraphFont font) {
        this.fontMap.put(key, font);
    }

    /**
     * Gets a font for use in the user interface.
     * @param key the key for the font.
     */
    public GraphFont getFont(String key) {
        if ( fontMap.containsKey(key))
            return fontMap.get(key);
        else
            return fontMap.get("default");
    }

    /**
     * Gets a Font List model.
     * @return a list model with all the fonts.
     */
    public ListModel getAllFonts() {
        return new GraphFontListModel(this.fontMap.values());
    }

    public Collection<GraphFont> getAllFontsAsList() {
        return this.fontMap.values();
    }

    public ListModel getAllGradients() {
        return new GradientListModel(this.gradientMap.values());
    }

    public Collection<GraphGradient> getAllGradientsAsList() {
        return this.gradientMap.values();
    }

    public Collection<NodeStyle> getAllStylesAsList() {
        return this.styleMap.values();
    }

    public void addStyle(NodeStyle style) {
        styleMap.put(style.getName(), style);
        this.styleAdded(style);
    }

    public NodeStyle getStyle(String name) {
        return styleMap.get(name);
    }

    /**
     * Adds a gradient to the list of gradients.
     * @param key the key to add the gradient into the gradient map.
     * @param gg the gradient object.
     */
    public void addGradient(String key, GraphGradient gg) {
        this.gradientMap.put(key, gg);
    }

    /**
     * Gets a gradient from the list of gradients.
     * @param key the name of the gradient.
     * @return the gradient object.
     */
    public GraphGradient getGradient(String key) {
        return gradientMap.get(key);
    }

    /**
     * Adds a color to the list of colors.
     * @param key the key to add the color with.
     * @param color the color to use.
     */
    public void addColor(String key, Color color) {
        this.colorMap.put(key, color);
    }

    /**
     * Returns a color from the list of colors.
     * @param key the name for the color.
     * @return
     */
    public Color getColor(String key) {
        return colorMap.get(key);
    }

    /**
     * Returns the list of all defined gradients.
     * @return all the defined gradients.
     */
    public Iterable<String> getGradients() {
        return this.gradientMap.keySet();
    }

    /**
     * Checks if the style name is allready present in the list of styles.
     * @param styleName the style name to check.
     * @return true if the style exists, false otherwise.
     */
    public boolean hasStyle(String styleName) {
        return this.styleMap.containsKey(styleName);
    }

    /**
     * Checks if the font exists in thist settings object.
     * @param fontName the name of the font.
     * @return true if the font exists , false otherwise.
     */
    public boolean hasFont(String fontName) {
        return this.fontMap.containsKey(fontName);
    }

    /**
     * Adds a symbol listener to this FXProject.
     * @param listener the object that will receive a notification if symbols were
     * added to the project.
     */
    public void addSymbolListener(SymbolListener listener) {
        Iterator<String> symbols = listener.getListenerSymbols();
        while (symbols.hasNext()) {
            String symbol = symbols.next();
            ArrayList<SymbolListener> sls = symbolListeners.get(symbol);
            if (sls == null) {
                sls = new ArrayList<SymbolListener>();
                symbolListeners.put(symbol, sls);
            }
            if (!sls.contains(listener)) {
                sls.add(listener);
                // send the symbols that are allready in the project
                if (symbol.equals("UISTYLE")) {
                    sendUIStyles(listener);
                }
            }
        }
    }

    private void styleAdded(NodeStyle style) {
        ArrayList<SymbolListener> sls = symbolListeners.get("UISTYLE");
        if (sls != null) {
            for (SymbolListener sl : sls) {
                sl.symbolAdded("UISTYLE", style.getName());
            }
        }
    }

    private void sendUIStyles(SymbolListener listener) {
        for (NodeStyle style : this.styleMap.values()) {
            listener.symbolAdded("UISTYLE", style.getName());
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(){
        this.loaded = true;
    }

    /**
     * Adds a project type to the list of project types.
     * @param projectType the projectType to add.
     */
    public void addProjectType(FXProjectType projectType) {
        this.projectTypes.put(projectType.getName(),projectType);
    }

    /**
     * Returns the list of project types.
     * @return the list of project types.
     */
    public Iterable<FXProjectType> getProjectTypes() {
        return projectTypes.values();
    }

    /**
     * Finds the latest version of a specific project type.
     * @param projectType the id of the project type.
     * @return the latest version of the project type.
     */
    public FXProjectType findLatestProjectType(String projectType) {
        return this.projectTypes.get(projectType);
    }
}
