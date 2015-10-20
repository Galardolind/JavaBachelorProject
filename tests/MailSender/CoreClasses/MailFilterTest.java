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
import static org.mockito.Mockito.*;

/**
 *
 * @author Guizmo
 */
public class MailFilterTest {
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        String path = System.getProperty("user.dir");
        int endIndex = path.lastIndexOf(File.separator);
        if (endIndex != -1) {
            String newstr = path.substring(endIndex, path.length());
            File fil = new File(path+File.separator+"mailFilter.txt");
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
            File fil = new File(path+File.separator+"mailFilter.txt");
            if (fil.exists()) {
                fil.delete();
            }
            File fil2 = new File(path + File.separator + newstr+".csv");
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
     * Test of filterMailAlreadyMaild method, of class MailFilter.
     */
    @Test
    public void testFilterUrlAlreadyVisited() {
        MailFilter instance;
        
        // db = null
        boolean expResult = false;
        boolean result = true;
        try {
            instance = new MailFilter(null);
            result = true;
        } catch(Exception e){
            result = false;
        }
        assertEquals(expResult, result);
        
        // db correct
        expResult = true;
        result = false;
        instance = null;
        DataBase db = null;
        DataBase dbSpy = null;
        try {
            db = new DataBase(System.getProperty("user.dir"));
            dbSpy = spy(db);
            instance = new MailFilter(dbSpy);
            result = true;
        } catch(Exception e){
            result = false;
        }
        assertEquals(expResult, result);
        
        // method with param = null
        expResult = false;
        result = true;
        try {
            instance.filterMailAlreadyMaild(null);
            result = true;
        } catch(Exception e){
            result = false;
        }
        assertEquals(expResult, result);
        
        // method with correct param
        // a dbspy will return the list I want
        expResult = true;
        result = false;
        ArrayList<String> mailResult = new ArrayList<String>();
        try {
            ArrayList<Row> testList = new ArrayList<Row>();
            Row row1 = new Row();
            row1.addColumn("filter","blabla@gmail.com");
            Row row2 = new Row();
            row2.addColumn("filter","blublu@gmail.com");
            Row row3 = new Row();
            row3.addColumn("filter","test@gmail.com");
            testList.add(row1);
            testList.add(row2);
            testList.add(row3);
            TableJSON tableTest = new TableJSON("mailFilter", dbSpy.getPathDataBase());
            Header headTest = new Header();
            headTest.addColumn("filter", new Properties(Type.STRING, true));
            tableTest.setHeader(headTest);
            tableTest.addRow(row1);
            tableTest.addRow(row2);
            tableTest.addRow(row3);
            tableTest.save();
//            when(dbSpy.getTableByName("mailFilter").getAll()).thenReturn(testList);
            ArrayList<String> emails = new ArrayList<String>();
            emails.add("blabla@gmail.com");
            emails.add("bloblo@gmail.com");
            emails.add("blibli@gmail.com");
            emails.add("blublu@gmail.com");
            emails.add("omg@gmail.com");
            mailResult.addAll(instance.filterMailAlreadyMaild(emails));
            result = true;
        } catch(Exception e){
            System.out.println(e);
            result = false;
        }
        assertEquals(expResult, result);
        
        // arraylist should contain the same mails after filtering
        ArrayList<String> mailExpected = new ArrayList<String>();
        mailExpected.add("bloblo@gmail.com");
        mailExpected.add("blibli@gmail.com");
        mailExpected.add("omg@gmail.com");
        assertEquals(mailResult,mailExpected);
        
    }
}