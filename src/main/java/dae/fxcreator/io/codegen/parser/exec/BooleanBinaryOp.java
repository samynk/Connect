package dae.fxcreator.io.codegen.parser.exec;

import dae.fxcreator.io.codegen.ExportTask;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class BooleanBinaryOp extends BooleanExpression {

    private final BooleanExpression first;
    private final BooleanExpression second;
    private final OPERAND op;

    public enum OPERAND {
        AND, OR, XOR
    };

    public BooleanBinaryOp(BooleanExpression first, BooleanExpression second, String operand) {
        this.first = first;
        this.second = second;
        switch(operand){
            case "&&": op = OPERAND.AND;break;
            case "||": op = OPERAND.OR;break;
            case "^": op = OPERAND.XOR;break;
            default: op = OPERAND.OR;
        }
        
    }

    @Override
    public boolean evaluate(ExportTask context) {
        boolean result;
        switch (op) {
            case AND:
                result =  first.evaluate(context) && second.evaluate(context);
            case OR:
                result = first.evaluate(context) || second.evaluate(context);
            case XOR:
                result = first.evaluate(context) ^ second.evaluate(context);
            default:
                result = false;
        }
        return not? !result:result;
    }
}
