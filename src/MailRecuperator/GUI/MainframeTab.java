package MailRecuperator.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JPanel;


/**
 * The class {@code MainframeTab} is an extension of the JPanel and is used as
 * a container for all the panels composing the mainframe tab.
 */
public class MainframeTab extends JPanel {
    
    /**
     * The keyword database panel, deals with the display of the keyword list
     * and the removal of a keyword from said list.
     */
    KeywordDatabasePanel keywordDBPanel;
    /**
     * The add keyword panel, deals with the adding of a keyword to the 
     * keyword list.
     */
    AddKeywordPanel addKeywordPanel;
    /**
     * The tools panel, deals with the two secondary frames calling functions.
     */
    ToolsPanel toolsPanel;
    /**
     * The system panel, deals with the calling of the closing warning popup.
     */
    QuitPanel systemPanel;
    
    /**
     * Class constructor.
     */
    public MainframeTab(){
    
        /**
         * Each panel is initialized and given a titled border.
         */
        this.keywordDBPanel = new KeywordDatabasePanel();
        this.keywordDBPanel.setBorder(BorderFactory
                .createTitledBorder("Keyword Database"));
        this.addKeywordPanel = new AddKeywordPanel();
        this.addKeywordPanel.setBorder(BorderFactory
                .createTitledBorder("Add Keyword"));
        this.toolsPanel = new ToolsPanel();
        this.toolsPanel.setBorder(BorderFactory.createTitledBorder("Tools"));
        this.systemPanel = new QuitPanel();
        this.systemPanel.setBorder(BorderFactory.createTitledBorder("System"));
        
        /**
         * Placing all the components in their place.
         */
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        this.add(this.keywordDBPanel, gbc);
        gbc.weighty = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0,5,5,5);
        this.add(this.addKeywordPanel, gbc);
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.weightx = 0;
        gbc.insets = new Insets(0,0,5,5);
        this.add(this.toolsPanel, gbc);
        gbc.gridx = GridBagConstraints.RELATIVE;
        this.add(this.systemPanel, gbc); 
        
    }
    
}
