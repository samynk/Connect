package dae.fxcreator.io.codegen;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class generates the code for a specific MathElement.
 *
 * @author Koen Samyn
 */
public class MathTemplate {

    private String operation;
    private String template;
    private static Pattern opPattern = Pattern.compile("(op\\d+)");
    private ArrayList<MathInstruction> instructions = new ArrayList<MathInstruction>();

    public MathTemplate(String operation) {
        this.operation = operation;
    }

    /**
     * Return the operator for this math template.
     *
     * @return
     */
    public String getOperation() {
        return operation;
    }

    public void setTemplate(String template) {
        this.template = template;
        Matcher m = opPattern.matcher(template);
        int startIndex = 0;
        int endIndex = 0;
        while (m.find()) {
            if (m.start() > startIndex) {
                String text = template.substring(startIndex, m.start());
                instructions.add(new MathInstruction(text));
            }
            String operand = template.substring(m.start(), m.end());
            int opIndex = Integer.parseInt(operand.substring(2));
            instructions.add(new MathInstruction(opIndex));

            endIndex = m.end();
            startIndex = endIndex;
        }
        if (endIndex < template.length()) {
            String text = template.substring(endIndex);
            instructions.add(new MathInstruction(text));
        }
    }

    public static void main(String[] args) {
        String test = "max(op1,op2)";
        Matcher m = opPattern.matcher(test);
        int startIndex = 0;
        int endIndex = 0;
        while (m.find()) {
            if (m.start() > startIndex) {
                System.out.println("Literal:" + test.substring(startIndex, m.start()));
            }
            System.out.println("Operand:" + test.substring(m.start(), m.end()));

            endIndex = m.end();
            startIndex = endIndex;
        }
        if (endIndex < test.length()) {
            System.out.println("End Literal: " + test.substring(endIndex));
        }
    }
    
    public Iterable<MathInstruction> getMathInstructions(){
        return this.instructions;
    }
}
