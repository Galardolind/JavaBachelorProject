package MailRecuperator.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The class {@code SavePathOptionPanel} is an extension of the JPanel and
 * deals with the selection of which directory will hold the generated database.
 */
public class SavePathOptionPanel extends JPanel {
	
    /**
     * Simple label.
     */
    JLabel savePathLabel;
    /**
     * The selector for paths that have already been saved.
     */
    JComboBox savePathSelector;
    /**
     * The button calling the file chooser.
     */
    JButton browseButton;
    
    /**
     * Class constructor.
     */
    public SavePathOptionPanel() {
		
        /**
         * Initializes the text.
         */
        this.savePathLabel = new JLabel("Please choose a directory where to "
                + "save your eMails");

        /**
         * Initializes the selector.
         */
        this.savePathSelector = new JComboBox();

        /**
         * Initializes the browse button.
         */
        browseButton = new JButton("Browse");

        /**
         * Placing all the components in their place.
         */
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(5,5,5,5);
        this.add(this.savePathLabel, gbc);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        this.add(this.savePathSelector, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0;
        this.add(this.browseButton, gbc);
		
    }

}
