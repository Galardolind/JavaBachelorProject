
package MailRecuperator.CoreClasses;

/**
 * Interface used by the different Object who propose to search urls.
 */
interface UrlSearcherInterface<T> {
    

    /**
     * Launch the search of the request on search engines.
     * 
     * @param request Contains the request to be search.
     * @return Urls found by search engine.
     * @throws ExceptionRecuperation If an Exception is thrown by search engine parser.
     */
    public T getUrlsWithRequest(String request) throws ExceptionRecuperation;
    
}
