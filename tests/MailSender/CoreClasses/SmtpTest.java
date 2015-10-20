package MailSender.CoreClasses;

import Database.CoreClasses.ExceptionDataBase;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Ces Test mettent assez longtemps a passer et ce a cause des tiemout.
 * Le smtpHost doit etre configuré le smtp courant
 */

public class SmtpTest {
    
    String host;

    public SmtpTest() {
        host = "smtp.orange.fr";
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

    /**
     * Test of sendToMultiWithTemplate method, of class Smtp.
     */
    @Test
    public void testSendToMultiWithTemplate() {
        System.out.println("sendToMultiWithTemplate");

        {
            System.out.println("TEST 1 : FULL NULL FAIL");
            Smtp instance;
            try {
                instance = new Smtp(null, null, null, null, false);
                instance.setStatus(null);
                instance.setFilteredMailList(null);
                instance.setEnv();
                instance.sendToMultiWithTemplate();
                fail("le lancement a reussi et n'aurais pas du.");
            } catch (ExceptionSender | ExceptionDataBase ex) {
                //sa plante donc sa marche.
            }
        }

        {
            System.out.println("TEST 2 : MAUVAIS PORT FAIL");
            try {
                //faux ports
                SimpleSettings ss = new SimpleSettings(host, "26", "", "", 0, false, "projet.licence.return@gmail.com", "localhost");
                String[] tos = new String[12];
                tos[0] = "topiko@yopmail.com";
                tos[1] = "topiko@yopmail.com";
                tos[2] = "topiko@yopmail.com";
                tos[3] = "topiko@yopmail.com";
                tos[4] = "topiko@yopmail.com";
                tos[5] = "topiko@yopmail.com";
                tos[6] = "topiko@yopmail.com";
                tos[7] = "topiko@yopmail.com";
                tos[8] = "topiko@yopmail.com";
                tos[9] = "topiko@yopmail.com";
                tos[10] = "topiko@yopmail.com";
                tos[11] = "topiko@yopmail.com";

                String[] keys = new String[2];
                keys[0] = "zett";
                keys[1] = "zbstb";
                Smtp instance = new Smtp(ss, new Template("", ""), tos, keys, true);
                SmtpStatus status = new SmtpStatus(tos.length);
                try {
                    DatabaseHandler db = new DatabaseHandler();
                    instance.setFilteredMailList(db);
                } catch (ExceptionDataBase ex) {
                    fail("Error during bdHandler init");
                }
                instance.setStatus(status);
                instance.setEnv();
                int temp = instance.sendToMultiWithTemplate();
                System.out.println(temp);
                assertTrue(temp > 0);
            } catch (ExceptionSender ex) {
            }
        }

        {
            System.out.println("TEST 3 : MAUVAIS SMTP FAIL");
            try {
                //faux smtp host
                SimpleSettings ss = new SimpleSettings("smtp.youpi.fr", "25", "", "", 0, false, "projet.licence.return@gmail.com", "localhost");
                String[] tos = new String[12];
                tos[0] = "topiko@yopmail.com";
                tos[1] = "topiko@yopmail.com";
                tos[2] = "topiko@yopmail.com";
                tos[3] = "topiko@yopmail.com";
                tos[4] = "topiko@yopmail.com";
                tos[5] = "topiko@yopmail.com";
                tos[6] = "topiko@yopmail.com";
                tos[7] = "topiko@yopmail.com";
                tos[8] = "topiko@yopmail.com";
                tos[9] = "topiko@yopmail.com";
                tos[10] = "topiko@yopmail.com";
                tos[11] = "topiko@yopmail.com";

                String[] keys = new String[2];
                keys[0] = "zett";
                keys[1] = "zbstb";
                Smtp instance = new Smtp(ss, new Template("", ""), tos, keys, true);
                SmtpStatus status = new SmtpStatus(tos.length);
                try {
                    DatabaseHandler db = new DatabaseHandler();
                    instance.setFilteredMailList(db);
                } catch (ExceptionDataBase ex) {
                    fail("Error during bdHandler init");
                }
                instance.setStatus(status);
                instance.setEnv();
                int temp = instance.sendToMultiWithTemplate();
                System.out.println(temp);
                assertTrue(temp > 0);
            } catch (ExceptionSender ex) {
            }
        }

        {
            System.out.println("TEST 4 : PASE DE TEMPLATE FAIL");
            try {
                //verification template
                SimpleSettings ss = new SimpleSettings(host, "25", "", "", 0, false, "projet.licence.return@gmail.com", "localhost");
                String[] tos = new String[12];
                tos[0] = "topiko@yopmail.com";
                tos[1] = "topiko@yopmail.com";
                tos[2] = "topiko@yopmail.com";
                tos[3] = "topiko@yopmail.com";
                tos[4] = "topiko@yopmail.com";
                tos[5] = "topiko@yopmail.com";
                tos[6] = "topiko@yopmail.com";
                tos[7] = "topiko@yopmail.com";
                tos[8] = "topiko@yopmail.com";
                tos[9] = "topiko@yopmail.com";
                tos[10] = "topiko@yopmail.com";
                tos[11] = "topiko@yopmail.com";

                String[] keys = new String[2];
                keys[0] = "zett";
                keys[1] = "zbstb";
                Smtp instance = new Smtp(ss, null, tos, keys, true);
                SmtpStatus status = new SmtpStatus(tos.length);
                try {
                    DatabaseHandler db = new DatabaseHandler();
                    instance.setFilteredMailList(db);
                } catch (ExceptionDataBase ex) {
                    fail("Error during bdHandler init");
                }
                instance.setStatus(status);
                instance.setEnv();
                int temp = instance.sendToMultiWithTemplate();
                System.out.println(temp);
                fail("template null : pass pas normalement");
            } catch (ExceptionSender ex) {
                
            }
        }



        {
            System.out.println("TEST 5 : OK");
            try {
                //String path = System.getProperty("user.dir")+File.separator+"testeur2500";
                SimpleSettings ss = new SimpleSettings(host, "25", "", "", 0, false, "projet.licence.return@gmail.com", "localhost");
                String[] tos = new String[2];
                tos[0] = "topiko@yopmail.com";
                tos[1] = "topiko@yopmail.com";
                String[] keys = new String[2];
                keys[0] = "zett";
                keys[1] = "zbstb";
                Smtp instance = new Smtp(ss, new Template("", ""), tos, keys, true );
                SmtpStatus status = new SmtpStatus(tos.length);
                try {
                    DatabaseHandler db = new DatabaseHandler();
                    instance.setFilteredMailList(db);
                } catch (ExceptionDataBase ex) {
                    fail("Error during bdHandler init");
                }
                instance.setStatus(status);
                instance.setEnv();
                int temp = instance.sendToMultiWithTemplate();
                System.out.println(temp);
                assertTrue(temp == 0);
            } catch (ExceptionSender ex) {
                fail("Error prt.");
            }
        }
    }
}
