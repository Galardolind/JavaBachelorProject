/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MailSender.CoreClasses;

import MailSender.GUI.UserInterfacePopDialog;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Guizmo
 */
public class PopTest {
    
    public PopTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     *  Test of constructor, of class Pop.
     */
    @Test
    public void testPopConstructor(){
        System.out.println("constructor");
        {
            try {
                Pop instance = new Pop(null, null, null, null, true, null);
                fail("should launch an exception");
            } catch (ExceptionSender ex) {
                System.out.println(ex);
            }
        }
        {
            // empty String in constructor should launch an exception
            try {
                Pop instance = new Pop("", "", "", "", true, new UserInterfacePopDialog(null));
                fail("should launch an exception");
            } catch (ExceptionSender ex) {
                System.out.println(ex);
            }
        }
        {
            // port is String
            try {
                Pop instance = new Pop("a", "a", "a", "a", true, new UserInterfacePopDialog(null));
                fail("should launch an exception");
            } catch (ExceptionSender ex) {
                System.out.println(ex);
            }
        }
        {
            // port negative
            try {
                Pop instance = new Pop("a", "-25", "a", "a", true, new UserInterfacePopDialog(null));
                fail("should launch an exception");
            } catch (ExceptionSender ex) {
                System.out.println(ex);
            }
        }
        {
            // port to high
            try {
                Pop instance = new Pop("a", "1000025", "a", "a", true, new UserInterfacePopDialog(null));
                fail("should launch an exception");
            } catch (ExceptionSender ex) {
                System.out.println(ex);
            }
        }
        {
            // should run correctly
            try {
                Pop instance = new Pop("a", "25", "a", "a", true, new UserInterfacePopDialog(null));
            } catch (ExceptionSender ex) {
                fail("should not launch an exception "+ex);
            }
        }
    }   
    
    /**
     * Test of setStatus method, of class Pop.
     */
    @Test
    public void testSetStatus() {
        System.out.println("setStatus");
        PopStatus status = null;
        Pop instance = null;
        try {
            instance = new Pop("a", "25", "a", "a", true, new UserInterfacePopDialog(null));
        } catch (ExceptionSender ex) {
            fail("should not launch an exception");
        }
        try {
            instance.setStatus(status);
            fail("should launch an exception");
        } catch (ExceptionSender ex) {
        }
        try {
            status = new PopStatus();
            instance.setStatus(status);
        } catch (ExceptionSender ex) {
            fail("shouldn't launch an exception");
        }
    }
    

}
