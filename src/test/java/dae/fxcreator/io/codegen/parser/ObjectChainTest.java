package dae.fxcreator.io.codegen.parser;

import dae.fxcreator.io.FXSingleton;
import dae.fxcreator.io.loaders.FXProjectTemplateLoader;
import dae.fxcreator.io.loaders.FXProjectTypeLoader;
import dae.fxcreator.io.templates.FXProjectTemplate;
import dae.fxcreator.io.templates.FXProjectTemplates;
import dae.fxcreator.io.util.PathUtil;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.project.FXProject;
import dae.fxcreator.node.project.FXProjectType;
import dae.fxcreator.node.project.ShaderStage;
import dae.fxcreator.node.transform.ExportTask;
import dae.fxcreator.node.transform.TemplateClassLibrary;
import dae.fxcreator.node.transform.exec.Expression;
import dae.fxcreator.node.transform.exec.MethodCall;
import dae.fxcreator.node.transform.exec.ObjectChain;
import dae.fxcreator.node.transform.exec.ObjectPortExpression;
import dae.fxcreator.node.transform.exec.StringValue;
import dae.fxcreator.node.transform.exec.VarID;
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
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ObjectChainTest {

    FXProjectTemplate basic;
    private FXProject basicProject;
    private IONode mathNode;
    private TemplateClassLibrary tcl;

    public ObjectChainTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

        FXProjectTypeLoader loader = new FXProjectTypeLoader("fxcreator.json");
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
        assertNotNull(basic);
        basicProject = basic.createNewProject(FXSingleton.getSingleton().getProjectTypeRegistry());
        basicProject.setName("testProject1");

        ShaderStage stage = basicProject.findShaderStage("ArmController");
        assertNotNull(stage);

        mathNode = stage.findNode("math1");
        assertNotNull(mathNode);

        GraphTransformerParser parser;
        try {
            parser = loadParser("transformers/rigging/rigging.codegen");
            Visitor classVisitor = new Visitor();
            Object traverseResult = classVisitor.visit(parser.transform());

            Assert.assertTrue(traverseResult instanceof TemplateClassLibrary);

            tcl = (TemplateClassLibrary) traverseResult;
        } catch (IOException ex) {
            Logger.getLogger(ObjectChainTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private GraphTransformerParser loadParser(String path) throws IOException {
        CharStream charStream = CharStreams.fromStream(PathUtil.createUserDirStream(path));
        GraphTransformerLexer lexer = new GraphTransformerLexer(charStream);
        TokenStream tokens = new CommonTokenStream(lexer);
        GraphTransformerParser parser = new GraphTransformerParser(tokens);

        int syntaxErrors = parser.getNumberOfSyntaxErrors();
        Assert.assertEquals("There were syntax errors", 0, syntaxErrors);

        return parser;
    }

    @After
    public void tearDown() {
    }

    // test if the single object port works.
    @Test
    public void testSingleObjectPort() {

        ExportTask et = new ExportTask(basicProject, PathUtil.createUserHomePath("test/test.rig"), null);
        et.setVar("node", mathNode);

        ObjectChain oc = new ObjectChain();

        ObjectPortExpression ope = new ObjectPortExpression("i1", true);
        oc.addExpression(ope);

        Object result = oc.evaluate(et);
        assertNotNull(result);
        assertTrue(result instanceof ShaderInput);
        ShaderInput si = (ShaderInput) result;
        assertEquals("i1", si.getId());
    }

    @Test
    public void testSingleMethodCall() {
        ExportTask et = new ExportTask(basicProject, PathUtil.createUserHomePath("test/test.rig"), tcl);
        et.setVar("node", mathNode);

        ObjectChain oc1 = new ObjectChain();
        Expression object = new VarID("node");
        Expression s = new StringValue("definition");
        MethodCall mc1 = new MethodCall("call", true, object, s);

        oc1.addExpression(mc1);
        oc1.evaluate(et);

        ObjectChain oc2 = new ObjectChain();
        
        Expression s1 = new StringValue("default");
        Expression s2 = new StringValue("default");
        MethodCall mc2 = new MethodCall("writeBufferToStream", true, s1, s2);

        oc2.addExpression(mc2);
        oc2.evaluate(et);

    }
}
