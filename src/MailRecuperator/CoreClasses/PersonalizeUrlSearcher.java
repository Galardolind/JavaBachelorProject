
package MailRecuperator.CoreClasses;

import java.util.Set;
import java.util.TreeSet;

/**
 * Search on number of pages choosen by the user of each search engine.
 */
public class PersonalizeUrlSearcher implements UrlSearcherInterface<Set<String>> {

    /**
     * Contains the number of pages of the search engines that will be visited.
     */
    private int number;
    /**
     * Contains the index of search engine page where he going to search. 
     */
    private int startPage;
    
    /**
     * This constructor initialise number and startPage.
     * 
     * @param numberOfPages Contains the number of pages of the search engines that will be visited.
     * @param startPage Contains the index of search engine page where he going to search. 
     * @throws ExceptionRecuperation If numberOfPages <= 0 or startPage < 0
     */
    public PersonalizeUrlSearcher(int numberOfPages, int startPage) throws ExceptionRecuperation {
        if(numberOfPages <= 0 || startPage < 0){
            throw new ExceptionRecuperation("PersonalizeUrlSearcher.PersonalizeUrlSearcher() : Parameters are incorrects");
        }
        this.number = numberOfPages;
        this.startPage = startPage;
    }
    
    /**
     * This method call the 4 search engine (yahoo/google/bing/babylon), to find urls adresses 
     * in report to the request and return them.
     * 
     * @param request Contains the request from current search.
     * @return Urls found by search engine.
     */
    @Override
    public Set<String> getUrlsWithRequest(String request) {
        Set<String> urlList = new TreeSet<>();
        GoogleSearchParsing gs = new GoogleSearchParsing();
        YahooSearchParsing ys = new YahooSearchParsing();
        BingSearchParsing bs = new BingSearchParsing();
        BabylonSearchParsing babs = new BabylonSearchParsing();
        for(int i = 1; i <= number ; i++){
            try {
                urlList.addAll(gs.personalizeSearchOfThisRequest(request, startPage, i));
            } catch(ExceptionRecuperation e){
            }
            try {
                urlList.addAll(ys.personalizeSearchOfThisRequest(request, startPage, i));
            } catch(ExceptionRecuperation e){
            }
            try {
                urlList.addAll(bs.personalizeSearchOfThisRequest(request, startPage, i));
            } catch(ExceptionRecuperation e){
            }
            try {
                urlList.addAll(babs.personalizeSearchOfThisRequest(request, startPage, i));
            } catch(ExceptionRecuperation e){
            }
        }
        return urlList;
    }
}
