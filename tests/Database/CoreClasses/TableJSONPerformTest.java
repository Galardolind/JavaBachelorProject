/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Database.CoreClasses;

import org.junit.After;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import java.io.IOException;
import java.util.Random;
import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.*;

import com.mongodb.*;
import java.io.File;
import java.net.UnknownHostException;

/**
 *
 * @author romain
 */
public class TableJSONPerformTest extends AbstractBenchmark {

    public TableJSONPerformTest() {
    }
    
    static String path = System.getProperty("user.dir") + File.separator + "zone_test" + File.separator;
    
    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnknownHostException, ExceptionDataBase, IOException {
        mongoClient = new MongoClient();
        db = mongoClient.getDB("test");
        collection = db.createCollection("TableTest", null);
//        Class.forName("com.mysql.jdbc.Driver").newInstance();
//        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/");
//        connection.createStatement().execute("CREATE DATABASE PerformTest");
        instance = new TableJSON("TableTest", path);

        //        connection.createStatement().execute("CREATE TABLE TableTest "
//                + "\"index\" AUTO_INCREMENT PRIMARY KEY, "
//                + "\"0\" TEXT NOT NULL,"
//                + "\"1\" TEXT NOT NULL,"
//                + "\"2\" TEXT NOT NULL;");

        Header h = new Header();
        Properties p = new Properties(Type.STRING);
        h.addColumn("0", new Properties(Type.STRING, true));
        h.addColumn("1", p);
        h.addColumn("2", p);
        instance.setHeader(h);
        for (int i = 0; i < 300000; i++) {
            Row r = new Row();
            BasicDBObject doc = new BasicDBObject();
            r.addColumn(String.valueOf(i % 3), String.valueOf(i));
            doc.append(String.valueOf(i % 3), String.valueOf(i));
            i++;
            r.addColumn(String.valueOf(i % 3), String.valueOf(i));
            doc.append(String.valueOf(i % 3), String.valueOf(i));
            i++;
            r.addColumn(String.valueOf(i % 3), String.valueOf(i));
            doc.append(String.valueOf(i % 3), String.valueOf(i));
//            connection.createStatement().execute("INSERT PerformTest SET "
//                    + "\"" + ((i - 2) % 3) + "\"=\"" + (i - 2) + "\","
//                    + "\"" + ((i - 1) % 3) + "\"=\"" + (i - 1) + "\","
//                    + "\"" + (i % 3) + "\"=\"" + i + "\"");
            //collection.insert(doc);
            instance.addRow(r);
        }
        System.out.println("\nTaille : " + instance.rows.size());
        instance.save();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
//        connection.createStatement().execute("DROP DATABASE PerformTest");
        mongoClient.dropDatabase("test");
    }

    @Before
    public void setUp() throws ExceptionDataBase, IOException, SQLException {
    }

    @After
    public void tearDown() throws SQLException {
//        connection.createStatement().execute("DROP TABLE TableTest");
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    private static DBCollection collection;
    private static DB db;
    private static MongoClient mongoClient;
    private static TableJSON instance;
    private static Connection connection;


    @Test
    public void testAddMillionAndSave() throws ExceptionDataBase, IOException {

        System.out.println("AddMillionAndSave");
        TableJSON instance2 = new TableJSON("TableTest1", path);
        Header h = new Header();
        Properties p = new Properties(Type.STRING);
        h.addColumn("0", new Properties(Type.STRING, true));
        h.addColumn("1", p);
        h.addColumn("2", p);
        instance2.setHeader(h);
        for (int i = 0; i < 300000; i++) {
            Row r = new Row();
            r.addColumn(String.valueOf(i % 3), String.valueOf(i));
            i++;
            r.addColumn(String.valueOf(i % 3), String.valueOf(i));
            i++;
            r.addColumn(String.valueOf(i % 3), String.valueOf(i));
            instance2.addRow(r);
        }
        instance2.save();

    }
    
    
    @Test
    public void testDeleteThousandElementInMillionRowAndSave() throws IOException, JSONException, ExceptionDataBase {
        System.out.println("DeleteThousandElementInMillionRowAndSave");

        TableJSON test = new TableJSON("TableTest", path);
        Header head = new Header();
        Properties prop = new Properties(Type.STRING);
        head.addColumn("0", new Properties(Type.STRING, true));
        head.addColumn("1", prop);
        head.addColumn("2", prop);
        test.load(head);
        for (int i = 0; i < 1000; i++) {
            Random rand = new Random();
            int nextInt = rand.nextInt(100000);
            //System.out.println("a delete : " + nextInt);
           
                test.delRow(""+(nextInt % 3), ""+nextInt);

        }

       // test.save();

    }

    @Ignore
    @Test
    public void AddMillionElementsSQL() throws SQLException {
        connection.createStatement().execute("CREATE TABLE TableTest "
                + "\"index\" AUTO_INCREMENT PRIMARY KEY, "
                + "\"0\" TEXT NOT NULL,"
                + "\"1\" TEXT NOT NULL,"
                + "\"2\" TEXT NOT NULL;");

        for (int i = 0; i < 1000000; i++) {
            i++;
            i++;
            connection.createStatement().execute("INSERT PerformTest SET "
                    + "\"" + ((i - 2) % 3) + "\"=\"" + (i - 2) + "\","
                    + "\"" + ((i - 1) % 3) + "\"=\"" + (i - 1) + "\","
                    + "\"" + (i % 3) + "\"=\"" + i + "\"");
        }
    }

 
    @Test
    public void testAddMillionAndSaveMongoDB() throws ExceptionDataBase, IOException {

        System.out.println("AddMillionAndSaveMongoDB");
        DBCollection coll = db.createCollection("TableTest", null);
        for (int i = 0; i < 300000; i++) {
            BasicDBObject doc = new BasicDBObject();
            doc.append("index", i);
            doc.append(String.valueOf(i % 3), String.valueOf(i));
            i++;
            doc.append(String.valueOf(i % 3), String.valueOf(i));
            i++;
            doc.append(String.valueOf(i % 3), String.valueOf(i));
            coll.insert(doc);
        }

    }

    
    @Test
    public void testDeleteThousandElementInMillionRowAndSaveMangoDB() throws IOException, JSONException, ExceptionDataBase {
        System.out.println("DeleteThousandElementInMillionRowAndSaveMangoDB");

        for (int i = 0; i < 1000; i++) {
            Random rand = new Random();

            int nextInt = rand.nextInt(100000);
            
            BasicDBObject doc = new BasicDBObject(String.valueOf(nextInt % 3), String.valueOf(nextInt));
            collection.dropIndex(doc);

        }

    }
}
