package dae.fxcreator.io.codegen.parser;

import dae.fxcreator.io.FXProject;
import dae.fxcreator.io.PathUtil;
import dae.fxcreator.io.ShaderStage;
import dae.fxcreator.io.codegen.ExportTask;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class VisitorTest {
    
    public VisitorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testProjectTemplate() {
        try {
            CharStream charStream = CharStreams.fromStream(ClassLoader.getSystemResourceAsStream("codegen/rigging/rigging.codegen"));
            GraphTransformerLexer lexer = new GraphTransformerLexer(charStream);
            TokenStream tokens = new CommonTokenStream(lexer);
            GraphTransformerParser parser = new GraphTransformerParser(tokens);
            
            Visitor classVisitor = new Visitor();
            Object traverseResult = classVisitor.visit(parser.transform());
            
            int syntaxErrors = parser.getNumberOfSyntaxErrors();
            Assert.assertEquals("There were syntax errors", 0,syntaxErrors);
            
            Assert.assertTrue( traverseResult instanceof TemplateClassLibrary);
            
            TemplateClassLibrary tcl = (TemplateClassLibrary)traverseResult;
            
            FXProject testProject = new FXProject(Paths.get("/test/test.fxpoj"));
            testProject.setName("testProject1");
            
            testProject.addShaderStage(new ShaderStage("vertexStage","vertex"));
            testProject.addShaderStage(new ShaderStage("pixelStage","pixel"));
            
            ExportTask et = new ExportTask(testProject,PathUtil.createUserDirPath("/test/test.rig"),tcl);
            et.export();
            
            StringBuilder sb = et.getCodeOutput().getBuffer("default");
            Assert.assertTrue("default buffer should have content.",sb.length() > 0 );
            
            System.out.println("The contents of the buffer :");
            System.out.println("----------------------------");
            System.out.println(sb.toString());
            System.out.println("----------------------------");
            
        } catch (IOException ex) {
            Logger.getLogger(VisitorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
