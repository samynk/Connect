package dae.fxcreator.io;

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

    /**
     * Gets a child of the specified parent. Either the parent is this object, or
     * it is an instance of of the TreeNode class.
     * @param parent the parent object to get the child from.
     * @param index the index of the child.
     * @return the child object at the specific index.
     */
    @Override
    public Object getChild(Object parent, int index) {
        if (parent == this) {
            if (index == 0) {
                return getStructs();
            } else if (index == 1) {
                return getTechniques();
            } 
        } else if (parent instanceof TreeNode) {
            TreeNode node = (TreeNode) parent;
            return node.getChildAt(index);
        }
        return null;
    }
}
