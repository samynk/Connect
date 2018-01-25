package dae.fxcreator.node.project;

import dae.fxcreator.util.ListHashMap;
import java.util.ArrayList;

/**
 * Maintains a list of all available project types and their
 * major and minor versions.
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class FXProjectTypeRegistry {
    ListHashMap<FXProjectType> projectTypes =
            new ListHashMap<> ();
    
    /**
     * Sets the supported project types in this application.
     * @param projectTypes the list of project types that are supported.
     */
    public void setSupportedProjectTypes(ArrayList<FXProjectType> projectTypes) {
        this.projectTypes.addAll(projectTypes);
    }
    
    /**
     * Tries to find a project type with the given name, version and minorversion.
     * @param name the name of the project type.
     * @param version the version of the project type.
     * @param minorVersion the minor version of the project type.
     * @return 
     */
    public FXProjectType findProjectType(String name, int version, int minorVersion){
        return projectTypes.find(name+"."+version+"."+minorVersion);
    }
}
