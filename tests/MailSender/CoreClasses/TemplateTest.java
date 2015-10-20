package MailSender.CoreClasses;

import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Willy Lellouch
 */
public class TemplateTest {
    
    public TemplateTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Begin series of tests for Template.java");
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("End series of tests");
    }
    
    @Before
    public void setUp() {
        System.out.print("Begin test ");
    }
    
    @After
    public void tearDown() {
        System.out.println("End Test");
    }

    /**
     * Test of getName method, of class Template.
     */
    @Test
    public void testGetName() {
        System.out.println("getName()");
        Template instance = new Template("test", "OBJECT TEST\n$$template_EOO\n<h1>$$var_name Apocalypse</h1><br /><img src=\"http://d24w6bsrhbeh9d.cloudfront.net/photo/4438023_700b.jpg\" />\n$$template_EOF\n$$var_name Lamastiflow");
        
        String expResult = "test";
        String result = instance.getName();
        assertEquals("Do not return correct name", expResult, result);
    }

    /**
     * Test of setName method, of class Template.
     */
    @Test
    public void testSetName() {
        System.out.println("setName()");
        String name = "test";
        Template instance = new Template("test", "OBJECT TEST\n$$template_EOO\n<h1>$$var_name Apocalypse</h1><br /><img src=\"http://d24w6bsrhbeh9d.cloudfront.net/photo/4438023_700b.jpg\" />\n$$template_EOF\n$$var_name Lamastiflow");
        
        instance.setName(name);
    }

    /**
     * Test of setContent method, of class Template.
     */
    @Test
    public void testSetContent() {
        System.out.println("setContent()");
        String content = "OBJECT TEST\n$$template_EOO\n<h1>$$var_name Apocalypse</h1><br /><img src=\"http://d24w6bsrhbeh9d.cloudfront.net/photo/4438023_700b.jpg\" />\n$$template_EOF\n$$var_name Lamastiflow";
        Template instance = new Template("test", "OBJECT TEST\n$$template_EOO\n<h1>$$var_name Apocalypse</h1><br /><img src=\"http://d24w6bsrhbeh9d.cloudfront.net/photo/4438023_700b.jpg\" />\n$$template_EOF\n$$var_name Lamastiflow");
        
        instance.setContent(content);
    }

    /**
     * Test of getContent method, of class Template.
     */
    @Test
    public void testGetContent() {
        System.out.println("getContent()");
        Template instance = new Template("test", "OBJECT TEST\n$$template_EOO\n<h1>$$var_name Apocalypse</h1><br /><img src=\"http://d24w6bsrhbeh9d.cloudfront.net/photo/4438023_700b.jpg\" />\n$$template_EOF\n$$var_name Lamastiflow");
        
        String expResult = "OBJECT TEST\n$$template_EOO\n<h1>$$var_name Apocalypse</h1><br /><img src=\"http://d24w6bsrhbeh9d.cloudfront.net/photo/4438023_700b.jpg\" />\n$$template_EOF\n$$var_name Lamastiflow";
        String result = instance.getContent();
        assertEquals(expResult, result);
    }

    /**
     * Test of getObject method, of class Template.
     */
    @Test
    public void testGetObject() {
        System.out.println("getObject()");
        Template instance = new Template("test", "OBJECT TEST\n$$template_EOO\n<h1>$$var_name Apocalypse</h1><br /><img src=\"http://d24w6bsrhbeh9d.cloudfront.net/photo/4438023_700b.jpg\" />\n$$template_EOF\n$$var_name Lamastiflow");
        String expResult = "OBJECT TEST";
        String result = instance.getObject();
        assertEquals(expResult, result);
    }

    /**
     * Test of getVarsAndValues method, of class Template.
     */
    @Test
    public void testGetVarsAndValues() {
        System.out.println("getVarsAndValues()");
        Template instance = new Template("test", "OBJECT TEST\n$$template_EOO\n<h1>$$var_name Apocalypse</h1><br /><img src=\"http://d24w6bsrhbeh9d.cloudfront.net/photo/4438023_700b.jpg\" />\n$$template_EOF\n$$var_name Lamastiflow");
        
        String[][] expResult = {{"$$var_name", "Lamastiflow"}};
        String[][] result = instance.getVarsAndValues();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getVarsAndValuesHash method, of class Template.
     */
    @Test
    public void testGetVarsAndValuesHash() {
        System.out.println("getVarsAndValuesHash()");
        Template instance = new Template("test", "OBJECT TEST\n$$template_EOO\n<h1>$$var_name Apocalypse</h1><br /><img src=\"http://d24w6bsrhbeh9d.cloudfront.net/photo/4438023_700b.jpg\" />\n$$template_EOF\n$$var_name Lamastiflow");
        
        HashMap expResult = new HashMap<>();
        expResult.put("$$var_name", "Lamastiflow");
        HashMap result = instance.getVarsAndValuesHash();
        assertEquals(expResult, result);
    }

    /**
     * Test of replaceVarsHash method, of class Template.
     */
    @Test
    public void testReplaceVarsHash() {
        System.out.println("replaceVarsHash()");
        HashMap<String, String> arg = new HashMap<>();
        arg.put("var_name", "Papageno");
        Template instance = new Template("test", "OBJECT TEST\n$$template_EOO\n<h1>$$var_name Apocalypse</h1><br /><img src=\"http://d24w6bsrhbeh9d.cloudfront.net/photo/4438023_700b.jpg\" />\n$$template_EOF\n$$var_name Lamastiflow");
        
        String expResult =  "<h1>Papageno Apocalypse</h1><br /><img src=\"http://d24w6bsrhbeh9d.cloudfront.net/photo/4438023_700b.jpg\" />\n";
        String result = instance.replaceVarsHash(arg);
        assertEquals(expResult, result);
    }

    /**
     * Test of getVarsFromMail method, of class Template.
     */
    @Test
    public void testGetVarsFromMail() {
        System.out.println("getVarsFromMail()");
        String mail = "";
        Template instance = new Template("test", "OBJECT TEST\n$$template_EOO\n<h1>$$var_name Apocalypse</h1><br /><img src=\"http://d24w6bsrhbeh9d.cloudfront.net/photo/4438023_700b.jpg\" />\n$$template_EOF\n$$var_name Lamastiflow");
        
        HashMap expResult = new HashMap<>();
        HashMap result = instance.getVarsFromMail(mail,"gal@yomail.fr");
        assertEquals(expResult, result);
    }

    /**
     * Test of getMessageFromMail method, of class Template.
     */
    @Test
    public void testGetMessageFromMail() {
        System.out.println("getMessageFromMail()");
        String mail = "";
        Template instance = new Template("test", "OBJECT TEST\n$$template_EOO\n<h1>$$var_name Apocalypse</h1><br /><img src=\"http://d24w6bsrhbeh9d.cloudfront.net/photo/4438023_700b.jpg\" />\n$$template_EOF\n$$var_name Lamastiflow");
        
        String expResult = "<h1>Lamastiflow Apocalypse</h1><br /><img src=\"http://d24w6bsrhbeh9d.cloudfront.net/photo/4438023_700b.jpg\" />\n";
        String result = instance.getMessageFromMail(mail,"gal@yomail.fr");
        assertEquals(expResult, result);
    }
}