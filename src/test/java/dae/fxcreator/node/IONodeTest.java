package dae.fxcreator.node;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class IONodeTest {
    
    public IONodeTest() {
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
    public void testRulePattern() {
        Pattern rulePattern = java.util.regex.Pattern.compile("\\b([\\w|\\d]*)(?:,([\\w|\\d]*))*\\b");
        String test = "op1,op2,op3,op4";
        Matcher m = rulePattern.matcher(test);
        assertTrue("Could not match anything",m.find());
        if (m.find()) {
            //assertEquals("Not matching 4 operands.",4,m.groupCount());
            int groupcount = m.groupCount();
            for (int i = 1; i <= groupcount; ++i) {
                System.out.println(m.group(i));
            }
            //assertEquals("op1",m.group(1));
        }
    }
}
