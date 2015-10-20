/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MailSender.CoreClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author romain
 */
public class DatabaseEmailTest {

    public DatabaseEmailTest() {
    }

    @BeforeClass
    public static void setUpClass() throws FileNotFoundException {
        path = System.getProperty("user.dir") + File.separator + "zone_test";

        
    }

    static public void delete(String path) {
        File f = new File(path);
        for (File fs : f.listFiles()) {
            if (fs.isDirectory()) {
                delete(fs.toString());
            }
            fs.delete();
        }
        f.delete();
    }
    static String path;

    @AfterClass
    public static void tearDownClass() {

        File test_path = new File(path);
        delete(test_path.toString());
    }

    @Before
    public void setUp() throws FileNotFoundException {
        File pathdb = new File(path + File.separator + "data");
        pathdb.mkdirs();
        File db = new File(pathdb + File.separator + "data.csv");
        
        PrintStream ps1 = new PrintStream(db);
        ps1.print("emails;json;index;INTEGER;false;null;email;STRING;true;null;request;STRING;false;null;\n");
        ps1.close();
        
        File emails = new File(pathdb + File.separator + "emails.txt");
        PrintStream ps2 = new PrintStream(emails);
        ps2.print("[{\"index\":\"4\",\"email\":\"jf@i3s.unice.fr\",\"request\":\"jascques farre\"},{\"index\":\"1\",\"email\":\"enquiries@poney.com.my\",\"request\":\"poney\"},{\"index\":\"2\",\"email\":\"Jacques.Farre@unice.fr\",\"request\":\"jascques farre\"},{\"index\":\"0\",\"email\":\"jfesler@gigo.com\",\"request\":\"test\"},{\"index\":\"3\",\"email\":\"info@watchco.com\",\"request\":\"jascques farre\"}]");
        ps2.close();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testConstrutor() {
        System.out.println("Construteur");
        try {
            DatabaseEmail dbe = new DatabaseEmail(null);
            fail("devrait lever une exception");
        } catch (ExceptionSender ex) {
        }

        try {
            DatabaseEmail dbe = new DatabaseEmail("");
            fail("devrait lever une exception");
        } catch (ExceptionSender ex) {
        }

        try {
            DatabaseEmail dbe = new DatabaseEmail(path + File.separator +"test");
            fail("devrait lever une exception");
        } catch (ExceptionSender ex) {
        }


        try {
            DatabaseEmail dbe = new DatabaseEmail(path +File.separator +"data");
        } catch (ExceptionSender ex) {
            fail(ex.toString());
        }
    }

    /**
     * Test of getKeywordsList method, of class DatabaseEmail.
     */
    @Test
    public void testGetKeywordsList() {
        System.out.println("getKeywordsList");
        try {
            DatabaseEmail dbe = new DatabaseEmail(path + File.separator +"data");
            ArrayList<String> ses = dbe.getKeywordsList();
            assertEquals(ses.size(), 3);
        } catch (ExceptionSender ex) {
            fail(ex.toString());
        }
    }

    /**
     * Test of getMailAdressesByKeywords method, of class DatabaseEmail.
     */
    @Test
    public void testGetMailAdressesByKeywords() throws Exception {
        System.out.println("getMailAdressesByKeywords");
        ArrayList<String> s1 = new ArrayList<>();
        ArrayList<String> s2 = new ArrayList<>();
        s2.add("test");
        ArrayList<String> s3 = new ArrayList<>();
        s3.add("test");
        s3.add("poney");
        ArrayList<String> s4 = null;

        try {
            DatabaseEmail dbe = new DatabaseEmail(path + File.separator +"data");
            ArrayList<String> ses = dbe.getMailAdressesByKeywords(s1);
            assertEquals(ses.size(), 0);
        } catch (ExceptionSender ex) {
            fail(ex.toString());
        }
        try {
            DatabaseEmail dbe = new DatabaseEmail(path + File.separator +"data");
            ArrayList<String> ses = dbe.getMailAdressesByKeywords(s2);
            assertEquals(ses.size(), 1);
        } catch (ExceptionSender ex) {
            fail(ex.toString());
        }
        try {
            DatabaseEmail dbe = new DatabaseEmail(path + File.separator +"data");
            ArrayList<String> ses = dbe.getMailAdressesByKeywords(s3);
            assertEquals(ses.size(), 2);
        } catch (ExceptionSender ex) {
            fail(ex.toString());
        }
        try {
            DatabaseEmail dbe = new DatabaseEmail(path + File.separator +"data");
            ArrayList<String> ses = dbe.getMailAdressesByKeywords(s4);
            fail("devrait lever une exception");
        } catch (ExceptionSender ex) {
            
        }
    }

    /**
     * Test of deleteMailFromDatabase method, of class DatabaseEmail.
     */
    @Test
    public void testDeleteMailFromDatabase() throws Exception {
        System.out.println("deleteMailFromDatabase");
        ArrayList<String> s1 = new ArrayList<>();
        s1.add("jascques farre");

        try {
            DatabaseEmail dbe = new DatabaseEmail(path + File.separator +"data");
            dbe.deleteMailFromDatabase("jf@i3s.unice.fr");
            ArrayList<String> ses = dbe.getMailAdressesByKeywords(s1);
            assertEquals(ses.size(), 2);
        } catch (ExceptionSender ex) {
            fail(ex.toString());
        }
        
        try {
            DatabaseEmail dbe = new DatabaseEmail(path + File.separator +"data");
            dbe.deleteMailFromDatabase("");
        } catch (ExceptionSender ex) {
            fail(ex.toString());
        }
        try {
            DatabaseEmail dbe = new DatabaseEmail(path + File.separator +"data");
            dbe.deleteMailFromDatabase(null);
            fail("devrait lever une exception");
        } catch (ExceptionSender ex) {
        }
    }

    /**
     * Test of saveDatabase method, of class DatabaseEmail.
     */
    @Ignore
    @Test
    public void testSaveDatabase() throws Exception {
        /**
         * ces tests sont deja tester dans la classe Database
         */
    }
}