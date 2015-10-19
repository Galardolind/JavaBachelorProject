package MailRecuperator.CoreClasses;

import java.util.Set;
import java.util.TreeSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 * This class permit to launch request on all search engine, parse url adresses
 * and recover link will be used to search email.
 * 
 * note: 4 search engine are basically installed, if you remove one , you need
 *      to care about time between each search, if too many are done (apparently more than 1/sec)
 *      you can be IP blacklisted by the search engine remaining.
 * 
 * note2: For the user Agent informations we use this link below.
 *        http://whatsmyuseragent.com/
 */
abstract class UrlSearch {

    /**
     * This method transform request into a valid url in the current search engine.
     * 
     * @param txtUrl request to be searched.
     * @param index Index number of page to search.(each page contain 10 urls)
     * @return valid url as a string.
     * @throws ExceptionRecuperation If the request is null or index < 0, or if the request can't be encoded into UTF-8.
     */
    public abstract String transformStringToValidUrl(String request, int index) throws ExceptionRecuperation;

    /**
     * This method is used to parse search engine pages and found url in theses pages, normaly
     * the page find match to the request.
     * 
     * @param doc Is jsoup Document which contains the current search engine page content.
     * @return Url lists find as a String set.
     * @throws ExceptionRecuperation If doc is null, or if its impossible to parse this page.
     */
    public abstract Set<String> getUrlInContent(Document doc) throws ExceptionRecuperation;

    /**
     * Method to find 80 urls maximum.
     * 
     * @param request request to be searched.
     * @return Url lists find as a String set.
     * @throws ExceptionRecuperation If request is null or problem is encounter during connexion to the current pages.
     */
    public Set<String> searchRapidlyThisRequest(String request) throws ExceptionRecuperation {
        Set<String> urlList = new TreeSet<>();
        if(request.length() < 1 || request == null){
            throw new ExceptionRecuperation("UrlSearch.searchRapidlyThisRequest() : Parameters are incorrects");
        }
        for (int i = 1; i <= 2; i++) {
            String urlFormated = transformStringToValidUrl(request, i * 10);
            try {
                Document doc = Jsoup.connect(urlFormated)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 6.1; fr-FR; rv19.0) Gecko/20100101 Firefox/19.0")
                        .referrer("http://www.google.com")
                        .get();
                urlList.addAll(getUrlInContent(doc));
            } catch (Exception e) {
                throw new ExceptionRecuperation("UrlSearch.searchRapidlyThisRequest() : "+e);
            }
        }
        return urlList;
    }

    /**
     * Method to find 160 urls maximum.
     * 
     * @param request request to be searched.
     * @return Url lists find as a String set.
     * @throws ExceptionRecuperation If request is null or problem is encounter during connexion to the current pages.
     */
    public Set<String> searchThoroughlyThisRequest(String request) throws ExceptionRecuperation {
        Set<String> urlList = new TreeSet<>();
        if(request.length() < 1 || request == null){
            throw new ExceptionRecuperation("UrlSearch.searchThoroughlyThisRequest() : Parameters are incorrects");
        }
        for (int i = 1; i <= 4; i++) {
            String urlFormated = transformStringToValidUrl(request, i * 10);
            try {
                Document doc = Jsoup.connect(urlFormated)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 6.1; fr-FR; rv19.0) Gecko/20100101 Firefox/19.0")
                        .referrer("http://www.google.com")
                        .get();
                urlList.addAll(getUrlInContent(doc));
            } catch (Exception e) {
                throw new ExceptionRecuperation("UrlSearch.searchThoroughlyThisRequest() : "+e);
            }
        }
        return urlList;
    }

    /**
     * Method to find 240 urls maximum.
     * 
     * 
     * @param request request to be searched.
     * @return Url lists find as a String set.
     * @throws ExceptionRecuperation If request is null or problem is encounter during connexion to the current pages.
     */
    public Set<String> searchExtensivelyThisRequest(String request) throws ExceptionRecuperation {
        Set<String> urlList = new TreeSet<>();
        if(request.length() < 1 || request == null){
            throw new ExceptionRecuperation("UrlSearch.searchExtensivelyThisRequest() : Parameters are incorrects");
        }
        for (int i = 1; i <= 6; i++) {
            String urlFormated = transformStringToValidUrl(request, i * 10);
            try {
                Document doc = Jsoup.connect(urlFormated)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 6.1; fr-FR; rv19.0) Gecko/20100101 Firefox/19.0")
                        .referrer("http://www.google.com")
                        .get();
                urlList.addAll(getUrlInContent(doc));
            } catch (Exception e) {
                throw new ExceptionRecuperation("UrlSearch.searchExtensivelyThisRequest() : "+e);
            }
        }
        return urlList;
    }
    
    /**
     * Method to find how much urls user want in the search engines in connections with
     * the request (take care if the number is to high user can be black listed by the search engine).
     * 
     * @param request request to be searched.
     * @param starting Index pages where to start searching (in search engine).
     * @param number Number of pages of search engines where you search urls.
     * @return Url lists find as a String set.
     * @throws ExceptionRecuperation If request is null or problem is encounter during connexion to the current pages.
     */
    public Set<String> personalizeSearchOfThisRequest(String request, int starting, int number) throws ExceptionRecuperation {
        Set<String> urlList = new TreeSet<>();
        if(starting < 0 || number < 0 || request.length() < 1 || request == null){
            throw new ExceptionRecuperation("UrlSearch.personalizeSearchOfThisRequest() : Parameters are incorrects");
        }
        for (int i = starting; i < starting + number; i++) {
            String urlFormated = transformStringToValidUrl(request, i * 10);
            try {
                Document doc = Jsoup.connect(urlFormated)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 6.1; fr-FR; rv19.0) Gecko/20100101 Firefox/19.0")
                        .referrer("http://www.google.com")
                        .get();
                urlList.addAll(getUrlInContent(doc));
            } catch (Exception e) {
                throw new ExceptionRecuperation("UrlSearch.personalizeSearchOfThisRequest() : "+e);
            }
        }
        return urlList;
    }
}
