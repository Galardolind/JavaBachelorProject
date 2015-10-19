
package MailRecuperator.CoreClasses;

import Database.CoreClasses.*;
import java.util.ArrayList;

/**
 * Class which manage url to be filtered.
 */
public class UrlFilter {
    
    /**
     * @see InterfaceTable
     */
    private InterfaceTable tableFilter;
    
    /**
     * The constructor is used to create table or get it if she already exist on the db path.
     * If an error is found during initialisation, an exception is thrown.
     * 
     * @param db Is an Database object already created.
     * @throws ExceptionRecuperation If the program can't initialise the table urlFilter or if the current table is malformed.
     */
    public UrlFilter(DataBase db) throws ExceptionRecuperation {
        if (tableFilter != null ) {
            
        } else {
            if(db == null ){
                throw new ExceptionRecuperation("UrlFilter : database is null");
            }
            this.tableFilter = db.getTableByName("urlFilter");
            if (this.tableFilter == null) {
                try {
                    TableJSON newTableFilter = new TableJSON("urlFilter", db.getPathDataBase());
                    Header headFilter = new Header();
                    headFilter.addColumn("filter", new Properties(Type.STRING, true));
                    newTableFilter.setHeader(headFilter);
                    db.addTable(newTableFilter);
                    this.tableFilter = db.getTableByName("urlFilter");
                } catch(ExceptionDataBase e){
                    throw new ExceptionRecuperation("UrlFilter : impossible to create UrlFilter database :"+e);
                }
            } else {
                Header h = this.tableFilter.getHeader();
                if (h == null) {
                    throw new ExceptionRecuperation("UrlFilter : filter haven't header ");
                }
                if (!h.containsKey("filter")) {
                    throw new ExceptionRecuperation("UrlFilter : filter should contains column \"filter\" ");
                }
                try {
                    this.tableFilter.load(h);
                } catch(ExceptionDataBase e){
                    
                }
            }
        }
        
    }
    
    /**
     * This method is call with url list to filter them, after that it automatically add
     * to the database remaining urls, and to finish save the new database.
     * 
     * @param urls is list of url to filter.
     * @return url after filter operation only good url is returned.
     * @throws ExceptionRecuperation If urls is null.
     */
    public ArrayList<String> filterUrlAlreadyVisited(ArrayList<String> urls) throws ExceptionRecuperation{
        if(urls == null){
            throw new ExceptionRecuperation("UrlFilter : filterUrlAlreadyVisited : urls is null");
        } else if (urls.isEmpty()){
            return urls;
        }
        ArrayList<String> urlFiltered = new ArrayList<String>();
        ArrayList<Row> urlList = new ArrayList<Row>();
        try {
            this.tableFilter.load(this.tableFilter.getHeader());
            if(this.tableFilter.getAll() == null){
                return urls;
            }
            urlList.addAll(this.tableFilter.getAll());
        } catch(Exception e){
        }
        for(int i = 0; i < urls.size(); i++){
            boolean test = true;
            for(int j = 0; j < urlList.size(); j++){
                if(urlList.get(j).containsValue(urls.get(i))){
                    test = false;
                }
            }
            if(test){
                urlFiltered.add(urls.get(i));
                addUrlVisitedToFilter(urls.get(i));
            }
        }
        try {
            tableFilter.save();
        } catch(ExceptionDataBase e){
            
        }
        return urlFiltered;
    }
    
    /**
     * Add url to Database (didn't save after).
     * @param url Url which need to be add in database.
     */
    private void addUrlVisitedToFilter(String url){
        try {
            Row roww = new Row();
            roww.addColumn("filter", url);
            tableFilter.addRow(roww);
        } catch(ExceptionDataBase e){
            
        }
    }
    
    /**
     * Clear the filter.
     */
    public void resetFilter(){
        try {
            tableFilter.dropTable();
            tableFilter.save();
        } catch(ExceptionDataBase e){
            
        }
    }
    
}
