package MailSender.CoreClasses;

import MailSender.GUI.UserInterfacePopDialog;

/**
 * Class that do the complete process of the pop.
 */
public class PopCompleteProcess extends Thread{
    /**
     * A pop instance.
     */
    Pop pop;
    
    /**
     * The message parser.
     */
    PopMessageParser parser;
    
    /**
     * The port of the pop provider.
     */
    String port;
    
    /**
     * The pop provider.
     */
    String provider;
    
    /**
     * The user name to connect to the store.
     */
    String user;
    
    /**
     * The password to connect to the store.
     */
    String pass;
    
    /**
     * The user interface dialog for the log about the pop.
     */
    UserInterfacePopDialog dial;
    
    /**
     * The status of the pop.
     */
    PopStatus status;

    /**
     * Constructor of the class PopCompleteProcess.
     * @param provider the pop provider
     * @param port the port of the provider
     * @param user the user name to connect to the store
     * @param pass the password to connect to the store
     * @param dial the user interface dialog for the log
     */
    public PopCompleteProcess(String provider, String port, String user, String pass, UserInterfacePopDialog dial) {
        this.provider = provider;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.dial = dial;
    }
    
    /**
     * Set the status.
     * @param status 
     */
    public void setStatus(PopStatus status) {
        this.status = status;
    }

    /**
     * Overlay on the pop and the process list
     */
    @Override
    public void run() {
        try {
            // generate the pop and give the status.
            log("Initializing pop :\n" + user + "\n"); 
            pop = new Pop(provider, port, user, pass,false, dial );  //generate the pop
            pop.setStatus(status);
            log("acessing MailBox...\n");            
            dial.collectProgess.setIndeterminate(true);
            pop.run();
            log(status.getMessages().size() + " has been retrieved from the mailBox.\n");
            if(status.getMessages().size() > 0){
                log("Processing mails...\n");
                dial.collectProgess.setIndeterminate(false);
                dial.collectProgess.setMaximum(status.getMessages().size());
                parser = new PopMessageParser(dial);  
                parser.setStatus(status);
                parser.run();
                log(status.getMessages().size() + " mails has been processed.\n");
                log(status.getMail().size() + " mail adresses has been found.\n");
            }
            else {
                dial.collectProgess.setIndeterminate(false);
                dial.collectProgess.setMaximum(100);
                dial.collectProgess.setValue(100);
                log("Nothing to process.\n");
            }
            log("End.\n");
            dial.flushButton.setEnabled(true); //restore the access to the flush /add (no more competitive access)
            dial.collectAdd.setEnabled(true);
            dial.collectButton.setEnabled(true);
        }
        catch(Exception e){
            log("An Error has been caught during pop processing :\n");
            dial.flushButton.setEnabled(true); //restore the access to the flush /add (no more competitive access)
            dial.collectAdd.setEnabled(true);
            dial.collectButton.setEnabled(true);

        }
        dial.collectProgess.setIndeterminate(false);
        dial.collectProgess.setMaximum(100);
        dial.collectProgess.setValue(100);
    }
    
    /**
     * Log the message.
     * @param message the message to write
     */
    public void log(String message){
        dial.log(message);
    }
    
}
