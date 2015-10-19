package Database.CoreClasses;

import java.util.HashMap;

/**
 * classe contenant l'entete d'une table de la BD
 *
 * 
 */
public class Header extends HashMap<String, Properties> {

    /**
     * ajoute une colonne dans la table
     *
     * @param key le nom de la colonnes
     * @param value la valeur de la colonne
     * @return precedente valeur
     */
    public Properties addColumn(String key, Properties value) {
        return put(key, value);
    }
}
