package dae.fxcreator.node.graphmath;

/**
 *
 * @author Koen
 */
public class Operation {

    private String text;

    /**
     * Creates a new operand with empty text.
     */
    public Operation() {
    }

    /**
     * Creates a new operand with initial text.
     * @param text the text for the operand.
     */
    public Operation(String text) {
        this.text = text;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    public void writeToXML(StringBuffer sb, int nrOfTabs) {
        writeTabs(sb, nrOfTabs );
        sb.append("<operation type=\"");
        sb.append(getText());
        sb.append("\"/>\n");
    }

    protected void writeTabs(StringBuffer sb, int tabs) {
        for (int i = 0; i < tabs; ++i) {
            sb.append("\t");
        }
    }
}
