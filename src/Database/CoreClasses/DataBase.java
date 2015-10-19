package Database.CoreClasses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Cette base de données est concu sur le modele relationnel, il est possible de
 * creer une BD en partant de rien ou bien en chargeant une deja present dans le
 * systeme.
 *
 *
 *
 * 
 */
public class DataBase {

    /**
     * chemin absolu ou relatif de la BD
     */
    private File pathDataBase;
    /**
     * nom de la base de données
     */
    private String nameDataBase;
    /**
     * repertoire de la BD
     */
    private File configFileDataBase = null;
    /**
     * tables de la BD
     */
    private HashMap<String, InterfaceTable> tables = new HashMap<>();
    /**
     * Extention du fichier config de la bd
     */
    private final static String SUFFIX_BDD_CONFIG_FILE = ".csv";

    /**
     * Initiatilaise les parametres de creation de la bd
     *
     * @param pathDataBase Repertoire de la bd
     * @throws ExceptionDataBase
     */
    public DataBase(String pathDataBase) throws ExceptionDataBase {
        if (pathDataBase == null) {
            throw new ExceptionDataBase("DataBase.Constructor() : le chemin passé en parametre est null");
        }
        this.pathDataBase = new File(pathDataBase);
        this.pathDataBase = new File(this.pathDataBase.getAbsolutePath());
        if (pathDataBase.length() == 0 || pathDataBase.equalsIgnoreCase("./")) {
            int lastindex = this.pathDataBase.getAbsolutePath().lastIndexOf("\\") + 1;
            this.nameDataBase = this.pathDataBase.getAbsolutePath().substring(lastindex);
        } else {
            this.nameDataBase = this.pathDataBase.getName();
        }

        if ((this.pathDataBase.exists() && this.pathDataBase.isDirectory()) || pathDataBase.length() == 0) {
            boolean configFileExist = false;
            for (File f : this.pathDataBase.listFiles()) {
                if (f.getName().equalsIgnoreCase(nameDataBase + ".csv")) {
                    configFileExist = true;
                }
            }
            if (!configFileExist) {
                this.create();

            } else {
                this.load();
            }

        } else {

            this.create();

        }
    }

    private DataBase() {
    }

    /**
     * Creé une BD vide dans le repertoire d'execution ou dans le repertoire
     * donnée l'or de la construction de l'object
     *
     * @return false si impossible de creer le repertoire de la bdd, fichier de
     * config ou si la BD est deja cree
     * @throws ExceptionDataBase
     */
    private void create() throws ExceptionDataBase {
        if (configFileDataBase != null) {
            throw new ExceptionDataBase("DataBase.create() : le configFileDataBase est null");
        }
        if (pathDataBase == null) {
            pathDataBase = new File(nameDataBase);
        }
        if (!pathDataBase.exists()) {
            if (!pathDataBase.mkdirs()) {
                throw new ExceptionDataBase("DataBase.create() : impossible de creer le directory a : " + pathDataBase.getAbsolutePath());
            }
        }
        configFileDataBase = new File(pathDataBase.getAbsolutePath() + File.separator + nameDataBase + SUFFIX_BDD_CONFIG_FILE);
        try {
            if (!configFileDataBase.createNewFile()) {
                throw new ExceptionDataBase("DataBase.create() : impossible de creer le fichier de configuration ");
            }
        } catch (IOException ex) {
            throw new ExceptionDataBase("DataBase.create() : impossible de creer le fichier de configuration ");
        }

    }

    /**
     * Ajoute une table a la BD
     *
     * @param nameTable le nom de la table
     * @param table la table
     * @throws ExceptionDataBase
     */
    public void addTable(InterfaceTable table) throws ExceptionDataBase {
        if (table == null) {
            throw new ExceptionDataBase("DataBase.addTable() : le champs passé en paramettre est null");
        }
        this.tables.put(table.getName(), table);
    }

    /**
     * Charge en memoire la BD a partir du fichier de configuration charge aussi
     * les differentes tables, peut aussi servir pour ajouter des BDs a la BD
     * courante mais instable
     *
     * @param pathBdd repertoire de la BD a charger
     * @return return false si le fichier de config est inexistant ou si il est
     * mal formaté
     * @throws ExceptionDataBase
     */
    private void load() throws ExceptionDataBase {
        configFileDataBase = new File(this.pathDataBase.getAbsolutePath() + File.separator + nameDataBase + SUFFIX_BDD_CONFIG_FILE);
        if (!configFileDataBase.isFile()) {
            throw new ExceptionDataBase("DataBase.load() : le fichier de configuration n'est pas un fichier");
        }
        try (InputStreamReader ips = new FileReader(configFileDataBase)) {
            BufferedReader handleReader = new BufferedReader(ips);
            String line;
            while ((line = handleReader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ";");
                InterfaceTable t;
                Header h = new Header();
                String tableName = null, tableType = null;
                try {
                    tableName = st.nextToken();
                    tableType = st.nextToken();
                } catch (NoSuchElementException e) {
                    throw new ExceptionDataBase("DataBase.load() : le fichier de configuration est mal formate 1 : " + tableName + " " + tableType);
                }
                while (st.hasMoreTokens()) {
                    String fieldName, fieldType, fieldIndex, fieldForeignKey;
                    try {
                        fieldName = st.nextToken();
                        fieldType = st.nextToken();
                        fieldIndex = st.nextToken();
                        fieldForeignKey = st.nextToken();
                    } catch (NoSuchElementException e) {
                        throw new ExceptionDataBase("DataBase.load() : le fichier de configuration est mal formate 2");
                    }
                    if (fieldName == null || fieldType == null || fieldIndex == null || fieldForeignKey == null) {
                        throw new ExceptionDataBase("DataBase.load() : fieldName ou fieldType ou fieldIndex ou fieldForeignKey est null");
                    }
                    Properties p;
                    if (fieldForeignKey.equalsIgnoreCase("null")) {
                        p = new Properties(Type.valueOf(fieldType),
                                Boolean.valueOf(fieldIndex));

                    } else {
                        p = new Properties(Type.valueOf(fieldType),
                                Boolean.valueOf(fieldIndex),
                                fieldForeignKey);
                    }
                    h.put(fieldName, p);
                }
                if (tableType.equalsIgnoreCase("json")) {
                    t = new TableJSON(tableName, this.pathDataBase.getAbsolutePath());
                } else {
                    throw new ExceptionDataBase("DataBase.load() : Aucun type de table precise");
                }
                t.load(h);
                tables.put(tableName, t);
            }
        } catch (IOException ex) {
            throw new ExceptionDataBase("DataBase.load() : " + ex.getMessage());
        }
    }

    /**
     * la fonction de sauvegarde creer un fichier de configuration pour la BD et
     * sauvegarde toutes les tables
     *
     *
     * @throws ExceptionDataBase
     */
    public void save() throws ExceptionDataBase {
        configFileDataBase.delete();
        try {
            if (!configFileDataBase.createNewFile()) {
                throw new ExceptionDataBase("DataBase.save() : impossible de creer le fichier ");
            }
        } catch (IOException ex) {
            throw new ExceptionDataBase("DataBase.save() : impossible de creer le fichier ");
        }
        try (FileWriter fw = new FileWriter(configFileDataBase); BufferedWriter bw = new BufferedWriter(fw)) {


            for (Entry<String, InterfaceTable> t : tables.entrySet()) {
                if (t.getValue().getHeader() == null) {
                    throw new ExceptionDataBase("DataBase.save() : Table sans entete");
                }
                bw.write(t.getKey() + ";" + t.getValue().getType() + ";");
                for (Entry<String, Properties> c : t.getValue().getHeader().entrySet()) {
                    bw.write(c.getKey() + ";" + c.getValue().getType() + ";" + c.getValue().isIndex() + ";" + c.getValue().getForeignKey() + ";");
                }
                bw.write("\n");
                t.getValue().save();
            }
        } catch (IOException ex) {
            throw new ExceptionDataBase("DataBase.save() : IOException : " + ex);
        }
    }

    /**
     * Renvoie la table souhaité
     *
     * @param Name nom de la table
     * @return si elle n'existe pas retourne null sinon retourne la table
     */
    public InterfaceTable getTableByName(String Name) {
        return tables.get(Name);
    }

    /**
     * renvoie le chemin absolue de la BD
     *
     * @return String
     */
    public String getPathDataBase() {
        return pathDataBase.getAbsolutePath();
    }
}
