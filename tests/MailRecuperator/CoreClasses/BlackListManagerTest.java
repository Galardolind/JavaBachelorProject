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
import static org.mockito.Mockito.*;

/**
 *
 * @author Guizmo
 */
public class BlackListManagerTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        String path = System.getProperty("user.dir");
        int endIndex = path.lastIndexOf(File.separator);
        if (endIndex != -1) {
            String newstr = path.substring(endIndex, path.length());
            File fil = new File(System.getProperty("user.dir") + File.separator + "black list.txt");
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
            File fil = new File(System.getProperty("user.dir") + File.separator + "black list.txt");
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
     * Test of class BlackListManager.
     */
    @Test
    public void testBlackListManager() throws Exception {
        System.out.println("BlackListManager");
        String path = System.getProperty("user.dir");
        System.out.println(path);
        DataBase db = new DataBase(path);
//        boolean expResult = false;
//        boolean result = true;
//        // instancié une DB null renvoie false
        boolean expResult = false;
        boolean result = true;
        try {
            BlackListManager instance2 = new BlackListManager(null);
            result = true;
        } catch (ExceptionRecuperation e) {
            result = false;
        }
        assertEquals(expResult, result);

        BlackListManager instance = new BlackListManager(db);

        BlackListManager spy = spy(instance);

        // on delete la DB (doit toujours marcher reourne true)
        spy.deleteAllBlackList();
        verify(spy).deleteAllBlackList();


        // verifie que la methode a bien etait appelee avec un parametre null
        expResult = false;
        result = true;
        try {
            spy.addBlackListedDomain(null);
            result = true;
        } catch (ExceptionRecuperation e) {
            result = false;
        }
        assertEquals(expResult, result);
        verify(spy).addBlackListedDomain(null);

        // verifie que la methode a bien etait appelee
        spy.addBlackListedDomain("test");
        verify(spy).addBlackListedDomain("test");

        // il doit maintenant y avoir 1 élément dans la liste
        expResult = true;
        result = spy.getBlackListedDomain().size() == 1;
        assertEquals(expResult, result);
        verify(spy).getBlackListedDomain();


        // ajouter 5 domains normalement
        spy.addBlackListedDomain("test1");
        spy.addBlackListedDomain("test2");
        spy.addBlackListedDomain("test3");
        spy.addBlackListedDomain("test4");
        spy.addBlackListedDomain("test5");
        // null + test + test1->test5 = 7
        verify(spy, times(7)).addBlackListedDomain(anyString());

        // il doit maintenant y avoir 6 élément dans la liste
        expResult = true;
        result = spy.getBlackListedDomain().size() == 6;
        verify(spy, times(2)).getBlackListedDomain();
        assertEquals(expResult, result);


        // delete un element qui n'existe pas ne fait rien (pas d'exception)
        spy.deleteBlackListedDomain("tedqsdqsdqsd");

        // il doit toujours y avoir 6 élément dans la liste
        expResult = true;
        result = spy.getBlackListedDomain().size() == 6;
        assertEquals(expResult, result);
        verify(spy, times(3)).getBlackListedDomain();

        // delete un objet null retourne false
        expResult = false;
        result = true;
        try {
            spy.deleteBlackListedDomain(null);
        } catch (ExceptionRecuperation e) {
            result = false;
        }
        assertEquals(expResult, result);


        spy.deleteBlackListedDomain("test");

        // il doit maintenant y avoir 5 élément dans la liste
        expResult = true;
        result = spy.getBlackListedDomain().size() == 5;
        verify(spy, times(4)).getBlackListedDomain();
        assertEquals(expResult, result);

        // ajouter 2 domain qui sont pre existant retourne true
        spy.addBlackListedDomain("test1");
        spy.addBlackListedDomain("test2");

        // il doit toujours y avoir 5 élément dans la liste
        expResult = true;
        result = spy.getBlackListedDomain().size() == 5;
        verify(spy, times(5)).getBlackListedDomain();
        assertEquals(expResult, result);

        // ajouter 3 element et en delete 2 retourne true

        spy.addBlackListedDomain("test10");
        spy.addBlackListedDomain("test20");
        spy.addBlackListedDomain("test30");
        spy.deleteBlackListedDomain("test10");
        spy.deleteBlackListedDomain("test20");
        verify(spy, times(12)).addBlackListedDomain(anyString());
        verify(spy, times(5)).deleteBlackListedDomain(anyString());

        // il doit maintenant y avoir 6 élément dans la liste
        expResult = true;
        result = spy.getBlackListedDomain().size() == 6;
        verify(spy, times(6)).getBlackListedDomain();
        assertEquals(expResult, result);

        // on verifie si tous les elements sont la
        assertTrue(spy.getBlackListedDomain().contains("test30"));
        assertTrue(spy.getBlackListedDomain().contains("test1"));
        assertTrue(spy.getBlackListedDomain().contains("test2"));
        assertTrue(spy.getBlackListedDomain().contains("test3"));
        assertTrue(spy.getBlackListedDomain().contains("test4"));
        assertTrue(spy.getBlackListedDomain().contains("test5"));


        // on instancie une nouvelle DB
        BlackListManager instance3 = new BlackListManager(db);
        BlackListManager spy2 = spy(instance3);
        //elle doit contenir la meme chose que la premiere normalement
        assertTrue(spy2.getBlackListedDomain().contains("test30"));
        assertTrue(spy2.getBlackListedDomain().contains("test1"));
        assertTrue(spy2.getBlackListedDomain().contains("test2"));
        assertTrue(spy2.getBlackListedDomain().contains("test3"));
        assertTrue(spy2.getBlackListedDomain().contains("test4"));
        assertTrue(spy2.getBlackListedDomain().contains("test5"));

        verify(spy2, times(6)).getBlackListedDomain();

        // on verifie la taille
        expResult = true;
        result = instance.getBlackListedDomain().size() == 6;
        assertEquals(expResult, result);

        // on delete les DB retourne true
        spy.deleteAllBlackList();
        verify(spy, times(2)).deleteAllBlackList();
        spy2.deleteAllBlackList();
        verify(spy2).deleteAllBlackList();
    }
}
