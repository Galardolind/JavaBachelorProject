package MailRecuperator.GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import MailRecuperator.CoreClasses.EmailRecuperation;
import MailRecuperator.CoreClasses.ExceptionRecuperation;
import java.io.File;
import javax.swing.JOptionPane;

/**
 * The class {@code RunNewSearchDialog} is an extention of the JDialog and is
 * the secondry frame dealing with the new searches, and also manage all the 
 * functionalities implemented in the panels that it is composed of.
 */
public class RunNewSearchDialog extends JDialog {
    
    /**
     * The parent Mainframe.
     */
    Mainframe papa;
    /**
     * The keyword selector panel, dealing with the selection of which keywords
     * will be used for the search.
     */
    KeywordSelectorPanel keywordSelectorPanel;
    /**
     * The search depth panel, dealing with the depth of the search.
     */
    SearchDepthPanel searchDepthPanel;
    /**
     * The savepath option panel, deals with the selection of which directory
     * will hold the generated database.
     */
    SavePathOptionPanel savePathOptionPanel;
    /**
     * The button that starts the search.
     */
    JButton startSearchButton;
    /**
     * the button closing the dialog.
     */
    JButton cancelSearchButton;
    /**
     * the file chooser for the database path.
     */
    JFileChooser pathChooser;
        
    /**
     * The constructor of the class.
     * 
     * @param parent is the parent JFrame, here constrained to be a Mainframe
     * @param modal specifies whether the dialog blocks user input to other 
     * top-level windows when shown.
     */
    public RunNewSearchDialog(Mainframe parent, boolean modal) {
        
        /**
         * First, the constructor calls the JDialog construtor.
         */
        super(parent, "New Search", modal);
        
        /**
         * Initializing the parent Mainframe.
         */
        this.papa = parent;
        
        /**
         * Initializing the basic shape and properties of the dialog.
         */
        this.setMinimumSize(new Dimension(800, 600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        /**
         * Windows Look and Feel.
         */
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows"
                    + ".WindowsLookAndFeel");
        } catch (ClassNotFoundException 
                | InstantiationException 
                | IllegalAccessException 
                | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(RecupSettingsDialog.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        /**
         * Each panel is initialized.
         */
        this.keywordSelectorPanel = new KeywordSelectorPanel();
        this.keywordSelectorPanel.setBorder(BorderFactory
                .createTitledBorder("Keyword Selection"));
        this.searchDepthPanel = new SearchDepthPanel();
        this.searchDepthPanel.setBorder(BorderFactory
                .createTitledBorder("Search Depth"));
        this.savePathOptionPanel = new SavePathOptionPanel();
        this.savePathOptionPanel.savePathSelector.addItem(this.papa.settings.getEmailDatabasePath());
        this.savePathOptionPanel.setBorder(BorderFactory
                .createTitledBorder("Database SavePath"));
        
        /**
         * Adding the listeners for the buttons in the panels.
         */
        this.keywordSelectorPanel.addOneButton
                .addActionListener(new AddOneButtonListener());
        this.keywordSelectorPanel.addAllButton
                .addActionListener(new AddAllButtonListener());
        this.keywordSelectorPanel.removeOneButton
                .addActionListener(new RemoveOneButtonListener());
        this.keywordSelectorPanel.removeAllButton
                .addActionListener(new RemoveAllButtonListener());
        this.savePathOptionPanel.browseButton
                .addActionListener(new BrowseButtonListener());
        
        /**
         * Initializes the start search button and adds a listener to it. 
         */
        this.startSearchButton = new JButton("Start Search");
        this.startSearchButton.addActionListener(new StartSearchButtonListener());
        
        /**
         * Initializes the cancel button and adds a listener to it. 
         */
        this.cancelSearchButton = new JButton("Cancel");
        this.cancelSearchButton.addActionListener(new CancelSearchButtonListener());
        
        /**
         * Placing all the components in their place.
         */
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(this.keywordSelectorPanel, gbc);
        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(0,5,5,5);
        this.add(this.searchDepthPanel, gbc);
        gbc.gridy = 2;
        this.add(this.savePathOptionPanel, gbc);
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.fill = GridBagConstraints.NONE;
        this.add(this.startSearchButton, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.insets = new Insets(0,0,5,5);
        this.add(this.cancelSearchButton, gbc);
        
        /**
         * Recuperate the keyword list from the mainframe.
         */
        try {
            for(String str : this.papa.settings.getKeyWordManager().getKeyWordList()){
                this.keywordSelectorPanel.notSelectedModel.addElement(str);
            }
        } catch (ExceptionRecuperation ex) {
            ex.printStackTrace();
        }
        
        /**
         * Setting the dialog to be visible.
         */
        this.setVisible(true);
        
    }
    
    /**
     * This is a basic closing function to use in a listener, that also shows
     * the search status tab when returning to the mainframe.
     *
     * @param event an action event generated by the listener
     */
    public void exitSearchDialog(ActionEvent event) {
        this.papa.tabs.setSelectedIndex(1);
        this.dispose();
        
    }
    
    /**
     * Programs a new search and adds it to the list in the mainframe.
     * 
     * @param event an action event generated by the listener
     * @return true if everything went smoothly, else false
     */
    public boolean addEmailsRecuperation(ActionEvent event){
        String forbiddenPath1 = System.getProperty("user.dir")+File.separator +"data"+File.separator+"recuperatorData";
        String forbiddenPath2 = System.getProperty("user.dir")+File.separator +"data";
        String forbiddenPath3 = System.getProperty("user.dir")+File.separator +"data"+File.separator+"sendData";
        if(forbiddenPath1.equals(this.savePathOptionPanel.savePathSelector.getSelectedItem().toString())){
             JOptionPane.showMessageDialog(null, "This directory is exclusive to the program core functions, please choose another", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if(forbiddenPath2.equals(this.savePathOptionPanel.savePathSelector.getSelectedItem().toString())){
             JOptionPane.showMessageDialog(null, "This directory is exclusive to the program core functions, please choose another", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if(forbiddenPath3.equals(this.savePathOptionPanel.savePathSelector.getSelectedItem().toString())){
             JOptionPane.showMessageDialog(null, "This directory is exclusive to the program core functions, please choose another", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            return false;
        }
        this.papa.status.setStop(false);
        this.papa.settings.setEmailDatabasePath(this.savePathOptionPanel.savePathSelector.getSelectedItem().toString());
        this.papa.refresher.setStartDate();
        this.papa.searchStatusTab.progressPanel.statusMsgs.setText("");
        this.papa.searchStatusTab.infoPanel.visitedPagesInfo.setText("0");
        this.papa.searchStatusTab.infoPanel.emailsFoundInfo.setText("0");
        int nb = 999999999;
        if(this.searchDepthPanel.depthSlider.getValue() != 0){
            nb = this.searchDepthPanel.depthSlider.getValue() / 40 ;
        }
        for(int i = 0 ; i < this.keywordSelectorPanel.selectedModel.getSize(); i++){
            try {
                EmailRecuperation emr = new EmailRecuperation(this.keywordSelectorPanel.selectedModel.getElementAt(i).toString(),
                this.papa.settings.getEmailDatabasePath(),nb);
                this.papa.emailRecuperationList.add(emr);
            } catch (ExceptionRecuperation ex) {
                JOptionPane.showMessageDialog(null, ex, "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        this.papa.refresher.setNumberOfKeyword(this.papa.emailRecuperationList.size());
        this.papa.searchStatusTab.infoPanel.percentageInfo.setText("0");
        return true;
        
    }
    
    /**
     * This is a fonction to use in a listener that moves a single keyword from 
     * the list of keywords not used for the search to the list of keywords 
     * used for the search.
     *
     * @param event an action event generated by the listener
     */
    public void addKeyword(ActionEvent event){
        int select = this.keywordSelectorPanel.notSelectedList.getSelectedIndex();
        if(select >= 0){
            this.keywordSelectorPanel.selectedModel.addElement(this.keywordSelectorPanel.notSelectedModel.getElementAt(select));
            this.keywordSelectorPanel.notSelectedModel.removeElementAt(select);
        }        
    }
    
    /**
     * This is a fonction to use in a listener that moves a single keyword from 
     * the list of keywords used for the search to the list of keywords not 
     * used for the search.
     *
     * @param event an action event generated by the listener
     */
    public void removeKeyword(ActionEvent event){
        int select = this.keywordSelectorPanel.selectedList.getSelectedIndex();
        if(select >= 0){
            this.keywordSelectorPanel.notSelectedModel.addElement(this.keywordSelectorPanel.selectedModel.getElementAt(select));
            this.keywordSelectorPanel.selectedModel.removeElementAt(select);
        }           
    }
    
    /**
     * This is a fonction to use in a listener that moves all keywords from the 
     * list of keywords not used for the search to the list of keywords used 
     * for the search.
     *
     * @param event an action event generated by the listener
     */
    public void addAllKeywords(ActionEvent event){
        while(this.keywordSelectorPanel.notSelectedModel.getSize() != 0){
            this.keywordSelectorPanel.selectedModel.addElement(this.keywordSelectorPanel.notSelectedModel.getElementAt(0));
            this.keywordSelectorPanel.notSelectedModel.removeElementAt(0);
        }
    }
    
    /**
     * This is a fonction to use in a listener that moves all keywords from the 
     * list of keywords used for the search to the list of keywords not used 
     * for the search.
     *
     * @param event an action event generated by the listener
     */
    public void removeAllKeywords(ActionEvent event){
        while(this.keywordSelectorPanel.selectedModel.getSize() != 0){
            this.keywordSelectorPanel.notSelectedModel.addElement(this.keywordSelectorPanel.selectedModel.getElementAt(0));
            this.keywordSelectorPanel.selectedModel.removeElementAt(0);
        }
    }
    
    /**
     * This is a fonction to use in a listener that opens a File selector
     * and assign its returning value to the database path variable.
     *
     * @param event an action event generated by the listener
     */
    private void browseWindowOpen(ActionEvent event) {                                               
        if (this.pathChooser == null) {
            this.pathChooser = new JFileChooser(".");
        }        
        
        this.pathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
 
        int returnVal = this.pathChooser.showOpenDialog(this);
 
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            this.savePathOptionPanel.savePathSelector.insertItemAt(this.pathChooser.getSelectedFile().getPath(), 0);
            this.savePathOptionPanel.savePathSelector.setSelectedIndex(0);
        } else {
            this.savePathOptionPanel.savePathSelector.setSelectedIndex(-1);
        }
 
        this.pathChooser.setSelectedFile(null);
        
    }
    
    /**
     * Listener for the cancel button, closes the dialog.
     */
    class CancelSearchButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            exitSearchDialog(e);
            
        }
        
    }
    
    /**
     * Listener for the add one button.
     */
    class AddOneButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            addKeyword(e);
        }
        
    }
    
    /**
     * Listener for the add all button.
     */
    class AddAllButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            addAllKeywords(e);
        }
        
    }
    
    /**
     * Listener for the remove one button.
     */
    class RemoveOneButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            removeKeyword(e); 
        }
        
    }
    
    /**
     * Listener for the remove all button.
     */
    class RemoveAllButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            removeAllKeywords(e);
        }
        
    }
    
    /**
     * Listener for the browse button in panel save path.
     */
    class BrowseButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            browseWindowOpen(e);
            
        }        
        
    }
    
    /**
     * Listener for start search button, begins a new search.
     */
    class StartSearchButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(addEmailsRecuperation(e)){
                exitSearchDialog(e);
            }
            
        }
        
    }    
    
}
