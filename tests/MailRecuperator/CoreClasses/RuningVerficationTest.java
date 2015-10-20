
package MailRecuperator.CoreClasses;

import java.io.File;
import static org.mockito.Mockito.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Cette classe a pour but de tester le déroulement du programme
 * via les mockito spy -> appel de methode etc ...
 */
public class RuningVerficationTest {
    
    @BeforeClass
    public static void setUpClass() {
        String path = System.getProperty("user.dir");
        File fil1 = new File(path+File.separator+"emails.txt");
        fil1.delete();
        int endIndex = path.lastIndexOf(File.separator);
        if (endIndex != -1) {
            String newstr = path.substring(endIndex, path.length());
            File fil2 = new File(path+ File.separator + newstr+".csv");
            if (fil2.exists()) {
                fil2.delete();
            }
        }
        path = System.getProperty("user.dir")+File.separator+"data"+File.separator+"recuperatorData"+File.separator;
        File fil3 = new File(path+"emails.txt");
        fil3.delete();
        File fil4 = new File(path+"black list.txt");
        fil4.delete();
        File fil5 = new File(path+"urlFilter.txt");
        fil5.delete();
        File fil6 = new File(path+"recuperatorData.csv");
        fil6.delete();
        File fil7 = new File(path+"keywords.txt");
        fil7.delete();
        File fil8 = new File(path+"regexs.txt");
        fil8.delete();
    }
    
    @AfterClass
    public static void tearDownClass() {
        String path = System.getProperty("user.dir");
        File fil1 = new File(path+File.separator+"emails.txt");
        fil1.delete();
        int endIndex = path.lastIndexOf(File.separator);
        if (endIndex != -1) {
            String newstr = path.substring(endIndex, path.length());
            File fil2 = new File(path+ File.separator + newstr+".csv");
            if (fil2.exists()) {
                fil2.delete();
            }
        }
        path = System.getProperty("user.dir")+File.separator+"data"+File.separator+"recuperatorData"+File.separator;
        File fil3 = new File(path+"emails.txt");
        fil3.delete();
        File fil4 = new File(path+"black list.txt");
        fil4.delete();
        File fil5 = new File(path+"urlFilter.txt");
        fil5.delete();
        File fil6 = new File(path+"recuperatorData.csv");
        fil6.delete();
        File fil7 = new File(path+"keywords.txt");
        fil7.delete();
        File fil8 = new File(path+"regexs.txt");
        fil8.delete();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void emailRecuperationTest(){
        System.out.println("emailRecuperation");
        
        String path = System.getProperty("user.dir");
        
        // initialisation nulle return false
        boolean expResult = false;
        boolean result = true;
        try {
            EmailRecuperation eRecupNull = new EmailRecuperation(null, null,0);
            result = true;
        }catch(ExceptionRecuperation e){
            result = false;
        }
        assertEquals(expResult, result);
        
        // essayer de lancer le programme sans intialiser return false
        expResult = false;
        result = true;
        try {
            EmailRecuperation eRecupNul = null;
            eRecupNul.doPersonalizeSearch(1);
            result = true;
        }catch(Exception e){
            result = false;
        }
        assertEquals(expResult, result);
        
        
        // initialisation du Mockito spy
        EmailRecuperation eRecup = null;
        try {
            eRecup = new EmailRecuperation("carrot",path,1);
        } catch (ExceptionRecuperation ex) {
            ex.printStackTrace();
            fail("should not return exception");
        }
        EmailRecuperation spy = spy(eRecup);
        
        StateOfRecup state = new StateOfRecup(0, 0, true, false);
        spy.setVar(state);
        // lancer la recherche incorrectement return false
        expResult = false;
        result = true;
        try {
            spy.start();
            spy.doPersonalizeSearch(0);
            result = true;
        } catch (ExceptionRecuperation e) {
            result = false;
        }
        assertEquals(expResult, result);
        
        // lancer la recherche incorrectement return false
        expResult = false;
        result = true;
        try {
            spy.doPersonalizeSearch(-1);
            result = true;
        } catch (ExceptionRecuperation e) {
            result = false;
        }
        assertEquals(expResult, result);
        
        // lancer la recherche normalement ne devrait pas returne d'exception
        try {
            spy.doPersonalizeSearch(1);
        } catch (ExceptionRecuperation e) {
            fail("should'nt launch exception");
        }
        
        // le fichier emails devrait exister maintenant
        expResult = true;
        result = false;
        try {
            File fil = new File(path+File.separator+"emails.txt");
            result = fil.exists();
        } catch(Exception e){
            result = false;
        }
        assertEquals(expResult, result);
        
        // le fichier black list devrait exister maintenant
        expResult = true;
        result = false;
        try {
            File fil = new File(path+File.separator+"data"+File.separator+"recuperatorData"+File.separator+"black list.txt");
            result = fil.exists();
        } catch(Exception e){
            result = false;
        }
        assertEquals(expResult, result);
        
        // le fichier urlFilter devrait exister maintenant
        expResult = true;
        result = false;
        try {
            File fil = new File(path+File.separator+"data"+File.separator+"recuperatorData"+File.separator+"urlFilter.txt");
            result = fil.exists();
        } catch(Exception e){
            result = false;
        }
        assertEquals(expResult, result);
        
        // on initialise une nouvelle EmailRecuperation
        expResult = true;
        result = false;
        EmailRecuperation emr = null;
        try {
            emr = new EmailRecuperation("mailto", System.getProperty("user.dir"),1);
            result = true;
        } catch(ExceptionRecuperation e){
            System.out.println(e);
            result = false;
        }
        assertEquals(expResult, result);
        
        // on lance la recherche
        emr.setVar(state);
        expResult = true;
        result = false;
        try {
            emr.start();
            emr.doPersonalizeSearch(1);
            result = true;
        } catch(ExceptionRecuperation e){
            result = false;
        }
        assertEquals(expResult, result);
        
        // le fichier emails devrait exister maintenant dans l'autre chemin
        expResult = true;
        result = false;
        try {
            File fil = new File(System.getProperty("user.dir")+File.separator+"emails.txt");
            result = fil.exists();
        } catch(Exception e){
            result = false;
        }
        assertEquals(expResult, result);
        
    }
    
}
