package MailRecuperator.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The class {@code ToolsPanel} is an extension of the JPanel and is a container
 * for the two buttons calling the two secondary frames, settings and run new 
 * search.
 */
public class ToolsPanel extends JPanel {
    
    /**
     * The run new search button, calls the secondary frame run new search.
     */
    JButton runNewSearchButton;
    /**
     * The settings button, calls the secondary frame settings.
     */
    JButton settingsButton;
    
    /**
     * Class constructor.
     */
    public ToolsPanel() {
        
        /**
         * Initialize the run new search button.
         */
        this.runNewSearchButton = new JButton("Run New Search");
        
        /**
         * Initialize the settings button.
         */
        this.settingsButton = new JButton("Settings");
        
        /**
         * Placing all the components in their place.
         */
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5,5,5,5);
        this.add(this.runNewSearchButton, gbc);
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5,0,5,5);
        this.add(this.settingsButton, gbc);
        
    }
    
}
