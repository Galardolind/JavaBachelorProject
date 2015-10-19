
package MailRecuperator.CoreClasses;

import Database.CoreClasses.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is used to save email and request associated in Database.
 */
public class Email implements Comparable<Email> {

    /**
     * @see InterfaceTable
     */
    private static InterfaceTable blackList = null;
    /**
     * @see InterfaceTable
     */
    private static InterfaceTable emails = null;
    /**
     * Contains current mail to save.
     */
    private String email;
    /**
     * Contains request associated to mail.
     */
    private String request;

    /**
     * Verify if database is intialised before saving (the constructor didn't save
     * you need to call method to do that).
     * 
     * @param email mail adress to be saved.
     * @param request request associated to mail.
     * @throws ExceptionRecuperation If Email Database is not set or email/request is null/empty.
     */
    public Email(String email, String request) throws ExceptionRecuperation {
        if(blackList == null || emails == null){
            throw new ExceptionRecuperation("Email : BlackList or emails database is null");
        }
        if(email ==null || request == null){
            throw new ExceptionRecuperation("Email : parameters are null");
        }
        if(email.length()==0 ||  request.length()==0){
            throw new ExceptionRecuperation("Email : invalid parameters");
        }
        this.email = email;
        this.request = request;
    }

    /**
     * Permit to set Email Database in the program (static database).
     * 
     * @param dbChoosen Client Database.
     * @param dbProg System Database.
     * @throws ExceptionRecuperation If an exception is thrown from setDB method.
     */
    public Email(DataBase dbChoosen, DataBase dbProg) throws ExceptionRecuperation {
        Email.setDB(dbChoosen,dbProg);
    }

    /**
     * 
     * @return current mail adress.
     */
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @return current request.
     */
    public String getRequest() {
        return request;
    }

    /**
     * This method set Email Database to all EmailRecuperation (static InterfaceTable),
     * and verify if the Database is created, if not the program create a new one.
     * this method also handles blacklist, and do the same things with the email database.
     * 
     * @param dbChoosen Client Database.
     * @param dbProg System Database.
     * @throws ExceptionRecuperation If problem during creation or if Database is corrupted.
     */
    public static void setDB(DataBase dbChoosen, DataBase dbProg) throws ExceptionRecuperation{
        if(dbProg == null || dbChoosen == null ){
            throw new ExceptionRecuperation("Email : setDB : databases are null");
        }
        emails = dbChoosen.getTableByName("emails");
        if (emails == null) {
            try {
                TableJSON emailTable = new TableJSON("emails", dbChoosen.getPathDataBase());
                Header emailHead = new Header();
                emailHead.addColumn("email", new Properties(Type.STRING, true));
                emailHead.addColumn("request", new Properties(Type.STRING));
                emailTable.setHeader(emailHead);
                dbChoosen.addTable(emailTable);
                emails = dbChoosen.getTableByName("emails");
            } catch(ExceptionDataBase e){
                throw new ExceptionRecuperation("Email : setDB : impossible to create emails table ");
            }
        }
        Header h = emails.getHeader();
        if (h == null) {
            throw new ExceptionRecuperation("Email : setDB : emails haven't header ");
        }
        if (!h.containsKey("email")) {
            throw new ExceptionRecuperation("Email : setDB : emails should contains column \"email\" ");
        }
        if (!h.containsKey("request")) {
            throw new ExceptionRecuperation("Email : setDB : emails should contains column \"request\" ");
        }
        try {
            emails.load(h);
        } catch (ExceptionDataBase ex) {
            throw new ExceptionRecuperation("Email : setDB : impossible to load emails database ");
        }
        blackList = dbProg.getTableByName("black list");
        if (blackList == null) {
            try {
                TableJSON newTableFilter = new TableJSON("black list", dbProg.getPathDataBase());
                Header headFilter = new Header();
                headFilter.addColumn("domain", new Properties(Type.STRING, true));
                newTableFilter.setHeader(headFilter);
                dbProg.addTable(newTableFilter);
                blackList = dbProg.getTableByName("black list");
            } catch(ExceptionDataBase e){
                throw new ExceptionRecuperation("Email : setDB : impossible to create blackList table ");
            }
        }
        h = blackList.getHeader();
        if (h == null) {
            throw new ExceptionRecuperation("Email : setDB : blacklist haven't header ");
        }
        if (!h.containsKey("domain")) {
            throw new ExceptionRecuperation("Email : setDB : blacklist should contains column \"domain\" ");
        }
        try {
            blackList.load(h);
        } catch (ExceptionDataBase ex) {
            throw new ExceptionRecuperation("Email : setDB : impossible de load blacklist database ");
        }
        
    }
    
    
    /**
     * This method verify if the current mail do not contains blacklist content, if not
     * he directly save in the Email Table (dbChoosen) with the associated request.
     * 
     * @throws ExceptionRecuperation If emails or blacklist isn't initalised (null).
     */
    public void saveEmail() throws ExceptionRecuperation {
        if (emails == null || blackList == null) {
            throw new ExceptionRecuperation("Email : saveEmail : tables should been set ");
        }
        ArrayList<Row> rows = blackList.getAll();
        ArrayList<String> ses = new ArrayList<>();
        Iterator<Row> itr = rows.iterator();
        while (itr.hasNext()) {
            Row r = itr.next();
            ses.add(r.get("domain"));
        }
        for (String s : ses) {
            if (email.matches(".*"+s+".*")) {
                return;
            }
        }
        Row r = new Row();
        r.addColumn("email", email);
        r.addColumn("request", request);
        try {
            emails.addRow(r);
        } catch (ExceptionDataBase ex) {
        }

    }

    /**
     * 
     * @return email adress. 
     */
    @Override
    public String toString() {
        return email;
    }

    /**
     * This method only compare name, not request.
     * 
     * @param t email already created.
     * @return Comparaison between two mail adresses.
     */
    @Override
    public int compareTo(Email t) {
        return this.email.compareTo(t.getEmail());
    }
}
