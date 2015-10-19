package MailSender.CoreClasses;

/**
 * The Exception sent when an error occured in this package.
 */
public class ExceptionSender extends Exception {
    int errorValue;
    
    /**
     * Constructor of the class ExceptionSender.
     * @param message the message to throw with this exception
     */
    public ExceptionSender(String message) {
        super(message);        
        errorValue = 0;
    }
    
    /**
     * Constructor of the class ExceptionSender.
     * @param message the message to throw with this exception
     * @param errorValue the value of the error
     */
    public ExceptionSender(String message, int errorValue){
        super(message);
        this.errorValue = errorValue;
    }
    
    /**
     * Get the value of the error.
     * @return the error value
     */
    public int getError(){
        return this.errorValue;
    }
}
