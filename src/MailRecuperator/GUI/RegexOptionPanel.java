package MailRecuperator.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The class {@code RegexOptionPanel} is an extension of the JPanel and deals
 * with the regex setting.
 */
public class RegexOptionPanel extends JPanel {
    
    /**
     * The info label.
     */
    JLabel regexInfoLabel;
    /**
     * The regex selector, is also a text field for saving new regexes.
     */
    JComboBox regexSelector;
        
    /**
     * Class Constructor.
     */
    public RegexOptionPanel() {
        
        /**
         * Initializes the text.
         */
        this.regexInfoLabel = new JLabel("Here you can configure which"
                + " regex is used in the parsing of the webpages");        
        
        /**
         * Initialize the selector.
         */
        this.regexSelector = new JComboBox();
        this.regexSelector.setEditable(true);
        this.regexSelector.addItem("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@" +
        		"[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})");
        
        /**
         * Placing all the components in their place.
         */
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(5,5,5,5);
        gbc.weightx = 1;
        gbc.weighty = 0;
        this.add(this.regexInfoLabel, gbc);
        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(this.regexSelector, gbc);
               
    }
    
}
