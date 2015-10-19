
package MailRecuperator.CoreClasses;

import java.util.Set;
import java.util.TreeSet;

/**
 * Search on 2 pages of each search engine.
 */
public class RapidUrlSearcher implements UrlSearcherInterface<Set<String>> {
    
    /**
     * Launch the search of the request on search engine.
     * 
     * @param request Contains the request to be search.
     * @return Urls found by search engine.
     * @throws ExceptionRecuperation If an Exception is thrown by search engine parser.
     */
    @Override
    public Set<String> getUrlsWithRequest(String request) throws ExceptionRecuperation {
        Set<String> urlList = new TreeSet<>();
        try {
            GoogleSearchParsing gs = new GoogleSearchParsing();
            urlList.addAll(gs.searchRapidlyThisRequest(request));
        } catch(ExceptionRecuperation e){
        }
        try {
            YahooSearchParsing ys = new YahooSearchParsing();
            urlList.addAll(ys.searchRapidlyThisRequest(request));
        } catch(ExceptionRecuperation e){
        }
        try {
            BingSearchParsing bs = new BingSearchParsing();
            urlList.addAll(bs.searchRapidlyThisRequest(request));
        } catch(ExceptionRecuperation e){
        }
        try {
            BabylonSearchParsing babs = new BabylonSearchParsing();
            urlList.addAll(babs.searchRapidlyThisRequest(request));
        } catch(ExceptionRecuperation e){
        }
        return urlList;
    }
    
}
