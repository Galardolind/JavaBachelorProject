package MailSender.CoreClasses;

import MailSender.GUI.UserInterfacePopDialog;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

/**
 * This class provides a pop access to a mailBox designed for it. This class is a
 * entry mirror of smtp system.
 */
public class Pop {

    /**
     * The properties of the pop.
     */
    Properties props;
    
    /**
     * The session of the pop.
     */
    Session session;
    
    /**
     * The store of the pop.
     */
    Store store;
    
    /**
     * The folder "inbox" in the store.
     */
    Folder inbox;
    
    /**
     * Messages in the inbox.
     */
    Message[] messages;
    
    /**
     * The pop provider.
     */
    String provider;
    
    /**
     * The port of the provider.
     */
    String port;
    
    /**
     * The user name to connect to the store.
     */
    String user;
    
    /**
     * The password to connect to the store.
     */
    String pass;
    
    /**
     * True if the thread has to wait 10 seconds and stop
     */
    boolean test;
    
    /**
     * The pop status.
     */
    PopStatus status;
    
    /**
     * The user interface for the pop dialog. Only used to write the log.
     */
    UserInterfacePopDialog dial;

    /**
     * Constructor of the class Pop.
     * @param provider the pop provider
     * @param port the port of the provider
     * @param user the user name to connect to the store
     * @param pass the password to connect to the store
     * @param test if true the thread will wait 10 seconds and stop
     * @param dial the dialog for the log
     */
    public Pop(String provider, String port, String user, String pass, boolean test, UserInterfacePopDialog dial) throws ExceptionSender {
        if(!(provider != null && !provider.isEmpty())) throw new ExceptionSender("Pop Error : provider must be not null");
        if(!(port != null && !port.isEmpty())) throw new ExceptionSender("Pop Error : port must be not null");
        if(!(user != null && !user.isEmpty())) throw new ExceptionSender("Pop Error : user must be not null");
        if(pass == null) throw new ExceptionSender("Pop Error : pass must be not null");
        if(dial == null) throw new ExceptionSender("Pop Error : dial must be not null");
        
        try {
            Integer tester = Integer.parseInt(port);
            if(tester < 0 || tester > 65635){
                throw new ExceptionSender("Pop Error : port must be into 0 and 65635");
            }
        } catch(NumberFormatException e){
            throw new ExceptionSender("Pop Error : port must be an Integer");
        }
        
        this.dial = dial;
        this.provider = provider;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.test = test;

        props = new Properties();

        props.put("mail.host", provider);
        props.put("mail.store.protocol", "pop3s");
        props.put("mail.pop3s.auth", "true");
        props.put("mail.pop3s.port", port);

        session = Session.getInstance(props, null);
    }

    /**
     * Set the status of this.
     * @param status 
     */
    public void setStatus(PopStatus status) throws ExceptionSender {
        if(status == null) throw new ExceptionSender("Pop Error : status must be not null");
        this.status = status;
    }

    /**
     * Connect to take all the mails and delete in the store and parse them.
     */
    public void run() {
        if (test) {
            try {
                Thread.sleep(10000);
                return;
            } catch (InterruptedException ex) {
                Logger.getLogger(Pop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            store = session.getStore();
            store.connect(user, pass);

            Folder folder = store.getFolder("inbox");

            if (!folder.exists()) {
                System.out.println("No INBOX...");
                System.exit(0);
            }
            folder.open(Folder.READ_WRITE);
            Message[] msg = folder.getMessages();


            for (int i = 0; i < msg.length; i++) {
                MailSample itemMail = new MailSample();

                String from = InternetAddress.toString(msg[i].getFrom());
                if (from != null) {
                    itemMail.setFrom(from);
                }

                String replyTo = InternetAddress.toString(
                        msg[i].getReplyTo());
                if (replyTo != null) {
                    itemMail.setReplyTo(replyTo);
                }
                String to = InternetAddress.toString(
                        msg[i].getRecipients(Message.RecipientType.TO));
                if (to != null) {
                    itemMail.setTo(to);
                }

                String subject = msg[i].getSubject();
                if (subject != null) {
                    itemMail.setSubject(subject);
                }
                Date sent = msg[i].getSentDate();
                if (sent != null) {
                    itemMail.setDate(sent);
                }
                String result = "";
                try {
                    Multipart multipart = (Multipart) msg[i].getContent();


                    for (int x = 0; x < multipart.getCount(); x++) {
                        BodyPart bodyPart = multipart.getBodyPart(x);

                        String disposition = bodyPart.getDisposition();

                        if (disposition != null && (disposition.equals(BodyPart.ATTACHMENT))) {
                        } else {
                            result += " " + bodyPart.getContent();
                        }
                    }
                } catch (Exception e) {
                    result = (String) msg[i].getContent();
                }

                itemMail.setContent(result);
                msg[i].setFlag(Flags.Flag.DELETED, true);
                this.status.addMessage(itemMail);
            }
            folder.close(true);
            store.close();

        } catch (IOException ex) {
            dial.log("IO Error caught : " + ex.getStackTrace()[0] + "\n");
        } catch (MessagingException ex) {
            dial.log("Messaging Error caught : " + ex.getStackTrace()[0] + "\n");
        }
    }
}
