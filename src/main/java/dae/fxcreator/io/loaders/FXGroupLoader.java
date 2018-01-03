package dae.fxcreator.io.loaders;

import dae.fxcreator.io.templates.NodeContainerProxy;
import dae.fxcreator.io.templates.NodeTemplateLibrary;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Loads the groups as defined for the project. Only the location for the group
 * will be saved, the groups will be loaded as projects need them.
 * @author Koen
 */
public class FXGroupLoader extends DefaultHandler {

    private final NodeTemplateLibrary library;
    private final File file;

    public FXGroupLoader(File file, NodeTemplateLibrary library) {
        this.library = library;
        this.file = file;
    }

    /**
     * Loads the groups into the NodeTemplateLibrary.
     */
    public void load() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(file, this);
            // create the node connections.
            } catch (IOException ex) {
            Logger.getLogger(FXProjectLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(FXProjectLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(FXProjectLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("group".equals(qName)) {
            String prefix = attributes.getValue("prefix");
            String label = attributes.getValue("label");
            String type = attributes.getValue("type");
            String icon = attributes.getValue("icon");
            String description = attributes.getValue("description");

            NodeContainerProxy proxy = new NodeContainerProxy(prefix,type, icon, label,description);
            library.addMethod(proxy);
        }
    }
}
