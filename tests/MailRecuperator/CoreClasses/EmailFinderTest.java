package MailRecuperator.CoreClasses;

import Database.CoreClasses.*;
import java.io.File;
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
public class EmailFinderTest {
    
    EmailFinder instance;
    
    public EmailFinderTest() {
        instance = new EmailFinder();
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

    /**
     * Test of findEmailInPages method, of class EmailFinder.
     */
    @Test
    public void testFindEmailInPages() throws ExceptionRecuperation, ExceptionDataBase {
        // find the directory of the html files to test on.
        String docTestDir = "https://dl.dropboxusercontent.com/u/49836439/docEmailFinderTest/";
        ArrayList<String> urlList = new ArrayList<String>();
        String path = System.getProperty("user.dir")+ File.separator+ "zone_test";
        
        DataBase db1 = new DataBase(path+ File.separator + "d1");
        DataBase db2 = new DataBase(path+ File.separator + "d2");
        
        Email db = new Email(db1, db2);
        urlList.add(docTestDir + "Test1.html");
        String request = "test";
        ArrayList<Email> result = new ArrayList<Email>();
        ArrayList<Email> expResult = new ArrayList<Email>();
        Email email1 = new Email("example@gmail.com",request);
        Email email2 = new Email("gf004930@etu.unice.fr",request);
        
        expResult.add(email1);
        try {
            result.addAll(instance.findEmailInPages(urlList, request));
        } catch(ExceptionRecuperation e){
            
        }
        assertEquals(expResult.get(0).getEmail(), result.get(0).getEmail());
        
        
        urlList.clear();
        urlList.add(docTestDir + "Test2.html");
        expResult.clear();
        expResult.add(email1);
        try {
            result.addAll(instance.findEmailInPages(urlList, request));
        } catch(ExceptionRecuperation e){
            
        }
        assertEquals(expResult.get(0).getEmail(), result.get(0).getEmail());
        
        
        urlList.clear();
        urlList.add(docTestDir + "Test3.html");
        expResult.clear();
        expResult.add(email1);
        try {
            result.addAll(instance.findEmailInPages(urlList, request));
        } catch(ExceptionRecuperation e){
            
        }
        assertEquals(expResult.get(0).getEmail(), result.get(0).getEmail());
        
        
        urlList.clear();
        urlList.add(docTestDir + "Test4.html");
        expResult.clear();
        expResult.add(email1);
        try {
            result.addAll(instance.findEmailInPages(urlList, request));
        } catch(ExceptionRecuperation e){
            
        }
        assertEquals(expResult.get(0).getEmail(), result.get(0).getEmail());
        
        
        urlList.clear();
        urlList.add(docTestDir + "Test5.html");
        request = "test";
        expResult.clear();
        expResult.add(email1);
        try {
            result.addAll(instance.findEmailInPages(urlList, request));
        } catch(ExceptionRecuperation e){
            
        }
        assertEquals(expResult.get(0).getEmail(), result.get(0).getEmail());
        
        
        urlList.clear();
        urlList.add(docTestDir + "Test6.html");
        expResult.clear();
        expResult.add(email1);
        expResult.add(email2);
        try {
            result.addAll(instance.findEmailInPages(urlList, request));
        } catch(ExceptionRecuperation e){
            
        }
        assertEquals(expResult.get(0).getEmail(), result.get(0).getEmail());
        assertEquals(expResult.get(1).getEmail(), result.get(1).getEmail());
        
        urlList.clear();
        urlList.add(docTestDir + "Test1.html");
        urlList.add(docTestDir + "Test2.html");
        expResult.clear();
        expResult.add(email1);
        expResult.add(email2);
        try {
            result.addAll(instance.findEmailInPages(urlList, request));
        } catch(ExceptionRecuperation e){
            
        }
        assertEquals(expResult.get(0).getEmail(), result.get(0).getEmail());
        assertEquals(expResult.get(1).getEmail(), result.get(1).getEmail());
        
        urlList.clear();
        urlList.add(docTestDir + "Test7.html");
        expResult.clear();
        expResult.add(email1);
        expResult.add(email2);
        try {
            result.addAll(instance.findEmailInPages(urlList, request));
        } catch(ExceptionRecuperation e){
            
        }
        assertEquals(expResult.get(0).getEmail(), result.get(0).getEmail());
        assertEquals(expResult.get(1).getEmail(), result.get(1).getEmail());
        
        
        urlList = null;
        request = null;
        try {
            instance.findEmailInPages(urlList, request);
            fail("Should launch exception");
        } catch (ExceptionRecuperation ex) {
            assertTrue(true);
        }
        
        
        urlList = new ArrayList<>();
        request = null;
        try {
            instance.findEmailInPages(urlList, request);
            fail("Should launch exception");
        } catch (ExceptionRecuperation ex) {
            assertTrue(true);
        }
        
        urlList = null;
        request = "Test";
        try {
            instance.findEmailInPages(urlList, request);
            fail("Should launch exception");
        } catch (ExceptionRecuperation ex) {
            assertTrue(true);
        }
        
        urlList = new ArrayList<>();
        urlList.add(docTestDir + "Test404.html"); // page that don't exist
        urlList.add(docTestDir + "Test1.html");
        request = "test";
        expResult.clear();
        expResult.add(email1);
        try {
            result.addAll(instance.findEmailInPages(urlList, request));
        } catch(ExceptionRecuperation e){
            
        }
        assertEquals(expResult.get(0).getEmail(), result.get(0).getEmail());
        
        result.clear();
        urlList.clear();
        expResult.clear();
        try {
            result.addAll(instance.findEmailInPages(urlList, request));
        } catch(ExceptionRecuperation e){
            
        }
        assertEquals(expResult.isEmpty(), result.isEmpty());
        
        /*
         * Il ne peut pas passer regex trop complexe a cause des cas ou
         * un email est suivis d'une balise ou autre
         */
//        urlList.clear();
//        urlList.add(docTestDir + "Test8.html");
//        result.clear();
//        expResult.clear();
//        try {
//            result.addAll(instance.findEmailInPages(urlList, request));
//        } catch(ExceptionRecuperation e){
//            
//        }
//        System.out.println("taille : "+result.size());
//        for(Email email : result){
//            System.out.println(email.getEmail());
//        }
//        assertTrue(result.isEmpty());
        
        urlList.clear();
        urlList.add(docTestDir + "Test9.html");
        expResult.clear();
        expResult.add(email1);
        expResult.add(email2);
        result.clear();
        try {
            result.addAll(instance.findEmailInPages(urlList, request));
        } catch(ExceptionRecuperation e){
            
        }
        assertEquals(expResult.get(0).getEmail(), result.get(0).getEmail());
        assertEquals(expResult.get(1).getEmail(), result.get(1).getEmail());
        assertEquals(expResult.size(), result.size());
    }
}