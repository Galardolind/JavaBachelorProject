package Database.CoreClasses;

import java.util.Comparator;

/**
 * classe de tri par valeur
 *
 * @deprecated pas utilise
 * 
 */
public class SortByValue implements Comparator<Row> {

    /**
     * le nom de la colonnes a comparer
     */
    private String index;

    /**
     * initialise les parametres
     *
     * @param index le nom de la colonne a comparer
     */
    public SortByValue(String index) {
        this.index = index;
    }

    @Override
    public int compare(Row o1, Row o2) {
        return o1.get(index).compareTo(o2.get(index));
    }
}
