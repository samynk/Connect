package dae.fxcreator.io.loaders;

import dae.fxcreator.node.project.FXProjectType;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dae.fxcreator.io.util.PathUtil;
import dae.fxcreator.io.codegen.parser.GraphTransformerLexer;
import dae.fxcreator.io.codegen.parser.GraphTransformerParser;
import dae.fxcreator.node.transform.TemplateClassLibrary;
import dae.fxcreator.io.codegen.parser.Visitor;
import dae.fxcreator.node.templates.NodeTemplateLibrary;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

/**
 *
 * @author samynk
 */
public class FXProjectTypeLoader {

    private final ArrayList<FXProjectType> projectTypes = new ArrayList<>();
    private final String location;

    public FXProjectTypeLoader(String location) {
        this.location = location;
    }

    public void load() {
        InputStream is = null;
        try {
            is = PathUtil.createUserDirStream(location);
            //create JsonReader object
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(is);

            if (node.isArray()) {
                Iterator<JsonNode> children = node.elements();
                JsonNode child;
                while (children.hasNext()) {
                    child = children.next();

                    String name = child.findValue("name").asText();
                    int version = child.findValue("version").asInt();
                    int minorversion = child.findValue("minorversion").asInt();
                    String nodesFile = child.findValue("nodes").asText();
                    String templatesFile = child.findValue("templates").asText();

                    
                    Path pNodesFile = PathUtil.createUserDirPath(nodesFile);
                    NodeTemplateLoader ntl = new NodeTemplateLoader(pNodesFile);
                    NodeTemplateLibrary library = ntl.load();

                    FXProjectType type = new FXProjectType(name, version, minorversion, nodesFile, templatesFile);
                    type.setNodeTemplateLibrary(library);
                    
                    JsonNode generators = child.findValue("codegenerators");
                    if (generators != null) {
                        Iterator<JsonNode> itGenerator = generators.elements();
                        while (itGenerator.hasNext()) {
                            JsonNode generator = itGenerator.next();
                            String label = generator.findValue("label").asText();
                            String description = generator.findValue("description").asText();
                            String codegen = generator.findValue("codegen").asText();
                            TemplateClassLibrary tcl = loadExporter(codegen);
                            tcl.setLabel(label);
                            tcl.setDescription(description);
                            type.addTemplateClassLibrary(tcl);
                        }
                    }
                    projectTypes.add(type);

                }
            }
            //JsonReader jsonReader = Json.createReader(fis);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXProjectTypeLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXProjectTypeLoader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FXProjectTypeLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private TemplateClassLibrary loadExporter(String exporterFile) throws IOException {
        InputStream is = PathUtil.createUserDirStream(exporterFile);
        CharStream charStream = CharStreams.fromStream(is);
        GraphTransformerLexer lexer = new GraphTransformerLexer(charStream);
        TokenStream tokens = new CommonTokenStream(lexer);
        GraphTransformerParser parser = new GraphTransformerParser(tokens);

        Visitor classVisitor = new Visitor();
        Object traverseResult = classVisitor.visit(parser.transform());

        int syntaxErrors = parser.getNumberOfSyntaxErrors();

        if (traverseResult != null && syntaxErrors == 0) {
            TemplateClassLibrary tcl = (TemplateClassLibrary) traverseResult;
            return tcl;
        } else {
            return null;
        }
    }

    public ArrayList<FXProjectType> getProjectTypes() {
        return this.projectTypes;
    }

}
