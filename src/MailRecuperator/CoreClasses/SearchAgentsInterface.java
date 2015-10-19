package MailRecuperator.CoreClasses;

import org.jsoup.nodes.Document;

/**
 * Search Engine Interface
 */
public interface SearchAgentsInterface<T> {
    
    /**
     * This method transform request into a valid url in the current search engine.
     * 
     * @param txtUrl request to be searched.
     * @param index Index number of page to search.(each page contain 10 urls)
     * @return valid url as a string.
     * @throws ExceptionRecuperation If the request is null or index < 0, or if the request can't be encoded into UTF-8.
     */
    public String transformStringToValidUrl(String txtUrl, int index) throws ExceptionRecuperation;
    
    /**
     * This method is used to parse search engine pages and found url in theses pages, normaly
     * the page find match to the request.
     * 
     * @param doc Is jsoup Document which contains the current search engine page content.
     * @return Url lists find as a String set.
     * @throws ExceptionRecuperation If doc is null, or if its impossible to parse this page.
     */
    public T getUrlInContent(Document doc) throws ExceptionRecuperation;
    
}
