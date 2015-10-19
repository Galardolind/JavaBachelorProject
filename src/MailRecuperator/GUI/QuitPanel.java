package MailRecuperator.GUI;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The class {@code QuitPanel} is an extension of the JPanle, and deals with
 * the function calling the closing warning popup.
 */
public class QuitPanel extends JPanel {
    
    /**
     * The buuton that calls the closing warning popup.
     */
    JButton quitButton;
    
    /**
     * Class constructor.
     */
    public QuitPanel() {
        
        /**
         * Initializes the quit button and add it to the panel.
         */
        this.quitButton = new JButton("Quit");        
        this.add(this.quitButton);
        
    }
    
}
