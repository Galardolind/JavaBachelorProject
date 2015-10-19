package MailSender.CoreClasses;

import Database.CoreClasses.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class that handles the mail addresses and the keywords in the database.
 */
public class DatabaseEmail {
    
    DataBase db;
    private InterfaceTable emails;
    
    /**
     * Constructor of class DatabaseEmail.
     * @param path the path to the database
     * @throws ExceptionSender if path is empty, null or if the datas are corrupted
     */
    public DatabaseEmail(String path) throws ExceptionSender{
        if(path == null || path.isEmpty()){
            throw new ExceptionSender("DatabaseEmail : the path is null");
        }
        try {
            this.db = new DataBase(path);
        } catch (ExceptionDataBase ex) {
            throw new ExceptionSender("DatabaseEmail : fail while initialization of the database" + ex.toString());
        }
        if(this.db == null){
            throw new ExceptionSender("DatabaseEmail : fail while initialization of the database");
        }
        emails = this.db.getTableByName("emails");
        if (emails == null) {
            throw new ExceptionSender("DatabaseEmail : impossible to create the email table");
        }
        Header h = emails.getHeader();
        if (h == null) {
            throw new ExceptionSender("DatabaseEmail : the list of emails  doesn't have a header");
        }
        if (!h.containsKey("email")) {
            throw new ExceptionSender("DatabaseEmail : the table of emails doesn't contain the \"email\" row");
        }
        if (!h.containsKey("request")) {
            throw new ExceptionSender("DatabaseEmail : the table of emails doesn't contain the \"request\" row");
        }
        try {
            emails.load(h);
        } catch (ExceptionDataBase ex) {
            throw new ExceptionSender("DatabaseEmail : impossible to load the list of emails");
        }
    }
    
    
    /**
     * Return the list of the keywords from the database.
     * @return the list of the keywords
     */
    public ArrayList<String> getKeywordsList() {
        ArrayList<String> keywordsList = new ArrayList<>();
        ArrayList<Row> rows = this.emails.getAll();
        Set<String> keywords = new TreeSet<>();
        for(int i = 0; i < rows.size(); i++) {
            keywords.add(rows.get(i).get("request"));
        }
        keywordsList.addAll(keywords);
        return keywordsList;
    }
    
    /**
     * Get the email addresses from the database associated to the keywords.
     * @param keywords the keywords that were used to find the email addresses.
     * @return the mail addresses that are linked to the keywords
     * @throws ExceptionSender if keywords is equal to null
     */
    public ArrayList<String> getMailAdressesByKeywords(ArrayList<String> keywords) throws ExceptionSender {
        
        if (keywords == null) {
            throw new ExceptionSender("DatabaseEmail : getMailAdressesByKeywords : keywords is equal to null");
        }
        // add mail addresses to the Set to avoid duplicates and then turn it to an array
        Set<String> mailAdresses = new TreeSet<String>();
        ArrayList<Row> rows = this.emails.getAll();
        for(int i = 0; i < keywords.size() ; i++){
            for(int j = 0; j < rows.size(); j++){
                if(rows.get(j).get("request").equals(keywords.get(i))){
                    mailAdresses.add(rows.get(j).get("email"));
                }
            }
        }
        ArrayList<String> mails = new ArrayList<>();
        mails.addAll(mailAdresses);
        return mails;
    }
    
    /**
     * Delete a mail address in the database.
     * @param mail a mail address
     * @throws ExceptionSender if the delete fails : if mail is equal to null or the database throws an exception 
     */
    public void deleteMailFromDatabase(String mail) throws ExceptionSender {
        if (mail == null) {
            throw new ExceptionSender("DatabaseEmail : deleteMailFromDatabase : keywords is equal to null");
        }
        if(mail.isEmpty()){
            return;
        }
        try {
            emails.delRow("email", mail);
        } catch (ExceptionDataBase ex) {
            throw new ExceptionSender("DatabaseEmail : deleteMailFromDatabase : an error occured while deleting");
        }
    }
    
    /**
     * Save the database.
     * @throws ExceptionSender if it fails : the database throws an exception.
     */
    public void saveDatabase() throws ExceptionSender{
        try {
            emails.save();
        } catch(ExceptionDataBase e){
            throw new ExceptionSender("DatabaseEmail : saveDatabase : an error occured while saving");
        }
    }
    
    /**
     * Get the number of mail addresses.
     * @return the number of mails
     */
    public int getNumberOfMails(){
        return emails.getAll().size();
    }
    
}
