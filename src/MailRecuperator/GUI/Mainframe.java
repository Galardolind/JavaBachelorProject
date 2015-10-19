package MailRecuperator.GUI;

import Fusion.Fusion;
import MailRecuperator.CoreClasses.ExceptionRecuperation;
import MailRecuperator.CoreClasses.SettingManager;
import MailRecuperator.CoreClasses.EmailRecuperation;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu.Separator;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import MailRecuperator.CoreClasses.StateOfRecup;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;



/**
 * The abstract class {@code RefresherMainframeRecup} is an extension of Thread
 * and is responsible for the management of the dynamic aspect of the GUI.
 */
abstract class RefresherMainFrameRecup extends Thread{
    
    /**
     * The Mainframe the thread is working for.
     */
    Mainframe mainFrame;
    /**
     * The current state of the recuperation process.
     */
    StateOfRecup status;
    /**
     * The object that is actually doing the recuperation.
     */
    EmailRecuperation actualWorker;
    /**
     * The boolean marking the all-clear for a new thread to start.
     */
    boolean refresherCanStart = false;
    /**
     * The boolean indicating if the thread must be terminated.
     */
    boolean quit;
    /**
     * The boolean indicating if the thread is currently working.
     */
    boolean working = false;
    /**
     * The date at wich the thread started.
     */
    Date startDate;
    /**
     * The date at wich the thread is terminated.
     */
    Date endDate;
    /**
     * The number of keywords used in the search.
     */
    int numberOfKeyword;
    
    /**
     * The class constructor, it only initializes the mainframe and the quit 
     * boolean.
     * 
     * @param mainFrame is the Mainframe the thread will be working for
     */
    public RefresherMainFrameRecup(Mainframe mainFrame) {
        this.mainFrame = mainFrame;
        this.quit = false;
    }
    
    /**
     * Sets the quit falue to true so to terminate the thread.
     */
    public void setQuit(){
        this.quit = true;
    }
    
    /**
     * Sets the starting date.
     */
    public void setStartDate(){
        startDate = new Date();
    }
    
    /**
     * Initialize the EmailRecuperation object that will execute the search and
     * sets the GUI to its primitive state.
     * 
     * @param worker is the actual object that will be doing the search
     */
    public void setWorker(EmailRecuperation worker){
        this.actualWorker = worker;
        this.mainFrame.searchStatusBar.setMinimum(0);
        
        this.mainFrame.searchStatusBar.setMaximum(worker.getNumberOfPages()*2);
        this.mainFrame.searchStatusTab.progressPanel.statusBar.setMinimum(0);
        this.mainFrame.searchStatusTab.progressPanel.statusBar.setMaximum(worker.getNumberOfPages()*2);
    }
    
    /**
     * Set the state of the recuperation process to the given value
     * 
     * @param ss is the value at which the state must be set
     */
    public void setStatus(StateOfRecup ss){
        this.status = ss;
    }
    
    /**
     * Sets the number of keywords for the search
     * 
     * @param number the number of keywords
     */
    void setNumberOfKeyword(int number) {
        this.numberOfKeyword = number;
    }
    
    /**
     * This is the method that describes the course of a run of the thread.
     */
    @Override
    public void run(){
        
        /**
         * permanent refresher until it quits.
         */
        while(true){
            if(this.quit){
                return;
            }            
            if(refresherCanStart){                
                actualWorker.start();
                working = true;
                System.out.println(actualWorker.getName() + " is now running.");
                while(actualWorker.isAlive()){
                    if(this.quit){
                        return;
                    }
                    try {
                        this.mainFrame.searchStatusTab.progressPanel.statusViewer.getVerticalScrollBar().setValue(this.mainFrame.searchStatusTab.progressPanel.statusMsgs.getText().length()-1);
                        doWorking();
                        /**
                         * Refresh every 10ms.
                         */
                        this.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                working = false;
                /**
                 * last refresh before the end of the process.
                 */
                doWorking(); 
                refresherCanStart = false;
                System.out.println(actualWorker.getName() + " is now ended.");
                /**
                 * cleaning the system.
                 */
                clean();
            }
            else {
                if(doUnloaded()){
                    /**
                     * True when ok to continue.
                     */
                    refresherCanStart = true;

                }
                try {
                    this.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RefresherMainFrameRecup.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    public abstract void clean();
    public abstract void doWorking();
    public abstract boolean doUnloaded();
}

/**
 * The class {@code Mainframe} extends the JFrame and is the primary frame for
 * the recuperator GUI, and also manage all the functionalities implemented in
 * the panels that it is composed of.
 */
public class Mainframe extends JFrame{
    
    /**
     * The parent Fusion if there is one.
     */
    Fusion papa;
    /**
     * the current status of the ongoing search.
     */
    StateOfRecup status;
    /**
     * The current settings of the program.
     */
    SettingManager settings;
    /**
     * The menu bar.
     */
    JMenuBar menu;
    /**
     * The file menu of the menu bar.
     */
    JMenu fileMenu;
    /**
     * The quit item of the file menu.
     */
    JMenuItem quitItem;
    /**
     * The tools menu of the menu bar.
     */
    JMenu toolsMenu;
    /**
     * The run new search item of the tools menu.
     */
    JMenuItem runNewSearchItem;
    /**
     * The settings item of the tools menu.
     */
    JMenuItem settingsItem;
    /**
     * The help menu of the menu bar.
     */
    JMenu helpMenu;
    /**
     * The wiki item of the help menu.
     */
    JMenuItem wikiItem;
    /**
     * The about item of the help menu.
     */
    JMenuItem aboutItem;
    /**
     * Separators for the menu bar.
     */
    Separator toolsMenuSeparator;
    Separator helpMenuSeparator;
    /**
     * The tab manager.
     */
    JTabbedPane tabs;
    /**
     * The mainframe tab.
     */
    MainframeTab mainframeTab;
    /**
     * The search status tab.
     */
    SearchStatusTab searchStatusTab;
    /**
     * The panel containing the miniaturized search status info.
     */
    JPanel miniBar;
    /**
     * The label of the progress bar.
     */
    JLabel searchStatusLabel;
    /**
     * The progress bar for the current search.
     */
    JProgressBar searchStatusBar;
    /**
     * The pause/resume button for the current search.
     */
    JButton tooglePauseButton;
    /**
     * The stop button for the current search.
     */
    JButton stopButton;
    /**
     * The path of the folder in which the database will be saved.
     */
    String path;
    /**
     * The refresher for the mainframe.
     */
    RefresherMainFrameRecup refresher;
    /**
     * The list of all the searches in line.
     */
    ArrayList<EmailRecuperation> emailRecuperationList;
    
    
    /**
     * Class constructor.
     */
    public Mainframe(){
        
        /**
         * Initializing the email recuperation list.
         */
        emailRecuperationList = new ArrayList<EmailRecuperation>();
        
        /**
         * Initializes the basic shape and properties of the dialog.
         */
        this.papa = null;
        this.setMinimumSize(new Dimension(800, 600));
        this.setTitle("Email Recuperator — Mainframe");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        /**
         * Initialising a window listener to override the closing operation.
         */
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitMainframe(e);
            }
        });        
        
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
        }
        
        /**
         * Initializing the menu bar and all its components.
         */
        this.menu = new JMenuBar();
        this.fileMenu = new JMenu("File");
        this.quitItem = new JMenuItem("Quit");
        this.quitItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt
                .event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        this.quitItem.addActionListener(new QuitItemListener());
        this.fileMenu.add(this.quitItem);
        this.menu.add(this.fileMenu);
        this.toolsMenu = new JMenu("Tools");
        this.runNewSearchItem = new JMenuItem("Run New Search");
        this.runNewSearchItem.setAccelerator(javax.swing.KeyStroke
                .getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event
                .InputEvent.CTRL_MASK));
        this.runNewSearchItem.addActionListener(new RunNewSearchItemListener());
        this.toolsMenu.add(this.runNewSearchItem);
        this.toolsMenuSeparator = new Separator();
        this.toolsMenu.add(this.toolsMenuSeparator);
        this.settingsItem = new JMenuItem("Settings");
        this.settingsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java
                .awt.event.KeyEvent.VK_S, java.awt.event.InputEvent
                .CTRL_MASK));
        this.settingsItem.addActionListener(new SettingsItemListener());
        this.toolsMenu.add(this.settingsItem);
        this.menu.add(this.toolsMenu);
        this.helpMenu = new JMenu("Help");
        this.wikiItem = new JMenuItem("Wiki");
        this.wikiItem.addActionListener(new WikiItemListener());
        this.helpMenu.add(this.wikiItem);
        this.helpMenuSeparator = new Separator();
        this.helpMenu.add(this.helpMenuSeparator);
        this.aboutItem = new JMenuItem("About");
        this.aboutItem.addActionListener(new AboutItemListener());
        this.helpMenu.add(this.aboutItem);
        this.menu.add(this.helpMenu);
        
        /**
         * Initializing the tab manager.
         */
        this.tabs = new JTabbedPane();
        
        /**
         * Initializing the mainframe tab and its action listeners.
         */
        this.mainframeTab = new MainframeTab();
        this.mainframeTab.systemPanel.quitButton
                .addActionListener(new QuitButtonListener());
        this.mainframeTab.toolsPanel.settingsButton
                .addActionListener(new SettingsButtonListener());        
        this.mainframeTab.keywordDBPanel.removeKWButton
                .addActionListener(new RemoveKeywordButtonListener());
        this.mainframeTab.addKeywordPanel.addKeywordButton
                .addActionListener(new AddKeywordButtonListener());
        this.mainframeTab.toolsPanel.runNewSearchButton
                .addActionListener(new RunNewSearchButtonListener());
                
        /**
         * Adding the mainframe tab to the tab manager.
         */
        this.tabs.addTab("Mainframe", this.mainframeTab);
        
        /**
         * Initializing the search status tab and its action listeners.
         */this.searchStatusTab = new SearchStatusTab();
        this.searchStatusTab.progressPanel.tooglePauseSearchButton
                .addActionListener(new TooglePauseSearchButtonListener());
        this.searchStatusTab.progressPanel.stopSearchButton
                .addActionListener(new StopSearchButtonListener());         
                
        /**
         * Adding the search status tab to the tab manager.
         */
        this.tabs.addTab("Search Status", this.searchStatusTab);
        
        /**
         * Initializing the container panel for the miniature search status.
         */
        this.miniBar = new JPanel();
        
        /**
         * Initializing the progress bar and its label.
         */
        this.searchStatusLabel = new JLabel("Search Status");
        this.searchStatusBar = new JProgressBar();
        
        /**
         * Initializing the control buttons for the mini search status and
         * their action listeners.
         */
        this.tooglePauseButton = new JButton("Pause / Resume");
        this.tooglePauseButton.addActionListener(new TooglePauseButtonListener());
        this.stopButton = new JButton("Stop");
        this.stopButton.addActionListener(new StopButtonListener());
        
        /**
         * Placing all the components in their place.
         */
        GridBagLayout status_layout = new GridBagLayout();
        GridBagConstraints slc = new GridBagConstraints();
        this.miniBar.setLayout(status_layout);
        slc .fill = GridBagConstraints.BOTH;
        slc.anchor = GridBagConstraints.WEST;
        slc.insets = new Insets(5,5,5,10);
        slc .weightx = 0;        
        slc .weighty = 1.0;
        slc .gridwidth = 1;
        slc .gridx = 0;
        slc .gridy = 0;
        this.miniBar.add(this.searchStatusLabel, slc);
        slc .fill = GridBagConstraints.BOTH;
        slc.insets = new Insets(5,0,5,5);
        slc .weightx = 1.0;
        slc .gridwidth = 1;
        slc .gridx = 1;
        this.miniBar.add(this.searchStatusBar, slc);
        this.miniBar.setPreferredSize(new Dimension(this.getWidth(), 30));
        slc.gridx = 2;
        slc.weightx = 0;
        this.miniBar.add(this.tooglePauseButton, slc);
        slc.gridx =3;
        this.miniBar.add(this.stopButton, slc);
        this.getContentPane().add(BorderLayout.NORTH, this.menu);
        this.getContentPane().add(BorderLayout.CENTER, this.tabs);
        this.getContentPane().add(BorderLayout.SOUTH, this.miniBar);
        
        /**
         * This changes the layout dynamically so that the mini status panel
         * is only present when the mainframe tab is selected.
         */
        this.tabs.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent ce) {
                if (tabs.getSelectedIndex() == 1){
                    miniBar.setVisible(false);
                }
                else{
                    miniBar.setVisible(true);
                }
            }
        });
        
        /**
         * This initialize the keyword database at the start of the program and
         * use it if it is already existent.
         */
        try {
            this.settings = new SettingManager();
            refreshListKeyword();
        } catch (ExceptionRecuperation ex) {
            JOptionPane.showMessageDialog(null, "Impossible to create database : "+ex, "Error",
                        JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
        
        /**
         * Initialize the status and refresher.
         */
        this.status = new StateOfRecup(0, 0, true, false);        
        this.refresher = new RefresherMainFrameRecup(this) {

            /**
             * What the refresher does when actually working.
             */
            @Override
            public void doWorking() {this.mainFrame.searchStatusBar.setValue(this.status.getPercent());
                this.mainFrame.searchStatusTab.progressPanel.statusBar.setValue(this.status.getPercent());
                this.mainFrame.searchStatusTab.infoPanel.visitedPagesInfo.setText(this.status.getNbOfPages()+"");
                this.mainFrame.searchStatusTab.infoPanel.emailsFoundInfo.setText((this.status.getNbOfEmails()+""));
                endDate = new Date();
                long time = (endDate.getTime() - startDate.getTime());
                long seconds = (time / 1000) % 60;
                long minutes = (time / 60000) % 60;
                long hours = (time / 3600000);
                this.mainFrame.searchStatusTab.infoPanel.timeInfo.setText(hours+" h "+minutes+" min "+seconds+" sec");
                float numberOfKeywordDone = numberOfKeyword - this.mainFrame.emailRecuperationList.size();
                float numberTotalOfKeyword = numberOfKeyword;
                float currentPercent = ((numberOfKeywordDone / numberTotalOfKeyword)*100);
                this.mainFrame.searchStatusTab.infoPanel.percentageInfo.setText((int)currentPercent+" %");
            }

            @Override
            public boolean doUnloaded() {     
                /**
                 * Presenting clean active state.
                 */
                this.mainFrame.searchStatusBar.setValue(0);
                this.mainFrame.searchStatusTab.progressPanel.statusBar.setValue(0);
                this.mainFrame.searchStatusTab.infoPanel.nbKeywordsInfo.setText(this.mainFrame.emailRecuperationList.size()+"");
                /**
                 * Checking the list's first item.
                 */
                if(this.mainFrame.emailRecuperationList.size() > 0){
                    setWorker(this.mainFrame.emailRecuperationList.get(0));
                    this.mainFrame.emailRecuperationList.get(0).setVar(status);
                    return true;
                } else {
                    this.mainFrame.searchStatusTab.infoPanel.percentageInfo.setText("100 %");
                }
                return false;
            }

            @Override
            public void clean() {
                this.mainFrame.status.setPercent(0);
                this.mainFrame.emailRecuperationList.remove(actualWorker);
            }
        };
        
        /**
         * Statrting the refresher.
         */
        this.refresher.setStatus(status);
        this.refresher.start();
        
        /**
         * Setting the frame to be visible.
         */
        this.setVisible(true);
        
    }
    
    /**
     * Class constructor that creates a mainframe that has a Fusion parent.
     * 
     * @param parent The Fusion parent
     */
    public Mainframe(Fusion parent) {
        
        this();
        this.papa = parent;
        
    }
    
    /**
     * This is call function to use in an action listener that calls up the 
     * closing warning popup.
     *
     * @param event an action event generated by the listener
     */
    public void exitMainframe(ActionEvent event) {
        
        ClosingWarningPopup cwp = new ClosingWarningPopup(this, true);
        
    }
    
    /**
     * This is call function to use in a window listener that calls up the 
     * closing warning popup.
     *
     * @param event a window event generated by the listener
     */
    public void exitMainframe(WindowEvent event) {
        
        ClosingWarningPopup cwp = new ClosingWarningPopup(this, true);
        
    }
    
    /**
     * This is call function to use in an action listener that calls up the 
     * new search secondary frame.
     *
     * @param event an action event generated by the listener
     */
    public void openNewSearch(ActionEvent event) {
        
        RunNewSearchDialog rnsd = new RunNewSearchDialog(this, true);
        
    }
    
    /**
     * This is call function to use in an action listener that calls up the 
     * settings secondary frame.
     *
     * @param event an action event generated by the listener
     */
    public void openSettings(ActionEvent event) {
    	
    	RecupSettingsDialog rsd = new RecupSettingsDialog(this, true);
    	
    }
    
    /**
     * This is a function that updates the keyword list.
     */
    public void refreshListKeyword(){
        this.mainframeTab.keywordDBPanel.dbListModel.clear();
        try {
            for(String keyword : this.settings.getKeyWordManager().getKeyWordList()){
                this.mainframeTab.keywordDBPanel.dbListModel.addElement(keyword);
            }
        } catch (ExceptionRecuperation ex) {
            try {
                this.settings = new SettingManager();
            } catch (ExceptionRecuperation ex1) {
                JOptionPane.showMessageDialog(null, "Impossible to create database : "+ex, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * This is a fonction to use in a listener that removes a keyword from the 
     * keyword list.
     *
     * @param event an action event generated by the listener
     */
    public void removeKeyword(ActionEvent event) {
        try {
            this.settings.getKeyWordManager().deleteKeyWord(this.mainframeTab.keywordDBPanel.databaseList.getSelectedValue().toString());
            refreshListKeyword();
        } catch (ExceptionRecuperation ex) {
        }
        
    }
    
    /**
     * This is a fonction to use in a listener that adds a keyword from the 
     * keyword list.
     *
     * @param event an action event generated by the listener
     */
    public void addKeyword(ActionEvent event) {
        try {
            this.settings.getKeyWordManager().addKeyWord(this.mainframeTab.addKeywordPanel.addKeywordField.getText());
            this.mainframeTab.addKeywordPanel.addKeywordField.setText("");
            refreshListKeyword();
        } catch (ExceptionRecuperation ex) {
        }
        
    }
    
    /**
     * This function pauses or resume a search.
     */
    public void setCurrentSearchPaused(){
        this.status.setRun();
    }
    
    /**
     * This function terminates a search.
     */
    public void setCurrentSearchStoped(){
        this.status.setStop(true);
    }
    
    /**
     * This is a fonction to use in a listener that opens the wiki page of the
     * project in the user's default browser.
     *
     * @param event an action event generated by the listener
     */
    public void openWiki(ActionEvent event) {
        try {
         String url = "https://reunion.unice.fr/redmine/projects/2013-s6-e02/wiki/Guide";
         java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
       }
       catch (java.io.IOException e) {
           System.out.println(e.getMessage());
       }
    }
    
    /**
     * This is a fonction to use in a listener that opens the about popup.
     *
     * @param event an action event generated by the listener
     */
    public void openAbout(ActionEvent event) {
        
        AboutPopup ap = new AboutPopup(this, true);
        
    }
    
    /**
     * Action Listener for the quit item in the menu bar.
     */
    class QuitItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            exitMainframe(e);
        }
        
    }
    
    /**
     * Action Listener for the quit button in the mainframe tab.
     */
    class QuitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
        	exitMainframe(e);
        }

    }
    
    /**
     * Action Listener for the about item in the menu bar.
     */
    class AboutItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            openAbout(e);
        }

    }
    
    /**
     * Action Listener for the run new search item in the menu bar.
     */
    class RunNewSearchItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            openNewSearch(e);
        }
        
    }

    /**
     * Action Listener for the settings item in the menu bar.
     */
    class SettingsItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            openSettings(e);
        }

    } 
    
    /**
     * Action Listener for the run new search button in the mainframe tab.
     */
    class RunNewSearchButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            openNewSearch(e);
        }
        
    }
    
    /**
     * Action Listener for the settings button in the mainframe tab.
     */
    class SettingsButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            openSettings(e);
        }

    }
    
    /**
     * Action Listener for the wiki item in the menu bar.
     */
    class WikiItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            openWiki(e);
        }

    }

    /**
     * Action Listener for the remove keyword button in the mainframe tab.
     */
    class RemoveKeywordButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            removeKeyword(e);
        }

    }
    
    /**
     * Action Listener for the add keyword button in the mainframe tab.
     */
    class AddKeywordButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            addKeyword(e);
        }

    }
    
    /**
     * Action Listener for the pause/resume button in the mini status panel.
     */
    class TooglePauseButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setCurrentSearchPaused();
        }

    }
    
    /**
     * Action Listener for the pause/resume button in the search status tab.
     */
    class TooglePauseSearchButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setCurrentSearchPaused();
        }

    }
    
    /**
     * Action Listener for the stop button in the mini status panel.
     */
    class StopButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setCurrentSearchStoped();
        }

    }
    
    /**
     * Action Listener for the stop button in the search status tab.
     */
    class StopSearchButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setCurrentSearchStoped();
        }

    }
    
    
    /**
     * The main method for the recuperator program.
     */
    public static void main(String[] args){
        
        Mainframe session = new Mainframe();
        
    }
        
}
