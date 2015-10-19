package MailRecuperator.GUI;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The class {@code PurgeOptionPanel} is an extension of the JPanel and deals
 * with the purging of the visited url history.
 */
public class PurgeOptionPanel extends JPanel {
    
    /**
     * The info and warning text.
     */
    JLabel purgeInfoLabel1;
    JLabel purgeInfoLabel2;
    JLabel purgeInfoLabel3;
    /**
     * The button that will call the purge warning popup.
     */
    JButton purgeDBButton;
    
    /**
     * Class contructor.
     */
    public PurgeOptionPanel() {
        
        /**
         * Initializes the text, warning is in red.
         */
        this.purgeInfoLabel1 = new JLabel("Here you can purge the searched "
                + "url history, thus erasing every entry you ever added to it");
        this.purgeInfoLabel2 = new JLabel("WARNING !");
        this.purgeInfoLabel2.setForeground(Color.RED);
        this.purgeInfoLabel3 = new JLabel("This operation cannot be undone, "
                + "so think about using the 'Remove Historic' function "
                + "instead !");
        this.purgeInfoLabel3.setForeground(Color.RED);
        
        /**
         * Initializes the purge button.
         */
        this.purgeDBButton = new JButton("Purge History");
                
        /**
         * Placing all the components in their place.
         */
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(5,5,10,5);
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        this.add(this.purgeInfoLabel1, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(0,5,0,5);
        gbc.anchor = GridBagConstraints.BASELINE;
        this.add(this.purgeInfoLabel2, gbc);
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.BASELINE;
        this.add(this.purgeInfoLabel3, gbc);
        gbc.gridy = 3;
        gbc.weighty = 1;
        gbc.insets = new Insets(10,5,5,5);
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(this.purgeDBButton, gbc);
        
    }
        
}
