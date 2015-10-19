package Database.CoreClasses;

import java.util.HashMap;
import java.util.Map;

/**
 * classe representant une ligne dans une table
 *
 * 
 */
public class Row extends HashMap<String, String> {

    public Row(Map<? extends String, ? extends String> m) {
        super(m);
    }

    public Row() {
    }

    /**
     * ajout d'une colonne dans une table
     *
     * @param key le nom de la colonne
     * @param value la valeur de la colonne
     * @return precendente valeur
     */
    public String addColumn(String key, String value) {
        return put(key, value);
    }
}
