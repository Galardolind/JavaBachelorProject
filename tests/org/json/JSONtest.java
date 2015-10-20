/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author romain
 */
public class JSONtest {

    public JSONtest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testWriter() throws IOException {
        System.out.println("ecriture dans un fichier json");

        File tmp = File.createTempFile("writer", null);
        BufferedReader br = new BufferedReader(new FileReader(tmp));

        FileWriter fw = new FileWriter(tmp);
        JSONWriter writer = new JSONWriter(fw);

        writer.array();
        writer.object().key("colonne1").value("valeur1").endObject();
        writer.object().key("colonne2").value("valeur2").endObject();
        writer.object().key("colonne3").value("valeur3").endObject();
        writer.object().key("colonne4").value("valeur4").endObject();
        writer.endArray();



        fw.close();

        String ret = br.readLine();

        assertEquals(ret, "[{\"colonne1\":\"valeur1\"},{\"colonne2\":\"valeur2\"},{\"colonne3\":\"valeur3\"},{\"colonne4\":\"valeur4\"}]");

        try {
            writer.endArray();
            writer.endObject();
            fail("devrait lever une exeption");
        } catch (JSONException e) {
        }

        File tmp2 = File.createTempFile("writer2", null);

        FileWriter fw2 = new FileWriter(tmp2);
        writer = new JSONWriter(fw2);

        try {
            writer.array();
            writer.object().key("colonne1").key("colonne2").value("valeur1").endObject();
            writer.endArray();
            fail("devrai lever une exeption");
        } catch (JSONException e) {
        }

    }

    @Test
    public void testReader() throws IOException {
        System.out.println("lecture dans un fichier json");

        File tmp = File.createTempFile("reader", null);

        PrintStream ps = new PrintStream(tmp);
        ps.println("[{\"colonne1\":\"valeur1\"},{\"colonne2\":\"valeur2\"},{\"colonne3\":\"valeur3\"},{\"colonne4\":\"valeur4\"}]");

        JSONTokener token = new JSONTokener(new FileReader(tmp));
        JSONArray array = (JSONArray) token.nextValue();

        try {
            token.nextValue();
            fail("dervrai lever une execption");
        } catch (JSONException e) {
        }

        assertEquals(array.toString(), "[{\"colonne1\":\"valeur1\"},{\"colonne2\":\"valeur2\"},{\"colonne3\":\"valeur3\"},{\"colonne4\":\"valeur4\"}]");

        assertEquals(array.length(), 4);

        assertEquals(array.getJSONObject(array.length() - 1).get("colonne4"), "valeur4");

        try {
            array.get(5);
            fail("dervrai lever une execption");
        } catch (JSONException e) {
        }



        File tmp2 = File.createTempFile("reader2", null);
        token = new JSONTokener(new FileReader(tmp2));

        try {
            token.nextValue();
            fail("dervrai lever une execption");
        } catch (JSONException e) {
        }
    }
}
