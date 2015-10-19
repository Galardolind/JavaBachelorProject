package MailSender.CoreClasses;

import Database.CoreClasses.ExceptionDataBase;
import com.sun.mail.smtp.SMTPAddressFailedException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * The class that provides the mail sending service.
 */
public class Smtp extends Thread {

    /**
     * The number of smtp instances.
     */
    static int smtpInstanceNumber = 0;
    /**
     * The number of the instance.
     */
    int numberInstance;
    /**
     * The parameters for the application : smtp host name, smtp auth...
     */
    private Properties properties;
    /**
     * The session that handle the parameters of the application.
     */
    private Session session;
    /**
     * The simple settings for the SMTP.
     */
    private SimpleSettings settings;
    /**
     * The SMTP status.
     */
    private SmtpStatus status;
    /**
     * The current template.
     */
    private Template template;
    /**
     * The database handler, the link with the database.
     */
    public DatabaseHandler dbProg;
    /**
     * The array of the recipient.
     */
    private String[] to;
    /**
     * If false, filter the mail addresses already mailed.
     */
    public boolean overideFilter;
    /**
     * The array of the keywords.
     */
    private String[] keys;
    private boolean tls = false;

    /**
     * Constructor of class Smtp.
     *
     * @param settings simple settings for the SMTP
     * @param template the template to use
     * @param to the array of the recipient
     * @param keys the array of the keywords
     * @param filter if false, filter the mail addresses already mailed
     * @throws ExceptionSender if one of the parameters is equal to null
     */
    public Smtp(SimpleSettings settings, Template template, String[] to, String[] keys, boolean filter) throws ExceptionSender {
        if (settings == null) {
            throw new ExceptionSender("Smtp Error : settings must be not null");
        }
        if (template == null) {
            throw new ExceptionSender("Smtp Error : template must be not null");
        }
        if (to == null) {
            throw new ExceptionSender("Smtp Error : to must be not null");
        }
        if (keys == null) {
            throw new ExceptionSender("Smtp Error : keys must be not null");
        }

        this.settings = settings;
        this.template = template;
        this.to = to;
        this.keys = keys;
        this.overideFilter = filter;
        Smtp.smtpInstanceNumber++;
        this.numberInstance = Smtp.smtpInstanceNumber;
    }

    /**
     * Set the status.
     *
     * @param status
     */
    public void setStatus(SmtpStatus status) throws ExceptionSender {
        if (status == null) {
            throw new ExceptionSender("Smtp Error : status must be not null");
        }
        this.status = status;
    }

    /**
     * Set the environment from the simple settings.
     */
    public void setEnv() {
        javax.mail.Authenticator auth;
        this.properties = System.getProperties();
        this.properties.setProperty("mail.smtp.host", this.settings.smtpHost);
        if (!this.settings.auth) {
            this.properties.setProperty("mail.smtp.auth", "false");
            this.properties.setProperty("mail.smtp.starttls.enable", "false");
            this.properties.setProperty("mail.smtp.port", this.settings.smtpPort);
            auth = new javax.mail.Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return null;
                }
            };
        } else if (tls) {
            this.properties = System.getProperties();
            this.properties.setProperty("mail.smtp.auth", "true");
            this.properties.setProperty("mail.smtp.starttls.enable", "true");
            this.properties.setProperty("mail.smtp.host", this.settings.smtpHost);
            this.properties.setProperty("mail.smtp.port", this.settings.smtpPort);
            auth = new javax.mail.Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(settings.user, settings.pass);
                }
            };
        } else {
            this.properties = System.getProperties();
            this.properties.setProperty("mail.smtp.auth", "true");
            this.properties.setProperty("mail.smtp.host", this.settings.smtpHost);
            this.properties.put("mail.smtp.socketFactory.port", this.settings.smtpPort);
            this.properties.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            this.properties.put("mail.smtp.port", this.settings.smtpPort);
            auth = new javax.mail.Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(settings.user, settings.pass);
                }
            };
        }
        this.session = Session.getInstance(this.properties, auth);
    }

    /**
     * Get the settings for the SMTP.
     *
     * @return the simple settings
     */
    public SimpleSettings getSettings() {
        return settings;
    }

    /**
     * Get the template used for sending.
     *
     * @return the current template
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * Count the number of recipients to give the number of mails to send.
     *
     * @return the number of mails to send
     */
    public int getNbMailTotal() {
        return this.to.length;
    }

    /**
     * Get the Smtp status.
     *
     * @return the Smtp status
     */
    public SmtpStatus getStatus() {
        return status;
    }

    /**
     * Get the keywords.
     *
     * @return the array of the keywords.
     */
    public String[] getKeys() {
        return keys;
    }

    /**
     * Set the list of the filtered mails.
     *
     * @param dbProg the database handler
     * @throws ExceptionDataBase if dbProg is equal to null
     */
    public void setFilteredMailList(DatabaseHandler dbProg) throws ExceptionDataBase {
        if (dbProg == null) {
            throw new ExceptionDataBase("Smtp : setFilteredMailList : parameter null");
        }
        if (!overideFilter) {
            this.dbProg = dbProg;
            ArrayList<String> emailsList = new ArrayList<>();
            for (String email : to) {
                emailsList.add(email);
            }
            ArrayList<String> emailListFiltered = this.dbProg.getMailFilter().filterMailAlreadyMaild(emailsList);
            String[] temp = new String[emailListFiltered.size()];
            for (int i = 0; i < emailListFiltered.size(); i++) {
                temp[i] = emailListFiltered.get(i);
            }
            if (temp != null) {
                this.to = temp;
            }
        }

    }

    /**
     * Send the mail with the content of the template to the mail addresses to.
     *
     * @return the number of mails that have not been correctly sent.
     * @throws ExceptionSender if status is equal to null
     */
    public int sendToMultiWithTemplate() throws ExceptionSender {
        //verify that delay is parsable :
        if (this.status == null) {
            throw new ExceptionSender("status has not been initialized");
        }
        this.status.time = 0;
        int nbExcept = 0;
        for (int i = 0; i < this.to.length; i++) {
            this.status.startTime = System.currentTimeMillis();
            if (!status.isStop()) {
                if (status.isRun()) {
                    long timeLocal = System.currentTimeMillis();
                    try {
                        status.insertOutput("[" + (i + 1) + "] " + this.to[i] + "...");
                        //prepare the sending
                        MimeMessage message = new MimeMessage(this.session);
                        message.setFrom(new InternetAddress(this.settings.from));
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.to[i]));
                        message.setSubject(template.getObject());
                        message.setContent(template.getMessageFromMail(this.to[i], this.settings.from), "text/html");

                        //sending of the message
                        Transport.send(message);

                        //update of sending status
                        status.setProgress(i + 1);
                        status.setNbMailAct(i);

                        //wait delay
                        Smtp.sleep(this.settings.delay);
                        timeLocal = System.currentTimeMillis() - timeLocal;
                        status.setTime(status.getTime() + timeLocal);
                        this.status.current_time = timeLocal;
                        status.insertOutput("Done in " + timeLocal + "ms \n");
                        nbExcept = 0;
                    } catch (MessagingException mex) {
                        if (mex.getClass() == SendFailedException.class) {
                            status.insertOutput("Acces denied by the provider\n"
                                    + "Please check connection settings validity.\n");
                            return 10;
                        } else {
                            mex.printStackTrace();
                            if (System.currentTimeMillis() - timeLocal > 10000) {
                                status.insertOutput("TimeOut - ");
                            }
                            status.insertOutput("Messaging Error.\n");
                            //change to tls.
                            tls = !tls;
                            setEnv();
                            nbExcept++;
                            if (nbExcept < 2) {
                                status.insertOutput("switching SSL/TLS\n");
                                i = i - 1;
                            }
                            if (nbExcept > 4) {
                                status.insertOutput("Connections have failed too many times.\n"
                                        + "Please check connection settings validity.\n");
                                return 10; //4 tentatives echec
                            }
                        }

                    } catch (InterruptedException mex) {
                        mex.printStackTrace();
                        status.insertOutput("Interrupt Error.\n");
                    }
                } else {
                    while (!status.isRun()) {
                        try {
                            if (status.isStop()) {
                                return nbExcept;
                            }
                            this.status.current_time = 0;
                            this.sleep(10);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Smtp.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } else {
                break;
            }
        }
        return nbExcept;
    }

    /**
     * Run this thread.
     */
    @Override
    public void run() {
        if (this.dbProg == null && !overideFilter) {
            return;
        }
        setEnv();
        this.status.setCurrentTime(System.currentTimeMillis());
        int nbErrors;
        try {
            nbErrors = sendToMultiWithTemplate();
        } catch (ExceptionSender ex) {
            Logger.getLogger(Smtp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Return a String representation of this object.
     *
     * @return a String representation of this object
     */
    @Override
    public String toString() {
        if (isAlive()) {
            return "[" + numberInstance + "] Intance(Running...) " + to.length + " mails.";
        } else {
            return "[" + numberInstance + "] Intance(Pending) " + to.length + " mails.";
        }
    }
}