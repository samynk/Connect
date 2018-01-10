package dae.fxcreator.io.loaders;

import dae.fxcreator.io.FXProjectType;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dae.fxcreator.io.PathUtil;
import java.util.ArrayList;
import java.util.Iterator;

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

                    FXProjectType type = new FXProjectType(name, version, minorversion, nodesFile, templatesFile);
                    JsonNode generators = child.findValue("codegenerators");
                    Iterator<JsonNode> itGenerator = generators.elements();
                    while (itGenerator.hasNext()) {
                        JsonNode generator = itGenerator.next();
                        System.out.println(generator.asText());
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

    public ArrayList<FXProjectType> getProjectTypes() {
        return this.projectTypes;
    }

}
