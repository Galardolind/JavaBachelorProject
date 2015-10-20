/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MailSender.CoreClasses;

import Database.CoreClasses.*;
import java.io.File;
import java.util.ArrayList;
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
public class DatabaseHandlerTest {

    public DatabaseHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ExceptionSender {
        DatabaseHandler.cleanBD();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testConstructeur() {
        System.out.println("Constructeur");
        try {
            DatabaseHandler db = new DatabaseHandler();
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
    }

    /**
     * Test of cleanBD method, of class DatabaseHandler.
     */
    @Test
    public void testCleanBD() {
        System.out.println("cleanBD");
        try {
            DatabaseHandler db = new DatabaseHandler();
            DatabaseHandler.cleanBD();
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
    }

    /**
     * Test of deleteDirectory method, of class DatabaseHandler.
     */
    @Test
    public void testDeleteDirectory() {
        System.out.println("deleteDirectory");
        File path = null;
        try {
            DatabaseHandler db = new DatabaseHandler();
            DatabaseHandler.deleteDirectory(path);
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
        path = new File("");
        try {
            DatabaseHandler db = new DatabaseHandler();
            DatabaseHandler.deleteDirectory(path);
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
        path = new File(System.getProperty("user.dir") + "/data/sendData");
        try {
            DatabaseHandler db = new DatabaseHandler();
            DatabaseHandler.deleteDirectory(path);
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }

    }

    /**
     * Test of makeDirs method, of class DatabaseHandler.
     */
    @Test
    public void testMakeDirs() throws Exception {
        System.out.println("makeDirs");
        String path = null;
        try {
            DatabaseHandler db = new DatabaseHandler();
            db.makeDirs(path);
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
        path = "";
        try {
            DatabaseHandler db = new DatabaseHandler();
            db.makeDirs(path);
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
        path = System.getProperty("user.dir") + "/data/sendData";
        try {
            DatabaseHandler db = new DatabaseHandler();
            db.makeDirs(path);
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
    }

    /**
     * Test of getTemplateList method, of class DatabaseHandler.
     */
    @Test
    public void testGetTemplateList() {
        System.out.println("getTemplateList");

        try {
            DatabaseHandler db = new DatabaseHandler();
            ArrayList result = db.getTemplateList();
            assertEquals(0, result.size());
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }

        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveTemplate(null);
            fail("devrait lever une execption");
            ArrayList result = db.getTemplateList();
            assertEquals(0, result.size());
        } catch (ExceptionSender | ExceptionDataBase e) {
            
        }

        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveTemplate(new Template(null, null));
            ArrayList result = db.getTemplateList();
            assertEquals(0, result.size());
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }

        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveTemplate(new Template("", ""));
            ArrayList result = db.getTemplateList();
            assertEquals(0, result.size());
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }

        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveTemplate(new Template("name", "content"));
            ArrayList result = db.getTemplateList();
            assertEquals(1, result.size());
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }

        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveTemplate(new Template("name", "content"));
            db.saveTemplate(new Template("name", "content"));
            db.saveTemplate(new Template("name", "content"));
            ArrayList result = db.getTemplateList();
            assertEquals(1, result.size());
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveTemplate(new Template("name1", "content1"));
            db.saveTemplate(new Template("name2", "content2"));
            db.saveTemplate(new Template("name3", "content3"));
            ArrayList result = db.getTemplateList();
            assertEquals(4, result.size());
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }

    }

    /**
     * Test of getTemplate method, of class DatabaseHandler.
     */
    @Test
    public void testGetTemplate() {
        System.out.println("getTemplate");

        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveTemplate(new Template("name1", "content1"));
            db.saveTemplate(new Template("name2", "content2"));
            db.saveTemplate(new Template("name3", "content3"));
            Template result = db.getTemplate(null);
            assertNull(result);
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }

        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveTemplate(new Template("name1", "content1"));
            db.saveTemplate(new Template("name2", "content2"));
            db.saveTemplate(new Template("name3", "content3"));
            Template result = db.getTemplate("");
            assertNull(result);
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }

        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveTemplate(new Template("name1", "content1"));
            db.saveTemplate(new Template("name2", "content2"));
            db.saveTemplate(new Template("name3", "content3"));
            Template result = db.getTemplate("name1");
            assertEquals("\n$$template_EOO\ncontent1\n$$template_EOF\n", result.getContent());
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
    }

    /**
     * Test of getTemplateNameList method, of class DatabaseHandler.
     */
    @Test
    public void testGetTemplateNameList() {
        System.out.println("getTemplateNameList");
        try {
            DatabaseHandler db = new DatabaseHandler();
            ArrayList result = db.getTemplateNameList();
            assertEquals(0, result.size());
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }

        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveTemplate(new Template("name1", "content1"));
            db.saveTemplate(new Template("name2", "content2"));
            db.saveTemplate(new Template("name3", "content3"));
            ArrayList result = db.getTemplateNameList();
            assertEquals(3, result.size());
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
    }

    /**
     * Test of saveTemplate method, of class DatabaseHandler.
     */
    @Ignore
    @Test
    public void testSaveTemplate() throws Exception {
        /**
         * les tests de cette methodes sont fait dans les autres fonction de
         * tests
         */
    }

    /**
     * Test of editTemplate method, of class DatabaseHandler.
     */
    @Test
    public void testEditTemplate() throws Exception {
        System.out.println("editTemplate");
        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveTemplate(new Template("name1", "content1"));
            db.saveTemplate(new Template("name2", "content2"));
            db.saveTemplate(new Template("name3", "content3"));
            db.editTemplate(null, null);
            fail("devrait lever une exception");
        } catch (ExceptionSender | ExceptionDataBase e) {
        }

        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveTemplate(new Template("name1", "content1"));
            db.saveTemplate(new Template("name2", "content2"));
            db.saveTemplate(new Template("name3", "content3"));
            db.editTemplate(new Template(null, null), null);
            fail("devrait lever une exception");
        } catch (ExceptionSender | ExceptionDataBase e) {
        }


        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveTemplate(new Template("name1", "content1"));
            db.saveTemplate(new Template("name2", "content2"));
            db.saveTemplate(new Template("name3", "content3"));
            db.editTemplate(new Template("", ""), "");
            fail("devrait lever une exception");
        } catch (ExceptionSender | ExceptionDataBase e) {
        }

        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveTemplate(new Template("name1", "content1"));
            db.saveTemplate(new Template("name2", "content2"));
            db.saveTemplate(new Template("name3", "content3"));
            db.editTemplate(new Template("name1", "content11"), "name1");
            Template t = db.getTemplate("name1");
            assertEquals(t.getContent(), "\n$$template_EOO\ncontent11\n$$template_EOF\n");
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }

    }

    /**
     * Test of deleteTemplate method, of class DatabaseHandler.
     */
    @Test
    public void testDeleteTemplate() throws Exception {
        System.out.println("deleteTemplate");
        DatabaseHandler db = null;
        try {
            db = new DatabaseHandler();
            db.saveTemplate(new Template("name1", "content1"));
            db.saveTemplate(new Template("name2", "content2"));
            db.saveTemplate(new Template("name3", "content3"));
            db.deleteTemplate(null);
            fail("devrait lever une exception");
        } catch (ExceptionSender | ExceptionDataBase e) {
        }
        ArrayList a = db.getTemplateList();
        assertEquals(a.size(), 3);

        try {
            db = new DatabaseHandler();
            db.saveTemplate(new Template("name1", "content1"));
            db.saveTemplate(new Template("name2", "content2"));
            db.saveTemplate(new Template("name3", "content3"));
            db.deleteTemplate("");
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }

        ArrayList b = db.getTemplateList();
        assertEquals(b.size(), 3);
        try {
            db = new DatabaseHandler();
            db.saveTemplate(new Template("name1", "content1"));
            db.saveTemplate(new Template("name2", "content2"));
            db.saveTemplate(new Template("name3", "content3"));
            db.deleteTemplate("name1");

        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
        ArrayList c = db.getTemplateList();
        assertEquals(c.size(), 2);
    }

    /**
     * Test of saveUserSettings method, of class DatabaseHandler.
     */
    @Test
    public void testSaveUserSettings() {
        System.out.println("saveUserSettings");
        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveUserSettings(null);
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveUserSettings(new SimpleSettings(null, null, null, null, 1, true, null, null));
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveUserSettings(new SimpleSettings(null, null, null, null, 1, true, null, null, null));
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveUserSettings(new SimpleSettings(null, null, null, null, 1, true, null, null, null));
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
    }

    /**
     * Test of loadUserSettings method, of class DatabaseHandler.
     */
    @Test
    public void testLoadUserSettings() {
        System.out.println("loadUserSettings");
        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveUserSettings(null);
            SimpleSettings s = db.loadUserSettings();
            assertNull(s);
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveUserSettings(new SimpleSettings(null, null, null, null, 1, true, null, null));
            SimpleSettings s = db.loadUserSettings();
            assertNull(s);
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveUserSettings(new SimpleSettings(null, null, null, null, 1, true, null, null, null));
            SimpleSettings s = db.loadUserSettings();
            assertNull(s);
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
        try {
            DatabaseHandler db = new DatabaseHandler();
            db.saveUserSettings(new SimpleSettings(null, null, null, null, 1, true, null, null, null));
            SimpleSettings s = db.loadUserSettings();
            assertNull(s);
        } catch (ExceptionSender | ExceptionDataBase e) {
            fail(e.toString());
        }
    }
}