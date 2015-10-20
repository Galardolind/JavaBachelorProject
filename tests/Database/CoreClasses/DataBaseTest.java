package Database.CoreClasses;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class DataBaseTest {

    public DataBaseTest() {
    }

    
    static private String bdManuelTestCV = "table_test;json;name;STRING;false;null;surname;STRING;false;null;age;INTEGER;false;null;";
    static private String tableTestTXT = "[\n"
            + "  {\n"
            + "     \"index\":\"0\",\n"
            + "     \"name\":\"moumoute\",\n"
            + "     \"surname\":\"powa\",\n"
            + "     \"age\":\"20\"\n"
            + "  },{\n"
            + "     \"index\":\"1\",\n"
            + "     \"name\":\"sadoitplanter\",\n"
            + "     \"surname\":\"tresfort\",\n"
            + "     \"age\":\"10\"\n"
            + "  },{\n"
            + "     \"index\":\"2\",\n"
            + "     \"name\":\"sadoitmarcher\",\n"
            + "     \"surname\":\"20\",\n"
            + "     \"age\":\"10\"\n"
            + "  }\n"
            + "]";
    static private String bdManuelTestDefectueuseCSV = "table_test;json;name;STRING;false;null;surname;STRING;false;null;overflow_field;STRING;false;null;\n"
            + "table_test_foireuse;json;name;STRING;false;false;surname;STRING;false;false;";
    static private String tableTestDefectueuseTXT = "[\n"
            + "  {\n"
            + "    \"name\":\"youpi\",\n"
            + "    \"surname\":\"tralala\"\n"
            + "  }\n"
            + "]";
    
    static private File test_path;
    
    static public void delete(String path){
        File f =new File(path);
        if(!f.isDirectory()){
            f.delete();
            return;
        }
        for(File fs : f.listFiles()){
            if(fs.isDirectory()){
                delete(fs.toString());
            }
            fs.delete();
        }
        f.delete();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        //File f = new File("")
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        String path = System.getProperty("user.dir");
        test_path = new File(path + File.separator+ "zone_test");
        delete(test_path.toString());
        delete("projet licence.csv");
    }

    @Before
    public void setUp() throws FileNotFoundException {
        String path = System.getProperty("user.dir");
        test_path = new File(path + File.separator+ "zone_test");
        File bdTest = new File(test_path.getAbsolutePath() + File.separator + "bd manuel");
        
        bdTest.mkdirs();
        
        File bdTestCSV = new File(bdTest + File.separator + "bd manuel.csv");
        File tableTest = new File(bdTest+ File.separator + "table_test.txt");

        PrintStream ps = new PrintStream(bdTestCSV);
        ps.print(bdManuelTestCV);
        ps.close();
        ps = new PrintStream(tableTest);
        ps.print(tableTestTXT);
        ps.close();
        
        File bdDefectueuseTest = new File(test_path.getAbsolutePath() + File.separator + "bd manuel defectueuse");
        
        bdDefectueuseTest.mkdirs();
        
        File bdTestCSVdefectueuse = new File(bdDefectueuseTest + File.separator + "bd manuel defectueuse.csv");
        File tableTestTEXTdefectueuse = new File(bdDefectueuseTest+ File.separator + "table_test.txt");
        
        ps = new PrintStream(bdTestCSVdefectueuse);
        ps.print(bdManuelTestDefectueuseCSV);
        ps.close();
        ps = new PrintStream(tableTestTEXTdefectueuse);
        ps.print(tableTestDefectueuseTXT);
        ps.close();
        
    }

    @After
    public void tearDown() {
        File bdTest = new File(test_path.getAbsolutePath() + File.separator + "bd manuel");
        bdTest.delete();
        File bdDefectueuseTest = new File(test_path.getAbsolutePath() + File.separator + "bd manuel defectueuse");
        bdDefectueuseTest.delete();
    }

    /**
     * Constructor Test, of class Database the only constructor tested is :
     * Database(String, String);
     */
    @Test
    public void testDataBase() throws Exception {
        //DataBase (path, name)
        System.out.println("Constructor");


        //cas : path (string vide), name (String vide) : true
        //creation en local test de la valeur path = ""
        boolean expResult = true;
        boolean result = false;
        try {
            DataBase instance = new DataBase("");
            result = true;
        } catch (ExceptionDataBase e) {
        }
        assertEquals(expResult, result);

        //cas : path (string vide), name (String vide) : true
        //chargement de la bd avec path = ""
        expResult = true;
        result = false;
        try {
            DataBase instance = new DataBase("");
            result = true;
        } catch (ExceptionDataBase e) {
        }
        assertEquals(expResult, result);

        //cas : path (string possible) :
        expResult = true;
        result = false;
        try {
            DataBase instance = new DataBase(test_path + File.separator + "abracadabra");
            result = true;
        } catch (ExceptionDataBase e) {
        }
        assertEquals(expResult, result);

        expResult = true;
        result = false;
        //cas : path (String existing bd), name (String vide) : false 
        //load impossible, bd inexistante
        try {
            DataBase instance = new DataBase(test_path + File.separator + "testBD");
            result = true;
        } catch (ExceptionDataBase e) {
        }
        assertEquals(expResult, result);

        expResult = true;
        result = false;
        //cas : path (string vide), name (String existing BD) : true
        //create fonctionnel
        try {
            DataBase instance = new DataBase(test_path + File.separator + "testBD");
            result = true;
        } catch (ExceptionDataBase e) {
        }
        assertEquals(expResult, result);

        expResult = true;
        result = false;
        //cas : path (string vide), name (String random)
        //creation bd dans bd deja existante (pollution invasive mais fonctionnelle) : true
        try {
            DataBase instance = new DataBase(test_path + File.separator + "testBD/testBD");
            result = true;
        } catch (ExceptionDataBase e) {
        }
        assertEquals(expResult, result);

        expResult = true;
        result = false;
        //cas : path (string vide), name (String random) : false
        //creation bd dans bd deja existante (pollution invasive mais fonctionnelle)
        //mais load prime sur la creation , load sur un domaine inexistant : fail (false)
        try {
            DataBase instance = new DataBase(test_path + File.separator + "testBD/testBD");
            result = true;
        } catch (ExceptionDataBase e) {
        }
        assertEquals(expResult, result);

        expResult = false;
        result = false;
        //cas : path (string foireuse), name (String vide) : false
        //creation d'une base de donne impossible (caracteres imposisbles)
        try {
            DataBase instance = new DataBase(test_path + File.separator + "<>/\\");
            result = true;
        } catch (ExceptionDataBase e) {
        }
        assertEquals(expResult, result);

        //fin de la partie de tentatives de demarrages avec des bds ; 
        //on test avec deux bds existantes et crees

        DataBase instance;
        expResult = true;
        result = false;
        //cas : on load une bd valide : expect true
        try {
            instance = new DataBase(test_path + File.separator + "bd manuel");
            result = true;
        } catch (ExceptionDataBase e) {
        }
        assertEquals(expResult, result);

        DataBase instance1;
        expResult = false;
        result = false;
        //cas : on load une bd qui ne peux pas etre bonne .. on ne s'attend a rien
        //d'autre actuellement qu'un echec pur et simple. -> false

        //detail du test
        //le fichier de configuration contient 2 table :
        //                  - table 1 : avec 3 champs 
        //                  - table 2 : avec 2 champs 
        //les tables implementé : 
        //                  - table 1 ne contient que 2 champs au lieu de 3
        //                  - table 2 n'existe pas
        try {
            instance1 = new DataBase(test_path + File.separator + "bd manuel defectueuse");
            result = true;
        } catch (ExceptionDataBase e) {
        }
        assertEquals(expResult, result);

        //les cas de correpondances des donnees seront vu dans les test :
        // testGetTableByName
    }

    /**
     * Test of save Method, of class Database.
     */
    @Test
    public void testSave() throws Exception {
        System.out.println("Save");



        //le test : on create une nouvelle bd 

        DataBase instance0;
        DataBase instance1;
        int state = 0;
        try {
            instance0 = new DataBase(test_path + File.separator + "new_bd_fail");

            try {
                // on ajoute une table a la bd, on save et on verifie la structure du fichier (sans header)
                TableJSON table_test_new = new TableJSON("table_test_new", test_path + File.separator + "new_bd_fail");
                instance0.addTable(table_test_new);
                instance0.save();
                //on ajoute des elements dedans
                //on try de retrouver les elements
                fail("No exception catched on bad function call @001");
            } catch (ExceptionDataBase e) {
                assertEquals(true, true);
            }

            instance1 = new DataBase(test_path + File.separator + "new_bd_success");

            try {
                TableJSON table_test_new = new TableJSON("table_test_new", test_path + File.separator + "new_bd_success");
                Header test_head = new Header();
                test_head.addColumn("word1", new Properties(Type.STRING));
                test_head.addColumn("word2", new Properties(Type.STRING));
                test_head.addColumn("word3", new Properties(Type.STRING));
                table_test_new.setHeader(test_head);
                instance1.addTable(table_test_new);
                instance1.save();
                assertEquals(true, true);
            } catch (ExceptionDataBase e) {
                fail("Exception catched [ " + e + " ]");
            }
        } catch (ExceptionDataBase e) {
            fail("Exception catched [ " + e + " ]");
        }

    }

    /**
     * Test of addTable method, of class DataBase.
     */
    @Test
    public void testAddTable() throws Exception {
        System.out.println("Add Table");


        DataBase instance0;
        DataBase instance1;
        int state = 0;
        try {
            instance0 = new DataBase(test_path + File.separator + "new_bd_fail");

            try {
                TableJSON table_test_new = new TableJSON("table_test_new", test_path + File.separator + "new_bd_fail");
                instance0.addTable(table_test_new);
                try {
                    InterfaceTable essai = instance0.getTableByName("table_test_new");
                    Row stringline = new Row();
                    stringline.addColumn("word1", "dtc");
                    stringline.addColumn("word2", "essaiencore");
                    stringline.addColumn("word3", "remetdixballes");
                    essai.addRow(stringline);
                    fail("Test 001 should not pass.");
                } catch (ExceptionDataBase e) {
                    assertEquals(true, true);
                }
                //tentative d'acces a une table qui ne possede pas de header ... Possible ? actuellement oui.
                instance0.getTableByName("table_test_new");
                assertEquals(true, true);
            } catch (ExceptionDataBase e) {
                fail("Exception catched [ " + e + " ]");
            }

            instance1 = new DataBase(test_path + File.separator + "new_bd_success");

            try {
                TableJSON table_test_new = new TableJSON("table_test_new", test_path + File.separator + "new_bd_success");

                Header test_head = new Header();
                test_head.addColumn("word1", new Properties(Type.STRING));
                test_head.addColumn("word2", new Properties(Type.STRING));
                test_head.addColumn("word3", new Properties(Type.STRING));
                table_test_new.setHeader(test_head);

                instance1.addTable(table_test_new);
                InterfaceTable essai = instance1.getTableByName("table_test_new");

                Row stringline = new Row();
                stringline.addColumn("word1", "dtc");
                stringline.addColumn("word2", "essaiencore");
                stringline.addColumn("word3", "remetdixballes");
                essai.addRow(stringline);

                ArrayList<Row> maniac = essai.getRows("word1", "dtc");
                assertEquals(maniac.size(), 1);
                assertEquals(maniac.get(0).get("world1"), stringline.get("world1"));

                assertEquals(true, true);

            } catch (ExceptionDataBase e) {
                fail("Exception catched [ " + e + " ]");
            }
        } catch (ExceptionDataBase e) {
            fail("Exception catched [ " + e + " ]");
        }
    }

    /**
     * Test of getTableByName method, of class DataBase.
     */
    @Test
    public void testGetTableByName() throws Exception {
        System.out.println("TestGetTableByName");
        

        DataBase instance;
        boolean expResult = true;
        boolean result = false;
        int state = 0;

        //IMPORTANT du a l'importance cruciale d'un chargement correct de la bd pour tester cette methode,
        //le constructor de database est placé dans un try, et les test sont ensuites chainés dans ce meme try
        //de ce fait, pour specifier quel test a planté, la variable state indique la derniere etape reussie.
        //

        //cas : on load une bd valide : expect true
        try {
            instance = new DataBase(test_path + File.separator + "bd manuel");
            //on teste que la ligne contenant moumoute existe bien
            ArrayList alphatest;

            try {
                alphatest = instance.getTableByName("table_test").getRows("name", "moumoute");
                assertEquals(alphatest.size(), 1);
            } catch (ExceptionDataBase e) {
                fail("erreur : on intance.getTableByName(\"table_test\").getRows(\"name\",\"moumoute\")");
            }
            state = 1;

            //on teste que la ligne "sadoitplanter" ne puisse etre lue
//            try {
//                instance.getTableByName("table_test").getRows("name", "sadoitplanter");
//                assertEquals(true, true);
//            } catch (ExceptionDataBase e) {
//                fail("erreur impossible que la ligne sois valide : conflit de type entre age et sa value\n"+e);
//            }
//            state = 2;

            //on teste que la ligne sadoitmarcher marche ... evidement.
            try {
                alphatest = instance.getTableByName("table_test").getRows("name", "sadoitmarcher");
                assertEquals(alphatest.size(), 1);
            } catch (ExceptionDataBase e) {
                fail("erreur : on intance.getTableByName(\"table_test\").getRows(\"name\",\"sadoitmarcher\")\n" + e);
            }
            state = 3;

            //on teste l'acces a une ligne qui manifestement n'existe pas.
            try {
                alphatest = instance.getTableByName("table_test").getRows("name", "ligneinexistante");
                assertEquals(alphatest.size(), 0);
            } catch (ExceptionDataBase e) {
                fail("erreur : on intance.getTableByName(\"table_test\").getRows(\"name\",\"inexistante\")\n" + e);
            }
            state = 4;

            //on teste l'acces a une key qui n'existe pas. ?? exception ou pas ??
            try {
                alphatest = instance.getTableByName("table_test").getRows("keyquiexistepas", "banzai");
                assertEquals(alphatest.size(), 0);
            } catch (ExceptionDataBase e) {
                fail("erreur : on intance.getTableByName(\"table_test\").getRows(\"keyquiexistepas\",\"banzai\")\n" + e);
            }
            state = 5;
            result = true;
        } catch (ExceptionDataBase e) {
            fail("Exception bd_manual_test @[ " + state + " ] -> " + e);
        }
        assertEquals(expResult, result);
    }
}