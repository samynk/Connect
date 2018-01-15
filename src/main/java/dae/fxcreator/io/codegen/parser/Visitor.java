package dae.fxcreator.io.codegen.parser;

import dae.fxcreator.io.codegen.parser.exec.Executable;
import dae.fxcreator.io.codegen.parser.exec.ExecuteBlock;
import dae.fxcreator.io.codegen.parser.exec.WriteValue;
import java.util.ArrayDeque;
import java.util.Deque;

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
        System.out.println("Code id : " + codeId);
        String subtype = null;
        if (ctx.property() != null) {
            subtype = ctx.property().getText();
            System.out.println("Subtype is : " + subtype);
        }
        String buffer = null;
        if (ctx.writeToBuffer() != null) {
            buffer = ctx.writeToBuffer().ID().getText();
            System.out.println("Buffer is : " + buffer);
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
        System.out.println("custom buffer :" + customBuffer);
        
        ExecuteBlock epBlock = new ExecuteBlock(customBuffer);
        addExecutable(epBlock);
        
        blockStack.push(epBlock);
        Object result = super.visitExpression(ctx);
        blockStack.pop();
        return result;
    }

    @Override
    public Object visitWrite(GraphTransformerParser.WriteContext ctx) {
        if ( ctx.value() != null ){
            writeValue(ctx.value());
        }
        
        return super.visitWrite(ctx);
    }
    
    private void writeValue(GraphTransformerParser.ValueContext ctx){
        if ( ctx.STRING() != null){
            String toWrite = ctx.STRING().getText();
            WriteValue value = new WriteValue(toWrite.substring(1,toWrite.length()-1));
            addExecutable(value);
        }
    }
    
    private void addExecutable(Executable e){
        ExecuteBlock eb = blockStack.peek();
        eb.addExecutable(e);
    }
}
