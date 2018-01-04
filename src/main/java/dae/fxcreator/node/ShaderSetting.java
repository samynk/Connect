package dae.fxcreator.node;

import java.util.ArrayList;

/**
 * This class defines a shader setting.
 * @author Koen
 */
public class ShaderSetting {
    private String name;
    private ArrayList<Object> possibleValues = new ArrayList<Object>();

    /**
     * Creates a new ShaderSetting object.
     * @param name the name for the new ShaderSetting object.
     */
    public ShaderSetting(String name){
        this.name = name;
    }

    /**
     * Returns the name for this shader settings.
     */
    public String getName(){
        return name;
    }

    /**
     * Add a possible value for this ShaderSetting.
     * @param value the value to add.
     */
    public void addPossibleValue(Object value){
        possibleValues.add(value);
    }
}