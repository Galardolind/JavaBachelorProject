/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MailRecuperator.CoreClasses;

import java.io.File;
import Database.CoreClasses.*;
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
public class KeyWordManagerTest {
    
    public KeyWordManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        String path = System.getProperty("user.dir");
        int endIndex = path.lastIndexOf(File.separator);
        if (endIndex != -1) {
            String newstr = path.substring(endIndex, path.length());
            File fil = new File(System.getProperty("user.dir") + File.separator + "keywords.txt");
            if (fil.exists()) {
                fil.delete();
            }
            File fil2 = new File(System.getProperty("user.dir") + File.separator + newstr+".csv");
            if (fil2.exists()) {
                fil2.delete();
            }
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        String path = System.getProperty("user.dir");
        int endIndex = path.lastIndexOf(File.separator);
        if (endIndex != -1) {
            String newstr = path.substring(endIndex, path.length());
            File fil = new File(System.getProperty("user.dir") + File.separator + "keywords.txt");
            if (fil.exists()) {
                fil.delete();
            }
            File fil2 = new File(System.getProperty("user.dir") + File.separator + newstr+".csv");
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
     * Test of class KeyWordManager.
     */
    @Test
    public void testKeyWordManager() throws Exception {
        System.out.println("KeyWordManager");
        String path = System.getProperty("user.dir");
        System.out.println(path);
        DataBase db = new DataBase(path);
//        boolean expResult = false;
//        boolean result = true;
//        // instancié une DB null renvoie false
        boolean expResult = false;
        boolean result = true;
        try {
            KeyWordManager instance2 = new KeyWordManager(null);
            result = true;
        } catch(ExceptionRecuperation e){
            result = false;
        }
        assertEquals(expResult, result);
        
        KeyWordManager instance = new KeyWordManager(db);
        
        KeyWordManager spy = spy(instance);
        
        // on delete la DB (doit toujours marcher reourne true)
        spy.deleteAllKeyWord();
        verify(spy).deleteAllKeyWord();
        
        
        // verifie que la methode a bien etait appelee avec un parametre null
        expResult = false;
        result = true;
        try {
            spy.addKeyWord(null);
            result = true;
        } catch (ExceptionRecuperation e){
            result = false;
        }
        assertEquals(expResult, result);
        verify(spy).addKeyWord(null);
        
        // verifie que la methode a bien etait appelee
        spy.addKeyWord("test");
        verify(spy).addKeyWord("test");
        
        // il doit maintenant y avoir 1 élément dans la liste
        expResult = true;
        result = spy.getKeyWordList().size() == 1;
        assertEquals(expResult, result);
        verify(spy).getKeyWordList();
        
        
        // ajouter 5 keywords normalement
        spy.addKeyWord("test1");
        spy.addKeyWord("test2");
        spy.addKeyWord("test3");
        spy.addKeyWord("test4");
        spy.addKeyWord("test5");
        // null + test + test1->test5 = 7
        verify(spy, times(7)).addKeyWord(anyString());
        
        // il doit maintenant y avoir 6 élément dans la liste
        expResult = true;
        result = spy.getKeyWordList().size() == 6;
        verify(spy, times(2)).getKeyWordList();
        assertEquals(expResult, result);
        
        
        // delete un element qui n'existe pas ne fait rien (pas d'exception)
        spy.deleteKeyWord("tedqsdqsdqsd");
        
        // il doit toujours y avoir 6 élément dans la liste
        expResult = true;
        result = spy.getKeyWordList().size() == 6;
        assertEquals(expResult, result);
        verify(spy, times(3)).getKeyWordList();
        
        // delete un objet null retourne false
        expResult = false;
        result = true;
        try {
            spy.deleteKeyWord(null);
        } catch(ExceptionRecuperation e){
            result = false;
        }
        assertEquals(expResult, result);
        
        
        spy.deleteKeyWord("test");
        
         // il doit maintenant y avoir 5 élément dans la liste
        expResult = true;
        result = spy.getKeyWordList().size() == 5;
        verify(spy, times(4)).getKeyWordList();
        assertEquals(expResult, result);
        
        // ajouter 2 keyword qui sont pre existant retourne true
        spy.addKeyWord("test1");
        spy.addKeyWord("test2");
        
         // il doit toujours y avoir 5 élément dans la liste
        expResult = true;
        result = spy.getKeyWordList().size() == 5;
        verify(spy, times(5)).getKeyWordList();
        assertEquals(expResult, result);
        
        // ajouter 3 element et en delete 2 retourne true
        
        spy.addKeyWord("test10");
        spy.addKeyWord("test20");
        spy.addKeyWord("test30");
        spy.deleteKeyWord("test10");
        spy.deleteKeyWord("test20");
        verify(spy, times(12)).addKeyWord(anyString());
        verify(spy, times(5)).deleteKeyWord(anyString());
        
        // il doit maintenant y avoir 6 élément dans la liste
        expResult = true;
        result = spy.getKeyWordList().size() == 6;
        verify(spy, times(6)).getKeyWordList();
        assertEquals(expResult, result);
        
        // on verifie si tous les elements sont la
        assertTrue(spy.getKeyWordList().contains("test30"));
        assertTrue(spy.getKeyWordList().contains("test1"));
        assertTrue(spy.getKeyWordList().contains("test2"));
        assertTrue(spy.getKeyWordList().contains("test3"));
        assertTrue(spy.getKeyWordList().contains("test4"));
        assertTrue(spy.getKeyWordList().contains("test5"));
        
        
        // on instancie une nouvelle DB
        KeyWordManager instance3 = new KeyWordManager(db);
        KeyWordManager spy2 = spy(instance3);
        //elle doit contenir la meme chose que la premiere normalement
        assertTrue(spy2.getKeyWordList().contains("test30"));
        assertTrue(spy2.getKeyWordList().contains("test1"));
        assertTrue(spy2.getKeyWordList().contains("test2"));
        assertTrue(spy2.getKeyWordList().contains("test3"));
        assertTrue(spy2.getKeyWordList().contains("test4"));
        assertTrue(spy2.getKeyWordList().contains("test5"));
        
        verify(spy2, times(6)).getKeyWordList();
        
        // on verifie la taille
        expResult = true;
        result = instance.getKeyWordList().size() == 6;
        assertEquals(expResult, result);
        
        // on delete les DB retourne true
        spy.deleteAllKeyWord();
        verify(spy, times(2)).deleteAllKeyWord();
        spy2.deleteAllKeyWord();
        verify(spy2).deleteAllKeyWord();
    }

}
