package dae.fxcreator.io;

import dae.fxcreator.node.project.FXProjectType;
import dae.fxcreator.node.project.FXProject;
import java.nio.file.Path;
import javax.swing.tree.TreeNode;

/**
 * A code project has another structure than a shader project. A code project is
 * organized as follows :
 * <li>
 *  
 * </li>
 * 
 * @author Koen Samyn
 */
public class CodeProject extends FXProject{
    
    /**
     * Creates a new code project.
     * @param location the location of the project.
     * @param type the project type.
     */
    public CodeProject(Path location, FXProjectType type){
       super(location,type);
    }
    
   /**
    * Creates a new code project.
    * @param location the location of the project.
    * @param type  the project type.
    */
    public CodeProject(String location, FXProjectType type){
        super(location,type);
    }
}
