package dae.fxcreator.io.codegen;

/**
 *
 * @author samyn_000
 */
public class MathInstruction {

    enum InstructionType {

        LITERAL, OPERAND
    };
    private InstructionType it;
    private int opIndex;
    private String text;

    public MathInstruction(String text) {
        it = InstructionType.LITERAL;
        this.text = text;
    }

    public MathInstruction(int opIndex) {
        it = InstructionType.OPERAND;
        this.opIndex = opIndex;
    }
    
    public boolean isLiteral(){
        return it == InstructionType.LITERAL;
    }
    
    public boolean isOperand(){
        return it == InstructionType.OPERAND;
    }
    
    public int getOpIndex(){
        return opIndex;
    }
    
    public String getText(){
        return text;
    }
}
