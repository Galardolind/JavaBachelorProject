package Database.CoreClasses;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * 
 */
public abstract class AbstractTable implements InterfaceTable {

    protected Rows rows;
    protected Header head;
    protected String tableName;
    protected String path;
    private String index = null;

    /**
     * Construit l'object
     *
     * @param tableName le nom de la table
     * @param path le repertoire de la table
     * @param suffix le suffix de la table
     */
    public AbstractTable(String tableName, String path, String suffix) throws ExceptionDataBase {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
            if (!f.isDirectory()) {
                throw new ExceptionDataBase("TableJSON.TableJSON() : le chemin est incorrect");
            }
        }
        this.tableName = tableName;
        this.path = path;
    }

    private AbstractTable() {
    }

    @Override
    public void setHeader(Header header) throws ExceptionDataBase {
        if (head != null || header == null || header.size() < 1) {
            throw new ExceptionDataBase("AbstractTable.setHeader() : le header est deja defini ou celui en parametre est incorrect");
        }

        Iterator<Entry<String, Properties>> it = header.entrySet().iterator();
        boolean haveIndex = false;
        while (it.hasNext()) {
            Entry<String, Properties> field = it.next();
            if (field.getValue().isIndex()) {
                this.index = field.getKey();
                if (haveIndex) {
                    throw new ExceptionDataBase("AbstractTable.setHeader() : il y a plusieurs index dans l'entete");
                }
                haveIndex = true;
            }
        }
        if (!haveIndex) {
            this.index = "index";
            header.addColumn("index", new Properties(Type.INTEGER, true));
        } else {
            header.addColumn("index", new Properties(Type.INTEGER));
        }
        rows = new Rows();

        head = header;

    }

    @Override
    public void dropTable() throws ExceptionDataBase {
        rows = new Rows();
        save();
    }

    @Override
    public String getIndex() {
        return index;
    }

    @Override
    public ArrayList<Row> getAll() {
        return new ArrayList<>(this.rows.values());
    }

    @Override
    public ArrayList<Row> getRows(String key, String value) {
        ArrayList<Row> r = new ArrayList<>();
        if (key == null || value == null || rows == null) {
            return r;
        }
        if (key.equalsIgnoreCase(getIndex())) {
            Row row = getRowByIndex(value);
            if (row != null) {
                r.add(row);
            }
            return r;
        }

        for (Row f : this.rows.values()) {
            String s = f.get(key);
            if (s == null) {
                return r;
            }
            if (s.equalsIgnoreCase(value)) {
                r.add(f);
            }
        }
        return r;
    }

    @Override
    public Row getRowByIndex(String value) {
        return rows.get(value);
    }

    @Override
    public void addRow(Row row) throws ExceptionDataBase {
        if (head == null) {
            this.save();

            throw new ExceptionDataBase("AbstractTable.addRow() : le header n'est pas defini ");
        }
        if (rows == null || row.size() != head.size() - 1) {
            this.save();


            throw new ExceptionDataBase("AbstractTable.addRow() : la ligne est incorrect");
        }

        for (Entry<String, Properties> field : head.entrySet()) {
            if (field.getKey().equalsIgnoreCase("index")) {
                continue;
            }
            if (!row.containsKey(field.getKey())) {

                this.save();

                throw new ExceptionDataBase("AbstractTable.addRow() : la ligne ne contient pas toutes les colonnes de l'entete");
            }
            String testField = row.get(field.getKey());
            if (!field.getValue().validConstraint(testField)) {

                this.save();

                throw new ExceptionDataBase("AbstratcTable.addRow() : le champs n'a pas de contrainte valide");
            }
        }
        Row r = new Row(row);


        rows.addRow(r);
    }

    @Override
    public void delRow(String key, String value) throws ExceptionDataBase {
        if (!head.containsKey(key)) {
            this.save();

            throw new ExceptionDataBase("AbstractTable.delRows():la key est invalide ");
        }

        if (key.equalsIgnoreCase(index)) {
            rows.remove(value);
            return;
        }

        Collection<Row> r = rows.values();

        for (Iterator<Row> it = r.iterator(); it.hasNext();) {
            if (it.next().get(key).equalsIgnoreCase(value)) {
                it.remove();
            }
        }

    }

    @Override
    public String toString() {
        return this.tableName;


    }

    public class Rows extends HashMap<String, Row> implements InterfaceTable.Rows {

        private int count = 0;

        @Override
        public Row addRow(Row r) {
            String value = "";
            for (String key : r.keySet()) {
                if (getIndex().equalsIgnoreCase(key)) {
                    value = r.get(key);
                    break;
                }
            }

            if (this.containsKey(value) || r.containsKey("index")) {
                return null;
            }
            if (value.length() == 0) {
                value = String.valueOf(count);
            }
            r.addColumn("index", String.valueOf(count));
            count++;
            return this.put(value, r);
        }

        /**
         * modifier le numero de du dernier element de la table
         *
         * @param count numero du dernier element de la table
         */
        public void setCount(int count) {
            this.count = count;
        }
    }
}
