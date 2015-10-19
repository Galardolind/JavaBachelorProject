package MailRecuperator.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * The class {@code SearchStatusTab} is an extension of the JPanel and is used 
 * as a container for all the panels composing the search status tab.
 */
public class SearchStatusTab extends JPanel {
    
    /**
     * Info panel, deals with the display of stats relative to the current
     * search or searches.
     */
    InfoPanel infoPanel;
    /**
     * Progress panel, deals with the display of the progress bar and the text
     * log, as well as the functions of pausing, resuming, and terminating
     * the search or searches.
     */
    StatusPanel progressPanel;
    
    /**
     * Class constructor.
     */
    public SearchStatusTab() {
        
        /**
         * Each panel is initialized and given a titled border.
         */
        this.infoPanel = new InfoPanel();
        this.infoPanel.setBorder(BorderFactory.createTitledBorder("Search Info"));
        this.progressPanel = new StatusPanel();
        this.progressPanel.setBorder(BorderFactory.createTitledBorder("Status"));
        
        /**
         * Placing all the components in their place.
         */
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.NORTH;
        this.add(this.infoPanel, gbc);
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weighty = 1;
        gbc.insets = new Insets(0,5,5,5);
        this.add(this.progressPanel, gbc);
        
    }
    
}
