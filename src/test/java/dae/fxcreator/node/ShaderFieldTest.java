/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.node;

import dae.fxcreator.node.project.FXProjectType;
import dae.fxcreator.io.util.PathUtil;
import dae.fxcreator.io.loaders.FXProjectTypeLoader;
import dae.fxcreator.node.templates.NodeTemplateLibrary;
import dae.fxcreator.io.loaders.NodeTemplateLoader;
import dae.fxcreator.io.type.ShaderTypeLibrary;
import java.util.ArrayList;
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
public class ShaderFieldTest {

    public ShaderFieldTest() {
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
    public void testDecode() {
        FXProjectTypeLoader loader = new FXProjectTypeLoader("conf/fxcreator.json");
        loader.load();

        ArrayList<FXProjectType> types = loader.getProjectTypes();
        assertTrue("Could not load project types", types.size() > 0);

        FXProjectType type1 = types.get(0);

        String nodes = type1.getNodesFile();
        NodeTemplateLoader ntLoader = new NodeTemplateLoader(PathUtil.createUserDirPath(nodes));
        NodeTemplateLibrary library = ntLoader.load();

        assertNotNull("Could not get shader library.", library);
        ShaderStruct struct = new ShaderStruct("test", library.getTypeLibrary());

        ShaderField field = new ShaderField("test", "WORLD", new ShaderType("FLOAT4", 8));
        struct.addShaderField(field);

        field.decode("FLOAT3 test2:WORLD");

        assertEquals(field.getName(), "test2");
        assertEquals(field.getSemantic().getValue(), "WORLD");
        assertEquals(field.getType().getType(), "FLOAT3");

        field.decode("FLOAT2 test3");

        assertEquals(field.getName(), "test3");
        assertEquals(field.getType().getType(), "FLOAT2");
        assertEquals(field.getSemantic().isValid(), false);

        field.decode("FLOAT3 newfield:WORLD");

        assertEquals(field.getName(), "newfield");
        assertEquals(field.getSemantic().getValue(), "WORLD");
        assertEquals(field.getType().getType(), "FLOAT3");
    }
}
