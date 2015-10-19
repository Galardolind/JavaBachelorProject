
package MailRecuperator.CoreClasses;

import Database.CoreClasses.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This manager is used to add / get / remove key words on the system
 * Database.
 */
public class KeyWordManager {

    /**
     * @see InterfaceTable
     */
    private InterfaceTable t;

    /**
     * The constructor is used to create table or get it if she already exist on the db path.
     * If an error is found during initialisation, an exception is thrown.
     * 
     * @param db Is an Database object already created.
     * @throws ExceptionRecuperation If the program can't initialise the table black list or if the current table is malformed.
     */
    public KeyWordManager(DataBase db) throws ExceptionRecuperation {
        if (db == null) {
            throw new ExceptionRecuperation("KeyWordManager : database is null");
        }
        InterfaceTable table = db.getTableByName("keywords");
        if (table == null) {
            try {
                table = new TableJSON("keywords", db.getPathDataBase());
                Header h = new Header();
                h.addColumn("keyword", new Properties(Type.STRING, true));
                table.setHeader(h);
                db.addTable(table);
            } catch (ExceptionDataBase  ex) {
                throw new ExceptionRecuperation("KeyWordManager : " + ex);
            }
            this.t = table;
            return;

        }
        Header h = table.getHeader();
        if(h == null ){
            throw new ExceptionRecuperation("KeyWordManager : keywords haven't header ");
        }
        if (!h.containsKey("keyword")) {
            throw new ExceptionRecuperation("KeyWordManager : keywords should contains column \"keyword\" ");
        }
        if (!h.get("keyword").isIndex()) {
            throw new ExceptionRecuperation("KeyWordManager : the column \"keyword\" is not an index");
        }

        this.t = table;
        try {
            this.t.load(h);
        } catch (Exception ex) {
            throw new ExceptionRecuperation("KeywordManager : "+ex);
        }

    }

    /**
     * This method call Database InterfaceTable to get all key words.
     * 
     * @return List which contain key words from System Database.
     * @throws ExceptionRecuperation If the program can't load the table correctly.
     */
    public ArrayList<String> getKeyWordList() throws ExceptionRecuperation {
        try {
            t.load(t.getHeader());
        } catch (Exception ex) {
            throw new ExceptionRecuperation("KeywordManager : getKeyWordList : "+ex);
        }
        ArrayList<Row> rows = t.getAll();
        ArrayList<String> ses = new ArrayList<>();
        Iterator<Row> it = rows.iterator();
        while (it.hasNext()) {
            Row r = it.next();
            ses.add(r.get("keyword"));
        }
        return ses;
    }

    /**
     * Add key word to database.
     * 
     * @param keyword Contains the key word to be added.
     * @throws ExceptionRecuperation If keyword is null or empty.
     */
    public void addKeyWord(String keyword) throws ExceptionRecuperation {
        if (keyword == null) {
            throw new ExceptionRecuperation("KeyWordManager : addKeyWord : keyword is null ");
        }
        if (keyword.length() == 0) {
            throw new ExceptionRecuperation("KeyWordManager : addKeyWord : invalid keyword ");
        }
        try {
            Row r = new Row();
            r.addColumn("keyword", keyword);
            t.addRow(r);
            t.save();
        } catch (ExceptionDataBase ex) {
        }
    }

    /**
     * Delete one key word on keywords Database.
     * 
     * @param keyword Contains the word to be remove from database.
     * @throws ExceptionRecuperation If keyword is null or empty.
     */
    public void deleteKeyWord(String keyword) throws ExceptionRecuperation {
        if (keyword == null) {
            throw new ExceptionRecuperation("KeyWordManager : deleteKeyWord : keyword is null ");
        }
        if (keyword.length() == 0) {
            throw new ExceptionRecuperation("KeyWordManager : deleteKeyWord : invalid keyword ");
        }
        try {
            t.delRow("keyword", keyword);
            t.save();
        } catch (ExceptionDataBase ex) {
        }
    }

    /**
     * Delete All key words on keywords Database.
     * 
     */
    public void deleteAllKeyWord() {
        try {
            t.dropTable();
            t.save();
        } catch (ExceptionDataBase ex) {
        }
    }
}
