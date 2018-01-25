/*
 * Digital Arts and Entertainment 2017
 */
package dae.fxcreator.loaders;

import dae.fxcreator.node.project.FXProjectType;
import dae.fxcreator.io.loaders.FXProjectTypeLoader;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author samynk
 */
public class FXProjectTypeLoaderTest {
    
    public FXProjectTypeLoaderTest() {
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
    public void testFXProjectTypeLoader() {
        FXProjectTypeLoader loader = new FXProjectTypeLoader("conf/fxcreator.json");
        loader.load();
        
        ArrayList<FXProjectType> types = loader.getProjectTypes();
        
        assertEquals(types.size(),2);
        
        FXProjectType first = types.get(0);
        assertEquals("daegame", first.getName());
        assertEquals(1, first.getVersion());
        assertEquals(0, first.getMinorVersion());
        assertEquals("nodes/daev1/nodes.xml", first.getNodesFile());
    }
}
