package dae.fxcreator.node.graphmath;

/**
 *
 * @author Koen
 */
public enum Operation {
    PLUS("+"),MINUS("-"),MULITPLY("*"),CROSS("\u00C4"),DIVIDE("/"),MODULO("%"), EQUALS("="), FUNCTION(""), POWER("^"), GROUP(""), SQUAREROOT(""), NOP(""), ASSIGN("=");
    private final String text;

     /**
     * Creates a new operand with initial text.
     * @param text the text for the operand.
     */
    Operation(String text) {
        this.text = text;
    }
    
    public static Operation parseOperation(String op){
        for (Operation o : Operation.values()){
            if ( o.text.equals(op)){
                return o;
            }
        }
        return Operation.NOP;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
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
    
    @Override
    public String toString(){
        return text;
    }
}
