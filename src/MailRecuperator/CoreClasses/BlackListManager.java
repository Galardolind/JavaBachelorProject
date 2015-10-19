
package MailRecuperator.CoreClasses;

import Database.CoreClasses.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This manager is used to add / get / remove black list domain on the system
 * Database.
 */
public class BlackListManager {
    
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
    public BlackListManager(DataBase db) throws ExceptionRecuperation {
        if (db == null) {
            throw new ExceptionRecuperation("BlackListManager : constructeur : database is null");
        }
        InterfaceTable table = db.getTableByName("black list");
        if (table == null) {
            try {
                table = new TableJSON("black list", db.getPathDataBase());
                Header h = new Header();
                h.addColumn("domain", new Properties(Type.STRING, true));
                table.setHeader(h);
                db.addTable(table);
            } catch (ExceptionDataBase  ex) {
                throw new ExceptionRecuperation("BlackListManager : " + ex);
            }
            this.t = table;
            return;

        }
        Header h = table.getHeader();
        if(h == null ){
            throw new ExceptionRecuperation("BlackListManager : black list haven't header");
        }
        if (!h.containsKey("domain")) {
            throw new ExceptionRecuperation("BlackListManager : black list should contains column \"domain\"");
        }
        if (!h.get("domain").isIndex()) {
            throw new ExceptionRecuperation("BlackListManager : column \"domain\" in not an index");
        }

        this.t = table;
        try {
            this.t.load(t.getHeader());
        } catch (Exception ex) {
            throw new ExceptionRecuperation("BlackListManager : "+ex);
        }

    }

    /**
     * This method call Database InterfaceTable to get all blacklisted domain.
     * 
     * @return List which contain black listed words from System Database.
     * @throws ExceptionRecuperation If the program can't load the table correctly.
     */
    public ArrayList<String> getBlackListedDomain() throws ExceptionRecuperation {
        try {
            this.t.load(t.getHeader());
        } catch (Exception ex) {
            throw new ExceptionRecuperation("BlackListManager : getBlackListedDomain : "+ex);
        }
        ArrayList<Row> rows = t.getAll();
        ArrayList<String> ses = new ArrayList<>();
        Iterator<Row> it = rows.iterator();
        while (it.hasNext()) {
            Row r = it.next();
            ses.add(r.get("domain"));
        }
        return ses;
    }

    /**
     * Add domain to database.
     * 
     * @param domain Contains the domain to be added.
     * @throws ExceptionRecuperation If domain is null or empty.
     */
    public void addBlackListedDomain(String domain) throws ExceptionRecuperation {
        if (domain == null) {
            throw new ExceptionRecuperation("BlackListManager : addBlackListedDomain : domain is null");
        }
        if (domain.length() == 0) {
            throw new ExceptionRecuperation("BlackListManager : addBlackListedDomain : invalid parameters");
        }
        try {
            Row r = new Row();
            r.addColumn("domain", domain);
            t.addRow(r);
            t.save();
        } catch (ExceptionDataBase ex) {
        }
    }

    /**
     * Delete one domain on blacklist Database.
     * 
     * @param domain Contains the domain to be remove from database.
     * @throws ExceptionRecuperation If domain is null or empty.
     */
    public void deleteBlackListedDomain(String domain) throws ExceptionRecuperation {
        if (domain == null) {
            throw new ExceptionRecuperation("BlackListManager : deleteBlackListedDomain : domain is null");
        }
        if (domain.length() == 0) {
            throw new ExceptionRecuperation("BlackListManager : deleteBlackListedDomain : invalid parameters ");
        }
        try {
            t.delRow("domain", domain);
            t.save();
        } catch (ExceptionDataBase ex) {
        }
    }

    /**
     * Delete All domains on blacklist Database.
     * 
     */
    public void deleteAllBlackList() {
        try {
            t.dropTable();
            t.save();
        } catch (ExceptionDataBase ex) {
        }
    }
}
