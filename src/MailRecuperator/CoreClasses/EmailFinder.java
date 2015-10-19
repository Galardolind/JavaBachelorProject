
package MailRecuperator.CoreClasses;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * This class is used to find email in internet pages. The regex can be modify
 * by the user if he want.This can found url or else content in internet pages
 * if the regex is correctly modify.
 */
public class EmailFinder {
    
    /**
     * String contains the regex value as a String.
     */
    static public String regex;
    
    /**
     * @param newRegex Contains the new regex to been set.
     */
    public void setRegex(String newRegex){
        this.regex = newRegex;
    }
    
    /**
     * This method is used to find Email in internet pages.If regex is not modify or incorrectly,
     * an default regex is set to work properly.
     * 
     * The official regex from RFC 2822 is this one below , as you see she's too long to analyse correctly
     * (in time) internet pages.
     * (?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])
     * 
     * @param urlList Contains url to visit.
     * @param request Contains request launch by user.
     * @return Contains Email Object correctly initialise.
     * @throws ExceptionRecuperation If urlList/request is null, if the regex is not correct java regex (verify above too).
     */
    public Set<Email> findEmailInPages(ArrayList<String> urlList, String request) throws ExceptionRecuperation {
        if(urlList == null || request == null){
            throw new ExceptionRecuperation("urlList is not initialize");
        }
        Set<Email> emailList = new TreeSet<Email>();
        Pattern emailRegex = null;
        if(this.regex == null || this.regex.equals("")){
            emailRegex = Pattern.compile("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})");
        } else {
            try {
                emailRegex = Pattern.compile(regex);
            } catch(Exception e){
                throw new ExceptionRecuperation("The regex is not conform to java regex : "+e);
            }
                }
        for(int i = 0; i < urlList.size(); i++){
            try{
                Document doc = Jsoup.connect(urlList.get(i))
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 6.1; fr-FR; rv19.0) Gecko/20100101 Firefox/19.0")
                        .referrer("http://www.google.com")
                        .get();
                
                Elements contents = doc.getElementsMatchingOwnText(emailRegex);
                for(Element email : contents){
                    Matcher matcher = emailRegex.matcher(email.text());
                    while(matcher.find()) {
                        Email newMail = new Email(matcher.group(), request);
                        emailList.add(newMail);
                    }
                }
            }catch(Exception e) {
            }
        }
        return emailList;
    }
    
}
