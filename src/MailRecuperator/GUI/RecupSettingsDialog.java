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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import MailRecuperator.CoreClasses.ExceptionRecuperation;
import MailRecuperator.CoreClasses.SettingManager;
import javax.swing.JOptionPane;

/**
 * The class {@code RecupSettingsDialog} extends the JDialog and is the 
 * secondary frame dealing with settings, and also manage all the 
 * functionalities implemented in the panels that it is composed of.
 */
public class RecupSettingsDialog extends JDialog {

    /**
     * The panel dealing with the use of regexes.
     */
    RegexOptionPanel regexOptionPanel;
    /**
     * The panel dealing with the blacklist management.
     */
    BlackListOptionPanel blackListOptionPanel;
    /**
     * The panel dealing with the purge of the searched url history.
     */
    PurgeOptionPanel purgeOptionPanel;
    /**
     * The apply button, applies all settings entered in the dialog.
     */
    JButton applyButton;
    /**
     * The default button, returns all settings to their default values.
     */
    JButton defaultButton;
    /**
     * The close button, closes the dialog.
     */
    JButton closeButton;
    /**
     * The parent Mainframe.
     */
    Mainframe papa;

    /**
     * The constructor of the class.
     * 
     * @param parent is the parent JFrame, here constrained to be a Mainframe
     * @param modal specifies whether the dialog blocks user input to other 
     * top-level windows when shown.
     */
    public RecupSettingsDialog(Mainframe parent, boolean modal) {

        /**
         * First, the constructor calls the JDialog construtor.
         */
        super(parent, "Settings", modal);

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(RecupSettingsDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * Each panel is initialized and given a titled border.
         */
        this.regexOptionPanel = new RegexOptionPanel();
        this.regexOptionPanel.setBorder(BorderFactory.createTitledBorder("Regex Configuration"));
        this.blackListOptionPanel = new BlackListOptionPanel();
        this.blackListOptionPanel.setBorder(BorderFactory.createTitledBorder("Blacklist"));        
        this.purgeOptionPanel = new PurgeOptionPanel();
        this.purgeOptionPanel.setBorder(BorderFactory.createTitledBorder("Purge History"));

        /**
         * Adding the listeners for the buttons in the panels.
         */
        this.blackListOptionPanel.addDomainButton.addActionListener(new AddDomainButtonListener());
        this.blackListOptionPanel.removeDomainButton.addActionListener(new RemoveDomainButtonListener());
        this.purgeOptionPanel.purgeDBButton.addActionListener(new PurgeDBButtonListener());

        /**
         * Initializes the apply button and adds a listener to it. 
         */
        this.applyButton = new JButton("Apply");
        this.applyButton.addActionListener(new ApplyButtonListener());

        /**
         * Initializes the default button and adds a listener to it. 
         */
        this.defaultButton = new JButton("Default");
        this.defaultButton.addActionListener(new DefaultButtonListener());

        /**
         * Initializes the close button and adds a listener to it. 
         */
        this.closeButton = new JButton("Close");
        this.closeButton.addActionListener(new CloseButtonListener());

        /**
         * Placing all the components in their place.
         */
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;
        this.add(this.regexOptionPanel, gbc);
        gbc.insets = new Insets(0, 5, 5, 5);
        gbc.gridy = 1;
        gbc.weighty = 1;
        this.add(this.blackListOptionPanel, gbc);
        gbc.gridy = 2;
        gbc.weighty = 0;
        this.add(this.purgeOptionPanel, gbc);
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        this.add(this.applyButton, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 5, 5);
        this.add(this.defaultButton, gbc);
        gbc.gridx = 2;
        this.add(this.closeButton, gbc);
        refreshRegexList();
        refreshBlackList();

        /**
         * Setting the dialog to be visible.
         */
        this.setVisible(true);

    }
/**
     * This is call function to use in an action listener that calls up the 
     * purge warning popup.
     *
     * @param event an action event generated by the listener
     */
    public void purgeWarningCall(ActionEvent event) {

        PurgeWarningPopup pwp = new PurgeWarningPopup(this.papa, true);

    }

    /**
     * This is a basic closing function to use in a listener.
     *
     * @param event an action event generated by the listener
     */
    public void closeSettingsWindow(ActionEvent event) {

        this.dispose();

    }

    /**
     * This function updates the blacklist display.
     */
    public void refreshBlackList() {
        this.blackListOptionPanel.blackListModel.clear();
        try {
            for (String domain : this.papa.settings.getBlackListManager().getBlackListedDomain()) {
                this.blackListOptionPanel.blackListModel.addElement(domain);
            }
        } catch (ExceptionRecuperation ex) {
            this.papa.settings.clearDatabase();
            try {
                this.papa.settings = new SettingManager();
            } catch (ExceptionRecuperation ex1) {
                JOptionPane.showMessageDialog(null, "Impossible to create database : " + ex, "Error",
                        JOptionPane.ERROR_MESSAGE);
                this.dispose();
                this.papa.dispose();
            }
        }

    }

    /**
     * This function updates the regex list.
     */
    public void refreshRegexList() {
        this.regexOptionPanel.regexSelector.removeAllItems();
        try {
            for (String domain : this.papa.settings.getRegexManager().getRegexList()) {
                this.regexOptionPanel.regexSelector.addItem(domain);
            }
        } catch (ExceptionRecuperation ex) {
            this.papa.settings.clearDatabase();
            try {
                this.papa.settings = new SettingManager();
            } catch (ExceptionRecuperation ex1) {
                JOptionPane.showMessageDialog(null, "Impossible to create database : " + ex, "Error",
                        JOptionPane.ERROR_MESSAGE);
                this.dispose();
                this.papa.dispose();
            }
        }

    }

    /**
     * This is a fonction that adds a domain to the blacklist.
     */
    public void addDomainToBlackList() {
        try {
            this.papa.settings.getBlackListManager().addBlackListedDomain(this.blackListOptionPanel.addDomainField.getText());
            this.blackListOptionPanel.addDomainField.setText("");
            refreshBlackList();
        } catch (ExceptionRecuperation ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This is a fonction that removes a selected domain from the blacklist.
     */
    public void removeDomainOnBlackList() {
        try {
            this.papa.settings.getBlackListManager().deleteBlackListedDomain(
                    this.blackListOptionPanel.blackList.getSelectedValue().toString());
            refreshBlackList();
        } catch (ExceptionRecuperation ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This function reestablish the default regex setting.
     */
    public void getDefaultRegex() {
        boolean temp = false;
        for (int i = 0; i < this.regexOptionPanel.regexSelector.getItemCount(); i++) {
            if (this.regexOptionPanel.regexSelector.getItemAt(i).toString().equals("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})")) {
                this.regexOptionPanel.regexSelector.setSelectedIndex(i);
                temp = true;
            }
        }
        if(!temp){
            regexOptionPanel.regexSelector.addItem("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})");
            try {
                this.papa.settings.getRegexManager().addRegex("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})");
            } catch (ExceptionRecuperation ex) {
            }
        }
    }

    /**
     * This applies the current settings, and if the regex is incorrect sets the default.
     */
    public void applyCurrentSettings() {
        try {
            String str = this.regexOptionPanel.regexSelector.getSelectedItem().toString();
            if (str != null) {
                if (this.papa.settings.setRegex(this.regexOptionPanel.regexSelector.getSelectedItem().toString())) {
                    try {
                        this.papa.settings.getRegexManager().addRegex(this.regexOptionPanel.regexSelector.getSelectedItem().toString());
                        refreshRegexList();
                        this.regexOptionPanel.regexSelector.setSelectedItem(str);
                    } catch (ExceptionRecuperation ex) {
                        getDefaultRegex();
                        JOptionPane.showMessageDialog(null, "Error : " + ex.toString(), "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    getDefaultRegex();
                    JOptionPane.showMessageDialog(null, "The regex expression is invalid", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * This sets the default settings.
     */
    public void setDefaultSettings() {
        this.papa.settings.setRegex("");
        getDefaultRegex();
    }

    /**
     * Listener for the close button, closes the dialog.
     */
    class CloseButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            closeSettingsWindow(e);

        }
    }

    /**
     * Listener for the purge button, calls the purge warning popup.
     */
    class PurgeDBButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            purgeWarningCall(e);

        }
    }

    /**
     * Listener for the add domain button, adds domain to the blacklist.
     */
    class AddDomainButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            addDomainToBlackList();

        }
    }

    /**
     * Listener for the remove domain button, removes domain from the blacklist.
     */
    class RemoveDomainButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            removeDomainOnBlackList();

        }
    }

    /**
     * Listener for the apply button, applies current settings.
     */
    class ApplyButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            applyCurrentSettings();

        }
    }

    /**
     * Listener for the default button, restore default settings.
     */
    class DefaultButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setDefaultSettings();
        }
    }
}
