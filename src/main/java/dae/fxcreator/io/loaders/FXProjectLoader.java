package dae.fxcreator.io.loaders;

import dae.fxcreator.node.project.ExportFile;
import dae.fxcreator.node.project.FXProjectType;
import dae.fxcreator.node.NodeGroup;
import dae.fxcreator.node.project.ShaderStage;
import dae.fxcreator.node.project.Pass;
import dae.fxcreator.node.project.Technique;
import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.io.*;
import dae.fxcreator.node.templates.NodeTemplate;
import dae.fxcreator.node.templates.NodeTemplateLibrary;
import dae.fxcreator.node.settings.Setting;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.IteratorNode;
import dae.fxcreator.node.NodeContainer;
import dae.fxcreator.node.ReferenceNode;
import dae.fxcreator.node.SettingsGroup;
import dae.fxcreator.node.ShaderField;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderNode;
import dae.fxcreator.node.ShaderOutput;
import dae.fxcreator.node.ShaderStruct;
import dae.fxcreator.node.ShaderType;
import dae.fxcreator.node.graph.math.MathLoader;
import java.awt.Point;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
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
 * Loads a project from file.
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class FXProjectLoader extends DefaultHandler {

    /**
     * The FXProject object to read.
     */
    private final FXProject project;
    /**
     * The node template library with the templates.
     */
    NodeTemplateLibrary library;
    /**
     * The current shaderstage.
     */
    ShaderStage currentStage;
    /**
     * The current shadernode.
     */
    ShaderNode currentNode;
    /**
     * The current technique
     */
    Technique currentTechnique;
    /**
     * The current pass
     */
    Pass currentPass;
    /**
     * The current exporter file.
     */
    ExportFile currentExportFile;
    Pattern positionMatcher;
    int defaultx, defaulty;
    private Setting currentSetting;
    private final StringBuffer charBuffer = new StringBuffer();
    /**
     * The shader nodes that are present in this FXProject.
     */
    private final HashMap<String, ShaderStage> stages = new HashMap<>();
    private ShaderStruct currentShaderStruct;
    private String currentInputStructID;
    private String currentOutputStructID;

    private enum ELEMENT {

        STATE, GLOBALNODE, CONTAINERNODE
    };
    ELEMENT currentElement;
    private final Stack<NodeGroup> nodeGroupStack = new Stack<>();
    private final HashMap<String, XMLHandler> extraHandlers = new HashMap<>();
    private XMLHandler currentExtraHandler = null;

    /**
     * Creates a new FXProjectLoader that loads a file into the project. The
     * NodeTemplateLibrary object is used to synchronize the setting options.
     *
     * @param project the project to load the file into.
     * @param library the library that provides the settings for the nodes.
     */
    public FXProjectLoader(FXProject project, NodeTemplateLibrary library) {
        this.project = project;
        this.library = library;
        positionMatcher = java.util.regex.Pattern.compile("\\[(-?\\d*),(-?\\d*)\\]");
        extraHandlers.put("mathformula", new MathLoader());
    }

    /**
     * Loads the project from the given file. The type of the project and the
     * node template library will be read from the project.
     *
     * @param path the path to load the file from.
     */
    public FXProjectLoader(Path path) {
        project = new FXProject(path);

        positionMatcher = java.util.regex.Pattern.compile("\\[(-?\\d*),(-?\\d*)\\]");
        extraHandlers.put("mathformula", new MathLoader());
    }

    /**
     * Loads the project from the given file.
     *
     * @return returns the loaded project, or null if the project could not be
     * loaded.
     */
    public FXProject load() {
        if (project != null) {
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                parser.parse(PathUtil.createBufferedInputStream(project.getFile()), this);
                return project;
            } catch (IOException | ParserConfigurationException | SAXException ex) {
                Logger.getLogger(FXProjectLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     * Adds a stage to the list of stages.
     *
     * @param stage the stage to add.
     */
    void addStage(ShaderStage stage) {
        stages.put(stage.getName(), stage);
    }

    /**
     * Finds a stage in the list of stages.
     *
     * @param name the name of the stage.
     * @return the ShaderStage object if found, null ohterwise.
     */
    ShaderStage findStage(String name) {
        return stages.get(name);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (currentExtraHandler != null) {
            currentExtraHandler.startElement(uri, localName, qName, attributes);
        }
        if ("stage".equals(qName)) {
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");
            Point inputPosition = this.parsePosition(attributes.getValue("inputPosition"));
            Point outputPosition = this.parsePosition(attributes.getValue("outputPosition"));
            ShaderStage stage = new ShaderStage(name, type);
            stage.getInputNode().setPosition(inputPosition.x, inputPosition.y);
            stage.getOutputNode().setPosition(outputPosition.x, outputPosition.y);

            currentInputStructID = attributes.getValue("inputStruct");
            currentOutputStructID = attributes.getValue("outputStruct");

            currentStage = stage;
            addStage(stage);
            project.addShaderStage(stage);
            nodeGroupStack.push(stage);
            currentElement = ELEMENT.CONTAINERNODE;
        } else if ("node".equals(qName)) {
            String id = attributes.getValue("id");
            String name = attributes.getValue("name");
            String position = attributes.getValue("position");
            String type = attributes.getValue("type");
            String sioEditable = attributes.getValue("ioEditable");
            String container = attributes.getValue("container");
            String inputanchor = attributes.getValue("inputanchor");
            String outputanchor = attributes.getValue("outputanchor");
            boolean ioEditable = false;
            if (sioEditable != null) {
                ioEditable = Boolean.parseBoolean(sioEditable);

            }
            ShaderNode node = null;
            if (container == null || "leaf".equals(container)) {
                NodeTemplate template = library.getNodeTemplate(type);
                node = new ShaderNode(id, name, type, null);
                if (template != null) {
                    node.setIcon(template.getIcon());
                }
            } else if ("iterator".equals(container)) {
                Point inputPosition = this.parsePosition(attributes.getValue("inputPosition"));
                Point outputPosition = this.parsePosition(attributes.getValue("outputPosition"));
                IteratorNode nc = new IteratorNode(id, type, library.getTypeLibrary());
                currentElement = ELEMENT.CONTAINERNODE;
                nc.getInputNode().setPosition(inputPosition.x, inputPosition.y);
                nc.getOutputNode().setPosition(outputPosition.x, outputPosition.y);
                node = nc;
            } else if ("group".equals(container)) {
                Point inputPosition = this.parsePosition(attributes.getValue("inputPosition"));
                Point outputPosition = this.parsePosition(attributes.getValue("outputPosition"));
                String subType = attributes.getValue("subtype");
                NodeContainer nc = library.createNodeContainer(subType);
                nc.setId(id);
                nc.setName(name);
                nc.setType(type);
                nc.setSubType(subType);

                nc.getInputNode().setPosition(inputPosition.x, inputPosition.y);
                nc.getOutputNode().setPosition(outputPosition.x, outputPosition.y);
                node = nc;
            }
            if (inputanchor != null) {
                node.setInputAnchor(inputanchor);
            }
            if (outputanchor != null) {
                node.setOutputAnchor(outputanchor);
            }

            node.setInputOutputEditable(ioEditable);

            Point p = this.parsePosition(position);
            node.setPosition(p.x, p.y);

            if (null == currentElement) {
                project.addGlobalNode(node);
            } else {
                switch (currentElement) {
                    case CONTAINERNODE:
                        NodeGroup ng = nodeGroupStack.peek();
                        ng.addNode(node);
                        break;
                    case STATE:
                        project.addState(node);
                        break;
                    default:
                        project.addGlobalNode(node);
                        break;
                }
            }

            currentNode = node;
            if (currentNode instanceof NodeGroup) {
                nodeGroupStack.push((NodeGroup) node);
            }
        } else if ("input".equals(qName)) {
            String name = attributes.getValue("name");
            ShaderType type = library.getType(attributes.getValue("type"));
            String semantic = attributes.getValue("semantic");

            if (semantic != null && semantic.length() == 0) {
                semantic = null;
            }
            ShaderInput input = new ShaderInput(currentNode, name, semantic, type);
            input.setConnectionString(attributes.getValue("connection"));

            String anchor = attributes.getValue("anchor");
            if (anchor != null) {
                input.setAnchor(anchor);
            }
            if (currentNode != null) {
                currentNode.addInput(input);
            } else if (currentStage != null) {
                currentStage.addInput(input);
            }

        } else if ("output".equals(qName)) {
            String name = attributes.getValue("name");
            ShaderType type = library.getType(attributes.getValue("type"));
            String semantic = attributes.getValue("semantic");
            String typeRule = attributes.getValue("typerule");
            ShaderOutput output = new ShaderOutput(currentNode, name, semantic, type, typeRule);
            output.setConnectionString(attributes.getValue("connection"));

            String anchor = attributes.getValue("anchor");
            if (anchor != null) {
                output.setAnchor(anchor);
            }
            if (currentNode != null) {
                currentNode.addOutput(output);
            } else if (currentStage != null) {
                currentStage.addOutput(output);
            }
        } else if ("setting".equals(qName)) {
            if (this.currentNode != null) {
                // node type is necessary to find template in library.
                NodeTemplate template = library.getNodeTemplate(currentNode.getType());
                if (template == null) {
                    return;
                }
                String group = attributes.getValue("group");
                SettingsGroup sg = template.getShaderNode().getSettingsGroups(group);
                String name = attributes.getValue("id");
                //System.out.println("finding :" + group + "." + name);
                Setting s = null;
                if (sg != null) {
                    s = sg.getSetting(name);
                } else {
                    // try to find a general setting.
                    s = library.getGeneralSetting(group, name);
                }
                if (s != null) {
                    try {
                        currentSetting = (Setting) s.clone();
                        this.project.addSymbolListener(currentSetting);
                        FXSettings settings = FXSingleton.getSingleton().getFXSettings();
                        if (settings != null) {
                            settings.addSymbolListener(currentSetting);
                        }
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
        } else if ("technique".equals(qName)) {
            String name = attributes.getValue("name");
            Technique shader = new Technique(project, name);
            currentTechnique = shader;
            project.addTechnique(shader);
            defaultx = 100;
            defaulty = 100;
        } else if ("pass".equals(qName)) {
            String name = attributes.getValue("name");
            Pass pass = new Pass(name);
            if (currentTechnique != null) {
                currentTechnique.addPass(pass);
            }
            currentPass = pass;
        } else if ("stageid".equals(qName)) {
            String id = attributes.getValue("id");
            ShaderStage stage = findStage(id);
            if (currentPass != null && stage != null) {
                currentPass.addShaderStage(stage);
            }
        } else if ("file".equals(qName)) {
            String exporterId = attributes.getValue("exporterId");
            String name = attributes.getValue("name");
            String extension = attributes.getValue("extension");
            currentExportFile = new ExportFile(name, extension);
            project.addExportDestination(exporterId, currentExportFile);
            charBuffer.delete(0, charBuffer.length());
        } else if ("project".equals(qName)) {
            String name = attributes.getValue("name");
            project.setName(name);
        } else if ("projecttype".equals(qName)) {
            String name = attributes.getValue("name");
            String version = attributes.getValue("version");
            String minorVersion = attributes.getValue("minorversion");

            int iVersion = Integer.parseInt(version);
            int iMinorVersion = Integer.parseInt(minorVersion);

            FXProjectType projectType = FXSingleton.getSingleton().findProjectType(name, iVersion, iMinorVersion);
            if (projectType != null) {
                project.setProjectType(projectType);
                library = project.getNodeTemplateLibrary();
            } else {
                // todo warn the user that the project type could not be found.
            }
        } else if ("value".equals(qName)) {
            charBuffer.delete(0, charBuffer.length());
        } else if ("struct".equals(qName)) {
            String id = attributes.getValue("id");
            currentShaderStruct = new ShaderStruct(id, library.getTypeLibrary());
            project.addShaderStruct(currentShaderStruct);
        } else if ("field".equals(qName)) {
            String type = attributes.getValue("type");
            String name = attributes.getValue("name");
            String semantic = attributes.getValue("semantic");
            ShaderField field = new ShaderField(name, semantic, library.getType(type));
            currentShaderStruct.addShaderField(field);
        } else if ("global".equals(qName)) {
            currentElement = ELEMENT.GLOBALNODE;
        } else if ("states".equals(qName)) {
            currentElement = ELEMENT.STATE;
        } else if ("rasterizerState".equals(qName)) {
            String id = attributes.getValue("id");
            ShaderNode rs = project.findState(id);
            if (rs != null) {
                currentPass.setRasterizerState(rs);
            }
        } else if ("refnode".equals(qName)) {
            String id = attributes.getValue("id");
            Point position = this.parsePosition(attributes.getValue("position"));
            NodeGroup ng = nodeGroupStack.peek();
            IONode reference = ng.findNode(id);
            if (reference != null) {
                ng.addReferenceNode(new ReferenceNode(reference, position));
            }
        } else if (this.extraHandlers.containsKey(qName)) {
            currentExtraHandler = extraHandlers.get(qName);
            if (currentExtraHandler != null) {
                currentExtraHandler.reset();
                currentExtraHandler.startElement(uri, localName, qName, attributes);
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

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentExtraHandler != null) {
            currentExtraHandler.endElement(uri, localName, qName);

            if (qName.equals(currentExtraHandler.getRootElementName())) {
                Object result = currentExtraHandler.getResult();
                this.currentSetting.setSettingValueAsObject(result);
                currentExtraHandler = null;
            }
        } else if ("node".equals(qName)) {
            NodeTemplate template = library.getNodeTemplate(currentNode.getType());
            if (template != null) {
                // synchronize new possible settings
                template.addNewSettings(currentNode);
                // adds the shader types that the node can accept.
                template.addAcceptShaderTypes(currentNode);
            }
            if (currentNode instanceof NodeGroup) {
                nodeGroupStack.pop();
                if (nodeGroupStack.peek() instanceof ShaderStage) {
                    currentStage = (ShaderStage) nodeGroupStack.peek();
                }
                ((NodeGroup) currentNode).connectNodes();
                currentNode = null;
            } else {
                addGeneralSettings(currentNode);
                if (!nodeGroupStack.isEmpty()) {
                    Object node = nodeGroupStack.peek();
                    if (node instanceof ShaderNode) {
                        currentNode = (ShaderNode) node;
                    } else {
                        currentNode = null;
                    }
                } else {
                    currentStage = null;
                    currentNode = null;
                }

            }

        } else if ("shader".equals(qName)) {
            currentTechnique = null;
        } else if ("pass".equals(qName)) {
            currentPass = null;
        } else if ("stage".equals(qName)) {
            nodeGroupStack.pop();
            currentStage.connectNodes();
            if (currentInputStructID != null) {
                ShaderStruct struct = this.project.getStruct(currentInputStructID);
                currentStage.setInputStruct(struct);
            }
            if (currentOutputStructID != null) {
                ShaderStruct struct = this.project.getStruct(currentOutputStructID);
                currentStage.setOutputStruct(struct);
            }
            currentInputStructID = null;
            currentOutputStructID = null;
            currentStage = null;
        } else if ("value".equals(qName)) {
            if (currentSetting != null) {
                currentSetting.setSettingValue(charBuffer.toString());
            }
        } else if ("file".equals(qName)) {
            String dir = charBuffer.toString();
            currentExportFile.setDirectory(Paths.get(dir));
        } else if (currentExtraHandler != null) {
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentExtraHandler != null) {
            currentExtraHandler.characters(ch, start, length);
        } else {
            charBuffer.append(ch, start, length);
        }
    }

    /**
     * Adds general settings to a node.
     *
     * @param node the node to add the general settings to.
     */
    private void addGeneralSettings(ShaderNode node) {
        for (SettingsGroup sg : library.getGeneralSettingGroups()) {
            for (Setting s : sg.getSettings()) {
                if (!node.hasSetting(s.getGroup(), s.getId())) {
                    try {
                        Setting cloned = (Setting) s.clone();
                        this.project.addSymbolListener(cloned);
                        FXSettings settings = FXSingleton.getSingleton().getFXSettings();
                        settings.addSymbolListener(cloned);
                        node.addSetting(s.getGroup(), cloned);
                    } catch (CloneNotSupportedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Pattern p = java.util.regex.Pattern.compile("\\[(\\d*),(\\d*)\\]");
        Matcher m = p.matcher("[100,0]");
        if (m.matches()) {
            String sx = m.group(1);
            String sy = m.group(2);
            System.out.println("[" + sx + "," + sy + "]");
        }
    }
}
