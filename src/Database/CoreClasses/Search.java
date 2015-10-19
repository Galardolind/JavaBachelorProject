package Database.CoreClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Cete commande recherche dans une table un champs spécifique.
 *
 * 
 */
public class Search extends AbstractCommand {

    public Search(Request request, DataBase db) {
        super(request, db);
    }
    /**
     * le nom de la table a appliquer la commande
     */
    private String currentTable = null;
    /**
     * option de la commande
     */
    private String option = null;
    /**
     * booleen debut de la commande
     */
    private boolean get = false;
    /**
     * booleen debut d'un champs
     */
    private boolean field = false;
    /**
     * le nom du champs courant
     */
    private String nameField = null;
    /**
     * liste des condition a appliquer sur le champs
     */
    private ArrayList<String> conditions = null;

    /**
     * renvoie le resultat de la recherche
     *
     * @return ArrayList<Row>
     */
    public ArrayList<Row> getResult() {
        if (!get) {
            return result;
        }
        return null;
    }

    private void init() {
        currentTable = null;
        option = null;
        get = false;
        field = false;
        nameField = null;
        conditions = null;
    }

    @Override
    public boolean initCommand() throws ExceptionDataBase {
        init();
        return true;
    }

    @Override
    public InterfaceCommand startCommand(Object currentTable) throws ExceptionDataBase {
        if (currentTable == null) {
            return null;
        }
        this.currentTable = (String) currentTable;
        result = null;
        get = true;
        return this;
    }

    public Search optionGet(String option) throws ExceptionDataBase {
        if (!get) {
            return null;
        }
        this.option = option;
        return this;
    }

    @Override
    public InterfaceCommand startField(Object arg) throws ExceptionDataBase {
        if (!get) {
            return null;
        }
        conditions = new ArrayList<>();
        field = true;
        return this;
    }

    @Override
    public InterfaceCommand name(Object nameField) throws ExceptionDataBase {
        if (!field) {
            return null;
        }
        this.nameField = (String) nameField;

        return this;
    }

    @Override
    public InterfaceCommand value(Object condition) throws ExceptionDataBase {
        if (!field) {
            return this;
        }
        this.conditions.add((String) condition);
        return this;
    }

    @Override
    public InterfaceCommand endField() throws ExceptionDataBase {
        if (!field) {
            return this;
        }
        ArrayList<Row> tmpResult = new ArrayList<>();
        if (result == null) {
            tmpResult = db.getTableByName(currentTable).getRows(nameField, conditions.get(0));
        } else {
            Iterator<Row> it = result.iterator();
            while (it.hasNext()) {
                Row l = it.next();
                if (l.get(nameField).equalsIgnoreCase(conditions.get(0))) {
                    tmpResult.add(l);
                }
            }
        }
        result = tmpResult;
        field = false;
        return this;
    }

    @Override
    public InterfaceCommand endCommand() {
        if (!get) {
            return this;
        }
        get = false;
        if (option.equalsIgnoreCase("--sort")) {
            SortByValue sort = new SortByValue(db.getTableByName(currentTable).getIndex());
            Collections.sort(result, sort);
        } else if (option.equalsIgnoreCase("--count")) {
            Row l = new Row();
            l.put("count", String.valueOf(result.size()));
            result.add(l);
        }
        init();
        return this;

    }
}
