
package MailRecuperator.CoreClasses;

/**
 * This Object is used to get statistics and other informations.
 */
public class StateOfRecup {
    
    /**
     * Number of internet pages already visited.
     */
    int nbOfPages;
    /**
     * Number of mail adresses already found.
     */
    int nbOfEmails;
    /**
     * Status in Percent of current EmailRecuperation.
     */
    int percent;
    /**
     * true if the program can run, false otherwise.
     */
    boolean run;
    /**
     * true if the program need to be stoped, false otherwise.
     */
    boolean stop;

    /**
     * The constructor set all parameters to get the correct statistics.
     * 
     * @param nbOfPages Number of internet pages already visited.
     * @param nbOfEmails Number of mail adresses already found.
     * @param run true if the program can run, false otherwise.
     * @param stop true if the program need to be stoped, false otherwise.
     */
    public StateOfRecup(int nbOfPages, int nbOfEmails, boolean run, boolean stop) {
        this.nbOfPages = nbOfPages;
        this.nbOfEmails = nbOfEmails;
        this.run = run;
        this.stop = stop;
        this.percent = 0;
    }

    /**
     * 
     * @return return if the program is stopped or not.
     */
    public boolean isStop() {
        return stop;
    }

    /**
     * 
     * @return return if the program can run or not
     */
    public boolean isRun() {
        return run;
    }

    /**
     * 
     * @param stop true if the program need to be stoped, false otherwise.
     */
    public void setStop(boolean stop) {
        this.stop = stop;
    }

    /**
     * Automatically switch run status.
     */
    public void setRun() {
        this.run = !this.run;
    }

    /**
     * 
     * @param nbOfEmails Number of mail adresses already found.
     */
    public void setNbOfEmails(int nbOfEmails) {
        this.nbOfEmails = nbOfEmails;
    }

    /**
     * 
     * @param nbOfPages Number of internet pages already visited.
     */
    public void setNbOfPages(int nbOfPages) {
        this.nbOfPages = nbOfPages;
    }
    
    /**
     * 
     * @param percent Status in Percent of current EmailRecuperation.
     */
    public void setPercent(int percent){
        this.percent = percent;
    }
    
    /**
     * 
     * @return percent of current EmailRecuperation
     */
    public int getPercent(){
        return percent;
    }

    /**
     * 
     * @return Number of mail adresses already found.
     */
    public int getNbOfEmails() {
        return nbOfEmails;
    }

    /**
     * 
     * @return Number of internet pages already visited.
     */
    public int getNbOfPages() {
        return nbOfPages;
    }
    
    /**
     * 
     * @param nb Number of mail adresses found to add to the current number of mails.
     */
    public void incrementNbOfEmails(int nb){
        this.nbOfEmails = this.nbOfEmails + nb;
    }
    
    /**
     * 
     * @param nb Number of internet pages visited to add to the current number of pages.
     */
    public void incrementNbOfPages(int nb){
        this.nbOfPages = this.nbOfPages + nb;
    }
    
    /**
     * 
     * @param nb Number of percent done to add to the current one.
     */
    public void incrementNbOfPercent(int nb){
        this.percent = this.percent + nb;
    }
    
}
