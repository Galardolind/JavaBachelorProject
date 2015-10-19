package Database.CoreClasses;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import org.json.*;

/**
 * Table au format d'enregistremement Json.
 *
 * 
 */
public class TableJSON extends AbstractTable {

    /**
     * suffix du fichier de sauvegarde de la table
     */
    private final static String FILE_SUFFIX = ".txt";

    public TableJSON(String tableName, String path) throws ExceptionDataBase {
        super(tableName, path, FILE_SUFFIX);
    }

    @Override
    public void load(Header header) throws ExceptionDataBase {
        if (header == null) {
            throw new ExceptionDataBase("TableJSON.load() : le header est null");
        }
        rows = new Rows();
        File f = new File(path + File.separator + tableName + FILE_SUFFIX);

        if (f.length() < 2) {
            head = null;
            setHeader(header);
            return;
        }

        if (!f.exists()) {
            try {
                if (!f.createNewFile()) {
                    throw new ExceptionDataBase("TableJSON.load() : impossible de creer le fichier");
                }
                head = null;
                setHeader(header);
                return;
            } catch (IOException e) {
                throw new ExceptionDataBase("TableJSON.load() : impossible de creer le fichier");
            }
        }
        head = null;
        setHeader(header);
        try (FileReader fr = new FileReader(f)) {
            JSONTokener tokener = new JSONTokener(fr);
            JSONArray array = (JSONArray) tokener.nextValue();
            int count = 0;
            for (int i = 0; i < array.length(); i++) {
                Row r = new Row();
                Iterator<Entry<String, Properties>> it = head.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, Properties> e = it.next();
                    if (!array.getJSONObject(i).has(e.getKey())) {
                        throw new ExceptionDataBase("TableJSON.load() : la colonne n'appartien pas au header");
                    }
                    String value = array.getJSONObject(i).getString(e.getKey());
                    if (e.getKey().equalsIgnoreCase("index")) {
                        int val = Integer.parseInt(value);
                        if (val > count) {
                            count = val;
                        }
                    }
                    if (e.getValue().validConstraint(value)) {
                        r.addColumn(e.getKey(), value);
                    } else {
                        fr.close();
                        throw new ExceptionDataBase("TableJSON.load() : le champs n'as pas les conraintes valide");
                    }

                }
                rows.put(r.get(getIndex()), r);
            }
            rows.setCount(++count);
        } catch (IOException ex) {
            throw new ExceptionDataBase("TableJSON.load() : erreur lecture du fichier");
        }
    }

    @Override
    public void save() throws ExceptionDataBase {
        if (rows == null) {
            throw new ExceptionDataBase("TableJSON.save() : rien a sauvegarder");
        }
        File f = new File(path + File.separator + tableName + FILE_SUFFIX);
        f.delete();
        try {
            if (!f.createNewFile()) {
                throw new ExceptionDataBase("TableJSON.save() : impossible de creer le fichier");
            }
        } catch (IOException ex) {
            throw new ExceptionDataBase("TableJSON.save() : impossible de creer le fichier");
        }
        try (FileWriter fw = new FileWriter(f)) {
            JSONWriter writer = new JSONWriter(fw);
            writer.array();

            for (Row l : rows.values()) {
                writer.object();
                for (Iterator<Entry<String, String>> it = l.entrySet().iterator(); it.hasNext();) {
                    Entry<String, String> field = it.next();
                    writer.key(field.getKey());
                    writer.value(field.getValue());

                }
                writer.endObject();
            }
            writer.endArray();
        } catch (IOException | JSONException ex) {
            throw new ExceptionDataBase("TableJSON.save() : erreur sur l'ecriture du fichier");
        }
    }

    @Override
    public Header getHeader() {
        return head;
    }

    @Override
    public String getName() {
        return tableName;
    }

    @Override
    public String getType() {
        return "json";
    }
}
