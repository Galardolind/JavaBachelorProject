package MailSender.CoreClasses;

import Database.CoreClasses.*;
import java.util.ArrayList;

/**
 * Class that filter the mail addresses.
 */
public class MailFilter {
    /**
     * The table where to put the filtered mail addresses.
     */
    private InterfaceTable table;
    
    /**
     * Constructor of the class MailFilter.
     * @param db the database
     * @throws ExceptionDataBase if the database is equal to null, 
     * the database can't be created or the datas are corrupted
     */
    public MailFilter(DataBase db) throws ExceptionDataBase {
        if(db == null ){
            throw new ExceptionDataBase("MailFilter : the database is equal to null");
        }
        this.table = db.getTableByName("mailFilter");
        if (this.table == null) {
            try {
                TableJSON newTableFilter = new TableJSON("mailFilter", db.getPathDataBase());
                Header headFilter = new Header();
                headFilter.addColumn("filter", new Properties(Type.STRING, true));
                newTableFilter.setHeader(headFilter);
                db.addTable(newTableFilter);
                this.table = db.getTableByName("mailFilter");
            } catch(ExceptionDataBase e){
                throw new ExceptionDataBase("MailFilter : impossible to create the MailFilter database. "+e);
            }
        } else {
            Header h = this.table.getHeader();
            if (h == null) {
                throw new ExceptionDataBase("MailFilter : the filter doesn't have a header");
            }
            if (!h.containsKey("filter")) {
                throw new ExceptionDataBase("MailFilter : the filter doesn't contain a \"filter\" row");
            }
        }
    }
    
    /**
     * Return the mails that have not been already mailed.
     * @param mails the list of mail to filter
     * @return mails the list after filter operation, only good mails are returned
     * @throws ExceptionDataBase if mails equals to null
     */
    public ArrayList<String> filterMailAlreadyMaild(ArrayList<String> mails) throws ExceptionDataBase{
        if(mails == null){
            throw new ExceptionDataBase("MailFilter : filterMailAlreadyMaild : mails is null");
        } else if (mails.isEmpty()){
            return mails;
        }
        ArrayList<String> mailFiltered = new ArrayList<String>();
        ArrayList<Row> mailList = new ArrayList<Row>();
        try {
            this.table.load(this.table.getHeader());
            if(this.table.getAll() == null){
                return mails;
            }
            mailList.addAll(this.table.getAll());
        } catch(Exception e){
        }
        for(int i = 0; i < mails.size(); i++){
            boolean test = true;
            for(int j = 0; j < mailList.size(); j++){
                if(mailList.get(j).containsValue(mails.get(i))){
                    test = false;
                }
            }
            if(test){
                mailFiltered.add(mails.get(i));
                this.addMailAlreadyMaildToFilter(mails.get(i));
            }
        }
        try {
            this.table.save();
        } catch(ExceptionDataBase e){
            
        }
        return mailFiltered;
    }
    
    /**
     * Add the mail addresses to the list of already mailed.
     * @param mail the mail already mailed
     * @return true if success, false otherwise.
     */
    private boolean addMailAlreadyMaildToFilter(String mail){
        try {
            Row row = new Row();
            row.addColumn("filter", mail);
            this.table.addRow(row);
            return true;
        } catch(ExceptionDataBase e){
            return false;
        }
    }
    
    /**
     * Reset the filter table.
     * @return true if success, false otherwise.
     */
    public boolean resetFilter(){
        try {
            this.table.dropTable();
            return true;
        } catch(ExceptionDataBase e){
            return false;
        }
    }
}