package MailSender.CoreClasses;

import java.util.HashMap;

/**
 * Class that contains the settings of the mail sender.
 */
public class SimpleSettings {
    /**
     * The SMTP host.
     */
    public String smtpHost;
    
    /**
     * The SMTP host port.
     */
    public String smtpPort;
    
    /**
     * The user name.
     */
    public String user;
    
    /**
     * The password.
     */
    public String pass;
    
    /**
     * The minimal delay between each sending.
     */
    public int delay;
    
    /**
     * True if authentication is required, false otherwise.
     */
    public boolean auth;
    
    /**
     * Path to database.
     */
    public String path;
    
    /**
     * The sender of the mails.
     */
    public String from;
    
    /**
     * The host.
     */
    public String host;
    
    /**
     * Constructor of the class SimpleSettings.
     * @param smtpHost the smtp host
     * @param smtpPort the smtp port
     * @param user the user name
     * @param pass the password
     * @param delay the minimal delay between each sending.
     * @param auth true if authentication is required, false otherwise
     * @param from the recipient of the mails
     * @param host the host
     */
    public SimpleSettings(String smtpHost, String smtpPort, String user, String pass, int delay, boolean auth, String from, String host){
        this.smtpHost = (smtpHost == null) ? "" : smtpHost;
        this.smtpPort = (smtpPort == null) ? "" : smtpPort;
        this.user = (user == null) ? "" : user;
        this.pass = (pass == null) ? "" : pass;
        this.delay = delay;
        this.auth = auth;
        this.path = null;
        this.from = (from == null) ? "" : from;
        this.host = (host == null) ? "" : host;
    }
    
    /**
     * Constructor of the class SimpleSettings.
     * @param smtpHost the smtp host
     * @param smtpPort the smtp port
     * @param user the user name
     * @param pass the password
     * @param delay the minimal delay between each sending.
     * @param auth true if authentication is required, false otherwise
     * @param path the path to the database
     * @param from the recipient of the mails
     * @param host the host
     */
    public SimpleSettings(String smtpHost, String smtpPort, String user, String pass, int delay, boolean auth, String path, String from, String host){
        this.smtpHost = (smtpHost == null) ? "" : smtpHost;
        this.smtpPort = (smtpPort == null) ? "" : smtpPort;
        this.user = (user == null) ? "" : user;
        this.pass = (pass == null) ? "" : pass;
        this.delay = delay;
        this.auth = auth;
        this.path = (path == null) ? "" : path;
        this.from = (from == null) ? "" : from;
        this.host = (host == null) ? "" : host;
    }
    
    /**
     * Get the fields of the class and put them in a HashMap.
     * @return a HashMap of the fields
     */
    public HashMap<String, String> getFields() {
        HashMap<String, String> fields = new HashMap<String,String>();
        fields.put("smtpHost", this.smtpHost);
        fields.put("smtpPort", this.smtpPort);
        fields.put("user", this.user);
        fields.put("pass", this.pass);
        fields.put("delay", Integer.toString(this.delay));
        fields.put("auth", Boolean.toString(auth));
        fields.put("path", this.path);
        fields.put("from", this.from);
        fields.put("host", this.host);
        
        return fields;
    }
}
