package Database.CoreClasses;

import java.io.File;
import java.io.IOException;

/**
 * Command Permetant de creer une base de donnée ou une table.
 *
 * 
 */
public class Create extends AbstractCommand {

    public Create(Request request, DataBase db) {
        super(request, db);
    }
    /**
     * booleen debut de commande
     */
    private boolean createTable = false;
    /**
     * booleen de creation d'un champs
     */
    private boolean createField = false;
    /**
     * le nom du champs courant
     */
    private String currentName = null;
    /**
     * les proprietes du champs
     */
    private Properties currentProperties = null;
    /**
     * booleen champs avec index
     */
    private boolean currentTableHaveIndex = false;
    /**
     * table sur laquelle on applique la commande
     */
    private InterfaceTable currentTable = null;
    /**
     * le header cree suite a la commande
     */
    private Header currentHeader = null;

    private void init() {
        createTable = false;
        createField = false;
        currentName = null;
        currentProperties = null;
        currentTableHaveIndex = false;
        currentTable = null;
        currentHeader = null;
    }

    @Override
    public boolean initCommand() throws ExceptionDataBase {
        init();
        return true;
    }

    @Override
    public InterfaceCommand startCommand(Object title) throws ExceptionDataBase {
        if (createTable || currentTable != null || currentHeader != null) {
            return null;
        }
        createTable = true;
        currentTable = new TableJSON((String) title, pathBdd);
        currentHeader = new Header();
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
    public InterfaceCommand value(Object propertiesField) throws ExceptionDataBase {
        if (!createField || currentProperties != null) {
            return null;
        }
        if (((Properties) propertiesField).isIndex() && !currentTableHaveIndex) {
            currentTableHaveIndex = true;
        } else if (((Properties) propertiesField).isIndex() && currentTableHaveIndex) {
            return null;
        }
        currentProperties = (Properties) propertiesField;
        return this;
    }

    @Override
    public InterfaceCommand endField() throws ExceptionDataBase {
        if (!createField) {
            return null;
        }
        createField = false;
        currentHeader.put(currentName, currentProperties);
        return this;
    }

    @Override
    public InterfaceCommand endCommand() throws ExceptionDataBase {
        if (!createTable) {
            return null;
        }
        createTable = false;
        currentTable.setHeader(currentHeader);

        db.addTable(currentTable);
        init();
        return this;
    }

    public Create addBdd(String pathBddConfigFile) throws IOException, ExceptionDataBase {
        File f = new File(pathBddConfigFile);
        if (!f.isFile()) {
            return null;
        }

        DataBase db = new DataBase(f.getPath() + File.separator + "tmp");
        return this;
    }
    private String pathBdd;
}
