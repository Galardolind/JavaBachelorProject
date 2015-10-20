/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Database.CoreClasses;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
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
public class TableJSONTest {

    public TableJSONTest() {
    }
    static String path = System.getProperty("user.dir") + File.separator + "zone_test" + File.separator;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    static public void delete(String path) {
        File f = new File(path);
        for (File fs : f.listFiles()) {
            if (fs.isDirectory()) {
                delete(fs.toString());
            }
            fs.delete();
        }
        f.delete();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        String path = System.getProperty("user.dir") + File.separator + "zone_test";
        File test_path = new File(path);
        delete(test_path.toString());
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of load method, of class TableJSON.
     */
    @Test
    public void testLoad() throws Exception {
        System.out.println("load");
        String tableName = "word";
        System.out.println("path :  " + System.getProperty("user.dir"));
        System.out.println("tableName :  " + tableName);

        TableJSON instance0 = new TableJSON("dqdqsdqsdqsd", path);
        Header head = new Header();
        head.addColumn(tableName, new Properties(Type.STRING));
        // load un fichier qui n'existe 

        try {
            instance0.load(head);
        } catch (ExceptionDataBase e) {
            fail(e.toString());
        }

        TableJSON instance01 = new TableJSON("", path);
        boolean expResult = false;
        boolean result = true;
        // load un fichier qui n'existe pas retourne false

        try {
            instance01.load(head);
        } catch (ExceptionDataBase e) {
            fail(e.toString());
        }

        TableJSON instance1 = new TableJSON("dqdqsdqsdqsd", path);

        // load un fichier qui n'existe pas a un chemin correct retourne false

        try {
            instance1.load(head);
        } catch (ExceptionDataBase e) {
            fail(e.toString());
        }

        TableJSON instance2 = new TableJSON(tableName, path);

        String testPath = path + File.separator + tableName + ".txt";
//        System.out.println(testPath);
        File file = new File(testPath);
        file.delete();

        // load un fichier qui n'existe pas encore a un chemin correct retourne false

        try {
            instance2.load(head);
        } catch (ExceptionDataBase e) {
            fail(e.toString());
        }

        // on cree le fichier
        Row line = new Row();
        line.addColumn(tableName, "test");

        instance2.addRow(line);
        instance2.save();

        // load la DB devrait marcher
        expResult = true;
        result = false;
        try {
            instance2.load(head);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        Row line1 = new Row();
        line1.addColumn(tableName, "test32");
        Row line2 = new Row();
        line2.addColumn(tableName, "test3");
        Row line3 = new Row();
        line3.addColumn(tableName, "test2");
        instance2.addRow(line1);
        instance2.addRow(line2);
        instance2.addRow(line3);
        instance2.save();

        // load la DB devrait marcher avec ces 3 nouvelles lignes
        expResult = true;
        result = false;
        try {
            instance2.load(head);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }

        assertEquals(expResult, result);

        TableJSON instance3 = new TableJSON(tableName, path);
        instance3.setHeader(head);
        // une nouvelle DB ne devrait pas contenir ces lignes avant de load
        expResult = false;
        result = instance3.getRows(tableName, "test3").size() == 1;
        assertEquals(expResult, result);

        // une fois load elle devrait contenir la ligne
        instance3.load(head);
        expResult = true;
        result = instance3.getRows(tableName, "test3").size() == 1;
        assertEquals(expResult, result);

        // test sur une nouvelle DB
        TableJSON instance5 = new TableJSON("word2", path);

        // declaration d'une table de 3 colonnes (word1,word2,word3)
        Header head2 = new Header();
        head2.addColumn("word1", new Properties(Type.STRING));
        head2.addColumn("word2", new Properties(Type.STRING));
        head2.addColumn("word3", new Properties(Type.STRING));
        instance5.setHeader(head2);

        // on ajoute une ligne dans cette table
        Row line5 = new Row();
        line5.addColumn("word1", "test32");
        line5.addColumn("word2", "test3");
        line5.addColumn("word3", "test2");
        instance5.addRow(line5);

        Row line6 = new Row();
        line6.addColumn("word1", "test32");
        line6.addColumn("word2", "test33");
        line6.addColumn("word3", "test34");
        instance5.addRow(line6);

        // on sauvegarde
        instance5.save();

        // une fois load elle devrait contenir les lignes
        instance5.load(head2);
        expResult = true;
        result = instance5.getRows("word1", "test32").size() == 2;
        assertEquals(expResult, result);

        // on va essayer de faire planter en loadant des fichier mal formatte
        String bugPath = path + File.separator;//+ "dbug"+".txt";
        String bugPath2 = path + File.separator + "dbug" + ".txt";
        File fileBug = new File(bugPath2);

        FileWriter fw = new FileWriter(fileBug);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write("aa");
        bw.close();
        fw.close();

        TableJSON instanceBug = new TableJSON("dbug", bugPath);
        Header head3 = new Header();
        head3.addColumn("wordBug", new Properties(Type.STRING));

        // load un fichier ne correspondant pas au header retourne false
        expResult = false;
        result = true;
        try {
            instance5.load(head3);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        FileWriter fw2 = new FileWriter(fileBug);
        BufferedWriter bw2 = new BufferedWriter(fw2);

        bw2.write("wordBug");
        bw2.close();
        fw2.close();

        // load un fichier ne correspondant pas au header (mais avec le nom du header) retourne false
        expResult = false;
        result = true;
        try {
            instance5.load(head3);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);


        FileWriter fw3 = new FileWriter(fileBug);
        BufferedWriter bw3 = new BufferedWriter(fw3);

        bw3.write("[{\"wordBug\":}");
        bw3.close();
        fw3.close();

        // load un fichier ne correspondant pas au header retourne false
        expResult = false;
        result = true;
        try {
            instance5.load(head3);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);


        FileWriter fw4 = new FileWriter(fileBug);
        BufferedWriter bw4 = new BufferedWriter(fw4);

        bw4.write("[{\"wordBug\":ezaeaz}");
        bw4.close();
        fw4.close();

        // load un fichier ne correspondant pas au header retourne false
        expResult = false;
        result = true;
        try {
            instance5.load(head3);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);


        FileWriter fw5 = new FileWriter(fileBug);
        BufferedWriter bw5 = new BufferedWriter(fw5);

        bw5.write("[{\"wordBug\":\"\"\"}");
        bw5.close();
        fw5.close();

        // load un fichier ne correspondant pas au header retourne false
        expResult = false;
        result = true;
        try {
            instance5.load(head3);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

    }

    /**
     * Test of save method, of class TableJSON.
     */
    @Test
    public void testSave() throws Exception {
        System.out.println("save");
        String tableName = "word";
        TableJSON instance = new TableJSON(tableName, path);

        // sauvegarder du vide retourne false
        boolean expResult = false;
        boolean result = true;
        try {
            instance.save();
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        Header head = new Header();
        Row line = new Row();
        line.addColumn(tableName, "test");
        head.addColumn(tableName, new Properties(Type.STRING));
        instance.setHeader(head);

        // ajouter une line et sauvegarder retourne true
        expResult = true;
        instance.addRow(line);
        result = false;
        try {
            instance.save();
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // verification de lexistence du fichier physiquement
        String testPath = path + File.separator + tableName + ".txt";
        File filetest1 = new File(testPath);

        expResult = true;
        result = filetest1.exists();
        assertEquals(expResult, result);

        // verification de la présence des données dans le fichier
        String test;
        expResult = true;
        result = false;
        String testComparator = "[{\"index\":\"0\",\"word\":\"test\"}]";
        try {
            FileReader fr = new FileReader(filetest1);
            BufferedReader br = new BufferedReader(fr);
            test = br.readLine();
            result = testComparator.equals(test);
            br.close();
            fr.close();
        } catch (Exception e) {
            result = false;
        }
        assertEquals(expResult, result);

        // delete une line et sauvegarder retourne true
        instance.delRow(tableName, "test");
        expResult = true;
        result = false;
        try {
            instance.save();
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // verification de lexistence du fichier physiquement
        testPath = path + File.separator + tableName + ".txt";
        File filetest2 = new File(testPath);

        expResult = true;
        result = filetest2.exists();
        assertEquals(expResult, result);

        // verification de la présence des données dans le fichier
        test = "";
        expResult = true;
        result = false;
        testComparator = "[]";
        try {
            FileReader fr = new FileReader(filetest2);
            BufferedReader br = new BufferedReader(fr);
            test = br.readLine();
            result = testComparator.equals(test);
            br.close();
            fr.close();
        } catch (Exception e) {
            result = false;
        }
        assertEquals(expResult, result);


        line.clear();
        line.addColumn("word", "test");
        Row line2 = new Row();
        line2.addColumn("word", "test2");
        Row line3 = new Row();
        line3.addColumn("word", "test3");

        // rajoute 3 line et sauvegarde retourne true
        expResult = true;
        instance.addRow(line);
        instance.addRow(line2);
        instance.addRow(line3);
        result = false;
        try {
            instance.save();
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);


        // verification de lexistence du fichier physiquement
        testPath = path + File.separator + tableName + ".txt";
        File filetest3 = new File(testPath);

        expResult = true;
        result = filetest3.exists();
        assertEquals(expResult, result);

        // verification de la présence des données dans le fichier
        test = "";
        expResult = true;
        result = false;
        testComparator = "[{\"index\":\"3\",\"word\":\"test3\"},{\"index\":\"2\",\"word\":\"test2\"},{\"index\":\"1\",\"word\":\"test\"}]";
        try {
            FileReader fr = new FileReader(filetest3);
            BufferedReader br = new BufferedReader(fr);
            test = br.readLine();
            result = testComparator.equals(test);
            br.close();
            fr.close();
        } catch (Exception e) {
            result = false;
        }
        assertEquals(expResult, result);

        // on declare une nouvelle BD a la meme adresse 
        TableJSON instance2 = new TableJSON(tableName, path);

        // on charge la DB en memoire
        instance2.setHeader(head);
        instance2.load(head);

        // normalement elle devrait contenir les anciennes
        expResult = true;
        result = instance2.getRows(tableName, "test").size() == 1;
        assertEquals(expResult, result);
        result = instance2.getRows(tableName, "test2").size() == 1;
        assertEquals(expResult, result);
        result = instance2.getRows(tableName, "test3").size() == 1;
        assertEquals(expResult, result);

        // on declare une nouvelle BD
        TableJSON instance3 = new TableJSON("word3", path);

        line.clear();
        line.addColumn("word", "test");
        line.addColumn("word2", "test2");
        line.addColumn("word3", "test3");

        // nouveau header
        Header head2 = new Header();
        head2.addColumn("word", new Properties(Type.STRING));
        head2.addColumn("word2", new Properties(Type.STRING));
        head2.addColumn("word3", new Properties(Type.STRING));
        instance3.setHeader(head2);

        // rajoute 1 line à 3 column et sauvegarde retourne true
        expResult = true;
        instance3.addRow(line);
        result = false;
        try {
            instance3.save();
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // verification de lexistence du fichier physiquement
        testPath = path + File.separator + "word3" + ".txt";
        File filetest4 = new File(testPath);

        expResult = true;
        result = filetest4.exists();
        assertEquals(expResult, result);

        // verification de la présence des données dans le fichier
        test = "";
        expResult = true;
        result = false;
        testComparator = "[{\"index\":\"0\",\"word2\":\"test2\",\"word3\":\"test3\",\"word\":\"test\"}]";
        try {
            FileReader fr = new FileReader(filetest4);
            BufferedReader br = new BufferedReader(fr);
            test = br.readLine();
            result = testComparator.equals(test);
            br.close();
            fr.close();
        } catch (Exception e) {
            result = false;
        }
        assertEquals(expResult, result);

        // on declare une nouvelle BD
        TableJSON instance4 = new TableJSON("word3", path);

        line.clear();
        line.addColumn("word", "test4");
        line.addColumn("word2", "test5");
        line.addColumn("word3", "test6");

        // nouveau header
        Header head3 = new Header();
        head3.addColumn("word", new Properties(Type.STRING));
        head3.addColumn("word2", new Properties(Type.STRING));
        head3.addColumn("word3", new Properties(Type.STRING));

        // load la db qui contient normalement la ligne à 3 colonnes
        instance4.load(head3);

        // rajoute 1 line à 3 column et sauvegarde retourne true
        expResult = true;
        instance4.addRow(line);
        result = false;
        try {
            instance4.save();
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // verification de lexistence du fichier physiquement
        testPath = path + File.separator + "word3" + ".txt";
        File filetest5 = new File(testPath);

        expResult = true;
        result = filetest5.exists();
        assertEquals(expResult, result);

        // verification de la présence des données dans le fichier
        test = "";
        expResult = true;
        result = false;
        testComparator = "[{\"index\":\"1\",\"word2\":\"test5\",\"word3\":\"test6\",\"word\":\"test4\"},{\"index\":\"0\",\"word2\":\"test2\",\"word3\":\"test3\",\"word\":\"test\"}]";
        try {
            FileReader fr = new FileReader(filetest5);
            BufferedReader br = new BufferedReader(fr);
            test = br.readLine();
            System.out.println(test);
            result = testComparator.equals(test);
            br.close();
            fr.close();
        } catch (Exception e) {
            result = false;
        }
        assertEquals(expResult, result);

    }

    /**
     * Test of setHeader method, of class AbstractTable.
     */
    @Test
    public void testSetHeader() throws Exception {
        System.out.println("setHeader");
        String tableName = "word";
        TableJSON instance = new TableJSON(tableName, path);

        Header head = null;

        // set un header null retourne false
        boolean expResult = false;
        boolean result = true;
        try {
            instance.setHeader(head);
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);


        head = new Header();

        // set un header vide retourne false
        expResult = false;
        result = true;
        try {
            instance.setHeader(head);
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // set un header correctement retourne true
        head.addColumn(tableName, new Properties(Type.STRING));
        expResult = true;
        result = false;
        try {
            instance.setHeader(head);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // set un header par dessus l'ancien retourne false
        expResult = false;
        result = true;
        try {
            instance.setHeader(head);
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // test sur une nouvelle DB
        TableJSON instance2 = new TableJSON("word2", path);

        // declaration d'une table de 3 colonnes (word1,word2,word3)
        Header head2 = new Header();
        head2.addColumn("word1", new Properties(Type.STRING));
        head2.addColumn("word2", new Properties(Type.STRING));
        head2.addColumn("word3", new Properties(Type.STRING));

        // set un header de 3 colonnes retourne true
        expResult = true;
        result = false;
        try {
            instance2.setHeader(head2);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // test sur une nouvelle DB
        TableJSON instance3 = new TableJSON("word2", path);

        // declaration d'une table de 3 colonnes (word1,word2,word3) etant toute l'index
        Header head3 = new Header();
        head3.addColumn("word1", new Properties(Type.STRING, true));
        head3.addColumn("word2", new Properties(Type.STRING, true));
        head3.addColumn("word3", new Properties(Type.STRING, true));

        // set un header de 3 colonnes étant toute l'index retourne false
        expResult = false;
        result = true;
        try {
            instance3.setHeader(head3);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // test sur une nouvelle DB
        TableJSON instance4 = new TableJSON("word2", path);

        // declaration d'une table de 3 colonnes (word1,word2,word3) ayant 2 index
        Header head4 = new Header();
        head4.addColumn("word1", new Properties(Type.STRING, true));
        head4.addColumn("word2", new Properties(Type.STRING, false));
        head4.addColumn("word3", new Properties(Type.STRING, true));

        // set un header de 3 colonnes ayant 2 index retourne false
        expResult = false;
        result = true;
        try {
            instance4.setHeader(head4);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // test sur une nouvelle DB
        TableJSON instance5 = new TableJSON("word2", path);

        // declaration d'une table de 3 colonnes (word1,word2,word3) ayant 1 index
        Header head5 = new Header();
        head5.addColumn("word1", new Properties(Type.STRING, true));
        head5.addColumn("word2", new Properties(Type.STRING));
        head5.addColumn("word3", new Properties(Type.STRING));

        // set un header de 3 colonnes ayant 1 index retourne false
        expResult = true;
        result = false;
        try {
            instance5.setHeader(head5);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

    }

    /**
     * Test of addLine method, of class AbstractTable.
     */
    @Test
    public void testAddLine() throws Exception {
        System.out.println("addLine");
        String tableName = "word";

        TableJSON instance = new TableJSON(tableName, path);
        Header head = new Header();
        head.addColumn(tableName, new Properties(Type.STRING));
        instance.setHeader(head);

        // ajouter une line vide retourne false
        Row line = new Row();
        boolean expResult = false;
        boolean result = true;
        try {
            instance.addRow(line);
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // ajouter une line sur une colonne innexistante retourne false
        line.addColumn("d", "test");
        expResult = false;
        result = true;
        try {
            instance.addRow(line);
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // ajouter une line dans la bonne colonne retourne true
        line.clear();
        line.addColumn("word", "test");
        expResult = true;
        result = false;
        try {
            instance.addRow(line);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // ajoute un element dans la bonne colonne retourne true // MYSTICAL //
        line.clear();
        line.addColumn("word", "test");
        line.addColumn("word", "test2");
        line.addColumn("word", "test3");
        expResult = true;
        result = false;
        try {
            instance.addRow(line);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // ajoute plusieurs elements dans la bonne et mauvaise colonne retourne false
        line.clear();
        line.addColumn("word", "test");
        line.addColumn("word2", "test2");
        line.addColumn("word", "test3");
        expResult = false;
        result = true;
        try {
            instance.addRow(line);
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);


        // declaration d'une nouvelle instance de TableJSONs
        head.clear();
        head.addColumn(tableName, new Properties(Type.INTEGER));
        TableJSON instance2 = new TableJSON(tableName, path);
        instance2.setHeader(head);

        // ajouter une String a une table d'Int retourne false
        line.clear();
        line.addColumn("word", "test");
        expResult = false;
        result = true;
        try {
            instance2.addRow(line);
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // ajouter un Integer a une table d'Int retourne true
        line.clear();
        line.addColumn("word", "13");
        expResult = true;
        result = false;
        try {
            instance2.addRow(line);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // test sur une nouvelle DB
        TableJSON instance5 = new TableJSON("word2", path);

        // declaration d'une table de 3 colonnes (word1,word2,word3)
        Header head2 = new Header();
        head2.addColumn("word1", new Properties(Type.STRING));
        head2.addColumn("word2", new Properties(Type.STRING));
        head2.addColumn("word3", new Properties(Type.STRING));
        instance5.setHeader(head2);

        Row line51 = new Row();

        // une ligne a une colonne (sur tableau a 3 colonne) retourne false
        line51.addColumn("word1", "test1");
        expResult = false;
        result = true;
        try {
            instance5.addRow(line51);
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // une ligne a 2 colonnes (sur tableau a 3) retourne false
        line51.addColumn("word2", "test1");
        expResult = false;
        result = true;
        try {
            instance5.addRow(line51);
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // une ligne a 3 colonnes (sur tableau a 3) retourne true
        line51.addColumn("word3", "test1");
        expResult = true;
        result = false;
        try {
            instance5.addRow(line51);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // une ligne a 4 colonnes (sur tableau a 3) retourne false
        line51.addColumn("word4", "test1");
        expResult = false;
        result = true;
        try {
            instance5.addRow(line51);
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // declaration d'une nouvelle instance
        TableJSON instanceFlood = new TableJSON("flood", path);

        // declaration d'une table de 1 colonne 
        Header headFlood = new Header();
        headFlood.addColumn("flooder", new Properties(Type.STRING, true));
        instanceFlood.setHeader(headFlood);

        // ajoute 100 ligne dans la table
        expResult = true;
        result = false;
        Row lineFlood = new Row();
        try {
            for (int i = 0; i < 100; i++) {
                lineFlood.clear();
                lineFlood.addColumn("flooder", ("test n :" + i));
                instanceFlood.addRow(lineFlood);
            }
            instanceFlood.save();
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // ajoute les 100 meme lignes dans la table
        expResult = true;
        result = false;
        try {
            for (int i = 0; i < 100; i++) {
                lineFlood.clear();
                lineFlood.addColumn("flooder", ("test n :" + i));
                instanceFlood.addRow(lineFlood);
            }
            instanceFlood.save();
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // il doit y avoir 100 ligne dans la db dans tous les cas 
        expResult = true;
        result = instanceFlood.rows.size() == 100;
        assertEquals(expResult, result);

        // declaration d'une nouvelle instance
        TableJSON instanceFlood2 = new TableJSON("flood", path);

        // declaration d'une table de 1 colonne 
        Header headFlood2 = new Header();
        headFlood2.addColumn("flooder", new Properties(Type.STRING));
        headFlood2.addColumn("floo", new Properties(Type.STRING, true));
        headFlood2.addColumn("der", new Properties(Type.STRING, false));
        instanceFlood2.setHeader(headFlood2);

        // ajoute 100 ligne dans la table
        expResult = true;
        result = false;
        Row lineFlood2 = new Row();
        try {
            for (int i = 0; i < 100; i++) {
                lineFlood2.clear();
                lineFlood2.addColumn("flooder", ("testflooder n :" + i));
                lineFlood2.addColumn("floo", ("testfloo n :" + i));
                lineFlood2.addColumn("der", ("testder n :" + i));
                instanceFlood2.addRow(lineFlood2);
            }
            instanceFlood2.save();
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // il doit y avoir 100 ligne dans la db dans tous les cas 
        expResult = true;
        result = instanceFlood2.rows.size() == 100;
        assertEquals(expResult, result);

        // ajoute 100 meme ligne indexe et les autres differentes dans la table
        expResult = true;
        result = false;
        try {
            for (int i = 0; i < 100; i++) {
                lineFlood2.clear();
                lineFlood2.addColumn("flooder", ("flooder n :" + i));
                lineFlood2.addColumn("floo", ("testfloo n :" + i));
                lineFlood2.addColumn("der", ("der n :" + i));
                instanceFlood2.addRow(lineFlood2);
            }
            instanceFlood2.save();
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // il doit toujours y avoir 100 ligne dans la db dans tous les cas 
        expResult = true;
        result = instanceFlood2.rows.size() == 100;
        assertEquals(expResult, result);

        // ajoute 100 lignes indexe different et les autres sont les meme
        expResult = true;
        result = false;
        try {
            for (int i = 0; i < 100; i++) {
                lineFlood2.clear();
                lineFlood2.addColumn("flooder", ("testflooder n :" + i));
                lineFlood2.addColumn("floo", ("floo n :" + i));
                lineFlood2.addColumn("der", ("testder n :" + i));
                instanceFlood2.addRow(lineFlood2);
            }
            instanceFlood2.save();
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // il doit toujours y avoir 100 ligne dans la db dans tous les cas 
        expResult = true;
        result = instanceFlood2.rows.size() == 200;
        assertEquals(expResult, result);

        // ajoute 100 lignes et ont fait planter la db
        expResult = false;
        result = true;
        try {
            for (int i = 0; i < 100; i++) {
                lineFlood2.clear();
                lineFlood2.addColumn("flooder", ("testplantage n :" + i));
                lineFlood2.addColumn("floo", ("plan n :" + i));
                lineFlood2.addColumn("der", ("tage n :" + i));
                instanceFlood2.addRow(lineFlood2);
            }
            // on ajoute la ligne qui fait planter la db
            lineFlood2.clear();
            lineFlood2.addColumn("azeazeaz", "plante!");
            instanceFlood2.addRow(lineFlood2);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // declaration d'une nouvelle instance a la meme adresse
        TableJSON instanceFloodPlantage = new TableJSON("flood", path);

        // avant de planter la DB devrait sauvegarder
        instanceFloodPlantage.load(headFlood2);
        expResult = true;
        result = instanceFloodPlantage.rows.size() == 300;
        assertEquals(expResult, result);
    }

    /**
     * Test of delLine method, of class AbstractTable.
     */
    @Test
    public void testDelLine() throws Exception {
        System.out.println("delLine");
        String tableName = "word";

        TableJSON instance = new TableJSON(tableName, path);
        Header head = new Header();
        head.addColumn(tableName, new Properties(Type.STRING));
        instance.setHeader(head);


        Row line = new Row();
        // ajouter une line dans la bonne colonne retourne true
        line.addColumn("word", "test");
        boolean expResult = true;
        boolean result = false;
        try {
            instance.addRow(line);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // ajoute une nouvelle line dans la bonne colonne
        line.addColumn("word", "test2");
        expResult = true;
        result = false;
        try {
            instance.addRow(line);
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // delete une ligne innexistante retourne false
        expResult = false;
        result = true;
        try {
            instance.delRow("", "");
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // delete la ligne dans la table mais qui n'existe pas 

        try {
            instance.delRow("word", "zezdzdaz");
        } catch (ExceptionDataBase e) {
            fail(e.toString());
        }

        // delete la ligne precedemment ajouter retourne true
        expResult = true;
        result = false;
        try {
            instance.delRow("word", "test");
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // delete la deuxieme ligne precedemment ajouter retourne true
        expResult = true;
        result = false;
        try {
            instance.delRow("word", "test2");
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // test sur une nouvelle DB
        TableJSON instance5 = new TableJSON("word2", path);

        // declaration d'une table de 3 colonnes (word1,word2,word3)
        Header head2 = new Header();
        head2.addColumn("word1", new Properties(Type.STRING));
        head2.addColumn("word2", new Properties(Type.STRING));
        head2.addColumn("word3", new Properties(Type.STRING));
        instance5.setHeader(head2);

        // on rajoute 2 lignes dans la DB
        Row line51 = new Row();
        line51.addColumn("word1", "test1");
        line51.addColumn("word2", "test2");
        line51.addColumn("word3", "test3");
        instance5.addRow(line51);

        Row line52 = new Row();
        line52.addColumn("word1", "test4");
        line52.addColumn("word2", "test5");
        line52.addColumn("word3", "test6");
        instance5.addRow(line52);

        // la DB devrait contenir la ligne test4 retourne true
        expResult = true;
        result = instance5.getRows("word1", "test4").size() == 1;
        assertEquals(expResult, result);

        // delete la ligne test4 retourne true
        expResult = true;
        result = false;
        try {
            instance5.delRow("word1", "test4");
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // la DB ne devrait plus contenir la ligne test4 retourne true
        expResult = true;
        result = instance5.getRows("word1", "test4").isEmpty();
        assertEquals(expResult, result);

        // la DB ne devrait plus contenir la ligne test5 retourne true
        expResult = true;
        result = instance5.getRows("word2", "test5").isEmpty();
        assertEquals(expResult, result);

        // la DB ne devrait plus contenir la ligne test6 retourne true
        expResult = true;
        result = instance5.getRows("word3", "test6").isEmpty();
        assertEquals(expResult, result);

        // la DB devrait toukours contenir la ligne test1 retourne true
        expResult = true;
        result = instance5.getRows("word1", "test1").size() == 1;
        assertEquals(expResult, result);


        // declaration d'une nouvelle instance
        TableJSON instanceFlood = new TableJSON("flood2", path);

        // declaration d'une table de 1 colonne étant l'index
        Header headFlood = new Header();
        headFlood.addColumn("flooder", new Properties(Type.STRING, true));
        instanceFlood.setHeader(headFlood);

        // ajoute 1000 ligne dans la table
        expResult = true;
        result = false;
        Row lineFlood = new Row();
        try {
            for (int i = 0; i < 1000; i++) {
                lineFlood.clear();
                lineFlood.addColumn("flooder", ("test n :" + i));
                instanceFlood.addRow(lineFlood);
            }
            instanceFlood.save();
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);


        // delete 400 ligne dans la db
        expResult = true;
        result = false;
        try {
            for (int i = 0; i < 400; i++) {
                instanceFlood.delRow("flooder", ("test n :" + (i + 100)));
            }
            instanceFlood.save();
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // il doit bien rester 600 lignes
        expResult = true;
        result = instanceFlood.rows.size() == 600;
        assertEquals(expResult, result);

        // il ne contient donc plus les valeurs
        for (int i = 0; i < 400; i++) {
            expResult = false;
            result = instanceFlood.rows.containsValue("test n :" + i + 100);
            assertEquals(expResult, result);
        }
        instanceFlood.save();

        // declaration d'une nouvelle instance au meme endroit que l'ancienne
        TableJSON instanceFlood2 = new TableJSON("flood2", path);

        // declaration de la table a charger
        Header headFlood2 = new Header();
        headFlood2.addColumn("flooder", new Properties(Type.STRING, true));

        instanceFlood2.load(headFlood2);

        // il doit bien rester 600 lignes
        expResult = true;
        result = instanceFlood2.rows.size() == 600;
        assertEquals(expResult, result);

        // il ne contient donc plus les valeurs
        for (int i = 0; i < 400; i++) {
            expResult = false;
            result = instanceFlood2.rows.containsValue("test n :" + i + 100);
            assertEquals(expResult, result);
        }

        // declaration d'une nouvelle instance
        TableJSON instanceFlood3 = new TableJSON("flood", path);

        // declaration d'une table de 1 colonne 
        Header headFlood3 = new Header();
        headFlood3.addColumn("flooder", new Properties(Type.STRING));
        headFlood3.addColumn("floo", new Properties(Type.STRING, true));
        headFlood3.addColumn("der", new Properties(Type.STRING, false));
        instanceFlood3.setHeader(headFlood3);

        // ajoute 200 ligne dans la table
        expResult = true;
        result = false;
        Row lineFlood2 = new Row();
        try {
            for (int i = 0; i < 100; i++) {
                lineFlood2.clear();
                lineFlood2.addColumn("flooder", ("testflooder n :" + i));
                lineFlood2.addColumn("floo", ("testfloo n :" + i));
                lineFlood2.addColumn("der", "test");
                instanceFlood3.addRow(lineFlood2);
            }
            for (int i = 0; i < 100; i++) {
                lineFlood2.clear();
                lineFlood2.addColumn("flooder", ("test"));
                lineFlood2.addColumn("floo", ("testfloo n :" + i + 100));
                lineFlood2.addColumn("der", ("test n :" + i));
                instanceFlood3.addRow(lineFlood2);
            }
            instanceFlood3.save();
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // il doit y avoir 200 ligne dans la db dans tous les cas 
        expResult = true;
        result = instanceFlood3.rows.size() == 200;
        assertEquals(expResult, result);

        // je supprime les ligne contenant test
        instanceFlood3.delRow("der", "test");

        // il doit y avoir 100 ligne dans la db dans tous les cas 
        expResult = true;
        result = instanceFlood3.rows.size() == 100;
        assertEquals(expResult, result);

        // normalement la colonne ne contient plus de test
        expResult = true;
        result = instanceFlood3.getRows("der", "test").isEmpty();
        assertEquals(expResult, result);

        // normalement la colonne ne contient plus de test
        expResult = true;
        result = !instanceFlood3.getRows("flooder", "test").isEmpty();
        assertEquals(expResult, result);

    }

    /*
     * Test of DropTable method, of class AbstractTable.
     */
    @Test
    public void testDropTable() throws Exception {
        System.out.println("DropTable");

        // declaration d'une nouvelle instance
        TableJSON instanceFlood3 = new TableJSON("dropT", path);

        // declaration d'une table de 1 colonne 
        Header headFlood3 = new Header();
        headFlood3.addColumn("flooder", new Properties(Type.STRING));
        headFlood3.addColumn("floo", new Properties(Type.STRING, true));
        headFlood3.addColumn("der", new Properties(Type.STRING, false));
        instanceFlood3.setHeader(headFlood3);

        // ajoute 200 ligne dans la table
        boolean expResult = true;
        boolean result = false;
        Row lineFlood2 = new Row();
        try {
            for (int i = 0; i < 100; i++) {
                lineFlood2.clear();
                lineFlood2.addColumn("flooder", ("testflooder n :" + i));
                lineFlood2.addColumn("floo", ("testfloo n :" + i));
                lineFlood2.addColumn("der", "test");
                instanceFlood3.addRow(lineFlood2);
            }
            for (int i = 0; i < 100; i++) {
                lineFlood2.clear();
                lineFlood2.addColumn("flooder", ("test"));
                lineFlood2.addColumn("floo", ("testfloo n :" + i + 100));
                lineFlood2.addColumn("der", ("test n :" + i));
                instanceFlood3.addRow(lineFlood2);
            }
            instanceFlood3.save();
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // il doit y avoir 200 ligne dans la db dans tous les cas 
        expResult = true;
        result = instanceFlood3.rows.size() == 200;
        assertEquals(expResult, result);

        // je delete la table
        instanceFlood3.dropTable();

        // declaration d'une nouvelle instance a la meme adresse
        TableJSON instanceFlood4 = new TableJSON("dropT", path);
        instanceFlood4.load(headFlood3);

        // elle devrait etre vide
        expResult = true;
        result = instanceFlood4.rows.isEmpty();
        assertEquals(expResult, result);

    }

    /*
     * Test of getAll method, of class AbstractTable.
     */
    @Test
    public void testgetAll() throws Exception {
        System.out.println("getAll");


        // declaration d'une nouvelle instance
        TableJSON instanceFlood3 = new TableJSON("getAll", path);

        // declaration d'une table de 1 colonne 
        Header headFlood3 = new Header();
        headFlood3.addColumn("flooder", new Properties(Type.STRING));
        headFlood3.addColumn("floo", new Properties(Type.STRING, true));
        headFlood3.addColumn("der", new Properties(Type.STRING, false));
        instanceFlood3.setHeader(headFlood3);

        // ajoute 200 ligne dans la table
        boolean expResult = true;
        boolean result = false;
        Row lineFlood2 = new Row();
        try {
            for (int i = 0; i < 100; i++) {
                lineFlood2.clear();
                lineFlood2.addColumn("flooder", ("testflooder n :" + i));
                lineFlood2.addColumn("floo", ("testfloo n :" + i));
                lineFlood2.addColumn("der", "test");
                instanceFlood3.addRow(lineFlood2);
            }
            for (int i = 0; i < 100; i++) {
                lineFlood2.clear();
                lineFlood2.addColumn("flooder", ("test"));
                lineFlood2.addColumn("floo", ("testfloo n :" + i + 100));
                lineFlood2.addColumn("der", ("test n :" + i));
                instanceFlood3.addRow(lineFlood2);
            }
            instanceFlood3.save();
            result = true;
        } catch (ExceptionDataBase e) {
            result = false;
        }
        assertEquals(expResult, result);

        // il doit y avoir 200 ligne dans la db dans tous les cas 
        expResult = true;
        result = instanceFlood3.rows.size() == 200;
        assertEquals(expResult, result);

        ArrayList<Row> arr = new ArrayList<Row>();
        arr = instanceFlood3.getAll();

        // il doit y avoir le meme nombre de ligne entre les deux
        expResult = true;
        result = arr.size() == instanceFlood3.rows.size();
        assertEquals(expResult, result);

    }

    /**
     * Test of getRowByIndex method, of class AbstractTable.
     */
    @Test
    public void testgetRowByIndex() throws Exception {
        System.out.println("getLineByIndex");
        String tableName = "wordRowByIndex";

        TableJSON instance = new TableJSON(tableName, path);
        Header head = new Header();
        head.addColumn("word", new Properties(Type.STRING, true));
        instance.setHeader(head);

        Row line = new Row();
        Row line2 = new Row();
        Row line3 = new Row();
        // ajouter trois line 
        line.addColumn("word", "test");
        instance.addRow(line);

        line2.addColumn("word", "test2");
        instance.addRow(line2);

        line3.addColumn("word", "test3");
        instance.addRow(line3);

        // recuperer la premiere retourne true
        boolean expResult = true;
        boolean result = instance.getRowByIndex("test").containsValue("test");
        assertEquals(expResult, result);

        // recuperer une innexistante retourne false
        expResult = false;
        result = instance.getRowByIndex("tet") != null;
        assertEquals(expResult, result);

        // recuperer la deuxieme retourne true
        expResult = true;
        result = instance.getRowByIndex("test2").containsValue("test2");
        assertEquals(expResult, result);

    }

    /**
     * Test of getRows method, of class AbstractTable.
     */
    @Test
    public void testgetLines() throws Exception {
        System.out.println("getLines");
        String tableName = "word";

        TableJSON instance = new TableJSON(tableName, path);
        Header head = new Header();
        head.addColumn(tableName, new Properties(Type.STRING));
        instance.setHeader(head);

        Row line = new Row();
        Row line2 = new Row();
        Row line3 = new Row();
        Row line4 = new Row();
        // ajouter quatre line 
        line.addColumn("word", "test");
        instance.addRow(line);

        line2.addColumn("word", "test2");
        instance.addRow(line2);

        line3.addColumn("word", "test3");
        instance.addRow(line3);

        line4.addColumn("word", "test");
        instance.addRow(line4);

        // recupere les lines contenant test retourne true
        boolean expResult = true;
        boolean result = instance.getRows("word", "test").size() == 2;
        assertEquals(expResult, result);

        // recuperer une innexistante retourne true
        expResult = true;
        int test = instance.getRows("word", "tet").size();
        result = test == 0;
        assertEquals(expResult, result);

        // recuperer une innexistante retourne true
        expResult = true;
        result = instance.getRows("rd", "tet").size() == 0;
        assertEquals(expResult, result);

        // recuperer la deuxieme retourne true
        expResult = true;
        result = instance.getRows("word", "test2").size() == 1;
        assertEquals(expResult, result);

        // test sur une nouvelle DB
        TableJSON instance5 = new TableJSON("word2", path);

        // declaration d'une table de 3 colonnes (word1,word2,word3)
        Header head2 = new Header();
        head2.addColumn("word1", new Properties(Type.STRING));
        head2.addColumn("word2", new Properties(Type.STRING));
        head2.addColumn("word3", new Properties(Type.STRING));
        instance5.setHeader(head2);

        // on rajoute 2 lignes dans la DB
        Row line51 = new Row();
        line51.addColumn("word1", "test1");
        line51.addColumn("word2", "test2");
        line51.addColumn("word3", "test3");
        instance5.addRow(line51);

        Row line52 = new Row();
        line52.addColumn("word1", "test4");
        line52.addColumn("word2", "test5");
        line52.addColumn("word3", "test6");
        instance5.addRow(line52);

        // recuperer la premiere retourne true
        expResult = true;
        result = instance5.getRows("word1", "test1").size() == 1;
        assertEquals(expResult, result);

        // recuperer la deuxieme retourne true
        expResult = true;
        result = instance5.getRows("word1", "test4").size() == 1;
        assertEquals(expResult, result);

        // recuperer la premiere a partir de la deuxieme colonne retourne true
        expResult = true;
        result = instance5.getRows("word2", "test2").size() == 1;
        assertEquals(expResult, result);

        // recuperer la premiere a partir de la deuxieme colonne retourne true
        expResult = true;
        result = instance5.getRows("word2", "test5").size() == 1;
        assertEquals(expResult, result);

        // recuperer la premiere a partir de la troisieme colonne retourne true
        expResult = true;
        result = instance5.getRows("word3", "test3").size() == 1;
        assertEquals(expResult, result);

        // recuperer la premiere a partir de la troisieme colonne retourne true
        expResult = true;
        result = instance5.getRows("word3", "test6").size() == 1;
        assertEquals(expResult, result);

        // recuperer une valeur innexistante dans une colonne retourne true
        expResult = true;
        result = instance5.getRows("word3", "test6azdadaz").size() == 0;
        assertEquals(expResult, result);

        // on ajoute une 3eme ligne
        Row line53 = new Row();
        line53.addColumn("word1", "test4");
        line53.addColumn("word2", "test5");
        line53.addColumn("word3", "test6");
        instance5.addRow(line53);

        // recuperer test1 devrait retourner 2 lines retourne true
        expResult = true;
        result = instance5.getRows("word1", "test4").size() == 2;
        assertEquals(expResult, result);
    }
}