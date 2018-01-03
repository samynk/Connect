/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dae.fxcreator.io.type;

import dae.fxcreator.node.ShaderType;
import java.util.ArrayList;

/**
 * This class calculates of an input is accepted or not.
 * @author Koen
 */
public class InputRule {
    /**
     * The list of accepted types.
     */
    private ArrayList<ShaderType> acceptedTypes;
    /**
     * Returns true if the type is accepted by the node, false otherwise.
     * @param type the type to check.
     * @return true if the type is accepted , false otherwise.
     */
    public boolean acceptType(ShaderType type)
    {
        return ( acceptedTypes.contains(type));
    }
}
