/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MailSender.CoreClasses;

import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Florian
 */
public class SimpleSettingsTest {
    
    public SimpleSettingsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Begin series of tests for SimpleSettings.java");
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
     * Test of constructor, of class SimpleSettings.
     */
    @Test
    public void testConstructor() {
        SimpleSettings instance;
        HashMap<String, String> expResult;
        //Test 1
        instance = new SimpleSettings(null, null, null, null, 0, true, null, null);
        assertEquals(instance.smtpHost, "");
        assertEquals(instance.smtpPort, "");
        assertEquals(instance.user, "");
        assertEquals(instance.pass, "");
        assertEquals(instance.delay, 0);
        assertEquals(instance.auth, true);
        assertEquals(instance.from, "");
        assertEquals(instance.host, "");
        
        //Test 2
        instance = new SimpleSettings("smtpHost", "smtpPort", "user", "pass", 5, true, "from", "host");
        assertEquals(instance.smtpHost, "smtpHost");
        assertEquals(instance.smtpPort, "smtpPort");
        assertEquals(instance.user, "user");
        assertEquals(instance.pass, "pass");
        assertEquals(instance.delay, 5);
        assertEquals(instance.auth, true);
        assertEquals(instance.from, "from");
        assertEquals(instance.host, "host");
    }

    /**
     * Test of getFields method, of class SimpleSettings.
     */
    @Test
    public void testGetFields() {
        SimpleSettings instance;
        HashMap<String, String> expResult;
        //Test 1
        instance = new SimpleSettings(null, null, null, null, 0, true, null, null);
        expResult = new HashMap<>();
        expResult.put("smtpHost", "");
        expResult.put("smtpPort", "");
        expResult.put("path", null);
        expResult.put("user", "");
        expResult.put("pass", "");
        expResult.put("delay", "0");
        expResult.put("auth", "true");
        expResult.put("from", "");
        expResult.put("host", "");
        assertEquals(expResult, instance.getFields());
        
        //Test 2
        instance = new SimpleSettings(null, null, null, null, 0, true, null, null);
        expResult = new HashMap<>();
        expResult.put("smtpHost", instance.smtpHost);
        expResult.put("smtpPort", instance.smtpPort);
        expResult.put("path", instance.path);
        expResult.put("user", instance.user);
        expResult.put("pass", instance.pass);
        expResult.put("delay", Integer.toString(instance.delay));
        expResult.put("auth", Boolean.toString(instance.auth));
        expResult.put("from", instance.from);
        expResult.put("host", instance.host);
        assertEquals(expResult, instance.getFields());
        
        //Test 3
        instance = new SimpleSettings("", "", "", "", 0, true, "", "");
        expResult = new HashMap<>();
        expResult.put("smtpHost", instance.smtpHost);
        expResult.put("smtpPort", instance.smtpPort);
        expResult.put("user", instance.user);
        expResult.put("path", instance.path);
        expResult.put("pass", instance.pass);
        expResult.put("delay", Integer.toString(instance.delay));
        expResult.put("auth", Boolean.toString(instance.auth));
        expResult.put("from", instance.from);
        expResult.put("host", instance.host);
        assertEquals(expResult, instance.getFields());
        
        //Test 4
        instance = new SimpleSettings("smtp.unice.fr", "25", "", "", 0, false, "gf004930@etu.unice.fr", "localhost");
        expResult = new HashMap<>();
        expResult.put("smtpHost", instance.smtpHost);
        expResult.put("smtpPort", instance.smtpPort);
        expResult.put("path", instance.path);
        expResult.put("user", instance.user);
        expResult.put("pass", instance.pass);
        expResult.put("delay", Integer.toString(instance.delay));
        expResult.put("auth", Boolean.toString(instance.auth));
        expResult.put("from", instance.from);
        expResult.put("host", instance.host);
        assertEquals(expResult, instance.getFields());
    }
}
