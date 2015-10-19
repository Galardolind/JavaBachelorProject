package MailRecuperator.CoreClasses;

import java.net.URLEncoder;
import java.util.Set;
import java.util.TreeSet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Transform a request (String) into valid url adress for Yahoo and
 * use YahooSearch to find URL about this request
 */
public class YahooSearchParsing extends UrlSearch implements SearchAgentsInterface<Set<String>> {

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
            throw new ExceptionRecuperation("YahooSearchParsing.transformStringToValidUrl() : Parameters are incorrects , txtUrl : "+txtUrl+" , index : "+index);
        }
        try {
            url = "http://fr.search.yahoo.com/search?p=" + URLEncoder.encode(txtUrl, "UTF-8") + "&ei=UTF-8&b=" + index;
        } catch (Exception e) {
            throw new ExceptionRecuperation("YahooSearchParsing.transformStringToValidUrl() : " + e);
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
            throw new ExceptionRecuperation("YahooSearchParsing.getUrlInContent() : Document is incorrect");
        }
        Set<String> urlList = new TreeSet<>();
        try {
            Elements searchDiv = doc.select("div#web").select("ol").select("h3").select("a[href]");
            String urlLink = "";
            for (Element link : searchDiv) {
                urlLink = link.attr("abs:href");
                if(urlLink.length() > 25){
                    if(urlLink.substring(9, 23).equals(".search.yahoo.")){
                        urlLink = "http://" + urlLink.substring(urlLink.lastIndexOf("http%3a//") + 9, urlLink.length());
                    }
                }
                urlList.add(urlLink);
            }
        } catch (Exception e) {
            throw new ExceptionRecuperation("YahooSearchParsing.getUrlInContent() : " + e);
        }
        return urlList;
    }
}
