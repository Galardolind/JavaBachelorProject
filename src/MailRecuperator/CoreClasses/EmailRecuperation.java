
package MailRecuperator.CoreClasses;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import Database.CoreClasses.*;
import java.io.File;

/**
 * This is main class to launch an email recuperation, that contain all object needed to do the job and finish properly.
 */
public class EmailRecuperation extends Thread {
    
    /**
     * Contains URL list from current search.
     */
    ArrayList<String> urlList;
    /**
     * Contains Email list from current search.
     */
    Set<Email> eMailList;
    /**
     * Contains the request from current search.
     */
    String request;
    /**
     * Contains the System DataBase from current search.
     */
    DataBase dbProg;
    /**
     * Contains the User DataBase from current search.
     */
    DataBase dbChoosen;
    /**
     * Contains all statistics of searcher.
     */
    StateOfRecup state;
    /**
     * Contains the number of pages of the search engines that will be visited
     */
    int numberToVisit;

    /**
     * Initialise an EmailRecuperation Object wich contains path choosen by the user where mail adresses is record,
     * the request to be search and number of pages of the search engines that will be visited.
     * 
     * @param req Request to search in internet.
     * @param pathToRecord path to record Email Database.
     * @param numberToVisit number of pages of the search engines that will be visited.
     * @throws ExceptionRecuperation If req/pathToRecord == null or numberToVisit <= 0, and if a problem is encountered during initialization of database.
     */
    public EmailRecuperation(String req, String pathToRecord, int numberToVisit) throws ExceptionRecuperation {
        if(req == null || pathToRecord == null || numberToVisit <= 0){
            throw new ExceptionRecuperation("EmailRecuperation : Invalid parameters ");
        }
        try {
            dbProg = new DataBase(System.getProperty("user.dir") +File.separator +"data"+File.separator+"recuperatorData");
            dbChoosen = new DataBase(pathToRecord);
        } catch(ExceptionDataBase e){
            throw new ExceptionRecuperation("EmailRecuperation : Impossible to create databases "+e);
        }
        urlList = new ArrayList<>();
        eMailList = new TreeSet<>();
        request = req;
        this.numberToVisit = numberToVisit;
    }
    
    /**
     * Launch the search.
     */
    public void run(){
        try {
            doPersonalizeSearch(numberToVisit);
        } catch (ExceptionRecuperation ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Used to set statistics Object as a reference.
     * @param stat Object which contains statistics.
     */
    public void setVar(StateOfRecup stat){
        state = stat;
    }
    
    /**
     * @return mail adresses recuperated by the program.
     */
    public Set<Email> getEmails(){
        return eMailList;
    }
    
    /**
     * @return urls adresses recuperated by the program.
     */
    public ArrayList<String> getUrls(){
        return urlList;
    }
    
    /**
     * @return number of search engine pages to be visited by the program.
     */
    public int getNumberOfPages(){
        return this.numberToVisit;
    }
    
    
    /**
     * This is the main method of the program, it launch request in search engine,
     * recovers urls adresses from search engine (google/bing/yahoo/babylon),increment
     * the historic database from these url, filter already visited pages, and trying to find
     * email addresses in these pages, after that that method save mails adresses found.
     * 
     * @param number pages of the search engines that will be visited.
     * @throws ExceptionRecuperation If System Database, or User Database, or request is null/void, if number is <= 0.
     */
    public void doPersonalizeSearch(int number) throws ExceptionRecuperation {
        if(number <= 0 || this.dbProg == null || this.dbChoosen == null || request.length() <= 0 || request == null){
            throw new ExceptionRecuperation("EmailRecuperation : doPersonalizeSearch : error "
                    + " in parameters initialization ");
        }
        for(int i = 0; i < number; i++){
            System.out.println();
            String pagestring = "Page Number : "+(i+1)+"/"+number+" ";
            if(state.isStop()){
                return;
            } else {
                if(state.isRun()){
                    try {
                        System.out.println(pagestring+" -Start search :  page number "+(i+1)+" of search engine");
                        try {
                            Email mail = new Email(dbChoosen,dbProg);
                        } catch(ExceptionRecuperation e){
                        }
                        PersonalizeUrlSearcher urlSearcher = new PersonalizeUrlSearcher(1,i);
                        urlList.addAll(urlSearcher.getUrlsWithRequest(request));
                        // si la liste d'url est vide on arrete la recherche la (pour la recherche infinie)
                        if(urlList.size() == 0){
                            return;
                        }
                        System.out.println(pagestring+" -Medium search :  "+urlList.size() +" pages have been recovered, they will be filtered by the visited url history ");
                        state.incrementNbOfPercent(1);
                        
                        ArrayList<String> temp = new ArrayList<String>();
                        temp.addAll(urlList);
                        urlList.clear();
                        UrlFilter historicFilter = new UrlFilter(dbProg);
                        urlList.addAll(historicFilter.filterUrlAlreadyVisited(temp));
                        
                        System.out.println(pagestring+" -Medium search :  "+urlList.size()+" pages left after history analysis ");
                        state.incrementNbOfPages(urlList.size());
                        
                        EmailFinder emf = new EmailFinder();
                        eMailList.addAll(emf.findEmailInPages(urlList, request));
                        
                        ArrayList<Email> tempMail = new ArrayList<Email>();
                        tempMail.addAll(eMailList);
                        System.out.println(pagestring+" -End search :  "+tempMail.size()+" emails have been found, saving ... ");
                        
                        state.incrementNbOfEmails(tempMail.size());
                        
                        for(int j = 0; j < tempMail.size(); j++){
                            tempMail.get(j).saveEmail();
                        }
                        try {
                            dbChoosen.save();
                            dbProg.save();
                        } catch(Exception e){
                            System.out.println("EmailRecuperation : doPersonalizeSearch :   Error during save : "+e);
                        }

                        System.out.println(pagestring+" -End search of page :  "+(i+1)+" on "+number+" pages ");
                        eMailList.clear();
                        urlList.clear();
                        state.incrementNbOfPercent(1);
                    } catch (ExceptionRecuperation e){
                    }
                } else {
                    while(!state.isRun()){
                        if(state.isStop()){
                            return;
                        }
                        try {
                            this.sleep(100);
                        } catch (InterruptedException ex) {
                        }
                    }
                    i--;
                }
            }
        }
    }
    
}
