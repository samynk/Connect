package dae.fxcreator.io.loaders;

import dae.fxcreator.io.FXProjectTemplate;
import dae.fxcreator.io.FXProjectTemplateGroup;
import dae.fxcreator.io.FXProjectTemplates;
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
 * Loads the project templates.
 * @author Koen
 */
public class FXProjectTemplateLoader extends DefaultHandler {

    /**
     * The resulting template object
     */
    private FXProjectTemplates template = new FXProjectTemplates();
    /**
     * The current FXProjectTemplateGroup
     */
    private FXProjectTemplateGroup currentGroup;
    /**
     * The current FXProjectTemplate.
     */
    private FXProjectTemplate currentTemplate;
    /**
     * The location of the template file.
     */
    private File file;

    private enum Element {

        FILE, DESCRIPTION
    };
    private Element currentElement;

    /**
     * Creates a new FXProjectTemplateLoader object.
     */
    public FXProjectTemplateLoader() {
        file = new File(System.getProperty("user.dir"), "fxtemplates/default.daefx");
    }

    /**
     * Loads the current FXProjectTemplates
     * @return the available project templates.
     */
    public FXProjectTemplates load() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(file, this);

        } catch (IOException ex) {
            Logger.getLogger(FXProjectLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(FXProjectLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(FXProjectLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return template;
    }
    private StringBuffer buffer = new StringBuffer();

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        buffer.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("file".equals(qName)) {
            String fileName = buffer.toString();
            if (fileName.startsWith("./")) {
                // relative file.
                String absoluteFile = System.getProperty("user.dir") + "/fxtemplates/" + currentGroup.getName() + fileName.substring(1);
                File templateFile = new File(absoluteFile);
                currentTemplate.setSourceFile(templateFile);
                currentTemplate.setRelativePath(fileName);
            } else {
                currentTemplate.setSourceFile(new File(fileName));
            }
        } else if ("description".equals(qName)) {
            currentTemplate.setDescription(buffer.toString());
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("group".equals(qName)) {
            currentGroup = new FXProjectTemplateGroup();
            currentGroup.setName(attributes.getValue("name"));
            currentGroup.setUILabel(attributes.getValue("label"));
            template.addTemplateGroup(currentGroup);
        } else if ("template".equals(qName)) {
            String name = attributes.getValue("name");
            String label = attributes.getValue("label");
            currentTemplate = new FXProjectTemplate();
            currentTemplate.setName(name);
            currentTemplate.setUILabel(label);
            currentGroup.addTemplate(currentTemplate);
        } else if ("file".equals(qName)) {
            currentElement = Element.FILE;
            buffer.delete(0, buffer.length());
        } else if ("description".equals(qName)) {
            currentElement = Element.DESCRIPTION;
            buffer.delete(0, buffer.length());
        } else if ("startproject".equals(qName)) {
            String templateName = attributes.getValue("template");
            template.setStartProject(templateName);
        } else if ( "iorule".equals(qName)){
            String rule= attributes.getValue("outputRule");
        }
    }
}
