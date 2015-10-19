package MailSender.CoreClasses;

import java.util.ArrayList;

/**
 * Class that represent the status of the pop.
 */
public class PopStatus {
    /**
     * The list of messages.
     */
    ArrayList<MailSample> messages;
    
    /**
     * The list of mails.
     */
    ArrayList<String> mail;
    
    /**
     * Constructor of class PopStatus.
     */
    public PopStatus(){
        messages = new ArrayList<>();
        mail = new ArrayList<>();
    }

    /**
     * Get the messages.
     * @return the list of messages
     */
    public ArrayList<MailSample> getMessages() {
        return messages;
    }

    /**
     * Get the mails.
     * @return the list of mails
     */
    public ArrayList<String> getMail() {
        return mail;
    }
    
    /**
     * Clear the list of mails.
     */
    public void clearMail(){
        this.mail.clear();
    }
    
    public void addMessage(MailSample msg){
        this.messages.add(msg);
    }
    
    public void addMail(String mail){
        this.mail.add(mail);
    }
}
