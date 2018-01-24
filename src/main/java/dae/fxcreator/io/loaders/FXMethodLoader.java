package dae.fxcreator.io.loaders;

import dae.fxcreator.node.NodeGroup;
import dae.fxcreator.node.templates.NodeTemplate;
import dae.fxcreator.node.templates.NodeTemplateLibrary;
import dae.fxcreator.node.settings.Setting;
import dae.fxcreator.node.IteratorNode;
import dae.fxcreator.node.NodeContainer;
import dae.fxcreator.node.SettingsGroup;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderNode;
import dae.fxcreator.node.ShaderOutput;
import dae.fxcreator.node.ShaderType;
import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class helps to load method definitions.
 * @author Koen
 */
public class FXMethodLoader extends DefaultHandler {

    /**
     * The location for the method definition
     */
    private Path location;
    /**
     * The node container that was loaded.
     */
    private NodeContainer result;
    /**
     * The node template library with the templates.
     */
    NodeTemplateLibrary library;

    private enum ELEMENT {

        STATE, GLOBALNODE, CONTAINERNODE
    };
    ELEMENT currentElement;
    private final Stack<NodeGroup> nodeGroupStack = new Stack<>();

    private ShaderNode currentNode;

    private Setting currentSetting;

    int defaultx, defaulty;

    private final Pattern positionMatcher;

    /**
     * Returns the result of the loading process.
     * @return the NodeContainer object.
     */
    public NodeContainer getResult() {
        return result;
    }

    /**
     * Creates a new FXMethodLoader object.
     * @param location the location for the file.
     * @param library the node template library
     */
    public FXMethodLoader(Path location,NodeTemplateLibrary library) {
        this.location = location;
        this.library = library;
        positionMatcher = java.util.regex.Pattern.compile("\\[(-?\\d*),(-?\\d*)\\]");
    }

    /**
     * load the node container object.
     */
    public void load() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            
            parser.parse(new BufferedInputStream(Files.newInputStream(location)), this);
            // create the node connections.
            System.out.println("node group read ! ");
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(FXProjectLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("method".equals(qName)) {
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");
            Point inputPosition = this.parsePosition(attributes.getValue("inputPosition"));
            Point outputPosition = this.parsePosition(attributes.getValue("outputPosition"));

            result = new NodeContainer(name, type);
            result.getInputNode().setPosition(inputPosition.x, inputPosition.y);
            result.getOutputNode().setPosition(outputPosition.x, outputPosition.y);
            nodeGroupStack.push(result);
            currentElement = ELEMENT.CONTAINERNODE;
        } else if ("node".equals(qName)) {
            String id = attributes.getValue("id");
            String name = attributes.getValue("name");
            String position = attributes.getValue("position");
            String type = attributes.getValue("type");
            String sioEditable = attributes.getValue("ioEditable");
            String container = attributes.getValue("container");
            boolean ioEditable = false;
            if (sioEditable != null) {
                ioEditable = Boolean.parseBoolean(sioEditable);

            }
            ShaderNode node = null;
            if (container == null || "leaf".equals(container)) {
                node = new ShaderNode(id, name, type, null);
            } else if ("iterator".equals(container)) {
                Point inputPosition = this.parsePosition(attributes.getValue("inputPosition"));
                Point outputPosition = this.parsePosition(attributes.getValue("outputPosition"));
                IteratorNode nc = new IteratorNode(id, type, library.getTypeLibrary());
                currentElement = ELEMENT.CONTAINERNODE;
                nc.getInputNode().setPosition(inputPosition.x, inputPosition.y);
                nc.getOutputNode().setPosition(outputPosition.x, outputPosition.y);
                node = nc;
            }
            node.setInputOutputEditable(ioEditable);

            Point p = this.parsePosition(position);
            node.setPosition(p.x, p.y);

            if (currentElement == ELEMENT.CONTAINERNODE) {
                NodeGroup ng = nodeGroupStack.peek();
                ng.addNode(node);
            } 

            currentNode = node;
            if (currentNode instanceof NodeGroup) {
                nodeGroupStack.push((NodeGroup) node);
            }
        } else if ("input".equals(qName)) {
            String name = attributes.getValue("name");
            ShaderType type = library.getType(attributes.getValue("type"));
            String semantic = attributes.getValue("semantic");
            ShaderInput input = new ShaderInput(currentNode, name, semantic, type);
            input.setConnectionString(attributes.getValue("connection"));
            if (currentNode != null) {
                currentNode.addInput(input);
            } else if (result != null) {
                result.addInput(input);
            }

        } else if ("output".equals(qName)) {
            String name = attributes.getValue("name");
            ShaderType type = library.getType(attributes.getValue("type"));
            String semantic = attributes.getValue("semantic");
            String typeRule = attributes.getValue("typerule");
            ShaderOutput output = new ShaderOutput(currentNode, name, semantic, type, typeRule);
            output.setConnectionString(attributes.getValue("connection"));
            if (currentNode != null) {
                currentNode.addOutput(output);
            } else if (result != null) {
                result.addOutput(output);
            }
        } else if ("setting".equals(qName)) {
            if (this.currentNode != null) {
                // node type is necessary to find template in library.
                NodeTemplate template = library.getNodeTemplate(currentNode.getType());
                String group = attributes.getValue("group");
                SettingsGroup sg = template.getShaderNode().getSettingsGroups(group);
                String name = attributes.getValue("id");
                System.out.println("finding :" + group + "." + name);
                Setting s = sg.getSetting(name);
                if (s != null) {
                    try {
                        currentSetting = (Setting) s.clone();
                        
                        // TODO : listen for new symbols.
                        //this.project.addSymbolListener(currentSetting);
                        currentNode.addSetting(group, currentSetting);
                        String value = attributes.getValue("value");
                        if (value != null) {
                            currentSetting.setSettingValue(value);
                        }
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(FXProjectLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private Point parsePosition(String position) {
        if (position == null) {
            return new Point(0, 0);
        }
        Matcher m = positionMatcher.matcher(position);
        if (!(position == null) && m.matches()) {
            int x = Integer.parseInt(m.group(1));
            int y = Integer.parseInt(m.group(2));
            return new Point(x, y);
        } else {
            Point p = new Point(defaultx, defaulty);
            defaultx += 10;
            defaulty += 10;
            return p;
        }
    }
}
