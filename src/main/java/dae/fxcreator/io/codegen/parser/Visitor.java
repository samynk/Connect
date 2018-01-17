package dae.fxcreator.io.codegen.parser;

import dae.fxcreator.io.codegen.parser.GraphTransformerParser.ParameterContext;
import dae.fxcreator.io.codegen.parser.exec.BooleanBinaryOp;
import dae.fxcreator.io.codegen.parser.exec.BooleanExpression;
import dae.fxcreator.io.codegen.parser.exec.BooleanProperty;
import dae.fxcreator.io.codegen.parser.exec.BooleanValue;
import dae.fxcreator.io.codegen.parser.exec.Executable;
import dae.fxcreator.io.codegen.parser.exec.ExecuteBlock;
import dae.fxcreator.io.codegen.parser.exec.ForLoop;
import dae.fxcreator.io.codegen.parser.exec.IfElseStatement;
import dae.fxcreator.io.codegen.parser.exec.MethodCall;
import dae.fxcreator.io.codegen.parser.exec.VarID;
import dae.fxcreator.io.codegen.parser.exec.WritePortProperty;
import dae.fxcreator.io.codegen.parser.exec.WriteProperty;
import dae.fxcreator.io.codegen.parser.exec.WriteSetting;
import dae.fxcreator.io.codegen.parser.exec.WriteValue;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class Visitor extends GraphTransformerBaseVisitor<Object> {

    TemplateClassLibrary library = new TemplateClassLibrary();
    private TemplateClass current;

    private final Deque<ExecuteBlock> blockStack = new ArrayDeque<>();

    @Override
    public Object visitTemplate(GraphTransformerParser.TemplateContext ctx) {
        String templateId = ctx.ID().getText();
        current = new TemplateClass();
        current.setId(templateId);
        library.addTemplate(current);
        super.visitTemplate(ctx);
        return library;
    }

    @Override
    public Object visitCode(GraphTransformerParser.CodeContext ctx) {
        String codeId = ctx.ID().getText();
        String subtype = null;
        if (ctx.property() != null) {
            subtype = ctx.property().getText();
        }
        String buffer = null;
        if (ctx.writeToBuffer() != null) {
            buffer = ctx.writeToBuffer().ID().getText();
        }

        CodeServlet servlet = current.addCode(codeId, subtype, buffer, false, null);
        blockStack.push(servlet);
        Object result = super.visitCode(ctx);
        blockStack.pop();
        return result;
    }

    @Override
    public Object visitExpression(GraphTransformerParser.ExpressionContext ctx) {
        String customBuffer = ctx.ID() != null ? ctx.ID().getText() : null;

        ExecuteBlock epBlock = new ExecuteBlock(customBuffer);
        addExecutable(epBlock);

        blockStack.push(epBlock);
        Object result = super.visitExpression(ctx);
        blockStack.pop();
        return result;
    }

    @Override
    public Object visitWrite(GraphTransformerParser.WriteContext ctx) {
        if (ctx.value() != null) {
            writeValue(ctx.value());
        } else if (ctx.property() != null) {
            writeProperty(ctx.property());
        } else if (ctx.portID() != null) {
            writePortProperty(ctx.portID());
        } else if ( ctx.setting() != null){
            writeSetting(ctx.setting());
        }

        return super.visitWrite(ctx);
    }

    @Override
    public Object visitForLoop(GraphTransformerParser.ForLoopContext ctx) {
        String varName = ctx.ID().getText();
        String object = ctx.property().ID(0).getText();
        String collection = ctx.property().ID(1).getText();

        ForLoop fr = new ForLoop(varName, object, collection);
        addExecutable(fr);
        blockStack.push(fr);
        Object result = super.visitForLoop(ctx);
        blockStack.pop();
        return result;
    }

    @Override
    public Object visitIfStatement(GraphTransformerParser.IfStatementContext ctx) {
        BooleanExpression condition = visitCondition(ctx.booleanExpr());
        IfElseStatement statement = new IfElseStatement(condition);
        addExecutable(statement);
        blockStack.push(statement.getIfBlock());
        // there is always an 
        for (GraphTransformerParser.ExpressionContext ec : ctx.expression()) {
            visitExpression(ec);
        }
        blockStack.pop();

        if (ctx.elseStatement() != null) {
            blockStack.push(statement.getElseBlock());
            for (GraphTransformerParser.ExpressionContext ec : ctx.elseStatement().expression()) {
                visitExpression(ec);
            }
            blockStack.pop();
        }
        return statement;
    }

    public BooleanExpression visitCondition(GraphTransformerParser.BooleanExprContext ctx) {
        if (ctx.booleanBinaryOp() != null) {
            BooleanExpression first = visitBooleanValue(ctx.booleanValue(0));
            BooleanExpression second = visitBooleanValue(ctx.booleanValue(1));
            String operation = ctx.booleanBinaryOp().getText();
            return new BooleanBinaryOp(first, second, operation);
        } else if (ctx.booleanValue(0) != null) {
            return visitBooleanValue(ctx.booleanValue(0));
        } else if (ctx.NOT() != null) {
            BooleanExpression condition = visitCondition(ctx.booleanExpr());
            condition.not();
            return condition;
        } else {
            return null;
        }
    }

    @Override
    public BooleanExpression visitBooleanValue(GraphTransformerParser.BooleanValueContext ctx) {
        if (ctx.BOOLEAN() != null) {
            return new BooleanValue(Boolean.parseBoolean(ctx.BOOLEAN().getText()));
        } else if (ctx.property() != null) {
            String object = ctx.property().ID(0).getText();
            String property = ctx.property().ID(1).getText();
            return new BooleanProperty(object, property);
        } else {
            return new BooleanValue(false);
        }
    }

    @Override
    public Object visitMethodCall(GraphTransformerParser.MethodCallContext ctx) {
        String object = ctx.property().ID(0).getText();
        String method = ctx.property().ID(1).getText();

        Object[] parameters = new Object[ctx.parameter().size()];
        int i = 0;
        for (ParameterContext pc : ctx.parameter()) {
            if (pc.ID() != null) {
                parameters[i] = new VarID(pc.ID().getText());
            } else if (pc.value() != null) {
                if (pc.value().STRING() != null) {
                    parameters[i] = RuntimeUtil.unescape(pc.value().STRING().getText(), true, true);
                } else if (pc.value().BOOLEAN() != null) {
                    parameters[i] = Boolean.parseBoolean(pc.value().BOOLEAN().getText());
                }
            }
            ++i;
        }
        MethodCall mc = new MethodCall(object, method, parameters);
        addExecutable(mc);
        return super.visitMethodCall(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    private void writeValue(GraphTransformerParser.ValueContext ctx) {
        if (ctx.STRING() != null) {

            String toWrite = ctx.STRING().getText();
            String unescaped = RuntimeUtil.unescape(toWrite, true, true);
            WriteValue value = new WriteValue(unescaped);
            addExecutable(value);
        }
    }

    private void addExecutable(Executable e) {
        ExecuteBlock eb = blockStack.peek();
        eb.addExecutable(e);
    }

    private void writeProperty(GraphTransformerParser.PropertyContext property) {
        String o = property.ID(0).getText();
        String p = property.ID(1).getText();
        addExecutable(new WriteProperty(o, p));

    }

    private void writePortProperty(GraphTransformerParser.PortIDContext property) {
        String o = property.ID(0).getText();
        String p = null;
        if (property.DOT() != null) {
            p = property.ID(1).getText();
        }
        addExecutable(new WritePortProperty(o, p));
    }

    private void writeSetting(GraphTransformerParser.SettingContext setting) {
        String o = setting.ID().getText();
        String group = setting.property().ID(0).getText();
        String prop = setting.property().ID(1).getText();
        addExecutable(new WriteSetting(o,group,prop));
    }
}
