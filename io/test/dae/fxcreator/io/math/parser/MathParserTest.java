package dae.fxcreator.io.math.parser;

import dae.fxcreator.node.graphmath.parser.MathVisitor;
import dae.fxcreator.node.graphmath.BinaryMathElement;
import dae.fxcreator.node.graphmath.MathBooleanValue;
import dae.fxcreator.node.graphmath.MathElement;
import dae.fxcreator.node.graphmath.MathFloatArrayValue;
import dae.fxcreator.node.graphmath.MathFloatValue;
import dae.fxcreator.node.graphmath.MathFormula;
import dae.fxcreator.node.graphmath.MathIntValue;
import dae.fxcreator.node.graphmath.MathVariable;
import dae.fxcreator.node.graphmath.Operation;
import java.io.IOException;
import java.io.InputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class MathParserTest {

    public MathParserTest() {
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

    private MathFormula load(String path) {
        try {
            InputStream is = ClassLoader.getSystemResourceAsStream(path);
            assertNotNull("Could not load : " + path, is);
            CharStream charStream = CharStreams.fromStream(is);
            MathScriptLexer lexer = new MathScriptLexer(charStream);
            TokenStream tokens = new CommonTokenStream(lexer);
            MathScriptParser parser = new MathScriptParser(tokens);

            MathVisitor classVisitor = new MathVisitor();
            MathFormula result = (MathFormula) classVisitor.visit(parser.script());
            return result;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Test
    public void testFloatAssignment() {
        MathFormula mf = load("assignment/floatassignment.math");
        BinaryMathElement bme = testAssignment(mf, 0, "o");

        assertTrue(bme.getSecond() instanceof MathFloatValue);
        MathFloatValue mfv = (MathFloatValue) bme.getSecond();
        assertEquals(7.0, mfv.getValue(), 0.001f);
    }

    @Test
    public void testFloat2Assignment() {
        MathFormula mf = load("assignment/float2assignment.math");
        BinaryMathElement bme = testAssignment(mf, 0, "o");

        assertTrue(bme.getSecond() instanceof MathFloatArrayValue);
        MathFloatArrayValue mfv = (MathFloatArrayValue) bme.getSecond();

        float[] values = mfv.getValues();
        assertEquals(2, values.length);
        assertEquals(7.0, values[0], 0.001f);
        assertEquals(3.0, values[1], 0.001f);
    }

    @Test
    public void testFloat3Assignment() {
        MathFormula mf = load("assignment/float3assignment.math");
        BinaryMathElement bme = testAssignment(mf, 0, "o");

        assertTrue(bme.getSecond() instanceof MathFloatArrayValue);
        MathFloatArrayValue mfv = (MathFloatArrayValue) bme.getSecond();

        float[] values = mfv.getValues();
        assertEquals(3, values.length);
        assertArrayEquals(new float[]{7, 3, 1}, values, 0.001f);
    }

    @Test
    public void testFloat4Assignment() {
        MathFormula mf = load("assignment/float4assignment.math");
        BinaryMathElement bme = testAssignment(mf, 0, "o");

        assertTrue(bme.getSecond() instanceof MathFloatArrayValue);
        MathFloatArrayValue mfv = (MathFloatArrayValue) bme.getSecond();

        float[] values = mfv.getValues();
        assertEquals(4, values.length);
        assertArrayEquals(new float[]{7, 3, 1, 0.2f}, values, 0.001f);
    }

    @Test
    public void testIntAssignment() {
        MathFormula mf = load("assignment/intassignment.math");
        BinaryMathElement bme = testAssignment(mf, 0, "o");

        assertTrue(bme.getSecond() instanceof MathIntValue);
        MathIntValue miv = (MathIntValue) bme.getSecond();
        assertEquals(4, miv.getValue());
    }

    @Test
    public void testBooleanAssignment() {
        MathFormula mf = load("assignment/booleanassignment.math");
        BinaryMathElement bme = testAssignment(mf, 0, "o");

        assertTrue(bme.getSecond() instanceof MathBooleanValue);
        MathBooleanValue mbv = (MathBooleanValue) bme.getSecond();
        assertEquals(false, mbv.getValue());
    }

    private BinaryMathElement testAssignment(MathFormula mf, int fIndex, String varName) {
        assertNotNull(mf);
        int nrOfRoots = mf.getNrOfRoots();
        assertEquals(1, nrOfRoots);
        MathElement firstRoot = mf.getRoot(fIndex);
        assertTrue(firstRoot instanceof BinaryMathElement);
        BinaryMathElement bme = (BinaryMathElement) firstRoot;
        assertTrue(bme.getFirst() instanceof MathVariable);
        MathVariable mv = (MathVariable) bme.getFirst();
        assertEquals(mv.getVarName(), varName);
        return bme;
    }

    @Test
    public void testFloatOperators() {
        MathFormula mf = load("operators/floatoperators.math");
        BinaryMathElement bme = testAssignment(mf, 0, "o1");

        assertTrue(bme.getSecond() instanceof BinaryMathElement);
        BinaryMathElement bme2 = (BinaryMathElement) bme.getSecond();
        assertEquals(Operation.PLUS, bme2.getOperation());

        testFloat(bme2.getFirst(), 7);
        testFloat(bme2.getSecond(), 3);

    }

    public void testFloat(MathElement elem, float value) {
        assertTrue(elem instanceof MathFloatValue);
        MathFloatValue mfv = (MathFloatValue) elem;
        assertEquals(value, mfv.getValue(), 0.0001f);
    }

}
