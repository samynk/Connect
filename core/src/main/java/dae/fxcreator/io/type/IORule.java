package dae.fxcreator.io.type;

import dae.fxcreator.node.IONode;
import dae.fxcreator.node.NodeInput;
import dae.fxcreator.node.IOType;

/**
 * This class calculates the type of the output(s) given the type of the inputs.
 * A rule command consists of a command and the inputs to apply the rule to.
 *
 * @author Koen
 */
public class IORule {

    /**
     * The rule command for this IORule.
     */
    private final String rule;

    public IORule(String rule) {
        this.rule = rule;
    }

    /**
     * Adapt the output types to the input types.
     *
     * @param node the node to calculate the outputs for.
     */
    public void calculateOutputTypes(IONode node) {

    }

    /**
     *
     */
    private IOType max(NodeInput... inputs) {
        if (inputs.length == 0) {
            return null;
        } else if (inputs.length == 1) {
            return inputs[0].getIOType();
        } else {
            int order = -1;
            IOType type = null;
            for (int i = 0; i < inputs.length; ++i) {
                IOType st = inputs[i].getIOType();
                if (st.getOrder() > order) {
                    type = st;
                }
            }
            return type;
        }
    }
}
