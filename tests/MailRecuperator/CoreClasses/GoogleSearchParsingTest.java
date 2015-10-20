package MailRecuperator.CoreClasses;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;
import java.util.TreeSet;
import org.jsoup.nodes.Document;
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
public class GoogleSearchParsingTest {
    
    GoogleSearchParsing instance;
    
    public GoogleSearchParsingTest() {
        this.instance = new GoogleSearchParsing();
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
     * Test of transformStringToValidUrl method, of class GoogleSearchParsing.
     */
    @Test
    public void testTransformStringToValidUrl() {
        // txtUrl = ""
        String txtUrl = "";
        int index = 0;
        String expResult = "https://www.google.com/search?oe=utf8&ie=utf8&source=uds&start=" + index + "&q=" + txtUrl;
        String result;
        try {
            result = instance.transformStringToValidUrl(txtUrl, index);
            assertEquals(expResult, result);
        } catch (ExceptionRecuperation ex) {
            fail("" + ex);
        }
        
        // txtUrl = "test"
        txtUrl = "test";
        index = 0;
        expResult = "https://www.google.com/search?oe=utf8&ie=utf8&source=uds&start=" + index + "&q=" + txtUrl;
        try {
            result = instance.transformStringToValidUrl(txtUrl, index);
            assertEquals(expResult, result);
        } catch (ExceptionRecuperation ex) {
            fail("" + ex);
        }
        
        // txtUrl = "" and index = 5
        txtUrl = "";
        index = 5;
        expResult = "https://www.google.com/search?oe=utf8&ie=utf8&source=uds&start=" + index + "&q=" + txtUrl;
        try {
            result = instance.transformStringToValidUrl(txtUrl, index);
            assertEquals(expResult, result);
        } catch (ExceptionRecuperation ex) {
            fail("" + ex);
        }
        
        // txtUrl = " " and index = 0
        txtUrl = " ";
        index = 0;
        try {
            expResult = "https://www.google.com/search?oe=utf8&ie=utf8&source=uds&start=" + index + "&q=" + URLEncoder.encode(txtUrl, "UTF-8");
            result = instance.transformStringToValidUrl(txtUrl, index);
            assertEquals(expResult, result);
        } catch (UnsupportedEncodingException | ExceptionRecuperation ex) {
            fail("" + ex);
        }
        
        // txtUrl = "&" and index = 0
        txtUrl = "&";
        index = 0;
        try {
            expResult = "https://www.google.com/search?oe=utf8&ie=utf8&source=uds&start=" + index + "&q=" + URLEncoder.encode(txtUrl, "UTF-8");
            result = instance.transformStringToValidUrl(txtUrl, index);
            assertEquals(expResult, result);
        } catch (UnsupportedEncodingException | ExceptionRecuperation ex) {
            fail("" + ex);
        }
        
        // txtUrl = "testé" and index = 0
        txtUrl = "testé";
        index = 0;
        try {
            expResult = "https://www.google.com/search?oe=utf8&ie=utf8&source=uds&start=" + index + "&q=" + URLEncoder.encode(txtUrl, "UTF-8");
            result = instance.transformStringToValidUrl(txtUrl, index);
            assertEquals(expResult, result);
        } catch (UnsupportedEncodingException | ExceptionRecuperation ex) {
            fail("" + ex);
        }
        
        // txtUrl = null
        txtUrl = null;
        index = 0;
        try {
            instance.transformStringToValidUrl(txtUrl, index);
            fail("Should launch exception.");
        } catch (ExceptionRecuperation ex) {
            assertTrue(true);
        }
        
        // txtUrl = null
        txtUrl = "";
        index = -1;
        try {
            instance.transformStringToValidUrl(txtUrl, index);
            fail("Should launch exception.");
        } catch (ExceptionRecuperation ex) {
            assertTrue(true);
        }
    }

    /**
     * Test of getUrlInContent method, of class GoogleSearchParsing.
     */
    @Test
    public void testGetUrlInContent() {
        // with a correct result in the document
        Document doc = new Document("");
        String link = "http://deptinfo.unice.fr";
        // create a document where link should be recognize by instance
        doc.html("<div id=\"search\"><ol><li><h3><a href=\""+link+"\" /></h3></li></ol></div>");
        Set<String> expResult = new TreeSet<String>();
        expResult.add(link);
        Set result;
        try {
            result = instance.getUrlInContent(doc);
            assertEquals(expResult, result);
        } catch (ExceptionRecuperation ex) {
            fail(""+ ex);
        }
        
        // with a correct result in the document but the link is ""
        doc = new Document("");
        link = "";
        // create a document where link should be recognize by instance
        doc.html("<div id=\"search\"><ol><li><h3><a href=\""+link+"\" /></h3></li></ol></div>");
        expResult = new TreeSet<String>();
        expResult.add(link);
        try {
            result = instance.getUrlInContent(doc);
            assertEquals(expResult, result);
        } catch (ExceptionRecuperation ex) {
            fail(""+ ex);
        }
        
        // without anything in the document
        doc = new Document("");
        expResult = new TreeSet<String>();
        try {
            result = instance.getUrlInContent(doc);
            assertEquals(expResult, result);
        } catch (ExceptionRecuperation ex) {
            fail(""+ ex);
        }
        
        // with 2 correct results in the document
        doc = new Document("");
        String link1 = "http://deptinfo.unice.fr";
        String link2 = "http://ent.unice.fr";
        // create a document where link1 and link2 should be recognize by instance
        doc.html("<div id=\"search\"><ol><li><h3><a href=\""+link1+"\" /></h3></li>"
                +"<li><h3><a href=\""+link2+"\" /></h3></li></ol></div>");
        expResult = new TreeSet<String>();
        expResult.add(link1);
        expResult.add(link2);
        try {
            result = instance.getUrlInContent(doc);
            assertEquals(expResult, result);
        } catch (ExceptionRecuperation ex) {
            fail(""+ ex);
        }
        
        // with 2 correct results in the document but the same link
        doc = new Document("");
        link = "http://deptinfo.unice.fr";
        // create a document where link1 and link2 should be recognize by instance
        doc.html("<div id=\"search\"><ol><li><h3><a href=\""+link+"\" /></h3></li>"
                +"<li><h3><a href=\""+link+"\" /></h3></li></ol></div>");
        expResult = new TreeSet<String>();
        expResult.add(link);
        try {
            result = instance.getUrlInContent(doc);
            assertEquals(expResult, result);
        } catch (ExceptionRecuperation ex) {
            fail(""+ ex);
        }
        
        // doc = null
        doc = null;
        try {
            instance.getUrlInContent(doc);
            fail("Should launch exception.");
        } catch (ExceptionRecuperation ex) {
            assertTrue(true);
        }
    }
    
    /**
     * Test of personalizeSearchOfThisRequest method, of class GoogleSearchParsing 
     * inherited from abstract class UrlSearch.
     */
    @Test
    public void testPersonalizeSearchOfThisRequest() {
        // with starting = 0 and number = 1
        String request = "coucou";
        int starting = 0;
        int number = 1;
        Set<String> urlList;
        try {
            urlList = instance.personalizeSearchOfThisRequest(request, starting, number);
            assertTrue(urlList.size() > 0);
        } catch (ExceptionRecuperation ex) {
            fail(""+ ex);
        }
        
        // with starting = -1 and number = 1
        starting = -1;
        number = 1;
        try {
            instance.personalizeSearchOfThisRequest(request, starting, number);
            fail("Should launch exception.");
        } catch (ExceptionRecuperation ex) {
            assertTrue(true);
        }
        
        // with starting = 0 and number = 0
        starting = 0;
        number = 0;
        try {
            urlList = instance.personalizeSearchOfThisRequest(request, starting, number);
            assertEquals(urlList.size(), 0);
        } catch (ExceptionRecuperation ex) {
            fail(""+ ex);
        }
        
        // with starting = 0 and number = -1
        starting = 0;
        number = -1;
        try {
            instance.personalizeSearchOfThisRequest(request, starting, number);
            fail("Should launch exception.");
        } catch (ExceptionRecuperation ex) {
            assertTrue(true);
        }
    }
}