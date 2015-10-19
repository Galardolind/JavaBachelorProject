package MailRecuperator.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * The class {@code KeywordDatabasePanel} is an extension of the JPanel that
 * deals with the displaying of the saved keywords list and the functionality
 * to remove a keyword from said list.
 */
public class KeywordDatabasePanel extends JPanel {

    /**
     * The Display of the keyword list.
     */
    public JList databaseList;
    /**
     * The actual keyword list put into a model.
     */
    public DefaultListModel dbListModel;
    /**
     * The container of the display authorising a scrolling.
     */
    public JScrollPane listViewer;
    /**
     * The button removing the keyword selected(highlighted) in the display.
     */
    public JButton removeKWButton;
        
    /**
     * Class constructor.
     */
    public KeywordDatabasePanel() {
        
        /**
         * Initializing the model and the diplay, and putting the first in 
         * the second.
         */
        this.databaseList = new JList();    
        this.dbListModel = new DefaultListModel();
        this.databaseList.setModel(this.dbListModel);
        this.databaseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
        /**
         * Initializing the scrollable container for the display.
         */
        this.listViewer = new JScrollPane(this.databaseList);
        
        /**
         * Initializing the remove keyword button.
         */
        this.removeKWButton = new JButton("Remove Keyword");
        
        /**
         * Placing all the components in their place.
         */
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5,5,0,5);
        this.add(this.listViewer, gbc);
        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(2,0,2,5);
        this.add(this.removeKWButton, gbc);      
        
    }
    
}
