package MailSender.GUI;

import java.io.IOException;
import javax.swing.JEditorPane;

/**
 * This class generate a JComponent to preview an html page.
 * The java JEditorPane only provides basic css and html 3.4 processing so 
 * the rendering may not be exactly like a real web-navigator.
 * 
 * WARNING /////////////////////// The component do not display properly before the preview
 * is completely done, and it freeze the whole userInterface all along the
 * preview time. preventing a freeze like bug, twin it with a Waiting dialog,
 * progressbar. /////////////////////// WARNING
 */
public class UserInterfaceHtmlPreviewPanel extends JEditorPane{    
    
    /**
     * Constructor.
     * 
     * Generate a basical instance of the preview of nothing.
     * just a white panel.
     */
    public UserInterfaceHtmlPreviewPanel(){
        super();
        this.setEditable(false);
    }
    
    
    /**
     * Constructor. 
     * 
     * Generate an instance from an URL.
     * 
     * @param url
     * @throws IOException 
     */
    public UserInterfaceHtmlPreviewPanel(String url) throws IOException{
        super(url);
        this.setEditable(false);
    }
    
    /**
     * Constructor.
     * 
     * Generate an instance from a String (content) and a MimeType
     * for html : "text/html".
     * 
     * check MimeType on the web for more explanations.
     * 
     * @param type
     * @param content 
     */
    public UserInterfaceHtmlPreviewPanel(String type, String content){
        super(type, content);
        this.setEditable(false);
    }
}
