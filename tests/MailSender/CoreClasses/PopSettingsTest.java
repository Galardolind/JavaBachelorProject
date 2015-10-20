package MailSender.CoreClasses;

import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PopSettingsTest {
    
    public PopSettingsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Begin series of tests for PopSettings.java");
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
     * Test of constructor, of class PopSettings.
     */
    @Test
    public void testConstructor() {
        PopSettings instance;
        //Test 1
        instance = new PopSettings(null, null, null, null);
        assertEquals("", instance.popProvider);
        assertEquals("", instance.popPort);
        assertEquals("", instance.user);
        assertEquals("", instance.pass);
        
        //Test 2
        instance = new PopSettings("popProvider", "popPort", "user", "pass");
        assertEquals("popProvider", instance.popProvider);
        assertEquals("popPort", instance.popPort);
        assertEquals("user", instance.user);
        assertEquals("pass", instance.pass);
    }

    /**
     * Test of getFields method, of class PopSettings.
     */
    @Test
    public void testGetFields() {
        PopSettings instance;
        HashMap<String, String> expResult;
        //Test 1
        instance = new PopSettings(null, null, null, null);
        expResult = new HashMap<>();
        expResult.put("popProvider", "");
        expResult.put("popPort", "");
        expResult.put("popUser", "");
        expResult.put("popPwd", "");
        assertEquals(expResult, instance.getFields());
        
        //Test 2
        instance = new PopSettings(null, null, null, null);
        expResult = new HashMap<>();
        expResult.put("popProvider", instance.popProvider);
        expResult.put("popPort", instance.popPort);
        expResult.put("popUser", instance.user);
        expResult.put("popPwd", instance.pass);
        assertEquals(expResult, instance.getFields());
        
        //Test 3
        instance = new PopSettings("", "", "", "");
        expResult = new HashMap<>();
        expResult.put("popProvider", instance.popProvider);
        expResult.put("popPort", instance.popPort);
        expResult.put("popUser", instance.user);
        expResult.put("popPwd", instance.pass);
        assertEquals(expResult, instance.getFields());
        
        //Test 4
        instance = new PopSettings("smtp.unice.fr", "25", "ls102425", "password");
        expResult = new HashMap<>();
        expResult.put("popProvider", instance.popProvider);
        expResult.put("popPort", instance.popPort);
        expResult.put("popUser", instance.user);
        expResult.put("popPwd", instance.pass);
        assertEquals(expResult, instance.getFields());
    }
}