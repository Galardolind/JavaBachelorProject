
package MailRecuperator.CoreClasses;

import java.net.URLEncoder;
import java.util.Set;
import java.util.TreeSet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Transform a request (String) into valid url adress for Google and
 * use GoogleSearch to find URL about this request
 */
public class GoogleSearchParsing extends UrlSearch implements SearchAgentsInterface<Set<String>> {

    /**
     * This method transform request into a valid url in the current search engine.
     * 
     * @param txtUrl request to be searched.
     * @param index Index number of page to search.(each page contain 10 urls)
     * @return valid url as a string.
     * @throws ExceptionRecuperation If the request is null or index < 0, or if the request can't be encoded into UTF-8.
     */
    @Override
    public String transformStringToValidUrl(String txtUrl, int index) throws ExceptionRecuperation {
        String url = null;
        if (txtUrl == null || index < 0) {
            throw new ExceptionRecuperation("GoogleSearchParsing.transformStringToValidUrl() : Parameters are incorrects , txtUrl : "+txtUrl+" , index : "+index);
        }
        try {
            url = "https://www.google.com/search?oe=utf8&ie=utf8&source=uds&start=" + index + "&q=" + URLEncoder.encode(txtUrl, "UTF-8");
        } catch (Exception e) {
            throw new ExceptionRecuperation("GoogleSearchParsing.transformStringToValidUrl() : " + e);
        }
        return url;
    }

    /**
     * This method is used to parse search engine pages and found url in theses pages, normaly
     * the page find match to the request.
     * 
     * @param doc Is jsoup Document which contains the current search engine page content.
     * @return Url lists find as a String set.
     * @throws ExceptionRecuperation If doc is null, or if its impossible to parse this page.
     */
    @Override
    public Set<String> getUrlInContent(Document doc) throws ExceptionRecuperation {
        if (doc == null) {
            throw new ExceptionRecuperation("GoogleSearchParsing.getUrlInContent() : Document is incorrect");
        }
        Set<String> urlList = new TreeSet<>();
        try {
            Elements searchDiv = doc.select("div#search").select("ol").select("h3").select("a[href]");
            String urlLink = "";
            for (Element link : searchDiv) {
                urlLink = link.attr("abs:href");
                if (urlLink.length() > 22) {
                    String test = "https://www.google.com/";
                    if (urlLink.substring(0, test.length()).equals(test)) {
                    } else {
                        urlList.add(urlLink);
                    }
                } else {
                    urlList.add(urlLink);
                }
            }
        } catch (Exception e) {
            throw new ExceptionRecuperation("GoogleSearchParsing.getUrlInContent() : " + e);
        }
        return urlList;
    }
}
