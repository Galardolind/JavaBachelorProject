package MailRecuperator.GUI;

import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * le class {@code BlackListOptionPanel} is an extension of the JPanel and deals
 * with the gestion of the blacklist.
 */
public class BlackListOptionPanel extends JPanel {
    
    
    /**
     * The Display of the blacklist.
     */
    public JList blackList;
    /**
     * The actual Blacklist put into a model.
     */
    public DefaultListModel blackListModel;
    /**
     * The container of the display authorising a scrolling.
     */
    public JScrollPane blackListViewer;
    /**
     * The field in which to write the domain to add to the blacklist.
     */
    public JTextField addDomainField;
    /**
     * The button adding the domain in the field to the list.
     */
    public JButton addDomainButton;
    /**
     * The button removing the domain selected(highlighted) in the display.
     */
    public JButton removeDomainButton;
	
    public BlackListOptionPanel() {

        /**
         * Initializing the model and the diplay, and putting the first in 
         * the second.
         */
        this.blackList = new JList();        
        this.blackListModel = new DefaultListModel();
        this.blackList.setModel(this.blackListModel);
        this.blackList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        /**
         * Initializing the scrollable container for the display.
         */
        this.blackListViewer = new JScrollPane(this.blackList);

        /**
         * Initializing the field.
         */
        this.addDomainField = new JTextField();
        
        /**
         * Initializing the add domain button.
         */
        this.addDomainButton = new JButton("Add Domain");
        
        /**
         * Initializing the remove domain button.
         */
        this.removeDomainButton = new JButton("Remove Domain");
        
        /**
         * Placing all the components in their place.
         */
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(5,5,5,5);
        this.add(this.addDomainField, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.insets = new Insets(5,0,5,5);
        this.add(this.addDomainButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,5,5,5);
        this.add(this.blackListViewer, gbc);
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.fill = GridBagConstraints.NONE;
        this.add(this.removeDomainButton, gbc);

    }

}
