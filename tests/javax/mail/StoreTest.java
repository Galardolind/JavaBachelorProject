package javax.mail;

import java.util.Properties;
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
public class StoreTest {

    public StoreTest() {
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
    public void testConnect() throws MessagingException {
        Properties props;
        Session session;
        Folder folder;
        props = new Properties();

        props.put("mail.host", "smtp.unice.fr");
        props.put("mail.store.protocol", "pop3s");
        props.put("mail.pop3s.auth", "true");
        props.put("mail.pop3s.port", 25);

        session = Session.getInstance(props, null);
        Store instance;
        try {
            instance = session.getStore();
            instance.connect(null, null);
            fail("Should throw MessagingException : params are null.");
        } catch (NoSuchProviderException ex) { 
            throw ex; // this exception should not be thrown to test connect method
        } catch (MessagingException ex) {
           assertTrue(true);
        }
    }
    
    @Test
    public void testGetFolder() throws MessagingException {
        Properties props;
        Session session;
        Folder folder;
        props = new Properties();

        props.put("mail.host", "pop.gmail.com");
        props.put("mail.store.protocol", "pop3s");
        props.put("mail.pop3s.auth", "true");
        props.put("mail.pop3s.port", 995);

        session = Session.getInstance(props, null);
        Store instance;
        
        // with wrong param
        instance = session.getStore();
        instance.connect("projet.licence.return@gmail.com", "spamreturn");
        try {
            folder = instance.getFolder("");
        } catch (MessagingException ex) {
            assertTrue(true);
        }

        // with valid param
        try {
            folder = instance.getFolder("INBOX");
            assertTrue(true);
        } catch (MessagingException ex) {
            fail("Should not throw MessagingException");
        }
    }
    
}
