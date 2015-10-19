package MailRecuperator.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * The class {@code KeywordSelectorPanel} is an extension of the JPanel and
 * deals with the process of selecting which keywords will be used in a new
 * search.
 */
public class KeywordSelectorPanel extends JPanel {
    
    /**
     * These are the labels designating what each display zone stands for.
     */
    public JLabel notSelectedLabel;
    public JLabel selectedLabel;
    /**
     * The Display of the list of keywords not used in the search.
     */
    public JList notSelectedList;
    /**
     * The Display of the list of keywords used in the search.
     */
    public JList selectedList;
    /**
     * The actual list of keywords not used in the search put into a model.
     */
    public DefaultListModel notSelectedModel;
    /**
     * The actual list of keywords used in the search put into a model.
     */
    public DefaultListModel selectedModel;
    /**
     * The containers of the displays authorising a scrolling.
     */
    public JScrollPane notSelectedListViewer;
    public JScrollPane selectedListViewer;
    /**
     * Button that moves a single selected keyword from the list of keywords
     * not used in the search to the list of keywords used in the search.
     */
    public JButton addOneButton;
    /**
     * Button that moves all keywords from the list of keywords not used in the
     * search to the list of keywords used in the search.
     */
    public JButton addAllButton;
    /**
     * Button that moves a single selected keyword from the list of keywords
     * used in the search to the list of keywords not used in the search.
     */
    public JButton removeOneButton;
    /**
     * Button that moves all keywords from the list of keywords used in the
     * search to the list of keywords not used in the search.
     */
    public JButton removeAllButton;
    
    /**
     * Class constructor.
     */
    public KeywordSelectorPanel() {
        
        /**
         * Initialization of the two labels identifying the diplay zones.
         */
        this.notSelectedLabel = new JLabel("Keywords not included in the search");        
        this.selectedLabel = new JLabel("Keywords included in the search");
        
        /**
         * Initializing the model and the diplay for the list of keywords not
         * used in the search, and putting the first in the second.
         */
        this.notSelectedList = new JList();
        this.notSelectedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.notSelectedModel = new DefaultListModel();
        this.notSelectedList.setModel(this.notSelectedModel);       
        
        /**
         * Initializing the model and the diplay for the list of keywords used 
         * in the search, and putting the first in the second.
         */
        this.selectedList = new JList();
        this.selectedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.selectedModel = new DefaultListModel();
        this.selectedList.setModel(this.selectedModel);
        
        /**
         * Initializing the scrollable containers for the displays.
         */
        this.notSelectedListViewer = new JScrollPane(this.notSelectedList);
        this.selectedListViewer = new JScrollPane(this.selectedList);
        
        /**
         * Initializing the add one keyword button.
         */
        this.addOneButton = new JButton(" > ");
        
        /**
         * Initializing the add all keywords button.
         */
        this.addAllButton = new JButton(" >> ");
        
        /**
         * Initializing the remove all keywords button.
         */
        this.removeAllButton = new JButton(" << ");
        
        /**
         * Initializing the remove one keyword button.
         */
        this.removeOneButton = new JButton(" < ");
        
        /**
         * Placing all the components in their place.
         */
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.fill = GridBagConstraints.NONE;
        this.add(this.notSelectedLabel, gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        this.add(this.selectedLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.gridheight = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(this.notSelectedListViewer, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(this.addOneButton, gbc);
        gbc.gridy = 2;
        gbc.weighty = 0;
        this.add(this.addAllButton, gbc);
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTH;
        this.add(this.removeAllButton, gbc);
        gbc.gridy = 4;
        gbc.weighty = 1;
        this.add(this.removeOneButton, gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.gridheight = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(this.selectedListViewer, gbc);
    }
    
}
