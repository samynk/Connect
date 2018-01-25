package dae.fxcreator.io.codegen.parser;

import dae.fxcreator.node.transform.TemplateClassLibrary;
import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.io.templates.FXProjectTemplate;
import dae.fxcreator.io.templates.FXProjectTemplates;
import dae.fxcreator.node.project.FXProjectType;
import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.io.PathUtil;
import dae.fxcreator.node.transform.ExportTask;
import dae.fxcreator.io.loaders.FXProjectTemplateLoader;
import dae.fxcreator.io.loaders.FXProjectTypeLoader;
import java.io.IOException;
import java.util.ArrayList;
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

    FXProjectTemplate basic;

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
        FXProjectTypeLoader loader = new FXProjectTypeLoader("conf/fxcreator.json");
        loader.load();
        ArrayList<FXProjectType> projectTypes = loader.getProjectTypes();
        
        FXSingleton.getSingleton().getProjectTypeRegistry().setSupportedProjectTypes(loader.getProjectTypes());

        for (FXProjectType projectType : projectTypes) {
            if ("rigging".equals(projectType.getName())) {
                String templatesFile = projectType.getTemplates();
                FXProjectTemplateLoader tLoader = new FXProjectTemplateLoader(templatesFile);
                FXProjectTemplates ft = tLoader.load();

                basic = ft.getTemplate("basic", "basic");
            }
        }
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
            Assert.assertEquals("There were syntax errors", 0, syntaxErrors);

            Assert.assertTrue(traverseResult instanceof TemplateClassLibrary);

            TemplateClassLibrary tcl = (TemplateClassLibrary) traverseResult;

            Assert.assertNotNull("Rigging project template is null", basic);
            
            
            FXProject testProject = basic.createNewProject(FXSingleton.getSingleton().getProjectTypeRegistry());
            testProject.setName("testProject1");

            ExportTask et = new ExportTask(testProject, PathUtil.createUserDirPath("test/test.rig"), tcl);
            et.export();

            StringBuilder sb = et.getCodeOutput().getBuffer("default");
            Assert.assertTrue("default buffer should have content.", sb.length() > 0);

            System.out.println("The contents of the buffer :");
            System.out.println("----------------------------");
            System.out.println(sb.toString());
            System.out.println("----------------------------");

        } catch (IOException ex) {
            Logger.getLogger(VisitorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
