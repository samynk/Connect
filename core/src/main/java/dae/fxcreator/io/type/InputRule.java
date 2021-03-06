package dae.fxcreator.io.type;

import dae.fxcreator.node.IOType;
import java.util.ArrayList;

/**
 * This class calculates of an input is accepted or not.
 * @author Koen
 */
public class InputRule {
    /**
     * The list of accepted types.
     */
    private ArrayList<IOType> acceptedTypes;
    /**
     * Returns true if the type is accepted by the node, false otherwise.
     * @param type the type to check.
     * @return true if the type is accepted , false otherwise.
     */
    public boolean acceptType(IOType type)
    {
        return ( acceptedTypes.contains(type));
    }
}
