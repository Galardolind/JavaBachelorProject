package Database.CoreClasses;

/**
 * Cette Command permet d'ajouter, dans une table deja cree, une ligne dans la
 * table.
 *
 * 
 */
public class Add extends AbstractCommand {

    public Add(Request request, DataBase db) {
        super(request, db);
    }
    /**
     * booleen debut de commande
     */
    private boolean addToTable = false;
    /**
     * booleen de creation d'un champs
     */
    private boolean createField = false;
    /**
     * nom courant du champs
     */
    private String currentName = null;
    /**
     * valeur courant du champs
     */
    private String currentValue = null;
    /**
     * la table sur laquelle on applique la commande
     */
    private InterfaceTable currentTable = null;
    /**
     * ligne de la table a ajouter
     */
    private Row currentLine = null;

    private void init() {
        addToTable = false;
        createField = false;
        currentName = null;
        currentValue = null;
        currentTable = null;
        currentLine = null;
    }

    @Override
    public boolean initCommand() throws ExceptionDataBase {
        init();
        return true;
    }

    @Override
    public InterfaceCommand startCommand(Object tableName) throws ExceptionDataBase {
        if (addToTable || currentTable != null || currentLine != null) {
            return null;
        }
        addToTable = true;
        currentTable = db.getTableByName((String) tableName);
        currentLine = new Row();
        return this;
    }

    @Override
    public InterfaceCommand startField(Object arg) throws ExceptionDataBase {
        if (!createField) {
            return null;
        }
        createField = true;
        return this;
    }

    @Override
    public InterfaceCommand name(Object nameField) throws ExceptionDataBase {
        if (!createField || currentName != null) {
            return null;
        }
        if (((String) nameField).compareTo("index") == 0) {
            return null;
        }
        currentName = (String) nameField;
        return this;
    }

    @Override
    public InterfaceCommand value(Object value) throws ExceptionDataBase {
        if (!createField || currentValue != null) {
            return null;
        }
        currentValue = (String) value;
        return this;
    }

    @Override
    public InterfaceCommand endField() throws ExceptionDataBase {
        if (!createField) {
            return null;
        }
        createField = false;
        currentLine.put(currentName, currentValue);
        return this;
    }

    @Override
    public InterfaceCommand endCommand() throws ExceptionDataBase {
        if (!addToTable) {
            return null;
        }
        addToTable = false;
        currentTable.addRow(currentLine);

        init();
        return this;
    }
    private String pathBdd;
}
