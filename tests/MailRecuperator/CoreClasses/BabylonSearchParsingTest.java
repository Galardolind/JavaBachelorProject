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
public class BabylonSearchParsingTest {
    
    BabylonSearchParsing instance;
    
    public BabylonSearchParsingTest() {
        this.instance = new BabylonSearchParsing();
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
     * Test of transformStringToValidUrl method, of class BabylonSearchParsing.
     */
    @Test
    public void testTransformStringToValidUrl() {
        // txtUrl = ""
        String txtUrl = "";
        int index = 0;
        String expResult = "http://search.babylon.com/?s=web&babsrc=home&rlz=0&q="+txtUrl+"&start="+index;
        String result;
        try {
            result = instance.transformStringToValidUrl(txtUrl, index);
            assertEquals(expResult, result);
        } catch (ExceptionRecuperation ex) {
            fail(""+ ex);
        }
        
        // txtUrl = "test"
        txtUrl = "test";
        index = 0;
        expResult = "http://search.babylon.com/?s=web&babsrc=home&rlz=0&q="+txtUrl+"&start="+index;
        try {
            result = instance.transformStringToValidUrl(txtUrl, index);
            assertEquals(expResult, result);
        } catch (ExceptionRecuperation ex) {
            fail(""+ ex);
        }
        
        // txtUrl = "" and index = 5
        txtUrl = "";
        index = 5;
        expResult = "http://search.babylon.com/?s=web&babsrc=home&rlz=0&q="+txtUrl+"&start="+index;
        try {
            result = instance.transformStringToValidUrl(txtUrl, index);
            assertEquals(expResult, result);
        } catch (ExceptionRecuperation ex) {
            fail(""+ ex);
        }
        
        // txtUrl = " " and index = 0
        txtUrl = " ";
        index = 0;
        try {
            expResult = "http://search.babylon.com/?s=web&babsrc=home&rlz=0&q="+URLEncoder.encode(txtUrl, "UTF-8")+"&start="+index;
            result = instance.transformStringToValidUrl(txtUrl, index);
            assertEquals(expResult, result);
        } catch (UnsupportedEncodingException | ExceptionRecuperation ex) {
            fail("" + ex);
        }
        
        // txtUrl = "&" and index = 0
        txtUrl = "&";
        index = 0;
        try {
            expResult = "http://search.babylon.com/?s=web&babsrc=home&rlz=0&q="+URLEncoder.encode(txtUrl, "UTF-8")+"&start="+index;
            result = instance.transformStringToValidUrl(txtUrl, index);
            assertEquals(expResult, result);
        } catch (UnsupportedEncodingException | ExceptionRecuperation ex) {
            fail("" + ex);
        }
        
        // txtUrl = "testé" and index = 0
        txtUrl = "testé";
        index = 0;
        try {
            expResult = "http://search.babylon.com/?s=web&babsrc=home&rlz=0&q="+URLEncoder.encode(txtUrl, "UTF-8")+"&start="+index;
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
     * Test of getUrlInContent method, of class BabylonSearchParsing.
     */
    @Test
    public void testGetUrlInContent() {
        // with a correct result in the document
        Document doc = new Document("");
        String link = "http://deptinfo.unice.fr";
        // create a document where link should be recognize by instance
        doc.html("<div id=\"gRsWeb\"><div class=\"gRsSlice\"><a class=\"gRsSlicetitle\" href=\""+link+"\" /></div></div>");
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
        doc.html("<div id=\"gRsWeb\"><div class=\"gRsSlice\"><a class=\"gRsSlicetitle\" href=\""+link+"\" /></div></div>");
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
        doc.html("<div id=\"gRsWeb\"><div class=\"gRsSlice\"><a class=\"gRsSlicetitle\" href=\""+link1+"\" /></div>"
                +"<div class=\"gRsSlice\"><a class=\"gRsSlicetitle\" href=\""+link2+"\" /></div></div>");
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
        doc.html("<div id=\"gRsWeb\"><div class=\"gRsSlice\"><a class=\"gRsSlicetitle\" href=\""+link+"\" /></div>"
                +"<div class=\"gRsSlice\"><a class=\"gRsSlicetitle\" href=\""+link+"\" /></div></div>");
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
}