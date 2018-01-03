package dae.fxcreator.io.codegen;

/**
 *
 * @author Koen Samyn
 */
public class MathConstant {

    private String name;
    private String value;
    private String type;

    public MathConstant(String name, String value, String type) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
    
    public String getName(){
        return name;
    }
    
    public String getValue(){
        return value;
    }
    
    public String getType(){
        return type;
    }
}
