package dae.fxcreator.io.codegen;

import dae.fxcreator.node.graph.math.MathFormula;
import java.util.HashMap;

/**
 *
 * @author Koen Samyn
 */
public class MathFormulaCodeGenerator {
    private final HashMap<String, MathTemplate> templates =
            new HashMap<>();    
    
    private final HashMap<String, MathConstant> constants =
            new HashMap<>();
    /**
     * Generates the code from a math formula.
     * @param root the root of the math formula.
     * @return the code from the specific mathformula.
     */
    public String createFormula(MathFormula root){
        StringBuilder result = new StringBuilder();
        //root.build(result, this);
        return result.toString();
    }
    
    public MathTemplate getMathTemplate(String operation){
        return templates.get(operation);
    }
    
    public MathConstant getMathConstant(String constant){
        return constants.get(constant);
    }
    
    public void addMathTemplate(MathTemplate mt){
        templates.put(mt.getOperation(), mt);
    }

    public void addMathConstant(MathConstant mc) {
       constants.put(mc.getName(), mc);
    }
}
