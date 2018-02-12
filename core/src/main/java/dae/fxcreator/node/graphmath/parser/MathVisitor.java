package dae.fxcreator.node.graphmath.parser;

import dae.fxcreator.io.math.parser.MathScriptBaseVisitor;
import dae.fxcreator.io.math.parser.MathScriptParser;
import dae.fxcreator.node.graphmath.BinaryMathElement;
import dae.fxcreator.node.graphmath.MathBooleanValue;
import dae.fxcreator.node.graphmath.MathElement;
import dae.fxcreator.node.graphmath.MathFloatArrayValue;
import dae.fxcreator.node.graphmath.MathFloatValue;
import dae.fxcreator.node.graphmath.MathFormula;
import dae.fxcreator.node.graphmath.MathIntValue;
import dae.fxcreator.node.graphmath.MathVariable;
import dae.fxcreator.node.graphmath.Operation;
import dae.fxcreator.node.graphmath.UnaryMathElement;
import java.util.List;
import java.util.Stack;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class MathVisitor extends MathScriptBaseVisitor {

    private final Stack<MathElement> elementStack = new Stack<>();

    @Override
    public Object visitScript(MathScriptParser.ScriptContext ctx) {
        MathFormula mf = new MathFormula();
        elementStack.push(mf);
        super.visitScript(ctx);
        return mf;
    }

    @Override
    public Object visitAssignment(MathScriptParser.AssignmentContext ctx) {

        String leftId = ctx.ID().getText();
        BinaryMathElement bme = new BinaryMathElement();
        bme.setOperation(Operation.ASSIGN);
        MathVariable left = new MathVariable();
        left.setVarName(leftId);
        bme.setFirst(left);

        addElementToTopOfStack(bme);
        elementStack.push(bme);
        super.visitAssignment(ctx);
        elementStack.pop();
        return bme;
    }

    @Override
    public Object visitFormula(MathScriptParser.FormulaContext ctx) {
        MathElement created = null;
        if (ctx.op != null) {
            BinaryMathElement bme = new BinaryMathElement();
            addElementToTopOfStack(bme);
            elementStack.add(bme);
            bme.setOperation(parseOperation(ctx.op));
            created = bme;
        }
        super.visitFormula(ctx);
        if (created != null) {
            elementStack.pop();
        }
        return created;
    }

    private Operation parseOperation(Token op) {
        switch (op.getText()) {
            case "+":
                return Operation.PLUS;
            case "-":
                return Operation.MINUS;
            case "*":
                return Operation.MULITPLY;
            case "/":
                return Operation.DIVIDE;
            case "%":
                return Operation.MODULO;
        }
        return Operation.NOP;
    }

    @Override
    public Object visitValue(MathScriptParser.ValueContext ctx) {
        MathElement parent = elementStack.peek();
        // we are at the end of the tree.
        if (ctx.primitive() != null) {
            parseAndAdd(ctx.primitive(), parent);
        } else if (ctx.fvec() != null) {
            parseAndAdd(ctx.fvec(), parent);
        } else if (ctx.ID() != null) {
            parent.addMathElement(new MathVariable(ctx.ID().getText()));
        }
        return null;
    }

    private void addElementToTopOfStack(MathElement element) {
        elementStack.peek().addMathElement(element);
    }

    private void parseAndAdd(MathScriptParser.PrimitiveContext ctx, MathElement parent) {
        if (ctx.BOOLEAN() != null) {
            boolean value = Boolean.parseBoolean(ctx.BOOLEAN().getText());
            parent.addMathElement(new MathBooleanValue(value));
        } else if (ctx.FLOAT() != null) {
            float value = Float.parseFloat(ctx.FLOAT().getText());
            parent.addMathElement(new MathFloatValue(value));
        } else if (ctx.INT() != null) {
            int value = Integer.parseInt(ctx.INT().getText());
            parent.addMathElement(new MathIntValue(value));
        }
    }

    private void parseAndAdd(MathScriptParser.FvecContext fvec, MathElement parent) {
        if (fvec.fvec2() != null) {
            parent.addMathElement(parseFloatArray(fvec.fvec2().FLOAT()));
        } else if (fvec.fvec3() != null) {
            parent.addMathElement(parseFloatArray(fvec.fvec3().FLOAT()));
        } else if (fvec.fvec4() != null) {
            parent.addMathElement(parseFloatArray(fvec.fvec4().FLOAT()));
        }
    }

    private MathFloatArrayValue parseFloatArray(List<TerminalNode> floats) {
        float[] values = new float[floats.size()];
        for (int i = 0; i < floats.size(); ++i) {
            float v = Float.parseFloat(floats.get(i).getText());
            values[i] = v;
        }
        return new MathFloatArrayValue(values);
    }
}
