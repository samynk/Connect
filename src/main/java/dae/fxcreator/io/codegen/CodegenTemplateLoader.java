package dae.fxcreator.io.codegen;

import dae.fxcreator.io.loaders.FXProjectLoader;
import dae.fxcreator.io.templates.NodeTemplateLibrary;
import dae.fxcreator.node.ShaderType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class reads the template that can generate a shader file for a given
 * shader technology (for example OpenGL, DirectX9, DirectX10, cgFX, ...)
 *
 * @author Koen
 */
public class CodegenTemplateLoader extends DefaultHandler {

    private File file;
    /**
     * The result of the loading process;
     */
    private CodeTemplateLibrary library = new CodeTemplateLibrary();
    /**
     * The library with (among other things) the type information.
     */
    private NodeTemplateLibrary nodeTemplateLibrary;
    /**
     * The current code template.
     */
    private CodeTemplate currentTemplate;
    /**
     * The current math template.
     */
    private MathTemplate mathTemplate;
    /**
     * The name for the code element.
     */
    private String codeName = null;
    /**
     * The buffername for the code element.
     */
    private String bufferName = null;
    /**
     * The subtype for the code element.
     */
    private String type = null;
    /**
     * The filename for the code element (optional).
     */
    private String codeFile = null;
    /**
     * boolean that indicates that this code element should be executed only
     * once for the object.
     */
    private boolean writeOnce;
    /**
     * The property that should be used to check for unicity in combination with
     * the writeOnce boolean.
     */
    private String writeOnceProperty;
    /**
     * The StringBuffer to help create
     */
    private StringBuffer buffer = new StringBuffer();

    /**
     * Creates a new CodegenTemplateLoader object.
     * @param nodeTemplateLib
     * @param file
     */
    public CodegenTemplateLoader(NodeTemplateLibrary nodeTemplateLib, File file) {
        this.file = file;
        this.nodeTemplateLibrary = nodeTemplateLib;
    }

    /**
     * Load all the code generation templates.
     * @return the loaded CodeTemplateLibrary object.
     */
    public CodeTemplateLibrary load() {
        if (file != null) {
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
        }
        return this.library;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentTemplate != null || mathTemplate != null) {
            buffer.append(ch, start, length);
            //String current = buffer.toString();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("template".equals(qName) || "code".equals(qName)) {
            if (currentTemplate != null && codeName != null) {
                String value = null;
                if (codeFile != null) {
                    Path path = Paths.get(codeFile);
                    File toRead = null;
                    if (path.isAbsolute()) {
                        toRead = path.toFile();
                    } else {
                        toRead = new File(file.getParentFile(), codeFile);
                    }
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new FileReader(toRead));
                        String line =reader.readLine();
                        StringBuilder sb = new StringBuilder();
                        while(line != null){
                            sb.append(line);
                            sb.append('\n');
                            line= reader.readLine();
                        }
                        value = sb.toString();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(CodegenTemplateLoader.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(CodegenTemplateLoader.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            reader.close();
                        } catch (IOException ex) {
                            Logger.getLogger(CodegenTemplateLoader.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    value = buffer.toString();
                }
                currentTemplate.addCode(codeName, type, bufferName, writeOnce, writeOnceProperty, value);
            }
        } else if ("operation".equals(qName)) {
            if (mathTemplate != null) {
                String value = buffer.toString();
                mathTemplate.setTemplate(value);
                mathTemplate = null;
            }
        }
        buffer.delete(0, buffer.length());

        if ("template".equals(qName)) {
            currentTemplate = null;
        } else if ("code".equals(qName)) {
            codeName = null;
        }

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        buffer.delete(0, buffer.length());
        if ("code".equals(qName)) {
            codeName = attributes.getValue("name");
            bufferName = attributes.getValue("buffer");
            type = attributes.getValue("type");
            codeFile = attributes.getValue("file");
            String sWriteOnce = attributes.getValue("writeOnce");
            if (sWriteOnce == null) {
                writeOnce = false;
            } else {
                writeOnce = Boolean.parseBoolean(sWriteOnce);
            }
            writeOnceProperty = attributes.getValue("writeOnceProperty");

        } else if ("template".equals(qName)) {
            String id = attributes.getValue("id");
            currentTemplate = new CodeTemplate();
            currentTemplate.setId(id);
            library.addTemplate(currentTemplate);
        } else if ("shadermodel".equals(qName)) {
            String id = attributes.getValue("id");
            String label = attributes.getValue("label");
            String base = attributes.getValue("extends");
            //System.out.println("Loading codegen template that extends : " + base);
            library.setId(id);
            library.setLabel(label);
            library.setBase(base);
        } else if ("type".equals(qName)) {
            String key = attributes.getValue("key");
            String value = attributes.getValue("value");
            if (key != null && value != null) {
                try {
                    ShaderType stype = nodeTemplateLibrary.getType(key);
                    this.library.addShaderType(stype, value);
                } catch (IllegalArgumentException ex) {
                    //System.out.println("No ShaderType for : " + key);
                }
            }
        } else if ("codeformat".equals(qName)) {
            CodeFormatter cf = new CodeFormatter();
            library.setCodeFormatter(cf);
        } else if ("sequence".equals(qName)) {
            if (library.hasCodeFormatter()) {
                String seqtype = attributes.getValue("type");
                String value = attributes.getValue("value");
                CodeFormatter cf = library.getCodeFormatter();
                if ("start".equals(seqtype)) {
                    cf.addBlockStart(value);
                } else if ("end".equals(seqtype)) {
                    cf.addBlockEnd(value);
                }
            }
        } else if ("operation".equals(qName)) {
            String name = attributes.getValue("name");
            mathTemplate = new MathTemplate(name);
            this.library.addMathTemplate(mathTemplate);
        } else if ("constant".equals(qName)) {
            String name = attributes.getValue("name");
            String value = attributes.getValue("value");
            String type = attributes.getValue("type");
            MathConstant mc = new MathConstant(name, value, type);
            this.library.addMathConstant(mc);
        }else if ("extension".equals(qName)){
            String extension = attributes.getValue("default");
            library.setDefaultExportExtension(extension);
        } else {
            System.out.println(uri);
            System.out.println(localName);
            System.out.println(qName);
        }
    }
}
