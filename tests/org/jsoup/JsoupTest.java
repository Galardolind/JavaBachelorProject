/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * Tests of Jsoup API
 * 
 */
public class JsoupTest {

    public JsoupTest() {
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
    public void testJsoupConnect() {

        // connect with all parameters null should return exception
        try {
            Document doc = Jsoup.connect(null).userAgent(null).referrer(null).get();
            fail("should return an exception");
        } catch (Exception ex) {
        }

        // connect with all parameters except url null should return exception
        try {
            Document doc = Jsoup.connect("http://google.com").userAgent(null).referrer(null).get();
            fail("should return an exception");
        } catch (Exception ex) {
        }

        // connect to null url should return exception
        try {
            Document doc = Jsoup.connect(null).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 6.1; fr-FR; rv19.0) Gecko/20100101 Firefox/19.0").referrer("http://www.google.com").get();
            fail("should return an exception");
        } catch (Exception ex) {
        }

        // connect to empty url should return exception
        try {
            Document doc = Jsoup.connect("").userAgent("Mozilla/5.0 (Windows; U; WindowsNT 6.1; fr-FR; rv19.0) Gecko/20100101 Firefox/19.0").referrer("http://www.google.com").get();
            fail("should return an exception");
        } catch (Exception ex) {
        }

        // connect to valid url should not return exception
        try {
            Document doc = Jsoup.connect("http://google.com").userAgent("Mozilla/5.0 (Windows; U; WindowsNT 6.1; fr-FR; rv19.0) Gecko/20100101 Firefox/19.0").referrer("http://www.google.com").get();

        } catch (Exception ex) {
            fail("should return an exception");
        }
    }

    @Test
    public void testJsoupParse() {

        // parse should return an exception with null argument
        try {
            Document doc = Jsoup.parse(null);
            fail("should return an exception");
        } catch (Exception ex) {
        }
        {
            // document should contain html tags after parsing
            Document doc = Jsoup.parse("");
            assertEquals("<html>\n <head></head>\n <body></body>\n</html>", doc.toString());
        }
        {
            // document should contain html body tags after parsing
            Document doc = Jsoup.parse("");
            assertEquals("<body></body>", doc.body().toString());
        }
        {
            // document should contain html header tags after parsing
            Document doc = Jsoup.parse("");
            assertEquals("<head></head>", doc.head().toString());
        }
    }

    @Test
    public void testJsoupGetElementsMatchingOwnText() {

        Pattern emailRegex = Pattern.compile("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})");

        {
            // should contain nothing
            Document doc = Jsoup.parse("<p>lalala apzekpoazje pmail.com</p><p>aaaaaa</p>");
            Elements contents = doc.getElementsMatchingOwnText(emailRegex);
            assertEquals("", contents.toString());
        }
        {
            // should contain only tags with mail inside
            Document doc = Jsoup.parse("<p>lalala apzekpoazje pa@gmail.com</p><p>aaaaaa</p>");
            Elements contents = doc.getElementsMatchingOwnText(emailRegex);
            assertEquals("<p>lalala apzekpoazje pa@gmail.com</p>", contents.toString());
        }
        {
            // should contain only all tags with mail adresses
            Document doc = Jsoup.parse("<p>lalala apzekpoazje pa@gmail.com</p><p>aaaaaa</p><ul>hihi@hotmail.com</ul> <h3>peti@unice.fr</h3><h3>aaaaaaaaaaa</h3>");
            Elements contents = doc.getElementsMatchingOwnText(emailRegex);
            String[] tab = {"<p>lalala apzekpoazje pa@gmail.com</p>", "<ul>\n hihi@hotmail.com\n</ul>", "<h3>peti@unice.fr</h3>"};
            int i = 0;
            for (Element elem : contents) {
                assertEquals(tab[i++], elem.toString());
            }
        }

    }

    @Test
    public void testJsoupSelect() {
        {
            // should return an empty string (don't found nothing)
            Document doc = Jsoup.parse("<p>lalala apzekpoazje pa@gmail.com</p><div id=\"results\"><h3 href=\"TIMI\">aaaaaaaaaaa</h3></div><p></p><div id=\"results\"><h3 href=\"JONY\">aaaaaaaaaaa</h3></div>");
            Elements searchDiv = doc.select("div#nope").select("h3").select("a[href]");
            assertEquals("", searchDiv.toString());
        }
        {
            // should return content of href tags in <div id="results"><h3 href="xxxxx"></h3></div>
            Document doc = Jsoup.parse("<p>lalala apzekpoazje pa@gmail.com</p><div id=\"results\"><h3 href=\"TIMI\">aaaaaaaaaaa</h3></div><p></p><div id=\"results\"><h3 href=\"JONY\">aaaaaaaaaaa</h3></div>");
            Elements searchDiv = doc.select("div#results").select("h3").select("a[href]");
            String urlLink = "";
            String[] strtest = {"TIMI","JONY"};
            int i = 0;
            for (Element link : searchDiv) {
                urlLink = link.attr("abs:href");
                assertEquals(strtest[i++], urlLink.toString());
                
                
            }

        }
    }
}
