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
import static org.mockito.Mockito.*;

/**
 *
 * @author Florian
 */
public class UrlFilterTest {
    
    public UrlFilterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        String path = System.getProperty("user.dir");
        int endIndex = path.lastIndexOf(File.separator);
        if (endIndex != -1) {
            String newstr = path.substring(endIndex, path.length());
            File fil = new File(path+ File.separator + "urlFilter.txt");
            if (fil.exists()) {
                fil.delete();
            }
            File fil2 = new File(path+ File.separator + newstr+".csv");
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
            File fil = new File(path+ File.separator + "urlFilter.txt");
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
     * Test of filterUrlAlreadyVisited method, of class UrlFilter.
     */
    @Test
    public void testFilterUrlAlreadyVisited() {
        UrlFilter instance;
        
        // db = null
        boolean expResult = false;
        boolean result = true;
        try {
            instance = new UrlFilter(null);
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
            instance = new UrlFilter(dbSpy);
            result = true;
        } catch(Exception e){
            result = false;
        }
        assertEquals(expResult, result);
        
        // method with param = null
        expResult = false;
        result = true;
        try {
            instance.filterUrlAlreadyVisited(null);
            result = true;
        } catch(Exception e){
            result = false;
        }
        assertEquals(expResult, result);
        
        // method with correct param
        // a dbspy will return the list I want
        expResult = true;
        result = false;
        ArrayList<String> urlResult = new ArrayList<String>();
        try {
            ArrayList<Row> testList = new ArrayList<Row>();
            Row row1 = new Row();
            row1.addColumn("filter","http://www.google.fr/");
            Row row2 = new Row();
            row2.addColumn("filter","http://deptinfo.unice.fr/");
            Row row3 = new Row();
            row3.addColumn("filter","http://ent.unice.fr/");
            testList.add(row1);
            testList.add(row2);
            testList.add(row3);
            TableJSON tableTest = new TableJSON("urlFilter", dbSpy.getPathDataBase());
            Header headTest = new Header();
            headTest.addColumn("filter", new Properties(Type.STRING, true));
            tableTest.setHeader(headTest);
            tableTest.addRow(row1);
            tableTest.addRow(row2);
            tableTest.addRow(row3);
            tableTest.save();
//            when(dbSpy.getTableByName("urlFilter").getAll()).thenReturn(testList);
            ArrayList<String> urls = new ArrayList<String>();
            urls.add("http://www.google.fr/");
            urls.add("http://www.monsite.fr/");
            urls.add("http://www.monurl.fr/");
            urls.add("http://deptinfo.unice.fr/");
            urls.add("http://www.monurl2.fr/");
            urlResult.addAll(instance.filterUrlAlreadyVisited(urls));
            result = true;
        } catch(Exception e){
            System.out.println(e);
            result = false;
        }
        assertEquals(expResult, result);
        
        // arraylist should contain the same urls after filtering
        ArrayList<String> urlExpected = new ArrayList<String>();
        urlExpected.add("http://www.monsite.fr/");
        urlExpected.add("http://www.monurl.fr/");
        urlExpected.add("http://www.monurl2.fr/");
        assertEquals(urlResult,urlExpected);
        
    }
}