package Database.CoreClasses;

/**
 * Exception de la BD
 *
 * 
 */
public class ExceptionDataBase extends Exception {

    /**
     * creation du message d'exception
     *
     * @param message String
     */
    public ExceptionDataBase(String message) {
        super(message);
    }
}
