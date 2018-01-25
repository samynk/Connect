package dae.fxcreator.node.transform;

import java.util.HashMap;

/**
 * Stores the data about the subtypes of a template.
 * If there is no subtype defined, the template will be stored
 * with "default" as key.
 * @author Koen
 */
public class TemplateClassSubtypes {

    /**
     * The name for the code element.
     */
    private final String codeId;
    private final HashMap<String, CodeServlet> codeServlets = new HashMap<>();
    private CodeServlet defaultCS;

    public TemplateClassSubtypes(String codeId) {
        this.codeId = codeId;
    }

    /**
     * Add a code generator definition to this TemplateSubtypeMap.
     * @param cs the CodeServlet that will generate code.
     */
    public void addCodeGeneratorDefinition(CodeServlet cs) {
        if (cs.isDefault()) {
            defaultCS = cs;
        } else {
            codeServlets.put(cs.getType(), cs);
        }
    }

    /**
     * Returns the default CodeGeneratorDefinition object.
     * @return the default code generator definition object.
     */
    public CodeServlet getDefault() {
        return defaultCS;
    }

    /**
     * Returns the CodeGeneratorDefinition object that has the given type as
     * property.
     * @param type the type of the CodeServlet.
     * @return a matching code servlet or null if none is found.
     */
    public CodeServlet getCodeGeneratorDefinition(String type) {
        return codeServlets.get(type);
    }
}
