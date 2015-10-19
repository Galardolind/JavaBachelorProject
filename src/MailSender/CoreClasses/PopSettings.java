
package MailSender.CoreClasses;

import java.util.HashMap;

/**
 * Class that handles the settings for the pop.
 */
public class PopSettings {

    /**
     * The pop provider.
     */
    public String popProvider;
    
    /**
     * The port of the pop provider.
     */
    public String popPort;
    
    /**
     * The user name to connect to the store.
     */
    public String user;
    
    /**
     * The password to connect to the store.
     */
    public String pass;
    
    /**
     * Constructor of class PopSettings.
     * @param popProvider the pop provider
     * @param popPort the port of the pop provider
     * @param user the user name to authenticate
     * @param pass the password to authenticate
     */
    public PopSettings(String popProvider, String popPort, String user, String pass){
        this.popProvider = (popProvider == null) ? "" : popProvider;
        this.popPort = (popPort == null) ? "" : popPort;
        this.user = (user == null) ? "" : user;
        this.pass = (pass == null) ? "" : pass;
    }
    
    /**
     * Get the fields of the class and put them in a HashMap.
     * @return a HashMap of the fields
     */
    public HashMap<String, String> getFields() {
        HashMap<String, String> fields = new HashMap<String,String>();
        fields.put("popProvider", this.popProvider);
        fields.put("popPort", this.popPort);
        fields.put("popUser", this.user);
        fields.put("popPwd", this.pass);
        
        return fields;
    }
}
