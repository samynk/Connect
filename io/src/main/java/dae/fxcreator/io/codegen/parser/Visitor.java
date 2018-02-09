package dae.fxcreator.io.codegen.parser;

import dae.fxcreator.io.codegen.parser.GraphTransformerParser.ParameterContext;
import dae.fxcreator.node.transform.CodeServlet;
import dae.fxcreator.node.transform.RuntimeUtil;
import dae.fxcreator.node.transform.TemplateClass;
import dae.fxcreator.node.transform.TemplateClassLibrary;
import dae.fxcreator.node.transform.exec.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import org.antlr.v4.runtime.tree.TerminalNode;

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
    public Object visitMapping(GraphTransformerParser.MappingContext ctx) {
        String key = ctx.key.getText();
        List<TerminalNode> packageList = ctx.ID();
        StringBuilder className = new StringBuilder();
        if (packageList.size() > 1) {
            for (int i = 1; i < packageList.size(); ++i) {
                className.append(packageList.get(i));
                className.append('.');
            }
            className.deleteCharAt(className.length() - 1);
        }
        String sClassName = className.toString();
        library.addTemplateClassNameMapping(key, sClassName);
        return className.toString();
    }

    @Override
    public Object visitCode(GraphTransformerParser.CodeContext ctx) {
        String codeId = ctx.codeID.getText();
        String subtype = null;
        if (ctx.setting() != null) {
            GraphTransformerParser.SettingContext sc = ctx.setting();
            subtype = sc.group.getText();
            if (sc.key != null) {
                subtype += "." + sc.key.getText();
            }
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
    public Object visitWriteBuffer(GraphTransformerParser.WriteBufferContext ctx) {
        String customBuffer = ctx.ID() != null ? ctx.ID().getText() : null;

        ExecuteBlock epBlock = new ExecuteBlock(customBuffer);
        addExecutable(epBlock);

        blockStack.push(epBlock);
        Object result = super.visitWriteBuffer(ctx);
        blockStack.pop();
        return result;
    }

    @Override
    public Object visitWrite(GraphTransformerParser.WriteContext ctx) {
        if (ctx.value() != null) {
            writeValue(ctx.value());
        } else if (ctx.objectChain() != null) {
            Object chain = visitObjectChain(ctx.objectChain(),false);
            WriteExpression we = new WriteExpression((ObjectChain) chain);
            addExecutable(we);
        }
        return super.visitWrite(ctx);
    }

    @Override
    public Object visitForLoop(GraphTransformerParser.ForLoopContext ctx) {
        String varName = ctx.var.getText();
        ObjectChain collection = (ObjectChain) visitObjectChain(ctx.objectChain(), false);

        ForLoop fr = new ForLoop(varName, collection);
        addExecutable(fr);
        blockStack.push(fr);
        super.visitStatements(ctx.statements());
        blockStack.pop();
        return fr;
    }

    @Override
    public Object visitIfStatement(GraphTransformerParser.IfStatementContext ctx) {
        BooleanExpression condition = visitCondition(ctx.booleanExpr());
        IfElseStatement statement = new IfElseStatement(condition);
        addExecutable(statement);
        blockStack.push(statement.getIfBlock());
        super.visitStatements(ctx.statements());
        blockStack.pop();

        if (ctx.elseStatement() != null) {
            blockStack.push(statement.getElseBlock());
            super.visitStatements(ctx.statements());
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
        } else if (ctx.objectChain() != null) {
            ObjectChain oc = (ObjectChain) visitObjectChain(ctx.objectChain(), false);
            return new BooleanObjectChain(oc);
        } else {
            return new BooleanValue(false);
        }
    }

    public Object visitObjectChain(GraphTransformerParser.ObjectChainContext ctx, boolean executable) {
        ObjectChain result = new ObjectChain();
        boolean root = true;
        for (GraphTransformerParser.ObjectContext obj : ctx.object()) {
            if (obj.parameterList() != null) {
                Expression[] es = new Expression[obj.parameterList().parameter().size()];
                int i = 0;
                for ( GraphTransformerParser.ParameterContext pc : obj.parameterList().parameter()){
                    Expression e = (Expression)visitParameter(pc);
                    es[i] = e;
                    ++i;
                }
                MethodCall mc = new MethodCall(obj.identifier.getText(),root,es);
                result.addExpression(mc);
            } else if (obj.setting() != null) {
                ObjectSetting setting = new ObjectSetting(
                        obj.identifier.getText(),
                        obj.setting().group.getText(),
                        obj.setting().key.getText(),
                        root);
                result.addExpression(setting);
            } else {
                // port or property call
                if (obj.PORT() != null) {
                    result.addExpression(new ObjectPortExpression(obj.identifier.getText(), root));
                } else {
                    result.addExpression(new ObjectExpression(obj.identifier.getText(), root));
                }
            }
            root = false;
        }
        /*
        String identifier = ctx.

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
            } else if (pc.setting() != null) {
                String varID = pc.setting().ID().getText();
                String groupID = pc.setting().property().ID(0).getText();
                String settingID = pc.setting().property().ID(1).getText();
                parameters[i] = new NodeSetting(varID, groupID, settingID);
            } else if (pc.property() != null) {
                String object = pc.property().ID(0).getText();
                String property = pc.property().ID(1).getText();
                parameters[i] = new NodeProperty(object, property);
            }
            ++i;
        }
        MethodCall mc = new MethodCall(method, parameters);
        addExecutable(mc);
         */
        if ( executable ){
            addExecutable(result);
        }
        return result;
    }

    @Override
    public Object visitObjectChain(GraphTransformerParser.ObjectChainContext ctx) {
        return this.visitObjectChain(ctx, true); 
    }
    
    

    @Override
    public Object visitParameter(ParameterContext ctx) {
        if ( ctx.value() != null){
            if (ctx.value().STRING() != null){
                String text = ctx.value().STRING().getText();
                StringValue v = new StringValue(RuntimeUtil.unescape(text, true, true));
                return v;
            }else if (ctx.value().BOOLEAN() != null){
                boolean value = Boolean.parseBoolean(ctx.value().BOOLEAN().getText());
                BooleanValue bv = new BooleanValue(value);
                return bv;
            }
        }else if(ctx.objectChain() != null ){
            return visitObjectChain(ctx.objectChain(), false);
        }
        return null;
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
}
