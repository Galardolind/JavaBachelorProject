/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MailRecuperator.CoreClasses;

import Database.CoreClasses.*;
import java.io.File;
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
public class EmailAnalizerTest {

    public EmailAnalizerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
        String path = System.getProperty("user.dir")+ File.separator+ "zone_test";
        File test_path = new File(path );
        delete(test_path.toString());
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void EmailTest() {


        // initialiser un objet en partie null retourne false
        boolean expResult = false;
        boolean result = true;
        try {
            Email email0 = new Email("test@free.fr", null);
        } catch (ExceptionRecuperation e) {
            result = false;
        }
        assertEquals(expResult, result);


        // initialiser un objet en partie null retourne false
        expResult = false;
        result = true;
        try {
            Email email0 = new Email(null, "test");
        } catch (ExceptionRecuperation e) {
            result = false;
        }
        assertEquals(expResult, result);


        // initialiser un objet email avec un email string vide retourne false
        expResult = false;
        result = true;
        try {
            Email email0 = new Email("", "test");
        } catch (ExceptionRecuperation e) {
            result = false;
        }
        assertEquals(expResult, result);


        // initialiser un objet email avec un critere string vide retourne false
        expResult = false;
        result = true;
        try {
            Email email0 = new Email("test@gmail.com", "");
        } catch (ExceptionRecuperation e) {
            result = false;
        }
        assertEquals(expResult, result);


        // initiliaser une db null retourne false
        DataBase db = null;
        DataBase db2 = null;
        expResult = false;
        result = true;

        String path = System.getProperty("user.dir") + File.separator + "zone_test";
        try {
            Email emailDB = new Email(db, db2);
            result = true;
        } catch (ExceptionRecuperation  e) {
            result = false;
        }
        assertEquals(expResult, result);
        
        try {
            db = new DataBase(path);
            db2 = new DataBase(path);
            Email emailDB = new Email(db, db2);
        } catch (ExceptionRecuperation | ExceptionDataBase e) {
            fail (e.toString());
        }



        // initialiser un objet email avec une db normalement formée return true
        try {
            TableJSON blackList = new TableJSON("black list", path);
            Header head = new Header();
            head.addColumn("domain", new Properties(Type.STRING, true));
            blackList.setHeader(head);
            db.addTable(blackList);
            TableJSON emails = new TableJSON("emails", path);
            Header head2 = new Header();
            head2.addColumn("email", new Properties(Type.STRING));
            head2.addColumn("request", new Properties(Type.STRING));
            emails.setHeader(head2);
            db.addTable(emails);
        } catch (ExceptionDataBase e) {
            fail("should not return exception");
        }

        // initialiser un objet normalement retourne true (car la db est initialisee)
        try {
            Email email0 = new Email("test@gmail.com", "test");
        } catch (ExceptionRecuperation e) {
            fail(e.toString());
        }


    }

    @Test
    public void emailSaveTest() {

        boolean expResult = false;
        boolean result = true;

        DataBase db = null;
        String path = System.getProperty("user.dir")+ File.separator + "zone_test";
        // initiliaser une db normale retourne false
        try {
            db = new DataBase(path);
        } catch (ExceptionDataBase e) {
            fail("should not return exception");
        }
        expResult = true;
        result = false;

        DataBase db2 = null;
        TableJSON blackList = null;
        TableJSON emails = null;
        // initialisation de la db emails et blacklist
        try {
            db2 = new DataBase(path);
            blackList = new TableJSON("black list", path);
            Header head = new Header();
            head.addColumn("domain", new Properties(Type.STRING, true));
            blackList.setHeader(head);
            db2.addTable(blackList);
            emails = new TableJSON("emails", path);
            Header head2 = new Header();
            head2.addColumn("email", new Properties(Type.STRING));
            head2.addColumn("request", new Properties(Type.STRING));
            emails.setHeader(head2);
            db2.addTable(emails);
        } catch (ExceptionDataBase e) {
            fail(e.toString());
        }

        Row roww = new Row();
        roww.addColumn("domain", "yopmail");
        try {
            blackList.addRow(roww);
            blackList.save();
        } catch (ExceptionDataBase e) {
            fail(e.toString());
        }


        // initialiser normalement une db return true

        try {
            Email emailDB2 = new Email(db, db2);
        } catch (ExceptionRecuperation e) {
            fail(e.toString());
        }
        emails = (TableJSON) db.getTableByName("emails");
        // save un email yopmail retourne false
        try {
            Email email0 = new Email("test@yopmail.com", "test");
            email0.saveEmail();
        } catch (ExceptionRecuperation e) {
            fail("should not return exception");
        }

        assertTrue(emails.getRows("email", "test@yopmail.com").isEmpty());


        // save un email correct retourne true
        try {
            Email email0 = new Email("test@gmail.com", "test");
            email0.saveEmail();
        } catch (ExceptionRecuperation e) {
            fail("should not return exception");
        }
        assertTrue(emails.getRows("email", "test@gmail.com").size() == 1);

    }
    
    static public void delete(String path){
        File f =new File(path);
        for(File fs : f.listFiles()){
            if(fs.isDirectory()){
                delete(fs.toString());
            }
            fs.delete();
        }
        f.delete();
    }

}
