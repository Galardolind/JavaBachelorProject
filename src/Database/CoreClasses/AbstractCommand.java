package Database.CoreClasses;

import java.util.ArrayList;

/**
 * Classe abstraite d'une commande
 *
 * @
 * 
 */
public abstract class AbstractCommand implements InterfaceCommand {

    /**
     * Requete lié a l'interpreteur
     */
    protected Request request;
    /**
     * liste des lignes de resultat
     */
    protected ArrayList<Row> result = null;
    /**
     * la BD sur laquelle on applique la requete
     */
    protected DataBase db;

    /**
     * Initialise les parametres nessessaire
     *
     * @param request la quete a executé
     * @param db la BD sur laquelle on effectue la requete
     */
    public AbstractCommand(Request request, DataBase db) {
        this.request = request;
        this.db = db;
    }
}
