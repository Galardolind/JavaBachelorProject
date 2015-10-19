package MailSender.CoreClasses;

import MailSender.GUI.UserInterfacePopDialog;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class that do the parsing of the messages got from the pop.
 */
public class PopMessageParser {
    /**
     * The status.
     */
    PopStatus status;
    
    /**
     * The user interface of the pop dialog. Only used to write the log.
     */
    UserInterfacePopDialog dial;

    /**
     * Constructor of class PopMessageParser.
     * @param dial the user interface dialog
     */
    public PopMessageParser(UserInterfacePopDialog dial) {
        this.dial = dial;
    }
    
    /**
     * Set the status.
     * @param status 
     */
    public void setStatus(PopStatus status){
        this.status = status;
    }

    /**
     * Parse a message formated as a MailSample.
     * @param msg the message to parse
     * @throws ExceptionSender if msg is equal to null
     */
    public void parseMessage(MailSample msg) throws ExceptionSender {
        if(msg == null){
            throw new ExceptionSender("PopMessageParser : ParseMessage : msg is equal to null");
        }
        Set<String> mails = new TreeSet<String>();
        Pattern emailRegex = Pattern.compile("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})");
        Document doc = Jsoup.parse(msg.getContent() +" "+ msg.getSubject());
        Elements contents = doc.getElementsMatchingOwnText(emailRegex);
        for (Element email : contents) {
            Matcher matcher = emailRegex.matcher(email.text());
            while(matcher.find()) {
                mails.add(matcher.group());                
            }
        }
        for(String mail : mails){
            status.addMail(mail);
            dial.collectTrace.append("Added : " + mail + " \n");
        }
    }

    /**
     * Run the parsing of all messages.
     */
    public void run(){
        for(int i = 0; i < status.getMessages().size() ; i++){
            try {
                parseMessage(status.getMessages().get(i));
                dial.collectProgess.setValue(dial.collectProgess.getValue() + 1);
            } catch (ExceptionSender ex) {
                Logger.getLogger(PopMessageParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}