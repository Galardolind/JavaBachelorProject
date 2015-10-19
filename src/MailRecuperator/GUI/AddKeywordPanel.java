package MailRecuperator.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The class {@code AddKeywordPanel} is an extension of the JPanel which deals
 * with the functionnality of adding a new keyword to the keyword list used to
 * start new searches.
 */
public class AddKeywordPanel extends JPanel {
    
    
    /**
     * The field in which to write the keyword to add.
     */
    public JTextField addKeywordField;
    /**
     * The button that adds the keyword in the textfield to the list.
     */
    public JButton addKeywordButton;
    
    
    /**
     * The class constructor.
     */
    public AddKeywordPanel() {
        
        /**
         * Initializing the field.
         */        
        this.addKeywordField = new JTextField();
        
        /**
         * Initializing the add keyword button.
         */
        this.addKeywordButton = new JButton("Add Keyword");
        
        
        /**
         * Placing all the components in their place.
         */
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(5,5,5,0);
        gbc.anchor = GridBagConstraints.BASELINE;
        this.add(this.addKeywordField, gbc);
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5,5,5,5);
        gbc.weightx = 0;
        this.add(this.addKeywordButton, gbc);
        
    }
    
}
