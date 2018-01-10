package dae.fxcreator.ui.usersettings;

import dae.fxcreator.ui.FXCreator;
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The UserSettings object tracks the files that the user opened, and also
 * keeps track of the user interface settings.
 * @author Koen
 */
public class UserSettings extends DefaultHandler {

    private enum LOADSTATE {

        FILE, UI
    };
    /**
     * The list of recent files.
     */
    private final ArrayList<Path> recentFiles = new ArrayList<>();
    /**
     * The hashmap with settings for the given resolution.
     */
    private final HashMap<String, UISettings> uiMap = new HashMap<>();
    private LOADSTATE state;
    private UISettings currentUISettings;
    private FXCreator frame;

    /**
     * Creates a new UserSettings object.
     */
    public UserSettings(FXCreator frame) {
        this.frame = frame;
        load();
    }

    /**
     * Loads the user settings.
     */
    private void load() {
        File file = new File(System.getProperty("user.home"),".umbrafx/settings.xml");
        if (file.exists()) {
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                parser.parse(file, this);
                
            } catch (IOException ex) {
                Logger.getLogger(UserSettings.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(UserSettings.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(UserSettings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //applySettings();
    }

    public void addRecentFile(Path file) {
        if (recentFiles.contains(file)) {
            return;
        }

        recentFiles.add(file);
        if (recentFiles.size() > 5) {
            recentFiles.remove(0);
        }
        //applySettings();
    }

    public void applySettings() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice gd : ge.getScreenDevices()) {
            GraphicsConfiguration framegc = frame.getGraphicsConfiguration();
            if (framegc.getDevice() == gd) {
                int width = gd.getDisplayMode().getWidth();
                int height = gd.getDisplayMode().getHeight();
                String resolution = width + "x" + height;
                UISettings settings = uiMap.get(resolution);
                if (settings == null) {
                    settings = new UISettings(resolution);
                    uiMap.put(resolution, settings);
                    settings.setLocation(0, 0);
                    settings.setSize(width, height);
                    settings.setFullscreen(true);
                    frame.setLocation(settings.getLocation());
                    frame.setSize(settings.getSize());
                    settings.setDividerLocation("editordivider", height/5);
                    settings.setDividerLocation("settingsdivider", (width * 4)/5);
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                } else {
                    frame.setLocation(settings.getLocation());
                    frame.setSize(settings.getSize());
                    if (settings.isFullscreen()) {
                        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    }
                }
                if (settings.hasDividerLocation("editordivider")) {
                    int editorLoc = settings.getDividerLocation("editordivider");
                    frame.setEditorDividerLocation(editorLoc);
                }else{
                    frame.setEditorDividerLocation(0.2);
                }
                if ( settings.hasDividerLocation("settingsdivider")){
                    int settingsLoc = settings.getDividerLocation("settingsdivider");
                    frame.setSettingsDividerLocation(settingsLoc);
                }else{
                    frame.setSettingsDividerLocation(0.8);
                }

                currentUISettings = settings;
            }
        }
    }

    /**
     * Saves the user settings.
     */
    public void save() {
        currentUISettings.setFullscreen(frame.getExtendedState() == JFrame.MAXIMIZED_BOTH);
        if (currentUISettings.isFullscreen()){
            DisplayMode mode = frame.getGraphicsConfiguration().getDevice().getDisplayMode();
            currentUISettings.setLocation((int)(mode.getWidth()*.05), (int)(mode.getHeight()*.05));
            currentUISettings.setSize((int)(mode.getWidth()*.9), (int)(mode.getHeight()*.9));
        }else{
            currentUISettings.setLocation(frame.getX(), frame.getY());
            currentUISettings.setSize(frame.getWidth(), frame.getHeight());
        }
        currentUISettings.setDividerLocation("editordivider", frame.getEditorDividerLocation());
        currentUISettings.setDividerLocation("settingsdivider", frame.getSettingsDividerLocation());

        //System.out.println(currentUISettings);

        File fileLocation = new File(System.getProperty("user.home"), "/.umbrafx/settings.xml");
        if ( !fileLocation.getParentFile().exists())
            fileLocation.getParentFile().mkdirs();
        //if (!file.exists()) {
        //    file.getParentFile().mkdirs();
        //}
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(fileLocation);
            bw = new BufferedWriter(fw);
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            bw.write("<settings>\n");
            bw.write("\t<recentfiles>\n");
            for (Path recentFile : recentFiles) {
                bw.write("\t\t<file><![CDATA[");
                bw.write(recentFile.toString());
                bw.write("]]></file>\n");
            }
            bw.write("\t</recentfiles>\n");

            for (UISettings settings : uiMap.values()) {
                bw.write("\t<ui ");
                writeAttribute(bw, "resolution", settings.getResolution());
                writeAttribute(bw, "fullscreen", Boolean.toString(settings.isFullscreen()));
                bw.write(">\n");
                bw.write("\t\t<location ");
                writeAttribute(bw, "x", Integer.toString(settings.getLocation().x));
                writeAttribute(bw, "y", Integer.toString(settings.getLocation().y));
                bw.write("/>\n");
                bw.write("\t\t<size ");
                
                writeAttribute(bw, "width", Integer.toString(settings.getSize().width));
                writeAttribute(bw, "height", Integer.toString(settings.getSize().height));
                bw.write("/>\n");
                bw.write("\t\t<divider ");
                writeAttribute(bw, "name", "editordivider");
                writeAttribute(bw, "location", ""+settings.getDividerLocation("editordivider"));
                bw.write("/>\n");

                bw.write("\t\t<divider ");
                writeAttribute(bw, "name", "settingsdivider");
                writeAttribute(bw, "location", ""+settings.getDividerLocation("settingsdivider"));
                bw.write("/>\n");

                bw.write("\t</ui>\n");
            }
            bw.write("</settings>\n");

        } catch (IOException ex) {
            Logger.getLogger(UserSettings.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(UserSettings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    private StringBuffer help = new StringBuffer();

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        help.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("file".equals(qName)) {
            String fileLocation = help.toString();
            Path file = Paths.get(fileLocation);
            if (Files.exists(file)) {
                recentFiles.add(file);
            }
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("file".equals(qName)) {
            state = LOADSTATE.FILE;
            help.delete(0, help.length());
        } else if ("ui".equals(qName)) {
            String resolution = attributes.getValue("resolution");
            currentUISettings = new UISettings();
            currentUISettings.setResolution(resolution);
            boolean fullscreen = Boolean.parseBoolean(attributes.getValue("fullscreen"));
            currentUISettings.setFullscreen(fullscreen);
            uiMap.put(resolution, currentUISettings);
        } else if ("location".equals(qName)) {
            int x = Integer.parseInt(attributes.getValue("x"));
            int y = Integer.parseInt(attributes.getValue("y"));
            currentUISettings.setLocation(x, y);
        } else if ("size".equals(qName)) {
            int width = Integer.parseInt(attributes.getValue("width"));
            int height = Integer.parseInt(attributes.getValue("height"));
            currentUISettings.setSize(width, height);
        } else if ("divider".equals(qName)) {
            int location = Integer.parseInt(attributes.getValue("location"));
            String name = attributes.getValue("name");
            currentUISettings.setDividerLocation(name, location);
        }
    }

    private void writeAttribute(BufferedWriter bw, String key, String value) throws IOException {
        bw.write(key);
        bw.write("=\"");
        bw.write(value);
        bw.write("\" ");
    }

    public Iterable<Path> getRecentFiles() {
        return recentFiles;
    }
}
