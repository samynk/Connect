package dae.fxcreator.io.templates;

import dae.fxcreator.node.templates.TemplateGroup;
import dae.fxcreator.node.templates.NodeTemplateLibrary;
import dae.fxcreator.node.templates.NodeTemplate;
import dae.fxcreator.node.settings.TextSetting;
import dae.fxcreator.node.settings.Setting;
import dae.fxcreator.node.settings.SemanticSetting;
import dae.fxcreator.node.settings.OptionSetting;
import dae.fxcreator.node.settings.MathSetting;
import dae.fxcreator.node.settings.IntSetting;
import dae.fxcreator.node.settings.ImageFileSetting;
import dae.fxcreator.node.settings.GradientSetting;
import dae.fxcreator.node.settings.FloatSetting;
import dae.fxcreator.node.settings.DoubleSetting;
import dae.fxcreator.node.settings.DefaultSetting;
import dae.fxcreator.node.settings.ColorSetting;
import dae.fxcreator.node.settings.CodeSetting;
import dae.fxcreator.node.settings.BooleanSetting;
import dae.fxcreator.io.PathUtil;
//import dae.fxcreator.io.loaders.FXProjectLoader;
import dae.fxcreator.node.SettingsGroup;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderNode;
import dae.fxcreator.node.ShaderOutput;
import dae.fxcreator.node.ShaderType;
import java.io.IOException;
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
 * This class loads the node templates from an xml description file. The node
 * templates contain the input nodes, the output nodes and the settings for a
 * given node.
 *
 * @author Koen
 */
public class NodeTemplateLoader extends DefaultHandler {

    private final Path path;
    /**
     * The resulting template library.
     */
    private final NodeTemplateLibrary library;
    /**
     * The current template group
     */
    private TemplateGroup currentGroup;
    /**
     * The current template.
     */
    private NodeTemplate currentTemplate;
    /**
     * The current option setting.
     */
    private OptionSetting currentOptionSetting;
    private TextSetting currentTextSetting;
    private FloatSetting currentFloatSetting;
    private DoubleSetting currentDoubleSetting;
    private SemanticSetting currentSemanticSetting;
    private ColorSetting currentColorSetting;
    private ImageFileSetting currentImageFileSetting;
    private CodeSetting currentCodeSetting;
    private IntSetting currentIntSetting;
    private BooleanSetting currentBooleanSetting;
    private GradientSetting currentGradientSetting;
    private DefaultSetting currentDefaultSetting;
    private MathSetting currentMathSetting;

    enum STATE {

        NODE, GENERAL
    };
    STATE currentState = STATE.NODE;

    /**
     * Creates a new NodeTemplateLoader object.
     *
     * @param path the path that leads to the node template library.
     */
    public NodeTemplateLoader(Path path) {
        this.path = path;
        library = new NodeTemplateLibrary(path);
    }

    /**
     * Load all the available templates.
     *
     * @return the node template.
     */
    public NodeTemplateLibrary load() {
        if (path != null) {
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                parser.parse(PathUtil.createUserDirStream(path), this);
            } catch (IOException | ParserConfigurationException | SAXException ex) {
                Logger.getLogger(NodeTemplateLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return this.library;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("group".equals(qName)) {
            String category = attributes.getValue("category");
            String name = attributes.getValue("name");
            String icon = attributes.getValue("icon");
            currentGroup = new TemplateGroup(category, name, icon);
            library.addGroup(currentGroup);
        } else if ("type".equals(qName)) {
            String type = attributes.getValue("name");
            String sorder = attributes.getValue("order");
            String sValueType = attributes.getValue("valueType");
            String icon = attributes.getValue("icon");
            // default is true
            boolean bValueType = sValueType != null ? Boolean.parseBoolean(sValueType) : true;
            int order = 0;
            try {
                order = Integer.parseInt(sorder);
            } catch (NumberFormatException ex) {
            }
            ShaderType st = new ShaderType(type, order, bValueType, icon);
            library.addType(st);

        } else if ("typeset".equals(qName)) {
            String name = attributes.getValue("name");
            String typesAttr = attributes.getValue("types");
            String[] types = typesAttr.split(",");
            for (int i = 0; i < types.length; ++i) {
                library.getTypeLibrary().addTypeToSet(name, types[i]);
            }
        } else if ("node".equals(qName)) {
            String type = attributes.getValue("type");
            String prefix = attributes.getValue("prefix");
            String icon = attributes.getValue("icon");
            String sioEditable = attributes.getValue("ioEditable");
            String containerType = attributes.getValue("container");

            String inputAnchor = attributes.getValue("inputanchor");
            if (inputAnchor == null) {
                inputAnchor = "NW";
            }
            String outputAnchor = attributes.getValue("outputanchor");
            if (outputAnchor == null) {
                outputAnchor = "NE";
            }

            boolean ioEditable = false;
            if (sioEditable != null) {
                ioEditable = Boolean.parseBoolean(sioEditable);
            }

            currentTemplate = new NodeTemplate(currentGroup.getName() + "." + type, prefix, icon, ioEditable, containerType, inputAnchor, outputAnchor);
            currentGroup.addNodeTemplate(currentTemplate);
            currentState = STATE.NODE;
        } else if ("generalsettings".equals(qName)) {
            currentState = STATE.GENERAL;
        } else if ("setting".equals(qName)) {
            String type = attributes.getValue("type");
            String id = attributes.getValue("id");
            String group = attributes.getValue("group");
            String label = attributes.getValue("label");
            String sValueAsAttribute = attributes.getValue("valueAsAttribute");
            String sVisualize = attributes.getValue("visualize");
            String sLabelVisible = attributes.getValue("labelVisible");
            String sValueAsXML = attributes.getValue("valueAsXML");
            String value = attributes.getValue("default");

            boolean valueAsAttribute = true;
            boolean visualize = false;
            boolean labelVisible = false;
            boolean valueAsXML = false;

            if (sValueAsAttribute != null) {
                valueAsAttribute = Boolean.parseBoolean(sValueAsAttribute);
            }
            if (sVisualize != null) {
                visualize = Boolean.parseBoolean(sVisualize);
            }
            if (sLabelVisible != null) {
                labelVisible = Boolean.parseBoolean(sLabelVisible);
            }
            if (sValueAsXML != null) {
                valueAsXML = Boolean.parseBoolean(sValueAsXML);
            }
            Setting current = null;

            if ("optionlist".equals(type)) {
                currentOptionSetting = new OptionSetting(id, label);
                current = currentOptionSetting;
            } else if ("text".equals(type)) {
                currentTextSetting = new TextSetting(id, label, value);
                current = currentTextSetting;
            } else if ("floatvector".equals(type)) {
                currentFloatSetting = new FloatSetting(id, label);
                current = currentFloatSetting;
            } else if ("doublevector".equals(type)) {
                currentDoubleSetting = new DoubleSetting(id, label);
                current = currentDoubleSetting;
            } else if ("semantic".equals(type)) {
                currentSemanticSetting = new SemanticSetting(id, label);
                current = currentSemanticSetting;
            } else if ("color".equals(type)) {
                currentColorSetting = new ColorSetting(id, label);
                current = currentColorSetting;
            } else if ("imagefile".equals(type)) {
                currentImageFileSetting = new ImageFileSetting(id, label);
                current = currentImageFileSetting;
            } else if ("code".equals(type)) {
                currentCodeSetting = new CodeSetting(id, label);
                current = currentCodeSetting;
            } else if ("intvector".equals(type)) {
                currentIntSetting = new IntSetting(id, label);
                current = currentIntSetting;
            } else if ("booleanvector".equals(type)) {
                currentBooleanSetting = new BooleanSetting(id, label);
                current = currentBooleanSetting;
            } else if ("gradient".equals(type)) {
                currentGradientSetting = new GradientSetting(id, label);
                current = currentGradientSetting;
            } else if ("math".equals(type)) {
                currentMathSetting = new MathSetting(id, label);
                current = currentMathSetting;
            } else if ("default".equals(type)) {
                currentDefaultSetting = new DefaultSetting(id, label, value);
                current = currentDefaultSetting;
            }
            if (current != null) {
                current.setWriteValueAsAttribute(valueAsAttribute);
                current.setVisualized(visualize);
                current.setLabelVisible(labelVisible);
                current.setValueAsXML(valueAsXML);
                current.setGroup(group);
                if (value != null) {
                    current.setSettingValue(value);
                }
                if (currentState == STATE.NODE) {
                    currentTemplate.addSetting(group, current);
                } else if (currentState == STATE.GENERAL) {
                    this.library.addGeneralSetting(current);
                }
            }

        } else if ("option".equals(qName)) {
            String value = attributes.getValue("value");
            if (value != null) {
                boolean selected = Boolean.parseBoolean(attributes.getValue("selected"));
                currentOptionSetting.addOption(value, selected);
            } else {
                value = attributes.getValue("dynvalue");
                if (value != null) {
                    currentOptionSetting.addListenerFor(value.toUpperCase());
                }
            }

        } else if ("output".equals(qName)) {
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");
            String semantic = attributes.getValue("semantic");
            String typeRule = attributes.getValue("typerule");
            String anchor = attributes.getValue("anchor");
            ShaderOutput so = new ShaderOutput(currentTemplate.getShaderNode(), name, semantic, library.getType(type), typeRule);
            if (anchor != null) {
                so.setAnchor(anchor);
            }
            currentTemplate.addOutput(so);
        } else if ("input".equals(qName)) {
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");
            String semantic = attributes.getValue("semantic");
            ShaderType st = library.getType(type);
            String acceptTypeSet = attributes.getValue("acceptTypeSet");
            String anchor = attributes.getValue("anchor");
            ShaderInput si = new ShaderInput(currentTemplate.getShaderNode(), name, semantic, st, acceptTypeSet);
            if (anchor != null) {
                si.setAnchor(anchor);
            }
            currentTemplate.addInput(si);

        } else if ("inputTemplate".equals(qName)) {
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");
            String semantic = attributes.getValue("semantic");
            ShaderType st = library.getType(type);
            String acceptTypeSet = attributes.getValue("acceptTypeSet");
            ShaderInput si = new ShaderInput(currentTemplate.getShaderNode(), name, semantic, st, acceptTypeSet);
            currentTemplate.addTemplateInput(si);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("node".equals(qName)) {
            addGeneralSettings(this.currentTemplate);
        }
    }

    /**
     * Adds general settings to a node.
     *
     * @param node the node to add the general settings to.
     */
    private void addGeneralSettings(NodeTemplate template) {
        ShaderNode node = template.getShaderNode();
        for (SettingsGroup sg : library.getGeneralSettingGroups()) {
            for (Setting s : sg.getSettings()) {
                try {
                    Setting cloned = (Setting) s.clone();
                    if (node.hasSetting(s.getGroup(), s.getId())) {
                        Setting original = node.getSetting(s.getGroup(), s.getId());
                        cloned.setSettingValue(original.getSettingValue());
                    }
                    node.addSetting(cloned.getGroup(), cloned, true);
                } catch (CloneNotSupportedException ex) {
                }
            }
        }
    }
}
