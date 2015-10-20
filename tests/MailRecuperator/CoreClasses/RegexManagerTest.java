package MailRecuperator.CoreClasses;

import java.io.File;
import Database.CoreClasses.*;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Florian
 */
public class RegexManagerTest {
    
    public RegexManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        String path = System.getProperty("user.dir");
        int endIndex = path.lastIndexOf(File.separator);
        if (endIndex != -1) {
            String newstr = path.substring(endIndex, path.length());
            File fil = new File(path + File.separator + "regexs.txt");
            if (fil.exists()) {
                fil.delete();
            }
            File fil2 = new File(path + File.separator + newstr+".csv");
            if (fil2.exists()) {
                fil2.delete();
            }
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        String path = System.getProperty("user.dir");
        int endIndex = path.lastIndexOf(File.separator);
        if (endIndex != -1) {
            String newstr = path.substring(endIndex, path.length());
            File fil = new File(path+ File.separator + "regexs.txt");
            if (fil.exists()) {
                fil.delete();
            }
            File fil2 = new File(path+ File.separator + newstr+".csv");
            if (fil2.exists()) {
                fil2.delete();
            }
        }
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of Constructor, of class RegexManager.
     */
    @Test
    public void testConstructor() throws ExceptionDataBase {
        System.out.println("constructor");
        DataBase db = null;
        RegexManager instance;
        try {
            instance = new RegexManager(db);
            fail("Should throw ExceptionRecueration");
        } catch (ExceptionRecuperation ex) {
            assertTrue(true);
        }
        try {
            instance = new RegexManager(new DataBase(""));
        } catch (ExceptionRecuperation ex) {
            fail("Should not throw ExceptionRecuperation");
        }
    }

    /**
     * Test of getRegexList method, of class RegexManager.
     */
    @Test
    public void testGetRegexList() throws ExceptionDataBase {
        System.out.println("getRegexList");
        DataBase db = new DataBase("");
        RegexManager instance;
        ArrayList<String> expResult;
        ArrayList<String> result;
        InterfaceTable t;
        Row row;
        String regexTest;
        try {
            instance = new RegexManager(db);
            try {
                // nothing in the table
                t = db.getTableByName("regexs");
                t.dropTable();
                result = instance.getRegexList();
                expResult = new ArrayList<>();
                assertEquals(expResult, result);
                
                // one regex
                row = new Row();
                regexTest = "regex1";
                row.addColumn("regex", regexTest);
                t.addRow(row);
                t.save();
                expResult.add(regexTest);
                result = instance.getRegexList();
                assertEquals(expResult, result);
                
                // two regex
                row = new Row();
                regexTest = "regex2";
                row.addColumn("regex", regexTest);
                t.addRow(row);
                t.save();
                expResult.add(regexTest);
                result = instance.getRegexList();
                assertEquals(expResult, result);
            } catch (ExceptionRecuperation ex) {
                fail(""+ ex);
            }
        } catch (ExceptionRecuperation ex) {
            fail(""+ ex);
        }
    }

    /**
     * Test of addRegex method, of class RegexManager.
     */
    @Test
    public void testAddRegex() throws ExceptionDataBase {
        System.out.println("addRegex");
        String regex = "";
        DataBase db;
        RegexManager instance;
        InterfaceTable t;
        Row row;
        ArrayList<Row> expResult = new ArrayList<>();
        ArrayList<Row> result = new ArrayList<>();
        try {
            db = new DataBase("");
            instance = new RegexManager(db);
            t = db.getTableByName("regexs");
            t.dropTable();
            
            // regex = null
            try {
                instance.addRegex(null);
                fail("Should throw ExceptionRecuperation");
            } catch (ExceptionRecuperation ex) {
                assertTrue(true);
            }
            
            // regex = ""
            try {
                instance.addRegex("");
                fail("Should throw ExceptionRecuperation");
            } catch (ExceptionRecuperation ex) {
                assertTrue(true);
            }
            
            // one regex add in an empty table
            regex = "regex1";
            row = new Row();
            row.addColumn("regex", regex);
            row.addColumn("index", "0");
            expResult.add(row);
            assertTrue(instance.addRegex(regex));
            result = t.getAll();
            assertEquals(expResult, result);
            
            // one regex add in the previous table
            regex = "regex2";
            row = new Row();
            row.addColumn("regex", regex);
            row.addColumn("index", "1");
            expResult.add(row);
            assertTrue(instance.addRegex(regex));
            result = t.getAll();
            assertEquals(expResult, result);
        } catch (ExceptionRecuperation ex) {
                fail(""+ ex);
        }
    }

    /**
     * Test of deleteRegex method, of class RegexManager.
     */
    @Test
    public void testDeleteRegex() throws ExceptionDataBase {
        System.out.println("deleteRegex");
        String regex = "";
        DataBase db;
        RegexManager instance;
        InterfaceTable t;
        Row row;
        ArrayList<Row> expResult = new ArrayList<>();
        ArrayList<Row> result = new ArrayList<>();
        try {
            db = new DataBase("");
            instance = new RegexManager(db);
            t = db.getTableByName("regexs");
            
            // regex = null
            try {
                instance.deleteRegex(null);
                fail("Should throw ExceptionRecuperation");
            } catch (ExceptionRecuperation ex) {
                assertTrue(true);
            }
            
            // regex = ""
            try {
                instance.deleteRegex("");
                fail("Should throw ExceptionRecuperation");
            } catch (ExceptionRecuperation ex) {
                assertTrue(true);
            }
            
            // one regex in a table with only one regex
            regex = "regex";
            row = new Row();
            row.addColumn("regex", regex);
            t.addRow(row);
            instance.deleteRegex(regex);
            result = t.getAll();
            assertEquals(expResult, result);
            
            // two regex the table, one to delete
            t.dropTable();
            regex = "regex1";
            row = new Row();
            row.addColumn("regex", regex);
            t.addRow(row);
            row.addColumn("index", "0");
            expResult.add(row);
            regex = "regex2";
            row = new Row();
            row.addColumn("regex", regex);
            t.addRow(row);
            row.addColumn("index", "1");
            expResult.add(row);
            result = t.getAll();
            assertEquals(result, expResult);
            expResult.remove(row);
            instance.deleteRegex(regex);
            result = t.getAll();
            assertEquals(expResult, result);
        } catch (ExceptionRecuperation ex) {
                fail(""+ ex);
        }
    }

    /**
     * Test of deleteAllRegex method, of class RegexManager.
     */
    @Test
    public void testDeleteAllRegex() throws ExceptionDataBase {
        System.out.println("deleteAllRegex");
        String regex = "";
        DataBase db;
        RegexManager instance;
        InterfaceTable t;
        Row row;
        ArrayList<Row> expResult = new ArrayList<>();
        ArrayList<Row> result = new ArrayList<>();
        try {
            db = new DataBase("");
            instance = new RegexManager(db);
            t = db.getTableByName("regexs");
            
            // one regex in a table with only one regex
            regex = "regex";
            row = new Row();
            row.addColumn("regex", regex);
            t.addRow(row);
            row.addColumn("index", "0");
            result = t.getAll();
            expResult.add(row);
            assertEquals(result, expResult);
            instance.deleteAllRegex();
            result = t.getAll();
            expResult = new ArrayList<>();
            assertEquals(expResult, result);
            
            // two regex the table
            t.dropTable();
            regex = "regex1";
            row = new Row();
            row.addColumn("regex", regex);
            t.addRow(row);
            row.addColumn("index", "0");
            expResult.add(row);
            regex = "regex2";
            row = new Row();
            row.addColumn("regex", regex);
            t.addRow(row);
            row.addColumn("index", "1");
            expResult.add(row);
            result = t.getAll();
            assertEquals(result, expResult);
            expResult = new ArrayList<>();
            instance.deleteAllRegex();
            result = t.getAll();
            assertEquals(expResult, result);
        } catch (ExceptionRecuperation ex) {
                fail(""+ ex);
        }
    }
}