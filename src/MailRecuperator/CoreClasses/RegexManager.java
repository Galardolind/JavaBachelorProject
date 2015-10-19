
package MailRecuperator.CoreClasses;

import Database.CoreClasses.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * This manager is used to add / get / remove regexs on the system
 * Database.
 */
public class RegexManager {

    /**
     * @see InterfaceTable
     */
    private InterfaceTable t;

    /**
     * The constructor is used to create table or get it if she already exist on the db path.
     * If an error is found during initialisation, an exception is thrown.
     * 
     * @param db Is an Database object already created.
     * @throws ExceptionRecuperation If the program can't initialise the table regexs or if the current table is malformed.
     */
    public RegexManager(DataBase db) throws ExceptionRecuperation {
        if (db == null) {
            throw new ExceptionRecuperation("KeyWordManager : db is null");
        }
        InterfaceTable table = db.getTableByName("regexs");
        if (table == null) {
            try {
                table = new TableJSON("regexs", db.getPathDataBase());
                Header h = new Header();
                h.addColumn("regex", new Properties(Type.STRING, true));
                table.setHeader(h);
                db.addTable(table);
            } catch (ExceptionDataBase  ex) {
                throw new ExceptionRecuperation("RegexManager : " + ex);
            }
            this.t = table;
            return;

        }
        Header h = table.getHeader();
        if(h == null ){
            throw new ExceptionRecuperation("RegexManager : regexs haven't header ");
        }
        if (!h.containsKey("regex")) {
            throw new ExceptionRecuperation("RegexManager : regexs should contains column \"keyword\"");
        }
        if (!h.get("regex").isIndex()) {
            throw new ExceptionRecuperation("RegexManager : the column \"regex\" is not an index");
        }

        this.t = table;
        try {
            this.t.load(h);
        } catch (Exception ex) {
            throw new ExceptionRecuperation("RegexManager : "+ex);
        }

    }

    /**
     * This method call Database InterfaceTable to get all regexs.
     * 
     * @return List which contain regex from System Database.
     * @throws ExceptionRecuperation If the program can't load the table correctly.
     */
    public ArrayList<String> getRegexList() throws ExceptionRecuperation {
        try {
            t.load(t.getHeader());
        } catch (Exception ex) {
            throw new ExceptionRecuperation("RegexManager : getRegexList : "+ex);
        }
        ArrayList<Row> rows = t.getAll();
        ArrayList<String> ses = new ArrayList<>();
        Iterator<Row> it = rows.iterator();
        while (it.hasNext()) {
            Row r = it.next();
            ses.add(r.get("regex"));
        }
        return ses;
    }

    /**
     * Add regex to database.
     * 
     * @param regex Contains the regex to be added.
     * @throws ExceptionRecuperation If regex is null or empty.
     */
    public boolean addRegex(String regex) throws ExceptionRecuperation {
        if (regex == null) {
            throw new ExceptionRecuperation("RegexManager : addRegex : regex is null");
        }
        if (regex.length() == 0) {
            throw new ExceptionRecuperation("RegexManager : addRegex : invalid regex");
        }
        try {
                Pattern emailRegex = Pattern.compile(regex);
        } catch(Exception e){
            throw new ExceptionRecuperation("RegexManager : addRegex : invalid regex");
        }
        try {
            Row r = new Row();
            r.addColumn("regex", regex);
            t.addRow(r);
            t.save();
        } catch (ExceptionDataBase ex) {
        }
        return true;
    }

    /**
     * Delete one regex on regex Database.
     * 
     * @param regex Contains the regex to be remove from database.
     * @throws ExceptionRecuperation If regex is null or empty.
     */
    public void deleteRegex(String regex) throws ExceptionRecuperation {
        if (regex == null) {
            throw new ExceptionRecuperation("RegexManager : deleteRegex : regex is null");
        }
        if (regex.length() == 0) {
            throw new ExceptionRecuperation("RegexManager : deleteRegex : invalid regex");
        }
        try {
            t.delRow("regex", regex);
            t.save();
        } catch (ExceptionDataBase ex) {
        }
    }

    /**
     * Delete All regexs on regex Database.
     * 
     */
    public void deleteAllRegex() {
        try {
            t.dropTable();
            t.save();
        } catch (ExceptionDataBase ex) {
        }
    }
}
