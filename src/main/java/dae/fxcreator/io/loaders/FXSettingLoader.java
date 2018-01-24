package dae.fxcreator.io.loaders;

import dae.fxcreator.node.project.FXProjectType;
import dae.fxcreator.io.FXSettings;
import dae.fxcreator.io.PathUtil;
import dae.fxcreator.io.codegen.CodeTemplateLibrary;
import dae.fxcreator.io.codegen.CodegenTemplateLoader;
import dae.fxcreator.node.templates.NodeTemplateLibrary;
import dae.fxcreator.io.templates.NodeTemplateLoader;
import dae.fxcreator.node.Semantic;
import dae.fxcreator.node.gui.GraphFont;
import dae.fxcreator.node.gui.GraphGradient;
import dae.fxcreator.node.gui.NodeStyle;
import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class loads the fxsettings for the project.
 *
 * @author Koen
 */
public class FXSettingLoader extends DefaultHandler {

    private FXSettings settings;
    private NodeTemplateLibrary library;

    private FXProjectType currentProjectType;

    /**
     * Creates a new FXSettingLoader.
     */
    public FXSettingLoader() {
    }

    /**
     * Load all the code generation templates.
     * @return the current FX settings
     */
    public FXSettings load() {
        settings = new FXSettings();
        Path file = PathUtil.createUserHomePath(".fxcreator/templates/settings.xml");
        
        if (!Files.exists(file)) {
            System.out.println(file.toString() + " does not exist!");
            file = PathUtil.createUserDirPath("templates/settings.xml");
        }
        if (Files.exists(file)) {
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                parser.parse(new BufferedInputStream(Files.newInputStream(file)), this);
                settings.setLoaded();
            } catch (IOException | ParserConfigurationException | SAXException ex) {
                Logger.getLogger(FXProjectLoader.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        settings.setChanged(false);
        return this.settings;
    }

    /**
     * Starts an element
     *
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("semantic".equals(qName)) {
            Semantic s = new Semantic(attributes.getValue("label"));
            settings.addSemantic(s);
        }  else if ("type".equals(qName)) {
            String value = attributes.getValue("value");
            String semantic = attributes.getValue("semantic");
            String icon = attributes.getValue("icon");
            if (value != null) {
                currentProjectType.addIconForType(library.getType(value), icon);
            } else if (semantic != null) {
                settings.addIconForSemantic(semantic, icon);
            }
        } else if ("font".equals(qName)) {
            String key = attributes.getValue("name");
            String fontSize = attributes.getValue("size");

            String color = attributes.getValue("color");
            String bold = attributes.getValue("bold");
            String italic = attributes.getValue("italic");
            String family = attributes.getValue("family");
            try {
                Integer fs = fontSize != null ? Integer.parseInt(fontSize) : 10;
                boolean b = bold != null ? Boolean.parseBoolean(bold) : false;
                boolean i = italic != null ? Boolean.parseBoolean(italic) : false;
                Color c = Color.GRAY;
                try {
                    c = color != null ? Color.decode(color) : Color.white;
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
                GraphFont gf = new GraphFont(key, family, fs, b, i, false, c);
                settings.addFont(key, gf);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if ("gradient".equals(qName)) {
            String key = attributes.getValue("name");
            String color1 = attributes.getValue("color1");
            String color2 = attributes.getValue("color2");
            Color c1 = Color.decode(color1);
            Color c2 = Color.decode(color2);
            GraphGradient gg = new GraphGradient(key, c1, c2);
            settings.addGradient(key, gg);
        } else if ("color".equals(qName)) {
            String key = attributes.getValue("name");
            String color = attributes.getValue("color");
            settings.addColor(key, Color.decode(color));
        } else if ("style".equals(qName)) {
            String key = attributes.getValue("name");
            String gradient = attributes.getValue("gradient");
            String titleFont = attributes.getValue("titleFont");
            String portFont = attributes.getValue("portFont");
            String semanticFont = attributes.getValue("semanticFont");
            NodeStyle style = new NodeStyle(key);
            style.setTitleFontName(titleFont);
            style.setPortFontName(portFont);
            style.setGradientName(gradient);
            style.setSemanticFontName(semanticFont);
            settings.addStyle(style);
        } else if ("projecttype".equals(qName)) {
            String name = attributes.getValue("name");
            String version = attributes.getValue("version");
            currentProjectType = new FXProjectType(name, Integer.parseInt(version));
            settings.addProjectType(currentProjectType);
        } else if ("nodes".equals(qName)) {
            String file = attributes.getValue("file");
            currentProjectType.setNodesFile(file);
            Path templatePath = PathUtil.createUserDirPath(file);
            if (Files.exists(templatePath)) {
                NodeTemplateLoader loader = new NodeTemplateLoader(templatePath);
                library = loader.load();
                currentProjectType.setNodeTemplateLibrary(library);
            }
        } else if ("groups".equals(qName)) {
            String groups = attributes.getValue("file");
            currentProjectType.setGroupsFile(groups);

            File groupFile = new File(System.getProperty("user.dir") + groups);
            if (groupFile.exists()) {
                FXGroupLoader gloader = new FXGroupLoader(groupFile, library);
                gloader.load();
            }
        } else if ("description".equals(qName)) {
            chars.delete(0, chars.length() - 1);
        }
    }

    StringBuilder chars = new StringBuilder();

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        chars.append(ch, start, length);
    }

    /**
     * Ends an element.
     *
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("description".equals(qName)) {
            String description = chars.toString();
            currentProjectType.setDescription(description);
        }
    }
}
